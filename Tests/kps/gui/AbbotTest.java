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

    public AbbotTest(String name) {
        super(name);
        // Robot.setEventMode(1);
        // Robot.componentDelay = 0;
        // Robot.setAutoDelay(0);
        // Robot.setAutoDelay(0);
    }

    public static Test suite() {

        return new ScriptTestSuite(AbbotTest.class, "abbotTestScripts/homeScreenTests") {

            // Determine whether the given script will be included //
            public boolean accept(File file) {
                String name = file.getName();
                return name.startsWith("abbotTest") && name.endsWith("Edit.xml");
            }
        };
    }

    public static void main(String[] args) {
        TestHelper.runTests(args, AbbotTest.class);
    }
}
