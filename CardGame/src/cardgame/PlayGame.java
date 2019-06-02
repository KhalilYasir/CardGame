package cardgame;

import java.util.Scanner;

public class PlayGame {
	
	public static final int NCARDS = 52;               //Total Cards in game. 
    public static final int NPLAYERS = 4;              //Total players. min 1 max 4. 
    public static final int CARDSperPLAYER = NCARDS/NPLAYERS; // cards given to per player
    public final static int NSHUFFLE = 600;  // Number of times deck of cards will be shuffled.
    
    public static void main(String[] args) {
        
        Player[] players = new Player[NPLAYERS];  //Initializing players..
        Card[] hand;							  //game hand
        
        int nPlayers;							//total number of players will be humans..
        Scanner input = new Scanner(System.in); //user input
        
        System.out.println("\t\tWelcome to Cards Game"); //intro message
        System.out.println("\nPlease type 1 for One Player Game and 2 for Two Player Game."); // ask for n player game
        nPlayers = input.nextInt(); //user input
        
        System.out.println("\nGive details of each player: \n"); 
        for (int i = 0 ; i<nPlayers; i++){ //names of each player
            System.out.println("Name of Player"+(i+1)+ ": ");
            String name = input.next();
            players[i] = new Player(name);
        }
        
        int j = 1;
        for (int i = nPlayers; i<NPLAYERS; i++){ //setting other players names with computer prefix
            players[i] = new Player ("Computer " + (j++));
        }
        
        System.out.println("\n\t\t Thank you for giving details.. Lets start the game..");
        System.out.println("\nDeck before shuffling: ");
        Deck deck = new Deck(); //Initializing deck.
        
        System.out.println(deck); //showing it without shuffling.
        
        System.out.println("\nDeck after shuffling: ");
        deck.shuffle(NSHUFFLE);  
        System.out.println(deck); //after shuffling...
        
        Match match = new Match(players, deck); //initializing match...
        
        System.out.println("Dealing 13 cards to each player.... "); //giving 13 cards to each player...
        match.giveCardsToEachPlayer(CARDSperPLAYER);
        
        System.out.println("Showing each player's hand: "); //showing each player's hand.....
        System.out.println(); //NEW LINE
        for (Player p: players){
            System.out.println(p);
            System.out.println(); //NEW LINE
        }
        //for(Card a: players[0].getCards())
        	System.out.println(); //NEW LINE
        
        while(!match.isGameEnded())
        {	
        	System.out.println("\nRound#"+ (match.getRoundNumber() ) + ": " );
            hand = new Card[NPLAYERS];
            System.out.println("\nPlease throw your card. Type position of card to throw....");

            for(int i=0; i<nPlayers; i++){

                if(!players[i].isCardsEmpty()) //if cards of players are not empty..
                {
                    System.out.println(players[i].getName() +"'s turn to throw card.");
                    Card selectedCard;

                    do{
                    System.out.print("Position:  ");
                    int cardPos = input.nextInt();

                    System.out.println(); //NEW LINE
                    selectedCard = players[i].popCardFromPlayerHandAtIndex(cardPos); //select card according to card pos 
                    if(selectedCard == null)
                        System.out.println("Incorrect input.");
                    else
                        System.out.println("You have choosed "+ selectedCard);
                    }
                    while(selectedCard==null); //if card position is incorrect

                    hand[i] = selectedCard;
                }
            }

            System.out.println(); //NEW LINE
            for(int i=nPlayers; i<NPLAYERS; i++){
                if(!players[i].isCardsEmpty())
                {
                    Card selectedCard = match.playRandomMove(i);
                    hand[i] = selectedCard;
                    System.out.println(players[i].getName() +" has choosed "+ selectedCard);    
                }
            }

            Player winnerOfRound = players[match.giveAllGameHandCardsToWinnerOfRound(hand)]; //get winner of round
            System.out.println("\n\nWinner of this round is " + winnerOfRound.getName() );
            System.out.println("Showing each player's hand: ");

            System.out.println("Showing each player's hand After Round Completion: ");
            System.out.println(); //NEW LINE
            for (Player p: players){
                System.out.println(p);
                System.out.println(); //NEW LINE
            }

        }
        //GAME ENDED
        System.out.println("\n\t\t.....GAME ENDED.....");
        System.out.println("\nTotal Rounds: "+ (match.getRoundNumber()-1)); //total number of rounds took to play game.
        System.out.println("\n\nWinner of Game is "+ match.getWinnerOfGame().getName()); //showing winner's name
        
        //Saving Record to XML File. XML File is saved to project folder. Open it with any browser like Chrome or edge.
        match.saveMatchToXML();
        input.close();
    }
}
