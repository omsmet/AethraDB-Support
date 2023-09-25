package FilterQueryData;

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
 * Generates a row-based "table" consisting of three 32-bit integer columns in Little Endian format.
 * These columns have the property that when they are selected over with the condition < 3000,
 * then each column will respectively induce a selectivity of 0.98, 0.98 and 0.02.
 */
public class FilterQueryDataGeneratorArrow {

    private static final double firstColumnSelectivity = 0.02;
    private static final double secondColumnSelectivity = 0.98;
    private static final double thirdColumnSelectivity = 0.98;

    private static final int selectionCondition = 3000;

    private static final long datasetSize = 100L * (3 * 1024 * 1024 * 10);
    public static final long vectorLength = 16384L;

    private static final String datasetFolderPath = "/nvtmp/AethraTestData/filter_query_int_sf100/";

    public static void main(String[] args) throws IOException {
        String col1RestrictiveFileName = datasetFolderPath + "arrow_col1_002_col2_098_col3_098/filter_query_table.arrow";
        String col2RestrictiveFileName = datasetFolderPath + "arrow_col1_098_col2_002_col3_098/filter_query_table.arrow";
        String col3RestrictiveFileName = datasetFolderPath + "arrow_col1_098_col2_098_col3_002/filter_query_table.arrow";

        long col1Selected = 0;
        long col2Selected = 0;
        long col3Selected = 0;
        Random randomSource = new Random(414243);

        try (BufferAllocator rootAllocator = new RootAllocator()) {
            // Define the schema
            Field col1Field = new Field("col1", FieldType.notNullable(new ArrowType.Int(32, true)), null);
            Field col2Field = new Field("col2", FieldType.notNullable(new ArrowType.Int(32, true)), null);
            Field col3Field = new Field("col3", FieldType.notNullable(new ArrowType.Int(32, true)), null);

            Schema filterQuerySchema = new Schema(asList(col1Field, col2Field, col3Field));

            // Create the objects for writing the three different arrow files
            VectorSchemaRoot col1RestrictiveRoot = VectorSchemaRoot.create(filterQuerySchema, rootAllocator);
            VectorSchemaRoot col2RestrictiveRoot = VectorSchemaRoot.create(filterQuerySchema, rootAllocator);
            VectorSchemaRoot col3RestrictiveRoot = VectorSchemaRoot.create(filterQuerySchema, rootAllocator);

            FileOutputStream col1RestrictiveOS = new FileOutputStream(col1RestrictiveFileName);
            FileOutputStream col2RestrictiveOS = new FileOutputStream(col2RestrictiveFileName);
            FileOutputStream col3RestrictiveOS = new FileOutputStream(col3RestrictiveFileName);

            ArrowFileWriter col1RestrictiveWriter = new ArrowFileWriter(col1RestrictiveRoot, null, col1RestrictiveOS.getChannel());
            ArrowFileWriter col2RestrictiveWriter = new ArrowFileWriter(col2RestrictiveRoot, null, col2RestrictiveOS.getChannel());
            ArrowFileWriter col3RestrictiveWriter = new ArrowFileWriter(col3RestrictiveRoot, null, col3RestrictiveOS.getChannel());

            col1RestrictiveWriter.start();
            col2RestrictiveWriter.start();
            col3RestrictiveWriter.start();

            // Initialise vectors for writing batches
            IntVector col1RestrictiveCol1 = (IntVector) col1RestrictiveRoot.getVector("col1");
            IntVector col1RestrictiveCol2 = (IntVector) col1RestrictiveRoot.getVector("col2");
            IntVector col1RestrictiveCol3 = (IntVector) col1RestrictiveRoot.getVector("col3");

            IntVector col2RestrictiveCol1 = (IntVector) col2RestrictiveRoot.getVector("col1");
            IntVector col2RestrictiveCol2 = (IntVector) col2RestrictiveRoot.getVector("col2");
            IntVector col2RestrictiveCol3 = (IntVector) col2RestrictiveRoot.getVector("col3");

            IntVector col3RestrictiveCol1 = (IntVector) col3RestrictiveRoot.getVector("col1");
            IntVector col3RestrictiveCol2 = (IntVector) col3RestrictiveRoot.getVector("col2");
            IntVector col3RestrictiveCol3 = (IntVector) col3RestrictiveRoot.getVector("col3");

            // Generate the batches and write them out as well
            long currentIndex = 0;
            while (currentIndex < datasetSize) {
                int trueVectorLength = (int) Math.min(vectorLength, datasetSize - currentIndex);

                col1RestrictiveCol1.reset();
                col1RestrictiveCol2.reset();
                col1RestrictiveCol3.reset();
                col2RestrictiveCol1.reset();
                col2RestrictiveCol2.reset();
                col2RestrictiveCol3.reset();
                col3RestrictiveCol1.reset();
                col3RestrictiveCol2.reset();
                col3RestrictiveCol3.reset();
                col1RestrictiveCol1.allocateNew(trueVectorLength);
                col1RestrictiveCol2.allocateNew(trueVectorLength);
                col1RestrictiveCol3.allocateNew(trueVectorLength);
                col2RestrictiveCol1.allocateNew(trueVectorLength);
                col2RestrictiveCol2.allocateNew(trueVectorLength);
                col2RestrictiveCol3.allocateNew(trueVectorLength);
                col3RestrictiveCol1.allocateNew(trueVectorLength);
                col3RestrictiveCol2.allocateNew(trueVectorLength);
                col3RestrictiveCol3.allocateNew(trueVectorLength);

                for (int i = 0; i < trueVectorLength; i++) {
                    int col1Val = generateNumberForPredicate(firstColumnSelectivity, selectionCondition, randomSource);
                    int col2Val = generateNumberForPredicate(secondColumnSelectivity, selectionCondition, randomSource);
                    int col3Val = generateNumberForPredicate(thirdColumnSelectivity, selectionCondition, randomSource);

                    col1Selected += (col1Val < selectionCondition) ? 1 : 0;
                    col2Selected += (col2Val < selectionCondition) ? 1 : 0;
                    col3Selected += (col3Val < selectionCondition) ? 1 : 0;

                    col1RestrictiveCol1.set(i, col1Val);
                    col1RestrictiveCol2.set(i, col2Val);
                    col1RestrictiveCol3.set(i, col3Val);

                    col2RestrictiveCol1.set(i, col2Val);
                    col2RestrictiveCol2.set(i, col1Val);
                    col2RestrictiveCol3.set(i, col3Val);

                    col3RestrictiveCol1.set(i, col2Val);
                    col3RestrictiveCol2.set(i, col3Val);
                    col3RestrictiveCol3.set(i, col1Val);
                }

                col1RestrictiveCol1.setValueCount(trueVectorLength);
                col1RestrictiveCol2.setValueCount(trueVectorLength);
                col1RestrictiveCol3.setValueCount(trueVectorLength);
                col2RestrictiveCol1.setValueCount(trueVectorLength);
                col2RestrictiveCol2.setValueCount(trueVectorLength);
                col2RestrictiveCol3.setValueCount(trueVectorLength);
                col3RestrictiveCol1.setValueCount(trueVectorLength);
                col3RestrictiveCol2.setValueCount(trueVectorLength);
                col3RestrictiveCol3.setValueCount(trueVectorLength);

                col1RestrictiveRoot.setRowCount(trueVectorLength);
                col2RestrictiveRoot.setRowCount(trueVectorLength);
                col3RestrictiveRoot.setRowCount(trueVectorLength);

                col1RestrictiveWriter.writeBatch();
                col2RestrictiveWriter.writeBatch();
                col3RestrictiveWriter.writeBatch();

                currentIndex += trueVectorLength;
            }

            // Clean up
            col1RestrictiveWriter.end();
            col2RestrictiveWriter.end();
            col3RestrictiveWriter.end();

            col1RestrictiveWriter.close();
            col2RestrictiveWriter.close();
            col3RestrictiveWriter.close();

            col1RestrictiveOS.close();
            col2RestrictiveOS.close();
            col3RestrictiveOS.close();

            col1RestrictiveRoot.close();
            col2RestrictiveRoot.close();
            col3RestrictiveRoot.close();
        }

        System.out.println("Finished generating data");
        double col1Selectivity = (double) col1Selected / datasetSize;
        double col2Selectivity = (double) col2Selected / datasetSize;
        double col3Selectivity = (double) col3Selected / datasetSize;
        System.out.println("Selectivity of columns: " + col1Selectivity + ", " + col2Selectivity + ", " + col3Selectivity);
    }

    private static int generateNumberForPredicate(double selectivity, double condition, Random randomSource) {
        double generatedNumber = (randomSource.nextDouble() * condition) / selectivity;
        return (int) generatedNumber;
    }

}
