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
dataset_file = datasets_path / 'lineitem.arrow'
temp_profile_file = datasets_path / 'duckdb_profile.json'

con = duckdb.connect()
con.sql("PRAGMA enable_profiling='json'")
con.sql("PRAGMA profile_output='" + str(temp_profile_file) + "'")
con.sql("SET threads TO 1;")
lineitem = ds.dataset(dataset_file, format='arrow')

cumulative_query_time = 0

for i in range(10):
    # Execute the query
    with redirect_stdout(io.StringIO()) as f:
        con.sql("""
                    select
                        l_returnflag,
                        l_linestatus,
                        sum(l_quantity) as sum_qty,
                        sum(l_extendedprice) as sum_base_price,
                        sum(l_extendedprice * (1 - l_discount)) as sum_disc_price,
                        sum(l_extendedprice * (1 - l_discount) * (1 + l_tax)) as sum_charge,
                        avg(l_quantity) as avg_qty,
                        avg(l_extendedprice) as avg_price,
                        avg(l_discount) as avg_disc,
                        count(*) as count_order
                    from
                        lineitem
                    where
                        l_shipdate <= date '1998-12-01' - interval 90 day
                    group by
                        l_returnflag,
                        l_linestatus
                    -- order by
                    --    l_returnflag,
                    --    l_linestatus;
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
print(str(dataset_file) + ", " + str(average_query_time))
