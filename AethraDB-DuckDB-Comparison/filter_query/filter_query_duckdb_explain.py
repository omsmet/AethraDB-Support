from pathlib import Path
import duckdb
import pyarrow as pa
import pyarrow.dataset as ds
import json
from contextlib import redirect_stdout
import io

# The base directory for the datasets
dataset_path = Path("/nvtmp/AethraTestData/filter_query_int")

print("DuckDB Version " + duckdb.__version__ + " - PyArrow Version " + pa.__version__ + " - Single Threaded Mode")

dataset_file = dataset_path / 'arrow_col1_002_col2_098_col3_098' / 'filter_query_table.arrow'

con = duckdb.connect()
con.sql("PRAGMA enable_profiling")
con.sql("SET threads TO 1;")
filter_query_table = ds.dataset(dataset_file, format='arrow')

# Explain the query plan
con.sql("SELECT COUNT(*) FROM filter_query_table WHERE col1 < 3000 AND col2 < 3000 AND col3 < 3000").show()
