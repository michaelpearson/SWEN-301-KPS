package kps.gui;

import abbot.tester.Robot;
import abbot.tester.*;
import junit.extensions.abbot.*;
import junit.framework.Test;
import java.io.File;
/**
 * Created by bryerscame on 16/06/16.
 *
 * Filepaths I used in abbot
 *
 * classname: kps.Main
 *
 * # Linux
 * classpath: /home/bryerscame/SWEN/301/SWEN-301-KPS/out/artifacts/SWEN_301_KPS_jar/SWEN-301-KPS.jar
 * arguments: /home/bryerscame/SWEN/301/SWEN-301-KPS/test_data/Test.xml
 *
 * # Mac
 * classpath: /Users/cambis/Documents/Uni/SWEN/301/SWEN-301-KPS/out/artifacts/SWEN_301_KPS_jar/SWEN-301-KPS.jar
 * arguments: /Users/cambis/Documents/Uni/SWEN/301/SWEN-301-KPS/test_data/Test.xml
 *
 * Files to start with abbotTest for them to run, you can append .old to them if you dont want to use them
 */
public class AbbotTest extends ScriptFixture {

    private static final boolean DEBUG = true;

    public AbbotTest(String name) {
        super(name);
    }

    public static Test suite() {

        if (DEBUG) {
            String dir = "updateCostTests";
            return new ScriptTestSuite(AbbotTest.class, "abbotTestScripts/" + dir) {
                // Determine whether the given script will be included //
                public boolean accept(File file) {
                    String name = file.getName();
                    return name.startsWith("abbotTest") && name.contains(".xml");
                }
            };
        } else {
            return new ScriptTestSuite(AbbotTest.class, "abbotTestScripts/",true) {//Run all tests
                public boolean accept(File file) {
                    String name = file.getName();
                    return name.startsWith("abbotTest") && name.endsWith(".xml");
                }
            };
        }
    }

    public static void main(String[] args) {
        TestHelper.runTests(args, AbbotTest.class);
    }
}
