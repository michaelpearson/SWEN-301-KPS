package kps.gui.windows;

import kps.gui.util.KeyListenerSlim;
import kps.gui.windows.form.FormBuilder;
import kps.gui.windows.form.FormDialog;
import kps.xml.objects.Simulation;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.Map;

public class Login extends FormDialog {

    public Login(Simulation s) {
        super("Login", s);
        buildDialog();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        pack();
        JTextField passwordField = (JTextField)getField("password");
        passwordField.requestFocus();
        passwordField.addKeyListener(new KeyListenerSlim() {
            @Override public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == 10) {
                    save();
                }
            }
        });
        setVisible(true);
    }

    @SuppressWarnings("WeakerAccess") //This class is used with reflection!
    public enum UserName {
        Manager,
        Clerk
    }

    @Override
    protected void initializeForm(FormBuilder builder) {
        builder.addEnumField("username", "User", UserName.Manager, UserName.class, null);
        builder.addStringField("password", "Password", "", null);
    }

    @Override
    protected void save() {
        Map<Object, Object> values = getAllFieldValues();
        UserName username = (UserName)values.get("username");
        String password = values.get("password").toString();
        if(username == UserName.Clerk && password.equals("")) {
            doLogin(username);
        } else if(username == UserName.Manager && password.equals("")) {
            doLogin(username);
        } else {
            JOptionPane.showMessageDialog(this, "Sorry the password you entered was incorrect.", "Invalid password", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void doLogin(UserName user) {
        dispose();
        new Home(simulation, user);
    }

    @Override
    protected boolean cancel() {
        System.exit(0);
        return true;
    }

    @Override
    protected void buildOptionButtons(JPanel panel) {
        JButton exitButton = new JButton("Exit");
        JButton loginButton = new JButton("Login");

        exitButton.addActionListener(e -> cancel());
        loginButton.addActionListener(e -> save());

        panel.add(exitButton);
        panel.add(loginButton);
    }
}
