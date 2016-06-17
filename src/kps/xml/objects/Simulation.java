package kps.xml.objects;

import kps.xml.objects.abstracts.BusinessEvent;
import kps.xml.objects.abstracts.BusinessEventWithLocation;
import kps.xml.objects.enums.Priority;
import kps.xml.objects.enums.TransportType;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.*;
import java.util.*;

@XmlRootElement(name="simulation") @XmlAccessorType(XmlAccessType.NONE) public class Simulation {
    //These locations are locations that are not currently in the object graph but are about to be used,
    //for example they may just have been added by clicking "add new location"
    @XmlTransient private static final Set<String> tempLocations = new HashSet<>();

    //These fields are saved in the XML log
    @XmlElement(name="route") private List<Route> routes;
    @XmlElement(name="mail") private List<Mail> mail;
    @XmlElement(name="price") private List<CustomerPrice> customerPrices;

    public Simulation() {
        this.routes = new ArrayList<>();
        this.mail = new ArrayList<>();
        this.customerPrices = new ArrayList<>();
    }

    @NotNull public List<Route> getRoutes() {
        return routes;
    }

    @NotNull public List<Mail> getMail() {
        return mail;
    }

    @NotNull public List<CustomerPrice> getCustomerPrices() {
        return customerPrices;
    }

    @NotNull public List<BusinessEvent> getAllBusinessEvents() {
        ArrayList<BusinessEvent> build = new ArrayList<>();
        build.addAll(routes);
        build.addAll(mail);
        build.addAll(customerPrices);
        build.sort((left, right) -> right.getDate().compareTo(left.getDate()));
        return build;
    }

    @NotNull public Set<String> getLocations() {
        Set<String> allLocations = new HashSet<>();
        List<BusinessEventWithLocation> businessEvents = new LinkedList<>();
        businessEvents.addAll(routes);
        businessEvents.addAll(mail);
        allLocations.addAll(tempLocations);

        for(BusinessEventWithLocation e : businessEvents) {
            allLocations.add(e.getFrom());
            allLocations.add(e.getTo());
        }
        return allLocations;
    }

    @NotNull public List<Route> getUniqueRoutes() {
        List<Route> uniqueRoutes = new ArrayList<>();
        for(Route r1 : routes) {
            boolean allBefore = true;
            for(Route r2 : routes) {
                if(r1 == r2 || !r1.getCompany().equals(r2.getCompany()) || r1.getTransportType() != r2.getTransportType()) {
                    continue;
                }
                if(!r1.getDate().after(r2.getDate())) {
                    allBefore = false;
                }
            }
            if(allBefore) {
                uniqueRoutes.add(r1);
            }
        }
        return uniqueRoutes;
    }

    @NotNull public CalculatedRoute buildCalculatedRoute(@NotNull String from, @NotNull String to, @NotNull Priority priority) {
        return new RouteCalculator().buildCalculatedRoute(from, to, priority);
    }

    public boolean isLocationValid(@NotNull String to) {
        return getLocations().contains(to);
    }

    private class RouteCalculator {
        @NotNull CalculatedRoute buildCalculatedRoute(@NotNull String from, @NotNull String to, @NotNull Priority priority) {
            List<CalculatedRoute> PossiblePaths = new ArrayList<>();
            for (Route r : routes){
                if (r.getFrom().equals(from)){
                    List<String> visitedNodes = new ArrayList<>();
                    visitedNodes.add(r.getFrom());
                    calculateCalculatedRoute(from, to, priority, new CalculatedRoute(), PossiblePaths, visitedNodes);
                }
            }

            CalculatedRoute bestRoute = new CalculatedRoute();
            int bestFit = Integer.MAX_VALUE;
            for (CalculatedRoute r : PossiblePaths){
                int fit = getFit(r, priority);
                if (fit < bestFit){
                    bestFit = fit;
                    bestRoute = r;
                }
            }

            return bestRoute;
        }

        @NotNull void calculateCalculatedRoute(@NotNull String from, @NotNull String goal, @NotNull Priority priority,
                                               CalculatedRoute currentRoute, List<CalculatedRoute> PossiblePaths, List<String> visitedNodes) {
            for (Route r : routes){
                if (r.getFrom().equals(from)){
                    if (visitedNodes.contains(r.getTo())) continue;
                    CalculatedRoute temp = new CalculatedRoute(currentRoute, r);

                    if (r.getTo().equals(goal)) {
                        PossiblePaths.add(temp);
                        return;
                    }

                    List<String> visited = new ArrayList<>(visitedNodes);
                    visited.add(r.getFrom());
                    calculateCalculatedRoute(r.getTo(), goal, priority, temp, PossiblePaths, visited);
                }
            }
        }

        private int getFit(CalculatedRoute c, Priority p){
            int fitness = 0;
            for (Route r : c.getRoutes()){
                fitness += 1;
                if (r.getTransportType() != TransportType.Air && p.isAir())
                    fitness += 1;
            }
            return fitness;
        }
    }

    public static void addTempLocation(String location) {
        tempLocations.add(location);
    }
}
