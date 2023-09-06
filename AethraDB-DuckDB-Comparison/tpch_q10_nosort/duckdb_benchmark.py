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
temp_profile_file = datasets_path / 'duckdb_profile.json'

con = duckdb.connect()
con.sql("PRAGMA enable_profiling='json'")
con.sql("PRAGMA profile_output='" + str(temp_profile_file) + "'")
con.sql("SET threads TO 1;")

nation = ds.dataset(datasets_path / 'nation.arrow', format='arrow')
customer = ds.dataset(datasets_path / 'customer.arrow', format='arrow')
orders = ds.dataset(datasets_path / 'orders.arrow', format='arrow')
lineitem = ds.dataset(datasets_path / 'lineitem.arrow', format='arrow')

cumulative_query_time = 0

for i in range(10):
    # Execute the query
    with redirect_stdout(io.StringIO()) as f:
        con.sql("""
                    select
	                    c_custkey,
	                    c_name,
	                    sum(l_extendedprice * (1 - l_discount)) as revenue,
	                    c_acctbal,
	                    n_name,
	                    c_address,
	                    c_phone,
	                    c_comment
                    from
	                    nation,
	                    customer,
	                    orders,
	                    lineitem
                    where
	                    c_custkey = o_custkey
	                    and l_orderkey = o_orderkey
	                    and o_orderdate >= date '1993-10-01'
	                    and o_orderdate < date '1993-10-01' + interval 3 month
	                    and l_returnflag = 'R'
	                    and c_nationkey = n_nationkey
                    group by
	                    c_custkey,
	                    c_name,
	                    c_acctbal,
	                    c_phone,
	                    n_name,
	                    c_address,
	                    c_comment
                    -- order by
                    -- 	revenue desc;
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
print(str(datasets_path) + ", " + str(average_query_time))
