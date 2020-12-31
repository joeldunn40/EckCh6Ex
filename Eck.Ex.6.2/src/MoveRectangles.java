/** Eck Exercise 6.2
 * Allow to use mouse to drag rectangles around
 * Moving outside the window will make the rectangle disappear
 * Escape key brings them back
 * @author jd07
 *
 */

import javafx.application.Application;
import javafx.scene.Scene;
//import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
//import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

public class MoveRectangles extends Application {
	/** Clear canvas
	/* Draw Rectangles starting position
	/*  On mouse click
	/*		if within a rectangle
	 * 			set currentRectangle prev coords
	 * 	On dragging 
	 * 			set currentRectangle		
	 * 			change new coords
	 * 			redraw with keep other rectangle original coords
	 * If mouse position outside canvas: don't draw currentRectangle
	 * On key press Escape: reset canvas
	 * 			  
	 */
	private Canvas canvas;
    private GraphicsContext g;
    private Square sq1;
    private Square sq2;
    private boolean dragging;
    private int selectedSquare;
    
    // Original square coords
    static final double x1 = 200; 
    static final double y1 = 100; 
    static final double x2 = 300; 
    static final double y2 = 100; 
	
    public static void main(String[] args) {
        launch(args);
    }
    
    public void start(Stage stage) {
               
        /* Create the canvas, and set up the GUI */
        
        canvas = new Canvas(550,400);
        Pane root = new Pane(canvas);
        Scene scene = new Scene( root );
        stage.setScene(scene);
        stage.setTitle("Move Squares");
        stage.setResizable(false);
        
        /* Set up canvas */
        // Create & draw squares
        sq1 = new Square();
        sq2 = new Square();
        resetCanvas();
                
        /* Install event handlers for common mouse events on the canvas.
         * I could have used a single event handler on the canvas, but this
         * shows how to handle the individual types of event.  The response
         * in each case is simply to call mouseEventOnCanvas() */
        
        canvas.setOnMousePressed( e -> mousePressed(e) );
        canvas.setOnMouseReleased( e -> mouseReleased(e) );
        canvas.setOnMouseDragged( e -> mouseDragged(e) );
        canvas.setOnMouseExited( e -> mouseExited(e) );
        scene.setOnKeyPressed( e -> keyPressed(e) );
        stage.show();  // make the window visible
        
    } // end start()
    
    // Key pressed event
    private void keyPressed(KeyEvent evt) {
        KeyCode key = evt.getCode();  // keyboard code for the pressed key
    	if (key == KeyCode.ESCAPE) {
            /* Reset canvas & squares */
            resetCanvas();
            System.out.println("Canvas Reset");
    	}
    }

    
    // Mouse pressed event response
    private void mousePressed(MouseEvent evt) {
    	if (dragging)
    		return;
    	
    	dragging = true; // set dragging flag
    	// select square to move if within region of square
    	// Mouse position on click
    	double x = evt.getX();
    	double y = evt.getY();
    	// Square positions
    	double sq1x = sq1.getX();
    	double sq1y = sq1.getY();
    	double sq2x = sq2.getX();
    	double sq2y = sq2.getY();
    	
    	// If coords within Square 1 border
    	if ( (x > (sq1x-15)) && (x < (sq1x+15)) && (y > (sq1y-15)) && (y < (sq1y+15)) ) {
    		selectedSquare = 1;    // square 1 selected
    		System.out.println("Selected Square 1");
    	} else if ( (x > (sq2x-15)) && (x < (sq2x+15)) && (y > (sq2y-15)) && (y < (sq2y+15)) ){
    		selectedSquare = 2;    	// square 2 selected
    		System.out.println("Selected Square 2");
    	} else {
    		selectedSquare = 0; // dummy value when no square selected
    		System.out.println("No square selected");
    	}
    	System.out.println("Mouse coords: X:" + x + " Y: " + y);
    } //end mousePressed
    
    private void mouseDragged(MouseEvent evt) {
    	if (dragging == false) 
    		return;
    	
    	// Mouse coordinates
    	double x = evt.getX();
    	double y = evt.getY();
    	// redraw selected square
    	clearCanvas();
    	if (selectedSquare == 1) {
    		// set new coords for square 1 
    		sq1.setX(x);
    		sq1.setY(y);
    	} else if (selectedSquare == 2) {
    		// set new coords for square 2 
    		sq2.setX(x);
    		sq2.setY(y);
    	}
    	// draw both squares in their (new) positions
		sq1.draw(g);
		sq2.draw(g);
//		System.out.println("Mouse dragging: Coords X: " + x + " Y: " + y);
    } // end mouseDragged
    
    
    private void mouseReleased(MouseEvent evt) {
    	dragging = false;
    } // end mouseReleased
    
    private void mouseExited(MouseEvent evt) {
    	if (dragging == false)
    		return; // only continue if dragging
    	
    	// redraw without selected square
    	clearCanvas();
    	if (selectedSquare == 1) {
    		sq2.draw(g);
    	} else 
    		sq1.draw(g);
    	System.out.println("Mouse exited while dragging");
    } // end mouseExited

    
    static class Square {
    	private double x, y;
    	private Color color;
    	
    	public double getX() {
    		return x;
    	}
    	public double getY() {
    		return y;
    	}
    	public void setX(double x) {
    		this.x = x;
    	}
    	public void setY(double y) {
    		this.y = y;
    	}
    	public void setColor(Color c) {
    		this.color = c;    		
    	}
    	public void draw(GraphicsContext g) {
    		g.setStroke( Color.BLACK);
    		g.strokeRect( this.x - 15, this.y - 15, 30, 30 );
    		g.setFill( this.color);
    		g.fillRect( this.x - 15, this.y - 15, 30, 30 );
    	}
    } // end square class
    
    private void clearCanvas() {
    	// Create cleared canvas
    	g = canvas.getGraphicsContext2D();
        g.setFill(Color.WHITE);
        g.fillRect(0,0,550,400);
    }

    // Clear canvas and add reset squares
    private void resetCanvas() {
    	clearCanvas();
        sq1.setX(x1);
        sq1.setY(y1);
        sq1.setColor(Color.BLUE);
        sq1.draw(g);
        
        sq2.setX(x2);
        sq2.setY(y2);
        sq2.setColor(Color.RED);
        sq2.draw(g);
    }
}
