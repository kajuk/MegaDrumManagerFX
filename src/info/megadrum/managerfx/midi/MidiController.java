package info.megadrum.managerfx.midi;

import java.util.List;

import javax.rmi.CORBA.Util;
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
	private Boolean sysexReceived;
	private byte currentSysexType;
	private byte currentSysexId;
	private boolean currentSysexWithId;
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
					//System.out.printf("Sending Sysex config with sysex[3,4] = %d %d\n", buf[3], buf[4]);
		        	sendSysexConfigFromThread(buf, maxRetries, retryDelay);
		        	if (sysexTimedOut) {
		        		// do something when timed out and break
		        		System.out.printf("Sysex timed out\n");
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
					//System.out.printf("Sending Sysex request with id = %d\n", typesAndIds.get(i)[1]);
		        	sendSysexRequestFromThread(typesAndIds.get(i)[0],typesAndIds.get(i)[1], maxRetries,retryDelay);
		        	if (sysexTimedOut) {
		        		// do something when timed out and break
		        		System.out.printf("Sysex timed out\n");
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
				Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
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
				});
			}

			@Override
			public void midiEventOccurredWithBuffer(MidiEvent evt, byte[] buffer) {
				// TODO Auto-generated method stub
				
			}
		});		
	}
	
	private void processSysex(byte [] buffer) {
		//TODO
		//implement comparison of sent and received sysex config 
		if (buffer[3] == currentSysexType) {
			if (currentSysexWithId) {
				if (buffer[4] == currentSysexId) {
					sysexReceived = true;					
				}
			} else {
				sysexReceived = true;
			}
		}
		sendingSysex = false;		
		//System.out.printf("Received sysex with id = %d\n", buffer[4]);
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
		currentSysexType = type;
		currentSysexWithId = false;
		currentSysexId = id;
		sysexReceived = false;
		while (sendSysexConfigRetries > 0) {
			if (sendSysexConfigRetries != maxRetries) {
				System.out.printf("Retry %d for id = %d\n", maxRetries - sendSysexConfigRetries + 1, id);				
			}
			sendSysexConfigRetries--;
			delayCounter = retryDelay;
        	switch (type) {
			case Constants.MD_SYSEX_3RD:
				currentSysexWithId = true;
				midiHandler.requestConfig3rd(id);
				break;
			case Constants.MD_SYSEX_CONFIG_COUNT:
				midiHandler.requestConfigCount();
				break;
			case Constants.MD_SYSEX_CONFIG_CURRENT:
				midiHandler.requestConfigCurrent();
				break;
			case Constants.MD_SYSEX_CONFIG_NAME:
				currentSysexWithId = true;
				midiHandler.requestConfigConfigName(id);
				break;
			case Constants.MD_SYSEX_CURVE:
				currentSysexWithId = true;
				midiHandler.requestConfigCurve(id);
				break;
			case Constants.MD_SYSEX_CUSTOM_NAME:
				currentSysexWithId = true;
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
				currentSysexWithId = true;
				currentSysexId = (byte)(id + 1);
				midiHandler.requestConfigPad(id + 1);
				break;
			case Constants.MD_SYSEX_PEDAL:
				midiHandler.requestConfigPedal();
				break;
			case Constants.MD_SYSEX_POS:
				currentSysexWithId = true;
				midiHandler.requestConfigPos(id);
				break;
			case Constants.MD_SYSEX_VERSION:
				midiHandler.requestVersion();
				break;
			case Constants.MD_SYSEX_CONFIG_LOAD:
				midiHandler.requestLoadFromSlot(id);
				break;
			case Constants.MD_SYSEX_CONFIG_SAVE:
				midiHandler.requestSaveToSlot(id);
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
			sysexTimedOut = true;
			//System.out.println(sendSysexConfigResult);
		}
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
			//System.out.printf("Starting thread with number of sysexes = %d\n", typesAndIdsList.size());midiEventOccurred
			Platform.runLater(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					new Thread(sendSysexRequestsTask).start();
					
				}
			});
			
		} else {
			progressBar.setVisible(false);
		}
	}

	public void sendSysexConfigFromThread(byte [] sysex, Integer maxRetries, Integer retryDelay) {
		sendSysexConfigRetries = maxRetries;
		byte id = sysex[4];
    	midiHandler.sendSysex(sysex);
		int delayCounter;
		currentSysexType = sysex[3];
		currentSysexWithId = false;
		currentSysexId = id;
		sysexReceived = false;
		//System.out.println("sendSysexConfigFromThread called\n");
		while (sendSysexConfigRetries > 0) {
			//System.out.printf("Retry %d\n", maxRetries - sendSysexConfigRetries + 1);
			sendSysexConfigRetries--;
			delayCounter = retryDelay;
        	switch (sysex[3]) {
			case Constants.MD_SYSEX_3RD:
				midiHandler.requestConfig3rd(id);
				break;
			// Config Count is read only
			//case Constants.MD_SYSEX_CONFIG_COUNT:
			//	midiHandler.requestConfigCount();
			//	break;
			case Constants.MD_SYSEX_CONFIG_CURRENT:
				midiHandler.requestConfigCurrent();
				break;
			case Constants.MD_SYSEX_CONFIG_NAME:
				currentSysexWithId = true;
				midiHandler.requestConfigConfigName(id);
				break;
			case Constants.MD_SYSEX_CURVE:
				currentSysexWithId = true;
				midiHandler.requestConfigCurve(id);
				break;
			case Constants.MD_SYSEX_CUSTOM_NAME:
				currentSysexWithId = true;
				midiHandler.requestConfigCustomName(id);
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
				currentSysexWithId = true;
				midiHandler.requestConfigPad(id);
				break;
			case Constants.MD_SYSEX_PEDAL:
				midiHandler.requestConfigPedal();
				break;
			case Constants.MD_SYSEX_POS:
				currentSysexWithId = true;
				midiHandler.requestConfigPos(id);
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
			sysexTimedOut = true;
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
		} else {
			progressBar.setVisible(false);
		}
	}

	public void getMidi() {
		midiHandler.getMidi();
	}
	
	public Boolean isMidiOpen() {
		return midiHandler.isMidiOpen();
	}
	
	public void closeAllPorts() {
		midiHandler.closeAllPorts();
	}
}
