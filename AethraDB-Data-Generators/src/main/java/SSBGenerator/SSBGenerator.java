package SSBGenerator;

import org.apache.arrow.vector.types.Types;
import org.apache.commons.lang3.tuple.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;

import static org.apache.arrow.vector.types.Types.MinorType.DATEDAY;
import static org.apache.arrow.vector.types.Types.MinorType.DECIMAL;
import static org.apache.arrow.vector.types.Types.MinorType.FIXEDSIZEBINARY;
import static org.apache.arrow.vector.types.Types.MinorType.INT;
import static org.apache.arrow.vector.types.Types.MinorType.VARCHAR;

public class SSBGenerator {

    public static File SSB_DBGEN_DIR = new File("/nvtmp/Star Schema Benchmark - dbgen/");

    public static void main(String[] args) throws Exception {
        // Extract the scale factor
        if (args.length != 1) {
            throw new IllegalArgumentException("Please supply the SSB Scale Factor to use ...");
        }

        int scaleFactor = Integer.parseInt(args[0]);
        System.out.println("Generating an AethraDB Arrow file for SSB at Scale-Factor " + scaleFactor + " ...");

        String containingFolderPath = "/nvtmp/AethraTestData/SSB/";
        String targetFolderPath = containingFolderPath + "sf-" + scaleFactor + "/";

        File containingFolder = new File(containingFolderPath);
        File targetFolder = new File(targetFolderPath);

        System.out.println("Output directory will be " + targetFolderPath);

        if (!containingFolder.exists())
            throw new IllegalStateException("Please ensure that the containing folder " + containingFolderPath + " exists ...");
        else if (targetFolder.exists()) {
            boolean didDelete = targetFolder.delete();
            if (!didDelete)
                throw new IllegalStateException("Could not delete the target folder " + targetFolderPath + " to obtain a clean state ...");
        }

        boolean createdTargetFolder = targetFolder.mkdir();
        if (!createdTargetFolder)
            throw new IllegalStateException("Could not create the target folder " + targetFolderPath);

        // Generate the TPC-H dataset using the official SSB generator
        File dbgenExecutable = new File(SSB_DBGEN_DIR, "dbgen");
        if (!dbgenExecutable.exists())
            throw new IllegalStateException("Could not locate the SSB generator executable ...");

        Runtime runtime = Runtime.getRuntime();
        String[] generateCommand = new String[] { dbgenExecutable.toString(), "-vf", "-s", Integer.toString(scaleFactor), "-T", "a" };
        String[] generateArgs = new String[] {};

        System.out.println("Generating Official SSB Dataset ...");
        System.out.println("---");
        try {
            Process generationProcess = runtime.exec(generateCommand, generateArgs, SSB_DBGEN_DIR);

            BufferedReader input = new BufferedReader(new InputStreamReader(generationProcess.getErrorStream()));

            String line = null;

            while ((line = input.readLine()) != null)
            {
                System.out.println(line);
            }

            int exitVal = generationProcess.waitFor();
            System.out.println("---");
            System.out.println("Finished Generating Official SSB Dataset!");
            System.out.println("Exited with error code " + exitVal);

            if (exitVal != 0) {
                System.out.println("Could not successfully generate official SSB dataset");
                return;
            }

            System.out.println("Official SSB Dataset Generation Successful!");

        } catch (Exception e) {
            throw new RuntimeException("Could not successfully generate the SSB dataset: ", e);
        }

        // Now translate each SSB table to an appropriate arrow file
        SSBTableTranslator.translateTable("lineorder", targetFolder, List.of(
                Pair.of("lo_orderkey", INT),
                Pair.of("lo_linenumber", INT),                                  // SPEC: numeric 1-7 (but we support no numeric smaller than int)
                Pair.of("lo_custkey", INT),
                Pair.of("lo_partkey", INT),
                Pair.of("lo_suppkey", INT),
                Pair.of("lo_orderdate", INT),
                Pair.of("15#lo_orderpriority", Types.MinorType.FIXEDSIZEBINARY),// SPEC: fixed text 15
                Pair.of("1#lo_shippriority", Types.MinorType.FIXEDSIZEBINARY),  // SPEC: fixed text 1
                Pair.of("lo_quantity", INT),                                    // SPEC: numeric 1-50 (but we support no numeric smaller than int)
                Pair.of("lo_extendedprice", INT),                               // SPEC: numeric <= 55450
                Pair.of("lo_ordtotalprice", INT),                               // SPEC: numeric <= 388000
                Pair.of("lo_discount", INT),                                    // SPEC: numeric 0-10
                Pair.of("lo_revenue", INT),                                     // SPEC: numeric
                Pair.of("lo_supplycost", INT),                                  // SPEC: numeric
                Pair.of("lo_tax", INT),                                         // SPEC: numeric 0-8
                Pair.of("lo_commitdate", INT),
                Pair.of("10#lo_shipmode", Types.MinorType.FIXEDSIZEBINARY)      // SPEC: fixed text 10
        ));

        SSBTableTranslator.translateTable("part", targetFolder, List.of(
                Pair.of("p_partkey", INT),
                Pair.of("p_name", Types.MinorType.VARCHAR),                     // SPEC: var text 22
                Pair.of("6#p_mfgr", Types.MinorType.FIXEDSIZEBINARY),           // SPEC: fixed text 6
                Pair.of("7#p_category", Types.MinorType.FIXEDSIZEBINARY),       // SPEC: fixed text 7
                Pair.of("9#p_brand1", Types.MinorType.FIXEDSIZEBINARY),         // SPEC: fixed text 9
                Pair.of("p_color", Types.MinorType.VARCHAR),                    // SPEC: var text 11
                Pair.of("p_type", Types.MinorType.VARCHAR),                     // SPEC: var text 25
                Pair.of("p_size", INT),                                         // SPEC: numeric 1-50 (but we support no numeric smaller than int)
                Pair.of("10#p_container", Types.MinorType.FIXEDSIZEBINARY)      // SPEC: fixed text 10
        ));

        SSBTableTranslator.translateTable("supplier", targetFolder, List.of(
                Pair.of("s_suppkey", INT),
                Pair.of("25#s_name", Types.MinorType.FIXEDSIZEBINARY),
                Pair.of("s_address", Types.MinorType.VARCHAR),
                Pair.of("10#s_city", Types.MinorType.FIXEDSIZEBINARY),
                Pair.of("15#s_nation", Types.MinorType.FIXEDSIZEBINARY),
                Pair.of("12#s_region", Types.MinorType.FIXEDSIZEBINARY),
                Pair.of("15#s_phone", Types.MinorType.FIXEDSIZEBINARY)
        ));

        SSBTableTranslator.translateTable("customer", targetFolder, List.of(
                Pair.of("c_custkey", INT),
                Pair.of("25#c_name", Types.MinorType.FIXEDSIZEBINARY),
                Pair.of("c_address", Types.MinorType.VARCHAR),
                Pair.of( "10#c_city", Types.MinorType.FIXEDSIZEBINARY),
                Pair.of( "15#c_nation", Types.MinorType.FIXEDSIZEBINARY),
                Pair.of( "12#c_region", Types.MinorType.FIXEDSIZEBINARY),
                Pair.of("15#c_phone", Types.MinorType.FIXEDSIZEBINARY),
                Pair.of("10#c_mktsegment", Types.MinorType.FIXEDSIZEBINARY)
        ));

        SSBTableTranslator.translateTable("date", targetFolder, List.of(
                Pair.of("d_datekey", INT),
                Pair.of("18#d_date", FIXEDSIZEBINARY),
                Pair.of("9#d_dayofweek", FIXEDSIZEBINARY),
                Pair.of("9#d_month", FIXEDSIZEBINARY),
                Pair.of("d_year", INT),
                Pair.of("d_yearmonthnum", INT),
                Pair.of("7#d_yearmonth", FIXEDSIZEBINARY),
                Pair.of("d_daynuminweek", INT),
                Pair.of("d_daynuminmonth", INT),
                Pair.of("d_daynuminyear", INT),
                Pair.of("d_monthnuminyear", INT),
                Pair.of("d_weeknuminyear", INT),
                Pair.of("12#d_sellingseason", FIXEDSIZEBINARY),
                Pair.of("1#d_lastdayinweekfl", FIXEDSIZEBINARY),
                Pair.of("1#d_lastdayinmonthfl", FIXEDSIZEBINARY),
                Pair.of("1#d_holidayfl", FIXEDSIZEBINARY),
                Pair.of("1#d_weekdayfl", FIXEDSIZEBINARY)
        ));

    }

}
