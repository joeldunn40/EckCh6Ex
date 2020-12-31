/** Eck Exercise 6.5
 * Draw checkboard
 * Allow user to select (& unselect square)
 * Copied drawPicture from DrawCheckerboard (Ex 3.8)
 * 
 */
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class CheckboardSelect extends Application {
	private GraphicsContext g;
	private int prevX = -1;
	private int prevY = -1;
    static final int width = 400;   // The width of the image.  You can modify this value!
    static final int height = 400;  // The height of the image. You can modify this value!
	
    public void start(Stage stage) {
        Canvas canvas = new Canvas(width,height);
        canvas.setOnMouseClicked(e -> mouseClicked(e) );
        g = canvas.getGraphicsContext2D();
        drawBoard();
        BorderPane root = new BorderPane(canvas);
        root.setStyle("-fx-border-width: 4px; -fx-border-color: #444");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Simple Graphics"); // STRING APPEARS IN WINDOW TITLEBAR!
        stage.show();
        stage.setResizable(false);
    } 

    public static void main(String[] args) {
        launch(args);
    }

    private void mouseClicked(MouseEvent evt) {
    	double x = evt.getX();
    	double y = evt.getY();
    	
    	// round x & y to nearest top-left square corner
    	int rx = (int)(x/50) * 50;
    	int ry = (int)(y/50) * 50;
    	// redraw checkerboard
    	drawBoard();
    	
    	// decide whether already selected
    	if ( (rx == prevX) && (ry == prevY) ) {
    		// do nothing - redrawn board will 'remove' selection
    	} else {
    		g.setStroke(Color.LIGHTBLUE);
    		g.setLineWidth(5.0);
    		g.strokeRect(rx, ry, 50, 50);
    	} // end if (already selected)
    	prevX = rx;
    	prevY = ry;
    }
    
    
    private void drawBoard() {

    	double x, y; // top left coord of each square
    	int i, j; // loop counters for squares
    	x = y = 0.0; // initialize x & y positions
    	for (i = 0; i < 8; i++ ) {
    		x = i*50;
    		for (j = 0; j < 8; j++) {
    			y = j*50;
    			// Red or Black (odd or even sum)
    			if ( (i+j)%2 == 0 ) {
    				g.setFill(Color.BLACK);    				
    			} else 
    				g.setFill(Color.RED);
    			g.fillRect(x, y, 50, 50);
    		}
    	}
    	
    } // end drawPicture()
}
