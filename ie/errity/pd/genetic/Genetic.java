package ie.errity.pd.genetic;

import ie.errity.pd.*;

import java.util.BitSet;
import java.util.Random;

/**
 *Provides Genetic operations
 *@author Andrew Errity 99086921
 */
public class Genetic
{	

	/**
	 *Mate two parents using random, one point crossover
	 *@param parenta first parent (<code>BitSet</code> representation)
	 *@param parentb second parent (<code>BitSet</code> representation)
	 *@return an array containing the two children (<code>BitSet</code> 
	 *representation)
	 */
	public static BitSet[] crossover(BitSet parenta,BitSet parentb)
	{
		Random rand = new Random();
		BitSet child1 = new BitSet(71);
		BitSet child2 = new BitSet(71);
		
		//One point splicing
		int slicePoint = rand.nextInt(71); //rnd num between 0-70
		BitSet a = (BitSet)parenta.clone();
		a.clear(slicePoint,71);
		BitSet b = (BitSet)parenta.clone();
		b.clear(0,slicePoint);
		BitSet c = (BitSet)parentb.clone();
		c.clear(slicePoint,71);
		BitSet d = (BitSet)parentb.clone();
		d.clear(0,slicePoint);
		
		//Combine start of p1 with end of p2
		child1.or(a);
		child1.or(d);
		//Combine start of p2 with end of p1		
		child2.or(c);
		child2.or(b);
		
		//Return the children
		BitSet[] offspring = {child1, child2};
		return offspring;
	}
	
	/**
	 *Mutate (Flip a bit in the bitset) with probability mProb
	 *@param original the entity to be mutated
	 *@param mProb the probability of a bit being mutated
	 *@return the (possibly) mutated entity
	 */
	public static BitSet mutate(BitSet original, double mProb)
	{
		Random rand = new Random();
		for(int m = 0; m < 71; m++)
		{
			//Small possibility a bit copied from parent to child is mutated
			if(rand.nextDouble() <= mProb)
				original.flip(m);
		}
		//Return the (possibly) strategy
		return original;
	}
	
	
	/**
	 *Linear fitness scaling of an array of 
	 *{@link  ie.errity.pd.Prisoner Prisoners} 
	 *<BR>Based on the {@link  ie.errity.pd.Prisoner Prisoner's} Scores
	 *@param curPopulation the array of {@link  ie.errity.pd.Prisoner Prisoners}
	 * to be scaled
	 *@return the scaled fitnesses
	 */
	public static int[] scale(Prisoner[] curPopulation)
	{
		//init
		int min, max, total = 0;
		double avg = 0;
		int fs = 0;
		double a,b,fsmax, delta = 0;
		final int cmult = 2;
		int popSize = curPopulation.length;
		int [] scaled = new int[popSize];
		
		//Calculate min, max and average payoffs
		min = curPopulation[0].getScore();
		max = curPopulation[0].getScore();
		total = curPopulation[0].getScore();
		for(int i = 1; i < popSize; i++)
		{
			if(curPopulation[i].getScore() < min)
				min = curPopulation[i].getScore();
			if(curPopulation[i].getScore() > max)
				max = curPopulation[i].getScore();
			total += curPopulation[i].getScore();	
		}
		avg = total/popSize;	
	
	
		//Calculate scaling factors
		if( min > ((cmult*avg - max) / (cmult - 1)) ) //non-negative test
		{
			delta = max - avg;
			fsmax = cmult * avg;
			a = (cmult-1) * avg / delta;
			b = avg * ((max-fsmax)/delta);
		}
		else //negative: scale as much as possible
		{
			delta = avg - min;
			a = avg / delta;
			b = -1 * min * avg / delta;
		}
		
		//scale each player's fitness value
		for(int s = 0; s < popSize; s++)
		{
			fs = (int) ((a*curPopulation[s].getScore()) + b);
			scaled[s] = fs;
		}
		
		return scaled;
	}
	
	
	/**
	 *Linear fitness scaling of a 2D array of 
	 *{@link  ie.errity.pd.Prisoner Prisoners} 
	 *<BR>Based on the {@link  ie.errity.pd.Prisoner Prisoner's} Scores
	 *@param curPop the array of {@link  ie.errity.pd.Prisoner Prisoners}  
	 *to be scaled
	 *@return the scaled fitnesses
	 */
	public static int[][] scale(Prisoner[][] curPop)
	{
		//init
		int min, max, total = 0;
		double avg = 0;
		int fs = 0;
		double a,b,fsmax, delta = 0;
		final int cmult = 2;
		int popSizeX = curPop.length;
		int popSizeY = curPop[0].length;
		int scaled[][] = new int[popSizeX][popSizeY];
		
		//Calculate min, max and average payoffs
		min = curPop[0][0].getScore();
		max = curPop[0][0].getScore();
		total = 0;
		for(int i = 0; i < popSizeX; i++)
		{
			for(int j = 0; j < popSizeY; j++)
			{
				if(curPop[i][j].getScore() < min)
					min = curPop[i][j].getScore();
				if(curPop[i][j].getScore() > max)
					max = curPop[i][j].getScore();
				total += curPop[i][j].getScore();	
			}
		}
		avg = total/(popSizeX*popSizeY);	
		
		//Calculate scaling factors
		if( min > ((cmult*avg - max) / (cmult - 1)) ) //non-negative test
		{
			delta = max - avg;
			fsmax = cmult * avg;
			a = (cmult-1) * avg / delta;
			b = avg * ((max-fsmax)/delta);
		}
		else //negative: scale as much as possible
		{
			delta = avg - min;
			a = avg / delta;
			b = -1 * min * avg / delta;
		}
		
		//scale each player's fitness value
		for(int s = 0; s < popSizeX; s++)
		{
			for(int g = 0; g < popSizeY; g++)
			{
				fs = (int) ((a*curPop[s][g].getScore()) + b);
				scaled[s][g] = fs;
			}
		}
		
		return scaled;
	}
	
	
	/**
	 *Linear fitness scaling of a 2D array
	 *@param curPop the array to be scaled
	 *@return the scaled fitnesses
	 */
	public static int[][] scale(int[][] curPop)
	{
		//init
		int min, max, total = 0;
		double avg = 0;
		int fs = 0;
		double a,b,fsmax, delta = 0;
		final int cmult = 2;
		int popSizeX = curPop.length;
		int popSizeY = curPop[0].length;
		int scaled[][] = new int[popSizeX][popSizeY];
		
		//Calculate min, max and average payoffs
		min = curPop[0][0];
		max = curPop[0][0];
		total = 0;
		for(int i = 0; i < popSizeX; i++)
		{
			for(int j = 0; j < popSizeY; j++)
			{
				if(curPop[i][j] < min)
					min = curPop[i][j];
				if(curPop[i][j] > max)
					max = curPop[i][j];
				total += curPop[i][j];	
			}
		}
		avg = total/(popSizeX*popSizeY);

		//Calculate scaling factors
		if( min > ((cmult*avg - max) / (cmult - 1)) ) //non-negative test
		{
			delta = max - avg;
			fsmax = cmult * avg;
			a = (cmult-1) * avg / delta;
			b = avg * ((max-fsmax)/delta);
		}
		else //negative: scale as much as possible
		{
			delta = avg - min;
			a = avg / delta;
			b = -1 * min * avg / delta;
		}
		
		
		//scale each player's fitness value
		for(int s = 0; s < popSizeX; s++)
		{
			for(int g = 0; g < popSizeY; g++)
			{
				fs = (int) ((a*curPop[s][g]) + b);
				scaled[s][g] = fs;
			}
		}
		
		return scaled;
	}
	
}