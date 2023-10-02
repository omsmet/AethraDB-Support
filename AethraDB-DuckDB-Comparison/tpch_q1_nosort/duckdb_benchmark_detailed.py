from pathlib import Path
import duckdb
import pyarrow as pa
import pyarrow.dataset as ds
import json
from contextlib import redirect_stdout
import io

# Helper function for later on
def computeCumulativeChildrenTime(start_element):
    total_time = start_element["timing"] * 1000
    
    for child_element in start_element["children"]:
        total_time += computeCumulativeChildrenTime(child_element)
    
    return total_time

# The base directory for the datasets
datasets_path = Path("/nvtmp/AethraTestData/tpch/sf-1")
print("DuckDB Version " + duckdb.__version__ + " - PyArrow Version " + pa.__version__ + " - Single Threaded Mode")

# Execute the query using duckdb and compute the average query time over 10 runs
temp_profile_file = datasets_path / 'duckdb_profile.json'

con = duckdb.connect()
con.sql("PRAGMA enable_profiling='json'")
con.sql("PRAGMA profile_output='" + str(temp_profile_file) + "'")
con.sql("SET threads TO 1;")

lineitem = ds.dataset(datasets_path / 'lineitem.arrow', format='arrow')

cumulative_optimiser_time = 0
cumulative_physical_planner_time = 0
cumulative_planner_time = 0
cumulative_exec_time = 0
cumulative_total_time = 0

number_of_iterations_to_average_over = 10

for i in range(number_of_iterations_to_average_over):
    # Execute the query
    with redirect_stdout(io.StringIO()) as f:
        con.sql("""
                    select
                        l_returnflag,
                        l_linestatus,
                        sum(l_quantity) as sum_qty,
                        sum(l_extendedprice) as sum_base_price,
                        sum(l_extendedprice * (1 - l_discount)) as sum_disc_price,
                        sum(l_extendedprice * (1 - l_discount) * (1 + l_tax)) as sum_charge,
                        avg(l_quantity) as avg_qty,
                        avg(l_extendedprice) as avg_price,
                        avg(l_discount) as avg_disc,
                        count(*) as count_order
                    from
                        lineitem
                    where
                        l_shipdate <= date '1998-12-01' - interval 90 day
                    group by
                        l_returnflag,
                        l_linestatus
                    -- order by
                    --    l_returnflag,
                    --    l_linestatus;
                """).show()

    # Read the total query time back
    with open(temp_profile_file) as temp_profile:
        profile_data = json.load(temp_profile)
        
        # Extract "overhead" data
        overhead_related_data = profile_data["timings"]
        for overhead_timing_element in overhead_related_data:
            if "optimizer" in overhead_timing_element["annotation"]:
                cumulative_optimiser_time += overhead_timing_element["timing"] * 1000
            elif "physical_planner" in overhead_timing_element["annotation"]:
                cumulative_physical_planner_time += overhead_timing_element["timing"] * 1000
            elif "planner" in overhead_timing_element["annotation"]:
                cumulative_planner_time += overhead_timing_element["timing"] * 1000
        
        # Extract execution time data
        cumulative_exec_time += computeCumulativeChildrenTime(profile_data["children"][0])
        
        # Extract total time data
        cumulative_total_time += profile_data["timing"] * 1000

    # Delete the profile file for the next run
    temp_profile_file.unlink()

# Finally echo the time for this dataset
average_optimiser_time = round(cumulative_optimiser_time / number_of_iterations_to_average_over, 1)
average_physical_planner_time = round(cumulative_physical_planner_time / number_of_iterations_to_average_over, 1)
average_planner_time = round(cumulative_planner_time / number_of_iterations_to_average_over, 1)
average_exec_time = round(cumulative_exec_time / number_of_iterations_to_average_over, 1)
average_total_time = round(cumulative_total_time / number_of_iterations_to_average_over, 1)

print("Query,Dataset,Optimiser Time,Physical Planner Time,Planner Time,Execuction Time,Total Time")
print("TPC-H Q1, " + str(datasets_path) + ", " + str(average_optimiser_time) + ", " + str(average_physical_planner_time) + ", " + str(average_planner_time) + ", " + str(average_exec_time) + ", " + str(average_total_time))
