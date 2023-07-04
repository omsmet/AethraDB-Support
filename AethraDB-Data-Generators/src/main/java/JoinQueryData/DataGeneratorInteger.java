package JoinQueryData;

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
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static java.util.Arrays.asList;

/**
 * This data generator is aimed at producing datasets for executing the join query
 * SELECT COUNT(*) FROM A INNER JOIN B ON A.col1 = B.col1 INNER JOIN C ON A.col2 = C.col1
 *
 * In this query, all tables have a three column layout and the tables are generated in a way that
 * ensures that a given percentage of all records in a table matches the join condition on a certain
 * attribute. Additionally, all columns participating in the join conditions are generated such that
 * they do not have negative values.
 *
 * Specifically, the generation works as follows:
 *  - First, a table A is generated, which will be used for all queries
 *  - Next, different instances of table B are generated, such that the records of these table
 *      instances will have a join record in table A with probability 0.2, 0.4, 0.6, 0.8
 *  - Finally, different instances of table C are generated such that the records of these table
 *      instances will have a join record in table A with probability 0.2, 0.4, 0.6, 0.8
 *  - Now, the user can create dataset instances by pairing the files that follow from the cartesian
 *      product of these table instances.
 */
public class DataGeneratorInteger {

    /**
     * The folder into which the base datasets should be produced.
     */
    public static final String targetDirectoryPrefix = "/nvtmp/AethraTestData/join_query_int/";

    /**
     * The number of records per table.
     */
    public static final int datasetSize = 3 * 1024 * 1024;

    /**
     * Random source for generating the datasets.
     */
    private static final Random rg = new Random(280623);

    /**
     * The length of vectors in the resulting arrow files.
     */
    public static final int vectorLength = 16384;

    public static void main(String[] args) {
        int[][] tableA = generateTableA();
        writeTable(tableA, targetDirectoryPrefix + "table_A.arrow");

        double[] matchProbabilities = new double[] {
                0.2,
                0.4,
                0.6,
                0.8
        };

        for (double matchProbability : matchProbabilities) {
            int[][] tableBMatchProbability = generateTableB(tableA, matchProbability);
            int[][] tableCMatchProbability = generateTableC(tableA, matchProbability);

            writeTable(tableBMatchProbability, targetDirectoryPrefix + "table_B_" + matchProbability + ".arrow");
            writeTable(tableCMatchProbability, targetDirectoryPrefix + "table_C_" + matchProbability + ".arrow");
        }
    }

    /**
     * Method which generates table A.
     */
    public static int[][] generateTableA() {
        int[][] tableA = new int[datasetSize][3];

        for (int i = 0; i < tableA.length; i++) {
            tableA[i][0] = rg.nextInt(0, Integer.MAX_VALUE);    // A.col1 --> non-negative
            tableA[i][1] = rg.nextInt(0, Integer.MAX_VALUE);    // A.col2 --> non-negative
            tableA[i][2] = rg.nextInt();                              // A.col3 --> any value possible
        }

        return tableA;
    }

    /**
     * Method which generates a table B for a given table A and a match probability.
     * @param tableA The table A to use for the generation.
     * @param matchProbability The probability with which each record of the generated table
     *                         instance should have a join partner.
     */
    public static int[][] generateTableB(int[][] tableA, double matchProbability) {
        int[][] tableB = new int[datasetSize][3];

        Set<Integer> tableACol1 = new HashSet<>(tableA.length);
        for (int[] record : tableA) {
            tableACol1.add(record[0]);
        }

        for (int i = 0; i < tableB.length; i++) {
            // Coin flip to determine if this record should match
            boolean shouldMatch = rg.nextDouble() <= matchProbability;

            tableB[i][1] = rg.nextInt(); // B.col2 --> any value possible
            tableB[i][2] = rg.nextInt(); // B.col3 --> any value possible

            if (shouldMatch) {
                tableB[i][0] = tableA[rg.nextInt(tableA.length)][0];       // ON A.col1 = B.col1

            } else {
                do {
                    tableB[i][0] = rg.nextInt(0, Integer.MAX_VALUE); // B.col1 --> non-negative
                } while (tableACol1.contains(tableB[i][0]));

            }

        }

        return tableB;
    }

    /**
     * Method which generates a table C for a given table A and a match probability.
     * @param tableA The table A to use for the generation.
     * @param matchProbability The probability with which each record of the generated table
     *                         instance should have a join partner.
     */
    public static int[][] generateTableC(int[][] tableA, double matchProbability) {
        int[][] tableC = new int[datasetSize][3];

        Set<Integer> tableACol2 = new HashSet<>(tableA.length);
        for (int[] record : tableA) {
            tableACol2.add(record[1]);
        }

        for (int i = 0; i < tableC.length; i++) {
            // Coin flip to determine if this record should match
            boolean shouldMatch = rg.nextDouble() <= matchProbability;

            tableC[i][1] = rg.nextInt(); // C.col2 --> any value possible
            tableC[i][2] = rg.nextInt(); // C.col3 --> any value possible

            if (shouldMatch) {
                tableC[i][0] = tableA[rg.nextInt(tableA.length)][1];       // ON A.col2 = C.col1

            } else {
                do {
                    tableC[i][0] = rg.nextInt(0, Integer.MAX_VALUE); // C.col1 --> non-negative
                } while (tableACol2.contains(tableC[i][0]));

            }

        }

        return tableC;
    }

    /**
     * Method to write a three-column table to disk.
     * @param table The table to write.
     * @param file The path of the file to write to.
     */
    public static void writeTable(int[][] table, String file) {
        File targetFile = new File(file);
        if (targetFile.exists())
            throw new IllegalStateException("The table file " + file + " already exists");

        // Generate the records and store them in an arrow file
        // Get an allocator to allocate the vectors
        try (BufferAllocator allocator = new RootAllocator()) {
            // Define the schema
            Field col1Field = new Field("col1", FieldType.notNullable(new ArrowType.Int(32, true)), null);
            Field col2Field = new Field("col2", FieldType.notNullable(new ArrowType.Int(32, true)), null);
            Field col3Field = new Field("col3", FieldType.notNullable(new ArrowType.Int(32, true)), null);

            Schema aggregationQuerySchema = new Schema(asList(col1Field, col2Field, col3Field));

            // Write the arrow file to disk
            try (
                    VectorSchemaRoot root = VectorSchemaRoot.create(aggregationQuerySchema, allocator);
                    FileOutputStream fileOutputStream = new FileOutputStream(targetFile);
                    ArrowFileWriter writer = new ArrowFileWriter(root, null, fileOutputStream.getChannel())
            ) {
                // Initialise the writer
                writer.start();

                // Initialise the vectors for the batches
                IntVector col1Vector = (IntVector) root.getVector("col1");
                IntVector col2Vector = (IntVector) root.getVector("col2");
                IntVector col3Vector = (IntVector) root.getVector("col3");

                // Initialise the read pointer into the columns
                int currentPosition = 0;

                // Create the batches
                while (currentPosition < table.length) {
                    int trueVectorLength = Math.min(vectorLength, table.length - currentPosition);

                    col1Vector.reset();
                    col2Vector.reset();
                    col3Vector.reset();
                    col1Vector.allocateNew(trueVectorLength);
                    col2Vector.allocateNew(trueVectorLength);
                    col3Vector.allocateNew(trueVectorLength);

                    for (int i = 0; i < trueVectorLength; i++) {
                        // Set each column value
                        col1Vector.set(i, table[currentPosition + i][0]);
                        col2Vector.set(i, table[currentPosition + i][1]);
                        col3Vector.set(i, table[currentPosition + i][2]);
                    }

                    col1Vector.setValueCount(trueVectorLength);
                    col2Vector.setValueCount(trueVectorLength);
                    col3Vector.setValueCount(trueVectorLength);
                    root.setRowCount(trueVectorLength);

                    writer.writeBatch();

                    currentPosition += trueVectorLength;
                }

                writer.end();
            } catch (IOException e) {
                System.out.println("Could not successfully construct " + targetFile);
            }
        }
    }

}
