package kps.gui.windows.dialogs;

import kps.gui.FormDialog;
import kps.xml.objects.Location;
import kps.xml.objects.Price;
import kps.xml.objects.Simulation;
import kps.xml.objects.enums.DayOfWeek;
import kps.xml.objects.enums.Priority;
import kps.xml.objects.enums.TransportType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class PriceDialog extends FormDialog {
    private @NotNull Price price;
    private boolean isInDocument;

    private enum FieldNames {
        LocationTo,
        LocationFrom,
        Priority,
        WeightCost,
        VolumeCost,
    }

    public PriceDialog(Frame owner, Simulation simulation) {
        this(owner, simulation, null);
    }

    public PriceDialog(Frame owner, Simulation simulation, @Nullable Price previousPrice) {
        this(owner, simulation, false, previousPrice);
    }
    public PriceDialog(@Nullable Frame owner, @NotNull Simulation simulation, boolean isUpdate, @Nullable Price previousPrice) {
        super(owner, previousPrice == null ? "Add Price" : "Edit Price", true, simulation);
        if(isUpdate) {
            isInDocument = false;
            setTitle("Update route");
        } else {
            this.isInDocument = previousPrice != null;
        }
        this.price = previousPrice == null ? new Price(simulation) : previousPrice;
        buildDialog();
        setVisible(true);
    }



    @Override
    protected JComponent[][] getAllFields() {
        return new JComponent[][]{
                getField(FieldNames.LocationTo, "Location to", price.getTo(), Location.class),
                getField(FieldNames.LocationFrom, "Location from", price.getFrom(), Location.class),
                getField(FieldNames.Priority, "Priority", price.getPriority(), TransportType.class),
                getField(FieldNames.WeightCost, "Weight cost (cents/grams)", price.getWeightCost(), Integer.class),
                getField(FieldNames.VolumeCost, "Volume cost (cents/cm^3)", price.getVolumeCost(), Integer.class),
        };
    }

    protected void save() {
        if (!validateFields()) return;
        Map<Object, Object> entries = getAllValues();
        price.setTo((Location)entries.get(FieldNames.LocationTo));
        price.setFrom((Location)entries.get(FieldNames.LocationFrom));
        price.setPriority((Priority) entries.get(FieldNames.Priority));
        price.setVolumeCost((Integer)entries.get(FieldNames.VolumeCost));
        price.setWeightCost((Integer)entries.get(FieldNames.WeightCost));

        if(!isInDocument) {
            simulation.getPrices().add(price);
        }
        cancel();
    }

    protected boolean cancel() {
        dispose();
        return true;
    }

}
