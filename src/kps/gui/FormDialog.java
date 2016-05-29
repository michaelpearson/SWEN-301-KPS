package kps.gui;

import kps.gui.models.LocationsModel;
import kps.gui.windows.LocationDialog;
import kps.xml.objects.Location;
import kps.xml.objects.Simulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.HashMap;
import java.util.Map;

public abstract class FormDialog extends JDialog {

    protected Simulation simulation;
    private Map<Object, Gettable<Object>> valueMap = new HashMap<>();
    private Map<Object, Gettable<JComponent>> componentMap = new HashMap<>();
    private KeyEventDispatcher keyboardDispather;


    private interface Gettable<T> {
        T getValue();
    }

    private static class Field<T> {
        JComponent field;
        Gettable<T> getter;
    }

    public FormDialog(Frame owner, String title, boolean modal, Simulation simulation) {
        super(owner, title, modal);
        this.simulation = simulation;
        setLocationRelativeTo(null);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyboardDispather = e -> {
            JRootPane component1 = ((JComponent)e.getSource()).getRootPane();
            if(e.getKeyCode() == 27 && (component1.getContentPane() == getContentPane())) {
                return cancel();
            }
            return false;
        });
    }

    public FormDialog(String title, Simulation simulation) {
        this(null, title, false, simulation);
    }

    protected void buildDialog() {
        JComponent[][] components = getAllFields();

        setLayout(new BorderLayout());
        JPanel formPanel = new JPanel();
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridLayout layout = new GridLayout(0, 2);
        layout.setHgap(5);
        layout.setVgap(5);
        formPanel.setLayout(layout);
        for(JComponent[] c : components) {
            for(int a = 0;a < 2;a++) {
                formPanel.add(c[a]);
            }
        }

        buildOptionButtons(formPanel);

        add(formPanel, BorderLayout.CENTER);
        setResizable(false);
        pack();

    }

    @Override
    public void dispose() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(keyboardDispather);
        super.dispose();
    }

    protected void buildOptionButtons(JPanel formPanel) {
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> save());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> cancel());

        formPanel.add(cancelButton);
        formPanel.add(saveButton);
    }
    protected JComponent[] getField(Object tag, String fieldName, Object fieldValue, Class c, String tooltip) {
        JComponent[] components = getField(tag, fieldName, fieldValue, c);
        components[1].setToolTipText(tooltip);
        return components;
    }

    @SuppressWarnings("unchecked")
    protected JComponent[] getField(Object tag, String fieldName, Object fieldValue, Class c) {
        JLabel label = new JLabel(fieldName);
        final JComponent field;
        Gettable<Object> fieldGetter;

        if(c.equals(Integer.class)) {
            field = new JSpinner(new SpinnerNumberModel((Integer)fieldValue, null, null, 1));
            fieldGetter = ((JSpinner) field)::getValue;
        } else if(c.equals(String.class)) {
            field = new JTextField(String.valueOf(fieldValue == null ? "" : fieldValue));
            fieldGetter = ((JTextField)field)::getText;
        } else if(Enum.class.isAssignableFrom(c)) {
            try {
                field = new JComboBox<>((Enum<?>[])c.getMethod("values").invoke(null));
                ((JComboBox) field).setSelectedItem(fieldValue);
                fieldGetter = ((JComboBox) field)::getSelectedItem;
            } catch (Exception ignore) {
                throw new RuntimeException("This will never happen");
            }
        } else if(c.equals(Location.class)) {
            Field<Object> locationField = makeLocationField((Location)fieldValue);
            fieldGetter = locationField.getter;
            field = locationField.field;
        } else {
            throw new RuntimeException("Unsupported field type");
        }
        field.setPreferredSize(new Dimension(200, 30));
        valueMap.put(tag, fieldGetter);
        componentMap.put(tag, () -> field);
        return new JComponent[] {label, field};
    }

    private Field<Object> makeLocationField(Location fieldValue) {
        JComboBox<Location> comboBox = new JComboBox<>();
        LocationsModel model = new LocationsModel(simulation);
        comboBox.setModel(model);
        comboBox.setSelectedItem(fieldValue);
        comboBox.addItemListener((e) -> {
            if(e.getStateChange() != ItemEvent.SELECTED) {
                return;
            }
            if(comboBox.getSelectedItem() == model.getDummyLocation()) {
                FormDialog dialog = new LocationDialog(null, "Add a new location", true, simulation);
                String newLocation = (String)dialog.getComponentValue(LocationDialog.NAME);
                if(newLocation == null || newLocation.equals("")) {
                    JOptionPane.showMessageDialog(this, "Sorry one or more fields was invalid. Please fill in all of the fields and try again.", "Invalid input", JOptionPane.WARNING_MESSAGE);
                    comboBox.setSelectedIndex(0);
                }
                Location l = new Location(simulation);
                l.setName(newLocation);
                simulation.getLocations().add(l);
                comboBox.setSelectedItem(l);
            }
        });
        Field<Object> field = new Field<>();
        field.getter = () -> {
            if(comboBox.getSelectedItem() == model.getDummyLocation()) {
                return null;
            }
            return comboBox.getSelectedItem();
        };
        field.field = comboBox;
        return field;
    }



    protected JComponent getField(Object tag) {
        return componentMap.get(tag).getValue();
    }

    private Object getComponentValue(Object tag) {
        Gettable<Object> component = valueMap.get(tag);
        if(component == null) {
            throw new RuntimeException("Could not find component");
        }
        return component.getValue();
    }

    protected Map<Object, Object> getAllValues() {
        Map<Object, Object> values = new HashMap<>();
        for(Map.Entry<Object, Gettable<Object>> c : valueMap.entrySet()) {
            values.put(c.getKey(), c.getValue().getValue());
        }
        return values;
    }

    protected boolean fieldsValid(){
        for(Map.Entry<Object, Gettable<Object>> c : valueMap.entrySet()) {
            if (c.getValue().getValue() == null || c.getValue().getValue().toString().length() == 0) {
                JOptionPane.showMessageDialog(this, "Sorry one or more fields was invalid. Please fill in all of the fields and try again.", "Invalid input", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }

    protected abstract JComponent[][] getAllFields();
    protected abstract void save();
    protected abstract boolean cancel();

}
