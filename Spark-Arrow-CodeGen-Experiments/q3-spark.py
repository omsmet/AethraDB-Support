import pyarrow as pa
from pyspark.sql import SparkSession

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
sparkSession.sparkContext.setLogLevel("DEBUG")

customer_table_file = '/nvtmp/AethraTestData/tpch/sf-1-no-flbin/customer.arrow'
orders_table_file = '/nvtmp/AethraTestData/tpch/sf-1-no-flbin/orders.arrow'
lineitem_table_file = '/nvtmp/AethraTestData/tpch/sf-1-no-flbin/lineitem.arrow'

customer_table_df = read_arrow_ipc(sparkSession, customer_table_file)
customer_table_df.createOrReplaceTempView("customer")

orders_table_df = read_arrow_ipc(sparkSession, orders_table_file)
orders_table_df.createOrReplaceTempView("orders")

lineitem_table_df = read_arrow_ipc(sparkSession, lineitem_table_file)
lineitem_table_df.createOrReplaceTempView("lineitem")

q1_result = sparkSession.sql("""
                                    select
                                        l_orderkey,
                                        sum(l_extendedprice * (1 - l_discount)) as revenue,
                                        o_orderdate,
                                        o_shippriority
                                    from
                                        customer,
                                        orders,
                                        lineitem
                                    where
                                        c_mktsegment = 'BUILDING'
                                        and c_custkey = o_custkey
                                        and l_orderkey = o_orderkey
                                        and o_orderdate < date '1995-03-15'
                                        and l_shipdate > date '1995-03-15'
                                    group by
                                        l_orderkey,
                                        o_orderdate,
                                        o_shippriority
                                    -- order by
                                    -- 	revenue desc,
                                    -- 	o_orderdate""")

q1_result.show()
q1_result.explain("codegen")

sparkSession.stop()