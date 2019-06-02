package cardgame;

import java.util.Random;


public class Deck {
   
    public static final int NCARDS = 52; //Total 52 CARDS..
    private int startIndexToRemoveCard; //This is index from where card is removed from deck in order to give to players. 
    
    private final Card[] deckOfCards; //So, it array which saves all 52 cards.

    public Deck(){
    	//Initializing deck of cards..
        deckOfCards = new Card[NCARDS];
        int j = 0;
        for ( int color = Card.CLUB; color <= Card.SPADE; color++ )
        {
            for ( int value = 1; value <= 13; value++ )
            {
                if(value == Card.ACE)
                {
                    deckOfCards[j+12] = new Card ( color, Card.ACE ); //placing ace at the end of suite.
                }
                else
                    deckOfCards[j++] = new Card( color, value );
                
            }
            j++;
            
        }
        
        this.startIndexToRemoveCard = 0; //First card will removed and then incremented..
        
    }
    /*
    How Shuffle works?
    
    We pick two random cards from deck array and exchange their positions.
    Random cards' position range will be from 0 to 51, since we have 52 cards in a deck.
    
    'times' parameter tells how many times shuffling will be performed. 
    
    *More the value of 'times' more random deck will be. Also, by increasing value of 'times'
    time of process of shuffling will be increased.
    *'times' value can be from 0 to Maximum integer.
    
    */
    public void shuffle(int times){
        
        int cardPosition1, cardPosition2;
        for ( int i = 1; i<=times; i++ )
        {
            cardPosition1 = this.randomValueGenerator(0, 51);
            cardPosition2 = this.randomValueGenerator(0, 51);
            
            /*
            Exchange both cards' positions though both random numbers are same.
            */
            Card temp = this.deckOfCards[cardPosition1];
            this.deckOfCards[cardPosition1] = this.deckOfCards[cardPosition2];
            this.deckOfCards[cardPosition2] = temp;
        }
    }
    
    //It generator random value in integer with specified range. Range is inclusive like if range is from [1-4]; 
    //then it can generate any random value from 1 to 4;
    private int randomValueGenerator(int startingValueOfRange, int endingValueOfRange)
    {
        Random r = new Random();
        return r.nextInt((endingValueOfRange - startingValueOfRange) + 1) + startingValueOfRange;
    }
    
    //Deal method gives cards to players from deck..
    public Card deal()
    {
        if( startIndexToRemoveCard < NCARDS )
        {
            return ( deckOfCards[startIndexToRemoveCard++] );
        }
        else{
            System.out.println("Deck is empty!");
            return null;
        }
    }
    
    @Override //IT shows how it is printed on console.
    public String toString()
    {
        String deck = "";
        
        int i = 0;
        
        for ( int color = Card.CLUB; color <= Card.SPADE; color++ )
        {   
            for ( int value = 1; value <= 13; value++ )
                deck += ( deckOfCards[i++] + "\n" );
            
            deck += "\n"; //NewLine after completion of one suite;
        }
        
        return deck;
    }
}


