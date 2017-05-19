package info.megadrum.managerfx.midi;

import java.util.EventObject;

import	javax.sound.midi.MidiMessage;
import	javax.sound.midi.ShortMessage;
import	javax.sound.midi.MetaMessage;
import	javax.sound.midi.SysexMessage;
import	javax.sound.midi.Receiver;
import javax.swing.event.EventListenerList;

public class DumpReceiver
	implements	Receiver
{

	public byte[] bytemessage = null;

	  protected EventListenerList listenerList = new EventListenerList();

	  public void addMidiEventListener(MidiEventListener listener) {
	    listenerList.add(MidiEventListener.class, listener);
	  }
	  public void removeMidiEventListener(MidiEventListener listener) {
	    listenerList.remove(MidiEventListener.class, listener);
	  }
	  protected void fireMidiEvent(MidiEvent evt) {
	    Object[] listeners = listenerList.getListenerList();
	    for (int i = 0; i < listeners.length; i = i+2) {
	      if (listeners[i] == MidiEventListener.class) {
	        ((MidiEventListener) listeners[i+1]).midiEventOccurred(evt);
	      }
	    }
	  }

	public DumpReceiver()
	{
	}

	public void close()
	{
	}
	
	public byte[] getByteMessage()
	{
		byte[] msg;
		msg = bytemessage;
		bytemessage = null;
		return msg;
	}

	public void getShortMessage(ShortMessage message)
	{
		bytemessage = message.getMessage();
		fireMidiEvent(new MidiEvent(this));
	}

	public void getSysexMessage(SysexMessage message)
	{
		bytemessage = message.getMessage();
		fireMidiEvent(new MidiEvent(this));
	}

	public void getMetaMessage(MetaMessage message)
	{
		bytemessage = message.getMessage();
		fireMidiEvent(new MidiEvent(this));
	}

	public void send(MidiMessage message, long lTimeStamp)
	{
		if (message instanceof ShortMessage)
		{
			getShortMessage((ShortMessage) message);
		}
		else if (message instanceof SysexMessage)
		{
			getSysexMessage((SysexMessage) message);
		}
		else if (message instanceof MetaMessage)
		{
			getMetaMessage((MetaMessage) message);
		}
		else
		{
			//strMessage = "unknown message type";
		}
	}

}

