package kps.business.routeCalculator;

import kps.business.RouteCalculator;
import kps.business.interfaces.IRouteCalculator;
import kps.xml.SimulationXML;
import kps.xml.exceptions.XMLException;
import kps.xml.objects.CalculatedRoute;
import kps.xml.objects.Mail;
import kps.xml.objects.Route;
import kps.xml.objects.Simulation;
import kps.xml.objects.enums.Priority;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class CalculatorTests {

    private static final String TEST_XML_FILE = "test_data/Test.xml";

    @Test public void ensureThatSystemCanCalculateARouteFromTestData() throws FileNotFoundException, XMLException {
        Simulation s = SimulationXML.readSimulationFromFile(new FileInputStream(TEST_XML_FILE));

        Mail m = new Mail();
        m.setFrom("Auckland");
        m.setTo("Suva");
        m.setPriority(Priority.INTERNATIONAL_AIR);

        IRouteCalculator rc = new RouteCalculator(s, m);
        CalculatedRoute calculatedRoute = rc.buildCalculatedRoute();
        Assert.assertNotNull(calculatedRoute);
    }

    @Test public void ensureThatSystemWillNotPickDiscontinuedRoute() throws FileNotFoundException, XMLException, InterruptedException {
        Simulation s = SimulationXML.readSimulationFromFile(new FileInputStream(TEST_XML_FILE));
        {
            Route cr = s.getUniqueRoutes().stream().filter(r -> r.getFrom().equals("Auckland") && r.getTo().equals("Wellington")).findFirst().get();
            cr.setDiscontinued(true);
        }
        {
            Route cr = s.getUniqueRoutes().stream().filter(r -> r.getFrom().equals("Auckland") && r.getTo().equals("Hamilton")).findFirst().get();
            cr.setDiscontinued(true);
        }


        Mail m = new Mail();
        m.setFrom("Auckland");
        m.setTo("Suva");
        m.setPriority(Priority.INTERNATIONAL_STANDARD);

        IRouteCalculator rc = new RouteCalculator(s, m);
        CalculatedRoute calculatedRoute = rc.buildCalculatedRoute();
        Assert.assertNull(calculatedRoute);
    }

    @Test public void ensureThatSystemWillPickLongRouteIfShortRouteIsDiscontinued() throws FileNotFoundException, XMLException, InterruptedException {
        Simulation s = SimulationXML.readSimulationFromFile(new FileInputStream(TEST_XML_FILE));
        Route cr = s.getUniqueRoutes().stream().filter(r -> r.getFrom().equals("Auckland") && r.getTo().equals("Wellington")).findFirst().get();
        cr.setDiscontinued(true);

        Mail m = new Mail();
        m.setFrom("Auckland");
        m.setTo("Wellington");
        m.setPriority(Priority.INTERNATIONAL_STANDARD);

        IRouteCalculator rc = new RouteCalculator(s, m);
        CalculatedRoute calculatedRoute = rc.buildCalculatedRoute();
        Assert.assertNotNull(calculatedRoute);
        Assert.assertTrue(calculatedRoute.getRoute().size() == 2);
    }

    @Test public void ensureThatSystemWillNotPickInferiorRoute() throws FileNotFoundException, XMLException, InterruptedException {
        Simulation s = SimulationXML.readSimulationFromFile(new FileInputStream(TEST_XML_FILE));
        Route cr = s.getUniqueRoutes().stream().filter(r -> r.getFrom().equals("Wellington") && r.getTo().equals("Sydney") && r.isDiscontinued()).findFirst().get();
        cr.setDiscontinued(false);


        Mail m = new Mail();
        m.setFrom("Auckland");
        m.setTo("Suva");
        m.setPriority(Priority.INTERNATIONAL_STANDARD);

        IRouteCalculator rc = new RouteCalculator(s, m);
        CalculatedRoute calculatedRoute = rc.buildCalculatedRoute();
        Assert.assertNotNull(calculatedRoute);
        Assert.assertTrue(calculatedRoute.getRoute().size() == 3);
    }

    @Test public void ensureThatRouteWillNotBePickedIfMailDeliveriesWeightIsTooLarge() throws FileNotFoundException, XMLException {
        Simulation s = SimulationXML.readSimulationFromFile(new FileInputStream(TEST_XML_FILE));

        Mail m = new Mail();
        m.setFrom("Wellington");
        m.setTo("Rome");
        m.setPriority(Priority.INTERNATIONAL_STANDARD);
        m.setWeight(600);

        IRouteCalculator rc = new RouteCalculator(s, m);
        CalculatedRoute calculatedRoute = rc.buildCalculatedRoute();
        calculatedRoute.getRoute().get(0).setMaxWeight(100);

        rc = new RouteCalculator(s, m);
        calculatedRoute = rc.buildCalculatedRoute();

        Assert.assertNull(calculatedRoute);
    }


}
