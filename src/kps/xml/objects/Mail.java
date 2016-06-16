package kps.xml.objects;

import kps.gui.windows.form.dialogs.MailDialog;
import kps.xml.objects.abstracts.BusinessEventWithLocation;
import kps.xml.objects.enums.DayOfWeek;
import kps.xml.objects.enums.Priority;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.awt.*;
import java.util.Iterator;
import java.util.Set;

@XmlAccessorType(XmlAccessType.FIELD) public class Mail extends BusinessEventWithLocation {
    @XmlElement(name="day") private DayOfWeek day;
    @XmlElement(name="weight") private int weight;
    @XmlElement(name="volume") private int volume;
    @XmlElement(name="priority") private Priority priority;
    @XmlElement(name="calculatedRoute") private CalculatedRoute calculatedRoute;

    public Mail(Simulation s) {
        super(s);
    }

    @SuppressWarnings("unused")
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

    @Nullable public Priority getPriority() { return priority; }

    public int getWeight() {
        return weight;
    }

    public int getVolume() {
        return volume;
    }

    @Nullable public DayOfWeek getDay() {
        return day;
    }

    @Override public double getExpenditure() {
        double expenditure = 0;
        calculatedRoute = simulation.buildCalculatedRoute(getFrom(),getTo(), priority);

        for (Route r : calculatedRoute.getRoutes()){
            expenditure += r.getVolumeCost() * volume + r.getWeightCost() * weight;
        }
        return expenditure;
    }

    @Override public double getRevenue() {
        double revenue = 0;
        for (CustomerPrice cp : simulation.getCustomerPrices()){
            if (priority == cp.getPriority()) {
                if (!priority.isDomestic() && !getTo().equals(cp.getTo())) continue;
                revenue += cp.getVolumeCost() * volume + cp.getWeightCost() * weight;
            }
        }
        return revenue;
    }

    public int getDeliveryTime() {
        int time = 0;
        calculatedRoute = simulation.buildCalculatedRoute(getFrom(),getTo(), priority);
        DayOfWeek day = getDay();
        for (Route r : calculatedRoute.getRoutes()){
            time += r.getDuration() + (day.ordinal() > r.getDay().ordinal()? 7 - day.ordinal() + r.getDay().ordinal() : r.getDay().ordinal() - day.ordinal());
            day = r.getDay();
        }
        return time;
    }

    @Nullable public CalculatedRoute getCalculatedRoute() {
        return calculatedRoute;
    }
}
