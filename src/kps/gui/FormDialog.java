package kps.gui;

import kps.xml.objects.Simulation;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public abstract class FormDialog extends JDialog {

    protected Simulation simulation;
    private Map<Object, Gettable<Object>> valueMap = new HashMap<>();
    private Map<Object, Gettable<JComponent>> componentMap = new HashMap<>();

    private interface Gettable<T> {
        T getValue();
    }

    public FormDialog(Frame owner, String title, boolean modal, Simulation simulation) {
        super(owner, title, modal);
        this.simulation = simulation;
        setLocationRelativeTo(null);
    }

    public FormDialog(String title, Simulation simulation) {
        super();
        setTitle(title);
        setLocationRelativeTo(null);
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

        buildOptionButtons(formPanel);

        add(formPanel, BorderLayout.CENTER);
        pack();

    }

    protected void buildOptionButtons(JPanel formPanel) {
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> save());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> cancel());

        formPanel.add(cancelButton);
        formPanel.add(saveButton);
    }

    protected JComponent[] getField(Object tag, String fieldName, Object fieldValue) {
        JLabel label = new JLabel(fieldName);
        final JComponent field;
        Gettable<Object> fieldGetter;

        if(fieldValue.getClass().equals(Integer.class)) {
            field = new JSpinner(new SpinnerNumberModel((Integer)fieldValue, null, null, 1));
            fieldGetter = ((JSpinner) field)::getValue;
        } else if(fieldValue.getClass().equals(String.class)) {
            field = new JTextField(String.valueOf(fieldValue));
            fieldGetter = ((JTextField)field)::getText;
        } else if(Enum.class.isAssignableFrom(fieldValue.getClass())) {
            Class c = (Class)fieldValue.getClass();
            try {
                field = new JComboBox<>((Enum[])c.getMethod("values").invoke(null));
                ((JComboBox) field).setSelectedItem(fieldValue);
                fieldGetter = ((JComboBox) field)::getSelectedItem;
            } catch (Exception ignore) {
                throw new RuntimeException("This will never happen");
            }
        } else {
            throw new RuntimeException("Unsupported field type");
        }
        valueMap.put(tag, fieldGetter);
        componentMap.put(tag, () -> field);
        return new JComponent[] {label, field};
    }

    protected JComponent getField(Object tag) {
        return componentMap.get(tag).getValue();
    }

    protected Object getComponentValue(Object tag) {
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

    protected abstract JComponent[][] getAllFields();
    protected abstract void save();
    protected abstract void cancel();

}
