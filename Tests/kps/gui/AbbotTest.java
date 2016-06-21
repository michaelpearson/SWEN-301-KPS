package kps.gui;

import abbot.tester.Robot;
import abbot.tester.*;
import junit.extensions.abbot.*;
import junit.framework.Test;

import java.io.File;

/**
 * Created by Cameron Bryers on 16/06/16.
 * <p>
 * Filepaths I used in abbot
 * <p>
 * classname: kps.Main
 * <p>
 * # Linux
 * classpath: /home/bryerscame/SWEN/301/SWEN-301-KPS/out/artifacts/SWEN_301_KPS_jar/SWEN-301-KPS.jar
 * arguments: /home/bryerscame/SWEN/301/SWEN-301-KPS/test_data/Test.xml
 * <p>
 * # Mac
 * classpath: /Users/cambis/Documents/Uni/SWEN/301/SWEN-301-KPS/out/artifacts/SWEN_301_KPS_jar/SWEN-301-KPS.jar
 * arguments: /Users/cambis/Documents/Uni/SWEN/301/SWEN-301-KPS/test_data/Test.xml
 * <p>
 */
public class AbbotTest extends ScriptFixture {

    /**
     * DEBUGGING MODE
     */
    private static final boolean DEBUG = true;

    /**
     * Directory to test for debugging mode
     */
    private static final String TEST_DIR = "abbotTestScripts/routeTests";

    public AbbotTest(String name) {
        super(name);
    }

    /**
     * Run the abbot scripts. If in debugging mode, run the specified directory as outlined by AbbotTest.TEST_DIR.
     * @return JUnit tests
     */
    public static Test suite() {

        return new ScriptTestSuite(AbbotTest.class, (DEBUG) ? TEST_DIR : "abbotTestScripts/", !DEBUG) {

            /**
             * Ensure that a script is valid to run.
             * @param file
             * @return
             */
            public boolean accept(File file) {
                String name = file.getName();
                return name.startsWith("abbotTest") && name.endsWith(".xml");
            }
        };
    }

    public static void main(String[] args) {
        TestHelper.runTests(args, AbbotTest.class);
    }
}
