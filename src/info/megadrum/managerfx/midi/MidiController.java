package info.megadrum.managerfx.midi;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.naming.spi.DirStateFactory.Result;
import javax.rmi.CORBA.Util;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;
import javax.sound.sampled.ReverbType;
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
	private Boolean sysexTimedOut = false;
	private Boolean sysexMismatch = false;
	private Boolean sysexReceived;
	private byte currentSysexType;
	private byte currentSysexId;
	private boolean currentSysexWithId;
	private Integer sendSysexConfigRetries = 1;
	private String sendSysexConfigResult = "";
	private Boolean compareSysex = false;
	private byte [] sysexToCompare;
	private int [] sysexStatus;
	private int chainId;
	private boolean upgradeCancelled = false;
	private String upgradeResultString;
	private int upgradeError;

	private List<byte[]> sysexSendListLocal;
	private List<byte[]> receivedMidiDataList;

	class SendSysexTask<V> extends Task<V> {
		
		private List<byte[]> sysexesList;
		private Integer maxRetries;
		private Integer retryDelay;
		private byte [] buf;
		
		@Override
		protected V call()  {
			//System.out.println("****************************************************************");
			//System.out.println("thread started");
			try {
				sysexStatus[0] = Constants.MD_SYSEX_STATUS_OK;
				int i = 0;
				final int max = sysexesList.size();
				updateProgress(0, max);
				while (sysexesList.size() > 0) {
					buf = sysexesList.get(0);
					if (buf.length > 3) {
						sysexStatus[1] = buf[3];
					} else {
						sysexStatus[1] = buf[0];
					}
		        	sendSysexFromThread(buf, maxRetries, retryDelay);
		        	if (sysexTimedOut) {
		        		// do something when timed out and break
		        		if ((sysexStatus[0]) == Constants.MD_SYSEX_STATUS_OK) {
							sysexStatus[0] = Constants.MD_SYSEX_STATUS_TIMEOUT;
		        		}
		        		//System.out.printf("Sysex timed out\n");
		        		break;
		        	}
		            updateProgress(i, max);
		            i++;
					sysexesList.remove(0);
				}
			} catch (Exception e) {
				Utils.show_error(String.format("Sysex Send thread exception text = %s\n", e.getMessage()));
			}
			//System.out.println("Send Sysex thread finished");
			//System.out.println("----------------------------------------------------------------");
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
		dump_receiver = midiHandler.getDumpReceiver();
		//receivedShortMidiList = new ArrayList<>();
		//receivedSysexList = new ArrayList<>();
		receivedMidiDataList = new ArrayList<>();
		sysexStatus = new int[2];
		
		midiHandler.addMidiEventListener(new MidiEventListener() {
			@Override
			public void midiEventOccurred(MidiEvent evt) {
				//System.out.println("MIDI received event occured");
				if (!isInFirmwareUpgrade) {
					midiHandler.getMidi();
					if (midiHandler.isSysexReceived()) {
						midiHandler.resetSysexReceived();
						processSysex(midiHandler.getBufferIn());						
						midiHandler.resetBufferIn();
					} else if (midiHandler.getBufferIn() != null) {
						processShortMidi(midiHandler.getBufferIn());
						midiHandler.resetBufferIn();
					}
				}
			}

			@Override
			public void midiEventOccurredWithBuffer(MidiEvent evt, byte[] buffer) {				
			}
		});		
	}
	
	private void processSysex(byte [] buffer) {
		//System.out.printf("processSysex length = %d\n", buffer.length);

		//implement comparison of sent and received sysex config
		//System.out.println("1111111111111111111");
		if (compareSysex) {
			//System.out.println("Comparing");
			if (buffer[3] == Constants.MD_SYSEX_GLOBAL_MISC) {
				/*
				#define GL_BF_AUTO_LOAD_CONFIG	0
				#define GL_BF_CUSTOM_NAMES_EN	1
				#define GL_BF_CONFIG_NAMES_EN	2
				#define GL_BF_ENCODERS_ALT		3
				#define GL_BF_ONE_ENCODER		4
				#define GL_BF_MIDI2_SYSEX_ONLY	5
				 */
				// Ignore bits 0, 3 and 4 missmattch;
				buffer[8] = (byte)(buffer[8]&0xfe);
				buffer[9] = (byte)(buffer[9]&0xf6);
				sysexToCompare[8] = (byte)(sysexToCompare[8]&0xfe);
				sysexToCompare[9] = (byte)(sysexToCompare[9]&0xf6);
			}
			if (Arrays.equals(buffer, sysexToCompare)) {
				sysexReceived = true;
				//System.out.println("Received sysex is equal");
			} else {
				sysexStatus[0] = Constants.MD_SYSEX_STATUS_MISMATCH;
				for (int i= 0; i < buffer.length; i++) {
					if (buffer[i] != sysexToCompare[i]) {
						System.out.printf("Missmatch. Id = %d, sent=%d, got=%d\n", i, sysexToCompare[i], buffer[i]);
						break;
					}
				}
				sysexMismatch = true;
				System.out.println("Received sysex is NOT equal");
			}
			//compareSysex = false;
		} else {
			//System.out.println("Not comparing");
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
		//fireMidiEventWithBuffer(new MidiEvent(this), buffer);
		//System.out.println("2222222222222222222");
		if (sysexReceived) {
			sysexStatus[0] = 0;
			//System.out.println("Fire MIDI event");
			receivedMidiDataList.add(buffer);
			fireMidiEvent(new MidiEvent(this));
		}
	}

	public int[] getStatus() {
		return sysexStatus;
	}
		
	private void processShortMidi(byte [] buffer) {
		//fireMidiEventWithBuffer(new MidiEvent(this), buffer);
		receivedMidiDataList.add(buffer);
		fireMidiEvent(new MidiEvent(this));
	}
	
	public List<byte[]> getMidiDataList() {
		return receivedMidiDataList;
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
	
	public void setChainId(int id ) {
		midiHandler.setChainId(id);
		chainId = id;
	}
	
	public void sendSysexFromThread(byte [] sysex, Integer maxRetries, Integer retryDelay) {
		//System.out.println("sendSysexConfigFromThread called\n");
		//sendSysexConfigRetries = maxRetries;
		sendSysexConfigRetries = 10;
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
		sysexMismatch = true;
		compareSysex = false;
		while (sendSysexConfigRetries > 0) {
			//System.out.printf("Retries remaining  before = %d\n", sendSysexConfigRetries);
			if (sendSysexConfigRetries != maxRetries) {
				System.out.println("Retrying");
			}
			if (sysex.length > 2) {
				if (sysexMismatch) {
					//System.out.println("Sending config");
					sysexMismatch = false;
					sysex[2] = (byte)chainId;
					sysexToCompare = Arrays.copyOf(sysex, sysex.length);
					compareSysex = true;
			    	midiHandler.sendSysex(sysex);
			    	if (sendSysexConfigRetries < maxRetries) {
			    		//System.out.println("Additional delay on mismatch retry");
						Utils.delayMs(sysex.length/4);
			    	}
					Utils.delayMs(sysex.length/5);
				}
				compareSysex = true;
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
				//System.out.println("Sysex received");
				break;
			} else {
				System.out.println("Resetting ports");
				midi_reset_ports();
				System.out.printf("Retries remaining after = %d\n", sendSysexConfigRetries);			
			}
		}
		if (!sysexReceived) {
			//getTimedOut(Constants.SYSEX_TIMEOUT_PEDAL_TXT);
			sendSysexConfigResult = "Sysex timed out";
			sysexTimedOut = true;
			System.out.println(sendSysexConfigResult);
		} 	
	}
	
	public int sendSysex(List<byte[]> sysexSendList, ProgressBar progressBar, Integer maxRetries, Integer retryDelay) {
		int result = 0;
		sendSysexConfigResult = "";
		sysexReceived = false;
		sendSysexConfigRetries = maxRetries;
		sysexTimedOut = false;

		if (midiHandler.isMidiOpen()) {
			sysexSendListLocal = new ArrayList<>(sysexSendList);
			sysexSendList.clear();
			sendSysexTask.setParameters(sysexSendListLocal,maxRetries,retryDelay);
			progressBar.progressProperty().bind(sendSysexTask.progressProperty());
			Platform.runLater(new Runnable() {
				
				@Override
				public void run() {
					//System.out.printf("Starting Sysex thread with number of sysexes = %d\n", sysexSendListLocal.size());
					new Thread(sendSysexTask).start();
				}
			});
		} else {
			progressBar.setVisible(false);
			result = 1;
		}
		return result;
	}

	private static int readHex(DataInputStream d) throws IOException {
		StringBuffer curr;
		int result;

		curr = new StringBuffer("");
		
		curr.append(String.format("%c", d.readByte()).toUpperCase());
		curr.append(String.format("%c", d.readByte()).toUpperCase());
		
		result = Integer.parseInt(curr.toString(),16);
		//out(String.valueOf(result));
		//System.out.printf("%h\n", result);
		return result;
	}

	public Task<Integer> doFirmwareUpgrade (File file, int mcuType, ProgressBar progressBar) throws IOException {		
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;
		upgradeResultString = "Upgrade completed successufully";
		upgradeCancelled = false;
		int[] buffer = new int[0x40000];	// Data buffer for sending the data

		int bufferSize = 0;			// Total number of bytes in the buffer
		upgradeError = 0;

		//closeAllPorts();
		//initPorts();
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// e.printStackTrace();
			Utils.show_error("Error loading from file:\n" +
					file.getAbsolutePath() + "\n" +
					"(" + e.getMessage() + ")");
		}
		bis = new BufferedInputStream(fis);
		dis = new DataInputStream(bis);
		//System.out.printf("Loading Firmware file\n");
		while (dis.available() > 1)
		{
			buffer[bufferSize] = readHex(dis);
			bufferSize++;
		}
		//System.out.printf("Firmware file loaded\n");
		dis.close();
		bis.close();
		fis.close();

		//("Starting upgrade\n");
				
		final int bufferSizeFinal = bufferSize;
		Task<Integer> task = new Task<Integer>() {
			int frameSize;				// Number of bytes in the current frame
			int receivedByte;		// One byte received from COM port
			int bytesSent = 0;			// Number of bytes sent so far
			int prevBytesSent = 0;
			int retries = 0;		// Number of tries so far
			//int Block_size;
			int nBytes;
			int inDelay;
			boolean firstDelay;
			byte[] receivedBuffer;


			@Override
			protected Integer call() throws Exception {
	            updateProgress(0, bufferSizeFinal);

	    		if (mcuType > 2) {
	    			// Restart ARM based MegaDrum in bootloader mode
	    			midiHandler.requestArmBootloader();
	    			//System.out.printf("Sent reboot request\n");
	    			Utils.delayMs(4000);
	    		}
	    		closeAllPorts();
	    		midiHandler.initPorts();
	    		//System.out.printf("Firmware size is %d bytes\n", bufferSizeFinal);
	    		
	    		midiHandler.clear_midi_input();
	            
	            
	    		firstDelay = true;
				for(int index = 0; index < bufferSizeFinal; index += frameSize)
				{
					frameSize = ((buffer[index] << 8) | buffer[index + 1]) + 2;
					if (((bytesSent-prevBytesSent)*100/(bufferSizeFinal/10)) > 1) {
			            updateProgress(bytesSent, bufferSizeFinal);
						prevBytesSent = bytesSent;				
					}
					//System.out.printf("index=%d , frameSize=%d \n", index, frameSize);

					//Block_size = frameSize;				// Seem it fails
					//if (frameSize < 80) Block_size = 2;	// with some firmware sizes
					//Block_size = 2;
					//if (Block_size > frameSize) Block_size = frameSize;
					midiHandler.writeMid(receiver, buffer, index, frameSize);
					//System.out.printf("Sent %d bytes\n", frameSize);
					

					nBytes = 0;
					if (firstDelay) {
						inDelay = 5000;				
						firstDelay = false;
					} else {
						inDelay = 1000;
					}
					receivedBuffer = null;
					int t = 0;
		 			while ((nBytes == 0) && (inDelay > 0)) {
		 				receivedBuffer = dump_receiver.getByteMessage();
		 				//Faking ok response for testing
		 				//if (t== 11) {
		 				//	receivedBuffer = new byte[3];
		 				//	receivedBuffer[1] = 1;
		 				//	receivedBuffer[2] = 1;
		 				//}
		 				t++;
		 				if (t > 100) {
		 					t = 0;
		 	 				//System.out.printf(".");
		 				}
		 				if (receivedBuffer != null)
		 				{
		 					nBytes = receivedBuffer.length;
		 				}
					    inDelay--;
					    Utils.delayMs(2);
					    if (upgradeCancelled) break;
					}
		 			//System.out.printf("\n");
					//System.out.printf("Received %d bytes\n", nBytes);
								
		 			receivedByte = Constants.Error_NoResponse;
					if (nBytes > 2) {
						receivedByte = receivedBuffer[1]<<4;
						receivedByte = receivedBuffer[2]|receivedByte;
						//System.out.println(String.valueOf((int)receivedByte));
					} else {
						//System.out.println("Read error\n");
						if (nBytes > 0) {
							receivedByte = Constants.Error_Read;
						}
					}

					switch (receivedByte) {
						case Constants.Error_OK:
							//System.out.printf("Got OK from MegaDrum\n");
							bytesSent += frameSize;
							retries = 0;
							break;

						default: // Error_CRC:
							if (++retries < 4) {
								index -= frameSize;
								//System.out.println("Retrying on error\n");
								Utils.delayMs(10);
							} else {
								//System.out.println("\nCRC error. File damaged.\n");
								switch (receivedByte) {
								case Constants.Error_CRC:
									upgradeError = 2;
									upgradeResultString = "CRC error. File damaged?";
									break;
								case Constants.Error_NoResponse:
									upgradeError = 3;
									upgradeResultString = "MegaDrum is not responding";
									break;
								case Constants.Error_Read:
									upgradeError = 4;
									upgradeResultString = "Read error. Bad communication?";
									break;
								default:
									upgradeError = 99;
									upgradeResultString = "Unknown error";
									break;
								}
								//System.out.printf("Exit with error: %s\n", upgradeResultString);
							}
							break;
					}
					if (upgradeCancelled) {
						//System.out.println("Upgrade cancelled\n");
						upgradeError = 1;
						upgradeResultString = "Upgrade cancelled";
					}
					if (upgradeError > 0) {
						break;
					}
				}		
				return 0;
			}
		};
		progressBar.progressProperty().bind(task.progressProperty());
		return task;
	}
	
	public void setInFirmwareUpgrade(Boolean inUpgrade) {
		isInFirmwareUpgrade = inUpgrade;
	}
	
	public void cancelUpgrade() {
		upgradeCancelled = true;
	}
	
	public int getUpgradeError() {
		return upgradeError;
	}
	
	public String getUpgradeString() {
		return upgradeResultString;
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
