package info.megadrum.managerfx.midi;

import java.util.EventListener;

public interface MidiRescanEventListener extends EventListener {
    public void midiRescanEventOccurred(MidiRescanEvent evt);
}
