/** Eck Exercise 6.8
 * User enters data into text area
 * On clicking the Process the Text button the number of line, words and chars are shown
 * @author jd07
 *
 */

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
//import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class CountLinesGui extends Application {

	public static void main(String[] args) {
        launch(args);
    }

	private TextArea textInput;
	private Button processTextButton;
	private Label lineLabel;
	private Label wordLabel;
	private Label charLabel;
	private int nChars = 0;
	private int nWords = 0;
	private int nLines = 0;
	
    public void start(Stage stage) {
    	
    	textInput = new TextArea();
    	textInput.setPrefRowCount(15); // height of text area 
    	textInput.setPrefColumnCount(30); // width of text area
    	
    	processTextButton = new Button("Process the Text");
    	processTextButton.setOnAction(e->processText());
    	// Border Pane allows to centre button
    	BorderPane processButtonPane = new BorderPane(processTextButton);
    	
    	lineLabel = makeLabel("Number of Lines:");
    	wordLabel = makeLabel("Number of Words:");
    	charLabel = makeLabel("Number of Characters:");
    	
    	// VBox allows components to have preferred height.
    	int vgap = 4;
//    	VBox root = new VBox(vgap,textInput,processTextButton,
//    			lineLabel,wordLabel,charLabel);
    	VBox root = new VBox(vgap,textInput,processButtonPane,
    			lineLabel,wordLabel,charLabel);
    	root.setStyle(
    	        "-fx-background-color: #009; -fx-border-color: #009; -fx-border-width:3px" );
    	Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Count Lines");
        stage.setResizable(false);
        stage.show();

        
    }  // end start();

    private Label makeLabel(String text) {
    	String style = 
    	         "-fx-padding: 5px; -fx-font: bold 14pt serif; -fx-background-color: white";
    	
    	 Label label = new Label(text);
         label.setMaxWidth(1000);
         label.setStyle(style);
         return label;
    } // end makeLabel
    
    private void processText() {
    	String str = textInput.getText();
    	nChars = countChars(str);
    	nWords = countWords(str);
    	nLines = countLines(str);
    	
    	if (nChars > 0 ) {
    		lineLabel.setText("Number of Lines: " + nLines);
    		wordLabel.setText("Number of Words: " + nWords);
    		charLabel.setText("Number of Characters: " + nChars);
    	} else { 
    		lineLabel.setText("Number of Lines:");
    		wordLabel.setText("Number of Words:");
    		charLabel.setText("Number of Characters:");
    	} // end if
    	
    } // end processText
    
    private int countChars(String str) {
    	int count = 0;
    	for (int i = 0; i < str.length(); i++) {
    		count++;
    	} // end for
    	return count;
    } // end countChars
    
    private int countWords(String str) {
    	int count = 0; // word count
//		int startWord = endWord = 0;
    	char ch;
		int i = 0;
		while (i<str.length()) {
			ch = str.charAt(i);
			if (Character.isLetter(ch)) {
//				startWord = i;
				while (true) {
					i++;
					ch = str.charAt(i);
					if (!Character.isLetter(ch)  && ch != '\'') {
//						endWord = i;
//						subStr = inputStr.substring(startWord,endWord);
//						System.out.println(subStr);
						count++;
						break;
					}						
				}
			} else 
				i++;
		}

    	return count;
    } // end countWords

    private int countLines(String str) {
    	int count = 0;
    	for (int i = 0; i < str.length(); i++) {
  		if (str.charAt(i) == String.format("\n").charAt(0) ) {
    			count++;
    		} // end if
    	} // end for
    	return count;    	
    } // end countLines
}
