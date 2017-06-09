package info.megadrum.managerfx.midi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.rmi.CORBA.Util;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;
import javax.swing.event.EventListenerList;

import org.apache.commons.collections.functors.IfClosure;

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
	private Boolean compareSysex = false;
	private byte [] sysexToCompare;

	private List<byte[]> sysexSendListLocal;
	//private List<byte[]> sysexSendList;
	//private List<byte[]> sysexRequestsList;

	class SendSysexTask<V> extends Task<V> {
		
		private List<byte[]> sysexesList;
		private Integer maxRetries;
		private Integer retryDelay;
		private byte [] buf;
		
		@Override
		protected V call()  {
			//System.out.println("thread started");
			try {
				int i = 0;
				final int max = sysexesList.size();
				while (sysexesList.size() > 0) {
					buf = sysexesList.get(0);
		        	sendSysexFromThread(buf, maxRetries, retryDelay);
		        	if (sysexTimedOut) {
		        		// do something when timed out and break
		        		System.out.printf("Sysex timed out\n");
		        		break;
		        	}
		            updateProgress(i, max);
		            i++;
					sysexesList.remove(0);
				}
			} catch (Exception e) {
				System.out.printf("Sysex Send thread exception text = %s\n", e.getMessage());				
			}
			sendingSysex = false;
			System.out.println("Send Sysex thread finished");
			return null;
		}
		
		public void setParameters(List<byte[]> list, Integer retries, Integer msDelay) {
			sysexesList = list;
			maxRetries = retries;
			retryDelay = msDelay;
		}
		
	}
	
	private SendSysexTask<Void> sendSysexTask;
	
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
		if (compareSysex) {
			if (Arrays.equals(buffer, sysexToCompare)) {
				sysexReceived = true;
				System.out.println("Received sysex is equal");
			} else {
				System.out.println("Received sysex is NOT equal");
			}
			compareSysex = false;
		} else {
			if (buffer[3] == currentSysexType) {
				if (currentSysexWithId) {
					if (buffer[4] == currentSysexId) {
						sysexReceived = true;					
					}
				} else {
					sysexReceived = true;
				}
			}
		}
		//System.out.printf("Received sysex with id = %d\n", buffer[4]);
		fireMidiEventWithBuffer(new MidiEvent(this), buffer);
	}

	private void processShortMidi(byte [] buffer) {
		fireMidiEventWithBuffer(new MidiEvent(this), buffer);
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

	public void sendSysexTaskRecreate() {
		if (sendSysexTask != null) {
			sendSysexTask = null;
		}
		sendSysexTask = new SendSysexTask<Void>();
	}

	public void addSendSysexTaskSucceedEventHandler(EventHandler<WorkerStateEvent> eh) {
		sendSysexTask.setOnSucceeded(eh);
	}
	
	public void sendSysexFromThread(byte [] sysex, Integer maxRetries, Integer retryDelay) {
		//System.out.println("sendSysexConfigFromThread called\n");
		sendSysexConfigRetries = maxRetries;
		byte type;
		byte id;
		if (sysex.length > 2) {
			type = sysex[3];
			id = sysex[4];
		} else {
			type = sysex[0];
			id = sysex[1];
		}
		int delayCounter;
		currentSysexType = type;
		currentSysexWithId = false;
		currentSysexId = id;
		sysexReceived = false;
		while (sendSysexConfigRetries > 0) {
			if (sysex.length > 2) {
				sysexToCompare = Arrays.copyOf(sysex, sysex.length);
				compareSysex = true;
		    	midiHandler.sendSysex(sysex);
			}
			//System.out.printf("Retry %d\n", maxRetries - sendSysexConfigRetries + 1);
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
				if (sysex.length > 2) {
					currentSysexId = (byte)(id);
				} else {
					currentSysexId = (byte)(id + 1);
				}
				midiHandler.requestConfigPad(currentSysexId);
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
				//No response for Load from/Save to
				// so faking sysex received
				sysexReceived = true;
				midiHandler.requestLoadFromSlot(id);
				break;
			case Constants.MD_SYSEX_CONFIG_SAVE:
				//No response for Load from/Save to
				// so faking sysex received
				sysexReceived = true;
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
	
	public void sendSysex(List<byte[]> sysexSendList, ProgressBar progressBar, Integer maxRetries, Integer retryDelay) {
		sendSysexConfigResult = "";
		sysexReceived = false;
		sendSysexConfigRetries = maxRetries;
		sysexTimedOut = false;

		if (midiHandler.isMidiOpen() && (!sendingSysex)) {
			sendingSysex = true;
			sysexSendListLocal = new ArrayList<>(sysexSendList);
			sysexSendList.clear();
			sendSysexTask.setParameters(sysexSendListLocal,maxRetries,retryDelay);
			progressBar.progressProperty().bind(sendSysexTask.progressProperty());
			Platform.runLater(new Runnable() {
				
				@Override
				public void run() {
					System.out.printf("Starting Sysex thread with number of sysexes = %d\n", sysexSendListLocal.size());
					new Thread(sendSysexTask).start();
				}
			});
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
	
	public Boolean isSendingSysex() {
		return sendingSysex;
	}
}
