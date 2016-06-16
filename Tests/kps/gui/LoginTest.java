package kps.gui;

import kps.gui.windows.Home;
import kps.gui.windows.Login;
import kps.gui.windows.form.FormDialog;
import kps.xml.SimulationXML;
import kps.xml.objects.Simulation;
import org.junit.Assert;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;

public class LoginTest {

    @Test
    public void validLogin_1() {
        Login loginWindow = new Login(makeSim());
        JTextField passwordBox = (JTextField)loginWindow.getField("password");
        JComboBox usernameField = (JComboBox)loginWindow.getField("username");
        usernameField.setSelectedItem(Login.UserName.Clerk);
        passwordBox.setText("");
        KeyEvent key = new KeyEvent(loginWindow, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,  KeyEvent.VK_ENTER,'Z');
        passwordBox.getKeyListeners()[0].keyPressed(key);
        Assert.assertFalse(loginWindow.isDisplayable());
    }

    @Test
    public void validLogin_2() {
        Login loginWindow = new Login(makeSim());
        JTextField passwordBox = (JTextField)loginWindow.getField("password");
        JComboBox usernameField = (JComboBox)loginWindow.getField("username");
        usernameField.setSelectedItem(Login.UserName.Manager);
        passwordBox.setText("");
        KeyEvent key = new KeyEvent(loginWindow, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,  KeyEvent.VK_ENTER,'Z');
        passwordBox.getKeyListeners()[0].keyPressed(key);
        Assert.assertFalse(loginWindow.isDisplayable());
    }

    @Test
    public void invalidLogin_1() {
        Login loginWindow = new Login(makeSim());
        JTextField passwordBox = (JTextField)loginWindow.getField("password");
        JComboBox usernameField = (JComboBox)loginWindow.getField("username");
        usernameField.setSelectedItem(Login.UserName.Clerk);
        passwordBox.setText("Incorrect Password");
        KeyEvent key = new KeyEvent(loginWindow, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,  KeyEvent.VK_ENTER,'Z');
        passwordBox.getKeyListeners()[0].keyPressed(key);
        Assert.assertTrue(loginWindow.isDisplayable());
    }

    @Test
    public void invalidLogin_2() {
        Login loginWindow = new Login(makeSim());
        JTextField passwordBox = (JTextField)loginWindow.getField("password");
        JComboBox usernameField = (JComboBox)loginWindow.getField("username");
        usernameField.setSelectedItem(Login.UserName.Manager);
        passwordBox.setText("Incorrect Password");
        KeyEvent key = new KeyEvent(loginWindow, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,  KeyEvent.VK_ENTER,'Z');
        passwordBox.getKeyListeners()[0].keyPressed(key);
        Assert.assertTrue(loginWindow.isDisplayable());
    }

    private Simulation makeSim(){
        Simulation simulation;
        try {
            simulation = SimulationXML.readSimulationFromFile(new FileInputStream("test_data/Test.xml"));
        } catch (Exception e) {
            e.printStackTrace();
            simulation = new Simulation();
        }
        return simulation;
    }
}