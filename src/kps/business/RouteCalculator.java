package kps.business;

import kps.xml.objects.CalculatedRoute;
import kps.xml.objects.Route;
import kps.xml.objects.Simulation;
import kps.xml.objects.enums.Priority;
import kps.xml.objects.enums.TransportType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RouteCalculator {
    private List<Route> routes;

    public RouteCalculator(Simulation simulation) {
        this.routes = simulation.getUniqueRoutes().stream().filter(r -> !r.isDiscontinued()).collect(Collectors.toList());
    }

    @Nullable public CalculatedRoute buildCalculatedRoute(@NotNull String from, @NotNull String to, @NotNull Priority priority) {
        List<CalculatedRoute> possiblePaths = new ArrayList<>();
        for (Route r : routes){
            if (r.getFrom().equals(from)){
                List<String> visitedNodes = new ArrayList<>();
                visitedNodes.add(r.getFrom());
                calculateCalculatedRoute(from, to, new CalculatedRoute(), possiblePaths, visitedNodes);
            }
        }

        if(possiblePaths.size() == 0) {
            return null;
        }
        return possiblePaths.stream().min((a, b) -> Integer.compare(getFit(a, priority), getFit(b, priority))).get();

    }

    private void calculateCalculatedRoute(@NotNull String from, @NotNull String goal, @NotNull CalculatedRoute currentRoute, @NotNull List<CalculatedRoute> PossiblePaths, @NotNull List<String> visitedNodes) {
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
                calculateCalculatedRoute(r.getTo(), goal, temp, PossiblePaths, visited);
            }
        }
    }

    private int getFit(CalculatedRoute c, Priority p){
        int fitness = 0;
        for (Route r : c.getRoute()){
            fitness += 1;
            if (r.getTransportType() != TransportType.Air && p.isAir())
                fitness += 1;
        }
        return fitness;
    }
}