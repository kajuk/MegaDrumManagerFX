package info.megadrum.managerfx.utils;

import java.util.Timer;
import java.util.TimerTask;

import info.megadrum.managerfx.data.Config3rd;
import info.megadrum.managerfx.data.ConfigConfigName;
import info.megadrum.managerfx.data.ConfigCurve;
import info.megadrum.managerfx.data.ConfigCustomName;
import info.megadrum.managerfx.data.ConfigGlobalMisc;
import info.megadrum.managerfx.data.ConfigMisc;
import info.megadrum.managerfx.data.ConfigPad;
import info.megadrum.managerfx.data.ConfigPedal;
import info.megadrum.managerfx.data.ConfigPositional;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.web.WebView;

public class Utils {

	public static void delayMs(int ms) {
	    try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			Utils.show_error("Unrecoverable timer error. Exiting.\n" +
					"(" + e.getMessage() + ")");
			System.exit(1);
		}
		
	}

	public static void show_error(String msg) {
		//System.out.printf("Utils.show_error -> %s\n",msg);
		Alert alert = new Alert(AlertType.ERROR);
	    alert.setHeaderText("Error!");
	    WebView webView = new WebView();
	    webView.getEngine().loadContent(msg);
	    webView.setPrefSize(300, 120);
	    alert.getDialogPane().setContent(webView);
		Timer warning_timer = new Timer();
		warning_timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						alert.showAndWait();
					}
				});
			}
		}, 10);
	}

	public static byte [] byte2sysex (byte b) {
		byte [] result = new byte[2];

		result[0] = (byte)((b&0xf0)>>4);
		result[1] = (byte)(b&0x0f);
		return result;
	}

	public static byte [] short2sysex (short s) {
		byte [] result = new byte[4];

		result[0] = (byte)((s&0x00f0)>>4);
		result[1] = (byte)(s&0x000f);
		result[2] = (byte)((s&0xf000)>>12);
		result[3] = (byte)((s&0x0f00)>>8);
		return result;
	}
	
	public static byte sysex2byte (byte [] sx) {
		byte result;
		
		result = (byte)(((sx[0]&0x0f)<<4)|(sx[1]&0x0f));
		return result;
	}

	public static short sysex2short (byte [] sx) {
		short result;
		
		result = (short)(((sx[0]&0x0f)<<4)|(sx[1]&0x0f)|((sx[2]&0x0f)<<12)|((sx[3]&0x0f)<<8));
		return result;
	}

	public static int validateInt(int value, int min, int max, int fallBack){
		//returns value if min <= value <= max
		//otherwise returns fallBack
		if ((value>=min) && (value<=max)) {
			return value;
		} else {
			return fallBack;
		}
	}
	
	public static Double validateDouble(Double value, Double min, Double max, Double fallBack){
		//returns value if min <= value <= max
		//otherwise returns fallBack
		if ((value>=min) && (value<=max)) {
			return value;
		} else {
			return fallBack;
		}
	}
	
	public static short validateShort(short value, int min, int max, short fallBack){
		//returns value if min <= value <= max
		//otherwise returns fallBack
		if ((value>=min) && (value<=max)) {
			return value;
		} else {
			return fallBack;
		}
	}

}
