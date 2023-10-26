from pathlib import Path
import duckdb
import pyarrow as pa
import pyarrow.dataset as ds
import json
from contextlib import redirect_stdout
import io

# The base directory for the datasets
datasets_path = Path("/nvtmp/AethraTestData/tpch/sf-100")
print("DuckDB Version " + duckdb.__version__ + " - PyArrow Version " + pa.__version__ + " - Single Threaded Mode")

# Execute the query using duckdb
con = duckdb.connect()
con.sql("SET threads TO 1;")

lineitem = ds.dataset(datasets_path / 'lineitem.arrow', format='arrow')
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
