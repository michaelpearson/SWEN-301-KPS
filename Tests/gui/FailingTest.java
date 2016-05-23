package gui;

import org.junit.Assert;
import org.junit.Test;

public class FailingTest {
    @Test
    public void failingTestToTestTravisCi() {
        Assert.fail();
    }
}
