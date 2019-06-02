package cardgame;

public class Card implements Comparable<Card>
{
	/*CLUB has lowest value while SPADE has highest..
	 *So, if i have 10 of SPADE and other player has 10 of HEART, then i will win the game..
	 * */
    public static final int CLUB    = 0; 
    public static final int DIAMOND = 1;
    public static final int HEART   = 2;
    public static final int SPADE   = 3;
    
    /*naming the cards with relevant value.
     * */
    public static final int ACE     = 1;
    public static final int KING    = 13;
    public static final int QUEEN   = 12;
    public static final int JACK    = 11;
    
    /*Saving colors names and their values....
     * */
    private static final String[] COLORS = { "Club", "Diamond", "Heart", "Spade"};
    private static final String[] VALUES = { "", "", "2", "3", "4", "5", 
                            "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
    
    private final byte color; //Saving in byte because it will consume less space also its range is under byte.
    private final byte value; //Same


    //Constructor//
    public Card( int color, int value )
    {
        if(value == Card.ACE) //If its ace, but its value is 14 in array. Also ACE is greater than all..
            this.value = 14;
        else
            this.value = (byte) value;
        
        this.color = (byte) color;
    }

    /**
     * @return the colors
     */
    public String getColorStr() {
        return COLORS[color];
    }

    /**
     * @return the values
     */
    public String getValueStr() {
        return VALUES[value];
    }

    /**
     * @return the color
     */
    public byte getColor() {
        return color;
    }

    /**
     * @return the value
     */
    public byte getValue() {
        return value;
    }
    
    
    @Override //returns 1 for greater and -1 for lower than card. and 0 for both are same rank card.
    public int compareTo(Card card){
    if( card == null ) //If comparing card is null..
        return 1; 
    if( this.value > card.value )  //If card value like ACE>KING>QUEEN
        return 1;
    else if (this.value == card.value) //In case of conflict...
        if (this.color > card.color) //Check the suit of card, if suit has higher precedence...
            return 1;
        else if (this.color < card.color)
            return -1;
        else
            return 0;
     else
        return -1;
    }
    @Override
    public String toString() //TOSTring method to print card's data..
    {
        return VALUES[getValue()] + " of "+ COLORS[getColor()].toLowerCase();
    }
}