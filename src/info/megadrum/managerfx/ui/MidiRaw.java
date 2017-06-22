package info.megadrum.managerfx.ui;

import java.util.ArrayList;
import java.util.List;

import info.megadrum.managerfx.utils.Constants;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;


public class MidiRaw extends VBox {
	private Text textHeader;
	private WebView webView;
	private WebEngine webEngine;
	
	private long prevTime = 0;
	private Double width, height;
	private String formatStringHeader;
	private String formatString;
	private String stringAllHtml;
	private List<String> listOfHtmlLines;
	private StringBuilder htmlHead;
	private StringBuilder htmlTail;
	
	//private VBox vBoxRoot;
	private VBox vBoxHeader;

	public MidiRaw () {
		textHeader = new Text();
		textHeader.setStyle("-fx-font-family: monospace");
		vBoxHeader = new VBox();
		vBoxHeader.getChildren().add(textHeader);
		textHeader.setStyle("-fx-border-width: 1px; -fx-border-color: grey");

		webView = new WebView();
		stringAllHtml = new String();
		webEngine = webView.getEngine();
        htmlHead= new StringBuilder().append("<html>");  
        htmlHead.append("<head>");  
        htmlHead.append("   <script language=\"javascript\" type=\"text/javascript\">");  
        htmlHead.append("       function toBottom(){");  
        htmlHead.append("           window.scrollTo(0, document.body.scrollHeight);");  
        htmlHead.append("       }");  
        htmlHead.append("   </script>"); 
        htmlHead.append("   <style>"); 
        htmlHead.append("   	body {");
        htmlHead.append("  			font-family:Consolas,Monaco,Lucida Console,Liberation Mono,DejaVu Sans Mono,Bitstream Vera Sans Mono,Courier New, monospace;");
        htmlHead.append("  			font-size:14px;");
        htmlHead.append("  			white-space:pre;");
        htmlHead.append("  			line-height: 0.3;");
        htmlHead.append("   	}");
        htmlHead.append("   </style>");         
        htmlHead.append("</head>");  
        htmlHead.append("<body onload='toBottom()'>");  
        htmlTail = new StringBuilder().append("</body></html>");
        listOfHtmlLines = new ArrayList<String>();

		formatStringHeader = "%-16s %-5s %-6s %-6s %-6s   %-6s   %-30s\n";
		formatString = "%-10s %-3s %-5s %-5s %-5s %-5s %-30s\n";
		textHeader.setText(String.format(formatStringHeader,
				"   Time delta", "Ch #", "Data1", "Data2", "Data3", "Note", "MIDI Message Name"));
		getChildren().addAll(vBoxHeader, webView);
		prevTime = System.nanoTime();
		//setStyle("-fx-background-color: lightgreen");
		textHeader.setFont(new Font(14.0));
		vBoxHeader.setMinHeight(16);
		vBoxHeader.setMaxHeight(16);
	}
	
	public void respondToResize(Double w, Double h) {
		width = w;
		height = h;
		setMinSize(width, height);
		setMaxSize(width, height);
		vBoxHeader.setMinHeight(18);
		vBoxHeader.setMaxHeight(18);
		textHeader.setFont(new Font(14.0));
		webView.setMinHeight(h - 5);
		webView.setMaxHeight(h - 5);
		//vBoxRoot.setMinSize(w, h);
		//vBoxRoot.setMaxSize(w, h);
	}

	public void addRawMidi (byte [] buffer) {
		String ch = "1";
		String d1 = "0";
		String d2 = "";
		String d3 = "";
		String note = "";
		String name = "";
		String time = "";
		String fontColor = "black";
		Double timeDiff;
		
		timeDiff = (Double)((double)(System.nanoTime() - prevTime)/1000000000);
		prevTime = System.nanoTime();
		time = String.format("%#8.3f", timeDiff);

		switch (buffer.length) {
		case 1:
			//shortMessage.setMessage(buf[0]);
			break;
		case 2:
			//shortMessage.setMessage(buf[0], buf[1],0);
			switch (buffer[0]&0xf0) {			
			case 0xc0:
				fontColor = Constants.MIDI_PC_COLOR;
				name = "Program Change";
				break;
			case 0xd0:
				fontColor = Constants.MIDI_CH_PR_COLOR;
				name = "Channel Pressure";
				break;
			default:
				break;
			}
			note = String.format(" %s% d", Constants.NOTES_NAMES[(buffer[1]&0x7f) % 12], ((buffer[1]&0x7f) / 12) - 1);
			ch = String.format("%2d", (buffer[0]&0x0f) + 1);
			d1 = String.format("0x%02x", buffer[0]&0xff);
			d2 = String.format("0x%02x", buffer[1]&0xff);
			d3 = String.format("      ");
			break;
		default:
			//shortMessage.setMessage(buf[0], buf[1],buf[2]);
			switch (buffer[0]&0xf0) {
			case 0x80:
				fontColor = Constants.MIDI_NOTE_OFF_COLOR;
				name = "Note Off";
				break;
			case 0x90:
				if (buffer[2] == 0) {
					fontColor = Constants.MIDI_NOTE_OFF_COLOR;
					name = "Note Off";
				} else {
					name = "Note On";
					fontColor = Constants.MIDI_NOTE_ON_COLOR;
				}
				break;
			case 0xa0:
				fontColor = Constants.MIDI_AFTERTOUCH_COLOR;
				name = "Aftertouch";
				break;
			case 0xb0:
				fontColor = Constants.MIDI_CC_COLOR;
				name = "CC: ";
				name += getControlChangeType(buffer[1]&0x7f);
				break;
			case 0xc0:
				fontColor = Constants.MIDI_PC_COLOR;
				name = "Program Change";
				break;
			case 0xd0:
				fontColor = Constants.MIDI_CH_PR_COLOR;
				name = "Channel Pressure";
				break;
			case 0xe0:
				fontColor = Constants.MIDI_PITCH_COLOR;
				name = "Pitch Wheel";
				break;
			default:
				break;
			}
			note = String.format("%s% d", Constants.NOTES_NAMES[(buffer[1]&0x7f) % 12], ((buffer[1]&0x7f) / 12) - 1);
			ch = String.format("%2d", (buffer[0]&0x0f) + 1);
			d1 = String.format("0x%02x", buffer[0]&0xff);
			d2 = String.format("0x%02x", buffer[1]&0xff);
			d3 = String.format("0x%02x", buffer[2]&0xff);
			//note = "     ?";
			break;
		}
		
		if (listOfHtmlLines.size() > 200) {
			listOfHtmlLines.remove(0);
		}
		listOfHtmlLines.add(String.format("<p style=color:" + fontColor + ">" + formatString + "</p>", time, ch, d1, d2, d3, note, name ));
		stringAllHtml = String.join("", listOfHtmlLines);
		webEngine.loadContent(htmlHead.toString() + stringAllHtml + htmlTail.toString());
	}

	public String getControlChangeType (Integer n) {
		String name = "";
		switch (n) {
		case 0:
			name += "Bank Select";
			break;
		case 1:
			name += "Modulation Wheel";
			break;
		case 2:
			name += "Breath Controller";
			break;
		case 4:
			name += "Foot Controller";
			break;
		case 5:
			name += "Partamento Time";
			break;
		case 6:
			name += "Data Entry MSB";
			break;
		case 7:
			name += "Channel Volume";
			break;
		case 8:
			name += "Balance";
			break;
		case 0xa:
			name += "Pan";
			break;
		case 0xb:
			name += "Expression Ctrl";
			break;
		case 0xc:
			name += "Effect Control 1";
			break;
		case 0xd:
			name += "Effect Control 2";
			break;
		case 0x10:
			name += "General Controller 1";
			break;
		case 0x11:
			name += "General Controller 2";
			break;
		case 0x12:
			name += "General Controller 3";
			break;
		case 0x13:
			name += "General Controller 4";
			break;
		case 0x20:
			name += "LSB Control 0";
			break;
		case 0x21:
			name += "LSB Control 1";
			break;
		case 0x22:
			name += "LSB Control 2";
			break;
		case 0x23:
			name += "LSB Control 3";
			break;
		case 0x24:
			name += "LSB Control 4";
			break;
		case 0x25:
			name += "LSB Control 5";
			break;
		case 0x26:
			name += "LSB Control 6";
			break;
		case 0x27:
			name += "LSB Control 7";
			break;
		case 0x28:
			name += "LSB Control 8";
			break;
		case 0x29:
			name += "LSB Control 9";
			break;
		case 0x2a:
			name += "LSB Control 10";
			break;
		case 0x2b:
			name += "LSB Control 11";
			break;
		case 0x2c:
			name += "LSB Control 12";
			break;
		case 0x2d:
			name += "LSB Control 13";
			break;
		case 0x2e:
			name += "LSB Control 14";
			break;
		case 0x2f:
			name += "LSB Control 15";
			break;
		case 0x30:
			name += "LSB Control 16";
			break;
		case 0x31:
			name += "LSB Control 17";
			break;
		case 0x32:
			name += "LSB Control 18";
			break;
		case 0x33:
			name += "LSB Control 19";
			break;
		case 0x34:
			name += "LSB Control 20";
			break;
		case 0x35:
			name += "LSB Control 21";
			break;
		case 0x36:
			name += "LSB Control 22";
			break;
		case 0x37:
			name += "LSB Control 23";
			break;
		case 0x38:
			name += "LSB Control 24";
			break;
		case 0x39:
			name += "LSB Control 25";
			break;
		case 0x3a:
			name += "LSB Control 26";
			break;
		case 0x3b:
			name += "LSB Control 27";
			break;
		case 0x3c:
			name += "LSB Control 28";
			break;
		case 0x3d:
			name += "LSB Control 29";
			break;
		case 0x3e:
			name += "LSB Control 30";
			break;
		case 0x3f:
			name += "LSB Control 31";
			break;
		case 0x40:
			name += "Damper Pedal";
			break;
		case 0x41:
			name += "Partamento";
			break;
		case 0x42:
			name += "Sostenuto";
			break;
		case 0x43:
			name += "Soft Pedal";
			break;
		case 0x44:
			name += "Legato Footswitch";
			break;
		case 0x45:
			name += "Hold 2";
			break;
		case 0x46:
			name += "Sound Controller 1";
			break;
		case 0x47:
			name += "Sound Controller 2";
			break;
		case 0x48:
			name += "Sound Controller 3";
			break;
		case 0x49:
			name += "Sound Controller 4";
			break;
		case 0x4a:
			name += "Sound Controller 5";
			break;
		case 0x4b:
			name += "Sound Controller 6";
			break;
		case 0x4c:
			name += "Sound Controller 7";
			break;
		case 0x4d:
			name += "Sound Controller 8";
			break;
		case 0x4e:
			name += "Sound Controller 9";
			break;
		case 0x4f:
			name += "Sound Controller 10";
			break;
		case 0x50:
			name += "General Controller 5";
			break;
		case 0x51:
			name += "General Controller 6";
			break;
		case 0x52:
			name += "General Controller 7";
			break;
		case 0x53:
			name += "General Controller 8";
			break;
		case 0x54:
			name += "Partamento Control";
			break;
		case 0x58:
			name += "Velocity Prefix";
			break;
		case 0x5b:
			name += "Effects 1 Depth";
			break;
		case 0x5c:
			name += "Effects 2 Depth";
			break;
		case 0x5d:
			name += "Effects 3 Depth";
			break;
		case 0x5e:
			name += "Effects 4 Depth";
			break;
		case 0x5f:
			name += "Effects 5 Depth";
			break;
		case 0x60:
			name += "Data Increment";
			break;
		case 0x61:
			name += "Data Decrement";
			break;
		case 0x62:
			name += "NRPN LSB";
			break;
		case 0x63:
			name += "NRPN MSB";
			break;
		case 0x64:
			name += "RPN LSB";
			break;
		case 0x65:
			name += "RPN MSB";
			break;
		case 0x78:
			name += "All Sound Off";
			break;
		case 0x79:
			name += "Reset All";
			break;
		case 0x7a:
			name += "Local Control";
			break;
		case 0x7b:
			name += "All Notes Off";
			break;
		case 0x7c:
			name += "Omni Mode Off";
			break;
		case 0x7d:
			name += "Omni Mode On";
			break;
		case 0x7e:
			name += "Mono Mode On";
			break;
		case 0x7f:
			name += "Poly Mode On";
			break;
		default:
			break;
		}
		return name;	
	}
	
}
