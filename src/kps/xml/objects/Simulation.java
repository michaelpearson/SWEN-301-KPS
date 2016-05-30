package kps.xml.objects;

import kps.xml.objects.abstracts.BusinessEvent;
import kps.xml.objects.enums.Priority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@XmlRootElement(name="simulation") @XmlAccessorType(XmlAccessType.NONE) public class Simulation {
    //Fields which are not preserved in XML
    private HashMap<Integer, Location> locationIdCache = new HashMap<>();
    private HashMap<String, Location> locationNameCache = new HashMap<>();

    //These fields are saved in the XML log
    @XmlElement(name="route") private List<Route> routes;
    @XmlElement(name="mail") private List<Mail> mail;
    @XmlElement(name="location") private List<Location> locations;

    public Simulation() {
        this.routes = new ArrayList<>();
        this.mail = new ArrayList<>();
        this.locations = new ArrayList<>();
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public List<Mail> getMail() {
        return mail;
    }

    @NotNull public List<Location> getLocations() {
        return locations;
    }

    @Nullable public Location getLocationById(int id) {
        if(locationIdCache.get(id) != null) {
            return locationIdCache.get(id);
        }
        for(Location l : locations) {
            if(l.getId() == id) {
                locationIdCache.put(id, l);
                return l;
            }
        }
        return null;
    }

    @Nullable public Location getLocationByName(@NotNull String name) {
        if(locationNameCache.get(name) != null) {
            return locationNameCache.get(name);
        }
        for(Location l : locations) {
            if(name.equals(l.getName())) {
                locationNameCache.put(name, l);
                return l;
            }
        }
        return null;
    }

    @NotNull public List<BusinessEvent> getAllBusinessEvents() {
        ArrayList<BusinessEvent> build = new ArrayList<>();
        build.addAll(routes);
        build.addAll(mail);
        build.sort((left, right) -> right.getDate().compareTo(left.getDate()));
        return build;
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

    @Nullable public Set<CalculatedRoute> buildCalculatedRoute(@NotNull Location from, @NotNull Location to, @NotNull Priority priority) {
        return new RouteCalculator().buildCalculatedRoute(from, to, priority);
    }

    private class RouteCalculator {
        private List<Location> visitedNodes = new ArrayList<>();
        private List<Route> uniqueRoutes = getUniqueRoutes();

        @NotNull Set<CalculatedRoute> buildCalculatedRoute(@NotNull Location from, @NotNull Location to, @NotNull Priority priority) {
            return calculateRoute(from, to, priority, new CalculatedRoute());
        }

        @NotNull Set<CalculatedRoute> calculateRoute(@NotNull Location from, @NotNull Location to, @NotNull Priority priority, @NotNull CalculatedRoute calculatedRoute) {
            visitedNodes.add(from);
            Set<Route> routesWithMatchingFrom = uniqueRoutes.stream().filter(n -> from.equals(n.getFrom())).filter(r -> priority.willSettleFor(r.getTransportType())).collect(Collectors.toSet());
            Set<CalculatedRoute> newRoutes = new HashSet<>();
            for(Route r : routesWithMatchingFrom) {
                if(visitedNodes.contains(r.getTo())) {

                } else if(r.getTo().equals(to)) {
                    newRoutes.add(new CalculatedRoute(calculatedRoute, r));
                } else {
                    newRoutes.addAll(calculateRoute(r.getTo(), to, priority, new CalculatedRoute(calculatedRoute, r)));
                }
            }
            return newRoutes;
        }
    }
}
