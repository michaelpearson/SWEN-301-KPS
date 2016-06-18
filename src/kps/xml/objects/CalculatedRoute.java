package kps.xml.objects;

import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

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
