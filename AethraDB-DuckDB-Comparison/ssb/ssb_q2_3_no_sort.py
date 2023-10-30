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
scale_factor = 1
datasets_path = Path("/nvtmp/AethraTestData/SSB/sf-" + str(scale_factor))
print("DuckDB Version " + duckdb.__version__ + " - PyArrow Version " + pa.__version__ + " - Single Threaded Mode")

# Execute the query using duckdb and compute the average query time over 10 runs
temp_profile_file = datasets_path / 'duckdb_profile.json'

con = duckdb.connect()
con.sql("PRAGMA enable_profiling='json'")
con.sql("PRAGMA profile_output='" + str(temp_profile_file) + "'")
con.sql("SET threads TO 1;")

datedef = ds.dataset(datasets_path / 'datedef.arrow', format='arrow')
lineorder = ds.dataset(datasets_path / 'lineorder.arrow', format='arrow')
part = ds.dataset(datasets_path / 'part.arrow', format='arrow')
supplier = ds.dataset(datasets_path / 'supplier.arrow', format='arrow')

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
                    select sum(lo_revenue), d_year, p_brand1
                    from part, lineorder, supplier, datedef
                    where p_brand1 = 'MFGR#2221'
                    and p_partkey = lo_partkey
                    and s_region = 'EUROPE      '
                    and lo_suppkey = s_suppkey
                    and lo_orderdate = d_datekey
                    group by d_year, p_brand1
                    -- order by d_year, p_brand1;
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

print("Query,Scale Factor,Optimiser Time,Physical Planner Time,Planner Time,Execuction Time,Total Time")
print("SSB Q2_3, " + str(scale_factor) + ", " + str(average_optimiser_time) + ", " + str(average_physical_planner_time) + ", " + str(average_planner_time) + ", " + str(average_exec_time) + ", " + str(average_total_time))
