package kps.gui.windows.form.dialogs;

import kps.gui.windows.form.FormBuilder;
import kps.gui.windows.form.FormDialog;
import kps.xml.objects.Route;
import kps.xml.objects.Simulation;
import kps.xml.objects.enums.DayOfWeek;
import kps.xml.objects.enums.TransportType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Map;

public class RouteDialog extends FormDialog {
    private @NotNull Route route;
    private boolean isInDocument;

    private enum FieldNames {
        CompanyName,
        LocationTo,
        LocationFrom,
        TransportType,
        WeightCost,
        VolumeCost,
        MaxWeight,
        MaxVolume,
        Duration,
        DayOfWeek,
        Frequency,
        Discontinued
    }

    public RouteDialog(Frame owner, Simulation simulation) {
        this(owner, simulation, null);
    }

    public RouteDialog(Frame owner, Simulation simulation, @Nullable Route previousRoute) {
        this(owner, simulation, false, previousRoute);
    }
    public RouteDialog(@Nullable Frame owner, @NotNull Simulation simulation, boolean isUpdate, @Nullable Route previousRoute) {
        super(owner, previousRoute == null ? "Add route" : "Edit route", true, simulation);
        if(isUpdate) {
            isInDocument = false;
            setTitle("Update route");
        } else {
            this.isInDocument = previousRoute != null;
        }
        this.route = previousRoute == null ? new Route(simulation) : previousRoute;
        buildDialog();
        setVisible(true);
    }



    @Override protected void initializeForm(FormBuilder builder) {
        builder.addStringField(FieldNames.CompanyName, "Company name", route.getCompany());
        builder.addLocationField(FieldNames.LocationTo, "Location to", route.getTo());
        builder.addLocationField(FieldNames.LocationFrom, "Location from", route.getFrom());
        builder.addEnumField(FieldNames.TransportType, "Transportation type", route.getTransportType(), TransportType.class);
        builder.addIntegerField(FieldNames.WeightCost, "Weight cost (cents/grams)", route.getWeightCost());
        builder.addIntegerField(FieldNames.VolumeCost, "Volume cost (cents/cm^3)", route.getVolumeCost());
        builder.addIntegerField(FieldNames.MaxWeight, "Max weight (grams)", route.getMaxWeight());
        builder.addIntegerField(FieldNames.MaxVolume, "Max volume (cm^3)", route.getMaxVolume());
        builder.addIntegerField(FieldNames.Duration, "Duration (days)", route.getDuration());
        builder.addEnumField(FieldNames.DayOfWeek, "Day of the week", route.getDay(), DayOfWeek.class);
        builder.addIntegerField(FieldNames.Frequency, "Frequency of delivery (days)", route.getFrequency());
        builder.addBooleanField(FieldNames.Discontinued, "Is discontinued", route.isDiscontinued());
    }

    protected void save() {
        if (!validateFields()) return;
        Map<Object, Object> entries = getAllFieldValues();
        route.setCompany((String)entries.get(FieldNames.CompanyName));
        route.setTo((String)entries.get(FieldNames.LocationTo));
        route.setFrom((String)entries.get(FieldNames.LocationFrom));
        route.setDay((DayOfWeek)entries.get(FieldNames.DayOfWeek));
        route.setDuration((Integer)entries.get(FieldNames.Duration));
        route.setFrequency((Integer)entries.get(FieldNames.Frequency));
        route.setMaxVolume((Integer)entries.get(FieldNames.MaxVolume));
        route.setMaxWeight((Integer)entries.get(FieldNames.MaxWeight));
        route.setTransportType((TransportType)entries.get(FieldNames.TransportType));
        route.setVolumeCost((Integer)entries.get(FieldNames.VolumeCost));
        route.setWeightCost((Integer)entries.get(FieldNames.WeightCost));
        route.setDiscontinued((Boolean)entries.get(FieldNames.Discontinued));
        if(!isInDocument) {
            simulation.getRoutes().add(route);
        }
        cancel();
    }

    protected boolean cancel() {
        dispose();
        return true;
    }

}
