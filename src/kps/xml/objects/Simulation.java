package kps.xml.objects;

import kps.xml.objects.abstracts.BusinessEvent;
import kps.xml.objects.abstracts.BusinessEventWithLocation;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.*;
import java.util.*;

/**
 * This is the simulation object which holds the state of the system including all routes, mail delivery events and
 * prices.
 */
@XmlRootElement(name="simulation") @XmlAccessorType(XmlAccessType.NONE) public class Simulation {
    //These locations are locations that are not currently in the object graph but are about to be used,
    //for example they may just have been added by clicking "add new location"
    @XmlTransient private static final Set<String> tempLocations = new HashSet<>();

    //These fields are saved in the XML log
    @XmlElement(name="route") private List<Route> routes;
    @XmlElement(name="mail") private List<Mail> mail;
    @XmlElement(name="price") private List<CustomerPrice> customerPrices;

    /**
     * Used to create a new blank simulation
     */
    public Simulation() {
        this.routes = new ArrayList<>();
        this.mail = new ArrayList<>();
        this.customerPrices = new ArrayList<>();
    }

    /**
     * Get all of the routes in the simulation
     * @return a list of {@link Route}'s
     */
    @NotNull public List<Route> getRoutes() {
        return routes;
    }

    /**
     * Get all of the mail delivery events in the simulation
     * @return a list of all the {@link Mail} delivery events
     */
    @NotNull public List<Mail> getMail() {
        return mail;
    }

    /**
     * Get all of the {@link CustomerPrice} objects.
     * @return a list of all the {@link CustomerPrice} objects.
     */
    @NotNull public List<CustomerPrice> getCustomerPrices() {
        return customerPrices;
    }

    /**
     * Get a list of all of the {@link Route}'s, {@link Mail} deliveries and {@link CustomerPrice}'s
     * @return all of the {@link BusinessEvent}'s in the simulation
     */
    @NotNull public List<BusinessEvent> getAllBusinessEvents() {
        ArrayList<BusinessEvent> build = new ArrayList<>();
        build.addAll(routes);
        build.addAll(mail);
        build.addAll(customerPrices);
        build.sort((left, right) -> right.getDate().compareTo(left.getDate()));
        return build;
    }

    /**
     * Gets all of the known locations in the system plus the temp locations created by the "Add new location..." option
     * @return a list of all of the locations the simulation knows about.
     */
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

    /**
     * Gets all unique routes in the system. Because an update is defined as creating a new route, this function only
     * returns the newest route, rather then the route and all of its updates.
     * @return only the current version of each route.
     */
    @NotNull public List<Route> getUniqueRoutes() {
        List<Route> uniqueRoutes = new ArrayList<>();
        for(Route r1 : routes) {
            boolean allBefore = true;
            for(Route r2 : routes) {
                if(r1 == r2 || !r1.getCompany().equals(r2.getCompany()) || r1.getTransportType() != r2.getTransportType()) {
                    continue;
                }
                if(!(r1.getFrom().equals(r2.getFrom()) && r1.getTo().equals(r2.getTo()))) {
                    continue;
                }
                if(!r1.getDate().after(r2.getDate())) {
                    allBefore = false;
                    break;
                }
            }
            if(allBefore) {
                uniqueRoutes.add(r1);
            }
        }
        return uniqueRoutes;
    }

    /**
     * Check that the passed route is valid.
     * @param to the route to check
     * @return whether the route exists in the system
     */
    public boolean isLocationValid(@NotNull String to) {
        return getLocations().contains(to);
    }

    /**
     * Add a new location (which is valid) without having to add it to a business event.
     * @param location the lcoation to add.
     */
    public static void addTempLocation(String location) {
        tempLocations.add(location);
    }
}
