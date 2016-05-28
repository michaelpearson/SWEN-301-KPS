package kps.gui.windows;

import kps.gui.FormDialog;
import kps.gui.util.KeyListenerSlim;
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


    public enum UserName {
        Manager,
        Clerk
    }

    @Override
    protected JComponent[][] getAllFields() {
        return new JComponent[][] {
                getField("username", "User", UserName.Manager),
                getField("password", "Password", "")
        };
    }

    @Override
    protected void save() {
        Map<Object, Object> values = getAllValues();
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
    protected void cancel() {
        System.exit(0);
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
