package kps.gui;

import kps.xml.objects.Simulation;
import kps.xml.objects.enums.DayOfWeek;
import kps.xml.objects.enums.TransportType;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public abstract class FormDialog extends JDialog {

    protected Simulation simulation;
    private Map<Object, Gettable> componentMap = new HashMap<>();

    private interface Gettable {
        Object getValue();
    }

    public FormDialog(Frame owner, String title, boolean modal, Simulation simulation) {
        super(owner, title, modal);
        this.simulation = simulation;
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

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> save());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> cancel());

        formPanel.add(cancelButton);
        formPanel.add(saveButton);

        add(formPanel, BorderLayout.CENTER);
        pack();
    }

    protected JComponent[] getField(Object tag, String fieldName, Object fieldValue) {
        JLabel label = new JLabel(fieldName);
        final JComponent field;
        Gettable fieldGetter;
        if(fieldValue.getClass().equals(Integer.class)) {
            field = new JSpinner(new SpinnerNumberModel((Integer)fieldValue, null, null, 1));
            fieldGetter = ((JSpinner) field)::getValue;
        } else if(fieldValue.getClass().equals(String.class)) {
            field = new JTextField(String.valueOf(fieldValue));
            fieldGetter = ((JTextField)field)::getText;
        } else if(fieldValue.getClass().equals(TransportType.class)) {
            field = new JComboBox<>(TransportType.values());
            ((JComboBox)field).setSelectedItem(fieldValue);
            fieldGetter = ((JComboBox) field)::getSelectedItem;
        } else if(fieldValue.getClass().equals(DayOfWeek.class)) {
            field = new JComboBox<>(DayOfWeek.values());
            ((JComboBox)field).setSelectedItem(fieldValue);
            fieldGetter = ((JComboBox) field)::getSelectedItem;
        } else {
            throw new RuntimeException("Unsupported field type");
        }
        componentMap.put(tag, fieldGetter);
        return new JComponent[] {label, field};
    }

    protected Object getComponentValue(Object tag) {
        Gettable component = componentMap.get(tag);
        if(component == null) {
            throw new RuntimeException("Could not find component");
        }
        return component.getValue();
    }

    protected Map<Object, Object> getAllValues() {
        Map<Object, Object> values = new HashMap<>();
        for(Map.Entry<Object, Gettable> c : componentMap.entrySet()) {
            values.put(c.getKey(), c.getValue().getValue());
        }
        return values;
    }

    protected abstract JComponent[][] getAllFields();
    protected abstract void save();
    protected abstract void cancel();

}
