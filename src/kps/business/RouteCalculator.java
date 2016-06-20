package kps.business;

import kps.business.interfaces.IRouteCalculator;
import kps.xml.objects.CalculatedRoute;
import kps.xml.objects.Mail;
import kps.xml.objects.Route;
import kps.xml.objects.Simulation;
import kps.xml.objects.enums.Priority;
import kps.xml.objects.enums.TransportType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of the route calculator
 */
public class RouteCalculator implements IRouteCalculator {
    private final List<Route> routes;
    private final List<CalculatedRoute> possiblePaths = new ArrayList<>();
    private final Set<String> visitedNodes = new HashSet<>();
    private final @NotNull Mail mailDelivery;

    public RouteCalculator(@NotNull Simulation simulation, @NotNull Mail mailDelivery) {
        this.mailDelivery = mailDelivery;
        this.routes = simulation.getUniqueRoutes().stream().filter(this::isRouteSuitable).collect(Collectors.toList());

    }

    @SuppressWarnings("RedundantIfStatement")
    private boolean isRouteSuitable(Route r) {
        if(r.isDiscontinued()) {
            return false;
        }
        if(r.getMaxVolume() < mailDelivery.getVolume() && r.getMaxVolume() != 0) {
            return false;
        }
        if(r.getMaxWeight() < mailDelivery.getWeight() && r.getMaxWeight() != 0) {
            return false;
        }
        return true;
    }

    @SuppressWarnings("Convert2streamapi") @Nullable
    public CalculatedRoute buildCalculatedRoute() {
        String from = mailDelivery.getFrom();
        String goal = mailDelivery.getTo();

        for (Route r : routes) {
            if (r.getFrom().equals(mailDelivery.getFrom())){
                visitedNodes.add(r.getFrom());
                calculateRoute(from, goal, null);
            }
        }
        if(possiblePaths.size() == 0) {
            return null;
        }
        return possiblePaths.stream().min((a, b) -> Integer.compare(getFit(a, mailDelivery.getPriority()), getFit(b, mailDelivery.getPriority()))).get();
    }

    private void calculateRoute(@NotNull String from, @NotNull String goal, @Nullable CalculatedRoute currentRoute) {
        if(currentRoute == null) {
            currentRoute = new CalculatedRoute();
        }
        for (Route r : routes) {
            if (r.getFrom().equals(from)) {
                if (visitedNodes.contains(r.getTo())) continue;
                CalculatedRoute temp = new CalculatedRoute(currentRoute, r);

                if (r.getTo().equals(goal)) {
                    possiblePaths.add(temp);
                    return;
                }

                List<String> visited = new ArrayList<>(visitedNodes);
                visited.add(r.getFrom());
                calculateRoute(r.getTo(), goal, temp);
            }
        }
    }

    private int getFit(CalculatedRoute c, Priority p) {
        int fitness = 0;
        for (Route r : c.getRoute()) {
            fitness += 1;
            if (r.getTransportType() != TransportType.Air && p.isAir())
                fitness += 1;
        }
        return fitness;
    }
}
