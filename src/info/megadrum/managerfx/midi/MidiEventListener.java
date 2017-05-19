package info.megadrum.managerfx.midi;

import java.util.EventListener;

public interface MidiEventListener extends EventListener {
    public void midiEventOccurred(MidiEvent evt);
}
