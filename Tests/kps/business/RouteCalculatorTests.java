package kps.business;

import kps.xml.SimulationXML;
import kps.xml.exceptions.XMLException;
import kps.xml.objects.CalculatedRoute;
import kps.xml.objects.Route;
import kps.xml.objects.Simulation;
import kps.xml.objects.enums.Priority;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class RouteCalculatorTests {
    @Test public void ensureThatSystemCanCalculateARouteFromTestData() throws FileNotFoundException, XMLException {
        Simulation s = SimulationXML.readSimulationFromFile(new FileInputStream("test_data/Test.xml"));
        RouteCalculator rc = new RouteCalculator(s);
        CalculatedRoute calculatedRoutes = rc.buildCalculatedRoute("Auckland", "Suva", Priority.INTERNATIONAL_AIR);
        Assert.assertNull(calculatedRoutes);
    }

    @Test public void ensureThatSystemWillNotPickDiscontinuedRoute() throws FileNotFoundException, XMLException, InterruptedException {
        Simulation s = SimulationXML.readSimulationFromFile(new FileInputStream("test_data/Test.xml"));
        RouteCalculator rc = new RouteCalculator(s);
        CalculatedRoute calculatedRoute = rc.buildCalculatedRoute("Auckland", "Suva", Priority.INTERNATIONAL_STANDARD);
        Assert.assertNull(calculatedRoute);
    }

    @Test public void ensureThatSystemWillPickLongRouteIfShortRouteIsDiscontinued() throws FileNotFoundException, XMLException, InterruptedException {
        Simulation s = SimulationXML.readSimulationFromFile(new FileInputStream("test_data/Test.xml"));
        Route cr = s.getUniqueRoutes().stream().filter(r -> r.getFrom().equals("Auckland") && r.getTo().equals("Wellington")).findFirst().get();
        cr.setDiscontinued(true);
        RouteCalculator rc = new RouteCalculator(s);
        CalculatedRoute calculatedRoute = rc.buildCalculatedRoute("Auckland", "Wellington", Priority.INTERNATIONAL_STANDARD);
        Assert.assertNotNull(calculatedRoute);
        Assert.assertTrue(calculatedRoute.getRoute().size() == 2);
    }

    @Test public void ensureThatSystemWillNotPickInferiorRoute() throws FileNotFoundException, XMLException, InterruptedException {
        Simulation s = SimulationXML.readSimulationFromFile(new FileInputStream("test_data/Test.xml"));
        Route cr = s.getUniqueRoutes().stream().filter(r -> r.getFrom().equals("Wellington") && r.getTo().equals("Sydney") && r.isDiscontinued()).findFirst().get();
        cr.setDiscontinued(false);
        RouteCalculator rc = new RouteCalculator(s);
        CalculatedRoute calculatedRoute = rc.buildCalculatedRoute("Auckland", "Suva", Priority.INTERNATIONAL_STANDARD);
        Assert.assertNotNull(calculatedRoute);
        Assert.assertTrue(calculatedRoute.getRoute().size() == 3);
    }



}
