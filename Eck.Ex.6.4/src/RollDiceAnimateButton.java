/** Eck Exercise 6.4
 * As RollDiceGraphics but animate roll and have Roll button
 * Set borderpane so canvas is only for dice, & bottom pane for button
 * Replace mouse click with button click
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
//import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;


public class RollDiceAnimateButton extends Application {
	private Canvas canvas;
	private GraphicsContext g;
	static final double dieWidth = 100; // die width
	static final double canvasWidth = 260; // die width
	private int frameNumber;
	private Button rollButton;
	// declare global animation timer 
	private AnimationTimer timer = new AnimationTimer() {
		public void handle( long time ) {
			int val1 = (int)(Math.random()*6) + 1;
			int val2 = (int)(Math.random()*6) + 1;
			if ((frameNumber % 10) == 0) { // redraw every 10th roll
				drawDie(g,val1,20,20);
				drawDie(g,val2,canvasWidth - dieWidth - 20,canvasWidth - dieWidth - 20);
			}
			frameNumber++;
			if (frameNumber == 60) {
				timer.stop();
				rollButton.setDisable(false);
			}; // end if frameNumber
		}
	}; // end of timer declaration

	public static void main(String[] args) {
		launch(args);
	} // end main

	public void start(Stage stage) {

		canvas = new Canvas(canvasWidth,canvasWidth);

		rollButton = new Button("Roll");
		rollButton.setOnAction( e -> roll() );
		rollButton.setPrefWidth(canvasWidth);

		//        HBox buttonBar = new HBox( rollButton );

		BorderPane root = new BorderPane();
		root.setCenter(canvas);
		root.setBottom(rollButton);


		// draw initial canvas
		g = canvas.getGraphicsContext2D();
		g.setFill(Color.LIGHTBLUE);
		g.fillRect(0, 0, canvasWidth, canvasWidth);
		g.setStroke(Color.BLUE);
		g.strokeRect(0,0,canvasWidth, canvasWidth);

		// draw blank dice
		drawDie(g,0,20,20);
		drawDie(g,0,canvasWidth - dieWidth - 20,canvasWidth - dieWidth - 20);

		//      Pane root = new Pane(canvas);
		Scene scene = new Scene( root );
		stage.setScene(scene);
		stage.setTitle("Roll Dice");
		stage.setResizable(false);

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

	private void roll() {
		frameNumber = 0;
		rollButton.setDisable(true);
		timer.start();
	}// end roll
	
} // end RollDiceAnimateButton
