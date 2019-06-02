package cardgame;

import java.util.Random;
import java.util.Date;

/*
 * XML Dependencies!
 * 
*/
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.File;
import java.text.SimpleDateFormat;


public class Match {
	private final Deck deck; //Declaring deck of cards for game.
    private Player[] players; //Declaring player of game or match.
    private int totalNumberOfPlayers; //Total Number of players in game.
    private Player winner; //Winner of game...
    private int roundNumber; //Round number at particular state of game. 
    
    //Constructor
    public Match(Player[] players, Deck deck){
        //Initializing the deck of 52 cards without shuffling;
        this.deck = deck;
        
        totalNumberOfPlayers = players.length;
        //Initializing the array of 4 Players. 

        this.players = players;
        
        this.winner = null;
        this.roundNumber = 1; //Starting Round is 1..
        
    }
    
    //So this method gives cards to each player from deck...
    public void giveCardsToEachPlayer(int numberOfCardsToGiveToEachPlayer)
    {
        for (int j=0; j<numberOfCardsToGiveToEachPlayer; j++)
            for (int i=0; i<totalNumberOfPlayers; i++)
                this.players[i].setCards(deck.deal());
    }
    
    //Add card to player's hand. In case of winning we call this function.
    public void addCardToPlayersDeck (int playerNumber, Card card){
        players[playerNumber].setCards(card);
    }
    
    //Same way remove card from player's hand if he loses.
    public void removeCardFromPlayersDeck (int playerNumber, int index){
        players[playerNumber].popCardFromPlayerHandAtIndex(index);
    }
    
    //This move is random move played by computer.
    public Card playRandomMove(int playerNumber)
    {
        if(players[playerNumber].countOfCards() > 0){ //if there are cards in player's hand
            int randomIndex = this.randomValueGenerator(0, players[playerNumber].countOfCards()-1);// range is from 0 to last card' position.
            
            return players[playerNumber].popCardFromPlayerHandAtIndex(randomIndex); //Pop that card..
        }
        return null; //else return null
    }
   
    public int giveAllGameHandCardsToWinnerOfRound(Card[] hand){ //This method is called when round is completed and hand of game
    	// is passed to it.. So player who has highest rank card will receive all cards which were thrown in round.
        
    	//Finding highest rank card..
        Card highestRankCard = hand[0];
        int highestRankPlayerPosition = 0;
        for (int i=0 ; i<hand.length ; i++ )
        {
            if( hand[i] != null )
                if( hand[i].compareTo(highestRankCard) == 1)
                {
                    highestRankCard = hand[i];
                    highestRankPlayerPosition = i;
                }
            
        }
        this.moveAllCardsToHighestRankPlayer(hand, highestRankPlayerPosition); //This function copies all cards to player's hand
        //who is winner of round.
        
        this.roundNumber++;
        return highestRankPlayerPosition; //ALso return the index of highest rank player position in players array.
    }
    
  
  
    public boolean isGameEnded(){ //Return true if game has ended else false.
        for(int i= 0; i<totalNumberOfPlayers; i++){
            if( players[i].totalCardsPlayerHas() == 52 ) 
                return true;
            //If anyone of players has all 52 cards, then it means game has ended. So it will return true; 
        }
        //Otherwise it will return false;
        return false;  
    }
    
    private void moveAllCardsToHighestRankPlayer(Card[] hand, int playerPosition){ //Moves all cards to highest rank player..
        for (Card card : hand) {
            if (card != null) {
                players[ playerPosition ].setCards( card );
            }
        }
    }

    //It generator random value in integer with specified range. Range is inclusive like if range is from [1-4]; 
    //then it can generate any random value from 1 to 4;
    private int randomValueGenerator(int startingValueOfRange, int endingValueOfRange)
    {
        Random r = new Random();
        return r.nextInt((endingValueOfRange - startingValueOfRange) + 1) + startingValueOfRange;
    }
    
    /*
     * GET Functions
     * */
    
    public int getRoundNumber() {
    	return this.roundNumber;
    }
    
    public Player getWinnerOfGame(){ //REturns name of winner of game..
        for(int i= 0; i<totalNumberOfPlayers; i++){
            if( players[i].totalCardsPlayerHas() == 52 ) 
            {    this.winner = players[i];
                return this.winner;
            }//If anyone of players has all 52 cards, then it means that player is winner of the game, else no one is winner uptill now.
            //So returning null in that condition.
        }
        return null;
    }
    
    //SAVING MATCH TO XML
    public void saveMatchToXML()
    {
		try 
		{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc;
			Element rootElement;
			
			File xmlFile = new File("GameRecords.xml"); 
			if(xmlFile.createNewFile()) 
			{ //If creating file for the first time.
				doc = dBuilder.newDocument();
				rootElement = doc.createElement("games");
		        doc.appendChild(rootElement);
			}
			else 
			{
				doc = dBuilder.parse(xmlFile);
				rootElement = doc.getDocumentElement();
			}
			
			Element gameElement = doc.createElement("game");
			rootElement.appendChild(gameElement);
			
			// date element. Date and time when game was played.
			Element dateElement = doc.createElement("date");
			 
			Date date = new Date(); // this object contains the current date value  
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");  
			String currentDateAndTime = formatter.format(date);  
			 
			dateElement.appendChild(doc.createTextNode(currentDateAndTime));
			gameElement.appendChild(dateElement);
			
			// Players Element
			Element playersElement = doc.createElement("players");
			
			for (int i=0; i<totalNumberOfPlayers; i++) {
			// Player Element
			Element playerElement = doc.createElement("player");
			playerElement.appendChild(doc.createTextNode(players[i].getName()));
			playersElement.appendChild(playerElement);
			}
			gameElement.appendChild(playersElement);
			
			//Winner Element
			Element winnerElement = doc.createElement("winner");
			winnerElement.appendChild(doc.createTextNode(this.winner.getName()));
			gameElement.appendChild(winnerElement);
			 
			//Rounds Element
			Element roundsElement = doc.createElement("rounds");
			roundsElement.appendChild(doc.createTextNode(String.valueOf(this.roundNumber)));
			gameElement.appendChild(roundsElement);
			
			
			
			// write the content into XML file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("GameRecords.xml"));
			transformer.transform(source, result);
			
			// Output to console for testing
			StreamResult consoleResult = new StreamResult(System.out);
			transformer.transform(source, consoleResult);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
    }
	              
}
    


