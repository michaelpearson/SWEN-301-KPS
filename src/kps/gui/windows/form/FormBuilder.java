package kps.gui.windows.form;

import kps.gui.models.LocationsModel;
import kps.gui.windows.form.dialogs.LocationDialog;
import kps.xml.objects.Simulation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper class to {@link FormDialog}. Used for building the form and taking care of field logic.
 */
@SuppressWarnings("WeakerAccess")
public class FormBuilder {
    private final Simulation simulation;
    private final JDialog owner;
    private Map<Object, FormDialog.Field> fields = new HashMap<>();

    /**
     * Only to be used from {@link FormDialog} to create a form builder for a FormDialog
     * @param s the simulation object used to get locations for the location object type
     * @param owner the frame owner for use with popup windows
     * @param fields the fields object of the FormDialog
     */
    FormBuilder(Simulation s, JDialog owner, Map<Object, FormDialog.Field> fields) {
        this.simulation = s;
        this.owner = owner;
        this.fields = fields;
    }

    /**
     * Builds a label for a {@link FormDialog} label
     * @param label the text
     * @param tooltip the tooltip
     * @return the label component
     */
    private JComponent getLabel(@NotNull String label, @Nullable String tooltip) {
        JLabel jlabel = new JLabel(label);
        if(tooltip != null) {
            jlabel.setToolTipText(tooltip);
        }
        return jlabel;
    }

    /**
     * Builds a field which will only accept integers
     * @param fieldTag the tag to attach to the component which can be used to find the component later.
     * @param label the label to attach to the field
     * @param value the initial value of the field
     */
    public void addIntegerField(@NotNull Object fieldTag, @NotNull String label, int value) {
        addIntegerField(fieldTag, label, value, null);
    }

    /**
     * Builds a field which will only accept integers {@link FormBuilder#addIntegerField(Object, String, int)}
     * @param fieldTag the tag to attach to the component which can be used to find the component later.
     * @param label the label to attach to the field
     * @param value the initial value of the field
     * @param tooltip the tooltip for this field
     */
    public void addIntegerField(@NotNull Object fieldTag, @NotNull String label, int value, @Nullable String tooltip) {
        FormDialog.Field field = new FormDialog.Field();
        field.label = getLabel(label, tooltip);
        field.field = new JSpinner(new SpinnerNumberModel(value, null, null, 1));
        field.getter = ((JSpinner) field.field)::getValue;
        field.field.setPreferredSize(new Dimension(200, 30));
        fields.put(fieldTag, field);
    }

    /**
     * Builds a field which will only accept a string
     * @param fieldTag the tag to attach to the component which can be used to find the component later.
     * @param label the label to attach to the field
     * @param value the initial value of the field
     */
    public void addStringField(@NotNull Object fieldTag, @NotNull String label, @Nullable String value) {
        addStringField(fieldTag, label, value, null);
    }

    /**
     * Builds a field which will only accept a string
     * @param fieldTag the tag to attach to the component which can be used to find the component later.
     * @param label the label to attach to the field
     * @param value the initial value of the field
     * @param tooltip the tooltip for this field
     */
    public void addStringField(@NotNull Object fieldTag, @NotNull String label, @Nullable String value, @Nullable String tooltip) {
        FormDialog.Field field = new FormDialog.Field();
        field.label = getLabel(label, tooltip);
        field.field = new JTextField(value);
        field.getter = ((JTextField) field.field)::getText;
        field.field.setPreferredSize(new Dimension(200, 30));
        fields.put(fieldTag, field);
    }

    /**
     * Builds a dropdown for the specified enum
     * @param fieldTag the tag to attach to the component which can be used to find the component later.
     * @param label the label to attach to the field
     * @param value the enum which starts selected
     * @param enumClass the class of the enum which should be used to build the dropdown
     */
    public void addEnumField(@NotNull Object fieldTag, @NotNull String label, @Nullable Enum value, @NotNull Class<? extends Enum> enumClass) {
        addEnumField(fieldTag, label, value, enumClass, null);
    }

    /**
     * Builds a dropdown for the specified enum
     * @param fieldTag the tag to attach to the component which can be used to find the component later.
     * @param label the label to attach to the field
     * @param value the enum which starts selected
     * @param enumClass the class of the enum which should be used to build the dropdown
     * @param tooltip the tooltip for this field
     */
    public void addEnumField(@NotNull Object fieldTag, @NotNull String label, @Nullable Enum value, @NotNull Class<? extends Enum> enumClass, @Nullable String tooltip) {
        FormDialog.Field field = new FormDialog.Field();
        field.label = getLabel(label, tooltip);
        if(value != null && !(value.getClass().equals(enumClass))) {
            throw new RuntimeException("Invalid enum value for passed enum class");
        }
        try {
            field.field = new JComboBox<>((Enum<?>[])enumClass.getMethod("values").invoke(null));
            ((JComboBox)field.field).setSelectedItem(value);
            field.getter = ((JComboBox)field.field)::getSelectedItem;
        } catch (Exception ignore) {
            throw new RuntimeException("This will never happen"); //lol
        }
        field.field.setPreferredSize(new Dimension(200, 30));
        fields.put(fieldTag, field);
    }

    /**
     * Builds a dropdown which displays locations which come from the simulation object.
     * @param fieldTag the tag to attach to the component which can be used to find the component later.
     * @param label the label to attach to the field
     * @param location the initial location to have selected
     */
    public void addLocationField(@NotNull Object fieldTag, @NotNull String label, @Nullable String location) {
        addLocationField(fieldTag, label, location, null);
    }

    /**
     *
     * @param fieldTag the tag to attach to the component which can be used to find the component later.
     * @param label the label to attach to the field
     * @param location the initial location to have selected
     * @param tooltip the tooltip for this field
     */
    public void addLocationField(@NotNull Object fieldTag, @NotNull String label, @Nullable String location, @Nullable String tooltip) {
        FormDialog.Field<Object> field = new FormDialog.Field<>();
        field.label = getLabel(label, tooltip);
        JComboBox<String> comboBox = new JComboBox<>();
        field.field = comboBox;

        LocationsModel model = new LocationsModel(simulation);
        comboBox.setModel(model);
        comboBox.setSelectedItem(location);
        comboBox.addItemListener((e) -> {
            if(e.getStateChange() != ItemEvent.SELECTED) {
                return;
            }
            if(comboBox.getSelectedItem() == model.getDummyLocation()) {
                FormDialog dialog = new LocationDialog(null, "Add a new location", true, simulation);
                String newLocation = (String)dialog.getValue(LocationDialog.NAME);
                if(newLocation == null || newLocation.equals("")) {
                    JOptionPane.showMessageDialog(owner, "Sorry one or more fields was invalid. Please fill in all of the fields and try again.", "Invalid input", JOptionPane.WARNING_MESSAGE);
                    comboBox.setSelectedIndex(0);
                }
                Simulation.addTempLocation(newLocation);
                model.updateModel();
                comboBox.setSelectedItem(newLocation);
            }
        });
        field.getter = () -> {
            if(comboBox.getSelectedItem() == model.getDummyLocation()) {
                return null;
            }
            return comboBox.getSelectedItem();
        };
        field.field.setPreferredSize(new Dimension(200, 30));
        fields.put(fieldTag, field);
    }

    /**
     * Builds a boolean checkbox
     * @param fieldTag the tag to attach to the component which can be used to find the component later.
     * @param label the label to attach to the field
     * @param value the enum which starts selected
     */
    public void addBooleanField(@NotNull Object fieldTag, @NotNull String label, boolean value) {
        addBooleanField(fieldTag, label, value, null);
    }

    /**
     * Builds a boolean checkbox
     * @param fieldTag the tag to attach to the component which can be used to find the component later.
     * @param label the label to attach to the field
     * @param value the enum which starts selected
     * @param tooltip the tooltip for this field
     */
    public void addBooleanField(@NotNull Object fieldTag, @NotNull String label, boolean value, @Nullable String tooltip) {
        FormDialog.Field<Object> field = new FormDialog.Field<>();
        field.label = getLabel(label, tooltip);

        JCheckBox cb = new JCheckBox();
        field.field = cb;
        cb.setSelected(value);
        field.getter = cb::isSelected;
        fields.put(fieldTag, field);
    }

}
