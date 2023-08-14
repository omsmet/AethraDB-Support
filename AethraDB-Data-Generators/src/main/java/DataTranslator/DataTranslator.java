package DataTranslator;

import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.vector.DateDayVector;
import org.apache.arrow.vector.DecimalVector;
import org.apache.arrow.vector.FieldVector;
import org.apache.arrow.vector.Float8Vector;
import org.apache.arrow.vector.IntVector;
import org.apache.arrow.vector.LargeVarCharVector;
import org.apache.arrow.vector.VectorSchemaRoot;
import org.apache.arrow.vector.ipc.ArrowFileReader;
import org.apache.arrow.vector.ipc.ArrowFileWriter;
import org.apache.arrow.vector.types.FloatingPointPrecision;
import org.apache.arrow.vector.types.pojo.ArrowType;
import org.apache.arrow.vector.types.pojo.Field;
import org.apache.arrow.vector.types.pojo.FieldType;
import org.apache.arrow.vector.types.pojo.Schema;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This data translator is aimed at taking in a given Arrow table file, and translating it to
 * another Arrow table file where some columns have been translated to a different data-type to make
 * the resulting table instance processable by the AethraDB engine.
 *
 * Currently, the translator performs the following translations:
 *  - It translates Decimal columns into Double columns
 */
public class DataTranslator {

    public static void main(String[] args) throws IOException {
        if (args.length != 2)
            throw new IllegalArgumentException("The data translator expects an input and output file path");

        File inputFile = new File(args[0]);
        File outputFile = new File(args[1]);

        if (!inputFile.exists())
            throw new IllegalArgumentException("The supplied input file does not exist");

        if (outputFile.exists())
            throw new IllegalArgumentException("The supplied output file path already exists");

        try (BufferAllocator allocator = new RootAllocator()) {
            // Get the information from the input table
            FileInputStream inputTableStream = new FileInputStream(inputFile);
            ArrowFileReader inputTableReader = new ArrowFileReader(inputTableStream.getChannel(), allocator);
            VectorSchemaRoot inputTableSchemaRoot = inputTableReader.getVectorSchemaRoot();

            // Translate the schema
            List<Field> translatedSchemaFields = new ArrayList<>();
            for (Field originalField : inputTableSchemaRoot.getSchema().getFields()) {

                FieldType originalFieldType = originalField.getFieldType();
                ArrowType originalArrowFieldType = originalFieldType.getType();
                ArrowType translatedArrowFieldType;

                if (originalField.getChildren().size() != 0)
                    throw new UnsupportedOperationException("The DataTranslator does not support nested types");

                // Translate the field based on its original type
                if (originalArrowFieldType instanceof ArrowType.Int originalIntField)
                    translatedArrowFieldType =
                            new ArrowType.Int(originalIntField.getBitWidth(), originalIntField.getIsSigned());

                else if (originalArrowFieldType instanceof ArrowType.Decimal originalDecimalField)
                    translatedArrowFieldType =
                            new ArrowType.FloatingPoint(FloatingPointPrecision.DOUBLE);

                else if (originalArrowFieldType instanceof ArrowType.LargeUtf8 originalUtf8Field)
                    translatedArrowFieldType = new ArrowType.LargeUtf8();

                else if (originalArrowFieldType instanceof ArrowType.Date originalDateField)
                    translatedArrowFieldType = new ArrowType.Date(originalDateField.getUnit());

                else
                    throw new UnsupportedOperationException("The original field type is not supported for translation: " + originalArrowFieldType);

                // Store the new field
                translatedSchemaFields.add(
                        new Field(
                                originalField.getName(),
                                originalField.isNullable() ? FieldType.nullable(translatedArrowFieldType) : FieldType.notNullable(translatedArrowFieldType),
                                null
                        )
                );

                System.out.println(
                        "Translated field '" + originalField.getName() + "' "
                        + "from " + (originalField.isNullable() ? "nullable " : "non-nullable ") + originalArrowFieldType
                        + " to " + (originalField.isNullable() ? "nullable " : "non-nullable ") + translatedArrowFieldType
                );

            }

            Schema translatedSchema = new Schema(translatedSchemaFields);

            // Translate each vector of the input file to an appropriately translated vector in the output file
            try (
                    VectorSchemaRoot outputTableSchemaRoot = VectorSchemaRoot.create(translatedSchema, allocator);
                    FileOutputStream outputTableStream = new FileOutputStream(outputFile);
                    ArrowFileWriter outputTableWriter = new ArrowFileWriter(outputTableSchemaRoot, null, outputTableStream.getChannel());
            ) {
                // Initialise the writer
                outputTableWriter.start();

                // Get references to output vectors
                List<FieldVector> outputVectors = outputTableSchemaRoot.getFieldVectors();

                // Loop over the input table vectors
                while (inputTableReader.loadNextBatch()) {
                    int vectorLength = inputTableSchemaRoot.getRowCount();

                    // Translate each input vector in the current batch
                    List<FieldVector> inputVectors = inputTableSchemaRoot.getFieldVectors();
                    for (int i = 0; i < inputVectors.size(); i++)
                        translateVector(inputVectors.get(i), outputVectors.get(i), vectorLength);

                    // Write the actual output batch
                    outputTableSchemaRoot.setRowCount(vectorLength);
                    outputTableWriter.writeBatch();

                }

                // End the writing
                outputTableWriter.end();
            }

            // Close the input objects
            inputTableSchemaRoot.close();
            inputTableReader.close();
            inputTableStream.close();
        }

    }

    private static void translateVector(FieldVector inputVector, FieldVector outputVector, int vectorLength) {
        // Start by initialising the outputVector
        outputVector.reset();

        // Now translate the actual input vector
        if (inputVector instanceof IntVector iiv && outputVector instanceof IntVector iov) {
            iov.allocateNew(vectorLength);
            for (int i = 0; i < vectorLength; i++)
                iov.set(i, iiv.get(i));

        } else if (inputVector instanceof DecimalVector div && outputVector instanceof Float8Vector f8ov) {
            f8ov.allocateNew(vectorLength);
            for (int i = 0; i < vectorLength; i++)
                f8ov.set(i, div.getObject(i).doubleValue());

        } else if (inputVector instanceof LargeVarCharVector lvciv && outputVector instanceof LargeVarCharVector lvcov) {
            lvcov.allocateNew(vectorLength);
            for (int i = 0; i < vectorLength; i++)
                lvcov.set(i, lvciv.get(i));

        } else if (inputVector instanceof DateDayVector ddiv && outputVector instanceof DateDayVector ddov) {
            ddov.allocateNew(vectorLength);
            for (int i = 0; i < vectorLength; i++)
                ddov.set(i, ddiv.get(i));

        } else {
            throw new UnsupportedOperationException("translateVector does not support this input-output vector type combination");
        }

        // Finish the output vector
        outputVector.setValueCount(vectorLength);
    }

}
