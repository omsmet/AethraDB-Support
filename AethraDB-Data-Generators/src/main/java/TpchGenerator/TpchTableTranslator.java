package TpchGenerator;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.vector.DateDayVector;
import org.apache.arrow.vector.FieldVector;
import org.apache.arrow.vector.FixedSizeBinaryVector;
import org.apache.arrow.vector.Float8Vector;
import org.apache.arrow.vector.IntVector;
import org.apache.arrow.vector.VarCharVector;
import org.apache.arrow.vector.VectorSchemaRoot;
import org.apache.arrow.vector.ipc.ArrowFileWriter;
import org.apache.arrow.vector.types.DateUnit;
import org.apache.arrow.vector.types.FloatingPointPrecision;
import org.apache.arrow.vector.types.Types;
import org.apache.arrow.vector.types.pojo.ArrowType;
import org.apache.arrow.vector.types.pojo.Field;
import org.apache.arrow.vector.types.pojo.FieldType;
import org.apache.arrow.vector.types.pojo.Schema;
import org.apache.commons.lang3.tuple.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class TpchTableTranslator {

    public static final int vectorLength = 16384;

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final LocalDateTime day_zero = LocalDate.parse("1970-01-01", dateTimeFormatter).atStartOfDay();

    public static void translateTable(String tableName, File targetDirectory, List<Pair<String, Types.MinorType>> tableLayout) throws Exception {
        File inputFile = new File(TpchGenerator.TPCH_DBGEN_DIR, tableName + ".tbl");
        File outputFile = new File(targetDirectory, tableName + ".arrow");

        // Create the arrow output file infrastructure
        BufferAllocator allocator = new RootAllocator();

        // Generate the schema
        Field[] schemaFields = new Field[tableLayout.size()];

        for (int i = 0; i < schemaFields.length; i++) {

            // Obtain the field definition from the table layout
            Pair<String, Types.MinorType> tableField = tableLayout.get(i);

            // Extract the field name by removing length indicators
            String fieldName = tableField.getLeft();
            int fixedLengthEncodingCharacter = fieldName.indexOf('#');
            int fixedLengthSize = 0;
            if (fixedLengthEncodingCharacter >= 0) {
                String[] fixedLengthInfo = fieldName.split("#");
                fixedLengthSize = Integer.parseInt(fixedLengthInfo[0]);
                fieldName = fixedLengthInfo[1];
            }

            // Now define the field based on the provided type
            schemaFields[i] = new Field(
                    fieldName,
                    FieldType.notNullable(switch (tableField.getRight()) {
                        case DECIMAL -> new ArrowType.FloatingPoint(FloatingPointPrecision.DOUBLE); // Convert decimal to double
                        case INT -> new ArrowType.Int(32, true);
                        case FIXEDSIZEBINARY -> new ArrowType.FixedSizeBinary(fixedLengthSize);
                        case VARCHAR -> new ArrowType.Utf8();
                        case DATEDAY -> new ArrowType.Date(DateUnit.DAY);
                        default -> throw new UnsupportedOperationException("The current field is not yet supported by the TpchTableTranslator: " + tableField.getRight());
                    }),
                    null);

        }

        Schema tableSchema = new Schema(Arrays.asList(schemaFields));
        VectorSchemaRoot tableSchemaRoot = VectorSchemaRoot.create(tableSchema, allocator);
        FileOutputStream tableOutputStream = new FileOutputStream(outputFile);
        ArrowFileWriter tableWriter = new ArrowFileWriter(tableSchemaRoot, null, tableOutputStream.getChannel());
        tableWriter.start();

        // Now, prepare for reading the input file
        // Get the number of lines first
        LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(inputFile));
        lineNumberReader.skip(Long.MAX_VALUE);
        int numberOfLines = lineNumberReader.getLineNumber();
        lineNumberReader.close();

        // Prepare the CSV parser and input file reader
        CSVParser parser = new CSVParserBuilder().withSeparator('|').build();
        BufferedReader inputFileReader = new BufferedReader(new FileReader(inputFile));

        // Now, convert the input file into an arrow table
        int currentPosition = 0;
        while (currentPosition < numberOfLines) {

            // Compute the length of the current output vector
            int trueVectorLength = Math.min(vectorLength, numberOfLines - currentPosition);

            // Reset and allocate the new vectors
            for (int i = 0; i < tableLayout.size(); i++) {
                Pair<String, Types.MinorType> tableField = tableLayout.get(i);
                FieldVector rawVector = tableSchemaRoot.getVector(i);

                switch (tableField.getRight()) {
                    case DECIMAL -> { // Convert decimal to double
                        Float8Vector vector = ((Float8Vector) rawVector);
                        vector.reset();
                        vector.allocateNew(trueVectorLength);
                    }

                    case INT -> {
                        IntVector vector = ((IntVector) rawVector);
                        vector.reset();
                        vector.allocateNew(trueVectorLength);
                    }

                    case FIXEDSIZEBINARY -> {
                        FixedSizeBinaryVector vector = ((FixedSizeBinaryVector) rawVector);
                        vector.reset();
                        vector.allocateNew(trueVectorLength);
                    }

                    case VARCHAR -> {
                        VarCharVector vector = ((VarCharVector) rawVector);
                        vector.reset();
                        vector.allocateNew(trueVectorLength);
                    }

                    case DATEDAY -> {
                        DateDayVector vector = ((DateDayVector) rawVector);
                        vector.reset();
                        vector.allocateNew(trueVectorLength);
                    }

                    default -> throw new UnsupportedOperationException("The current field is not yet supported by the TpchTableTranslator: " + tableField.getRight());
                }
            }

            // Now, write the values to the appropriate vectors

            for (int v = 0; v < trueVectorLength; v++) {
                String line = inputFileReader.readLine();
                String[] currentValue = parser.parseLine(line);

                for (int i = 0; i < tableLayout.size(); i++) {
                    String rawValue = currentValue[i];

                    // Deal with possible conversion
                    Pair<String, Types.MinorType> tableField = tableLayout.get(i);
                    String fieldName = tableField.getLeft();

                    // Get the raw vector
                    FieldVector rawVector = tableSchemaRoot.getVector(i);

                    switch (tableField.getRight()) {
                        case DECIMAL -> {   // Convert decimal to double
                            Float8Vector vector = ((Float8Vector) rawVector);
                            vector.set(v, Double.parseDouble(rawValue));

                        }

                        case INT -> {
                            IntVector vector = ((IntVector) rawVector);
                            vector.set(v, Integer.parseInt(rawValue));

                        }

                        case FIXEDSIZEBINARY -> {
                            FixedSizeBinaryVector vector = ((FixedSizeBinaryVector) rawVector);

                            // Need to upgrade size
                            byte[] buffer = new byte[vector.getByteWidth()];
                            Arrays.fill(buffer, (byte) 32); // Pad with trailing spaces
                            byte[] rawValueAsBytes = rawValue.getBytes(StandardCharsets.US_ASCII);
                            System.arraycopy(rawValueAsBytes, 0, buffer, 0, rawValueAsBytes.length);

                            vector.set(v, buffer);
                        }

                        case VARCHAR -> {
                            VarCharVector vector = ((VarCharVector) rawVector);
                            vector.setSafe(v, rawValue.getBytes(StandardCharsets.UTF_8));
                        }

                        case DATEDAY -> {
                            DateDayVector vector = ((DateDayVector) rawVector);
                            // Compute "unix day": days since 1 Januari 1970
                            LocalDateTime parsedDate = LocalDate.parse(rawValue, dateTimeFormatter).atStartOfDay();
                            int unixDays = (int) Duration.between(day_zero, parsedDate).toDays();

                            vector.set(v, unixDays);
                        }

                        default -> throw new UnsupportedOperationException("The current field is not yet supported by the TpchTableTranslator: " + tableField.getRight());
                    }

                }

            }

            // Set the value count on each vector and the schema root
            for (int i = 0; i < tableLayout.size(); i++) {
                Pair<String, Types.MinorType> tableField = tableLayout.get(i);
                FieldVector rawVector = tableSchemaRoot.getVector(i);

                switch (tableField.getRight()) {
                    case DECIMAL -> { // Convert decimal to double
                        Float8Vector vector = ((Float8Vector) rawVector);
                        vector.setValueCount(trueVectorLength);
                    }

                    case INT -> {
                        IntVector vector = ((IntVector) rawVector);
                        vector.setValueCount(trueVectorLength);
                    }

                    case FIXEDSIZEBINARY -> {
                        FixedSizeBinaryVector vector = ((FixedSizeBinaryVector) rawVector);
                        vector.setValueCount(trueVectorLength);
                    }

                    case VARCHAR -> {
                        VarCharVector vector = ((VarCharVector) rawVector);
                        vector.setValueCount(trueVectorLength);
                    }

                    case DATEDAY -> {
                        DateDayVector vector = ((DateDayVector) rawVector);
                        vector.setValueCount(trueVectorLength);
                    }

                    default -> throw new UnsupportedOperationException("The current field is not yet supported by the TpchTableTranslator: " + tableField.getRight());
                }
            }
            tableSchemaRoot.setRowCount(trueVectorLength);

            // Write the current arrow batch to disk
            tableWriter.writeBatch();

            // Mark the part of the input file which has now been processed
            currentPosition += trueVectorLength;

        }

        // Close all working objects
        inputFileReader.close();
        tableWriter.end();
        tableWriter.close();
        tableOutputStream.close();
        tableSchemaRoot.close();
        allocator.close();
    }

}
