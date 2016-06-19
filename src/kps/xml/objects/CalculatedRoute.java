package kps.xml.objects;

import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Object which represents a route which has been calculated by the {@link kps.business.RouteCalculator}
 * A calculated route is made up of any number of routes which link together to get from a -> b
 */
@XmlAccessorType(XmlAccessType.NONE) public class CalculatedRoute {
    @XmlElement(name="route") private List<Route> routes;

    /**
     * Default constructor for xml unmarshaller
     */
    public CalculatedRoute() {}

    public CalculatedRoute(CalculatedRoute current, Route append) {
        routes = new ArrayList<>();
        routes.addAll(current.getRoute());
        routes.add(append);
    }

    /**
     * Gets the list of routes which make up this calculated route
     * @return the list of routes that make up this calculated route
     */
    @NotNull public List<Route> getRoute() {
        if(routes == null) {
            routes = new ArrayList<>();
        }
        return routes;
    }

    @Override public String toString() {
        StringBuilder builder = new StringBuilder("" + routes.get(0).getFrom());
        for(Route r : routes) {
            builder.append(" -> " + r.getTo());
        }
        return builder.toString();
    }
}
