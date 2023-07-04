from pathlib import Path
import duckdb
import pyarrow as pa
import pyarrow.dataset as ds
import pandas

# The base directory for the datasets
datasets_path = Path("/nvtmp/AethraTestData/aggregation_query_int")

# Get the directories of the actual datasets
dataset_directories = [p for p in datasets_path.glob("*") if p.is_dir()]

# For each dataset, execute the query using duckdb and store the result if
# it does not exist yet
for dataset in dataset_directories:
    dataset_file = dataset / 'aggregation_query_table.arrow'
    result_file = dataset / 'expected_result.csv'

    if (result_file.exists()):
        continue

    con = duckdb.connect()
    aggregation_query_table = ds.dataset(dataset_file, format='arrow')
    result_df = con.sql("SELECT col1, SUM(col2), SUM(col3), SUM(col4) FROM aggregation_query_table GROUP BY col1").df().astype(int)
    result_df.to_csv(result_file, header=False, index=False)
