import pyarrow as pa
from pyspark.sql import SparkSession
import time

# Support method (https://stackoverflow.com/questions/53569580/how-to-read-feather-arrow-file-natively)
def read_arrow_ipc(spark, filename, schema=None):

    def mapper(iterator):
        with pa.memory_map(filename, "rb") as source:
            f = pa.ipc.open_file(source)
            for batch in iterator:
                for i in batch['id']:
                    yield f.get_batch(i.as_py())

    tmp_reader = pa.ipc.open_file(filename)
    num_batches = tmp_reader.num_record_batches
    if schema is None:
        # read first batch and convert just one row to pandas
        tmp_row = tmp_reader.get_batch(0)[:1]
        schema = spark.createDataFrame(tmp_row.to_pandas()).schema
    return spark.range(num_batches).mapInArrow(mapper, schema)

# Actual code
sparkSession = SparkSession.builder.appName("SparkArrowCodeGenXperiments")\
                                   .master("local[1]")\
                                   .config("spark.sql.execution.arrow.pyspark.enabled", "true")\
                                   .getOrCreate()
sparkSession.sparkContext.setLogLevel("ERROR")

lineitem_table_file = '/nvtmp/AethraTestData/tpch/sf-100-no-flbin/lineitem.arrow'
lineitem_table_df = read_arrow_ipc(sparkSession, lineitem_table_file)
lineitem_table_df.createOrReplaceTempView("lineitem")

q1_result = sparkSession.sql("""
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
                                    -- 	l_returnflag,
                                    -- 	l_linestatus""")

print(">>>>> Starting Execution\n")
start_time = time.time()
q1_result.show()
end_time = time.time()
print(">>>>> Done Executing")
run_time = (end_time - start_time) * 1000
print(">>>>> Query executed for " + str(run_time) + "ms")

sparkSession.stop()
