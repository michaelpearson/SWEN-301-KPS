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
import java.util.Set;

public class RouteCalculator {
    @Test public void ensureThatSystemCanCalculateARouteFromTestData() throws FileNotFoundException, XMLException {
        Simulation s = SimulationXML.readSimulationFromFile(new FileInputStream("test_data/Test.xml"));
        Set<CalculatedRoute> calculatedRoutes = s.buildCalculatedRoute(s.getLocationByName("Wellington"), s.getLocationByName("Suva"), Priority.INTERNATIONAL_STANDARD);
        Assert.assertNotNull(calculatedRoutes);
        Assert.assertTrue(calculatedRoutes.size() > 0);
    }

    @Test public void ensureThatSystemWillNotPickInferiorRoute() throws FileNotFoundException, XMLException {
        Simulation s = SimulationXML.readSimulationFromFile(new FileInputStream("test_data/Test.xml"));
        Set<CalculatedRoute> calculatedRoute = s.buildCalculatedRoute(s.getLocationByName("Wellington"), s.getLocationByName("Suva"), Priority.INTERNATIONAL_AIR);
        Assert.assertTrue(calculatedRoute == null || calculatedRoute.size() == 0);
    }

}
