final class GeneratedIteratorForCodegenStage2 extends org.apache.spark.sql.execution.BufferedRowIterator {
    private Object[] references;
    private scala.collection.Iterator[] inputs;
    private boolean hashAgg_initAgg_0;
    private org.apache.spark.unsafe.KVIterator hashAgg_mapIter_0;
    private org.apache.spark.sql.execution.UnsafeFixedWidthAggregationMap hashAgg_hashMap_0;
    private org.apache.spark.sql.execution.UnsafeKVExternalSorter hashAgg_sorter_0;
    private boolean hashAgg_initAgg_1;
    private boolean hashAgg_bufIsNull_0;
    private double hashAgg_bufValue_0;
    private boolean hashAgg_bufIsNull_1;
    private double hashAgg_bufValue_1;
    private boolean hashAgg_bufIsNull_2;
    private double hashAgg_bufValue_2;
    private boolean hashAgg_bufIsNull_3;
    private double hashAgg_bufValue_3;
    private boolean hashAgg_bufIsNull_4;
    private double hashAgg_bufValue_4;
    private boolean hashAgg_bufIsNull_5;
    private long hashAgg_bufValue_5;
    private boolean hashAgg_bufIsNull_6;
    private double hashAgg_bufValue_6;
    private boolean hashAgg_bufIsNull_7;
    private long hashAgg_bufValue_7;
    private boolean hashAgg_bufIsNull_8;
    private double hashAgg_bufValue_8;
    private boolean hashAgg_bufIsNull_9;
    private long hashAgg_bufValue_9;
    private boolean hashAgg_bufIsNull_10;
    private long hashAgg_bufValue_10;
    private hashAgg_FastHashMap_0 hashAgg_fastHashMap_0;
    private org.apache.spark.unsafe.KVIterator<UnsafeRow, UnsafeRow> hashAgg_fastHashMapIter_0;
    private org.apache.spark.unsafe.KVIterator hashAgg_mapIter_1;
    private org.apache.spark.sql.execution.UnsafeFixedWidthAggregationMap hashAgg_hashMap_1;
    private org.apache.spark.sql.execution.UnsafeKVExternalSorter hashAgg_sorter_1;
    private scala.collection.Iterator inputadapter_input_0;
    private boolean hashAgg_hashAgg_isNull_26_0;
    private boolean hashAgg_hashAgg_isNull_28_0;
    private boolean hashAgg_hashAgg_isNull_33_0;
    private boolean hashAgg_hashAgg_isNull_35_0;
    private boolean hashAgg_hashAgg_isNull_40_0;
    private boolean hashAgg_hashAgg_isNull_42_0;
    private boolean hashAgg_hashAgg_isNull_46_0;
    private boolean hashAgg_hashAgg_isNull_48_0;
    private boolean hashAgg_hashAgg_isNull_58_0;
    private boolean hashAgg_hashAgg_isNull_70_0;
    private boolean hashAgg_hashAgg_isNull_82_0;
    private boolean hashAgg_hashAgg_isNull_151_0;
    private boolean hashAgg_hashAgg_isNull_153_0;
    private boolean hashAgg_hashAgg_isNull_158_0;
    private boolean hashAgg_hashAgg_isNull_160_0;
    private boolean hashAgg_hashAgg_isNull_165_0;
    private boolean hashAgg_hashAgg_isNull_167_0;
    private boolean hashAgg_hashAgg_isNull_172_0;
    private boolean hashAgg_hashAgg_isNull_174_0;
    private org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter[] filter_mutableStateArray_0 = new org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter[8];

    //// Seems to be the top-level method of the query
    protected void processNext() {
        if (!hashAgg_initAgg_0) {
            hashAgg_initAgg_0 = true;

            //// Initialise some kind of general purpose aggregation map
            hashAgg_hashMap_0 = ((org.apache.spark.sql.execution.aggregate.HashAggregateExec) references[0] /* plan */).createHashMap();
            
            //// Initialise some query-specific hash-map infrastructure
            hashAgg_doAggregateWithKeys_0();
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

    //// Method which seems to start initialisation of the generated hash-map infrastructure
    private void hashAgg_doAggregateWithKeys_0() throws java.io.IOException {
        if (!hashAgg_initAgg_1) {
            hashAgg_initAgg_1 = true;
            //// Initialise the query-specific map
            hashAgg_fastHashMap_0 = new hashAgg_FastHashMap_0(
                    ((org.apache.spark.sql.execution.aggregate.HashAggregateExec) references[5] /* plan */)
                            .getTaskContext().taskMemoryManager(),
                    ((org.apache.spark.sql.execution.aggregate.HashAggregateExec) references[5] /* plan */)
                            .getEmptyAggregationBuffer());

            //// Initialise another GP hash-map
            hashAgg_hashMap_1 = ((org.apache.spark.sql.execution.aggregate.HashAggregateExec) references[5] /* plan */)
                    .createHashMap();

            hashAgg_doAggregateWithKeys_1();

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

    private void hashAgg_doAggregateWithKeys_1() throws java.io.IOException {
        while (inputadapter_input_0.hasNext()) {
            InternalRow inputadapter_row_0 = (InternalRow) inputadapter_input_0.next();

            do {
                //// Check if l_shipdate <= 1998-12-01, code simplified
                boolean filter_value_3 = inputadapter_row_0.getInt(10) <= 10471;
                if (!filter_value_3)
                    continue;

                ((org.apache.spark.sql.execution.metric.SQLMetric) references[12] /* numOutputRows */).add(1);

                //// Simplified common sub-expressions
                double inputadapter_value_4 = //// get l_quantity
                double inputadapter_value_5 = //// get l_extendedprice
                double inputadapter_value_6 = //// get l_discount
                double inputadapter_value_7 = //// get l_tax
                UTF8String inputadapter_value_8 = //// get l_returnflag (group key part 2)
                UTF8String inputadapter_value_9 = //// get l_linestatus (group key part 1)

                //// Consume the hash aggregate
                hashAgg_doConsume_0(inputadapter_value_4, inputadapter_isNull_4, inputadapter_value_5,
                        inputadapter_isNull_5, inputadapter_value_6, inputadapter_isNull_6, inputadapter_value_7,
                        inputadapter_isNull_7, inputadapter_value_8, inputadapter_isNull_8, inputadapter_value_9,
                        inputadapter_isNull_9);

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

    private void hashAgg_doConsume_0(double hashAgg_expr_0_0, boolean hashAgg_exprIsNull_0_0, double hashAgg_expr_1_0,
            boolean hashAgg_exprIsNull_1_0, double hashAgg_expr_2_0, boolean hashAgg_exprIsNull_2_0,
            double hashAgg_expr_3_0, boolean hashAgg_exprIsNull_3_0, UTF8String hashAgg_expr_4_0,
            boolean hashAgg_exprIsNull_4_0, UTF8String hashAgg_expr_5_0, boolean hashAgg_exprIsNull_5_0)
            throws java.io.IOException {
        UnsafeRow hashAgg_unsafeRowAggBuffer_0 = null;
        UnsafeRow hashAgg_fastAggBuffer_0 = null;
        //// Look up the key-group in the somewhat query-specific fast hashmap
        if (!hashAgg_exprIsNull_4_0 && !hashAgg_exprIsNull_5_0) {
            hashAgg_fastAggBuffer_0 = hashAgg_fastHashMap_0.findOrInsert(
                    hashAgg_expr_4_0, hashAgg_expr_5_0);
        }
        // Cannot find the key in fast hash map, try regular hash map.
        if (hashAgg_fastAggBuffer_0 == null) {
            // generate grouping key
            filter_mutableStateArray_0[3].reset();
            filter_mutableStateArray_0[3].zeroOutNullBytes();
            if (hashAgg_exprIsNull_4_0) {
                filter_mutableStateArray_0[3].setNullAt(0);
            } else {
                filter_mutableStateArray_0[3].write(0, hashAgg_expr_4_0);
            }
            if (hashAgg_exprIsNull_5_0) {
                filter_mutableStateArray_0[3].setNullAt(1);
            } else {
                filter_mutableStateArray_0[3].write(1, hashAgg_expr_5_0);
            }
            int hashAgg_unsafeRowKeyHash_0 = (filter_mutableStateArray_0[3].getRow()).hashCode();
            if (true) {
                // try to get the buffer from hash map
                hashAgg_unsafeRowAggBuffer_0 = hashAgg_hashMap_1.getAggregationBufferFromUnsafeRow(
                        (filter_mutableStateArray_0[3].getRow()), hashAgg_unsafeRowKeyHash_0);
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
                        (filter_mutableStateArray_0[3].getRow()), hashAgg_unsafeRowKeyHash_0);
                if (hashAgg_unsafeRowAggBuffer_0 == null) {
                    // failed to allocate the first page
                    throw new org.apache.spark.memory.SparkOutOfMemoryError("No enough memory for aggregation");
                }
            }
        }
        //// Basically, after the hash-table lookups, it seems there is a general "Row" datastructure which maintains the value for each "aggregate"
        // Updates the proper row buffer
        if (hashAgg_fastAggBuffer_0 != null) {
            hashAgg_unsafeRowAggBuffer_0 = hashAgg_fastAggBuffer_0;
        }
        // common sub-expressions
        boolean hashAgg_isNull_21 = true;
        double hashAgg_value_32 = -1.0;
        if (!hashAgg_exprIsNull_1_0) {
            boolean hashAgg_isNull_23 = true;
            double hashAgg_value_34 = -1.0;
            if (!hashAgg_exprIsNull_2_0) {
                hashAgg_isNull_23 = false; // resultCode could change nullability.
                hashAgg_value_34 = 1.0D - hashAgg_expr_2_0;
            }
            if (!hashAgg_isNull_23) {
                hashAgg_isNull_21 = false; // resultCode could change nullability.
                hashAgg_value_32 = hashAgg_expr_1_0 * hashAgg_value_34;
            }
        }
        // evaluate aggregate functions and update aggregation buffers
        hashAgg_doAggregate_sum_0(hashAgg_unsafeRowAggBuffer_0, hashAgg_expr_0_0, hashAgg_exprIsNull_0_0);  // maintain sum_qty
        hashAgg_doAggregate_sum_1(hashAgg_expr_1_0, hashAgg_exprIsNull_1_0, hashAgg_unsafeRowAggBuffer_0);  // maintian sum_base_price
        hashAgg_doAggregate_sum_2(hashAgg_isNull_21, hashAgg_unsafeRowAggBuffer_0, hashAgg_value_32);       // maintain sum_disc_price
        hashAgg_doAggregate_sum_3(hashAgg_exprIsNull_3_0, hashAgg_expr_3_0, hashAgg_unsafeRowAggBuffer_0,   // maintain sum_charge
                hashAgg_value_32, hashAgg_isNull_21);
        hashAgg_doAggregate_avg_0(hashAgg_unsafeRowAggBuffer_0, hashAgg_expr_0_0, hashAgg_exprIsNull_0_0);  // maintain avg_qty
        hashAgg_doAggregate_avg_1(hashAgg_expr_1_0, hashAgg_exprIsNull_1_0, hashAgg_unsafeRowAggBuffer_0);  // maintain avg_price
        hashAgg_doAggregate_avg_2(hashAgg_expr_2_0, hashAgg_unsafeRowAggBuffer_0, hashAgg_exprIsNull_2_0);  // maintain avg_discount
        hashAgg_doAggregate_count_0(hashAgg_unsafeRowAggBuffer_0);                                          // maintain count
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

        public hashAgg_FastHashMap_0(TaskMemoryManager taskMemoryManager, InternalRow emptyAggregationBuffer) {
            batch = org.apache.spark.sql.catalyst.expressions.RowBasedKeyValueBatch.allocate(...);
            final UnsafeProjection valueProjection = UnsafeProjection.create(...);
            final byte[] emptyBuffer = valueProjection.apply(emptyAggregationBuffer).getBytes();
            emptyVBase = emptyBuffer;
            emptyVOff = Platform.BYTE_ARRAY_OFFSET;
            emptyVLen = emptyBuffer.length;
            agg_rowWriter = new org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter(2, 64);
            buckets = new int[numBuckets];
            java.util.Arrays.fill(buckets, -1);
        }

        // Method which finds/creates the "bucket" for a given group-key as represented by the two string args
        // Seems to be query-specific
        public org.apache.spark.sql.catalyst.expressions.UnsafeRow findOrInsert(UTF8String hashAgg_key_0, UTF8String hashAgg_key_1) {
            long h = hash(hashAgg_key_0, hashAgg_key_1);
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
                } else if (equals(idx, hashAgg_key_0, hashAgg_key_1)) {
                    return batch.getValueRow(buckets[idx]);
                }
                idx = (idx + 1) & (numBuckets - 1);
                step++;
            }
            // Didn't find it
            return null;
        }

        // Checks for key-equality, seems query specific
        private boolean equals(int idx, UTF8String hashAgg_key_0, UTF8String hashAgg_key_1) {
            UnsafeRow row = batch.getKeyRow(buckets[idx]);
            return (row.getUTF8String(0).equals(hashAgg_key_0)) && (row.getUTF8String(1).equals(hashAgg_key_1));
        }

        // Creates a hash-value based on the two key arguments, seems query specific
        private long hash(UTF8String hashAgg_key_0, UTF8String hashAgg_key_1) {
            long hashAgg_hash_0 = 0;
            int hashAgg_result_0 = 0;
            byte[] hashAgg_bytes_0 = hashAgg_key_0.getBytes();
            for (int i = 0; i < hashAgg_bytes_0.length; i++) {
                int hashAgg_hash_1 = hashAgg_bytes_0[i];
                hashAgg_result_0 = (hashAgg_result_0 ^ (0x9e3779b9)) + hashAgg_hash_1 + (hashAgg_result_0 << 6)
                        + (hashAgg_result_0 >>> 2);
            }
            hashAgg_hash_0 = (hashAgg_hash_0 ^ (0x9e3779b9)) + hashAgg_result_0 + (hashAgg_hash_0 << 6)
                    + (hashAgg_hash_0 >>> 2);
            int hashAgg_result_1 = 0;
            byte[] hashAgg_bytes_1 = hashAgg_key_1.getBytes();
            for (int i = 0; i < hashAgg_bytes_1.length; i++) {
                int hashAgg_hash_2 = hashAgg_bytes_1[i];
                hashAgg_result_1 = (hashAgg_result_1 ^ (0x9e3779b9)) + hashAgg_hash_2 + (hashAgg_result_1 << 6)
                        + (hashAgg_result_1 >>> 2);
            }
            hashAgg_hash_0 = (hashAgg_hash_0 ^ (0x9e3779b9)) + hashAgg_result_1 + (hashAgg_hash_0 << 6)
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

    //// Not investigated in detail yet

    private void hashAgg_doConsume_1(UTF8String hashAgg_expr_0_1, boolean hashAgg_exprIsNull_0_1,
            UTF8String hashAgg_expr_1_1, boolean hashAgg_exprIsNull_1_1, double hashAgg_expr_2_1,
            boolean hashAgg_exprIsNull_2_1, double hashAgg_expr_3_1, boolean hashAgg_exprIsNull_3_1,
            double hashAgg_expr_4_1, boolean hashAgg_exprIsNull_4_1, double hashAgg_expr_5_1,
            boolean hashAgg_exprIsNull_5_1, double hashAgg_expr_6_0, boolean hashAgg_exprIsNull_6_0,
            long hashAgg_expr_7_0, boolean hashAgg_exprIsNull_7_0, double hashAgg_expr_8_0,
            boolean hashAgg_exprIsNull_8_0, long hashAgg_expr_9_0, boolean hashAgg_exprIsNull_9_0,
            double hashAgg_expr_10_0, boolean hashAgg_exprIsNull_10_0, long hashAgg_expr_11_0,
            boolean hashAgg_exprIsNull_11_0, long hashAgg_expr_12_0) throws java.io.IOException {
        UnsafeRow hashAgg_unsafeRowAggBuffer_1 = null;
        // generate grouping key
        filter_mutableStateArray_0[6].reset();
        filter_mutableStateArray_0[6].zeroOutNullBytes();
        if (hashAgg_exprIsNull_0_1) {
            filter_mutableStateArray_0[6].setNullAt(0);
        } else {
            filter_mutableStateArray_0[6].write(0, hashAgg_expr_0_1);
        }
        if (hashAgg_exprIsNull_1_1) {
            filter_mutableStateArray_0[6].setNullAt(1);
        } else {
            filter_mutableStateArray_0[6].write(1, hashAgg_expr_1_1);
        }
        int hashAgg_unsafeRowKeyHash_1 = (filter_mutableStateArray_0[6].getRow()).hashCode();
        if (true) {
            // try to get the buffer from hash map
            hashAgg_unsafeRowAggBuffer_1 = hashAgg_hashMap_0.getAggregationBufferFromUnsafeRow(
                    (filter_mutableStateArray_0[6].getRow()), hashAgg_unsafeRowKeyHash_1);
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
                    (filter_mutableStateArray_0[6].getRow()), hashAgg_unsafeRowKeyHash_1);
            if (hashAgg_unsafeRowAggBuffer_1 == null) {
                // failed to allocate the first page
                throw new org.apache.spark.memory.SparkOutOfMemoryError("No enough memory for aggregation");
            }
        }
        // common sub-expressions
        // evaluate aggregate functions and update aggregation buffers
        hashAgg_doAggregate_sum_4(hashAgg_exprIsNull_2_1, hashAgg_unsafeRowAggBuffer_1, hashAgg_expr_2_1);
        hashAgg_doAggregate_sum_5(hashAgg_exprIsNull_3_1, hashAgg_unsafeRowAggBuffer_1, hashAgg_expr_3_1);
        hashAgg_doAggregate_sum_6(hashAgg_unsafeRowAggBuffer_1, hashAgg_exprIsNull_4_1, hashAgg_expr_4_1);
        hashAgg_doAggregate_sum_7(hashAgg_unsafeRowAggBuffer_1, hashAgg_exprIsNull_5_1, hashAgg_expr_5_1);
        hashAgg_doAggregate_avg_3(hashAgg_expr_6_0, hashAgg_exprIsNull_6_0, hashAgg_expr_7_0,
                hashAgg_unsafeRowAggBuffer_1, hashAgg_exprIsNull_7_0);
        hashAgg_doAggregate_avg_4(hashAgg_exprIsNull_9_0, hashAgg_expr_8_0, hashAgg_exprIsNull_8_0,
                hashAgg_unsafeRowAggBuffer_1, hashAgg_expr_9_0);
        hashAgg_doAggregate_avg_5(hashAgg_exprIsNull_10_0, hashAgg_exprIsNull_11_0, hashAgg_unsafeRowAggBuffer_1,
                hashAgg_expr_10_0, hashAgg_expr_11_0);
        hashAgg_doAggregate_count_1(hashAgg_unsafeRowAggBuffer_1, hashAgg_expr_12_0);
    }

    private void hashAgg_doAggregate_sum_4(boolean hashAgg_exprIsNull_2_1,
            org.apache.spark.sql.catalyst.InternalRow hashAgg_unsafeRowAggBuffer_1, double hashAgg_expr_2_1)
            throws java.io.IOException {
        hashAgg_hashAgg_isNull_151_0 = true;
        double hashAgg_value_162 = -1.0;
        do {
            boolean hashAgg_isNull_152 = true;
            double hashAgg_value_163 = -1.0;
            hashAgg_hashAgg_isNull_153_0 = true;
            double hashAgg_value_164 = -1.0;
            do {
                boolean hashAgg_isNull_154 = hashAgg_unsafeRowAggBuffer_1.isNullAt(0);
                double hashAgg_value_165 = hashAgg_isNull_154 ? -1.0 : (hashAgg_unsafeRowAggBuffer_1.getDouble(0));
                if (!hashAgg_isNull_154) {
                    hashAgg_hashAgg_isNull_153_0 = false;
                    hashAgg_value_164 = hashAgg_value_165;
                    continue;
                }
                if (!false) {
                    hashAgg_hashAgg_isNull_153_0 = false;
                    hashAgg_value_164 = 0.0D;
                    continue;
                }
            } while (false);
            if (!hashAgg_exprIsNull_2_1) {
                hashAgg_isNull_152 = false; // resultCode could change nullability.
                hashAgg_value_163 = hashAgg_value_164 + hashAgg_expr_2_1;
            }
            if (!hashAgg_isNull_152) {
                hashAgg_hashAgg_isNull_151_0 = false;
                hashAgg_value_162 = hashAgg_value_163;
                continue;
            }
            boolean hashAgg_isNull_157 = hashAgg_unsafeRowAggBuffer_1.isNullAt(0);
            double hashAgg_value_168 = hashAgg_isNull_157 ? -1.0 : (hashAgg_unsafeRowAggBuffer_1.getDouble(0));
            if (!hashAgg_isNull_157) {
                hashAgg_hashAgg_isNull_151_0 = false;
                hashAgg_value_162 = hashAgg_value_168;
                continue;
            }
        } while (false);
        if (!hashAgg_hashAgg_isNull_151_0) {
            hashAgg_unsafeRowAggBuffer_1.setDouble(0, hashAgg_value_162);
        } else {
            hashAgg_unsafeRowAggBuffer_1.setNullAt(0);
        }
    }

    private void hashAgg_doAggregate_sum_7(org.apache.spark.sql.catalyst.InternalRow hashAgg_unsafeRowAggBuffer_1,
            boolean hashAgg_exprIsNull_5_1, double hashAgg_expr_5_1) throws java.io.IOException {
        hashAgg_hashAgg_isNull_172_0 = true;
        double hashAgg_value_183 = -1.0;
        do {
            boolean hashAgg_isNull_173 = true;
            double hashAgg_value_184 = -1.0;
            hashAgg_hashAgg_isNull_174_0 = true;
            double hashAgg_value_185 = -1.0;
            do {
                boolean hashAgg_isNull_175 = hashAgg_unsafeRowAggBuffer_1.isNullAt(3);
                double hashAgg_value_186 = hashAgg_isNull_175 ? -1.0 : (hashAgg_unsafeRowAggBuffer_1.getDouble(3));
                if (!hashAgg_isNull_175) {
                    hashAgg_hashAgg_isNull_174_0 = false;
                    hashAgg_value_185 = hashAgg_value_186;
                    continue;
                }
                if (!false) {
                    hashAgg_hashAgg_isNull_174_0 = false;
                    hashAgg_value_185 = 0.0D;
                    continue;
                }
            } while (false);
            if (!hashAgg_exprIsNull_5_1) {
                hashAgg_isNull_173 = false; // resultCode could change nullability.
                hashAgg_value_184 = hashAgg_value_185 + hashAgg_expr_5_1;
            }
            if (!hashAgg_isNull_173) {
                hashAgg_hashAgg_isNull_172_0 = false;
                hashAgg_value_183 = hashAgg_value_184;
                continue;
            }
            boolean hashAgg_isNull_178 = hashAgg_unsafeRowAggBuffer_1.isNullAt(3);
            double hashAgg_value_189 = hashAgg_isNull_178 ? -1.0 : (hashAgg_unsafeRowAggBuffer_1.getDouble(3));
            if (!hashAgg_isNull_178) {
                hashAgg_hashAgg_isNull_172_0 = false;
                hashAgg_value_183 = hashAgg_value_189;
                continue;
            }
        } while (false);
        if (!hashAgg_hashAgg_isNull_172_0) {
            hashAgg_unsafeRowAggBuffer_1.setDouble(3, hashAgg_value_183);
        } else {
            hashAgg_unsafeRowAggBuffer_1.setNullAt(3);
        }
    }

    

    private void hashAgg_doAggregate_sum_1(double hashAgg_expr_1_0, boolean hashAgg_exprIsNull_1_0,
            org.apache.spark.sql.catalyst.InternalRow hashAgg_unsafeRowAggBuffer_0) throws java.io.IOException {
        hashAgg_hashAgg_isNull_33_0 = true;
        double hashAgg_value_44 = -1.0;
        do {
            boolean hashAgg_isNull_34 = true;
            double hashAgg_value_45 = -1.0;
            hashAgg_hashAgg_isNull_35_0 = true;
            double hashAgg_value_46 = -1.0;
            do {
                boolean hashAgg_isNull_36 = hashAgg_unsafeRowAggBuffer_0.isNullAt(1);
                double hashAgg_value_47 = hashAgg_isNull_36 ? -1.0 : (hashAgg_unsafeRowAggBuffer_0.getDouble(1));
                if (!hashAgg_isNull_36) {
                    hashAgg_hashAgg_isNull_35_0 = false;
                    hashAgg_value_46 = hashAgg_value_47;
                    continue;
                }
                if (!false) {
                    hashAgg_hashAgg_isNull_35_0 = false;
                    hashAgg_value_46 = 0.0D;
                    continue;
                }
            } while (false);
            if (!hashAgg_exprIsNull_1_0) {
                hashAgg_isNull_34 = false; // resultCode could change nullability.
                hashAgg_value_45 = hashAgg_value_46 + hashAgg_expr_1_0;
            }
            if (!hashAgg_isNull_34) {
                hashAgg_hashAgg_isNull_33_0 = false;
                hashAgg_value_44 = hashAgg_value_45;
                continue;
            }
            boolean hashAgg_isNull_39 = hashAgg_unsafeRowAggBuffer_0.isNullAt(1);
            double hashAgg_value_50 = hashAgg_isNull_39 ? -1.0 : (hashAgg_unsafeRowAggBuffer_0.getDouble(1));
            if (!hashAgg_isNull_39) {
                hashAgg_hashAgg_isNull_33_0 = false;
                hashAgg_value_44 = hashAgg_value_50;
                continue;
            }
        } while (false);
        if (!hashAgg_hashAgg_isNull_33_0) {
            hashAgg_unsafeRowAggBuffer_0.setDouble(1, hashAgg_value_44);
        } else {
            hashAgg_unsafeRowAggBuffer_0.setNullAt(1);
        }
    }

    private void hashAgg_doAggregate_avg_0(org.apache.spark.sql.catalyst.InternalRow hashAgg_unsafeRowAggBuffer_0,
            double hashAgg_expr_0_0, boolean hashAgg_exprIsNull_0_0) throws java.io.IOException {
        boolean hashAgg_isNull_56 = true;
        double hashAgg_value_67 = -1.0;
        boolean hashAgg_isNull_57 = hashAgg_unsafeRowAggBuffer_0.isNullAt(4);
        double hashAgg_value_68 = hashAgg_isNull_57 ? -1.0 : (hashAgg_unsafeRowAggBuffer_0.getDouble(4));
        if (!hashAgg_isNull_57) {
            hashAgg_hashAgg_isNull_58_0 = true;
            double hashAgg_value_69 = -1.0;
            do {
                if (!hashAgg_exprIsNull_0_0) {
                    hashAgg_hashAgg_isNull_58_0 = false;
                    hashAgg_value_69 = hashAgg_expr_0_0;
                    continue;
                }
                if (!false) {
                    hashAgg_hashAgg_isNull_58_0 = false;
                    hashAgg_value_69 = 0.0D;
                    continue;
                }
            } while (false);
            hashAgg_isNull_56 = false; // resultCode could change nullability.
            hashAgg_value_67 = hashAgg_value_68 + hashAgg_value_69;
        }
        boolean hashAgg_isNull_61 = false;
        long hashAgg_value_72 = -1L;
        if (!false && hashAgg_exprIsNull_0_0) {
            boolean hashAgg_isNull_64 = hashAgg_unsafeRowAggBuffer_0.isNullAt(5);
            long hashAgg_value_75 = hashAgg_isNull_64 ? -1L : (hashAgg_unsafeRowAggBuffer_0.getLong(5));
            hashAgg_isNull_61 = hashAgg_isNull_64;
            hashAgg_value_72 = hashAgg_value_75;
        } else {
            boolean hashAgg_isNull_65 = true;
            long hashAgg_value_76 = -1L;
            boolean hashAgg_isNull_66 = hashAgg_unsafeRowAggBuffer_0.isNullAt(5);
            long hashAgg_value_77 = hashAgg_isNull_66 ? -1L : (hashAgg_unsafeRowAggBuffer_0.getLong(5));
            if (!hashAgg_isNull_66) {
                hashAgg_isNull_65 = false; // resultCode could change nullability.
                hashAgg_value_76 = hashAgg_value_77 + 1L;
            }
            hashAgg_isNull_61 = hashAgg_isNull_65;
            hashAgg_value_72 = hashAgg_value_76;
        }
        if (!hashAgg_isNull_56) {
            hashAgg_unsafeRowAggBuffer_0.setDouble(4, hashAgg_value_67);
        } else {
            hashAgg_unsafeRowAggBuffer_0.setNullAt(4);
        }
        if (!hashAgg_isNull_61) {
            hashAgg_unsafeRowAggBuffer_0.setLong(5, hashAgg_value_72);
        } else {
            hashAgg_unsafeRowAggBuffer_0.setNullAt(5);
        }
    }

    private void hashAgg_doAggregate_avg_3(double hashAgg_expr_6_0, boolean hashAgg_exprIsNull_6_0,
            long hashAgg_expr_7_0, org.apache.spark.sql.catalyst.InternalRow hashAgg_unsafeRowAggBuffer_1,
            boolean hashAgg_exprIsNull_7_0) throws java.io.IOException {
        boolean hashAgg_isNull_179 = true;
        double hashAgg_value_190 = -1.0;
        boolean hashAgg_isNull_180 = hashAgg_unsafeRowAggBuffer_1.isNullAt(4);
        double hashAgg_value_191 = hashAgg_isNull_180 ? -1.0 : (hashAgg_unsafeRowAggBuffer_1.getDouble(4));
        if (!hashAgg_isNull_180) {
            if (!hashAgg_exprIsNull_6_0) {
                hashAgg_isNull_179 = false; // resultCode could change nullability.
                hashAgg_value_190 = hashAgg_value_191 + hashAgg_expr_6_0;
            }
        }
        boolean hashAgg_isNull_182 = true;
        long hashAgg_value_193 = -1L;
        boolean hashAgg_isNull_183 = hashAgg_unsafeRowAggBuffer_1.isNullAt(5);
        long hashAgg_value_194 = hashAgg_isNull_183 ? -1L : (hashAgg_unsafeRowAggBuffer_1.getLong(5));
        if (!hashAgg_isNull_183) {
            if (!hashAgg_exprIsNull_7_0) {
                hashAgg_isNull_182 = false; // resultCode could change nullability.
                hashAgg_value_193 = hashAgg_value_194 + hashAgg_expr_7_0;
            }
        }
        if (!hashAgg_isNull_179) {
            hashAgg_unsafeRowAggBuffer_1.setDouble(4, hashAgg_value_190);
        } else {
            hashAgg_unsafeRowAggBuffer_1.setNullAt(4);
        }
        if (!hashAgg_isNull_182) {
            hashAgg_unsafeRowAggBuffer_1.setLong(5, hashAgg_value_193);
        } else {
            hashAgg_unsafeRowAggBuffer_1.setNullAt(5);
        }
    }

    private void hashAgg_doAggregateWithKeysOutput_1(UnsafeRow hashAgg_keyTerm_1, UnsafeRow hashAgg_bufferTerm_1)
            throws java.io.IOException {
        ((org.apache.spark.sql.execution.metric.SQLMetric) references[15] /* numOutputRows */).add(1);
        boolean hashAgg_isNull_200 = hashAgg_keyTerm_1.isNullAt(0);
        UTF8String hashAgg_value_211 = hashAgg_isNull_200 ? null : (hashAgg_keyTerm_1.getUTF8String(0));
        boolean hashAgg_isNull_201 = hashAgg_keyTerm_1.isNullAt(1);
        UTF8String hashAgg_value_212 = hashAgg_isNull_201 ? null : (hashAgg_keyTerm_1.getUTF8String(1));
        boolean hashAgg_isNull_202 = hashAgg_bufferTerm_1.isNullAt(0);
        double hashAgg_value_213 = hashAgg_isNull_202 ? -1.0 : (hashAgg_bufferTerm_1.getDouble(0));
        boolean hashAgg_isNull_203 = hashAgg_bufferTerm_1.isNullAt(1);
        double hashAgg_value_214 = hashAgg_isNull_203 ? -1.0 : (hashAgg_bufferTerm_1.getDouble(1));
        boolean hashAgg_isNull_204 = hashAgg_bufferTerm_1.isNullAt(2);
        double hashAgg_value_215 = hashAgg_isNull_204 ? -1.0 : (hashAgg_bufferTerm_1.getDouble(2));
        boolean hashAgg_isNull_205 = hashAgg_bufferTerm_1.isNullAt(3);
        double hashAgg_value_216 = hashAgg_isNull_205 ? -1.0 : (hashAgg_bufferTerm_1.getDouble(3));
        boolean hashAgg_isNull_206 = hashAgg_bufferTerm_1.isNullAt(4);
        double hashAgg_value_217 = hashAgg_isNull_206 ? -1.0 : (hashAgg_bufferTerm_1.getDouble(4));
        boolean hashAgg_isNull_207 = hashAgg_bufferTerm_1.isNullAt(5);
        long hashAgg_value_218 = hashAgg_isNull_207 ? -1L : (hashAgg_bufferTerm_1.getLong(5));
        boolean hashAgg_isNull_208 = hashAgg_bufferTerm_1.isNullAt(6);
        double hashAgg_value_219 = hashAgg_isNull_208 ? -1.0 : (hashAgg_bufferTerm_1.getDouble(6));
        boolean hashAgg_isNull_209 = hashAgg_bufferTerm_1.isNullAt(7);
        long hashAgg_value_220 = hashAgg_isNull_209 ? -1L : (hashAgg_bufferTerm_1.getLong(7));
        boolean hashAgg_isNull_210 = hashAgg_bufferTerm_1.isNullAt(8);
        double hashAgg_value_221 = hashAgg_isNull_210 ? -1.0 : (hashAgg_bufferTerm_1.getDouble(8));
        boolean hashAgg_isNull_211 = hashAgg_bufferTerm_1.isNullAt(9);
        long hashAgg_value_222 = hashAgg_isNull_211 ? -1L : (hashAgg_bufferTerm_1.getLong(9));
        long hashAgg_value_223 = hashAgg_bufferTerm_1.getLong(10);
        boolean hashAgg_isNull_219 = hashAgg_isNull_207;
        double hashAgg_value_230 = -1.0;
        if (!hashAgg_isNull_207) {
            hashAgg_value_230 = (double) hashAgg_value_218;
        }
        boolean hashAgg_isNull_217 = false;
        double hashAgg_value_228 = -1.0;
        if (hashAgg_isNull_219 || hashAgg_value_230 == 0) {
            hashAgg_isNull_217 = true;
        } else {
            if (hashAgg_isNull_206) {
                hashAgg_isNull_217 = true;
            } else {
                hashAgg_value_228 = (double) (hashAgg_value_217 / hashAgg_value_230);
            }
        }
        boolean hashAgg_isNull_223 = hashAgg_isNull_209;
        double hashAgg_value_234 = -1.0;
        if (!hashAgg_isNull_209) {
            hashAgg_value_234 = (double) hashAgg_value_220;
        }
        boolean hashAgg_isNull_221 = false;
        double hashAgg_value_232 = -1.0;
        if (hashAgg_isNull_223 || hashAgg_value_234 == 0) {
            hashAgg_isNull_221 = true;
        } else {
            if (hashAgg_isNull_208) {
                hashAgg_isNull_221 = true;
            } else {
                hashAgg_value_232 = (double) (hashAgg_value_219 / hashAgg_value_234);
            }
        }
        boolean hashAgg_isNull_227 = hashAgg_isNull_211;
        double hashAgg_value_238 = -1.0;
        if (!hashAgg_isNull_211) {
            hashAgg_value_238 = (double) hashAgg_value_222;
        }
        boolean hashAgg_isNull_225 = false;
        double hashAgg_value_236 = -1.0;
        if (hashAgg_isNull_227 || hashAgg_value_238 == 0) {
            hashAgg_isNull_225 = true;
        } else {
            if (hashAgg_isNull_210) {
                hashAgg_isNull_225 = true;
            } else {
                hashAgg_value_236 = (double) (hashAgg_value_221 / hashAgg_value_238);
            }
        }
        boolean hashAgg_isNull_232 = hashAgg_isNull_202;
        UTF8String hashAgg_value_243 = null;
        if (!hashAgg_isNull_202) {
            hashAgg_value_243 = UTF8String.fromString(String.valueOf(hashAgg_value_213));
        }
        boolean hashAgg_isNull_234 = hashAgg_isNull_203;
        UTF8String hashAgg_value_245 = null;
        if (!hashAgg_isNull_203) {
            hashAgg_value_245 = UTF8String.fromString(String.valueOf(hashAgg_value_214));
        }
        boolean hashAgg_isNull_236 = hashAgg_isNull_204;
        UTF8String hashAgg_value_247 = null;
        if (!hashAgg_isNull_204) {
            hashAgg_value_247 = UTF8String.fromString(String.valueOf(hashAgg_value_215));
        }
        boolean hashAgg_isNull_238 = hashAgg_isNull_205;
        UTF8String hashAgg_value_249 = null;
        if (!hashAgg_isNull_205) {
            hashAgg_value_249 = UTF8String.fromString(String.valueOf(hashAgg_value_216));
        }
        boolean hashAgg_isNull_240 = hashAgg_isNull_217;
        UTF8String hashAgg_value_251 = null;
        if (!hashAgg_isNull_217) {
            hashAgg_value_251 = UTF8String.fromString(String.valueOf(hashAgg_value_228));
        }
        boolean hashAgg_isNull_242 = hashAgg_isNull_221;
        UTF8String hashAgg_value_253 = null;
        if (!hashAgg_isNull_221) {
            hashAgg_value_253 = UTF8String.fromString(String.valueOf(hashAgg_value_232));
        }
        boolean hashAgg_isNull_244 = hashAgg_isNull_225;
        UTF8String hashAgg_value_255 = null;
        if (!hashAgg_isNull_225) {
            hashAgg_value_255 = UTF8String.fromString(String.valueOf(hashAgg_value_236));
        }
        boolean hashAgg_isNull_246 = false;
        UTF8String hashAgg_value_257 = null;
        if (!false) {
            hashAgg_value_257 = UTF8String.fromString(String.valueOf(hashAgg_value_223));
        }
        filter_mutableStateArray_0[7].reset();
        filter_mutableStateArray_0[7].zeroOutNullBytes();
        if (hashAgg_isNull_200) {
            filter_mutableStateArray_0[7].setNullAt(0);
        } else {
            filter_mutableStateArray_0[7].write(0, hashAgg_value_211);
        }
        if (hashAgg_isNull_201) {
            filter_mutableStateArray_0[7].setNullAt(1);
        } else {
            filter_mutableStateArray_0[7].write(1, hashAgg_value_212);
        }
        if (hashAgg_isNull_232) {
            filter_mutableStateArray_0[7].setNullAt(2);
        } else {
            filter_mutableStateArray_0[7].write(2, hashAgg_value_243);
        }
        if (hashAgg_isNull_234) {
            filter_mutableStateArray_0[7].setNullAt(3);
        } else {
            filter_mutableStateArray_0[7].write(3, hashAgg_value_245);
        }
        if (hashAgg_isNull_236) {
            filter_mutableStateArray_0[7].setNullAt(4);
        } else {
            filter_mutableStateArray_0[7].write(4, hashAgg_value_247);
        }
        if (hashAgg_isNull_238) {
            filter_mutableStateArray_0[7].setNullAt(5);
        } else {
            filter_mutableStateArray_0[7].write(5, hashAgg_value_249);
        }
        if (hashAgg_isNull_240) {
            filter_mutableStateArray_0[7].setNullAt(6);
        } else {
            filter_mutableStateArray_0[7].write(6, hashAgg_value_251);
        }
        if (hashAgg_isNull_242) {
            filter_mutableStateArray_0[7].setNullAt(7);
        } else {
            filter_mutableStateArray_0[7].write(7, hashAgg_value_253);
        }
        if (hashAgg_isNull_244) {
            filter_mutableStateArray_0[7].setNullAt(8);
        } else {
            filter_mutableStateArray_0[7].write(8, hashAgg_value_255);
        }
        filter_mutableStateArray_0[7].write(9, hashAgg_value_257);
        append((filter_mutableStateArray_0[7].getRow()));
    }

    private void hashAgg_doAggregate_count_1(org.apache.spark.sql.catalyst.InternalRow hashAgg_unsafeRowAggBuffer_1,
            long hashAgg_expr_12_0) throws java.io.IOException {
        long hashAgg_value_209 = hashAgg_unsafeRowAggBuffer_1.getLong(10);
        long hashAgg_value_208 = -1L;
        hashAgg_value_208 = hashAgg_value_209 + hashAgg_expr_12_0;
        hashAgg_unsafeRowAggBuffer_1.setLong(10, hashAgg_value_208);
    }

    private void hashAgg_doAggregate_sum_3(boolean hashAgg_exprIsNull_3_0, double hashAgg_expr_3_0,
            org.apache.spark.sql.catalyst.InternalRow hashAgg_unsafeRowAggBuffer_0, double hashAgg_value_32,
            boolean hashAgg_isNull_21) throws java.io.IOException {
        hashAgg_hashAgg_isNull_46_0 = true;
        double hashAgg_value_57 = -1.0;
        do {
            boolean hashAgg_isNull_47 = true;
            double hashAgg_value_58 = -1.0;
            hashAgg_hashAgg_isNull_48_0 = true;
            double hashAgg_value_59 = -1.0;
            do {
                boolean hashAgg_isNull_49 = hashAgg_unsafeRowAggBuffer_0.isNullAt(3);
                double hashAgg_value_60 = hashAgg_isNull_49 ? -1.0 : (hashAgg_unsafeRowAggBuffer_0.getDouble(3));
                if (!hashAgg_isNull_49) {
                    hashAgg_hashAgg_isNull_48_0 = false;
                    hashAgg_value_59 = hashAgg_value_60;
                    continue;
                }
                if (!false) {
                    hashAgg_hashAgg_isNull_48_0 = false;
                    hashAgg_value_59 = 0.0D;
                    continue;
                }
            } while (false);
            boolean hashAgg_isNull_51 = true;
            double hashAgg_value_62 = -1.0;
            if (!hashAgg_isNull_21) {
                boolean hashAgg_isNull_52 = true;
                double hashAgg_value_63 = -1.0;
                if (!hashAgg_exprIsNull_3_0) {
                    hashAgg_isNull_52 = false; // resultCode could change nullability.
                    hashAgg_value_63 = 1.0D + hashAgg_expr_3_0;
                }
                if (!hashAgg_isNull_52) {
                    hashAgg_isNull_51 = false; // resultCode could change nullability.
                    hashAgg_value_62 = hashAgg_value_32 * hashAgg_value_63;
                }
            }
            if (!hashAgg_isNull_51) {
                hashAgg_isNull_47 = false; // resultCode could change nullability.
                hashAgg_value_58 = hashAgg_value_59 + hashAgg_value_62;
            }
            if (!hashAgg_isNull_47) {
                hashAgg_hashAgg_isNull_46_0 = false;
                hashAgg_value_57 = hashAgg_value_58;
                continue;
            }
            boolean hashAgg_isNull_55 = hashAgg_unsafeRowAggBuffer_0.isNullAt(3);
            double hashAgg_value_66 = hashAgg_isNull_55 ? -1.0 : (hashAgg_unsafeRowAggBuffer_0.getDouble(3));
            if (!hashAgg_isNull_55) {
                hashAgg_hashAgg_isNull_46_0 = false;
                hashAgg_value_57 = hashAgg_value_66;
                continue;
            }
        } while (false);
        if (!hashAgg_hashAgg_isNull_46_0) {
            hashAgg_unsafeRowAggBuffer_0.setDouble(3, hashAgg_value_57);
        } else {
            hashAgg_unsafeRowAggBuffer_0.setNullAt(3);
        }
    }

    private void hashAgg_doAggregate_avg_2(double hashAgg_expr_2_0,
            org.apache.spark.sql.catalyst.InternalRow hashAgg_unsafeRowAggBuffer_0, boolean hashAgg_exprIsNull_2_0)
            throws java.io.IOException {
        boolean hashAgg_isNull_80 = true;
        double hashAgg_value_91 = -1.0;
        boolean hashAgg_isNull_81 = hashAgg_unsafeRowAggBuffer_0.isNullAt(8);
        double hashAgg_value_92 = hashAgg_isNull_81 ? -1.0 : (hashAgg_unsafeRowAggBuffer_0.getDouble(8));
        if (!hashAgg_isNull_81) {
            hashAgg_hashAgg_isNull_82_0 = true;
            double hashAgg_value_93 = -1.0;
            do {
                if (!hashAgg_exprIsNull_2_0) {
                    hashAgg_hashAgg_isNull_82_0 = false;
                    hashAgg_value_93 = hashAgg_expr_2_0;
                    continue;
                }
                if (!false) {
                    hashAgg_hashAgg_isNull_82_0 = false;
                    hashAgg_value_93 = 0.0D;
                    continue;
                }
            } while (false);
            hashAgg_isNull_80 = false; // resultCode could change nullability.
            hashAgg_value_91 = hashAgg_value_92 + hashAgg_value_93;
        }
        boolean hashAgg_isNull_85 = false;
        long hashAgg_value_96 = -1L;
        if (!false && hashAgg_exprIsNull_2_0) {
            boolean hashAgg_isNull_88 = hashAgg_unsafeRowAggBuffer_0.isNullAt(9);
            long hashAgg_value_99 = hashAgg_isNull_88 ? -1L : (hashAgg_unsafeRowAggBuffer_0.getLong(9));
            hashAgg_isNull_85 = hashAgg_isNull_88;
            hashAgg_value_96 = hashAgg_value_99;
        } else {
            boolean hashAgg_isNull_89 = true;
            long hashAgg_value_100 = -1L;
            boolean hashAgg_isNull_90 = hashAgg_unsafeRowAggBuffer_0.isNullAt(9);
            long hashAgg_value_101 = hashAgg_isNull_90 ? -1L : (hashAgg_unsafeRowAggBuffer_0.getLong(9));
            if (!hashAgg_isNull_90) {
                hashAgg_isNull_89 = false; // resultCode could change nullability.
                hashAgg_value_100 = hashAgg_value_101 + 1L;
            }
            hashAgg_isNull_85 = hashAgg_isNull_89;
            hashAgg_value_96 = hashAgg_value_100;
        }
        if (!hashAgg_isNull_80) {
            hashAgg_unsafeRowAggBuffer_0.setDouble(8, hashAgg_value_91);
        } else {
            hashAgg_unsafeRowAggBuffer_0.setNullAt(8);
        }

        if (!hashAgg_isNull_85) {
            hashAgg_unsafeRowAggBuffer_0.setLong(9, hashAgg_value_96);
        } else {
            hashAgg_unsafeRowAggBuffer_0.setNullAt(9);
        }
    }

    private void hashAgg_doAggregate_sum_6(org.apache.spark.sql.catalyst.InternalRow hashAgg_unsafeRowAggBuffer_1,
            boolean hashAgg_exprIsNull_4_1, double hashAgg_expr_4_1) throws java.io.IOException {
        hashAgg_hashAgg_isNull_165_0 = true;
        double hashAgg_value_176 = -1.0;
        do {
            boolean hashAgg_isNull_166 = true;
            double hashAgg_value_177 = -1.0;
            hashAgg_hashAgg_isNull_167_0 = true;
            double hashAgg_value_178 = -1.0;
            do {
                boolean hashAgg_isNull_168 = hashAgg_unsafeRowAggBuffer_1.isNullAt(2);
                double hashAgg_value_179 = hashAgg_isNull_168 ? -1.0 : (hashAgg_unsafeRowAggBuffer_1.getDouble(2));
                if (!hashAgg_isNull_168) {
                    hashAgg_hashAgg_isNull_167_0 = false;
                    hashAgg_value_178 = hashAgg_value_179;
                    continue;
                }

                if (!false) {
                    hashAgg_hashAgg_isNull_167_0 = false;
                    hashAgg_value_178 = 0.0D;
                    continue;
                }

            } while (false);

            if (!hashAgg_exprIsNull_4_1) {
                hashAgg_isNull_166 = false; // resultCode could change nullability.

                hashAgg_value_177 = hashAgg_value_178 + hashAgg_expr_4_1;

            }
            if (!hashAgg_isNull_166) {
                hashAgg_hashAgg_isNull_165_0 = false;
                hashAgg_value_176 = hashAgg_value_177;
                continue;
            }

            boolean hashAgg_isNull_171 = hashAgg_unsafeRowAggBuffer_1.isNullAt(2);
            double hashAgg_value_182 = hashAgg_isNull_171 ? -1.0 : (hashAgg_unsafeRowAggBuffer_1.getDouble(2));
            if (!hashAgg_isNull_171) {
                hashAgg_hashAgg_isNull_165_0 = false;
                hashAgg_value_176 = hashAgg_value_182;
                continue;
            }

        } while (false);

        if (!hashAgg_hashAgg_isNull_165_0) {
            hashAgg_unsafeRowAggBuffer_1.setDouble(2, hashAgg_value_176);
        } else {
            hashAgg_unsafeRowAggBuffer_1.setNullAt(2);
        }
    }

    private void hashAgg_doAggregate_avg_5(boolean hashAgg_exprIsNull_10_0, boolean hashAgg_exprIsNull_11_0,
            org.apache.spark.sql.catalyst.InternalRow hashAgg_unsafeRowAggBuffer_1, double hashAgg_expr_10_0,
            long hashAgg_expr_11_0) throws java.io.IOException {
        boolean hashAgg_isNull_191 = true;
        double hashAgg_value_202 = -1.0;
        boolean hashAgg_isNull_192 = hashAgg_unsafeRowAggBuffer_1.isNullAt(8);
        double hashAgg_value_203 = hashAgg_isNull_192 ? -1.0 : (hashAgg_unsafeRowAggBuffer_1.getDouble(8));
        if (!hashAgg_isNull_192) {
            if (!hashAgg_exprIsNull_10_0) {
                hashAgg_isNull_191 = false; // resultCode could change nullability.

                hashAgg_value_202 = hashAgg_value_203 + hashAgg_expr_10_0;

            }

        }
        boolean hashAgg_isNull_194 = true;
        long hashAgg_value_205 = -1L;
        boolean hashAgg_isNull_195 = hashAgg_unsafeRowAggBuffer_1.isNullAt(9);
        long hashAgg_value_206 = hashAgg_isNull_195 ? -1L : (hashAgg_unsafeRowAggBuffer_1.getLong(9));
        if (!hashAgg_isNull_195) {
            if (!hashAgg_exprIsNull_11_0) {
                hashAgg_isNull_194 = false; // resultCode could change nullability.

                hashAgg_value_205 = hashAgg_value_206 + hashAgg_expr_11_0;

            }

        }

        if (!hashAgg_isNull_191) {
            hashAgg_unsafeRowAggBuffer_1.setDouble(8, hashAgg_value_202);
        } else {
            hashAgg_unsafeRowAggBuffer_1.setNullAt(8);
        }

        if (!hashAgg_isNull_194) {
            hashAgg_unsafeRowAggBuffer_1.setLong(9, hashAgg_value_205);
        } else {
            hashAgg_unsafeRowAggBuffer_1.setNullAt(9);
        }
    }

    private void hashAgg_doAggregate_sum_0(org.apache.spark.sql.catalyst.InternalRow hashAgg_unsafeRowAggBuffer_0,
            double hashAgg_expr_0_0, boolean hashAgg_exprIsNull_0_0) throws java.io.IOException {
        hashAgg_hashAgg_isNull_26_0 = true;
        double hashAgg_value_37 = -1.0;
        do {
            boolean hashAgg_isNull_27 = true;
            double hashAgg_value_38 = -1.0;
            hashAgg_hashAgg_isNull_28_0 = true;
            double hashAgg_value_39 = -1.0;
            do {
                boolean hashAgg_isNull_29 = hashAgg_unsafeRowAggBuffer_0.isNullAt(0);
                double hashAgg_value_40 = hashAgg_isNull_29 ? -1.0 : (hashAgg_unsafeRowAggBuffer_0.getDouble(0));
                if (!hashAgg_isNull_29) {
                    hashAgg_hashAgg_isNull_28_0 = false;
                    hashAgg_value_39 = hashAgg_value_40;
                    continue;
                }

                if (!false) {
                    hashAgg_hashAgg_isNull_28_0 = false;
                    hashAgg_value_39 = 0.0D;
                    continue;
                }

            } while (false);

            if (!hashAgg_exprIsNull_0_0) {
                hashAgg_isNull_27 = false; // resultCode could change nullability.

                hashAgg_value_38 = hashAgg_value_39 + hashAgg_expr_0_0;

            }
            if (!hashAgg_isNull_27) {
                hashAgg_hashAgg_isNull_26_0 = false;
                hashAgg_value_37 = hashAgg_value_38;
                continue;
            }

            boolean hashAgg_isNull_32 = hashAgg_unsafeRowAggBuffer_0.isNullAt(0);
            double hashAgg_value_43 = hashAgg_isNull_32 ? -1.0 : (hashAgg_unsafeRowAggBuffer_0.getDouble(0));
            if (!hashAgg_isNull_32) {
                hashAgg_hashAgg_isNull_26_0 = false;
                hashAgg_value_37 = hashAgg_value_43;
                continue;
            }

        } while (false);

        if (!hashAgg_hashAgg_isNull_26_0) {
            hashAgg_unsafeRowAggBuffer_0.setDouble(0, hashAgg_value_37);
        } else {
            hashAgg_unsafeRowAggBuffer_0.setNullAt(0);
        }
    }

    private void hashAgg_doAggregate_count_0(org.apache.spark.sql.catalyst.InternalRow hashAgg_unsafeRowAggBuffer_0)
            throws java.io.IOException {
        long hashAgg_value_104 = hashAgg_unsafeRowAggBuffer_0.getLong(10);

        long hashAgg_value_103 = -1L;

        hashAgg_value_103 = hashAgg_value_104 + 1L;

        hashAgg_unsafeRowAggBuffer_0.setLong(10, hashAgg_value_103);
    }

    private void hashAgg_doAggregateWithKeysOutput_0(UnsafeRow hashAgg_keyTerm_0, UnsafeRow hashAgg_bufferTerm_0)
            throws java.io.IOException {
        ((org.apache.spark.sql.execution.metric.SQLMetric) references[13] /* numOutputRows */).add(1);

        boolean hashAgg_isNull_95 = hashAgg_keyTerm_0.isNullAt(0);
        UTF8String hashAgg_value_106 = hashAgg_isNull_95 ? null : (hashAgg_keyTerm_0.getUTF8String(0));
        boolean hashAgg_isNull_96 = hashAgg_keyTerm_0.isNullAt(1);
        UTF8String hashAgg_value_107 = hashAgg_isNull_96 ? null : (hashAgg_keyTerm_0.getUTF8String(1));
        boolean hashAgg_isNull_97 = hashAgg_bufferTerm_0.isNullAt(0);
        double hashAgg_value_108 = hashAgg_isNull_97 ? -1.0 : (hashAgg_bufferTerm_0.getDouble(0));
        boolean hashAgg_isNull_98 = hashAgg_bufferTerm_0.isNullAt(1);
        double hashAgg_value_109 = hashAgg_isNull_98 ? -1.0 : (hashAgg_bufferTerm_0.getDouble(1));
        boolean hashAgg_isNull_99 = hashAgg_bufferTerm_0.isNullAt(2);
        double hashAgg_value_110 = hashAgg_isNull_99 ? -1.0 : (hashAgg_bufferTerm_0.getDouble(2));
        boolean hashAgg_isNull_100 = hashAgg_bufferTerm_0.isNullAt(3);
        double hashAgg_value_111 = hashAgg_isNull_100 ? -1.0 : (hashAgg_bufferTerm_0.getDouble(3));
        boolean hashAgg_isNull_101 = hashAgg_bufferTerm_0.isNullAt(4);
        double hashAgg_value_112 = hashAgg_isNull_101 ? -1.0 : (hashAgg_bufferTerm_0.getDouble(4));
        boolean hashAgg_isNull_102 = hashAgg_bufferTerm_0.isNullAt(5);
        long hashAgg_value_113 = hashAgg_isNull_102 ? -1L : (hashAgg_bufferTerm_0.getLong(5));
        boolean hashAgg_isNull_103 = hashAgg_bufferTerm_0.isNullAt(6);
        double hashAgg_value_114 = hashAgg_isNull_103 ? -1.0 : (hashAgg_bufferTerm_0.getDouble(6));
        boolean hashAgg_isNull_104 = hashAgg_bufferTerm_0.isNullAt(7);
        long hashAgg_value_115 = hashAgg_isNull_104 ? -1L : (hashAgg_bufferTerm_0.getLong(7));
        boolean hashAgg_isNull_105 = hashAgg_bufferTerm_0.isNullAt(8);
        double hashAgg_value_116 = hashAgg_isNull_105 ? -1.0 : (hashAgg_bufferTerm_0.getDouble(8));
        boolean hashAgg_isNull_106 = hashAgg_bufferTerm_0.isNullAt(9);
        long hashAgg_value_117 = hashAgg_isNull_106 ? -1L : (hashAgg_bufferTerm_0.getLong(9));
        long hashAgg_value_118 = hashAgg_bufferTerm_0.getLong(10);

        hashAgg_doConsume_1(hashAgg_value_106, hashAgg_isNull_95, hashAgg_value_107, hashAgg_isNull_96,
                hashAgg_value_108, hashAgg_isNull_97, hashAgg_value_109, hashAgg_isNull_98, hashAgg_value_110,
                hashAgg_isNull_99, hashAgg_value_111, hashAgg_isNull_100, hashAgg_value_112, hashAgg_isNull_101,
                hashAgg_value_113, hashAgg_isNull_102, hashAgg_value_114, hashAgg_isNull_103, hashAgg_value_115,
                hashAgg_isNull_104, hashAgg_value_116, hashAgg_isNull_105, hashAgg_value_117, hashAgg_isNull_106,
                hashAgg_value_118);

    }

    private void hashAgg_doAggregate_sum_2(boolean hashAgg_isNull_21,
            org.apache.spark.sql.catalyst.InternalRow hashAgg_unsafeRowAggBuffer_0, double hashAgg_value_32)
            throws java.io.IOException {
        hashAgg_hashAgg_isNull_40_0 = true;
        double hashAgg_value_51 = -1.0;
        do {
            boolean hashAgg_isNull_41 = true;
            double hashAgg_value_52 = -1.0;
            hashAgg_hashAgg_isNull_42_0 = true;
            double hashAgg_value_53 = -1.0;
            do {
                boolean hashAgg_isNull_43 = hashAgg_unsafeRowAggBuffer_0.isNullAt(2);
                double hashAgg_value_54 = hashAgg_isNull_43 ? -1.0 : (hashAgg_unsafeRowAggBuffer_0.getDouble(2));
                if (!hashAgg_isNull_43) {
                    hashAgg_hashAgg_isNull_42_0 = false;
                    hashAgg_value_53 = hashAgg_value_54;
                    continue;
                }

                if (!false) {
                    hashAgg_hashAgg_isNull_42_0 = false;
                    hashAgg_value_53 = 0.0D;
                    continue;
                }

            } while (false);

            if (!hashAgg_isNull_21) {
                hashAgg_isNull_41 = false; // resultCode could change nullability.

                hashAgg_value_52 = hashAgg_value_53 + hashAgg_value_32;

            }
            if (!hashAgg_isNull_41) {
                hashAgg_hashAgg_isNull_40_0 = false;
                hashAgg_value_51 = hashAgg_value_52;
                continue;
            }

            boolean hashAgg_isNull_45 = hashAgg_unsafeRowAggBuffer_0.isNullAt(2);
            double hashAgg_value_56 = hashAgg_isNull_45 ? -1.0 : (hashAgg_unsafeRowAggBuffer_0.getDouble(2));
            if (!hashAgg_isNull_45) {
                hashAgg_hashAgg_isNull_40_0 = false;
                hashAgg_value_51 = hashAgg_value_56;
                continue;
            }

        } while (false);

        if (!hashAgg_hashAgg_isNull_40_0) {
            hashAgg_unsafeRowAggBuffer_0.setDouble(2, hashAgg_value_51);
        } else {
            hashAgg_unsafeRowAggBuffer_0.setNullAt(2);
        }
    }

    private void hashAgg_doAggregate_sum_5(boolean hashAgg_exprIsNull_3_1,
            org.apache.spark.sql.catalyst.InternalRow hashAgg_unsafeRowAggBuffer_1, double hashAgg_expr_3_1)
            throws java.io.IOException {
        hashAgg_hashAgg_isNull_158_0 = true;
        double hashAgg_value_169 = -1.0;
        do {
            boolean hashAgg_isNull_159 = true;
            double hashAgg_value_170 = -1.0;
            hashAgg_hashAgg_isNull_160_0 = true;
            double hashAgg_value_171 = -1.0;
            do {
                boolean hashAgg_isNull_161 = hashAgg_unsafeRowAggBuffer_1.isNullAt(1);
                double hashAgg_value_172 = hashAgg_isNull_161 ? -1.0 : (hashAgg_unsafeRowAggBuffer_1.getDouble(1));
                if (!hashAgg_isNull_161) {
                    hashAgg_hashAgg_isNull_160_0 = false;
                    hashAgg_value_171 = hashAgg_value_172;
                    continue;
                }

                if (!false) {
                    hashAgg_hashAgg_isNull_160_0 = false;
                    hashAgg_value_171 = 0.0D;
                    continue;
                }

            } while (false);

            if (!hashAgg_exprIsNull_3_1) {
                hashAgg_isNull_159 = false; // resultCode could change nullability.

                hashAgg_value_170 = hashAgg_value_171 + hashAgg_expr_3_1;

            }
            if (!hashAgg_isNull_159) {
                hashAgg_hashAgg_isNull_158_0 = false;
                hashAgg_value_169 = hashAgg_value_170;
                continue;
            }

            boolean hashAgg_isNull_164 = hashAgg_unsafeRowAggBuffer_1.isNullAt(1);
            double hashAgg_value_175 = hashAgg_isNull_164 ? -1.0 : (hashAgg_unsafeRowAggBuffer_1.getDouble(1));
            if (!hashAgg_isNull_164) {
                hashAgg_hashAgg_isNull_158_0 = false;
                hashAgg_value_169 = hashAgg_value_175;
                continue;
            }

        } while (false);

        if (!hashAgg_hashAgg_isNull_158_0) {
            hashAgg_unsafeRowAggBuffer_1.setDouble(1, hashAgg_value_169);
        } else {
            hashAgg_unsafeRowAggBuffer_1.setNullAt(1);
        }
    }

    private void hashAgg_doAggregate_avg_1(double hashAgg_expr_1_0, boolean hashAgg_exprIsNull_1_0,
            org.apache.spark.sql.catalyst.InternalRow hashAgg_unsafeRowAggBuffer_0) throws java.io.IOException {
        boolean hashAgg_isNull_68 = true;
        double hashAgg_value_79 = -1.0;
        boolean hashAgg_isNull_69 = hashAgg_unsafeRowAggBuffer_0.isNullAt(6);
        double hashAgg_value_80 = hashAgg_isNull_69 ? -1.0 : (hashAgg_unsafeRowAggBuffer_0.getDouble(6));
        if (!hashAgg_isNull_69) {
            hashAgg_hashAgg_isNull_70_0 = true;
            double hashAgg_value_81 = -1.0;
            do {
                if (!hashAgg_exprIsNull_1_0) {
                    hashAgg_hashAgg_isNull_70_0 = false;
                    hashAgg_value_81 = hashAgg_expr_1_0;
                    continue;
                }

                if (!false) {
                    hashAgg_hashAgg_isNull_70_0 = false;
                    hashAgg_value_81 = 0.0D;
                    continue;
                }

            } while (false);

            hashAgg_isNull_68 = false; // resultCode could change nullability.

            hashAgg_value_79 = hashAgg_value_80 + hashAgg_value_81;

        }
        boolean hashAgg_isNull_73 = false;
        long hashAgg_value_84 = -1L;
        if (!false && hashAgg_exprIsNull_1_0) {
            boolean hashAgg_isNull_76 = hashAgg_unsafeRowAggBuffer_0.isNullAt(7);
            long hashAgg_value_87 = hashAgg_isNull_76 ? -1L : (hashAgg_unsafeRowAggBuffer_0.getLong(7));
            hashAgg_isNull_73 = hashAgg_isNull_76;
            hashAgg_value_84 = hashAgg_value_87;
        } else {
            boolean hashAgg_isNull_77 = true;
            long hashAgg_value_88 = -1L;
            boolean hashAgg_isNull_78 = hashAgg_unsafeRowAggBuffer_0.isNullAt(7);
            long hashAgg_value_89 = hashAgg_isNull_78 ? -1L : (hashAgg_unsafeRowAggBuffer_0.getLong(7));
            if (!hashAgg_isNull_78) {
                hashAgg_isNull_77 = false; // resultCode could change nullability.

                hashAgg_value_88 = hashAgg_value_89 + 1L;

            }
            hashAgg_isNull_73 = hashAgg_isNull_77;
            hashAgg_value_84 = hashAgg_value_88;
        }

        if (!hashAgg_isNull_68) {
            hashAgg_unsafeRowAggBuffer_0.setDouble(6, hashAgg_value_79);
        } else {
            hashAgg_unsafeRowAggBuffer_0.setNullAt(6);
        }

        if (!hashAgg_isNull_73) {
            hashAgg_unsafeRowAggBuffer_0.setLong(7, hashAgg_value_84);
        } else {
            hashAgg_unsafeRowAggBuffer_0.setNullAt(7);
        }
    }

    private void hashAgg_doAggregate_avg_4(boolean hashAgg_exprIsNull_9_0, double hashAgg_expr_8_0,
            boolean hashAgg_exprIsNull_8_0, org.apache.spark.sql.catalyst.InternalRow hashAgg_unsafeRowAggBuffer_1,
            long hashAgg_expr_9_0) throws java.io.IOException {
        boolean hashAgg_isNull_185 = true;
        double hashAgg_value_196 = -1.0;
        boolean hashAgg_isNull_186 = hashAgg_unsafeRowAggBuffer_1.isNullAt(6);
        double hashAgg_value_197 = hashAgg_isNull_186 ? -1.0 : (hashAgg_unsafeRowAggBuffer_1.getDouble(6));
        if (!hashAgg_isNull_186) {
            if (!hashAgg_exprIsNull_8_0) {
                hashAgg_isNull_185 = false; // resultCode could change nullability.

                hashAgg_value_196 = hashAgg_value_197 + hashAgg_expr_8_0;

            }

        }
        boolean hashAgg_isNull_188 = true;
        long hashAgg_value_199 = -1L;
        boolean hashAgg_isNull_189 = hashAgg_unsafeRowAggBuffer_1.isNullAt(7);
        long hashAgg_value_200 = hashAgg_isNull_189 ? -1L : (hashAgg_unsafeRowAggBuffer_1.getLong(7));
        if (!hashAgg_isNull_189) {
            if (!hashAgg_exprIsNull_9_0) {
                hashAgg_isNull_188 = false; // resultCode could change nullability.

                hashAgg_value_199 = hashAgg_value_200 + hashAgg_expr_9_0;

            }

        }

        if (!hashAgg_isNull_185) {
            hashAgg_unsafeRowAggBuffer_1.setDouble(6, hashAgg_value_196);
        } else {
            hashAgg_unsafeRowAggBuffer_1.setNullAt(6);
        }

        if (!hashAgg_isNull_188) {
            hashAgg_unsafeRowAggBuffer_1.setLong(7, hashAgg_value_199);
        } else {
            hashAgg_unsafeRowAggBuffer_1.setNullAt(7);
        }
    }

}