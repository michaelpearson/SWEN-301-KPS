package kps.xml.objects;

import kps.gui.windows.dialogs.MailDialog;
import kps.xml.objects.abstracts.BusinessEventWithLocation;
import kps.xml.objects.enums.DayOfWeek;
import kps.xml.objects.enums.Priority;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.awt.*;

@XmlAccessorType(XmlAccessType.FIELD) public class Mail extends BusinessEventWithLocation {
    @XmlElement(name="day") private DayOfWeek day;
    @XmlElement(name="weight") private int weight;
    @XmlElement(name="volume") private int volume;
    @XmlElement(name="priority") private Priority priority;
    @XmlElement(name="calculatedRoute") private CalculatedRoute calculatedRoute;

    public Mail(Simulation s) {
        super(s);
    }

    public Mail() {}

    @Override public String getEventType() {
        return "Mail delivery";
    }

    @Override public void edit(Frame owner) {
        new MailDialog(owner, getSimulation(), this);
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public void setCalculatedRoute(CalculatedRoute calculatedRoute) {
        this.calculatedRoute = calculatedRoute;
    }

    public Priority getPriority() { return priority; }

    public int getWeight() {
        return weight;
    }

    public int getVolume() {
        return volume;
    }

    public DayOfWeek getDay() {
        return day;
    }

    @Override public double getExpenditure() {
        return 0;
    }

    @Override public double getRevenue() {
        return 0;
    }

    public int getDeliveryTime() {
        return 0;
    }

    public CalculatedRoute getCalculatedRoute() {
        return calculatedRoute;
    }
}
