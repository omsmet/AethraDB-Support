from pathlib import Path
import duckdb
import pyarrow as pa
import pyarrow.dataset as ds
import json
from contextlib import redirect_stdout
import io

# The dataset path
dataset_path = Path("/nvtmp/duckdb/arrow_tpch")

print("DuckDB Version " + duckdb.__version__ + " - PyArrow Version " + pa.__version__ + " - Single Threaded Mode")

# For the dataset, execute the query using duckdb and compute the average query time over 10 runs
dataset_file = dataset_path / 'lineitem_doubles.arrow'
temp_profile_file = dataset_path / 'duckdb_profile.json'

con = duckdb.connect()
con.sql("PRAGMA enable_profiling='json'")
con.sql("PRAGMA profile_output='" + str(temp_profile_file) + "'")
con.sql("SET threads TO 1;")
lineitem_doubles = ds.dataset(dataset_file, format='arrow')
    
cumulative_query_time = 0

for i in range(10):
    # Execute the query
    with redirect_stdout(io.StringIO()) as f:
        con.sql("SELECT l_extendedprice * (1 - l_discount) * (1 + l_tax) FROM lineitem_doubles").show()

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
