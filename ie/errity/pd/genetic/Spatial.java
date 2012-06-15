package ie.errity.pd.genetic;

import ie.errity.pd.*;
import java.util.Hashtable;
import java.util.Random;
import java.util.BitSet;
//Import GUI Components
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 *Provides the ability to evolve a two dimensional grid of 
 *{@link  ie.errity.pd.Prisoner Prisoners}. This class allows for spatial 
 *interactions, that is, a {@link  ie.errity.pd.Prisoner Prisoner} can only 
 *interact (play IPD or mate) with the {@link  ie.errity.pd.Prisoner Prisoners} 
 *in the cells surrounding it. The edges of the grid 'overlap' so each 
 *{@link  ie.errity.pd.Prisoner Prisoner} has neighbours on all sides. The 
 *production of the next generation may be via simple evolution or genetic
 *evolution. This class also allows such a grid to be represented graphically.
 *@author Andrew Errity 99086921
 */
public class Spatial extends JPanel
{
	private Prisoner [][] world;	//Prisoners in the world
	private int[][] scores;			//Raw Fitness value for each player
	private int[][] scaledScores;	//Scaled Fitness value for each player
	private Hashtable matches;		//Record results of individual matches 
							//(decrease redundant calculation, improves speed)
	private Rules rules;			//Game Rules
	private int X;					//Max world X coordinate
	private int Y;					//Max world Y coordinate
	
	private Random rand = new Random();
	private int[] lastP;			//last player selected		
	
	
	/**
	 *Create a new 'Spatial grid' as described above
	 */
	public Spatial()
	{
		super();
		scores = new int[X][Y];
		scaledScores = new int[X][Y];
		matches = new Hashtable();
			
		lastP = new int[2];	
		lastP[0] = -1;
		lastP[1] = -1;
		
	}
	
	/**
	 *Create a new 'Spatial grid' of {@link  ie.errity.pd.Prisoner Prisoners} 
	 *as described above
	 *@param Players	two dimensional array of 
	 *{@link  ie.errity.pd.Prisoner Prisoners} to inhabit the grid
	 *@param r	{@link  ie.errity.pd.Rules Rules} to govern the grid
	 */
	public Spatial(Prisoner Players[][],Rules r )
	{
		super();
		world = Players;
		rules = r;
		X = world.length;
		Y = world[0].length;
		scores = new int[X][Y];
		scaledScores = new int[X][Y];
		matches = new Hashtable();
			
		lastP = new int[2];	
		lastP[0] = -1;
		lastP[1] = -1;
	}
	
	/**
	 *Every player plays the IPD against it's eight neighbours as goverend by 
	 *the {@link  ie.errity.pd.Rules Rules}
	 *<BR>With a large number of players and/or a large number of iterations of 
	 *each IPD {@link  ie.errity.pd.Game Game} this will be computationally 
	 *demanding and may take some time to complete (depending on Hardware 
	 *Specifications)
	 */
	public void Play()
	{
		int x, x_minus, x_plus = 0;
		int y, y_plus = 0;
		int y_minus = 0;
		
		for(x = 0; x < X; x++)
		{	
			//overlapping boundaries
			if(x <= 0)
				x_minus = X-1;
			else
				x_minus = x - 1;
				
			if(x < (X-1))
				x_plus = x + 1;
			else
				x_plus = 0;
				
			for(y = 0; y < Y; y++)
			{
				//overlapping boundaries	
				if(y <= 0)
					y_minus = Y-1;
				else
					y_minus = y - 1;
					
				if(y < (Y-1))
					y_plus = y + 1;
				else
					y_plus = 0;
					
				//reset the current players score to ignore previous games	
				world[x][y].setScore(0); 
				
				//play 8 surrounding neighbours
				match(x,y,x,y_minus);
				
				match(x,y,x,y_plus);
		
				match(x,y,x_minus,y_minus);
	
				match(x,y,x_minus,y);
		
				match(x,y,x_minus,y_plus);
	
				match(x,y,x_plus,y_minus);
	
				match(x,y,x_plus,y);
		
				match(x,y,x_plus,y_plus);
			
				//save score
				scores[x][y] = world[x][y].getScore();
			
			}
		}
		repaint();	//Update GUI (if present)
	}
	
	/**
	 *Calculate Player (x,y)'s fitness by playing IPD against its neighbours
	 *(without speed optimization)
	 *@param x	x coordinate of the player
	 *@param y	y coordinate of the player
	 */
	public void Play(int x, int y)
	{
		String key;
		int results[] = new int[2];
		Game g;
		int x_minus, x_plus = 0;
		int y_minus, y_plus = 0;
		
		//overlapping boundaries
		if(x <= 0)
			x_minus = X-1;
		else
			x_minus = x - 1;
			
		if(x < (X-1))
			x_plus = x + 1;
		else
			x_plus = 0;
		
		if(y <= 0)
			y_minus = Y-1;
		else
			y_minus = y - 1;
			
		if(y < (Y-1))
			y_plus = y + 1;
		else
			y_plus = 0;
		
		//reset the current players score to ignore previous games	
		world[x][y].setScore(0); 
		
		//play 8 surrounding neighbours
		key = encodeKey(x,y,x,y_minus);
		matches.remove(key);	//remove any history for these players, 
								//ensure up to date
		match(x,y,x,y_minus);
		
		key = encodeKey(x,y,x,y_plus);
		matches.remove(key);
		match(x,y,x,y_plus);

		key = encodeKey(x,y,x_minus,y_minus);
		matches.remove(key);
		match(x,y,x_minus,y_minus);
		
		key = encodeKey(x,y,x_minus,y);
		matches.remove(key);
		match(x,y,x_minus,y);
		
		key = encodeKey(x,y,x_minus,y_plus);
		matches.remove(key);
		match(x,y,x_minus,y_plus);
		
		key = encodeKey(x,y,x_plus,y_minus);
		matches.remove(key);
		match(x,y,x_plus,y_minus);
		
		key = encodeKey(x,y,x_plus,y);
		matches.remove(key);
		match(x,y,x_plus,y);

		key = encodeKey(x,y,x_plus,y_plus);
		matches.remove(key);
		match(x,y,x_plus,y_plus);
	
		//save score
		scores[x][y] = world[x][y].getScore();			
	}
	
	
	/**
	 *Calculate Player (x,y)'s fitness by playing IPD against its neighbours
	 *(with speed optimization)
	 *@param x	x coordinate of the player
	 *@param y	y coordinate of the player
	 */
	public void refresh(int x, int y)
	{
		String key;
		int results[] = new int[2];
		Game g;
		int x_minus, x_plus = 0;
		int y_minus, y_plus = 0;
		
		//overlapping boundaries
		if(x <= 0)
			x_minus = X-1;
		else
			x_minus = x - 1;
			
		if(x < (X-1))
			x_plus = x + 1;
		else
			x_plus = 0;
		
		if(y <= 0)
			y_minus = Y-1;
		else
			y_minus = y - 1;
			
		if(y < (Y-1))
			y_plus = y + 1;
		else
			y_plus = 0;
		
		//reset the current players score to ignore previous games	
		world[x][y].setScore(0); 
		
		//play 8 surrounding neighbours
		
		match(x,y,x,y_minus);
				
		match(x,y,x,y_plus);
		
		match(x,y,x_minus,y_minus);
		
		match(x,y,x_minus,y);
			
		match(x,y,x_minus,y_plus);
			
		match(x,y,x_plus,y_minus);
		
		match(x,y,x_plus,y);

		match(x,y,x_plus,y_plus);
	
		//save score
		scores[x][y] = world[x][y].getScore();	
		
	}
	
	/**
	 *Calculate the score of player (x,y) and refresh surrounding players to 
	 *take changes into consideration
	 *@param x	x coordinate of the player
	 *@param y	y coordinate of the player
	 */
	public void update(int x, int y)
	{
		String key;
		int results[] = new int[2];
		Game g;
		int x_minus, x_plus = 0;
		int y_minus, y_plus = 0;
		
		//overlapping boundaries
		if(x <= 0)
			x_minus = X-1;
		else
			x_minus = x - 1;
			
		if(x < (X-1))
			x_plus = x + 1;
		else
			x_plus = 0;
		
		if(y <= 0)
			y_minus = Y-1;
		else
			y_minus = y - 1;
			
		if(y < (Y-1))
			y_plus = y + 1;
		else
			y_plus = 0;
		
		Play(x,y); //recalculate score of (x,y)
				
		//refresh 8 surrounding neighbours
		//to take into account this change
		refresh(x,y_minus);
		
		refresh(x,y_plus);

		refresh(x_minus,y_minus);
		
		refresh(x_minus,y);
		
		refresh(x_minus,y_plus);
		
		refresh(x_plus,y_minus);
		
		refresh(x_plus,y);

		refresh(x_plus,y_plus);
	}
	
	/**
	 *Get all player's scores
	 *@return an array containing the player's scores
	 */
	public int[][] getScores(){ return scores;}
	
	/**
	 *Evolve a new generation using a Genetic algorithm
	 */
	public void Mate()
	{
		int [] parent1 = new int[2];
		int [] parent2 = new int[2];
		int [] weak = new int[2];
		BitSet[] Offspring = new BitSet[2];
		
		//Fitness scale the payoffs
		scaledScores = Genetic.scale(scores);
		
		//SELECTION
		parent1 = fitSelect();
		parent2 = fitSelectMate(parent1);
		
		
		//CROSSOVER
		if(rand.nextDouble() <= rules.getCrossP())
			Offspring = Genetic.crossover(world[ parent1[0] ][ parent1[1] ].getStrat(),world[parent2[0]][parent2[1]].getStrat());	
		else //CLONE
		{
			Offspring[0] = world[parent1[0]][parent1[1]].getStrat();
			Offspring[1] = world[parent2[0]][parent2[1]].getStrat();
		}
		
		//MUTATION
		Offspring[0] = Genetic.mutate(Offspring[0], rules.getMutateP());
		Offspring[1] = Genetic.mutate(Offspring[1], rules.getMutateP());
		
		//REPLACEMENT
		weak = weakReplace(parent1); //find who to replace
		world[ weak[0] ][ weak[1] ] = new Prisoner(Offspring[0]); //replace them
		update(weak[0],weak[1]); //update scores
		scaledScores = Genetic.scale(scores);
				
		weak = weakReplace(parent2); //find who to replace
		world[ weak[0] ][ weak[1] ] = new Prisoner(Offspring[1]);//replace them
		update(weak[0],weak[1]); //update scores
			
	}
	
	/**
	 *Fitness proportional selection (roulette wheel)
	 *@return the [x,y] coordinates selected
	 */
	private int[] fitSelect()
	{
		double t1, fitSum = 0;
		int target;
		
		//Set Target Fitness
		for(int i = 0; i < X; i++)
			for(int j = 0; j < Y; j++)
					fitSum += scaledScores[i][j];
		t1 = fitSum * rand.nextDouble();
		target = (int)t1;
		
		//Build up a sum of fitness
		//the individual who's fitness causes the sum to
		//exceed the target is selected	
		int fitness = 0;
		for(int i = lastP[0] + 1; ;i++)
			for(int j = lastP[1] + 1; ;j++)
			{
				if(i >= X)
					i = 0;
				if(j >= Y)
					j = 0;
				if(i != lastP[0] || j != lastP[1])
					fitness += scaledScores[i][j];
				if(fitness >= target)
				{
					lastP[0] = i;
					lastP[1] = j;
					return lastP;
				}
			}
				
	}
	
	/**
	 *Given the co-ordinates of a player, finds the best mate for the player
	 *from the surrounding 8 cells
	 *@param p1 array containing [x,y] coordinates of the player
	 *@return array containing [x,y] coordinates of the mate
	 */
	private int[] fitSelectMate(int[] p1)
	{
		int x = p1[0];
		int y = p1[1];
		int x_minus, x_plus = 0;
		int y_minus, y_plus = 0;
		
		//overlapping boundaries
		if(x <= 0)
			x_minus = X-1;
		else
			x_minus = x - 1;
			
		if(x < (X-1))
			x_plus = x + 1;
		else
			x_plus = 0;
	
		//overlapping boundaries	
		if(y <= 0)
			y_minus = Y-1;
		else
			y_minus = y - 1;
			
		if(y < (Y-1))
			y_plus = y + 1;
		else
			y_plus = 0;
		
		
		int max = scaledScores[x][y_minus];
		int index[] = new int[2];
		index = new int[]{x,y_minus};
		
		if(scaledScores[x][y_plus] > max)
		{
			max = scaledScores[x][y_plus];
			index = new int[]{x,y_plus};
		}
		if(scaledScores[x_minus][y_minus] > max)
		{
			max = scaledScores[x_minus][y_minus];
			index = new int[]{x_minus,y_minus};
		}
		if(scaledScores[x_minus][y] > max)
		{
			max = scaledScores[x_minus][y];
			index = new int[]{x_minus,y};
		}
		if(scaledScores[x_minus][y_plus] > max)
		{
			max = scaledScores[x_minus][y_plus];
			index = new int[]{x_minus,y_plus};
		}
		if(scaledScores[x_plus][y_minus] > max)
		{
			max = scaledScores[x_plus][y_minus];
			index = new int[]{x_plus,y_minus};
		}
		if(scaledScores[x_plus][y] > max)
		{
			max = scaledScores[x_plus][y];
			index = new int[]{x_plus,y};
		}
		if(scaledScores[x_plus][y_plus] > max)
		{
			max = scaledScores[x_plus][y_plus];
			index = new int[]{x_plus,y_plus};
		}
		return index;
	}
	

	/**
	 *Given the co-ordinates of a player, finds the worst player
	 *from the surrounding 8 cells
	 *@param p array containing [x,y] coordinates of the player
	 *@return array containing [x,y] coordinates of the worst surrounding player
	 */
	private int[] weakReplace(int[] p)
	{
		int x = p[0];
		int y = p[1];
		int x_minus, x_plus = 0;
		int y_minus, y_plus = 0;
		
		//overlapping boundaries
		if(x <= 0)
			x_minus = X-1;
		else
			x_minus = x - 1;
			
		if(x < (X-1))
			x_plus = x + 1;
		else
			x_plus = 0;
	
		//overlapping boundaries	
		if(y <= 0)
			y_minus = Y-1;
		else
			y_minus = y - 1;
			
		if(y < (Y-1))
			y_plus = y + 1;
		else
			y_plus = 0;
		
		
		int min = scaledScores[x][y_minus];
		int index[] = new int[2];
		index = new int[]{x,y_minus};
		
		if(scaledScores[x][y_plus]< min)
		{
			min = scaledScores[x][y_plus];
			index = new int[]{x,y_plus};
		}
		if(scaledScores[x_minus][y_minus] < min)
		{
			min = scaledScores[x_minus][y_minus];
			index = new int[]{x_minus,y_minus};
		}
		if(scaledScores[x_minus][y] < min)
		{
			min = scaledScores[x_minus][y];
			index = new int[]{x_minus,y};
		}
		if(scaledScores[x_minus][y_plus] < min)
		{
			min = scaledScores[x_minus][y_plus];
			index = new int[]{x_minus,y_plus};
		}
		if(scaledScores[x_plus][y_minus] < min)
		{
			min = scaledScores[x_plus][y_minus];
			index = new int[]{x_plus,y_minus};
		}
		if(scaledScores[x_plus][y] < min)
		{
			min = scaledScores[x_plus][y];
			index = new int[]{x_plus,y};
		}
		if(scaledScores[x_plus][y_plus] < min)
		{
			min = scaledScores[x_plus][y_plus];
			index = new int[]{x_plus,y_plus};
		}
		return index;	
	}
	
	
	/**
	 *Encodes a key to reference the hashtable history
	 *@param x0 x coordinate of player 1
	 *@param y0 y coordinate of player 1
	 *@param x1 x coordinate of player 2
	 *@param y1 y coordinate of player 2
	 *@return key to index history hashtable
	 */
	private String encodeKey(int x0,int y0,int x1,int y1)
	{		
		if(x0 < x1)
			return Integer.toString(x0) + '_' + Integer.toString(y0) + '_' + Integer.toString(x1) + '_' + Integer.toString(y1);
		else if(x0 > x1)
			return Integer.toString(x1) + '_' + Integer.toString(y1) + '_' + Integer.toString(x0) + '_' + Integer.toString(y0);
		else if(y0 < y1)
			return Integer.toString(x0) + '_' + Integer.toString(y0) + '_' + Integer.toString(x1) + '_' + Integer.toString(y1);
		else
			return Integer.toString(x1) + '_' + Integer.toString(y1) + '_' + Integer.toString(x0) + '_' + Integer.toString(y0);
	}
	
	/**
	 *Decodes a hashtable reference key
	 *@param s	key to index history hashtable
	 *@return array of player coordinates [x0,y0,x1,y1]
	 */
	private int[] decodeKey(String s)
	{
		int x0,y0,x1,y1,temp;
		temp = s.indexOf('_');
		x0 = Integer.parseInt( s.substring(0,temp) ,10 );
		y0 = Integer.parseInt( s.substring(temp+1,s.indexOf('_',temp+1) ) ,10);
		temp = s.indexOf('_',temp+1);
		x1 = Integer.parseInt( s.substring(temp+1,s.indexOf('_',temp+1) ) ,10);
		temp = s.indexOf('_',temp+1);
		y1 = Integer.parseInt( s.substring( temp+1, s.length()) ,10);
		return new int[]{x0,y0,x1,y1};
	}
	
	/**
	 *Play IPD: {@link  ie.errity.pd.Prisoner Prisoner} (x0,y0) vs 
	 *(x1,y1)
	 *@param x0 x coordinate of player 1
	 *@param y0 y coordinate of player 1
	 *@param x1 x coordinate of player 2
	 *@param y1 y coordinate of player 2
	 */
	private void match(int x0,int y0,int x1,int y1)
	{
		//if game has already been played, just retrieve score from memory
		//otherwise play IPD and update memory
		Game g;
		String key;
		int pos; //position of player's score in matches hashtable
		int [] gameScores;
		
		//determine whether player 0's score appears first or second in 
		//hashtable
		if(x0 < x1)
			pos = 0;
		else if(x0 > x1)
			pos = 1;
		else if(y0 < y1)
			pos = 0;
		else
			pos = 1;
		
		key = encodeKey(x0, y0, x1, y1);
		
		try
		{
			gameScores = (int[]) matches.get(key);//if match previously played
			world[x0][y0].updateScore(gameScores[pos]);	
		}
		catch(NullPointerException e)	
		{
			g = new Game(world[x0][y0],world[x1][y1],rules);
			g.Play();
			gameScores = g.getScores();	
		
			//update matches hashtable
			if(pos == 0)
				matches.put(key,gameScores);
			else
				matches.put(key,new int[]{gameScores[1],gameScores[0]});
		}
	}
	
	
	
	
	/**
	 *Responsible for painting the spatial grid to screen when neccesary
	 */
	public void paintComponent(Graphics g) 
    {
        super.paintComponent(g); //paint background

		//Get Limits
      	Insets insets = getInsets();
      	int x0 = insets.left;
      	int y0 = insets.top;
	    int currentWidth = getWidth() - insets.left - insets.right;
	    int currentHeight = getHeight() - insets.top - insets.bottom;
	    
	    //Display a series of rectangles, representing the players
	    for(int i = 0; i < X; i++)
	    {
	    	for(int j = 0; j < Y; j++)
	    	{
	    		g.setColor(world[i][j].getColor());	
	    		g.fillRect(x0+((currentWidth/X)*(i)),y0+((currentHeight/Y)*(j)),(currentWidth/X),(currentHeight/Y));
	    	}
	    }
	    
	   
	}
	
	/**
	  *Reset the grid
	  */
	public void clear()
	{
		X = 0;
		Y = 0;
		repaint();
	}
	
	/**
	 *Set the {@link  ie.errity.pd.Rules Rules}
	 *@param r new {@link  ie.errity.pd.Rules Rules}
	 */
	public void setRules(Rules r)
	{
		rules = r;
	}
	
	/**
	 *Set the players on the grid
	 *@param p	a two dimensional array of players on the grid
	 */
	public void setPlayers(Prisoner[][] p)
	{
		world = p;
		X = world.length;
		Y = world[0].length;
		scores = new int[X][Y];
		scaledScores = new int[X][Y];
		matches = new Hashtable();
			
		lastP = new int[2];	
		lastP[0] = -1;
		lastP[1] = -1;
	}
	
	
	/**
	 *Evolve a new generation using an Evolutionary algorithm
	 */
	public void Evolve()
	{
		//fitness scaling
		scaledScores = Genetic.scale(scores);
		
		//pick random player
		int x = 0;
		int y = 0;
		try{
		x = rand.nextInt(X);
		y = rand.nextInt(Y);
		}
		catch(Exception e){}
		//find fittest surrounding
		int [] fittest = fitSelectMate(new int[]{x,y});
		
		//replace current with fittest
		if(scaledScores[ fittest[0] ][ fittest[1] ] > scaledScores[x][y])
		{
			//Very small mutatations
			BitSet fitB = world[ fittest[0] ][ fittest[1] ].getStrat();
			if(rand.nextDouble() <= rules.getMutateP())
				fitB.flip(rand.nextInt(71));
	
			Prisoner fit = new Prisoner(fitB);
			world[x][y] = fit;	
			update(x,y);
		}
		
	}
	
	
		
}