from pathlib import Path
import duckdb
import pyarrow as pa
import pyarrow.dataset as ds
import json
from contextlib import redirect_stdout
import io

# The base directory for the datasets
datasets_path = Path("/nvtmp/AethraTestData/tpch/sf-1")
print("DuckDB Version " + duckdb.__version__ + " - PyArrow Version " + pa.__version__ + " - Single Threaded Mode")

# Execute the query using duckdb and compute the average query time over 10 runs
dataset_file = datasets_path / 'lineitem.arrow'
temp_profile_file = datasets_path / 'duckdb_profile.json'

con = duckdb.connect()
con.sql("PRAGMA enable_profiling='json'")
con.sql("PRAGMA profile_output='" + str(temp_profile_file) + "'")
con.sql("SET threads TO 1;")
lineitem = ds.dataset(dataset_file, format='arrow')

cumulative_query_time = 0

for i in range(10):
    # Execute the query
    with redirect_stdout(io.StringIO()) as f:
        con.sql("""
                    select
	                    sum(l_extendedprice * l_discount) as revenue
                    from
	                    lineitem
                    where
	                    l_shipdate >= date '1994-01-01'
	                    and l_shipdate < date '1994-01-01' + interval 1 year
	                    -- and l_discount between 0.06 - 0.01 and 0.06 + 0.01
	                    -- simplified:
	                    and l_discount between 0.05 and 0.07
	                    and l_quantity < 24
                """).show()

    # Read the total query time back
    with open(temp_profile_file) as temp_profile:
        profile_data = json.load(temp_profile)
        total_execution_time = profile_data["timing"]
        cumulative_query_time += total_execution_time

    # Delete the profile file for the next run
    temp_profile_file.unlink()

# Finally echo the time for this dataset
average_query_time = cumulative_query_time / 10
print(str(dataset_file) + ", " + str(average_query_time))
