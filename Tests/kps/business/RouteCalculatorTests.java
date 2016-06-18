package kps.business;

import kps.xml.SimulationXML;
import kps.xml.exceptions.XMLException;
import kps.xml.objects.CalculatedRoute;
import kps.xml.objects.Simulation;
import kps.xml.objects.enums.Priority;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class RouteCalculatorTests {
    @Test public void ensureThatSystemCanCalculateARouteFromTestData() throws FileNotFoundException, XMLException {
        Simulation s = SimulationXML.readSimulationFromFile(new FileInputStream("test_data/Test.xml"));
        RouteCalculator rc = new RouteCalculator(s);
        CalculatedRoute calculatedRoutes = rc.buildCalculatedRoute("Auckland", "Suva", Priority.INTERNATIONAL_AIR);
        Assert.assertNotNull(calculatedRoutes);
        Assert.assertTrue(calculatedRoutes.getRoutes().size() > 0);
    }

    @Test public void ensureThatSystemWillNotPickInferiorRoute() throws FileNotFoundException, XMLException {
        Simulation s = SimulationXML.readSimulationFromFile(new FileInputStream("test_data/Test.xml"));
        RouteCalculator rc = new RouteCalculator(s);
        CalculatedRoute calculatedRoute = rc.buildCalculatedRoute("Auckland", "Suva", Priority.INTERNATIONAL_STANDARD);
        Assert.assertTrue(calculatedRoute.getRoutes().size() == 3);
    }

}
