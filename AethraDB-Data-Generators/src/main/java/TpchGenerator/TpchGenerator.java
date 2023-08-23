package TpchGenerator;

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

public class TpchGenerator {

    public static File TPCH_DBGEN_DIR = new File("/nvtmp/TPC-H V3.0.1/dbgen/");

    public static void main(String[] args) throws Exception {
        // Extract the scale factor
        if (args.length != 1) {
            throw new IllegalArgumentException("Please supply the TPC-H Scale Factor to use ...");
        }

        int scaleFactor = Integer.parseInt(args[0]);
        System.out.println("Generating an AethraDB Arrow file for TPC-H at Scale-Factor " + scaleFactor + " ...");

        String containingFolderPath = "/nvtmp/AethraTestData/tpch/";
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

        // Generate the TPC-H dataset using the official TPC-H generator
        File dbgenExecutable = new File(TPCH_DBGEN_DIR, "dbgen");
        if (!dbgenExecutable.exists())
            throw new IllegalStateException("Could not locate the TPC-H generator executable ...");

        Runtime runtime = Runtime.getRuntime();
        String[] generateCommand = new String[] { dbgenExecutable.toString(), "-vf", "-s", Integer.toString(scaleFactor) };
        String[] generateArgs = new String[] {};

        System.out.println("Generating Official TPC-H Dataset ...");
        System.out.println("---");
        try {
            Process generationProcess = runtime.exec(generateCommand, generateArgs, TPCH_DBGEN_DIR);

            BufferedReader input = new BufferedReader(new InputStreamReader(generationProcess.getErrorStream()));

            String line = null;

            while ((line = input.readLine()) != null)
            {
                System.out.println(line);
            }

            int exitVal = generationProcess.waitFor();
            System.out.println("---");
            System.out.println("Finished Generating Official TPC-H Dataset!");
            System.out.println("Exited with error code " + exitVal);

            if (exitVal != 0) {
                System.out.println("Could not successfully generate official TPC-H dataset");
                return;
            }

            System.out.println("Official TPC-H Dataset Generation Successful!");

        } catch (Exception e) {
            throw new RuntimeException("Could not successfully generate the TPC-H dataset: ", e);
        }

        // Now translate each TPC-H table to an appropriate arrow file
        TpchTableTranslator.translateTable("part", targetFolder, List.of(
                Pair.of("p_partkey", INT),
                Pair.of("p_name", Types.MinorType.VARCHAR),                     // SPEC: var text 55
                Pair.of("25#p_mfgr", Types.MinorType.FIXEDSIZEBINARY),          // SPEC: fixed text 25
                Pair.of("10#p_brand", Types.MinorType.FIXEDSIZEBINARY),         // SPEC: fixed text 10
                Pair.of("p_type", Types.MinorType.VARCHAR),                     // SPEC: var text 25
                Pair.of("p_size", INT),
                Pair.of("10#p_container", Types.MinorType.FIXEDSIZEBINARY),     // SPEC: fixed text 10
                Pair.of("p_retailprice", DECIMAL),                              // SPEC: decimal, we multiply x 100 to get integers
                Pair.of("p_comment", Types.MinorType.VARCHAR)                   // SPEC: var text 23
        ));

        TpchTableTranslator.translateTable("supplier", targetFolder, List.of(
                Pair.of("s_suppkey", INT),
                Pair.of("25#s_name", Types.MinorType.FIXEDSIZEBINARY),
                Pair.of("s_address", Types.MinorType.VARCHAR),
                Pair.of("s_nationkey", INT),
                Pair.of("15#s_phone", Types.MinorType.FIXEDSIZEBINARY),
                Pair.of("s_acctbal", DECIMAL),
                Pair.of("s_comment", Types.MinorType.VARCHAR)
        ));

        TpchTableTranslator.translateTable("partsupp", targetFolder, List.of(
                Pair.of("ps_partkey", INT),
                Pair.of("ps_suppkey", INT),
                Pair.of("ps_availqty", INT),
                Pair.of("ps_supplycost", DECIMAL),
                Pair.of("ps_comment", Types.MinorType.VARCHAR)
        ));

        TpchTableTranslator.translateTable("customer", targetFolder, List.of(
                Pair.of("c_custkey", INT),
                Pair.of("c_name", Types.MinorType.VARCHAR),
                Pair.of("c_address", Types.MinorType.VARCHAR),
                Pair.of( "c_nationkey", INT),
                Pair.of("15#c_phone", Types.MinorType.FIXEDSIZEBINARY),
                Pair.of("c_acctbal", DECIMAL),
                Pair.of("10#c_mktsegment", Types.MinorType.FIXEDSIZEBINARY),
                Pair.of("c_comment", Types.MinorType.VARCHAR)
        ));

        TpchTableTranslator.translateTable("orders", targetFolder, List.of(
                Pair.of("o_orderkey", INT),
                Pair.of("o_custkey", INT),
                Pair.of("1#o_orderstatus", Types.MinorType.FIXEDSIZEBINARY),
                Pair.of("o_totalprice", DECIMAL),
                Pair.of("o_orderdate", Types.MinorType.DATEDAY),
                Pair.of("15#o_orderpriority", Types.MinorType.FIXEDSIZEBINARY),
                Pair.of("15#o_clerk", Types.MinorType.FIXEDSIZEBINARY),
                Pair.of("o_shippriority", INT),
                Pair.of("o_comment", Types.MinorType.VARCHAR)
        ));

        TpchTableTranslator.translateTable("lineitem", targetFolder, List.of(
                Pair.of("l_orderkey", INT),
                Pair.of("l_partkey", INT),
                Pair.of("l_suppkey", INT),
                Pair.of("l_linenumber", INT),
                Pair.of("l_quantity", DECIMAL),
                Pair.of("l_extendedprice", DECIMAL),
                Pair.of("l_discount", DECIMAL),
                Pair.of("l_tax", DECIMAL),
                Pair.of("1#l_returnflag", FIXEDSIZEBINARY),
                Pair.of("1#l_linestatus", FIXEDSIZEBINARY),
                Pair.of("l_shipdate", DATEDAY),
                Pair.of("l_commitdate", DATEDAY),
                Pair.of("l_receiptdate", DATEDAY),
                Pair.of("25#l_shipinstruct", FIXEDSIZEBINARY),
                Pair.of("10#l_shipmode", FIXEDSIZEBINARY),
                Pair.of("l_comment", VARCHAR)
        ));

        TpchTableTranslator.translateTable("nation", targetFolder, List.of(
                Pair.of("n_nationkey", INT),
                Pair.of("25#n_name", FIXEDSIZEBINARY),
                Pair.of("n_regionkey", INT),
                Pair.of("n_comment", VARCHAR)
        ));

        TpchTableTranslator.translateTable("region", targetFolder, List.of(
                Pair.of("r_regionkey", INT),
                Pair.of("25#r_name", FIXEDSIZEBINARY),
                Pair.of("r_comment", VARCHAR)
        ));

    }

}
