from pathlib import Path
import duckdb
import pyarrow as pa
import pyarrow.dataset as ds
import json
from contextlib import redirect_stdout
import io

scale_factor = "sf-1"

# The base directory for the datasets
datasets_path = Path("/nvtmp/AethraTestData/tpch/" + scale_factor)
print("DuckDB Version " + duckdb.__version__ + " - PyArrow Version " + pa.__version__ + " - Single Threaded Mode")

arrow_nation = ds.dataset(datasets_path / 'nation.arrow', format='arrow')
arrow_customer = ds.dataset(datasets_path / 'customer.arrow', format='arrow')
arrow_orders = ds.dataset(datasets_path / 'orders.arrow', format='arrow')
arrow_lineitem = ds.dataset(datasets_path / 'lineitem.arrow', format='arrow')

con = duckdb.connect(scale_factor + ".db")

con.sql("CREATE SCHEMA tpch;")
con.sql("CREATE TABLE tpch.nation AS SELECT * FROM arrow_nation")
con.sql("CREATE TABLE tpch.customer AS SELECT * FROM arrow_customer")
con.sql("CREATE TABLE tpch.orders AS SELECT * FROM arrow_orders")
con.sql("CREATE TABLE tpch.lineitem AS SELECT * FROM arrow_lineitem")