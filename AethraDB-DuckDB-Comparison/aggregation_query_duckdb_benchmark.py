from pathlib import Path
import duckdb
import pyarrow as pa
import pyarrow.dataset as ds
import pandas
import json
from contextlib import redirect_stdout
import io

# The base directory for the datasets
datasets_path = Path("/nvtmp/AethraTestData/aggregation_query_int")

# Get the directories of the actual datasets
dataset_directories = [p for p in datasets_path.glob("*") if p.is_dir()]

print("DuckDB Version " + duckdb.__version__ + " - PyArrow Version " + pa.__version__ + " - Single Threaded Mode")

# For each dataset, execute the query using duckdb and compute the average query time over 10 runs
for dataset in dataset_directories:
    dataset_file = dataset / 'aggregation_query_table.arrow'
    temp_profile_file = dataset / 'duckdb_profile.json'

    con = duckdb.connect()
    con.sql("PRAGMA enable_profiling='json'")
    con.sql("PRAGMA profile_output='" + str(temp_profile_file) + "'")
    con.sql("SET threads TO 1;")
    aggregation_query_table = ds.dataset(dataset_file, format='arrow')
    
    cumulative_query_time = 0

    for i in range(10):
        # Execute the query
        with redirect_stdout(io.StringIO()) as f:
            con.sql("SELECT col1, SUM(col2), SUM(col3), SUM(col4) FROM aggregation_query_table GROUP BY col1").show()

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
