package kps.xml.objects;

import kps.gui.windows.form.dialogs.MailDialog;
import kps.xml.objects.abstracts.BusinessEventWithLocation;
import kps.xml.objects.enums.DayOfWeek;
import kps.xml.objects.enums.Priority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.awt.*;

@XmlAccessorType(XmlAccessType.NONE) public class Mail extends BusinessEventWithLocation {
    @XmlElement(name="day") private DayOfWeek day;
    @XmlElement(name="weight") private int weight;
    @XmlElement(name="volume") private int volume;
    @XmlElement(name="priority") private Priority priority;
    @XmlElement(name="calculatedroute") private CalculatedRoute calculatedRoute;

    public Mail(Simulation s) {
        super(s);
    }

    @SuppressWarnings("unused")
    public Mail() {}

    @Override public String getEventType() {
        return "Mail delivery";
    }

    @Override public void edit(@NotNull Frame owner) {
        new MailDialog(owner, getSimulation(), this);
    }

    public void setPriority(@NotNull Priority priority) {
        this.priority = priority;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void setDay(@NotNull DayOfWeek day) {
        this.day = day;
    }

    public void setCalculatedRoute(@NotNull CalculatedRoute calculatedRoute) {
        this.calculatedRoute = calculatedRoute;
    }

    @Nullable public Priority getPriority() {
        return priority;
    }

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
        if(calculatedRoute == null) {
            throw new RuntimeException("Cannot calculate expenditure without a calculated route");
        }
        double expenditure = 0;
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
        if(calculatedRoute == null) {
            throw new RuntimeException("Cannot calculate delivery time without a calculated route");
        }
        int time = 0;
        DayOfWeek day = getDay();
        for (Route r : calculatedRoute.getRoutes()){
            if(day == null) {
                throw new RuntimeException("Cannot calculate delivery time without day");
            }
            time += r.getDuration() + (day.ordinal() > r.getDay().ordinal()? 7 - day.ordinal() + r.getDay().ordinal() : r.getDay().ordinal() - day.ordinal());
            day = r.getDay();
        }
        return time;
    }

    @Nullable public CalculatedRoute getCalculatedRoute() {
        return calculatedRoute;
    }
}
