package kps.gui.windows;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import kps.gui.FormDialog;
import kps.xml.objects.Cost;
import kps.xml.objects.Simulation;
import kps.xml.objects.enums.DayOfWeek;
import kps.xml.objects.enums.TransportType;

import javax.swing.*;
import java.awt.*;

public class AddRoute extends FormDialog {
    @NotNull private Cost route;
    private boolean isInDocument;

    public AddRoute(Frame owner, Simulation simulation) {
        this(owner, simulation, null);
    }

    public AddRoute(Frame owner, Simulation simulation, @Nullable Cost previousRoute) {
        super(owner, "Add route", true, simulation);
        this.isInDocument = previousRoute != null;
        this.route = previousRoute == null ? new Cost(simulation) : previousRoute;

        buildDialog();
        setVisible(true);
    }

    @Override
    protected JComponent[][] getAllFields() {
        JComponent components[][] = {
                getField("Company name", route.getCompany() == null ? "" : route.getCompany()),
                getField("Location to", route.getTo() != null ? route.getTo().getName() : ""),
                getField("Location from", route.getFrom() != null ? route.getFrom().getName() : ""),
                getField("Transportation type", route.getTransportType() == null ? TransportType.Air : route.getTransportType()),
                getField("Weight cost", route.getWeightCost()),
                getField("Volume cost", route.getVolumeCost()),
                getField("Max weight", route.getMaxWeight()),
                getField("Max volume", route.getMaxVolume()),
                getField("Duration", route.getDuration()),
                getField("Day of the week", route.getDay() == null ? DayOfWeek.Monday : route.getDay()),
                getField("Frequency of delivery", route.getFrequency())
        };
        return components;
    }

    protected void save() {


        if(!isInDocument) {
            simulation.getCosts().add(route);
        }
        cancel();
    }

    protected void cancel() {
        dispose();
    }

}
