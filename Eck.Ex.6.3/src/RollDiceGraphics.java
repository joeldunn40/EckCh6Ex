/** Eck Exercise 6.3
 * Roll Pair of Dice on click
 * Show dice on screen
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class RollDiceGraphics extends Application {

	private Canvas canvas;
    private GraphicsContext g;
	static final double dieWidth = 100; // die width
	static final double canvasWidth = 260; // die width

    public static void main(String[] args) {
        launch(args);
    } // end main
    
    public void start(Stage stage) {
    	
        canvas = new Canvas(canvasWidth,canvasWidth);
        Pane root = new Pane(canvas);
        Scene scene = new Scene( root );
        stage.setScene(scene);
        stage.setTitle("Click to Roll Dice");
        stage.setResizable(false);
        canvas.setOnMouseClicked( e -> mouseClicked(e) );
        
        // draw initial canvas
    	g = canvas.getGraphicsContext2D();
        g.setFill(Color.LIGHTBLUE);
        g.fillRect(0, 0, canvasWidth, canvasWidth);
        g.setStroke(Color.BLUE);
        g.strokeRect(0,0,canvasWidth, canvasWidth);
        
        // draw blank dice
        drawDie(g,0,20,20);
        drawDie(g,0,canvasWidth - dieWidth - 20,canvasWidth - dieWidth - 20);
        
        stage.show();  // make the window visible

    } // end start
    
	private void drawDie(GraphicsContext g, int val, double x, double y) {
		// Draw border
		// Fill face
		// locations for 9 black spots
		// Choose spots based on val
		double dieBorder = 10; // die border
		double spotDia = 20; // spot diameter
		double spotOffset = (dieWidth - 2*dieBorder - spotDia)/2; // distance between spot coords.

		// draw die with border
		g.setFill(Color.WHITE);
		g.fillRect(x, y, dieWidth, dieWidth);
		g.setStroke(Color.BLACK);
		g.strokeRect(x, y, dieWidth, dieWidth);
		
		// draw spots:
		// code for all 3 x 3 spots
		// chose spots to draw based on val
		g.setFill(Color.BLACK);
		
		if (val == 2  || val == 3  || val == 4  || val == 5  || val == 6 )
			g.fillOval(x + dieBorder + 0*spotOffset,y + dieBorder + 0*spotOffset,spotDia,spotDia); 		// 1,1
//		if (val == 6 ) // Column 2, Row 1 not used
//			g.fillOval(x + dieBorder + 1*spotOffset,y + dieBorder + 0*spotOffset,spotDia,spotDia); 		// 2,1
		if (val == 4  || val == 5  || val == 6 )
			g.fillOval(x + dieBorder + 2*spotOffset,y + dieBorder + 0*spotOffset,spotDia,spotDia); 		// 3,1
		if (val == 6 ) 
			g.fillOval(x + dieBorder + 0*spotOffset,y + dieBorder + 1*spotOffset,spotDia,spotDia); 		// 1,2
		if (val == 1 || val == 3  || val == 5)
			g.fillOval(x + dieBorder + 1*spotOffset,y + dieBorder + 1*spotOffset,spotDia,spotDia); 		// 2,2
		if (val == 6 ) 
			g.fillOval(x + dieBorder + 2*spotOffset,y + dieBorder + 1*spotOffset,spotDia,spotDia); 		// 3,2
		if (val == 4  || val == 5  || val == 6 )
			g.fillOval(x + dieBorder + 0*spotOffset,y + dieBorder + 2*spotOffset,spotDia,spotDia); 		// 1,3
//		if (val == 6 ) // Column 2, Row 3 not used
//			g.fillOval(x + dieBorder + 1*spotOffset,y + dieBorder + 2*spotOffset,spotDia,spotDia); 		// 2,3
		if (val == 2 || val == 3 || val == 4  || val == 5  || val == 6 )
			g.fillOval(x + dieBorder + 2*spotOffset,y + dieBorder + 2*spotOffset,spotDia,spotDia); 		// 3,3
	} // end drawDie
	
	private void mouseClicked(MouseEvent evt) {
		// Check if mouse within canvas
//		System.out.println("Mouse target: " + evt.getTarget());
        if (evt.getTarget() != canvas) 
        	return;
        
        int val1 = (int)(Math.random()*6) + 1;
        int val2 = (int)(Math.random()*6) + 1;
        drawDie(g,val1,20,20);
        drawDie(g,val2,canvasWidth - dieWidth - 20,canvasWidth - dieWidth - 20);
//		System.out.println("Dice value: " + val1 + " " + val2);
        
	}// end mouseClicked
	
	
} // end RollDiceGraphics
