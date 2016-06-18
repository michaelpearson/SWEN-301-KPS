package kps.xml.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE) public class CalculatedRoute {
    @XmlElement(name="route") private List<Route> routes = new ArrayList<>();

    public CalculatedRoute() {}

    public CalculatedRoute(CalculatedRoute current, Route append) {
        routes.addAll(current.routes);
        routes.add(append);
    }

    public List<Route> getRoutes() {
        return routes;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("" + routes.get(0).getFrom());
        for(Route r : routes) {
            builder.append(" -> " + r.getTo());
        }
        return builder.toString();
    }
}
