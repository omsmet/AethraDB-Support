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

lineitem_table_file = '/nvtmp/AethraTestData/tpch/sf-1-no-flbin/lineitem.arrow'

lineitem_table_df = read_arrow_ipc(sparkSession, lineitem_table_file)
lineitem_table_df.createOrReplaceTempView("lineitem")

q1_result = sparkSession.sql("""
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
                                        and l_quantity < 24""")

q1_result.show()
q1_result.explain("codegen")

sparkSession.stop()