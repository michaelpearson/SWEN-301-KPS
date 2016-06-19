package kps.gui.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * A slim version of the key listener interface so you don't have to implement every single method.
 * @see KeyListener
 */
public abstract class KeyListenerSlim implements KeyListener {
    /**
     * @see KeyListener
     */
    @Override public void keyTyped(KeyEvent e) {}

    /**
     * @see KeyListener
     */
    @Override
    public void keyReleased(KeyEvent e) {}
}
