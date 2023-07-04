from pathlib import Path
import duckdb
import pyarrow as pa
import pyarrow.dataset as ds
import pandas

# The base directory for the datasets
datasets_path = Path("/nvtmp/AethraTestData/join_query_int")

# Get the directories of the actual datasets
dataset_directories = [p for p in datasets_path.glob("*") if p.is_dir()]

# For each dataset, execute the query using duckdb and store the result if it does not exist yet
for dataset in dataset_directories:
    table_A_file = dataset / 'table_A.arrow'
    table_B_file = dataset / 'table_B.arrow'
    table_C_file = dataset / 'table_C.arrow'
    result_file = dataset / 'expected_count.csv'

    if (result_file.exists()):
        continue

    con = duckdb.connect()
    A = ds.dataset(table_A_file, format='arrow')
    B = ds.dataset(table_B_file, format='arrow')
    C = ds.dataset(table_C_file, format='arrow')
    result_df = con.sql("SELECT COUNT(*) FROM A INNER JOIN B ON A.col1 = B.col1 INNER JOIN C ON A.col2 = C.col1").df().astype(int)
    result_df.to_csv(result_file, header=False, index=False)
