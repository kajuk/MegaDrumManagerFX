package info.megadrum.managerfx.midi;

import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;
import javax.swing.event.EventListenerList;

import info.megadrum.managerfx.utils.Constants;
import info.megadrum.managerfx.utils.Utils;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ProgressBar;

public class MidiController {
	private MidiDevice midiin;
	private MidiDevice midiout;
	private MidiDevice midithru;
	private MidiDevice.Info[]	aInfos;
	private Receiver receiver;
	private Receiver thruReceiver;
	private DumpReceiver dump_receiver;
	private Transmitter	transmitter;
	private Boolean isInFirmwareUpgrade = false;
	
	private Midi_handler midiHandler;
	private Boolean sendingSysex = false;
	private Boolean sysexTimedOut = false;
	private Boolean sysexReceived = false;
	private Integer sendSysexConfigRetries = 1;
	private String sendSysexConfigResult = "";

	//private List<byte[]> sysexSendList;
	//private List<byte[]> sysexRequestsList;

	class SendSysexConfigsTask<V> extends Task<V> {
		
		private List<byte[]> sysexesList;
		private Integer maxRetries;
		private Integer retryDelay;
		private byte [] buf;
		
		@Override
		protected V call()  {
			//System.out.println("thread started");
			try {
		        final int max = sysexesList.size();
		        for (int i = 0; i < max; i++) {
					buf = sysexesList.get(i);					
		        	sendSysexConfigFromThread(buf, maxRetries, retryDelay);
		        	if (sysexTimedOut) {
		        		// do something when timed out and break
		        		break;
		        	}
		            updateProgress(i, max);
		        }
			} catch (Exception e) {
				System.out.printf("Thread exception text = %s\n", e.getMessage());				
			}
			//System.out.println("thread finished");
			return null;
		}
		
		public void setParameters(List<byte[]> list, Integer retries, Integer msDelay) {
			sysexesList = list;
			maxRetries = retries;
			retryDelay = msDelay;
		}
		
	}

	class SendSysexRequestsTask<V> extends Task<V> {
		
		private List<byte[]> typesAndIds;
		private Integer maxRetries;
		private Integer retryDelay;
		
		@Override
		protected V call()  {
			//System.out.println("thread started");
			try {
		        final int max = typesAndIds.size();
		        for (int i = 0; i < max; i++) {
		        	sendSysexRequestFromThread(typesAndIds.get(i)[0],typesAndIds.get(i)[1], maxRetries,retryDelay);
		        	if (sysexTimedOut) {
		        		// do something when timed out and break
		        		break;
		        	}
		            updateProgress(i, max);
		        }
			} catch (Exception e) {
				System.out.printf("Thread exception text = %s\n", e.getMessage());				
			}
			//System.out.println("thread finished");
			return null;
		}
		
		public void setParameters(List<byte[]> typesAndIdsList, Integer retries, Integer msDelay) {
			typesAndIds = typesAndIdsList;
			maxRetries = retries;
			retryDelay = msDelay;
		}
		
	}
	
	private SendSysexConfigsTask<Void> sendSysexConfigsTask;
	private SendSysexRequestsTask<Void> sendSysexRequestsTask;
	
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
	
	protected void fireMidiEventWithBuffer(MidiEvent evt, byte [] buffer) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i = i+2) {
			if (listeners[i] == MidiEventListener.class) {
				((MidiEventListener) listeners[i+1]).midiEventOccurredWithBuffer(evt, buffer);
			}
		}
	}

	public MidiController() {
		midiHandler = new Midi_handler();
		
		midiHandler.addMidiEventListener(new MidiEventListener() {
			@Override
			public void midiEventOccurred(MidiEvent evt) {
				if (!isInFirmwareUpgrade) {
					midiHandler.getMidi();
					if (midiHandler.isSysexReceived()) {
						midiHandler.resetSysexReceived();
						processSysex(midiHandler.getBufferIn());						
					} else if (midiHandler.getBufferIn() != null) {
						processShortMidi(midiHandler.getBufferIn());
					}
					midiHandler.resetBufferIn();
				}
			}

			@Override
			public void midiEventOccurredWithBuffer(MidiEvent evt, byte[] buffer) {
				// TODO Auto-generated method stub
				
			}
		});		
	}
	
	private void processSysex(byte [] buffer) {
		sendingSysex = false;
		sysexReceived = true;
		fireMidiEventWithBuffer(new MidiEvent(this), buffer);
	}

	private void processShortMidi(byte [] buffer) {
		
	}
	
	public String [] getMidiInList() {
		return midiHandler.getMidiInList();
	}
	
	public String [] getMidiOutList() {
		return midiHandler.getMidiOutList();
	}
	
	public void openMidi(String midiIn, String midiOut, String midiThru) {
		midiHandler.setMidiInName(midiIn);
		midiHandler.setMidiOutName(midiOut);
		midiHandler.setMidiThruName(midiThru);
		midiHandler.initPorts();
	}
	
	public void closeMidi() {
		
	}
	
	private void midi_reset_ports() {
		// On MIDI response timeouts,
		// 8 resets at least needed to workaround Windows/Java/USB MIDI sysex corruption(bug?)
	    //System.out.print("Resetting MIDI ports\n");
	    midiHandler.clearMidiOut();
	}

	public void sendSysexRequestFromThread(byte type, byte id, Integer maxRetries, Integer retryDelay) {
		sendSysexConfigRetries = maxRetries;
		int delayCounter;
		//System.out.println("sendSysexRequestFromThread called\n");
		while (sendSysexConfigRetries > 0) {
			//System.out.printf("Retry %d\n", maxRetries - sendSysexConfigRetries + 1);
			sendSysexConfigRetries--;
			delayCounter = retryDelay;
        	switch (type) {
			case Constants.MD_SYSEX_3RD:
				midiHandler.requestConfig3rd(id);
				break;
			case Constants.MD_SYSEX_CONFIG_COUNT:
				midiHandler.requestConfigCount();
				break;
			case Constants.MD_SYSEX_CONFIG_CURRENT:
				midiHandler.requestConfigCurrent();
				break;
			case Constants.MD_SYSEX_CONFIG_NAME:
				midiHandler.requestConfigConfigName(id);
				break;
			case Constants.MD_SYSEX_CURVE:
				midiHandler.requestConfigCurve(id);
				break;
			case Constants.MD_SYSEX_CUSTOM_NAME:
				midiHandler.requestConfigCustomName(id);
				break;
			case Constants.MD_SYSEX_GLOBAL_MISC:
				midiHandler.requestConfigGlobalMisc();
				break;
			case Constants.MD_SYSEX_MCU_TYPE:
				midiHandler.requestMCU();
				break;
			case Constants.MD_SYSEX_MISC:
				midiHandler.requestConfigMisc();
				break;
			case Constants.MD_SYSEX_PAD:
				midiHandler.requestConfigPad(id);
				break;
			case Constants.MD_SYSEX_PEDAL:
				midiHandler.requestConfigPedal();
				break;
			case Constants.MD_SYSEX_POS:
				midiHandler.requestConfigPos(id);
				break;
			case Constants.MD_SYSEX_VERSION:
				midiHandler.requestVersion();
				break;
			default:
				break;
			}
			while ((delayCounter > 0) && (!sysexReceived)) {
				Utils.delayMs(1);
				delayCounter--;
			}
			if (sysexReceived) {
				break;
			} else {
				midi_reset_ports();
			}
		}
		if (!sysexReceived) {
			//getTimedOut(Constants.SYSEX_TIMEOUT_PEDAL_TXT);
			sendSysexConfigResult = "Sysex timed out";
			//System.out.println(sendSysexConfigResult);
		}
		Utils.delayMs(100);
	}

	public void sendSysexRequestsTaskRecreate() {
		if (sendSysexRequestsTask != null) {
			sendSysexRequestsTask = null;
		}
		sendSysexRequestsTask = new SendSysexRequestsTask<Void>();
	}
	
	public void addSendSysexRequestsTaskSucceedEventHandler(EventHandler<WorkerStateEvent> eh) {
		sendSysexRequestsTask.setOnSucceeded(eh);
	}
	
	public void sendSysexRequests(List<byte[]> typesAndIdsList, ProgressBar progressBar, Integer maxRetries, Integer retryDelay) {
		sendSysexConfigResult = "";
		sendingSysex = true;
		sysexReceived = false;
		sendSysexConfigRetries = maxRetries;
		sysexTimedOut = false;

		if (midiHandler.isMidiOpen()) {
			sendSysexRequestsTask.setParameters(typesAndIdsList,maxRetries,retryDelay);
			progressBar.progressProperty().bind(sendSysexRequestsTask.progressProperty());
			//System.out.printf("Starting thread with number of sysexes = %d\n", typesAndIdsList.size());
			new Thread(sendSysexRequestsTask).start();
			
		}
	}

	public void sendSysexConfigFromThread(byte [] sysex, Integer maxRetries, Integer retryDelay) {
		sendSysexConfigRetries = maxRetries;
    	midiHandler.sendSysex(sysex);
		int delayCounter;
		//System.out.println("sendSysexConfigFromThread called\n");
		while (sendSysexConfigRetries > 0) {
			//System.out.printf("Retry %d\n", maxRetries - sendSysexConfigRetries + 1);
			sendSysexConfigRetries--;
			delayCounter = retryDelay;
        	switch (sysex[3]) {
			case Constants.MD_SYSEX_3RD:
				midiHandler.requestConfig3rd(sysex[4]);
				break;
			// Config Count is read only
			//case Constants.MD_SYSEX_CONFIG_COUNT:
			//	midiHandler.requestConfigCount();
			//	break;
			case Constants.MD_SYSEX_CONFIG_CURRENT:
				midiHandler.requestConfigCurrent();
				break;
			case Constants.MD_SYSEX_CONFIG_NAME:
				midiHandler.requestConfigConfigName(sysex[4]);
				break;
			case Constants.MD_SYSEX_CURVE:
				midiHandler.requestConfigCurve(sysex[4]);
				break;
			case Constants.MD_SYSEX_CUSTOM_NAME:
				midiHandler.requestConfigCustomName(sysex[4]);
				break;
			case Constants.MD_SYSEX_GLOBAL_MISC:
				midiHandler.requestConfigGlobalMisc();
				break;
			// MCU is read only
			//case Constants.MD_SYSEX_MCU_TYPE:
			//	midiHandler.requestMCU();
			//	break;
			case Constants.MD_SYSEX_MISC:
				midiHandler.requestConfigMisc();
				break;
			case Constants.MD_SYSEX_PAD:
				midiHandler.requestConfigPad(sysex[4]);
				break;
			case Constants.MD_SYSEX_PEDAL:
				midiHandler.requestConfigPedal();
				break;
			case Constants.MD_SYSEX_POS:
				midiHandler.requestConfigPos(sysex[4]);
				break;
			// Version is read only
			//case Constants.MD_SYSEX_VERSION:
			//	midiHandler.requestVersion();
			//	break;
			default:
				break;
			}
			while ((delayCounter > 0) && (!sysexReceived)) {
				Utils.delayMs(1);
				delayCounter--;
			}
			if (sysexReceived) {
				break;
			} else {
				midi_reset_ports();
			}
		}
		if (!sysexReceived) {
			//getTimedOut(Constants.SYSEX_TIMEOUT_PEDAL_TXT);
			sendSysexConfigResult = "Sysex timed out";
			//System.out.println(sendSysexConfigResult);
		} 	
	}
	
	public void sendSysexConfigsTaskRecreate() {
		if (sendSysexConfigsTask != null) {
			sendSysexConfigsTask = null;
		}
		sendSysexConfigsTask = new SendSysexConfigsTask<Void>();
	}
	
	public void addSendSysexConfigsTaskSucceedEventHandler(EventHandler<WorkerStateEvent> eh) {
		sendSysexConfigsTask.setOnSucceeded(eh);
	}
	
	public void sendSysexConfigs(List<byte[]> sysexSendList, ProgressBar progressBar, Integer maxRetries, Integer retryDelay) {
		sendSysexConfigResult = "";
		sendingSysex = true;
		sysexReceived = false;
		sendSysexConfigRetries = maxRetries;
		sysexTimedOut = false;

		if (midiHandler.isMidiOpen()) {
			sendSysexConfigsTask.setParameters(sysexSendList,maxRetries,retryDelay);
			progressBar.progressProperty().bind(sendSysexConfigsTask.progressProperty());
			//System.out.printf("Starting thread with number of sysexes = %d\n", sysexSendList.size());
			new Thread(sendSysexConfigsTask).start();
			
		}
	}

	public void getMidi() {
		midiHandler.getMidi();
	}
}
