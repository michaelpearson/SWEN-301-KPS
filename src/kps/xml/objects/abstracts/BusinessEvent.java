package kps.xml.objects.abstracts;

import kps.xml.adapters.DateAdapter;
import kps.xml.objects.Simulation;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;
import java.util.Date;

/**
 * The base class for a business event.
 * A business event is an entry in the simulation which has a to and from and is also {@link Priceable}.
 */
@XmlTransient public abstract class BusinessEvent extends ModelObject implements Priceable {

    @XmlJavaTypeAdapter(DateAdapter.class) @XmlAttribute private Date date;

    BusinessEvent(Simulation s) {
        super(s);
    }

    BusinessEvent() {}

    public Date getDate() {
        if(date == null) {
            date = new Date();
        }
        return date;
    }

    public abstract String getEventType();
    public abstract void edit(Frame owner);

    @NotNull public abstract String getFrom();
    @NotNull public abstract String getTo();

}
