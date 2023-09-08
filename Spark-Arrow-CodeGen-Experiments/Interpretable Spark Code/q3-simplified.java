//// This class basically filters the customer table on c_mktsegment = 'BUILDING' and projects out c_custkey
final class GeneratedIteratorForCodegenStage2 extends org.apache.spark.sql.execution.BufferedRowIterator {
    private Object[] references;
    private scala.collection.Iterator[] inputs;
    private scala.collection.Iterator inputadapter_input_0;
    private org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter[] filter_mutableStateArray_0 = new org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter[2];

    public GeneratedIteratorForCodegenStage2(Object[] references) {
        this.references = references;
    }

    public void init(int index, scala.collection.Iterator[] inputs) {
        partitionIndex = index;
        this.inputs = inputs;
        inputadapter_input_0 = inputs[0];
        filter_mutableStateArray_0[0] = new org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter(8, 160);
        filter_mutableStateArray_0[1] = new org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter(1, 0);

    }

    protected void processNext() throws java.io.IOException {
        while (inputadapter_input_0.hasNext()) {
            InternalRow inputadapter_row_0 = (InternalRow) inputadapter_input_0.next();

            do {
                //// c_mktsegment = 'BUILDING'
                UTF8String inputadapter_value_6 = inputadapter_row_0.getUTF8String(6);
                boolean filter_value_3 = inputadapter_value_6.equals(((UTF8String) references[1] /* literal */));
                if (!filter_value_3)
                    continue;

                //// c_custkey
                int inputadapter_value_0 = inputadapter_row_0.getInt(0);

                // common sub-expressions

                filter_mutableStateArray_0[1].reset();

                if (false) {
                    filter_mutableStateArray_0[1].setNullAt(0);
                } else {
                    filter_mutableStateArray_0[1].write(0, inputadapter_value_0);
                }
                //// basically project out c_custkey
                append((filter_mutableStateArray_0[1].getRow()));

            } while (false);
            if (shouldStop())
                return;
        }
    }

}

//// This class filters lineitem table on l_shipdate > date '1995-03-15' and
//// projects out l_orderkey, l_extendedprice and l_discount
final class GeneratedIteratorForCodegenStage4 extends org.apache.spark.sql.execution.BufferedRowIterator {
    private Object[] references;
    private scala.collection.Iterator[] inputs;
    private scala.collection.Iterator inputadapter_input_0;
    private org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter[] filter_mutableStateArray_0 = new org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter[2];

    public GeneratedIteratorForCodegenStage4(Object[] references) {
        this.references = references;
    }

    public void init(int index, scala.collection.Iterator[] inputs) {
        partitionIndex = index;
        this.inputs = inputs;
        inputadapter_input_0 = inputs[0];
        filter_mutableStateArray_0[0] = new org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter(16,
                160);
        filter_mutableStateArray_0[1] = new org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter(3, 0);

    }

    protected void processNext() throws java.io.IOException {
        while (inputadapter_input_0.hasNext()) {
            InternalRow inputadapter_row_0 = (InternalRow) inputadapter_input_0.next();

            do {
                //// l_shipdate > date '1995-03-15'
                int inputadapter_value_10 = inputadapter_row_0.getInt(10);
                boolean filter_value_3 = inputadapter_value_10 > 9204;
                if (!filter_value_3)
                    continue;

                //// l_orderkey
                int inputadapter_value_0 = inputadapter_row_0.getInt(0);
                //// l_extendedprice
                double inputadapter_value_5 = inputadapter_row_0.getDouble(5);
                //// l_discount
                double inputadapter_value_6 = inputadapter_row_0.getDouble(6);

                //// simplified code
                filter_mutableStateArray_0[1].reset();
                filter_mutableStateArray_0[1].zeroOutNullBytes();
                filter_mutableStateArray_0[1].write(0, inputadapter_value_0);
                filter_mutableStateArray_0[1].write(1, inputadapter_value_5);
                filter_mutableStateArray_0[1].write(2, inputadapter_value_6);
                append((filter_mutableStateArray_0[1].getRow()));

            } while (false);
            if (shouldStop())
                return;
        }
    }

}

//// This code is the aggregation map for l_orderkey, o_orderdate, o_shippriority -> revenue
final class GeneratedIteratorForCodegenStage6 extends org.apache.spark.sql.execution.BufferedRowIterator {
    private Object[] references;
    private scala.collection.Iterator[] inputs;
    private boolean hashAgg_initAgg_0;
    private org.apache.spark.unsafe.KVIterator hashAgg_mapIter_0;
    private org.apache.spark.sql.execution.UnsafeFixedWidthAggregationMap hashAgg_hashMap_0;
    private org.apache.spark.sql.execution.UnsafeKVExternalSorter hashAgg_sorter_0;
    private boolean hashAgg_initAgg_1;
    private boolean hashAgg_bufIsNull_0;
    private double hashAgg_bufValue_0;
    private hashAgg_FastHashMap_0 hashAgg_fastHashMap_0;
    private org.apache.spark.unsafe.KVIterator<UnsafeRow, UnsafeRow> hashAgg_fastHashMapIter_0;
    private org.apache.spark.unsafe.KVIterator hashAgg_mapIter_1;
    private org.apache.spark.sql.execution.UnsafeFixedWidthAggregationMap hashAgg_hashMap_1;
    private org.apache.spark.sql.execution.UnsafeKVExternalSorter hashAgg_sorter_1;
    private scala.collection.Iterator inputadapter_input_0;
    private org.apache.spark.sql.execution.joins.LongHashedRelation bhj_relation_0;
    private org.apache.spark.sql.execution.joins.LongHashedRelation bhj_relation_1;
    private boolean hashAgg_hashAgg_isNull_12_0;
    private boolean hashAgg_hashAgg_isNull_14_0;
    private boolean hashAgg_hashAgg_isNull_45_0;
    private boolean hashAgg_hashAgg_isNull_47_0;
    private org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter[] filter_mutableStateArray_0 = new org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter[12];

    public GeneratedIteratorForCodegenStage6(Object[] references) {
        this.references = references;
    }

    public void init(int index, scala.collection.Iterator[] inputs) {
        partitionIndex = index;
        this.inputs = inputs;
        wholestagecodegen_init_0_0();
        wholestagecodegen_init_0_1();

    }

    public class hashAgg_FastHashMap_0 {
        private org.apache.spark.sql.catalyst.expressions.RowBasedKeyValueBatch batch;
        private int[] buckets;
        private int capacity = 1 << 16;
        private double loadFactor = 0.5;
        private int numBuckets = (int) (capacity / loadFactor);
        private int maxSteps = 2;
        private int numRows = 0;
        private Object emptyVBase;
        private long emptyVOff;
        private int emptyVLen;
        private boolean isBatchFull = false;
        private org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter agg_rowWriter;

        public hashAgg_FastHashMap_0(
                org.apache.spark.memory.TaskMemoryManager taskMemoryManager,
                InternalRow emptyAggregationBuffer) {
            batch = org.apache.spark.sql.catalyst.expressions.RowBasedKeyValueBatch
                    .allocate(((org.apache.spark.sql.types.StructType) references[6] /* keySchemaTerm */),
                            ((org.apache.spark.sql.types.StructType) references[7] /* valueSchemaTerm */),
                            taskMemoryManager, capacity);

            final UnsafeProjection valueProjection = UnsafeProjection
                    .create(((org.apache.spark.sql.types.StructType) references[7] /* valueSchemaTerm */));
            final byte[] emptyBuffer = valueProjection.apply(emptyAggregationBuffer).getBytes();

            emptyVBase = emptyBuffer;
            emptyVOff = Platform.BYTE_ARRAY_OFFSET;
            emptyVLen = emptyBuffer.length;

            agg_rowWriter = new org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter(
                    3, 0);

            buckets = new int[numBuckets];
            java.util.Arrays.fill(buckets, -1);
        }

        public org.apache.spark.sql.catalyst.expressions.UnsafeRow findOrInsert(int hashAgg_key_0, int hashAgg_key_1,
                int hashAgg_key_2) {
            long h = hash(hashAgg_key_0, hashAgg_key_1, hashAgg_key_2);
            int step = 0;
            int idx = (int) h & (numBuckets - 1);
            while (step < maxSteps) {
                // Return bucket index if it's either an empty slot or already contains the key
                if (buckets[idx] == -1) {
                    if (numRows < capacity && !isBatchFull) {
                        agg_rowWriter.reset();
                        agg_rowWriter.zeroOutNullBytes();
                        agg_rowWriter.write(0, hashAgg_key_0);
                        agg_rowWriter.write(1, hashAgg_key_1);
                        agg_rowWriter.write(2, hashAgg_key_2);
                        org.apache.spark.sql.catalyst.expressions.UnsafeRow agg_result = agg_rowWriter.getRow();
                        Object kbase = agg_result.getBaseObject();
                        long koff = agg_result.getBaseOffset();
                        int klen = agg_result.getSizeInBytes();

                        UnsafeRow vRow = batch.appendRow(kbase, koff, klen, emptyVBase, emptyVOff, emptyVLen);
                        if (vRow == null) {
                            isBatchFull = true;
                        } else {
                            buckets[idx] = numRows++;
                        }
                        return vRow;
                    } else {
                        // No more space
                        return null;
                    }
                } else if (equals(idx, hashAgg_key_0, hashAgg_key_1, hashAgg_key_2)) {
                    return batch.getValueRow(buckets[idx]);
                }
                idx = (idx + 1) & (numBuckets - 1);
                step++;
            }
            // Didn't find it
            return null;
        }

        private boolean equals(int idx, int hashAgg_key_0, int hashAgg_key_1, int hashAgg_key_2) {
            UnsafeRow row = batch.getKeyRow(buckets[idx]);
            return (row.getInt(0) == hashAgg_key_0) && (row.getInt(1) == hashAgg_key_1)
                    && (row.getInt(2) == hashAgg_key_2);
        }

        private long hash(int hashAgg_key_0, int hashAgg_key_1, int hashAgg_key_2) {
            long hashAgg_hash_0 = 0;

            int hashAgg_result_0 = hashAgg_key_0;
            hashAgg_hash_0 = (hashAgg_hash_0 ^ (0x9e3779b9)) + hashAgg_result_0 + (hashAgg_hash_0 << 6)
                    + (hashAgg_hash_0 >>> 2);

            int hashAgg_result_1 = hashAgg_key_1;
            hashAgg_hash_0 = (hashAgg_hash_0 ^ (0x9e3779b9)) + hashAgg_result_1 + (hashAgg_hash_0 << 6)
                    + (hashAgg_hash_0 >>> 2);

            int hashAgg_result_2 = hashAgg_key_2;
            hashAgg_hash_0 = (hashAgg_hash_0 ^ (0x9e3779b9)) + hashAgg_result_2 + (hashAgg_hash_0 << 6)
                    + (hashAgg_hash_0 >>> 2);

            return hashAgg_hash_0;
        }

        public org.apache.spark.unsafe.KVIterator<UnsafeRow, UnsafeRow> rowIterator() {
            return batch.rowIterator();
        }

        public void close() {
            batch.close();
        }

    }

    private void hashAgg_doConsume_1(int hashAgg_expr_0_1, boolean hashAgg_exprIsNull_0_1, int hashAgg_expr_1_1,
            boolean hashAgg_exprIsNull_1_1, int hashAgg_expr_2_1, boolean hashAgg_exprIsNull_2_1,
            double hashAgg_expr_3_1, boolean hashAgg_exprIsNull_3_1) throws java.io.IOException {
        UnsafeRow hashAgg_unsafeRowAggBuffer_1 = null;

        // generate grouping key
        filter_mutableStateArray_0[10].reset();

        filter_mutableStateArray_0[10].zeroOutNullBytes();

        if (hashAgg_exprIsNull_0_1) {
            filter_mutableStateArray_0[10].setNullAt(0);
        } else {
            filter_mutableStateArray_0[10].write(0, hashAgg_expr_0_1);
        }

        if (hashAgg_exprIsNull_1_1) {
            filter_mutableStateArray_0[10].setNullAt(1);
        } else {
            filter_mutableStateArray_0[10].write(1, hashAgg_expr_1_1);
        }

        if (hashAgg_exprIsNull_2_1) {
            filter_mutableStateArray_0[10].setNullAt(2);
        } else {
            filter_mutableStateArray_0[10].write(2, hashAgg_expr_2_1);
        }
        int hashAgg_unsafeRowKeyHash_1 = (filter_mutableStateArray_0[10].getRow()).hashCode();
        if (true) {
            // try to get the buffer from hash map
            hashAgg_unsafeRowAggBuffer_1 = hashAgg_hashMap_0.getAggregationBufferFromUnsafeRow(
                    (filter_mutableStateArray_0[10].getRow()), hashAgg_unsafeRowKeyHash_1);
        }
        // Can't allocate buffer from the hash map. Spill the map and fallback to
        // sort-based
        // aggregation after processing all input rows.
        if (hashAgg_unsafeRowAggBuffer_1 == null) {
            if (hashAgg_sorter_0 == null) {
                hashAgg_sorter_0 = hashAgg_hashMap_0.destructAndCreateExternalSorter();
            } else {
                hashAgg_sorter_0.merge(hashAgg_hashMap_0.destructAndCreateExternalSorter());
            }

            // the hash map had be spilled, it should have enough memory now,
            // try to allocate buffer again.
            hashAgg_unsafeRowAggBuffer_1 = hashAgg_hashMap_0.getAggregationBufferFromUnsafeRow(
                    (filter_mutableStateArray_0[10].getRow()), hashAgg_unsafeRowKeyHash_1);
            if (hashAgg_unsafeRowAggBuffer_1 == null) {
                // failed to allocate the first page
                throw new org.apache.spark.memory.SparkOutOfMemoryError("No enough memory for aggregation");
            }
        }

        // common sub-expressions

        // evaluate aggregate functions and update aggregation buffers
        hashAgg_doAggregate_sum_1(hashAgg_unsafeRowAggBuffer_1, hashAgg_exprIsNull_3_1, hashAgg_expr_3_1);

    }

    private void hashAgg_doAggregateWithKeys_0() throws java.io.IOException {
        if (!hashAgg_initAgg_1) {
            hashAgg_initAgg_1 = true;
            hashAgg_fastHashMap_0 = new hashAgg_FastHashMap_0(
                    ((org.apache.spark.sql.execution.aggregate.HashAggregateExec) references[5] /* plan */)
                            .getTaskContext().taskMemoryManager(),
                    ((org.apache.spark.sql.execution.aggregate.HashAggregateExec) references[5] /* plan */)
                            .getEmptyAggregationBuffer());

            ((org.apache.spark.sql.execution.aggregate.HashAggregateExec) references[5] /* plan */).getTaskContext()
                    .addTaskCompletionListener(
                            new org.apache.spark.util.TaskCompletionListener() {
                                @Override
                                public void onTaskCompletion(org.apache.spark.TaskContext context) {
                                    hashAgg_fastHashMap_0.close();
                                }
                            });

            hashAgg_hashMap_1 = ((org.apache.spark.sql.execution.aggregate.HashAggregateExec) references[5] /* plan */)
                    .createHashMap();
            long hashAgg_beforeAgg_0 = System.nanoTime();
            hashAgg_doAggregateWithKeys_1();
            ((org.apache.spark.sql.execution.metric.SQLMetric) references[18] /* aggTime */)
                    .add((System.nanoTime() - hashAgg_beforeAgg_0) / 1000000);
        }
        // output the result

        while (hashAgg_fastHashMapIter_0.next()) {
            UnsafeRow hashAgg_aggKey_0 = (UnsafeRow) hashAgg_fastHashMapIter_0.getKey();
            UnsafeRow hashAgg_aggBuffer_0 = (UnsafeRow) hashAgg_fastHashMapIter_0.getValue();
            hashAgg_doAggregateWithKeysOutput_0(hashAgg_aggKey_0, hashAgg_aggBuffer_0);

            if (shouldStop())
                return;
        }
        hashAgg_fastHashMap_0.close();

        while (hashAgg_mapIter_1.next()) {
            UnsafeRow hashAgg_aggKey_0 = (UnsafeRow) hashAgg_mapIter_1.getKey();
            UnsafeRow hashAgg_aggBuffer_0 = (UnsafeRow) hashAgg_mapIter_1.getValue();
            hashAgg_doAggregateWithKeysOutput_0(hashAgg_aggKey_0, hashAgg_aggBuffer_0);
            if (shouldStop())
                return;
        }
        hashAgg_mapIter_1.close();
        if (hashAgg_sorter_1 == null) {
            hashAgg_hashMap_1.free();
        }

        hashAgg_mapIter_0 = ((org.apache.spark.sql.execution.aggregate.HashAggregateExec) references[0] /* plan */)
                .finishAggregate(hashAgg_hashMap_0, hashAgg_sorter_0,
                        ((org.apache.spark.sql.execution.metric.SQLMetric) references[1] /* peakMemory */),
                        ((org.apache.spark.sql.execution.metric.SQLMetric) references[2] /* spillSize */),
                        ((org.apache.spark.sql.execution.metric.SQLMetric) references[3] /* avgHashProbe */),
                        ((org.apache.spark.sql.execution.metric.SQLMetric) references[4] /* numTasksFallBacked */));
    }

    private void hashAgg_doAggregate_sum_1(org.apache.spark.sql.catalyst.InternalRow hashAgg_unsafeRowAggBuffer_1,
            boolean hashAgg_exprIsNull_3_1, double hashAgg_expr_3_1) throws java.io.IOException {
        hashAgg_hashAgg_isNull_45_0 = true;
        double hashAgg_value_46 = -1.0;
        do {
            boolean hashAgg_isNull_46 = true;
            double hashAgg_value_47 = -1.0;
            hashAgg_hashAgg_isNull_47_0 = true;
            double hashAgg_value_48 = -1.0;
            do {
                boolean hashAgg_isNull_48 = hashAgg_unsafeRowAggBuffer_1.isNullAt(0);
                double hashAgg_value_49 = hashAgg_isNull_48 ? -1.0 : (hashAgg_unsafeRowAggBuffer_1.getDouble(0));
                if (!hashAgg_isNull_48) {
                    hashAgg_hashAgg_isNull_47_0 = false;
                    hashAgg_value_48 = hashAgg_value_49;
                    continue;
                }

                if (!false) {
                    hashAgg_hashAgg_isNull_47_0 = false;
                    hashAgg_value_48 = 0.0D;
                    continue;
                }

            } while (false);

            if (!hashAgg_exprIsNull_3_1) {
                hashAgg_isNull_46 = false; // resultCode could change nullability.

                hashAgg_value_47 = hashAgg_value_48 + hashAgg_expr_3_1;

            }
            if (!hashAgg_isNull_46) {
                hashAgg_hashAgg_isNull_45_0 = false;
                hashAgg_value_46 = hashAgg_value_47;
                continue;
            }

            boolean hashAgg_isNull_51 = hashAgg_unsafeRowAggBuffer_1.isNullAt(0);
            double hashAgg_value_52 = hashAgg_isNull_51 ? -1.0 : (hashAgg_unsafeRowAggBuffer_1.getDouble(0));
            if (!hashAgg_isNull_51) {
                hashAgg_hashAgg_isNull_45_0 = false;
                hashAgg_value_46 = hashAgg_value_52;
                continue;
            }

        } while (false);

        if (!hashAgg_hashAgg_isNull_45_0) {
            hashAgg_unsafeRowAggBuffer_1.setDouble(0, hashAgg_value_46);
        } else {
            hashAgg_unsafeRowAggBuffer_1.setNullAt(0);
        }
    }

    private void hashAgg_doAggregateWithKeysOutput_1(UnsafeRow hashAgg_keyTerm_1, UnsafeRow hashAgg_bufferTerm_1)
            throws java.io.IOException {
        ((org.apache.spark.sql.execution.metric.SQLMetric) references[19] /* numOutputRows */).add(1);

        boolean hashAgg_isNull_52 = hashAgg_keyTerm_1.isNullAt(0);
        int hashAgg_value_53 = hashAgg_isNull_52 ? -1 : (hashAgg_keyTerm_1.getInt(0));
        boolean hashAgg_isNull_53 = hashAgg_keyTerm_1.isNullAt(1);
        int hashAgg_value_54 = hashAgg_isNull_53 ? -1 : (hashAgg_keyTerm_1.getInt(1));
        boolean hashAgg_isNull_54 = hashAgg_keyTerm_1.isNullAt(2);
        int hashAgg_value_55 = hashAgg_isNull_54 ? -1 : (hashAgg_keyTerm_1.getInt(2));
        boolean hashAgg_isNull_55 = hashAgg_bufferTerm_1.isNullAt(0);
        double hashAgg_value_56 = hashAgg_isNull_55 ? -1.0 : (hashAgg_bufferTerm_1.getDouble(0));

        boolean hashAgg_isNull_57 = hashAgg_isNull_52;
        UTF8String hashAgg_value_58 = null;
        if (!hashAgg_isNull_52) {
            hashAgg_value_58 = UTF8String.fromString(String.valueOf(hashAgg_value_53));
        }
        boolean hashAgg_isNull_59 = hashAgg_isNull_55;
        UTF8String hashAgg_value_60 = null;
        if (!hashAgg_isNull_55) {
            hashAgg_value_60 = UTF8String.fromString(String.valueOf(hashAgg_value_56));
        }
        boolean hashAgg_isNull_61 = hashAgg_isNull_53;
        UTF8String hashAgg_value_62 = null;
        if (!hashAgg_isNull_53) {
            hashAgg_value_62 = UTF8String.fromString(
                    ((org.apache.spark.sql.catalyst.util.DefaultDateFormatter) references[20] /* dateFormatter */)
                            .format(hashAgg_value_54));
        }
        boolean hashAgg_isNull_63 = hashAgg_isNull_54;
        UTF8String hashAgg_value_64 = null;
        if (!hashAgg_isNull_54) {
            hashAgg_value_64 = UTF8String.fromString(String.valueOf(hashAgg_value_55));
        }
        filter_mutableStateArray_0[11].reset();

        filter_mutableStateArray_0[11].zeroOutNullBytes();

        if (hashAgg_isNull_57) {
            filter_mutableStateArray_0[11].setNullAt(0);
        } else {
            filter_mutableStateArray_0[11].write(0, hashAgg_value_58);
        }

        if (hashAgg_isNull_59) {
            filter_mutableStateArray_0[11].setNullAt(1);
        } else {
            filter_mutableStateArray_0[11].write(1, hashAgg_value_60);
        }

        if (hashAgg_isNull_61) {
            filter_mutableStateArray_0[11].setNullAt(2);
        } else {
            filter_mutableStateArray_0[11].write(2, hashAgg_value_62);
        }

        if (hashAgg_isNull_63) {
            filter_mutableStateArray_0[11].setNullAt(3);
        } else {
            filter_mutableStateArray_0[11].write(3, hashAgg_value_64);
        }
        append((filter_mutableStateArray_0[11].getRow()));

    }

    private void hashAgg_doConsume_0(int hashAgg_expr_0_0, boolean hashAgg_exprIsNull_0_0, int hashAgg_expr_1_0,
            boolean hashAgg_exprIsNull_1_0, int hashAgg_expr_2_0, boolean hashAgg_exprIsNull_2_0,
            double hashAgg_expr_3_0, boolean hashAgg_exprIsNull_3_0, double hashAgg_expr_4_0,
            boolean hashAgg_exprIsNull_4_0) throws java.io.IOException {
        UnsafeRow hashAgg_unsafeRowAggBuffer_0 = null;
        UnsafeRow hashAgg_fastAggBuffer_0 = null;

        if (!hashAgg_exprIsNull_2_0 && !hashAgg_exprIsNull_0_0 && !hashAgg_exprIsNull_1_0) {
            hashAgg_fastAggBuffer_0 = hashAgg_fastHashMap_0.findOrInsert(
                    hashAgg_expr_2_0, hashAgg_expr_0_0, hashAgg_expr_1_0);
        }
        // Cannot find the key in fast hash map, try regular hash map.
        if (hashAgg_fastAggBuffer_0 == null) {
            // generate grouping key
            filter_mutableStateArray_0[7].reset();

            filter_mutableStateArray_0[7].zeroOutNullBytes();

            if (hashAgg_exprIsNull_2_0) {
                filter_mutableStateArray_0[7].setNullAt(0);
            } else {
                filter_mutableStateArray_0[7].write(0, hashAgg_expr_2_0);
            }

            if (hashAgg_exprIsNull_0_0) {
                filter_mutableStateArray_0[7].setNullAt(1);
            } else {
                filter_mutableStateArray_0[7].write(1, hashAgg_expr_0_0);
            }

            if (hashAgg_exprIsNull_1_0) {
                filter_mutableStateArray_0[7].setNullAt(2);
            } else {
                filter_mutableStateArray_0[7].write(2, hashAgg_expr_1_0);
            }
            int hashAgg_unsafeRowKeyHash_0 = (filter_mutableStateArray_0[7].getRow()).hashCode();
            if (true) {
                // try to get the buffer from hash map
                hashAgg_unsafeRowAggBuffer_0 = hashAgg_hashMap_1.getAggregationBufferFromUnsafeRow(
                        (filter_mutableStateArray_0[7].getRow()), hashAgg_unsafeRowKeyHash_0);
            }
            // Can't allocate buffer from the hash map. Spill the map and fallback to
            // sort-based
            // aggregation after processing all input rows.
            if (hashAgg_unsafeRowAggBuffer_0 == null) {
                if (hashAgg_sorter_1 == null) {
                    hashAgg_sorter_1 = hashAgg_hashMap_1.destructAndCreateExternalSorter();
                } else {
                    hashAgg_sorter_1.merge(hashAgg_hashMap_1.destructAndCreateExternalSorter());
                }

                // the hash map had be spilled, it should have enough memory now,
                // try to allocate buffer again.
                hashAgg_unsafeRowAggBuffer_0 = hashAgg_hashMap_1.getAggregationBufferFromUnsafeRow(
                        (filter_mutableStateArray_0[7].getRow()), hashAgg_unsafeRowKeyHash_0);
                if (hashAgg_unsafeRowAggBuffer_0 == null) {
                    // failed to allocate the first page
                    throw new org.apache.spark.memory.SparkOutOfMemoryError("No enough memory for aggregation");
                }
            }

        }

        // Updates the proper row buffer
        if (hashAgg_fastAggBuffer_0 != null) {
            hashAgg_unsafeRowAggBuffer_0 = hashAgg_fastAggBuffer_0;
        }

        // common sub-expressions

        // evaluate aggregate functions and update aggregation buffers
        hashAgg_doAggregate_sum_0(hashAgg_exprIsNull_3_0, hashAgg_expr_3_0, hashAgg_unsafeRowAggBuffer_0,
                hashAgg_exprIsNull_4_0, hashAgg_expr_4_0);

    }

    private void wholestagecodegen_init_0_1() {
        filter_mutableStateArray_0[5] = new org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter(5, 0);
        filter_mutableStateArray_0[6] = new org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter(5, 0);
        filter_mutableStateArray_0[7] = new org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter(3, 0);
        filter_mutableStateArray_0[8] = new org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter(4, 0);
        filter_mutableStateArray_0[9] = new org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter(4, 0);
        filter_mutableStateArray_0[10] = new org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter(3, 0);
        filter_mutableStateArray_0[11] = new org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter(4, 128);

    }

    private void hashAgg_doAggregate_sum_0(boolean hashAgg_exprIsNull_3_0, double hashAgg_expr_3_0,
            org.apache.spark.sql.catalyst.InternalRow hashAgg_unsafeRowAggBuffer_0, boolean hashAgg_exprIsNull_4_0,
            double hashAgg_expr_4_0) throws java.io.IOException {
        hashAgg_hashAgg_isNull_12_0 = true;
        double hashAgg_value_13 = -1.0;
        do {
            boolean hashAgg_isNull_13 = true;
            double hashAgg_value_14 = -1.0;
            hashAgg_hashAgg_isNull_14_0 = true;
            double hashAgg_value_15 = -1.0;
            do {
                boolean hashAgg_isNull_15 = hashAgg_unsafeRowAggBuffer_0.isNullAt(0);
                double hashAgg_value_16 = hashAgg_isNull_15 ? -1.0 : (hashAgg_unsafeRowAggBuffer_0.getDouble(0));
                if (!hashAgg_isNull_15) {
                    hashAgg_hashAgg_isNull_14_0 = false;
                    hashAgg_value_15 = hashAgg_value_16;
                    continue;
                }

                if (!false) {
                    hashAgg_hashAgg_isNull_14_0 = false;
                    hashAgg_value_15 = 0.0D;
                    continue;
                }

            } while (false);
            boolean hashAgg_isNull_17 = true;
            double hashAgg_value_18 = -1.0;

            if (!hashAgg_exprIsNull_3_0) {
                boolean hashAgg_isNull_19 = true;
                double hashAgg_value_20 = -1.0;

                if (!hashAgg_exprIsNull_4_0) {
                    hashAgg_isNull_19 = false; // resultCode could change nullability.

                    hashAgg_value_20 = 1.0D - hashAgg_expr_4_0;

                }
                if (!hashAgg_isNull_19) {
                    hashAgg_isNull_17 = false; // resultCode could change nullability.

                    hashAgg_value_18 = hashAgg_expr_3_0 * hashAgg_value_20;

                }

            }
            if (!hashAgg_isNull_17) {
                hashAgg_isNull_13 = false; // resultCode could change nullability.

                hashAgg_value_14 = hashAgg_value_15 + hashAgg_value_18;

            }
            if (!hashAgg_isNull_13) {
                hashAgg_hashAgg_isNull_12_0 = false;
                hashAgg_value_13 = hashAgg_value_14;
                continue;
            }

            boolean hashAgg_isNull_22 = hashAgg_unsafeRowAggBuffer_0.isNullAt(0);
            double hashAgg_value_23 = hashAgg_isNull_22 ? -1.0 : (hashAgg_unsafeRowAggBuffer_0.getDouble(0));
            if (!hashAgg_isNull_22) {
                hashAgg_hashAgg_isNull_12_0 = false;
                hashAgg_value_13 = hashAgg_value_23;
                continue;
            }

        } while (false);

        if (!hashAgg_hashAgg_isNull_12_0) {
            hashAgg_unsafeRowAggBuffer_0.setDouble(0, hashAgg_value_13);
        } else {
            hashAgg_unsafeRowAggBuffer_0.setNullAt(0);
        }
    }

    private void hashAgg_doAggregateWithKeysOutput_0(UnsafeRow hashAgg_keyTerm_0, UnsafeRow hashAgg_bufferTerm_0)
            throws java.io.IOException {
        ((org.apache.spark.sql.execution.metric.SQLMetric) references[17] /* numOutputRows */).add(1);

        boolean hashAgg_isNull_23 = hashAgg_keyTerm_0.isNullAt(0);
        int hashAgg_value_24 = hashAgg_isNull_23 ? -1 : (hashAgg_keyTerm_0.getInt(0));
        boolean hashAgg_isNull_24 = hashAgg_keyTerm_0.isNullAt(1);
        int hashAgg_value_25 = hashAgg_isNull_24 ? -1 : (hashAgg_keyTerm_0.getInt(1));
        boolean hashAgg_isNull_25 = hashAgg_keyTerm_0.isNullAt(2);
        int hashAgg_value_26 = hashAgg_isNull_25 ? -1 : (hashAgg_keyTerm_0.getInt(2));
        boolean hashAgg_isNull_26 = hashAgg_bufferTerm_0.isNullAt(0);
        double hashAgg_value_27 = hashAgg_isNull_26 ? -1.0 : (hashAgg_bufferTerm_0.getDouble(0));

        hashAgg_doConsume_1(hashAgg_value_24, hashAgg_isNull_23, hashAgg_value_25, hashAgg_isNull_24, hashAgg_value_26,
                hashAgg_isNull_25, hashAgg_value_27, hashAgg_isNull_26);

    }

    private void hashAgg_doAggregateWithKeys_1() throws java.io.IOException {
        while (inputadapter_input_0.hasNext()) {
            InternalRow inputadapter_row_0 = (InternalRow) inputadapter_input_0.next();

            do {
                boolean inputadapter_isNull_4 = inputadapter_row_0.isNullAt(4);
                int inputadapter_value_4 = inputadapter_isNull_4 ? -1 : (inputadapter_row_0.getInt(4));

                boolean filter_value_2 = !inputadapter_isNull_4;
                if (!filter_value_2)
                    continue;

                boolean filter_value_3 = false;
                filter_value_3 = inputadapter_value_4 < 9204;
                if (!filter_value_3)
                    continue;

                boolean inputadapter_isNull_1 = inputadapter_row_0.isNullAt(1);
                int inputadapter_value_1 = inputadapter_isNull_1 ? -1 : (inputadapter_row_0.getInt(1));

                boolean filter_value_8 = !inputadapter_isNull_1;
                if (!filter_value_8)
                    continue;

                boolean inputadapter_isNull_0 = inputadapter_row_0.isNullAt(0);
                int inputadapter_value_0 = inputadapter_isNull_0 ? -1 : (inputadapter_row_0.getInt(0));

                boolean filter_value_11 = !inputadapter_isNull_0;
                if (!filter_value_11)
                    continue;

                ((org.apache.spark.sql.execution.metric.SQLMetric) references[12] /* numOutputRows */).add(1);

                // common sub-expressions

                // generate join key for stream side
                boolean bhj_isNull_0 = false;
                long bhj_value_0 = -1L;
                if (!false) {
                    bhj_value_0 = (long) inputadapter_value_1;
                }
                // find matches from HashedRelation
                UnsafeRow bhj_buildRow_0 = bhj_isNull_0 ? null : (UnsafeRow) bhj_relation_0.getValue(bhj_value_0);
                if (bhj_buildRow_0 != null) {
                    {
                        ((org.apache.spark.sql.execution.metric.SQLMetric) references[14] /* numOutputRows */).add(1);

                        // common sub-expressions

                        // generate join key for stream side
                        boolean bhj_isNull_8 = false;
                        long bhj_value_8 = -1L;
                        if (!false) {
                            bhj_value_8 = (long) inputadapter_value_0;
                        }
                        // find matches from HashRelation
                        scala.collection.Iterator bhj_matches_0 = bhj_isNull_8 ? null
                                : (scala.collection.Iterator) bhj_relation_1.get(bhj_value_8);
                        if (bhj_matches_0 != null) {
                            while (bhj_matches_0.hasNext()) {
                                UnsafeRow bhj_buildRow_1 = (UnsafeRow) bhj_matches_0.next();
                                {
                                    ((org.apache.spark.sql.execution.metric.SQLMetric) references[16] /*
                                                                                                       * numOutputRows
                                                                                                       */).add(1);

                                    // common sub-expressions

                                    boolean inputadapter_isNull_7 = inputadapter_row_0.isNullAt(7);
                                    int inputadapter_value_7 = inputadapter_isNull_7 ? -1
                                            : (inputadapter_row_0.getInt(7));
                                    boolean bhj_isNull_10 = bhj_buildRow_1.isNullAt(0);
                                    int bhj_value_10 = bhj_isNull_10 ? -1 : (bhj_buildRow_1.getInt(0));
                                    boolean bhj_isNull_11 = bhj_buildRow_1.isNullAt(1);
                                    double bhj_value_11 = bhj_isNull_11 ? -1.0 : (bhj_buildRow_1.getDouble(1));
                                    boolean bhj_isNull_12 = bhj_buildRow_1.isNullAt(2);
                                    double bhj_value_12 = bhj_isNull_12 ? -1.0 : (bhj_buildRow_1.getDouble(2));

                                    hashAgg_doConsume_0(inputadapter_value_4, false, inputadapter_value_7,
                                            inputadapter_isNull_7, bhj_value_10, bhj_isNull_10, bhj_value_11,
                                            bhj_isNull_11, bhj_value_12, bhj_isNull_12);

                                }
                            }
                        }

                    }
                }

            } while (false);
            // shouldStop check is eliminated
        }

        hashAgg_fastHashMapIter_0 = hashAgg_fastHashMap_0.rowIterator();
        hashAgg_mapIter_1 = ((org.apache.spark.sql.execution.aggregate.HashAggregateExec) references[5] /* plan */)
                .finishAggregate(hashAgg_hashMap_1, hashAgg_sorter_1,
                        ((org.apache.spark.sql.execution.metric.SQLMetric) references[8] /* peakMemory */),
                        ((org.apache.spark.sql.execution.metric.SQLMetric) references[9] /* spillSize */),
                        ((org.apache.spark.sql.execution.metric.SQLMetric) references[10] /* avgHashProbe */),
                        ((org.apache.spark.sql.execution.metric.SQLMetric) references[11] /* numTasksFallBacked */));

    }

    protected void processNext() throws java.io.IOException {
        if (!hashAgg_initAgg_0) {
            hashAgg_initAgg_0 = true;

            hashAgg_hashMap_0 = ((org.apache.spark.sql.execution.aggregate.HashAggregateExec) references[0] /* plan */)
                    .createHashMap();
            long wholestagecodegen_beforeAgg_0 = System.nanoTime();
            hashAgg_doAggregateWithKeys_0();
            ((org.apache.spark.sql.execution.metric.SQLMetric) references[21] /* aggTime */)
                    .add((System.nanoTime() - wholestagecodegen_beforeAgg_0) / 1000000);
        }
        // output the result

        while (hashAgg_mapIter_0.next()) {
            UnsafeRow hashAgg_aggKey_1 = (UnsafeRow) hashAgg_mapIter_0.getKey();
            UnsafeRow hashAgg_aggBuffer_1 = (UnsafeRow) hashAgg_mapIter_0.getValue();
            hashAgg_doAggregateWithKeysOutput_1(hashAgg_aggKey_1, hashAgg_aggBuffer_1);
            if (shouldStop())
                return;
        }
        hashAgg_mapIter_0.close();
        if (hashAgg_sorter_0 == null) {
            hashAgg_hashMap_0.free();
        }
    }

    private void wholestagecodegen_init_0_0() {
        inputadapter_input_0 = inputs[0];
        filter_mutableStateArray_0[0] = new org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter(9, 128);
        filter_mutableStateArray_0[1] = new org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter(4, 0);

        bhj_relation_0 = ((org.apache.spark.sql.execution.joins.LongHashedRelation) ((org.apache.spark.broadcast.TorrentBroadcast) references[13] /*
                                                                                                                                                   * broadcast
                                                                                                                                                   */)
                .value()).asReadOnlyCopy();
        incPeakExecutionMemory(bhj_relation_0.estimatedSize());

        filter_mutableStateArray_0[2] = new org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter(5, 0);
        filter_mutableStateArray_0[3] = new org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter(3, 0);

        bhj_relation_1 = ((org.apache.spark.sql.execution.joins.LongHashedRelation) ((org.apache.spark.broadcast.TorrentBroadcast) references[15] /*
                                                                                                                                                   * broadcast
                                                                                                                                                   */)
                .value()).asReadOnlyCopy();
        incPeakExecutionMemory(bhj_relation_1.estimatedSize());

        filter_mutableStateArray_0[4] = new org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter(6, 0);

    }

}