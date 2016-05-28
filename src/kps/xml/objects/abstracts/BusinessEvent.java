package kps.xml.objects.abstracts;

import kps.xml.adapters.DateAdapter;
import kps.xml.objects.Location;
import kps.xml.objects.Simulation;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;
import java.util.Date;

@XmlTransient
public abstract class BusinessEvent extends ModelObject implements Priceable {

    @XmlJavaTypeAdapter(DateAdapter.class) @XmlAttribute private Date date;

    public BusinessEvent(Simulation s) {
        super(s);
    }

    public BusinessEvent() {}

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        if(date == null) {
            date = new Date();
        }
        return date;
    }

    public abstract String getEventType();
    public abstract void edit(Frame owner);

    @Nullable public abstract Location getFrom();
    @Nullable public abstract Location getTo();

}
