/** Eck Exercise 6.7
 * User enters numbers and statistics (N, sum, mean, SD) are updated
 * User types numbers into a text box: the stats can be updated by clicking the Enter button or pressing Return
 * Stats can be cleared by clicking the clear button.
 * 
 * GUI:
 * 	1st row: title
 * 	2nd row: text box, Enter button, Clear button
 * 		Grouped into one HBox
 *  3rd row: "Number of Entries: ..."
 *  4th row: "Sum: ..."
 *  5th row: "Average: ..."
 *  6th row: "Standard Deviation: ..."
 *  Each row grouped into one VBox
 * 
 * Global Variables to keep track of stats:
 * 	count
 *  sum
 *  Average & SD can be calculated from count & sum.
 *  
 *  
 * @author jd07
 *
 */

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class StatCalcGraphics extends Application {
	private TextField entryBox; // text field where data will be entered by user
	private Label nStat = new Label(); // text field where count will be shown
	private Label sStat = new Label(); // text field where sum will be shown
	private Label mStat = new Label(); // text field where mean will be shown
	private Label sdStat = new Label(); // text field where sd will be shown
	private int count = 0; // count of data entries
	private double sum = 0; // sum of data entries
	private double squaredSum = 0; // sum of squares
	
    public static void main(String[] args) {
        launch(args);
    }

	
    public void start(Stage stage) {
    	
    	// title row
    	Label title = new Label("Enter a number, click Enter:");
    	title.setTextFill(Color.WHITE);
    	title.setStyle("-fx-font-weight:bold; " + 
    			"-fx-background-color: black; -fx-padding: 6px");
    	title.setAlignment(Pos.CENTER);
    	title.setMaxWidth(Double.POSITIVE_INFINITY);
    	
    	// data entry row & buttons
    	entryBox = new TextField("");
    	Button enterButton = new Button("Enter");
    	enterButton.setOnAction(e->updateStats());
    	enterButton.setDefaultButton(true);
    	Button clearButton = new Button("Clear");
    	clearButton.setOnAction(e->clearStats());
    	HBox entryPane = new HBox(entryBox,enterButton,clearButton);
    	
    	updateResults();
    	
        VBox root = new VBox( title, entryPane, nStat, sStat, mStat, sdStat);
        root.setPadding( new Insets( 8,3,8,3) );
        root.setStyle("-fx-border-color:black; -fx-border-width:2px");

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Stats Calc");
        stage.setResizable(false);
        stage.show();

        
    }  // end start();

    private void updateResults() {
    	int c = 0;
    	double s = 0;
    	double m = 0;
    	double sd = 0;
    	if (count>0) {
    		c = count;
    		s = sum;
    		m = getMean();
    		sd = getSD();
    	}
    	// Set labels to report values
    	nStat.setText("Number of entries: " + c);
    	sStat.setText("Sum: " + s);
    	mStat.setText("Average: " + m);
    	sdStat.setText("Standard Deviation: " + sd);
    	
    } // end updateResults
    
    private void updateStats() {
    	// get value in entry box
        try {
            String xStr = entryBox.getText();
            double x = Double.parseDouble(xStr);
            count++;
            sum+=x;
            squaredSum+=(x*x);
            updateResults();
        	entryBox.setText(""); // clear entryBox for new data point
        }
        catch (NumberFormatException e) {
        	// The string xStr is not a legal number.
        	// do nothing
            return;
        }
    } // end updateStats
    
    private void clearStats() {
    	// reset count & sum
    	// redraw stats text boxes
    	count = 0;
    	sum = 0;
    	entryBox.setText("");
    	updateResults();
    }
    
    
    private double getMean() {
    	return (sum/count);
    }
    
    private double getSD() {
    	double mean = getMean();
    	return Math.sqrt( ( (squaredSum)/count) - (mean*mean) );
    }
}
