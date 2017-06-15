package info.megadrum.managerfx.midi;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;
import javax.sound.midi.Receiver;
import	javax.sound.midi.Sequence;
import	javax.sound.midi.Sequencer;
//import	javax.sound.midi.Track;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.SysexMessage;
import javax.swing.event.EventListenerList;

import info.megadrum.managerfx.utils.Constants;
import info.megadrum.managerfx.utils.Utils;

public class Midi_handler {

	//private int nPorts;
	//private int port;

	private MidiDevice midiin;
	private String midiInName = "";
	private MidiDevice midiout;
	private String midiOutName = "";
	private MidiDevice midithru;
	private String midiThruName = "";
	private MidiDevice.Info[]	aInfos;
	private Receiver receiver;
	private Receiver thruReceiver;
	private DumpReceiver dump_receiver;
	private Transmitter	transmitter;
	//private ConfigOptions configOptions;
	
	private int chainId = 0;
	private boolean getMidiBlocked;
	private boolean upgradeCancelled = false;
	private byte [] bufferIn;
	private boolean sysexReceived;
	private boolean useMidiThru = false;
	private int Block_size;

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

	public Midi_handler () {
		init();
	}
	
	public void init() {
		midiin = null;
		midiout = null;
		midithru = null;
		receiver = null;
		thruReceiver = null;
		transmitter = null;
		chainId = 0;
		dump_receiver = null;
		dump_receiver = new DumpReceiver();
		dump_receiver.addMidiEventListener(new MidiEventListener() {
			@Override
			public void midiEventOccurred(MidiEvent evt) {
				fireMidiEvent(new MidiEvent(this));
			}

			@Override
			public void midiEventOccurredWithBuffer(MidiEvent evt, byte[] buffer) {
				// TODO Auto-generated method stub
				
			}
		});
		getMidiBlocked = false;
		bufferIn = null;
		sysexReceived = false;		
	}
	
	public void setChainId(int id ) {
		chainId = id;
	}

	public byte[] getBufferIn() {
		return bufferIn;
	}
	
	public Boolean isSysexReceived() {
		return sysexReceived;
	}
	
	public void resetSysexReceived() {
		sysexReceived = false;
	}
	
	public void resetBufferIn() {
		bufferIn = null;
	}
	
	public void closeAllPorts() {
		if (midiin != null) {
			if (midiin.isOpen()) {
				midiin.close();
			}
		}
		if (midiout != null) {
			if (midiout.isOpen()) {
				midiout.close();
			}
		}
		if (midithru != null) {
			if (midithru.isOpen()) {
				midithru.close();
			}
		}
	}

	public void clearMidiOut() {
		if (midiout != null) {
			if (midiout.isOpen()) {
				midiout.close();
			}
		}
		try {
			midiout.open();
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			Utils.show_error("Error re-opening MIDI Out port:\n" +
					midiout.getDeviceInfo().getName() + "\n" +
					"(" + e.getMessage() + ")");
		}
/*
    	byte[] shortMessage={(byte)0xb0, (byte)0x7b, (byte) 0x00};
		for (int j = 0; j < 16; j++) {
			//sendMidiShort({(byte) 1,(byte)2,(byte)3});
			this.sendMidiShort(shortMessage);
			delayMs(10);
		}
*/
	}
	
	public void sendMidiShort(byte [] buf) {
		//System.out.printf("Preparing to send SysEx\n");
		if (midiout != null) {
			//System.out.printf("midiout is not Null\n");
			if (midiout.isOpen()) {
				//System.out.printf("midiout is open\n");
				ShortMessage shortMessage = new ShortMessage();
				//midiout.close();
				//System.out.printf("midiout closed\n");
		    	try {
					//midiout.open();
					//System.out.printf("midiout re-opened\n");
					receiver = midiout.getReceiver();
					//System.out.printf("midiout provded receiver\n");
				} catch (MidiUnavailableException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					switch (buf.length) {
					case 1:
						shortMessage.setMessage(buf[0]);
						break;
					case 2:
						shortMessage.setMessage(buf[0], buf[1],0);
						break;
					default:
						shortMessage.setMessage(buf[0], buf[1],buf[2]);
						break;
					}
					receiver.send(shortMessage, -1);
				} catch (InvalidMidiDataException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					Utils.show_error("Error sending Short MIDI message to port:\n" +
							midiout.getDeviceInfo().getName() + "\n" +
							"(" + e.getMessage() + ")");
				}		
			}
		}
	}
	public void sendMidiShortThru(byte [] buf) {
		if ((midithru != null) && (midithru.isOpen())) {
			ShortMessage shortMessage = new ShortMessage();
			try {
				switch (buf.length) {
				case 1:
					shortMessage.setMessage(buf[0]);
					break;
				case 2:
					shortMessage.setMessage(buf[0], buf[1],0);
					break;
				default:
					shortMessage.setMessage(buf[0], buf[1],buf[2]);
					break;
				}
				thruReceiver.send(shortMessage, -1);
			} catch (InvalidMidiDataException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				Utils.show_error("Error sending Short MIDI message to port:\n" +
						midithru.getDeviceInfo().getName() + "\n" +
						"(" + e.getMessage() + ")");
			}
		}
	}
	
	public void sendSysex(byte [] buf) {
		//System.out.printf("Preparing to send SysEx\n");
		if (midiout != null) {
			//System.out.printf("midiout is not Null\n");
			if (midiout.isOpen()) {
				//System.out.printf("midiout is open\n");
				SysexMessage	sysexMessage = new SysexMessage();
				//midiout.close();
				//System.out.printf("midiout closed\n");
		    	try {
					//midiout.open();
					//System.out.printf("midiout re-opened\n");
					receiver = midiout.getReceiver();
					//System.out.printf("midiout provded receiver\n");
				} catch (MidiUnavailableException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					sysexMessage.setMessage(buf, buf.length);
					//sysexMessage.setMessage(0xf0,buf, buf.length);
					//System.out.printf("trying to send\n");
					receiver.send(sysexMessage, -1);
					//System.out.printf("SysEx has been sent\n");
				} catch (InvalidMidiDataException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					Utils.show_error("Error sending Sysex MIDI message to port:\n" +
							midiout.getDeviceInfo().getName() + "\n" +
							"(" + e.getMessage() + ")");
				}		
			}
		}
	}

	public void sendSysexUpgrade(byte [] buf) {
		//System.out.printf("Preparing to send SysEx\n");
		if (midiout != null) {
			if (!midiout.isOpen()) {
				try {
					midiout.open();
				} catch (MidiUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			SysexMessage	sysexMessage = new SysexMessage();
			try {
				receiver = midiout.getReceiver();
			} catch (MidiUnavailableException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			try {
				sysexMessage.setMessage(buf, buf.length);
			} catch (InvalidMidiDataException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				Utils.show_error("Error sending Sysex MIDI message to port:\n" +
						midiout.getDeviceInfo().getName() + "\n" +
						"(" + e.getMessage() + ")");
			}
			receiver.send(sysexMessage, -1);
		}
	}

	public void requestConfigGlobalMisc() {
		byte [] sx = new byte[5];
		
		sx[0] = Constants.SYSEX_START;
		sx[1] = Constants.MD_SYSEX;
		sx[2] = (byte)chainId;
		sx[3] = Constants.MD_SYSEX_GLOBAL_MISC;
		sx[4] = Constants.SYSEX_END;
		sendSysex(sx);
	}

	public void requestConfigMisc() {
		byte [] sx = new byte[5];
		
		sx[0] = Constants.SYSEX_START;
		sx[1] = Constants.MD_SYSEX;
		sx[2] = (byte)chainId;
		sx[3] = Constants.MD_SYSEX_MISC;
		sx[4] = Constants.SYSEX_END;
		sendSysex(sx);
	}

	public void requestVersion() {
		byte [] sx = new byte[5];
		
		sx[0] = Constants.SYSEX_START;
		sx[1] = Constants.MD_SYSEX;
		sx[2] = (byte)chainId;
		sx[3] = Constants.MD_SYSEX_VERSION;
		sx[4] = Constants.SYSEX_END;
		sendSysex(sx);
	}

	public void requestMCU() {
		byte [] sx = new byte[5];
		
		sx[0] = Constants.SYSEX_START;
		sx[1] = Constants.MD_SYSEX;
		sx[2] = (byte)chainId;
		sx[3] = Constants.MD_SYSEX_MCU_TYPE;
		sx[4] = Constants.SYSEX_END;
		sendSysex(sx);
	}

	public void requestConfigCount() {
		byte [] sx = new byte[5];
		
		sx[0] = Constants.SYSEX_START;
		sx[1] = Constants.MD_SYSEX;
		sx[2] = (byte)chainId;
		sx[3] = Constants.MD_SYSEX_CONFIG_COUNT;
		sx[4] = Constants.SYSEX_END;
		sendSysex(sx);
	}

	public void requestConfigCurrent() {
		byte [] sx = new byte[5];
		
		sx[0] = Constants.SYSEX_START;
		sx[1] = Constants.MD_SYSEX;
		sx[2] = (byte)chainId;
		sx[3] = Constants.MD_SYSEX_CONFIG_CURRENT;
		sx[4] = Constants.SYSEX_END;
		sendSysex(sx);
	}

	public void requestConfigPedal() {
		byte [] sx = new byte[5];
		
		sx[0] = Constants.SYSEX_START;
		sx[1] = Constants.MD_SYSEX;
		sx[2] = (byte)chainId;
		sx[3] = Constants.MD_SYSEX_PEDAL;
		sx[4] = Constants.SYSEX_END;
		sendSysex(sx);
	}
	
	public void requestConfigPad(int pad_id) {
		byte [] sx = new byte[6];
		
		sx[0] = Constants.SYSEX_START;
		sx[1] = Constants.MD_SYSEX;
		sx[2] = (byte)chainId;
		sx[3] = Constants.MD_SYSEX_PAD;
		sx[4] = (byte)pad_id;
		sx[5] = Constants.SYSEX_END;
		sendSysex(sx);
	}

	public void requestConfigPos(int pad_id) {
		int id = pad_id;
		byte [] sx = new byte[6];
		sx[0] = Constants.SYSEX_START;
		sx[1] = Constants.MD_SYSEX;
		sx[2] = (byte)chainId;
		sx[3] = Constants.MD_SYSEX_POS;
		sx[4] = (byte)id;
		sx[5] = Constants.SYSEX_END;
		sendSysex(sx);
	}

	public void requestConfig3rd(int third_id) {
		byte [] sx = new byte[6];
		
		sx[0] = Constants.SYSEX_START;
		sx[1] = Constants.MD_SYSEX;
		sx[2] = (byte)chainId;
		sx[3] = Constants.MD_SYSEX_3RD;
		sx[4] = (byte)third_id;
		sx[5] = Constants.SYSEX_END;
		sendSysex(sx);
	}

	public void requestConfigCurve(int curve_id) {
		byte [] sx = new byte[6];
		
		sx[0] = Constants.SYSEX_START;
		sx[1] = Constants.MD_SYSEX;
		sx[2] = (byte)chainId;
		sx[3] = Constants.MD_SYSEX_CURVE;
		sx[4] = (byte)curve_id;
		sx[5] = Constants.SYSEX_END;
		sendSysex(sx);
	}

	public void requestConfigCustomName(int name_id) {
		byte [] sx = new byte[6];
		
		sx[0] = Constants.SYSEX_START;
		sx[1] = Constants.MD_SYSEX;
		sx[2] = (byte)chainId;
		sx[3] = Constants.MD_SYSEX_CUSTOM_NAME;
		sx[4] = (byte)name_id;
		sx[5] = Constants.SYSEX_END;
		sendSysex(sx);
	}

	public void requestConfigConfigName(int name_id) {
		byte [] sx = new byte[6];
		
		sx[0] = Constants.SYSEX_START;
		sx[1] = Constants.MD_SYSEX;
		sx[2] = (byte)chainId;
		sx[3] = Constants.MD_SYSEX_CONFIG_NAME;
		sx[4] = (byte)name_id;
		sx[5] = Constants.SYSEX_END;
		sendSysex(sx);
	}

	public void requestSaveToSlot(int config_id) {
		byte [] sx = new byte[6];
		
		sx[0] = Constants.SYSEX_START;
		sx[1] = Constants.MD_SYSEX;
		sx[2] = (byte)chainId;
		sx[3] = Constants.MD_SYSEX_CONFIG_SAVE;
		sx[4] = (byte)config_id;
		sx[5] = Constants.SYSEX_END;
		sendSysex(sx);
	}

	public void requestLoadFromSlot(int config_id) {
		byte [] sx = new byte[6];
		
		sx[0] = Constants.SYSEX_START;
		sx[1] = Constants.MD_SYSEX;
		sx[2] = (byte)chainId;
		sx[3] = Constants.MD_SYSEX_CONFIG_LOAD;
		sx[4] = (byte)config_id;
		sx[5] = Constants.SYSEX_END;
		sendSysex(sx);
	}

	public void requestArmBootloader() {
		byte [] sx = new byte[21];
		
		sx[0] = Constants.SYSEX_START;
		sx[1] = Constants.MD_SYSEX;
		sx[2] = (byte)chainId;
		sx[3] = Constants.MD_SYSEX_BOOTLOADER;
		sx[4] = 0x09;
		sx[5] = 0x03;
		sx[6] = 0x0a;
		sx[7] = 0x07;
		sx[8] = 0x0c;
		sx[9] = 0x01;
		sx[10] = 0x05;
		sx[11] = 0x0d;
		sx[12] = 0x03;
		sx[13] = 0x0f;
		sx[14] = 0x0e;
		sx[15] = 0x06;
		sx[16] = 0x04;
		sx[17] = 0x0b;
		sx[18] = 0x00;
		sx[19] = 0x05;
		sx[20] = Constants.SYSEX_END;
		sendSysex(sx);
	}
	
	public void requestVersionAndMcu() {
		// A workaround for Windows USB MIDI bug? or STM32F205 USB MIDI bug?
		// Where first 8 (2x4) MIDI SysEx replies from MegaDrum are lost
		requestVersion();
		requestMCU();
	}
	
	public void clear_midi_input() {
		byte [] result;
		result = null;
		int size = 1;
		
		if (midiin != null) {
			if (midiin.isOpen()) {
				// Clear MIDI input buffer
				while (size > 0) {
					result = dump_receiver.getByteMessage();
					if (result == null)
					{
						size = 0;
						//out("input buffer cleared");
					}
					else
					{
						size = result.length;
					}			
				}
			}
		}
	}	

	public void getMidi() {
		int size = 0;
		if (bufferIn == null) {
			bufferIn = dump_receiver.getByteMessage();
			if (bufferIn != null) {
				//System.out.printf("bufferIn length = %d\n", bufferIn.length);
				size = bufferIn.length;
				if (( bufferIn[0] == Constants.SYSEX_START) && (bufferIn[size-1] == Constants.SYSEX_END)) {
					sysexReceived = true;
				} else {
					// TO-DO
					sendMidiShortThru(bufferIn);
				}
			}
		}
	}

	public String [] getMidiInList() {
		String [] list;
		int nPorts;
		int port;
		aInfos = MidiSystem.getMidiDeviceInfo(); 
		nPorts = aInfos.length;
		int[] table = new int[nPorts];
		
		port = 0;		
		for (int i = 0; i < nPorts; i++) {
			try
			{
				midiin = MidiSystem.getMidiDevice(aInfos[i]);
				if (midiin.getMaxTransmitters() != 0)
				{
					table[port] = i;
					port++;
				}
			}
			catch (MidiUnavailableException e)
			{
				Utils.show_error("Error trying to list MIDI In ports.\n"
						+"(" + e.getMessage() + ")");
			}
			
		}
		list = new String[port];
		for (int i = 0; i < port; i++) {
			list[i] = aInfos[table[i]].getName();
		}
		
		return list;
	}
	
	public String [] getMidiOutList() {
		String [] list;
		int nPorts;
		int port;
		aInfos = MidiSystem.getMidiDeviceInfo(); 
		nPorts = aInfos.length;
		int[] table = new int[nPorts];
		
		port = 0;		
		for (int i = 0; i < nPorts; i++) {
			try
			{
				midiout = MidiSystem.getMidiDevice(aInfos[i]);
				if (midiout.getMaxReceivers() != 0)
				{
					table[port] = i;
					port++;
				}
			}
			catch (MidiUnavailableException e)
			{
				Utils.show_error("Error trying to list MIDI Out ports.\n"
						+"(" + e.getMessage() + ")");
			}
			
		}
		list = new String[port];
		for (int i = 0; i < port; i++) {
			list[i] = aInfos[table[i]].getName();
		}
		
		return list;		
	}
	
	public void setMidiInName(String name) {
		midiInName = name;
	}
	
	public void setMidiOutName(String name) {
		midiOutName = name;
	}

	public void setMidiThruName(String name) {
		midiThruName = name;
	}

	public void initPorts() {
		aInfos = MidiSystem.getMidiDeviceInfo(); 
		int nPorts;
		nPorts = aInfos.length;

		closeAllPorts();
		
 		if (!midiInName.equals("")) {
			try
			{
				for (int i = 0; i < nPorts; i++) {
					midiin = MidiSystem.getMidiDevice(aInfos[i]);
					if (midiin.getMaxTransmitters() != 0) {
						if (aInfos[i].getName().equals(midiInName)) {
					    	midiin = MidiSystem.getMidiDevice(aInfos[i]);
					    	midiin.open();
							transmitter = midiin.getTransmitter();
							transmitter.setReceiver(dump_receiver);
							//System.out.printf("Opened MIDI In Port: %s\n",configOptions.MidiInName);
							break;
						}
					}
				}
			} catch (MidiUnavailableException e)
			{
				Utils.show_error("Error trying to open MIDI In port:\n" +
						midiin.getDeviceInfo().getName());
			}
 		}

 		if (!midiOutName.equals("")) {
			try
			{
				for (int i = 0; i < nPorts; i++) {
					midiout = MidiSystem.getMidiDevice(aInfos[i]);
					if (midiout.getMaxReceivers() != 0) {
						if (aInfos[i].getName().equals(midiOutName)) {
					    	midiout = MidiSystem.getMidiDevice(aInfos[i]);
					    	midiout.open();
					    	receiver = midiout.getReceiver();
							//System.out.printf("Opened MIDI Out Port: %s\n",configOptions.MidiOutName);					    
							break;
						}
					}
				}
			} catch (MidiUnavailableException e)
			{
				Utils.show_error("Error trying to open MIDI Out port:\n" +
						midiout.getDeviceInfo().getName());
			}
 		}

 		if ((!midiThruName.equals("")) && (useMidiThru)) {
			try
			{
				for (int i = 0; i < nPorts; i++) {
					midithru = MidiSystem.getMidiDevice(aInfos[i]);
					if (midithru.getMaxReceivers() != 0) {
						if (aInfos[i].getName().equals(midiThruName)) {
					    	midithru = MidiSystem.getMidiDevice(aInfos[i]);
					    	midithru.open();
					    	thruReceiver = midithru.getReceiver();
							break;
						}
					}
				}
			} catch (MidiUnavailableException e)
			{
				Utils.show_error("Error trying to open MIDI Thru port:\n" +
						midithru.getDeviceInfo().getName());
			}
 		}
 		
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

	public static void write(Receiver rr, int[] buf, int ind, int size)
	{
		byte[] ba = new byte[(size*2) + 2];
		int p = 0;
		ba[p++] =(byte) 0xf0;
		while (size > 0)
		{
			//System.out.printf("%h\n", buf[ind]);
			ba[p++] = (byte) ((byte) (buf[ind]>>4) & 0x0f);
			//System.out.printf("%h", ba[p-1]);
			ba[p++] = (byte) ((byte) (buf[ind]) & 0x0f);
			//System.out.printf("%h ", ba[p-1]);
			size --;
			ind++;
		}
		//System.out.printf("\n");
		ba[p++] = (byte) 0xf7;
		SysexMessage msg = new SysexMessage();
		try {
			msg.setMessage(ba, p);
			rr.send(msg, -1);
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			Utils.show_error("Error sending Sysex.\n" +
					"(" + e.getMessage() + ")");
		}
	}	

	public void writeMidX(Receiver rr, int[] buf, int ind, int size)
	{
		int p = 0;
		while ((p + Block_size) < size) {
			write(rr,buf,ind + p, Block_size);
			p += Block_size;
		}
		if (p < size) {
			write(rr,buf,ind + p, size - p);
		}
	}	

	public void writeMid(Receiver rr, int[] buf, int ind, int size)
	{
		byte[] buffer = new byte[(size*2) + 2];
		int p = 0;
		buffer[0] = (byte)0xf0;
		for (p=0;p<size;p++) {
			buffer[(p*2) + 1] = (byte)((buf[ind+p]>>4) & 0x0f);
			buffer[(p*2) + 2] = (byte)((buf[ind+p]) & 0x0f);
		}
		buffer[buffer.length - 1] = (byte)0xf7;
		sendSysexUpgrade(buffer);
		//sendSysex(buffer);
	}
/*	
	public void doFirmwareUpgrade (Upgrade parent, File file) throws IOException {		
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;
		String resultString = "Upgrade completed successufully";
		int[] buffer = new int[0x40000];	// Data buffer for sending the data

		byte[] receivedBuffer;
		int receivedByte;		// One byte received from COM port
		int retries = 0;		// Number of tries so far
		int index;				// Index in the data buffer
		int frameSize;				// Number of bytes in the current frame
		int bufferSize = 0;			// Total number of bytes in the buffer
		int bytesSent = 0;			// Number of bytes sent so far
		int prevBytesSent = 0;
		int nBytes;
		int inDelay;
		boolean firstDelay;
		int upgradeError = 0;

		return;
		System.out.println("Not implemented yet");
		//closeAllPorts();
		//initPorts();
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			Utils.show_error("Error loading from file:\n" +
					file.getAbsolutePath() + "\n" +
					"(" + e.getMessage() + ")");
		}
		bis = new BufferedInputStream(fis);
		dis = new DataInputStream(bis);

		//("Starting upgrade\n");
				
		if (configOptions.mcuType > 2) {
			// Restart ARM based MegaDrum in bootloader mode
			requestArmBootloader();
			//System.out.printf("Sent reboot request\n");
			delayMs(4000);
		}
		closeAllPorts();
		//System.out.printf("Loading Firmware file\n");
		while (dis.available() > 1)
		{
			buffer[bufferSize] = readHex(dis);
			bufferSize++;
		}
		//System.out.printf("Firmware file loaded\n");
		initPorts();
		parent.getProgressBar().setMinimum(0);
		parent.getProgressBar().setMaximum(bufferSize);
		dis.close();
		bis.close();
		fis.close();
		//System.out.printf("Firmware size is %d bytes\n", bufferSize);
		
		clear_midi_input();
		firstDelay = true;
		for(index = 0; index < bufferSize; index += frameSize)
		{
			frameSize = ((buffer[index] << 8) | buffer[index + 1]) + 2;
			//clear_midi_input();
			if (((bytesSent-prevBytesSent)*100/(bufferSize/10)) > 1) {
				parent.setProgressBar(bytesSent);
				prevBytesSent = bytesSent;				
			}
			//System.out.printf("index=%d , frameSize=%d \n", index, frameSize);

			Block_size = frameSize;				// Seem it fails
			//if (frameSize < 80) Block_size = 2;	// with some firmware sizes
			Block_size = 2;
			//if (Block_size > frameSize) Block_size = frameSize;
			writeMid(receiver, buffer, index, frameSize);
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
			    delayMs(2);
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
						delayMs(10);
					} else {
						//System.out.println("\nCRC error. File damaged.\n");
						switch (receivedByte) {
						case Constants.Error_CRC:
							upgradeError = 2;
							resultString = "CRC error. File damaged?";
							break;
						case Constants.Error_NoResponse:
							upgradeError = 3;
							resultString = "MegaDrum is not responding";
							break;
						case Constants.Error_Read:
							upgradeError = 4;
							resultString = "Read error. Bad communication?";
							break;
						default:
							upgradeError = 99;
							resultString = "Unknown error";
							break;
						}
						//System.out.printf("Exit with error: %s\n", resultString);
					}
					break;
			}
			if (upgradeCancelled) {
				//System.out.println("Upgrade cancelled\n");
				upgradeError = 1;
				resultString = "Upgrade cancelled";
			}
			if (upgradeError > 0) {
				break;
			}
		}		
		
		parent.midiFinished(upgradeError, resultString);
	}
*/	
	public boolean isMidiOpen() {
		boolean result = false;
		if ((midiin != null) && (midiout != null)) {
			if (midiin.isOpen() && midiout.isOpen()) {
				result = true;
			}
		}
		return result;
	}
	
}
