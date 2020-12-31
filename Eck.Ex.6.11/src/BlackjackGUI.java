/** Eck Exercise 6.11
 * Like 6.10 but with betting, disabled buttons, textfield and improved messaging
 * GUI to play Blackjack game
 * Uses text based BlackjackVsDealer & HighLowGUI as template.
 * 
 */

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class BlackjackGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    //---------------------------------------------------------------------
    
    // Graphics
    private Canvas board;     // The canvas on which cards and message are drawn.
    private GraphicsContext g;
    private Image cardImages; // A single image contains the images of every card.

    // Card game objects
    private Deck deck;      // A deck of cards to be used in the game.
    private BlackjackHand playersHand;      // Player's hand
    private BlackjackHand dealersHand;      // Dealer's hand
    
    // GUI messages
    private String message; // A message drawn on the canvas, which changes
                            //    to reflect the state of the game.

    // controllers for game operation
    private boolean isDealersTurn = false;
    private boolean gameInProgress = false;  // Set to true when a game begins and to false
                                     //   when the game ends.

    // uicontrols
    private Button hit;
    private Button stand;
    private Button newGame;
    private TextField inputBet;
    
    // betting values
    private int bet = 0;
    private int pot = 100;
    
    /* Betting:
     * See cards: Bet: Play
     * 	New game: get bet before players first choice? disable hit/stand?
     * 			Default bet = 0: must be >= 1
     * Update pot based on win/lose
     * On Bet: check amount is valid: ask to reinput if bet > pot
     * 		On win/lose: check if pot empty -> end game (disable all uicontrols).
     */
    
    
    /**
     * The start() method sets up the GUI and event handling. The root pane is
     * is a BorderPane. A canvas where cards are displayed occupies the center 
     * position of the BorderPane.  On the bottom is a HBox that holds three buttons.  
     * ActionEvent handlers are set up to call methods defined elsewhere
     * in this class when the user clicks a button.
     */
    public void start(Stage stage) {

        cardImages = new Image("cards.png");

        board = new Canvas(5*99 + 20, 123*2 + 140); 
                         // space for 5 cards, 2 rows, with 20-pixel border
                         // and space on the bottom for a message
        g = board.getGraphicsContext2D();

        deck = new Deck();
       
        
        hit = new Button("Hit");
        hit.setOnAction( e -> doHit() );
        stand = new Button("Stand");
        stand.setOnAction( e -> doStand() );
        newGame = new Button("New Game");
        newGame.setOnAction( e -> doNewGame() );
        Label betLabel = new Label("          Your bet:");
        inputBet = new TextField();
        inputBet.setPrefWidth(100);
        inputBet.setOnKeyPressed(e-> getBet(e));

        HBox buttonBar = new HBox(10, hit, stand, newGame, betLabel, inputBet );
        buttonBar.setPrefWidth(board.getWidth());
        buttonBar.setAlignment(Pos.CENTER);
        
        /* Improve the layout of the buttonBar. Without adjustment
         * the buttons will be grouped at the left end of the
         * HBox.  My solution is to set their preferred widths
         * to make them all the same size and make them fill the
         * entire HBox.  */
        
//        hit.setPrefWidth(board.getWidth()/3.0);
//        stand.setPrefWidth(board.getWidth()/3.0);
//        newGame.setPrefWidth(board.getWidth()/3.0);
        
        BorderPane root = new BorderPane();
        root.setCenter(board);
        root.setBottom(buttonBar);

        message = "Welcome to Blackjack! Click \"New Game\" to start";
        setGameInProgress(false);
        drawBlankBoard();
        		
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Blackjack Game");
        stage.setResizable(false);
        stage.show();
        
    }  // end start()

    private void getBet(KeyEvent evt) {
    	if (evt.getCode() == KeyCode.ENTER) {
    		String betStr = inputBet.getText();
    		boolean cont = checkValidBet(betStr);
    		if (cont) {
    			setGameInProgress(true);
    			message = "Player's turn";
    			drawBoard();
    		}
    	}
    }
    
    private boolean checkValidBet(String betStr) {

    	try {
    		bet = Integer.parseInt(betStr);
    		if (pot <= 0 ) {
    			message = "You can't afford it buddy! See ya!";
    			endGame();
    			return false; // shouldn't get this far?...
    		}
    		if ((pot < bet) && (pot > 0)) {
    			message = "You don't have enough in the pot! Try again...";
        		drawBlankBoard();
        		inputBet.requestFocus();
        		return false;
    		}
			return true;
    	} catch (IllegalArgumentException e) {
    		message = "Enter a valid number (integer)";
    		drawBlankBoard();
    		inputBet.requestFocus();
    		return false;
    	}
    	
    } // end checkValidBet
    
    private void doNewGame() {
    	// deal 2 cards to player & dealer
    	// dealer hand face down
    	// player hand face up

    	// on new game:
    	// ask for a bet (disable newGame)
    	// check bet is valid in a loop on each enter press in textfield
    	
    	newGame.setDisable(true);
    	// This can only happen once valid bet placed so game not in progress yet
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
		
		isDealersTurn = false;
		
		message = "Enter bet";
		drawBlankBoard(); 
    	} // end doNewGame
    
    private void doStand() {
    	// pass to dealer's go
    	if (gameInProgress == false)
    		return; 
    	isDealersTurn = true;
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
    		pot -= bet;
    		setGameInProgress(false);
    	} else if (playersHand.getCardCount() == 5) {
    		// player wins - end game
    		message = String.format("Player Wins with 5 cards.");
    		pot += bet;
    		setGameInProgress(false);
    	} // end if

    	drawBoard();    	
    } //end doHit

    // Algorithmic play for dealer
    private void dealersTurn() {
    	if (!isDealersTurn) {
    		throw new IllegalStateException("Should not be able to call dealers turn without isDealersTurn = true.");
    	}
    	
    	while(gameInProgress) {
    		if 	(dealersHand.getBlackjackValue() <= 21 ) { // Dealer can win
    			
    			if (dealersHand.getCardCount() == 5 ) {
    				message =  String.format("Dealer wins with 5 cards."); // will be overridden in statement below if > 21    				
    				// dealer has won - update pot & change game state
    				pot -= bet; 
    				setGameInProgress(false);
    			}

    			if (dealersHand.getBlackjackValue() >= playersHand.getBlackjackValue()) {
    				message = String.format("Dealer wins with %d points to player's %d.", dealersHand.getBlackjackValue(), playersHand.getBlackjackValue());
    				// dealer has won - update pot & change game state
    				pot -= bet;
    				setGameInProgress(false);
    			}
    			// game is still in progress if Dealer hasn't yet won
				
    		} else if (dealersHand.getBlackjackValue() > 21 ) { // Dealer has lost
        		message = String.format("Dealer went bust with %d points. Player wins.", dealersHand.getBlackjackValue());
				// dealer has lost - update pot & change game state
        		pot += bet;
    			setGameInProgress(false);
    			
    		} // end if
    		
			// game is still in progress if Dealer hasn't yet won or lost
    		if (gameInProgress) {
    			dealersHand.addCard(deck.dealCard());
    		}
    	} // end while
    	drawBoard();	
    } // end dealersTurn
    
    private void setGameInProgress(boolean x) {
    	gameInProgress = x;
    	if (gameInProgress) {
    		newGame.setDisable(true);
    		hit.setDisable(false);
    		stand.setDisable(false);
    	} else { 
    		newGame.setDisable(false);
    		hit.setDisable(true);
    		stand.setDisable(true);
    	} // end if
    } // end setGameInProgress
    
    // Make further games impossible
    private void endGame() {
    	drawBlankBoard();
    	hit.setDisable(true);
    	stand.setDisable(true);
    	newGame.setDisable(true);
    	inputBet.setEditable(false);
    } // end endGame
    
    /**
     * This method draws the message at the bottom of the
     * canvas, and it draws all of the dealt cards spread out
     * across the canvas.  If the game is in progress, an extra
     * card is drawn face down representing the card to be dealt next.
     */
    private void drawBoard() {
        drawBlankBoard();
        
        // draw players hand - lower row
        g.fillText("Player's hand", 20, 123+20+30);
        for (int i = 0; i < playersHand.getCardCount(); i++) {
        	drawCard(g, playersHand.getCard(i), 20 + i * 99, 123 + 20 + 40);
        } // end for

        // draw dealers hand - upper row
        g.fillText("Dealer's hand", 20, 20);
        if (isDealersTurn) {
        	for (int i = 0; i < dealersHand.getCardCount(); i++) {
        		drawCard(g, dealersHand.getCard(i), 20 + i * 99,  20 + 10);
        	}
        }  else {
        	for (int i = 0; i < 2; i++) {
        		drawCard(g, null, 20 + i * 99,  20 + 10);
        	}
        } // end if isDealersTurn
    } 

    // Draw board with no cards dealt - used for welcome screen
    private void drawBlankBoard() {
        g.setFill( Color.DARKGREEN);
        g.fillRect(0,0,board.getWidth(),board.getHeight());
        g.setFill( Color.rgb(220,255,220) );
        g.setFont( Font.font(16) );
        g.fillText(message,20,330);
        g.fillText(String.format("You have $%d", pot), 20, 350);
    } // end drawBlankBoard

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
