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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import static java.util.Arrays.asList;

/**
 * Generates a row-based "table" consisting of some 32-bit integer columns in Little Endian format.
 * These columns all contain randomly generated integers from [0, Integer.MAX_VALUE) so that one
 * can easily choose a selectivity for a given column by setting the predicate appropriately.
 * That is, to obtain an overall selectivity of 20%, one should choose the predicate to be
 * < (Integer.MAX_VALUE / 5)
 */
public class IncreasingFilterCountGenerator {

    // We want to generate
    private static final int numberOfColumns = 100;
    private static final long datasetSize = 75L; // GB
    private static final long numberOfInts = (datasetSize * 1024L * 1024L * 1024L) / 4L;
    private static final long intsPerColumn = (long) Math.ceil(((double) numberOfInts) / numberOfColumns);
    public static final long vectorLength = 16384L;

    private static final String datasetFolderPath = "/nvtmp/AethraTestData/increasing_filter_count_" + numberOfColumns + "_cols_sf" + datasetSize  + "/";

    public static void main(String[] args) throws IOException {
        File datasetFolder = new File(datasetFolderPath);
        if (datasetFolder.exists()) {
            System.out.println("Dataset already exists, not regenerating it again");
            return;
        }

        boolean directoryCreated = datasetFolder.mkdir();
        if (!directoryCreated) {
            System.out.println("Could not create dataset directory");
            return;
        }

        String datasetName = datasetFolderPath + "increasing_filter_count_table.arrow";

        Random randomSource = new Random(301023);

        try (BufferAllocator rootAllocator = new RootAllocator()) {
            // Define the schema
            Field[] schemaFields = new Field[numberOfColumns];
            for (int i = 0; i < numberOfColumns; i++)
                schemaFields[i] = new Field("col" + i, FieldType.notNullable(new ArrowType.Int(32, true)), null);

            Schema filterQuerySchema = new Schema(Arrays.asList(schemaFields));

            // Create the objects for writing the three different arrow files
            VectorSchemaRoot vectorSchemaRoot = VectorSchemaRoot.create(filterQuerySchema, rootAllocator);
            FileOutputStream outputStream = new FileOutputStream(datasetName);
            ArrowFileWriter arrowWriter = new ArrowFileWriter(vectorSchemaRoot, null, outputStream.getChannel());
            arrowWriter.start();

            // Initialise vectors for writing batches
            IntVector[] colVectors = new IntVector[numberOfColumns];
            for (int i = 0; i < colVectors.length; i++)
                colVectors[i] = (IntVector) vectorSchemaRoot.getVector("col" + i);

            // Generate the batches and write them out as well
            long currentIndex = 0;
            while (currentIndex < intsPerColumn) {
                int trueVectorLength = (int) Math.min(vectorLength, intsPerColumn - currentIndex);

                for (int i = 0; i < numberOfColumns; i++) {
                    colVectors[i].reset();
                    colVectors[i].allocateNew(trueVectorLength);
                }

                for (int j = 0; j < trueVectorLength; j++) {
                    for (int i = 0; i < numberOfColumns; i++)
                        colVectors[i].set(j, randomSource.nextInt(Integer.MAX_VALUE));
                }

                for (int i = 0; i < numberOfColumns; i++) {
                    colVectors[i].setValueCount(trueVectorLength);
                }

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
