/** Eck Exercise 6.9
 * User draws a polygon
 * Mouse clicks create vertices
 * Line segments are drawn as vertices created
 * When mouse click near origin: polygon redrawn with black border & red fill
 * Next click clears & resets canvas
 * @author jd07
 *
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
//import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class PolygonDraw extends Application {
    /**
     * This main routine allows this class to be run as a program.
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    private Canvas canvas;
    private GraphicsContext g;
    private int maxCoords = 1000;
    private int vCount = 0; // vertex count
    private boolean isComplete = false; // flag if polygon complete
    private double[] xCoords = new double[maxCoords];
    private double[] yCoords = new double[maxCoords];
    
    public void start(Stage stage) {
        
        /* Create the canvans and draw its content for the first time. */
        
        canvas = new Canvas(600,400);
        g = canvas.getGraphicsContext2D();
        clearCanvas();
        
        /* Respond to mouse events on the canvas, by calling methods in this class. */
        canvas.setOnMouseClicked(e -> mouseClicked(e));
        
        // create border
        BorderPane root = new BorderPane(canvas);
        root.setStyle("-fx-border-color: black; -fx-border-width: 2px");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setTitle("Draw Polygon");
        stage.show();
    } // end start

    // Clear canvas: create white rectangle
    private void clearCanvas() {
        int width = (int)canvas.getWidth();    // Width of the canvas.
        int height = (int)canvas.getHeight();  // Height of the canvas.

        g.setFill(Color.WHITE);
        g.fillRect(0,0,width,height);
    } // end clear canvas

    // Mouse click method
    private void mouseClicked(MouseEvent evt) {
    	/* Options on click
    	 * if isComplete
    			Reset canvas
    			Reset x & y Coords
    			Reset vCount
    			Reset isComplete
    		else if ( mouse coords not close to x/yCoord[0] )
    			Get mouse coords & store in xCoord[vCount] & yCoord[vCount] 
    		 	vCount++;
    		 	if vCount>0
    		 		stroke line between x/yCoord[vCount-1] & x/yCoord[vCount]
    		else 
    			Complete polygon:
    				isComplete = true
    				draw polygon: stroke & fill
    		end
    		*/
    	// Current Mouse Coords on Click
    	double x=evt.getX();
    	double y=evt.getY();
    	boolean isCloseToOrigin = false;
    	
    	// Check if close to origin (within 5 points)
    	if (vCount > 0 ) {
    		if ( (Math.abs(x-xCoords[0]) < 5) && (Math.abs(y-yCoords[0]) < 5)  )
    			isCloseToOrigin = true;
    	} // end if
    	
//    	// For Debugging:
//    	System.out.println("V: " + vCount + "  X: " + x + "  Y: " + y);
    	
    	
    	// Main algorithm for creating vertices, drawing lines or polygons or resetting the program
    	if (isComplete) {
    		clearCanvas();
    		vCount = 0;
    		isComplete = false;
    	} else if ( !isCloseToOrigin ) {
    		// Add new point to vertices coordinates array
    		xCoords[vCount] = x;
    		yCoords[vCount] = y;
    		// Draw Vertex point (circle, dia. 4)
    		g.setFill(Color.BLACK);
    		g.fillOval(x-2, y-2, 4, 4);
    		// Now draw the line 
    		if (vCount>0) {
    			g.setStroke(Color.BLUE);
    			g.strokeLine(xCoords[vCount-1], yCoords[vCount-1], xCoords[vCount], yCoords[vCount]);
    		} // end if draw
    		vCount++;
    	} else {
    		clearCanvas(); // draw on fresh canvas
    		isComplete = true; // set flag for next click
    		g.setStroke(Color.BLACK);
    		g.strokePolygon(xCoords, yCoords, vCount);
    		g.setFill(Color.RED);
    		g.fillPolygon(xCoords, yCoords, vCount);
    		
    	} // end if
    	
    } // end mouseClicked
    
    
} // end PolygonDraw
