package kps.xml.objects;

import kps.gui.windows.form.dialogs.MailDialog;
import kps.xml.adapters.DateAdapter;
import kps.xml.objects.abstracts.BusinessEvent;
import kps.xml.objects.abstracts.BusinessEventWithLocation;
import kps.xml.objects.abstracts.ModelObject;
import kps.xml.objects.enums.DayOfWeek;
import kps.xml.objects.enums.Priority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;
import java.util.Date;

/**
 * The object which represents a mail event.
 */
@XmlAccessorType(XmlAccessType.NONE) public class Location extends ModelObject {
    @XmlJavaTypeAdapter(DateAdapter.class) @XmlAttribute private Date date;
    @XmlElement private String name;
    @XmlElement private boolean domestic;
    @XmlElement private boolean available;

    public Location(Simulation s) {
        super(s);
        this.simulation = s;
    }

    public Location(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDomestic() {
        return domestic;
    }

    public void setDomestic(boolean domestic) {
        this.domestic = domestic;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void edit(@NotNull Frame owner) {
        //new MailDialog(owner, simulation, this);
    }
}
