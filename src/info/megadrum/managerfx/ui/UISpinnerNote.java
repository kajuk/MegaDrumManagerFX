package info.megadrum.managerfx.ui;

import info.megadrum.managerfx.utils.Constants;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;

public class UISpinnerNote extends UIControl {
	private SpinnerFast<Integer> spinnerFast;
	private SpinnerValueFactory<Integer> valueFactory;
	private Integer 	minValue;
	private Integer 	maxValue;
	private Integer 	step;
	private Label 		labelNote;
	private CheckBox	checkBoxNoteLinked;
	private boolean		linkedNote = false;
	private boolean		disabledNoteAllowed = false;
	private GridPane	layoutC;
	private Integer		octaveShift = 0;
	private Integer 	octave;
	private Integer 	note_pointer;
	private Integer		linkedChangedFromSet = 0;
	private Boolean		mainNote = false;
	private static final String [] note_names = {"C ", "C#", "D ", "D#", "E ", "F ", "F#", "G ", "G#", "A ", "A#", "B "};


	public UISpinnerNote(Boolean showCopyButton) {
		super(showCopyButton);
		init();
	}
		
	public UISpinnerNote(String labelText, Boolean showCopyButton) {
		super(labelText, showCopyButton);
		init();
	}

	public UISpinnerNote(String labelText, Boolean showCopyButton, Boolean showLinked) {
		super(labelText, showCopyButton);
		linkedNote =  showLinked;
		init();
	}

	private void init() {
		init(0,127,0,1);
	}
	
	private void init(Integer min, Integer max, Integer initial, Integer s) {
		minValue = min;
		maxValue = max;
		intValue = initial;
		valueType = Constants.VALUE_TYPE_INT;
		step = s;
		valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(minValue, maxValue, intValue, step);

		spinnerFast = new SpinnerFast<Integer>();
		spinnerFast.setValueFactory(valueFactory);
		spinnerFast.setEditable(true);
		//uispinner.seton
		//uispinner.getEditor().setStyle("-fx-text-fill: black; -fx-alignment: CENTER_RIGHT;"
		//		);    
		//uispinner.set
		spinnerFast.getEditor().textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				//System.out.println("Spinner change event");
				// Spinner number validation
		    	if (changedFromSet > 0) {
		    		changedFromSet--;
		        	//System.out.printf("changedFromSet reduced to %d for %s\n", changedFromSet, label.getText());
		    	} else {
		            if (!newValue.matches("\\d*")) {
		            	spinnerFast.getEditor().setText(intValue.toString());
		            } else {
						if (newValue.matches("")) {
							spinnerFast.getEditor().setText(intValue.toString());
						} else {
							if (intValue.intValue() != Integer.valueOf(newValue).intValue()) {
								//System.out.printf("%s: new value = %d, old value = %d\n",label.getText(),Integer.valueOf(newValue),intValue );
								intValue = Integer.valueOf(newValue);
								changeNoteName();
								if (mainNote) {
									fireControlChangeEvent(new ControlChangeEvent(this), Constants.CONTROL_CHANGE_EVENT_NOTE_MAIN);									
								} else {
									fireControlChangeEvent(new ControlChangeEvent(this), 0);
								}
								if (syncState != Constants.SYNC_STATE_UNKNOWN) {
									if (intValue.intValue() == mdIntValue.intValue()) {
										setSyncState(Constants.SYNC_STATE_SYNCED);						
									} else {
										setSyncState(Constants.SYNC_STATE_NOT_SYNCED);
									}
									
								}
								//resizeFont();
							}
						}
		            }		    		
		    	}
				
			}
	    });
		
	    
	    layoutC = new GridPane();
	    labelNote = new Label("");
	    checkBoxNoteLinked = new CheckBox();
	    checkBoxNoteLinked.setTooltip(new Tooltip("Linked to Note"));
	    //HBox.setHgrow(labelNote, Priority.ALWAYS);
	    layoutC.setAlignment(Pos.CENTER_LEFT);
	    
		GridPane.setConstraints(spinnerFast, 0, 0);
		GridPane.setHalignment(spinnerFast, HPos.LEFT);
		GridPane.setValignment(spinnerFast, VPos.CENTER);

		GridPane.setConstraints(labelNote, 1, 0);
		GridPane.setHalignment(labelNote, HPos.CENTER);
		GridPane.setValignment(labelNote, VPos.CENTER);

		GridPane.setConstraints(checkBoxNoteLinked, 2, 0);
		GridPane.setHalignment(checkBoxNoteLinked, HPos.RIGHT);
		GridPane.setValignment(checkBoxNoteLinked, VPos.CENTER);

		if (linkedNote) {
			layoutC.getChildren().addAll(spinnerFast,labelNote,checkBoxNoteLinked);		
			checkBoxNoteLinked.selectedProperty().addListener(new ChangeListener<Boolean>() {
			    @Override
			    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
			    	if (linkedChangedFromSet > 0) {
			    		linkedChangedFromSet = 0;
			    	} else {
						fireControlChangeEvent(new ControlChangeEvent(this), Constants.CONTROL_CHANGE_EVENT_NOTE_LINKED);
			    	}
			    }
			});
		} else {
			layoutC.getChildren().addAll(spinnerFast,labelNote);
		}

		//initControl(uispinner);
		changeNoteName();
		initControl(layoutC);
	}

    private void resizeFont(Double h) {
		Double we = h*2.2;
		Integer l = maxValue.toString().length();
		Double ll = (16/(16 + l.doubleValue()))*0.31;
		we = we*ll;
		//uispinner.getEditor().setFont(new Font(h*0.4));
		//uispinner.getEditor().setFont(new Font(we));    	
		spinnerFast.getEditor().setFont(new Font(we));
		//rootPane.setStyle("-fx-font-size: " + tabsFontSize.toString() + "pt");
		//layout.setStyle("-fx-font-size: 3pt");
    }

    @Override
    public void respondToResize(Double w, Double h) {
    	super.respondToResize(w, h);
	  	Double spinnerButtonsFontSize = h*0.3;
    	Double checkBoxFontSize = h*0.26;
		spinnerFast.setMinHeight(h);
		spinnerFast.setMaxHeight(h);
		spinnerFast.setMaxWidth(h*2.8);
		spinnerFast.setMinWidth(h*2.8);
		
		layoutC.getColumnConstraints().clear();
		//layoutC.getColumnConstraints().add(new ColumnConstraints((w - padding*2)*0.2 + 30));
		layoutC.getColumnConstraints().add(new ColumnConstraints(w*0.265));
		if (linkedNote) {
			layoutC.getColumnConstraints().add(new ColumnConstraints(w*0.22));
			layoutC.getColumnConstraints().add(new ColumnConstraints(w*0.02));			
		} else {
			layoutC.getColumnConstraints().add(new ColumnConstraints(w*0.23));
			layoutC.getColumnConstraints().add(new ColumnConstraints(w*0.01));
		}
		layoutC.getRowConstraints().clear();
		layoutC.getRowConstraints().add(new RowConstraints(h-padding*2 - 1));

		// Spinner buttons size is actually controlled by -fx-font-size on the spinner
		spinnerFast.setStyle("-fx-font-size: " + spinnerButtonsFontSize.toString() + "pt");		
		resizeFont(h);
		if (linkedNote) {
			labelNote.setFont(new Font(h*0.50));			
	    	// Padding setting really should be done via css and on init.
	    	// This is a temp hack
			//checkBoxNoteLinked.lookup(".box").setStyle("-fx-padding: 0.6em 0.6em 0.8em 0.8em;");
			checkBoxNoteLinked.setStyle("-fx-font-size: " + checkBoxFontSize.toString() + "pt");
		} else {
			labelNote.setFont(new Font(h*0.55));
		}
   }
    
    public void changeNoteName() {
		int note_number;
		int base;
		String note_text;
		note_number = intValue;
		if ((note_number > 0) || (!disabledNoteAllowed)) {
			octave = note_number/12 ;
			base = octave*12;
			note_pointer = note_number - base;
			note_text = note_names[note_pointer] + " " + Integer.toString(octave - 3 + octaveShift);
			labelNote.setText(note_text);
			labelNote.setTooltip(new Tooltip("Note = " + note_text));
		} else {
			labelNote.setText("Disabled");
			labelNote.setTooltip(new Tooltip("Input is Disabled"));
		}		
    }
    
    public void setDisabledNoteAllowed(Boolean b) {
    	disabledNoteAllowed = b;
    	changeNoteName();
    }

    public void uiCtlSetValue(Integer n, Boolean setFromSysex) {
    	if (intValue.intValue() != n.intValue()) {
        	changedFromSet++;
    		intValue = n;
    	}
    	//System.out.printf("New value = %d for %s\n", intValue, label.getText());
    	//System.out.printf("changedFromSet = %d for %s\n", changedFromSet, label.getText());
    	if (setFromSysex) {
    		setSyncState(Constants.SYNC_STATE_SYNCED);
    		mdIntValue = n;
    	} else {
        	updateSyncStateConditional();
    	}
    	valueFactory.setValue(n);
    	spinnerFast.getEditor().setText(intValue.toString());
		resizeFont(spinnerFast.getHeight());
		changeNoteName();
    }
    
    public Integer uiCtlGetValue() {
    	return intValue;
    }
    
    public void setLinked(Boolean linked) {
    	if (linked != checkBoxNoteLinked.isSelected()) {
        	linkedChangedFromSet = 1;
    		checkBoxNoteLinked.setSelected(linked);
    	}
    	spinnerFast.setDisable(linked);
    }
    
    public Boolean getLinked() {
    	return checkBoxNoteLinked.isSelected();
    }
    
    public void setNoteIsMain(Boolean main) {
    	mainNote = main;
    }

/*
    @Override
	public void setControlMinWidth(Double w) {
    	// don't change spinner control width so override setControlMinWidth here
	}
*/
}
