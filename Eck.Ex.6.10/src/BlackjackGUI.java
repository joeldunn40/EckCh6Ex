/** Eck Exercise 6.10
 * GUI to play Blackjack game
 * Uses text based BlackjackVsDealer & HighLowGUI as template.
 * 
 */

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class BlackjackGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    //---------------------------------------------------------------------

    private Canvas board;     // The canvas on which cards and message are drawn.
    private Image cardImages; // A single image contains the images of every card.

    private Deck deck;      // A deck of cards to be used in the game.
    private BlackjackHand playersHand;      // Player's hand
    private BlackjackHand dealersHand;      // Dealer's hand
    private String message; // A message drawn on the canvas, which changes
                            //    to reflect the state of the game.

    private boolean gameInProgress = true;  // Set to true when a game begins and to false
                                     //   when the game ends.
    
    private int gamesPlayed = 0; // count of games played
    private int gamesPlayerWon = 0; // count of games the player won.
    private boolean isDealersTurn = false;
    
    /**
     * The start() method sets up the GUI and event handling. The root pane is
     * is a BorderPane. A canvas where cards are displayed occupies the center 
     * position of the BorderPane.  On the bottom is a HBox that holds three buttons.  
     * ActionEvent handlers are set up to call methods defined elsewhere
     * in this class when the user clicks a button.
     */
    public void start(Stage stage) {

        cardImages = new Image("cards.png");

        board = new Canvas(5*99 + 20, 123*2 + 80); 
                         // space for 5 cards, 2 rows, with 20-pixel border
                         // and space on the bottom for a message
        deck = new Deck();
       
        
        Button hit = new Button("Hit");
        hit.setOnAction( e -> doHit() );
        Button stand = new Button("Stand");
        stand.setOnAction( e -> doStand() );
        Button newGame = new Button("New Game");
        newGame.setOnAction( e -> doNewGame() );

        HBox buttonBar = new HBox( hit, stand, newGame );
        
        /* Improve the layout of the buttonBar. Without adjustment
         * the buttons will be grouped at the left end of the
         * HBox.  My solution is to set their preferred widths
         * to make them all the same size and make them fill the
         * entire HBox.  */
        
        hit.setPrefWidth(board.getWidth()/3.0);
        stand.setPrefWidth(board.getWidth()/3.0);
        newGame.setPrefWidth(board.getWidth()/3.0);
        
        BorderPane root = new BorderPane();
        root.setCenter(board);
        root.setBottom(buttonBar);

        doNewGame();  // set up for the first game

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Blackjack Game");
        stage.setResizable(false);
        stage.show();
        
    }  // end start()
    
    private void doNewGame() {
    	// deal 2 cards to player & dealer
    	// dealer hand face down
    	// player hand face up
    	
    	// shuffle cards - perhaps create new rule later
    	deck.shuffle();

    	// declare new hands
    	playersHand = new BlackjackHand();      // Player's hand
    	dealersHand = new BlackjackHand();      // Dealer's hand

    	// deal two cards to player & dealer
		playersHand.addCard(deck.dealCard());
		playersHand.addCard(deck.dealCard());
		dealersHand.addCard(deck.dealCard());
		dealersHand.addCard(deck.dealCard());
		
		message = "Player's go.";
		isDealersTurn = false;
		gameInProgress = true;
		gamesPlayed++;
		
		drawBoard();
    	} // end doNewGame
    
    private void doStand() {
    	// pass to dealer's go
    	if (gameInProgress == false)
    		return; 
    	isDealersTurn = true;
    	// Won't get seen!
    	message = String.format("Player Stands with score of %d: Dealer's go.",playersHand.getBlackjackValue());
    	drawBoard();
    	//
    	dealersTurn();
    } // end doStand
    
    private void doHit() {
    	if (playersHand.getCardCount() >= 5) {
    		throw new IllegalStateException("Should not be able to call a HIT with 5 cards in hand.");
    	}
    	if (gameInProgress == false)
    		return; 
    	// add card - assume will never go over 5 cards?? can't happen??
    	playersHand.addCard(deck.dealCard());
    	message = String.format("Player has %d points. Player's go.", playersHand.getBlackjackValue());
    	
    	if (playersHand.getBlackjackValue() > 21) {
    		// player loses - end game
    		message = String.format("Player went bust with %d points. Dealer wins.", playersHand.getBlackjackValue());
    		gameInProgress = false;
    	} else if (playersHand.getCardCount() == 5) {
    		// player wins - end game
    		message = String.format("Player Wins with 5 cards.");
    		gameInProgress = false;
    		gamesPlayerWon++;
    	} // end if

    	drawBoard();    	
    } //end doHit

    // Algorithmic play for dealer
    private void dealersTurn() {
    	if (!isDealersTurn) {
    		throw new IllegalStateException("Should not be able to call dealers turn without isDealersTurn = true.");
    	}
    	while(gameInProgress) {
    		
    		if (dealersHand.getCardCount() == 5 ) {
    			gameInProgress = false;
    			message =  String.format("Dealer wins with 5 cards."); // will be overridden in statement below if > 21
    		}
    		
    		if (dealersHand.getBlackjackValue() >= playersHand.getBlackjackValue()) {
    			// dealer wins - end game
    			gameInProgress = false;
        		message = String.format("Dealer wins with %d points to player's %d.", dealersHand.getBlackjackValue(), playersHand.getBlackjackValue());
        	}
    		
    		if (dealersHand.getBlackjackValue() > 21 ) {
    			// dealer went bust - player wins - end game
    			gameInProgress = false;
        		message = String.format("Dealer went bust with %d points. Player wins.", dealersHand.getBlackjackValue());
        		gamesPlayerWon++;
    		} // end if
    		
    		if (gameInProgress) {
    			dealersHand.addCard(deck.dealCard());
    		}
    	} // end while
    	drawBoard();	
    } // end dealersTurn
    
    
    /**
     * This method draws the message at the bottom of the
     * canvas, and it draws all of the dealt cards spread out
     * across the canvas.  If the game is in progress, an extra
     * card is drawn face down representing the card to be dealt next.
     */
    private void drawBoard() {
        GraphicsContext g = board.getGraphicsContext2D();
        g.setFill( Color.DARKGREEN);
        g.fillRect(0,0,board.getWidth(),board.getHeight());
        g.setFill( Color.rgb(220,255,220) );
        g.setFont( Font.font(16) );
        g.fillText(message,20,280);
                
        // draw players hand        
        for (int i = 0; i < playersHand.getCardCount(); i++) {
        	drawCard(g, playersHand.getCard(i), 20 + i * 99, 20);
        } // end for

        // draw dealers hand
        if (isDealersTurn) {
        	for (int i = 0; i < dealersHand.getCardCount(); i++) {
        		drawCard(g, dealersHand.getCard(i), 20 + i * 99, 123 + 20);
        	}
        }  else {
        	for (int i = 0; i < 2; i++) {
        		drawCard(g, null, 20 + i * 99, 123 + 20);
        	}
        } // end if isDealersTurn
    } 


    /**
     * Draws a card with top-left corner at (x,y).  If card is null,
     * then a face-down card is drawn.  The card images are from 
     * the file cards.png; this program will fail without it.
     */
    private void drawCard(GraphicsContext g, Card card, int x, int y) {
        int cardRow, cardCol;
        if (card == null) {  
            cardRow = 4;   // row and column of a face down card
            cardCol = 2;
        }
        else {
            cardRow = 3 - card.getSuit();
            cardCol = card.getValue() - 1;
        }
        double sx,sy;  // top left corner of source rect for card in cardImages
        sx = 79 * cardCol;
        sy = 123 * cardRow;
        g.drawImage( cardImages, sx,sy,79,123, x,y,79,123 );
    } // end drawCard()
}
