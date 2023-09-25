import pyarrow as pa
from pyspark.sql import SparkSession
from pyspark.sql.types import StructType, IntegerType, DoubleType, DateType, StringType
import time

# Actual code
sparkSession = SparkSession.builder.appName("SparkArrowCodeGenXperiments")\
                                   .master("local[1]")\
                                   .config("spark.sql.execution.arrow.pyspark.enabled", "true")\
                                   .config("spark.driver.memory", "32g")\
                                   .config("spark.executor.memory", "32g")\
                                   .getOrCreate()
sparkSession.sparkContext.setLogLevel("ERROR")

lineitem_table_bsv_file = '/nvtmp/TPC-H V3.0.1/dbgen/lineitem.tbl'
lineitem_table_bsv_schema = StructType()\
    .add("l_orderkey", IntegerType(), False)\
    .add("l_partkey", IntegerType(), False)\
    .add("l_suppkey", IntegerType(), False)\
    .add("l_linenumber", IntegerType(), False)\
    .add("l_quantity", DoubleType(), False)\
    .add("l_extendedprice", DoubleType(), False)\
    .add("l_discount", DoubleType(), False)\
    .add("l_tax", DoubleType(), False)\
    .add("l_returnflag", StringType(), False)\
    .add("l_linestatus", StringType(), False)\
    .add("l_shipdate", DateType(), False)\
    .add("l_commitdate", DateType(), False)\
    .add("l_receiptdate", DateType(), False)\
    .add("l_shipinstruct", StringType(), False)\
    .add("l_shipmode", StringType(), False)\
    .add("l_comment", StringType(), False)

lineitem_table_df = sparkSession.read.options(delimiter='|', header=False).schema(lineitem_table_bsv_schema).csv(lineitem_table_bsv_file)
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
run_time = (end_time - start_time)
print(">>>>> Query executed for " + str(run_time) + "s")

sparkSession.stop()
