package cardgame;

import java.util.Arrays;

public class Player {
    
    private String name;		//name of player..
    private int numberOfWins;   //number of wins of that player..
    private int numbderOfDraws; //number of draws of that player..
    private int numberOfLosts;  //How many game player has lost?
    private int totalNumberOfGamesPlayed; //Total number of games player has played.
    private Card[] cards;		//Cards of player at any instance of time of game.
    private int countOfCards;   //Count of cards of player.. 
    
    public Player(String name){
        
    	this.name = name; 		//initializing player's name....
        cards = new Card[52];	//Maximum player can have 52 cards if player becomes winner. 
        	//So maximum size of player's hand is 52
        for (int i=0; i < 52; i++) //Initializing all cards to null..
        {
            cards[i] = null;
        }
        countOfCards = 0;		//First count of cards is null.
        
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the numberOfWins
     */
    public int getNumberOfWins() {
        return numberOfWins;
    }

    /**
     * @param numberOfWins the numberOfWins to set
     */
    public void setNumberOfWins(int numberOfWins) {
        this.numberOfWins = numberOfWins;
    }

    /**
     * @return the numbderOfDraws
     */
    public int getNumbderOfDraws() {
        return numbderOfDraws;
    }

    /**
     * @param numbderOfDraws the numbderOfDraws to set
     */
    public void setNumbderOfDraws(int numbderOfDraws) {
        this.numbderOfDraws = numbderOfDraws;
    }

    /**
     * @return the numberOfLosts
     */
    public int getNumberOfLosts() {
        return numberOfLosts;
    }

    /**
     * @param numberOfLosts the numberOfLosts to set
     */
    public void setNumberOfLosts(int numberOfLosts) {
        this.numberOfLosts = numberOfLosts;
    }

    /**
     * @return the totalNumberOfGamesPlayed
     */
    public int getTotalNumberOfGamesPlayed() {
        return totalNumberOfGamesPlayed;
    }

    /**
     * @param totalNumberOfGamesPlayed the totalNumberOfGamesPlayed to set
     */
    public void setTotalNumberOfGamesPlayed(int totalNumberOfGamesPlayed) {
        this.totalNumberOfGamesPlayed = totalNumberOfGamesPlayed;
    }

    /**
     * @param index
     * @return
     */
    public Card returnCardAtIndex(int index) {
        if(index >=0 && index < countOfCards)//If index is not negative value and is less than current 
                                            //count of cards then return card at that index else return null.
            return cards[index];
        else 
            return null;
    }

    
    public Card[] getCards() {		
    	return Arrays.copyOf(cards, countOfCards); //Since we have maximum size array of cards, so we can give whole array.
    	//We have to give subarray of cards of length of countOfCards. countOfCards is maintained whenever we 
    	//inserts card to cards array 
	}


	/**
     * @param card
     */
    public void setCards( Card card) {
        if(countOfCards < 52)
            this.cards[this.countOfCards++] = card;
        
        else
            System.out.println("Error. Player's deck is full");
    }
    
    public Card popCardFromPlayerHandAtIndex(int index){
        Card poppedCard = null;
        if( index >= 0 && index < this.countOfCards ) //If index is not negative value and is less than current 
                                        //count of cards then perform popping operation else return null.
        {
            poppedCard = this.cards[index];
            for(int i = index+1; i<this.countOfCards; i++){
                this.cards[i-1] = this.cards[i]; 
            }
            this.cards[this.countOfCards-1] = null;
            countOfCards--;
        }
        return poppedCard;
    }

    /**
     * @return the countOfCards
     */
    public int countOfCards() { 
        return countOfCards;
    }

    public boolean isCardsEmpty(){ //Return true if player has no cards left in game else false;
        return this.countOfCards <= 0; //if count of cards are 0 or less.
    }
    
    public int totalCardsPlayerHas(){ //Total number of cards player at particular state of game.
        return this.countOfCards;
    }
    
    @Override
    public String toString(){ //Function to show Player's hand....
        
        String out = "";
        
        out += this.name +"'s Hand: \n";
        if(this.countOfCards <=0 ){
            out+= "Empty.";
        }
        for (int i=0; i<this.countOfCards ; i++){
            out += "["+i+"] "+ this.cards[i] + ", ";
        }
        return out;
    }
    
}
