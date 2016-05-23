package xml.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="simulation")
@XmlAccessorType(XmlAccessType.FIELD)
public class Simulation {
    @XmlElement(name="cost") List<Cost> costs;
    @XmlElement(name="mail") List<Mail> mail;
    @XmlElement(name="price") List<Price> price;
    @XmlElement(name="discontinue") List<Discontinue> discontinuedRoutes;
}
