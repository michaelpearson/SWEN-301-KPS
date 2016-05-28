package kps.business;

import kps.xml.objects.Cost;
import kps.xml.objects.Location;
import kps.xml.objects.abstracts.BusinessEventWithLocation;
import kps.xml.objects.enums.Priority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class Route {
    @NotNull private Location from;
    @NotNull private Location to;

    public Route(@NotNull Location from, @NotNull Location to) {
        this.from = from;
        this.to = to;
    }

    public Route(@NotNull BusinessEventWithLocation businessEvent) {
        Location f = businessEvent.getFrom();
        Location t = businessEvent.getTo();
        if(f == null || t == null) {
            throw new RuntimeException("This business event should not have a null location");
        }
        this.from = f;
        this.to = t;
    }

    @NotNull public Location getFrom() {
        return from;
    }

    @NotNull public Location getTo() {
        return to;
    }

    @Nullable public List<Cost> calculateIntermediateRoutes(@NotNull Priority minimumPriority) {
        return calculateIntermediateRoutes(minimumPriority, null);
    }

    @Nullable public List<Cost> calculateIntermediateRoutes(@NotNull Priority minimumPriority, @Nullable Date latestCost) {
        throw new RuntimeException("Not implemented");
        //return new ArrayList<>();
    }
}
