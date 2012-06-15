package ie.errity.pd;

/**
 *This class represents the rules for a {@link  ie.errity.pd.Game Game} of the Prisoner's Dilemma.
 *<BR>Payoff Matrix used is
 *<PRE>
 *          -------------------------
 *          |Cooperate    |Defect   |
 *-----------------------------------
 *|Cooperate|     R,R     |   S,T   |
 *-----------------------------------
 *|Defect   |     T,S     |   P,P   |
 *-----------------------------------
 *</PRE>
 *@author	Andrew Errity 99086921
 */
public class Rules
{
	private int	iterations, generations, players;
	
	private int T,S,R,P;
	private double mutateP, crossP;	//mutate & crossover probabilities
	
	/**
	 *Default constructor<BR>
	 *Initialises rules as follows:
	 *Game Iterations = 100,
	 *T = 5, 
	 *S = 0, 
	 *R = 3, 
	 *P = 1, 
	 *Mutation probability = .95, 
	 *Generations = 250, 
	 *Number of Players = 50, 
	 */
	public Rules()
	{
		iterations = 100; //iterations of IPD
		T = 5; //Temptation to defect
		S = 0; //Sucker's Payoff
		R = 3; //Reward for mutual cooperation
		P = 1; //Punishment for mutual defection
		mutateP = 0.001; //probability of mutation
		crossP = 0.95;	//probability of crossover
		generations = 250;
		players = 50;
	}
	
	/**
	 *Create new game rules with specified parameters<BR>
	 *@param it the number of PD games to play in an iterated PD
	 *@param t the T value for the above payoff matrix
	 *@param s the S value for the above payoff matrix
	 *@param r the R value for the above payoff matrix
	 *@param p the P value for the above payoff matrix
	 */
	public Rules(int it, int t,int s, int r, int p)
	{
		iterations = it; //iterations of IPD
		T = t; //Temptation to defect
		S = s; //Sucker's Payoff
		R = r; //Reward for mutual cooperation
		P = p; //Punishment for mutual defection
		mutateP = 0.001; //probability of mutation
		crossP = 0.95; //probability of crossover
		generations = 250;
		players = 50;
	}
	
	/**
	 *Create new game rules with specified parameters<BR>
	 *@param it the number of PD games to play in an iterated PD
	 *@param t the T value for the above payoff matrix
	 *@param s the S value for the above payoff matrix
	 *@param r the R value for the above payoff matrix
	 *@param p the P value for the above payoff matrix
	 *@param m the probability of mutation
	 *@param c the probability of crossover
	 */
	public Rules(int it, int t,int s, int r, int p, double m, double c)
	{
		iterations = it; //iterations of IPD
		T = t; //Temptation to defect
		S = s; //Sucker's Payoff
		R = r; //Reward for mutual cooperation
		P = p; //Punishment for mutual defection
		mutateP = m; //probability of mutation
		crossP = c; //probability of crossover
		generations = 250;
		players = 50;
	}
	
	/**
	 *Create new game rules with specified parameters<BR>
	 *@param it the number of PD games to play in an iterated PD
	 *@param t the T value for the above payoff matrix
	 *@param s the S value for the above payoff matrix
	 *@param r the R value for the above payoff matrix
	 *@param p the P value for the above payoff matrix
	 *@param m the probability of mutation
	 *@param c the probability of crossover
	 *@param gen the maximum number of generations
	 *@param pl the number of players
	 */
	public Rules(int it, int t,int s, int r, int p, double m, double c, int gen, int pl)
	{
		iterations = it; //iterations of IPD
		T = t; //Temptation to defect
		S = s; //Sucker's Payoff
		R = r; //Reward for mutual cooperation
		P = p; //Punishment for mutual defection
		mutateP = m; //probability of mutation
		crossP = c; //probability of crossover
		generations = gen;
		players = pl;
	}
	
	/**
	 *@return the number of PD games to play in an iterated PD
	 */
	public int getIterations() {return iterations;}
	
	/**
	 *@return the T value for the above payoff matrix
	 */
	public int getT() {return T;}
	
	/**
	 *@return the S value for the above payoff matrix
	 */
	public int getS() {return S;}
	
	/**
	 *@return the R value for the above payoff matrix
	 */
	public int getR() {return R;}
	
	/**
	 *@return the P value for the above payoff matrix
	 */
	public int getP() {return P;}
	
	/**
	 *@return the probabilty of mutation
	 */	
	public double getMutateP() {return mutateP;}
	
	/**
	 *@return the probabilty of crossover
	 */
	public double getCrossP() {return crossP;}
	
	/**
	 *@return the number of players
	 */
	public int getNumPlayers() {return players;}
	
	/**
	 *@return the maximum number of generations
	 */
	public int getGenerations() {return generations;}
	
	
	 
	 
	 
	 
	 
	 
	
	/**
	 *@param it the number of PD games to play in an iterated PD
	 */
	public void setIterations(int it) {iterations = it;}
	
	/**
	 *@param t the T value for the above payoff matrix
	 */
	public void setT(int t) {T = t;}
	
	/**
	 *@param s the S value for the above payoff matrix
	 */
	public void setS(int s) {S = s;}
	
	/**
	 *@param r the R value for the above payoff matrix
	 */
	public void setR(int r) {R = r;}
	
	/**
	 *@param p the P value for the above payoff matrix
	 */
	public void setP(int p) {P = p;}	
	
	/**
	 *@param mp the probability of mutation
	 */
	public void setMutateP(double mp) {mutateP = mp;}
	
	/**
	 *@param cp the probability of crossover
	 */
	public void setCrossP(double cp) {crossP = cp;}	
	
	/**
	 *@param p the number of players
	 */
	public void setNumPlayers(int p) {players = p;}
	
	/**
	 *@param g the maximum number of generations
	 */
	public void setGenerations(int g) {generations = g;}		
	
}