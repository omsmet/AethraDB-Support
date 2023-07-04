from pathlib import Path
import duckdb
import pyarrow as pa
import pyarrow.dataset as ds
import json
from contextlib import redirect_stdout
import io

# The base directory for the datasets
datasets_path = Path("/nvtmp/AethraTestData/join_query_int")

# Get the directories of the actual datasets
dataset_directories = [p for p in datasets_path.glob("*") if p.is_dir()]

print("DuckDB Version " + duckdb.__version__ + " - PyArrow Version " + pa.__version__ + " - Single Threaded Mode")

# For each dataset, execute the query using duckdb and compute the average query time over 10 runs
for dataset in dataset_directories:
    table_A_file = dataset / 'table_A.arrow'
    table_B_file = dataset / 'table_B.arrow'
    table_C_file = dataset / 'table_C.arrow'
    temp_profile_file = dataset / 'duckdb_profile.json'

    con = duckdb.connect()
    con.sql("PRAGMA enable_profiling='json'")
    con.sql("PRAGMA profile_output='" + str(temp_profile_file) + "'")
    con.sql("SET threads TO 1;")
    
    A = ds.dataset(table_A_file, format='arrow')
    B = ds.dataset(table_B_file, format='arrow')
    C = ds.dataset(table_C_file, format='arrow')
    
    cumulative_query_time = 0

    for i in range(10):
        # Execute the query
        with redirect_stdout(io.StringIO()) as f:
            con.sql("SELECT COUNT(*) FROM A INNER JOIN B ON A.col1 = B.col1 INNER JOIN C ON A.col2 = C.col1").show()

        # Read the total query time back
        with open(temp_profile_file) as temp_profile:
            profile_data = json.load(temp_profile)
            total_execution_time = profile_data["timing"]
            cumulative_query_time += total_execution_time

        # Delete the profile file for the next run
        temp_profile_file.unlink()
    
    # Finally echo the time for this dataset
    average_query_time = cumulative_query_time / 10
    print(str(dataset) + ", " + str(average_query_time))
