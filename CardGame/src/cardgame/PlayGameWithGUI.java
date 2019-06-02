package cardgame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class PlayGameWithGUI {
	public final static int NPLAYERS = 4;     // Total number of players including computer.  
	public final static int NPLAYER_GAME = 2; // How many players will be humans?  
	public final static int NCARDS = 52;	  // Total number of cards used in game.	
	public static final int CARDSperPLAYER = NCARDS/NPLAYERS; //Number of Cards given to per player..
	public final static int NSHUFFLE = 600;  // Number of times deck of cards will be shuffled.
	
	/*DECLARING NECESSARY GUI ELEMENTS..
	 * 
	 * */
	private JFrame frame; //MAIN FRAME WHERE COMPONENTS Lie.
	//*********LABELS**********
	private JLabel lblWinner; //Label shown for notifying which player is winner of particular round..
	private JLabel lblChoosedCard; //Label shown for which card player has chose.
	private JLabel[] labelsOfPlayers; //Labels for showing names of players on the top.
	private JLabel labelRoundNumber; //Label for round number of game.
	
	//**********BUTTON**********
	private JButton playButton; //Button for playing your move and giving turn to other player.
	//*********PANEL************
	private JPanel[] panels; // There will be separate panel for each player. Number of panels depend on NPLAYERS.
	//*********TABLE***********
	private JTable[] tables; // Separate table showing each player's cards.
	private JScrollPane[] scrollPanes; //Scroll Panels to make tables scrollable.
	//****************************************END DECLARING GUI ELEMENTS/COMPONENTS*************************************
	
	private Card[] gameHand; //Which card each player played in particular round. This array saves that.
	//gameHand length is equal to number of players.
	private Player[] players; //Saves data like name and cards of each player.
	private Match match; //Gives functionality in playing game.
	private int turn; //In playing game whose turn is it? 0 for player1 and 3 for player4
	
	 
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() //Helps in launching GUI app.
		{ 
			public void run() {
				try {
					
					Deck deck = new Deck(); //First initializing deck of 52 cards.
			        deck.shuffle(NSHUFFLE); //Shuffling the deck for NSHUFFLE times.
			        
			        Player[] players = new Player[NPLAYERS]; //Initializing players.
			        for (int i=0; i<NPLAYER_GAME; i++) { //getting data like name from user input for each player.
			        	String name;
			        	do 
			        	{
							name = JOptionPane.showInputDialog("Name of Player" + (i+1) + ": ");
						}
						while(name.equals("")); //If name is empty then again prompt user to write player's name;
			        	
						players[i] = new Player(name);
					}
			        
			        int j = 1; //Which computer number?
					for (int i=NPLAYER_GAME; i<NPLAYERS; i++) { //Writing hard coded names for computer players.
						String name = "Computer " + ( j++ );
						players[i] = new Player( name );
						}
			        
			        Match match = new Match(players, deck); //Giving deck and players data to match.			        
			        
			        PlayGameWithGUI window = new PlayGameWithGUI(match, players); //Now we are ready to play game. 
					
					
					window.frame.setVisible(true); //Setting frame visibility.
				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PlayGameWithGUI(Match match, Player[] players) {
		this.players = players;
		this.match = match;
		
		initialize(); //Initialize GUI Components...
		
		turn = 0; //First turn is player 1. Hence 0;
		gameHand = new Card[NPLAYERS] ; //Initializing gameHand.
	}
    
	/*
	 * This function is called when ever user plays move by clicking play move button... 
	 * This is event based function...
	 * */
    private void playGame() {
    	
    	int row = tables[turn].getSelectedRow(); //When ever user plays his move by selecting any cell (card) of table
    					//we get its row number.
		if(row == -1 && turn < NPLAYER_GAME) { //If row is -1, then user has not selected any row before clicking on button.
			//and turn is of human..
			JOptionPane.showMessageDialog(frame, "No row is selected. Please select any."); //Prompt User.
			return; //EXIT.
		}
		//********ELSE PART******
		
		Card selectedCard;
		if(turn >= NPLAYER_GAME) {
			selectedCard = match.playRandomMove(turn);
		}
		else {
			selectedCard = players[turn].popCardFromPlayerHandAtIndex(row); //this function will give us detail of card
			//by giving index number of card in player's cards' array. Each player has his/her cards in player class.
		}
		
		gameHand[turn] = selectedCard; //So we save that card in gameHand....
		
		lblChoosedCard.setText("You have chosed " + selectedCard.toString()+ "!"); //This label will show details of selected
		//card.
		
		lblChoosedCard.setBounds( 5 + (250*turn) , 400, 280, 40); //Label position is dynamic. It is changed according to turn.
		//If turn is of player 1, meaning turn variable will set to 0.  So it's x will be at 5. For player 2, this label's x
		//position will be 250 farther from player1's position turn will be set to 1 for player 2.
		
		if(turn == NPLAYERS-1) //If this turn is of last player... Meaning we have reach to completion of round.
		{
			afterRoundCompleted(); 	//So do all things before moving to next round. Like updating cards of all players.
			//Deciding winner of round and if this round is last round then end the game...
		}
		
		turn = (turn+1) % (NPLAYERS); //Incrementing the turn in range 0-3. Whenever it goes to 4, it resets to 0.
		//Now giving turn to next player..
		
		while(players[turn].isCardsEmpty()) //If next player's cards are empty, then we don't have to give him turn.
		{
			
			gameHand[turn] = null; //So, that player plays no card i.e. null.
			
			if(turn == NPLAYERS-1) //If that player is last player, then we have to again do all things we do after round 
				//completion.
			{
				afterRoundCompleted(); //So do all things before moving to next round. Like updating cards of all players.
				//Deciding winner of round and if this round is last round then end the game...
			}
			
			turn = (turn+1) % (NPLAYERS); //Incrementing the turn in range 0-3. Whenever it goes to 4, it resets to 0.
		}
		
		if( turn>=NPLAYER_GAME ) //If next turn is of Computer Player, then we show text NEXT in button.
		{
			playButton.setText("NEXT");
		}
		else //ELSE PLAY MOVE to player notifying you have to play game by selecting the card.
		{
			playButton.setText("Play Move");
		}
		
		playButton.setBounds(57 + turn*250, 406, 136, 36); //Also locate the button under player, whose turn is this. 
		
	    }
    
    private void afterRoundCompleted() { //All necessary things to do after completion of any round...
    	
    	int winnerOfRound = match.giveAllGameHandCardsToWinnerOfRound(gameHand); //If round has completed then we will distribute
    	// cards of game hand (i.e. cards played by players in any round) to winner of round. Finally this function
    	//will return Highest Rank card player of round (who is winner of that round.
		
    	refreshTables(players); //Since cards have changed to each player, so refresh the GUI tables.
		
		lblWinner.setBounds(26 + (250*winnerOfRound) , 363, 226, 30); //Relocate the label of winner under winner player.
		
		labelRoundNumber.setText("Round#"+String.valueOf(match.getRoundNumber())); //Also change the round number. 
		//Since round has completed...
		
		if (match.isGameEnded()) { //IF game has ended, that is if all 52 cards are at one player and 
			//no other player has any card to play.   
		//STOP THE GAME and NOTIGY USER
			JOptionPane.showMessageDialog(frame, "Game Ended at Round#"+ (match.getRoundNumber() -1) +". Winner of Game is "+ match.getWinnerOfGame().getName());
			JOptionPane.showMessageDialog(frame, "Game Results are saved in XML."); 
			
			match.saveMatchToXML(); //Save all Game Record to XML.
			frame.dispose(); //Close the window..
		}
    }
    
	
	
	//Refresh the table after round completion.
	private void refreshTables(Player[] players) {
		for (int k=0 ; k<NPLAYERS; k++) {
		    DefaultTableModel tableModel = (DefaultTableModel) tables[k].getModel();
		    tableModel.setRowCount(0); //Set count of rows to 0 first.
			Object rowData[][];
			
			rowData = new Object[players[k].totalCardsPlayerHas()][1];
			Card[] cards = players[k].getCards();
			for (int i=0; i<players[k].totalCardsPlayerHas(); i++) {
				rowData[i]= new Object[1];
				
				for(int j1=0; j1<1; j1++) {
					String[] str = {cards[i].toString()};
					tableModel.addRow(str);
				}
			}
			tableModel.fireTableDataChanged(); //trigger table to change its model.
		}	
	}
	
	//This gives model with filled date for table.
	private DefaultTableModel giveModel (Player player) {
		Object rowData[][];
		Object columnNames[] = { "Cards"}; //Column name
		
		rowData = new Object[player.totalCardsPlayerHas()][1]; //Number of cards that player has in his hand at any point of game.
		Card[] cards = player.getCards();  //Get all player's cards.
		
		for (int i=0; i<player.totalCardsPlayerHas(); i++) { //Save cards' data to model.
			rowData[i]= new Object[1];
			
			for(int j1=0; j1<1; j1++) {
				rowData[i][j1]= cards[i];
			}
			
		}
		@SuppressWarnings("serial")
		DefaultTableModel model = new DefaultTableModel(rowData,columnNames) 
		{
			@Override
		    public boolean isCellEditable(int row, int column) { //Make rows uneditable.
		       //all cells false
		       return false;
		    }
		};
		return model;
		
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//Initializing frame by setting size, layout and exit flag.
		frame = new JFrame();
		frame.setBounds(100, 100, 1050, 635);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		//Initializing Labels of Player names shown on top of GUI Screen.
		labelsOfPlayers  = new JLabel[NPLAYERS];
		for (int i=0; i<NPLAYERS; i++) {
			initializeLabelsOfPlayersName(i,players[i].getName());
		}
		
		//Initializing Play Move Button and attaching event driven function.
		playButton = new JButton("Play Move");
		playButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		playButton.setBounds(57 + 0*250, 406, 136, 36);
		frame.getContentPane().add(playButton);
		playButton.setVisible(true);
        playButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {		
				playGame();	
		}});  

        // Initializing other labels..
        lblWinner = new JLabel("Winner of previous round");
        lblChoosedCard = new JLabel("You have chosed queen of spades");
		this.initializeLabels(lblWinner, Color.RED);
		this.initializeLabels(lblChoosedCard, Color.BLACK);
		
		/*Initializing the label of round number shown at bottom of screen.
		 * */
		labelRoundNumber = new JLabel("Round#1");
		labelRoundNumber.setForeground(Color.BLUE);
		labelRoundNumber.setHorizontalAlignment(SwingConstants.CENTER);
		labelRoundNumber.setFont(new Font("Tahoma", Font.PLAIN, 25));
		labelRoundNumber.setBounds(10, 521, 1024, 52);
		frame.getContentPane().add(labelRoundNumber);
		
		//INITIALIZING PANELS, TABLES AND SCROLLPANES OF PLAYERS
		panels = new JPanel[NPLAYERS];
		tables = new JTable[NPLAYERS];
		scrollPanes = new JScrollPane[NPLAYERS];
		match.giveCardsToEachPlayer(CARDSperPLAYER);
		fillDataToTables(players); //This will set cards of each player after shuffling of deck.
        
	}
	// Initialize labels of player with their name.
	private void initializeLabelsOfPlayersName(int i, String name) {
		labelsOfPlayers[i] = new JLabel(name);
		labelsOfPlayers[i].setHorizontalAlignment(SwingConstants.CENTER);
		labelsOfPlayers[i].setFont(new Font("Tahoma", Font.BOLD, 15));
		labelsOfPlayers[i].setBackground(new Color(176, 196, 222));
		labelsOfPlayers[i].setBounds(26 + i*250, 11, 226, 30);
		frame.getContentPane().add(labelsOfPlayers[i]);
		
	}
	// Initialize labels of chose card and winner of game information..
	private void initializeLabels(JLabel label, Color color) {
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setForeground(color);
		label.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		frame.getContentPane().add(label);	
	}
	
	private void fillDataToTables(Player[] players) {
		for (int k=0 ; k<NPLAYERS; k++) { 
			DefaultTableModel model = giveModel(players[k]); //First making model for table. Give model gives 
			//model filled with date.
			panels[k] = new JPanel(); //Panel for each player
			panels[k].setBounds(26 + k*250, 43, 226, 321); //It size and location
			panels[k].setLayout(null); //Its absolute layout.
			frame.getContentPane().add(panels[k]); //Attaching panels to frame.
			
			tables[k] = new JTable(model); //Initializing tables by giving models in their constructors..
			tables[k].setBounds(16, 5, 225, 276); //Setting location of table in panel.
			
			if(k>=NPLAYER_GAME) //If player is computer. Then make that tables rows non selectable. Since user can not select 
				//rows. Computer will play random
			{
				tables[k].setFocusable(false);
				tables[k].setRowSelectionAllowed(false);
			}
			//Attaching tables to scrollpanes;
			scrollPanes[k] = new JScrollPane();
			scrollPanes[k].setViewportView(tables[k]);
			scrollPanes[k].setLocation(10, 11);
			scrollPanes[k].setSize(197, 288);
			panels[k].add(scrollPanes[k]);
		}
		
	}
}
