package kps.xml.objects;

import kps.xml.objects.abstracts.BusinessEvent;
import kps.xml.objects.abstracts.BusinessEventWithLocation;
import kps.xml.objects.enums.Priority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@XmlRootElement(name="simulation") @XmlAccessorType(XmlAccessType.NONE) public class Simulation {
    //These locations are locations that are not currently in the object graph but are about to be used,
    //for example they may just have been added by clicking "add new location"
    @XmlTransient private static final Set<String> tempLocations = new HashSet<>();

    //These fields are saved in the XML log
    @XmlElement(name="route") private List<Route> routes;
    @XmlElement(name="mail") private List<Mail> mail;
    @XmlElement(name="price") private List<Price> prices;

    public Simulation() {
        this.routes = new ArrayList<>();
        this.mail = new ArrayList<>();
        this.prices = new ArrayList<>();
    }

    @NotNull public List<Route> getRoutes() {
        return routes;
    }

    @NotNull public List<Mail> getMail() {
        return mail;
    }

    @NotNull public List<Price> getPrices() {
        return prices;
    }

    @NotNull public List<BusinessEvent> getAllBusinessEvents() {
        ArrayList<BusinessEvent> build = new ArrayList<>();
        build.addAll(routes);
        build.addAll(mail);
        build.addAll(prices);
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

    @Nullable public Set<CalculatedRoute> buildCalculatedRoute(@NotNull String from, @NotNull String to, @NotNull Priority priority) {
        return new RouteCalculator().buildCalculatedRoute(from, to, priority);
    }

    public boolean isLocationValid(@NotNull String to) {
        return getLocations().contains(to);
    }

    private class RouteCalculator {
        private List<String> visitedNodes = new ArrayList<>();
        private List<Route> uniqueRoutes = getUniqueRoutes();

        @NotNull Set<CalculatedRoute> buildCalculatedRoute(@NotNull String from, @NotNull String to, @NotNull Priority priority) {
            return calculateRoute(from, to, priority, new CalculatedRoute());
        }

        @NotNull Set<CalculatedRoute> calculateRoute(@NotNull String from, @NotNull String to, @NotNull Priority priority, @NotNull CalculatedRoute calculatedRoute) {
            visitedNodes.add(from);
            Set<Route> routesWithMatchingFrom = uniqueRoutes.stream().filter(n -> from.equals(n.getFrom())).filter(r -> priority.willSettleFor(r.getTransportType())).collect(Collectors.toSet());
            Set<CalculatedRoute> newRoutes = new HashSet<>();
            for(Route r : routesWithMatchingFrom) {
                if(r.getTo().equals(to)) {
                    newRoutes.add(new CalculatedRoute(calculatedRoute, r));
                } else {
                    if(!visitedNodes.contains(r.getTo())) {
                        newRoutes.addAll(calculateRoute(r.getTo(), to, priority, new CalculatedRoute(calculatedRoute, r)));
                    }
                }
            }
            return newRoutes;
        }
    }

    public static void addTempLocation(String location) {
        tempLocations.add(location);
    }
}
