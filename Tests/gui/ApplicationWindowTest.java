package gui;

import org.junit.Assert;
import org.junit.Test;

public class ApplicationWindowTest {
    @Test
    public void testApplicationName() {
        Assert.assertEquals(ApplicationWindow.APPLICATION_NAME, "Swen 301 - KPS");
    }
}