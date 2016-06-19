package kps.gui.windows.form;

import kps.xml.objects.Simulation;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This is a class which builds a generic form to collect input data from the user
 */
public abstract class FormDialog extends JDialog {
    protected Simulation simulation;
    //Linked hash map because it retains order.
    private Map<Object, Field> fields = new LinkedHashMap<>();
    private KeyEventDispatcher keyboardDispatcher;

    /**
     * An interface used to get the return value from a field in a standardised manner
     * @param <T>
     */
    interface Gettable<T> {
        T getValue();
    }

    /**
     * A simple representation for a "field". Contains all of the components related to the field and a method to get
     * the return value.
     * @param <T>
     */
    static class Field<T> {
        JComponent field;
        JComponent label;
        /**
         * a method to get the return value of the field.
         */
        Gettable<T> getter;
    }

    /**
     * Create a form.
     * @param owner the frames parent
     * @param title the title of the frame
     * @param modal whether the frame should block input to other windows
     * @param simulation the simulation to work from
     */
    public FormDialog(Frame owner, String title, boolean modal, Simulation simulation) {
        super(owner, title, modal);
        this.simulation = simulation;
        setLocationRelativeTo(null);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyboardDispatcher = e -> {
            JRootPane component1 = ((JComponent) e.getSource()).getRootPane();
            return e.getKeyCode() == 27 && (component1.getContentPane() == getContentPane()) && cancel();
        });
    }

    public FormDialog(String title, Simulation simulation) {
        this(null, title, false, simulation);
    }

    protected void buildDialog() {
        initializeForm(new FormBuilder(simulation, this, fields));

        setLayout(new BorderLayout());
        JPanel formPanel = new JPanel();
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridLayout layout = new GridLayout(0, 2);
        layout.setHgap(5);
        layout.setVgap(5);
        formPanel.setLayout(layout);

        for(Map.Entry<Object, Field> e : fields.entrySet()) {
            formPanel.add(e.getValue().label);
            formPanel.add(e.getValue().field);
        }

        buildOptionButtons(formPanel);

        add(formPanel, BorderLayout.CENTER);
        setResizable(false);
        pack();

    }

    @Override
    public void dispose() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(keyboardDispatcher);
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


    protected Object getValue(Object tag) {
        Field field = fields.get(tag);
        if(field == null) {
            throw new RuntimeException("Could not find component");
        }
        return field.getter.getValue();
    }

    protected JComponent getField(Object tag) {
        return fields.get(tag).field;
    }

    protected Map<Object, Object> getAllFieldValues() {
        Map<Object, Object> values = new HashMap<>();
        for(Map.Entry<Object, Field> c : fields.entrySet()) {
            values.put(c.getKey(), c.getValue().getter.getValue());
        }
        return values;
    }

    protected boolean validateFields(){
        for(Map.Entry<Object, Field> c : fields.entrySet()) {
            if (c.getValue().getter.getValue() == null || c.getValue().getter.getValue().toString().length() == 0) {
                JOptionPane.showMessageDialog(this, "Sorry one or more fields was invalid. Please fill in all of the fields and try again.", "Invalid input", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }

    protected abstract void initializeForm(FormBuilder builder);
    protected abstract void save();
    protected abstract boolean cancel();

}
