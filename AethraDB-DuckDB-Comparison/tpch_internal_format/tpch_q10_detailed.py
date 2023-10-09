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
scale_factor = "sf-1"
print("DuckDB Version " + duckdb.__version__ + " - PyArrow Version " + pa.__version__ + " - Single Threaded Mode")

# Execute the query using duckdb and compute the average query time over 10 runs
temp_profile_file = Path('duckdb_profile.json')

con = duckdb.connect(scale_factor + ".db")
con.sql("PRAGMA enable_profiling='json'")
con.sql("PRAGMA profile_output='" + str(temp_profile_file) + "'")
con.sql("SET threads TO 1;")

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
	                    c_custkey,
	                    c_name,
	                    sum(l_extendedprice * (1 - l_discount)) as revenue,
	                    c_acctbal,
	                    n_name,
	                    c_address,
	                    c_phone,
	                    c_comment
                    from
	                    tpch.nation,
	                    tpch.customer,
	                    tpch.orders,
	                    tpch.lineitem
                    where
	                    c_custkey = o_custkey
	                    and l_orderkey = o_orderkey
	                    and o_orderdate >= date '1993-10-01'
	                    and o_orderdate < date '1993-10-01' + interval 3 month
	                    and l_returnflag = 'R'
	                    and c_nationkey = n_nationkey
                    group by
	                    c_custkey,
	                    c_name,
	                    c_acctbal,
	                    c_phone,
	                    n_name,
	                    c_address,
	                    c_comment
                    -- order by
                    -- 	revenue desc;
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
print("TPC-H Q10, " + scale_factor + ", " + str(average_optimiser_time) + ", " + str(average_physical_planner_time) + ", " + str(average_planner_time) + ", " + str(average_exec_time) + ", " + str(average_total_time))
