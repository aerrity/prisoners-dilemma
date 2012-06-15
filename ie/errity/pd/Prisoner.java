package ie.errity.pd;

import java.util.BitSet;
import java.util.Random;
import java.awt.Color;

/**
 *This class represents a Prisoner with a strategy to play the 
 *Prisoner's Dilemma {@link  ie.errity.pd.Game Game}.
 *@author	Andrew Errity 99086921
 */
public class Prisoner implements Cloneable
{
	private String name;
	final private BitSet Strategy;
	
	private int Score; 	//total payoffs recieved
	private Moves m;	//table used to decode strategy
	
	/**
	 *Create a new Prisoner to play the prisoners dilemma
	 *@param na 	the Prisoner's name
	 *@param strat 	a <code>BitSet</code> representing the player's strategy
	 */
	public Prisoner(String na, BitSet strat)
	{
		name = na;
		Strategy = strat;		
		Score= 0;
     	m = new Moves();
     
	}
	
	/**
	 *Create a new Prisoner to play the prisoners dilemma
	 *@param na 	the Prisoner's name
	 *@param strat 	a String representing the player's strategy
	 */
	public Prisoner(String na, String strat)
	{
		name = na;
		BitSet strat2 = new BitSet(71);
		for(int i = 0; i < 71; i++)
		{
			if(strat.charAt(i) == 'C')
				strat2.set(i);
		}
		
		Strategy = strat2;		
		Score= 0;
     	m = new Moves();
	}
	
	/**
	 *Create a new Prisoner to play the prisoners dilemma (nameless)
	 *@param strat a <code>BitSet</code> representing the player's strategy
	 */
	public Prisoner(BitSet strat)
	{
		name = null;
		Strategy = strat;			
		Score = 0;
     	m = new Moves();
	}
	
	
	/**
	 *Create a 	Predefined Prisoner
	 *@param s 	the name of a predefined prisoner <BR> Valid values include:
	 *<UL>
	 *<LI><code>TFT</code> - Tit for Tat</LI>
	 *<LI><code>TFTT</code> - Tit for Two Tats</LI>
	 *<LI><code>ALLC</code> - Always cooperate</LI>
	 *<LI><code>ALLD</code> - Always defect</LI>
	 *</UL>
	 */
	public Prisoner(String s)
	{
		BitSet temp = new BitSet(71);
		name = s;
		if(s.equals("TFT"))		
		{
			//Tit for Tat
			//Only Defect if opponent defected on the previous move
			temp.set(0); //C on first move
			temp.set(1); //Start-up info
			temp.set(3);
			temp.set(5);
			for(int i = 7; i < 71;i+=2) //set strategy
				temp.set(i);
			Strategy = temp;
		}	
		else if(s.equals("TF2T") || s.equals("TFTT"))		
		{
			//Tit for Two Tats
			//Only Defect if opponent defected on the two previous moves
			temp.set(0,71); //set all bits (all C)
			temp.clear(6);
			for(int i = 14; i < 71; i+=8)
			{
				//clear moves where opponent defected on the two previous moves
				temp.clear(i);
				temp.clear(i-2);
			}
			Strategy = temp;
			
		}	
		else if(s.equals("ALLC"))
		{
			temp.set(0,71); //set all bits (all C)
			Strategy = temp;
		}
		else if(s.equals("ALLD"))
		{
			temp.clear(0,71); //clear all bits (all D)
			Strategy = temp;
		}
		else 
			Strategy = null;
		
		Score= 0;
     	m = new Moves();
	}
	
	/**
	 *Adds value to the Prisoner's current score
	 *@param s the value to be added
	 *@return the new score
	 */
	public int updateScore(int s)
	{
		Score = s + Score;
		return Score;
	}
	
	/**
	 *Returns the Prisoner's current score
	 *@return the Prisoner's current score
	 */
	public int getScore(){return Score;} 
	
	/**
	 *Set the Prisoner's current score
	 *@param s the new score
	 */
	public void setScore(int s)
	{
		Score = s;
	}
	
	/**
	 *Returns the Prisoner's name
	 *@return the Prisoner's name
	 */
	public String getName(){return name;}
	
	/**
	 *Returns the Prisoner's strategy
	 *@return a <code>BitSet</code> representing the strategy
	 */
	public BitSet getStrat(){return Strategy;}
	
	
	/**
	 *Gets the Prisoner's next game move
	 *@param iteration	 current iteration number
	 *@param History game history represented as a <code>BitSet</code>
	 *<BR>History should represent previous moves as C=1 and D=0
	 *<BR>This players move should be followed by the opponents move, one
	 *such pair for each previous iteration of PD
	 *@return <code>true</code> or <code>false</code> {C or D}
	 */
	public boolean play(int iteration, BitSet History)
	{
		// if first move return start move
		if(iteration == 0)
			return Strategy.get(0);
		// if second move
		else if(iteration == 1)
		{
			 if(History.get(1)) //opponent Cooperated
			 	return Strategy.get(1);
			 else //opponent Defected
			 	return Strategy.get(2);	 	
		}
		// if third move
		else if(iteration == 2)
		{
			if(History.get(1) && History.get(3)) //opponent CC
				return Strategy.get(3);	 
			else if(History.get(1) && !History.get(3)) //opponent CD
				return Strategy.get(4);	 
			else if(!History.get(1) && History.get(3)) //opponent DC
				return Strategy.get(5);	 
			else if(!History.get(1) && !History.get(3)) ////opponent DD
				return Strategy.get(6);	 
		}
		// if normal move use normal strategy 
		else
		{
			//Get last 3 sets of moves
			BitSet hist = History.get( (iteration*2) - 6, (iteration*2));
			int x = m.get(hist);
			return Strategy.get(x+7); //adjust index to skip setup info
		}
		return false;
	}
	
	/**
	 *Copy the current Prisoner
	 *@return a copy of the current Prisoner
	 */
	public Object clone ()
    {
        Object self = null;
        try
		{
        	self = super.clone();
        }
        catch(CloneNotSupportedException e)
        {
        	throw new RuntimeException("CloneNotSupportedException");
        }
        return self;
    }
    
    /**
     *Convert the Prisoner's strategy to a string of C's and D's
     *@return a string representation of the Prisoner's strategy
     */
    public String toString()
	{	
		String p = new String();
		for(int i = 0; i < 71; i++)
		{
			if(Strategy.get(i))
				p = p + 'C';
			else
				p = p + 'D';
		}
		return p;
	}
	
	/**
	 *Returns a Prisoner with a random strategy
	 *@return a Prisoner with a random strategy
	 */
	public static Prisoner getRand()
	{
		BitSet ra;
		Random rand = new Random();
		
		//build random player
		ra = new BitSet(71);
		for(int i = 0; i < 71;i++) //set strategy
		{
			if(rand.nextBoolean())
				ra.set(i);
			else
				ra.clear(i);
		}
		return new Prisoner(ra);
	}
	
	/**
	 *Returns an array of Prisoners with random strategies
	 *@param num_players the number of Prisoners to create
	 *@return an array of Prisoners with random strategies
	 */
	public static Prisoner[] getRand(int num_players)
	{
		BitSet ra;
		Random rand = new Random();
		Prisoner Players [] = new Prisoner[num_players];
		//build random players
		for(int j = 0; j < num_players; j++)
		{
			ra = new BitSet(71);
			for(int i = 0; i < 71;i++) //set strategy
			{
				if(rand.nextBoolean())
					ra.set(i);
				else
					ra.clear(i);
			}
			Players[j] = new Prisoner(ra);
			
		}
		return Players;
	}
	
	/**
	 *Returns a 2D array of Prisoners with random strategies
	 *@param num_players the length of array to return
	 *@param num_players2 the depth of array to return
	 *@return a 2D array of Prisoners with random strategies
	 */
	public static Prisoner[][] getRand(int num_players,int num_players2)
	{
		BitSet ra;
		Random rand = new Random();
		Prisoner Players [][] = new Prisoner[num_players][num_players2];
		for(int f = 0; f < num_players; f++)
		{
			for(int j = 0; j < num_players2; j++)
			{
				ra = new BitSet(71);
				for(int i = 0; i < 71;i++) //set strategy
				{
					if(rand.nextBoolean())
						ra.set(i);
					else
						ra.clear(i);
				}
				Players[f][j] = new Prisoner(ra);
				
			}
		}
		return Players;
	}
	
	
	/**
	 *Return which color to render the prisoner (based on it's strategy)
	 *<BR>In order to differentiate between different 'types' of player
	 *this method sets color according to how often the Prisoner will
	 *cooperate and how often they defect
	 *<UL>	
	 *<LI>Orange/Red signifies defecting ('nasty') strategies</LI>
	 *<LI>Yellow/Green signifies cooperating ('nice') strategies</LI>
	 *<LI>Blue indicates balanced strategies</LI>
	 *<UL>
	 *@return the color to render the prisoner (based on it's strategy)
	 */
	public Color getColor()
	{
		double card = Strategy.cardinality();
		card = card/71;
		if(card > .65)
			return new Color(0xFF,0xFF,0x00); //YELLOW
		else if(card > .6)
			return new Color(0x99,0xCC,0x32); //YELLOW GREEN
		else if(card > .55)
			return new Color(0x52,0x7F,0x76); //GREEN
		else if(card > .5)
				return new Color(0x4D,0x4D,0xFF); //NEON BLUE
		else if(card > .45)
			return new Color(0x23,0x23,0x8E);	//NAVY BLUE
		else if(card > .4)
			return new Color(0xFF,0x7F,0x00);	//ORANGE
		else if(card > .35)
			return new Color(0xFF,0x24,0x00);	//ORANGE RED
		else
			return new Color(0x8C,0x17,0x17);	//SCARLET 
	}
	
	/**
	 *Return the strategy 'type' of the prisoner
	 *<UL>	
	 *<LI>0,1,2 signifies defecting ('nasty') strategies</LI>
	 *<LI>5,6,7 signifies cooperating ('nice') strategies</LI>
	 *<LI>3,4  indicates balanced strategies</LI>
	 *<UL>
	 *@return the strategy 'type' of the prisoner
	 */
	public int getType()
	{
		double card = Strategy.cardinality();
		card = card/71;
		if(card > .65)
			return 0; 
		else if(card > .6)
			return 1; 
		else if(card > .55)
			return 2; 
		else if(card > .5)
			return 3; 
		else if(card > .45)
			return 4;	
		else if(card > .4)
			return 5;	
		else if(card > .35)
			return 6;
		else
			return 7;	
	}
}
			 

