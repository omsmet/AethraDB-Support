package MicrobenchmarkGenerators;

import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.vector.IntVector;
import org.apache.arrow.vector.VectorSchemaRoot;
import org.apache.arrow.vector.ipc.ArrowFileWriter;
import org.apache.arrow.vector.types.pojo.ArrowType;
import org.apache.arrow.vector.types.pojo.Field;
import org.apache.arrow.vector.types.pojo.FieldType;
import org.apache.arrow.vector.types.pojo.Schema;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import static java.util.Arrays.asList;

/**
 * Generates a row-based "table" consisting of five 32-bit integer columns in Little Endian format.
 * These columns all contain randomly generated integers from [0, Integer.MAX_VALUE) so that one
 * can easily choose a selectivity for a given column by setting the predicate appropriately.
 * That is, to obtain an overall selectivity of 20%, one should choose the predicate to be
 * < (Integer.MAX_VALUE / 5)
 */
public class IncreasingFilterCountGenerator {

    // We want to generate
    private static final long datasetSize = 4_026_531_840L;
    public static final long vectorLength = 16384L;

    private static final String datasetFolderPath = "/nvtmp/AethraTestData/increasing_filter_count_sf75/";

    public static void main(String[] args) throws IOException {
        String datasetName = datasetFolderPath + "increasing_filter_count_table.arrow";

        Random randomSource = new Random(301023);

        try (BufferAllocator rootAllocator = new RootAllocator()) {
            // Define the schema
            Field col1Field = new Field("col1", FieldType.notNullable(new ArrowType.Int(32, true)), null);
            Field col2Field = new Field("col2", FieldType.notNullable(new ArrowType.Int(32, true)), null);
            Field col3Field = new Field("col3", FieldType.notNullable(new ArrowType.Int(32, true)), null);
            Field col4Field = new Field("col4", FieldType.notNullable(new ArrowType.Int(32, true)), null);
            Field col5Field = new Field("col5", FieldType.notNullable(new ArrowType.Int(32, true)), null);

            Schema filterQuerySchema = new Schema(asList(col1Field, col2Field, col3Field, col4Field, col5Field));

            // Create the objects for writing the three different arrow files
            VectorSchemaRoot vectorSchemaRoot = VectorSchemaRoot.create(filterQuerySchema, rootAllocator);
            FileOutputStream outputStream = new FileOutputStream(datasetName);
            ArrowFileWriter arrowWriter = new ArrowFileWriter(vectorSchemaRoot, null, outputStream.getChannel());
            arrowWriter.start();

            // Initialise vectors for writing batches
            IntVector col1Vector = (IntVector) vectorSchemaRoot.getVector("col1");
            IntVector col2Vector = (IntVector) vectorSchemaRoot.getVector("col2");
            IntVector col3Vector = (IntVector) vectorSchemaRoot.getVector("col3");
            IntVector col4Vector = (IntVector) vectorSchemaRoot.getVector("col4");
            IntVector col5Vector = (IntVector) vectorSchemaRoot.getVector("col5");

            // Generate the batches and write them out as well
            long currentIndex = 0;
            while (currentIndex < datasetSize) {
                int trueVectorLength = (int) Math.min(vectorLength, datasetSize - currentIndex);

                col1Vector.reset();
                col2Vector.reset();
                col3Vector.reset();
                col4Vector.reset();
                col5Vector.reset();

                col1Vector.allocateNew(trueVectorLength);
                col2Vector.allocateNew(trueVectorLength);
                col3Vector.allocateNew(trueVectorLength);
                col4Vector.allocateNew(trueVectorLength);
                col5Vector.allocateNew(trueVectorLength);

                for (int i = 0; i < trueVectorLength; i++) {
                    col1Vector.set(i, randomSource.nextInt(Integer.MAX_VALUE));
                    col2Vector.set(i, randomSource.nextInt(Integer.MAX_VALUE));
                    col3Vector.set(i, randomSource.nextInt(Integer.MAX_VALUE));
                    col4Vector.set(i, randomSource.nextInt(Integer.MAX_VALUE));
                    col5Vector.set(i, randomSource.nextInt(Integer.MAX_VALUE));
                }

                col1Vector.setValueCount(trueVectorLength);
                col2Vector.setValueCount(trueVectorLength);
                col3Vector.setValueCount(trueVectorLength);
                col4Vector.setValueCount(trueVectorLength);
                col5Vector.setValueCount(trueVectorLength);

                vectorSchemaRoot.setRowCount(trueVectorLength);
                arrowWriter.writeBatch();
                currentIndex += trueVectorLength;
            }

            // Clean up
            arrowWriter.end();
            arrowWriter.close();
            outputStream.close();
            vectorSchemaRoot.close();
        }

        System.out.println("Finished generating data");
    }

}
