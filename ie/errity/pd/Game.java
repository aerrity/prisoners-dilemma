package ie.errity.pd;

import java.util.BitSet;


/**
 *A game of the Iterated Prisoner's Dilemma between two 
 *{@link  ie.errity.pd.Prisoner  Prisoners}
 *@author	Andrew Errity 99086921
 */
public class Game
{
	//Two Players
	private Prisoner P1;
	private Prisoner P2;
	
	//Histories from each players view
	private BitSet P1History;
	private BitSet P2History;
	
	//Payoffs recieved
	private int P1Score;
	private int P2Score;

	//Rules for the IPD
	private Rules rules;
	
	/**
	 *Create a new game of the Iterated Prisoner's Dilemma between two players
	 *@param p1 player 1
	 *@param p2 player 2
	 *@param r 	the rules which govern the game
	 */
	public Game(Prisoner p1, Prisoner p2, Rules r)
	{
		P1 = p1;
		P2 = p2;
		rules = r;	
	}
	
	
	/**Play a game of IPD according to the rules*/
	public void Play()
	{
		//Init
		int length = rules.getIterations();
		int iteration = 0;
		P1Score = 0;
		P2Score = 0;
		BitSet P1History = new BitSet();
		BitSet P2History = new BitSet();
		boolean P1move, P2move;
		
		//Play the specified number of PD games
		for(iteration = 0; iteration < length; iteration++)
		{	
			//Get each players move
			P1move = P1.play(iteration,P1History);
			P2move = P2.play(iteration,P2History);
			
			//Update scores according to payoffs
			if(P1move && P2move) //CC
			{
				P1Score += rules.getR();
				P2Score += rules.getR();
			}
			else if(P1move && !P2move) //CD
			{
				P1Score += rules.getS();
				P2Score += rules.getT();
			
			}
			else if(!P1move && P2move) //DC
			{
				P1Score += rules.getT();
				P2Score += rules.getS();
			
			}
			else if(!P1move && !P2move) //DD
			{
				P1Score += rules.getP();
				P2Score += rules.getP();
			
			}
					
			//Update player histories
			if(P1move)
			{
				P1History.set(iteration*2);
				P2History.set((iteration*2)+1);
			}
			else
			{
				P1History.clear(iteration*2);
				P2History.clear((iteration*2)+1);
			}
			if(P2move)
			{			
				P1History.set((iteration*2)+1);
				P2History.set((iteration*2));
			}
			else
			{
				P1History.clear((iteration*2)+1);
				P2History.clear((iteration*2));
			}							
		}
		//Update each players score
		P1.updateScore(P1Score);
		P2.updateScore(P2Score);
	}
	
	/**
	 *Get game results
	 *@return array containing player 1 and player 2's score - [p1,p2]
	 */
	public int[] getScores()
	{ 
		int	[] scores = {P1Score, P2Score};
		return scores;
	}
}