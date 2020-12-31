/** Eck Exercise 6.1
 * Program allows user to add rectangles of fixed size to a painting canvas using the mouse.
 * Left click = red rectangle
 * Shift + Left Click = blue rectangle
 * Right click = clear canvas
 * Click-Drag draws multiple rectangles following mouse
 * 
 * @author jd07
 *
 */
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class RectanglePaint extends Application {
    private double prevX, prevY;   // The previous location of the mouse, when
    // the user is drawing by dragging the mouse.
     private boolean dragging;   // This is set to true while the user is drawing.
    private Canvas canvas;  // The canvas on which everything is drawn.
    private GraphicsContext g;  // For drawing on the canvas.

    /**
     * This main routine allows this class to be run as a program.
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    /** Copied from Eck
     * The start() method creates the GUI, sets up event listening, and
     * shows the window on the screen.
     */
    public void start(Stage stage) {
        
        /* Create the canvans and draw its content for the first time. */
        
        canvas = new Canvas(600,400);
        g = canvas.getGraphicsContext2D();
        clearCanvas();
        
        /* Respond to mouse events on the canvas, by calling methods in this class. */
//        
        canvas.setOnMousePressed( e -> mousePressed(e) );
        canvas.setOnMouseDragged( e -> mouseDragged(e) );
        canvas.setOnMouseReleased( e -> mouseReleased(e) ); 
        
        BorderPane root = new BorderPane(canvas);
        root.setStyle("-fx-border-color: black; -fx-border-width: 2px");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setTitle("Rectangle Paint");
        stage.show();
    }

    // Clear canvas: create white rectangle
    private void clearCanvas() {
        int width = (int)canvas.getWidth();    // Width of the canvas.
        int height = (int)canvas.getHeight();  // Height of the canvas.

        g.setFill(Color.WHITE);
        g.fillRect(0,0,width,height);
    } // end clear canvas
    
    // Mouse pressed event response
    private void mousePressed(MouseEvent evt) {
    	if (dragging)
    		return; // if dragging ignore (go to mouseDragged)
    	dragging = true; // start dragging flag
    	if ( (evt.getButton() == MouseButton.PRIMARY) && 
    			!( (evt.getButton() == MouseButton.SECONDARY) || (evt.getButton() == MouseButton.MIDDLE) ) ) {
    		if ( evt.isShiftDown() ) {
    			drawShape1(evt.getX(),evt.getY());
    		}
    		else {
    			drawShape2(evt.getX(),evt.getY());
    			}
   			prevX = evt.getX();
   			prevY = evt.getY();   		 
    	} else if ( (evt.getButton() == MouseButton.SECONDARY) && 
    			!( (evt.getButton() == MouseButton.PRIMARY) || (evt.getButton() == MouseButton.MIDDLE) ) ) {
    		clearCanvas();
    	}
    	
	} // end mousePressed
    
    private void mouseDragged(MouseEvent evt) {
    	if (dragging == false)
    		return;
    	
    	// Only continue if more than 5 pixels moved
    	if ( (Math.abs(prevX - evt.getX()) < 5 ) || (Math.abs(prevY - evt.getY()) < 5 ) ) 
    			return; 
    	
    	if ( evt.isShiftDown() ) {
    		drawShape1(evt.getX(),evt.getY());
    	}
    	else {
    		drawShape2(evt.getX(),evt.getY());
    	}
    	prevX = evt.getX();
    	prevY = evt.getY();   		 
	} // end mousePressed
    /**
     * Called whenever the user releases the mouse button. Just sets
     * dragging to false.
     */
    public void mouseReleased(MouseEvent evt) {
        dragging = false; //end dragging flag
    } // end mouseReleased

    // Draw shape 1 at coords
    private void drawShape1(double x,double y) {
		g.setFill( Color.BLUE );
		g.fillRect( x - 30, y - 15, 60, 30 );
		g.setStroke( Color.BLACK);
		g.strokeRect( x - 30, y - 15, 60, 30 );
    	
    }

    // Draw shape 1 at coords
    private void drawShape2(double x, double y) {
		g.setFill( Color.RED );
		g.fillRect( x- 30, y - 15, 60, 30 );
		g.setStroke( Color.BLACK);
		g.strokeRect( x - 30, y - 15, 60, 30 );
    	
    }
    
}
