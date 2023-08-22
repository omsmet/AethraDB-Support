from pathlib import Path
import duckdb
import pyarrow as pa
import pyarrow.dataset as ds
import pandas

# The dataset path
dataset_path = Path("/nvtmp/duckdb/arrow_tpch")

# For the dataset, execute the query using duckdb and store the result if
# it does not exist yet
dataset_file = dataset_path / 'lineitem_doubles.arrow'
result_file = dataset_path / 'expected_result.csv'

if (result_file.exists()):
    exit()

con = duckdb.connect()
lineitem_doubles = ds.dataset(dataset_file, format='arrow')
result_df = con.sql("SELECT l_extendedprice * (1 - l_discount) * (1 + l_tax) FROM lineitem_doubles").df().astype(float)
result_df.to_csv(result_file, header=False, index=False)

