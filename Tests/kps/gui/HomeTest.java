package kps.gui;

import kps.gui.windows.Home;
import org.junit.Assert;
import org.junit.Test;

public class HomeTest {
    @Test
    public void testApplicationName() {
        Assert.assertEquals(Home.APPLICATION_NAME, "Swen 301 - KPS");
    }
}