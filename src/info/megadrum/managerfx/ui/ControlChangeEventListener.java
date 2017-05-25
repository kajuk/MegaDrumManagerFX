package info.megadrum.managerfx.ui;

import java.util.EventListener;

public interface ControlChangeEventListener extends EventListener {
    public void controlChangeEventOccurred(ControlChangeEvent evt, Integer parameter);
}
