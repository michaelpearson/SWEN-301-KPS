package kps.xml.objects;

import kps.gui.windows.form.dialogs.CustomerPriceDialog;
import kps.xml.objects.abstracts.BusinessEventWithLocation;
import kps.xml.objects.enums.Priority;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.awt.*;

/**
 * The route object represents a route on a given transport provider.
 */
@XmlAccessorType(XmlAccessType.NONE) public class CustomerPrice extends BusinessEventWithLocation {
    @XmlElement(name="priority") private Priority priority;
    @XmlElement(name="weightcost") private int weightCost;
    @XmlElement(name="volumecost") private int volumeCost;

    CustomerPrice() {}

    @Override
    @NotNull public String getEventType() {
        return "Customer Price";
    }

    public CustomerPrice(@NotNull Simulation s) {
        super(s);
    }

    @Override public void edit(Frame owner) {
        new CustomerPriceDialog(owner, getSimulation(), this);
    }

    public void update(Frame owner) {
        new CustomerPriceDialog(owner, getSimulation(), true, this.copy());
    }

    public void setWeightCost(int weightCost) {
        this.weightCost = weightCost;
    }

    public void setVolumeCost(int volumeCost) {
        this.volumeCost = volumeCost;
    }


    public int getWeightCost() {
        return weightCost;
    }

    public int getVolumeCost() {
        return volumeCost;
    }

    @NotNull public Priority getPriority() {
        if(priority == null) {
            priority = Priority.values()[0];
        }
        return priority;
    }

    public void setPriority(@NotNull Priority priority) {
        this.priority = priority;
    }

    @Override public double getExpenditure() {
        return 0;
    }

    @Override public double getRevenue() {
        return 0;
    }

    private CustomerPrice copy() {
        CustomerPrice object = new CustomerPrice(simulation);
        object.setPriority(getPriority());
        object.setWeightCost(getWeightCost());
        object.setVolumeCost(getVolumeCost());
        object.setFrom(getFrom());
        object.setTo(getTo());
        return object;
    }
}