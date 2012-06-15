package ie.errity.pd.graphics;

import ie.errity.pd.*;
import ie.errity.pd.graphics.*;
import java.util.BitSet;
//Import GUI components
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 *Interactive Prisoner's Dilemma Panel.
 *<BR>Allows a user to play the IPD against the computer or watch two computer 
 *players play each other. Saved strategies may be loaded and played.
 *@author	Andrew Errity 99086921
 */
public class InteractivePanel extends JPanel implements ActionListener
{
	//Swing components
	JPanel actionPanel, historyPanel, scoresPanel;
	JButton playBtn, stopBtn, cBtn, dBtn, viewBtn,viewBtn2;
	JComboBox prisList,prisList2;
	ButtonGroup group;
	JLabel opponentScore, yourScore, opponentMove, yourMove, Opptxt, Opptxt2;
	JRadioButton human,comp;
	JList list1;
	PayOffPanel payoffPanel;
	MenuFrame mf; //Master Frame
	
	Prisoner opponent, opponent2;	//players
	Rules rules;
	
	
	//Scores
	private int P1Score;
	private int P2Score;
	private int P1Total;
	private int P2Total;
	String[] hist1;		//Player Histories
	private int iteration;	//current iteration of the PD
	private int itMax; 		//maximum number of game iterations
	private BitSet history,P2History;;
	boolean humanp;		//Play with human
	boolean stopped;	//Stop game running
	


	/**
	 *Create new Interactive panel
	 *@param frame 	application master Frame 
	 *@param s 		current saved strategies
	 */
	public InteractivePanel(final MenuFrame frame, String []s)
	{		
		super();
		
		//init
		rules = new Rules();
		iteration = 0;
		P1Score = 0;
		P2Score = 0;
		P1Total = 0;
		P2Total = 0;
		history = new BitSet();
		P2History = new BitSet();
		
		hist1 = new String[12];
		for(int i = 0; i < 12; i++)
			hist1[i] = "(Player 1)    -   vs   -    (Player 2)";
	
		stopped = true;
		humanp = true;
		mf = frame;
		
		//Player Selection combo boxes
		prisList = new JComboBox(s);
		prisList.setSelectedIndex(0);
		prisList.addActionListener(this);
		prisList2 = new JComboBox(s);
		prisList2.setSelectedIndex(0);
		prisList2.addActionListener(this);
		prisList2.setEnabled(false);
		
		//Create 4 Subpanels
		payoffPanel = new PayOffPanel();
		actionPanel = new JPanel();
		historyPanel = new JPanel();
		scoresPanel = new JPanel();
		
		payP();
		historyP();
		actionP();
		scoresP();
		
		setPreferredSize(new Dimension(800, 600));
		setLayout(new GridLayout(2,2,5,5));
		add(actionPanel);
		add(scoresPanel);
		add(payoffPanel);
		add(historyPanel);
	}
	
	//Create payoff Panel
	private void payP()
	{
		payoffPanel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createTitledBorder("Payoff matrix"), 
			BorderFactory.createEmptyBorder(5,5,5,5)));
	}
	
	//Create History Panel
	private void historyP()
	{
		historyPanel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createTitledBorder("Game History"), 
			BorderFactory.createEmptyBorder(5,5,5,5)));
		
		//Add scrolling list of player histories
		list1 = new JList();
		list1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list1.setVisibleRowCount(12);
		JScrollPane scr1 = new JScrollPane(list1);
		historyPanel.add(scr1);
		list1.setListData(hist1);
	}
	
	//Create Action Panel
	private void actionP()
	{
		actionPanel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createTitledBorder("Game Controls"), 
			BorderFactory.createEmptyBorder(5,5,5,5)));
		actionPanel.setLayout(new GridLayout(4,1,5,5));
	
		//RADIO BUTTONS
		human = new JRadioButton("Human vs Computer");
	    human.setMnemonic(KeyEvent.VK_H);
	    human.setActionCommand("Human");
	    human.addActionListener(this);
	    comp = new JRadioButton("Computer vs Computer");
	    comp.setMnemonic(KeyEvent.VK_M);
	    comp.setActionCommand("Computer");
	    comp.addActionListener(this);
	    human.setSelected(true);
	    //Group the radio buttons.
	    group = new ButtonGroup();
	    group.add(human);
	    group.add(comp);
	    JPanel rdo = new JPanel();
	    rdo.setLayout(new GridLayout(1,2,5,5));
	    rdo.add(human);
	    rdo.add(comp);
	
		//BUTTONS
		playBtn = new JButton("Start");
		playBtn.setMnemonic(KeyEvent.VK_P);
	    playBtn.setActionCommand("Play");
	    playBtn.addActionListener(this);
	    playBtn.setEnabled(true);
	    
	    stopBtn = new JButton("Stop");
		stopBtn.setMnemonic(KeyEvent.VK_O);
	    stopBtn.setActionCommand("Stop");
	    stopBtn.addActionListener(this);
	    stopBtn.setEnabled(false);
	    
	    viewBtn = new JButton("View");
	    viewBtn.setMnemonic(KeyEvent.VK_O);
        viewBtn.setActionCommand("View");
        viewBtn.addActionListener(this);
        
        viewBtn2 = new JButton("View");
	    viewBtn2.setMnemonic(KeyEvent.VK_I);
        viewBtn2.setActionCommand("View2");
        viewBtn2.addActionListener(this);
        viewBtn2.setEnabled(false);
     
	    
	    JPanel command = new JPanel();
	    command.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
	    command.setLayout(new GridLayout(1,3,2,2));
	    command.add(playBtn);
	    command.add(stopBtn);

	    
	    //GAME BUTTONS
	    cBtn = new JButton("COOPERATE");
		cBtn.setMnemonic(KeyEvent.VK_C);
	    cBtn.setActionCommand("C");
	    cBtn.addActionListener(this);
	    cBtn.setEnabled(false);
	    
	    dBtn = new JButton("DEFECT");
		dBtn.setMnemonic(KeyEvent.VK_D);
	    dBtn.setActionCommand("D");
	    dBtn.addActionListener(this);
	    dBtn.setEnabled(false);
	    
	    JPanel game = new JPanel();
	    game.setBorder(	BorderFactory.createEmptyBorder(2,2,2,2));
		game.setLayout(new GridLayout(1,2,5,5));
	    game.add(cBtn);
	    game.add(dBtn);

		//PLAYER LISTBOXES
		Opptxt = new JLabel("Select Player 1 :       ");
		JPanel opp = new JPanel();
		opp.setLayout(new BoxLayout(opp,BoxLayout.X_AXIS));
		opp.add(Opptxt);
		opp.add(prisList);
		opp.add(viewBtn);
	    
		Opptxt2 = new JLabel("Player 2 : HUMAN");
		opp.setLayout(new GridLayout(2,3,5,5));
		opp.add(Opptxt2);
		opp.add(prisList2);
		opp.add(viewBtn2);
		
		actionPanel.add(rdo);
		actionPanel.add(opp);
	    actionPanel.add(command);
	    actionPanel.add(game);
	}
	
	//Create Scores Panel
	private void scoresP()
	{
		scoresPanel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createTitledBorder("Scores"), 
			BorderFactory.createEmptyBorder(5,5,5,5)));
		scoresPanel.setLayout(new BoxLayout(scoresPanel,BoxLayout.Y_AXIS));
			
		opponentScore = new JLabel("<html><font color=#FF0000 size=+2>Player 1's Total Score: " +  (new Integer(0)).toString() + "</font>");
		yourScore = new JLabel("<html><font color=#0000FF size=+2>Player 2's Total Score: " +  (new Integer(0)).toString() + "</font>");
	
		opponentMove = new JLabel();
		yourMove = new JLabel();
		scoresPanel.add(opponentScore);
		scoresPanel.add(yourScore);
	
		JPanel tPanel = new JPanel();
		tPanel.setBorder(BorderFactory.createEmptyBorder(35,5,5,5));
		tPanel.setLayout(new BoxLayout(tPanel,BoxLayout.Y_AXIS));
		tPanel.add(opponentMove);
		tPanel.add(yourMove);
		scoresPanel.add(tPanel);
	}
	
	
	/**
	 *ActionListener for any button events
	 */
	public void actionPerformed(ActionEvent e) 
    {	
    	boolean move;
    	if ("Play".equals(e.getActionCommand())) 
    	{
	    	//Start was clicked
	    	boolean invalid = false;
	    	
	    	if( !humanp && (prisList2.getSelectedItem().equals("Empty")))
	    		invalid = true;
	    	if( prisList.getSelectedItem().equals("Empty"))
	    		invalid = true;
	    	
	    	if(!invalid) 
	    	{
	    		//Enable/Disable buttons as required
	    		if(humanp)
	    		{
	    			cBtn.setEnabled(true);
	    			dBtn.setEnabled(true);
	    		}
	    		stopBtn.setEnabled(true);
	    		playBtn.setEnabled(false);
	    		prisList.setEnabled(false);
	    		prisList2.setEnabled(false);
	    		human.setEnabled(false);
	    		comp.setEnabled(false);
	    		  
	    		
	    		//Setup for a new Game
	    		if(!humanp)
	    			opponent2 = mf.getPrisoner(prisList2.getSelectedIndex());	
	    		opponent = mf.getPrisoner(prisList.getSelectedIndex());
	    		iteration = 0;
				P1Score = 0;
				P2Score = 0;
				P1Total = 0;
				P2Total = 0;
				history = new BitSet();
				P2History = new BitSet();
				opponentScore.setText("<html><font color=#FF0000 size=+2>Player 1's Total Score: " +  (new Integer(P2Total)).toString() + "</font>");
				yourScore.setText("<html><font color=#0000FF size=+2>Player 2's Total Score: " +  (new Integer(P1Total)).toString() + "</font>");
	    		
	    		opponentMove.setText("<html><font color=#FF0000 size=+1>-</font>");
				yourMove.setText("<html><font color=#0000FF size=+1>-</font>");
				
				itMax = rules.getIterations();
				hist1 = new String[itMax];
				
				for(int i = 0; i < itMax; i++)
					hist1[i] = "(Player 1)    -   vs   -    (Player 2)";		
				list1.setListData(hist1);
				
				//If "Computer v Computer" - begin game
				//otherwise wait for C or D press from user
				if(!humanp)
				{
					move();
	    			stopped = false;
	    		}
			}
			else
				opponentMove.setText("<html><font color=#FFFFFF size=+1>Please select a valid player</font>");
    	}
    	else if ("Stop".equals(e.getActionCommand())) 
    	{
    		//Stop was clicked
    		
    		//Enable/Disable buttons as required
    		cBtn.setEnabled(false);
    		dBtn.setEnabled(false);
    		stopBtn.setEnabled(false);
    		playBtn.setEnabled(true);
    		prisList.setEnabled(true);
    		if(!humanp)
    		{
    			prisList2.setEnabled(true);
    			
    		}
    		human.setEnabled(true); 
    		comp.setEnabled(true);
			stopped = true;
    	}
    	
    	else if ("C".equals(e.getActionCommand()) && (iteration < itMax))
    	{
    		//Cooperate was clicked and we haven't exceed iteration limits
    		
    		//Get opponent's move and update scores & history
    		move = move(true);
    		opponentScore.setText("<html><font color=#FF0000 size=+2>Player 1's Total Score: " +  (new Integer(P2Total)).toString() + "</font>");
			yourScore.setText("<html><font color=#0000FF size=+2>Player 2's Total Score: " +  (new Integer(P1Total)).toString() + "</font>");
			if(move)
				opponentMove.setText("<html><font color=#FF0000 size=+1>Player 1 Cooperated - Payoff: " + (new Integer(P2Score)).toString()+ "</font>");
			else
				opponentMove.setText("<html><font color=#FF0000 size=+1>Player 1 Defected - Payoff: " + (new Integer(P2Score)).toString()+ "</font>");
			yourMove.setText("<html><font color=#0000FF size=+1>Player 2 Cooperated - Payoff: " + (new Integer(P1Score)).toString()+ "</font>");	
			//history
			if(move)
				hist1[iteration-1] = "(Player 1)    C   vs   C    (Player 2)";
			else
				hist1[iteration-1] = "(Player 1)    D   vs   C    (Player 2)";
			list1.setListData(hist1);
    	}
    	else if ("D".equals(e.getActionCommand()) && (iteration < itMax))
    	{
    		//Defect was clicked and we haven't exceed iteration limits
    		
    		//Get opponent's move and update scores & history
    		move = move(false);
    		opponentScore.setText("<html><font color=#FF0000 size=+2>Player 1's Total Score: " +  (new Integer(P2Total)).toString() + "</font>");
			yourScore.setText("<html><font color=#0000FF size=+2>Player 2's Total Score: " +  (new Integer(P1Total)).toString() + "</font>");
			if(move)
				opponentMove.setText("<html><font color=#FF0000 size=+1>Player 1 Cooperated - Payoff: " + (new Integer(P2Score)).toString()+ "</font>");
			else
				opponentMove.setText("<html><font color=#FF0000 size=+1>Player 1 Defected - Payoff: " + (new Integer(P2Score)).toString()+ "</font>");
			yourMove.setText("<html><font color=#0000FF size=+1>Player 2 Defected - Payoff: " + (new Integer(P1Score)).toString()+ "</font>");
    		
    		//history
			if(move)
				hist1[iteration-1] = "(Player 1)    C   vs   D    (Player 2)";
			else
				hist1[iteration-1] = "(Player 1)    D   vs   D    (Player 2)";
			list1.setListData(hist1);
    	}
    	else if ("View".equals(e.getActionCommand()))
    	{
    		//View for player 1 was clicked
    		mf.view(prisList.getSelectedIndex());
    	}
    	else if ("View2".equals(e.getActionCommand()))
    	{
    		//View for player 2 was clicked
    		mf.view(prisList2.getSelectedIndex());
    	}
    	else if(( "C".equals(e.getActionCommand()) || "D".equals(e.getActionCommand()) ) && (iteration >= itMax) )
    	{
    		//C or D was clicked but iteration limit is exceeded
    		
    		//indicate that game has ended
    		opponentMove.setText("<html><font color=#FFFFFF size=+1>Game over!</font>");
			yourMove.setText("<html><font color=#0000FF size=+1> </font>");
			//Enable/Disable buttons as required
			cBtn.setEnabled(false);
    		dBtn.setEnabled(false);
    		stopBtn.setEnabled(false);
    		playBtn.setEnabled(true);
    		prisList.setEnabled(true);
    		if(!humanp)
    		{
    			prisList2.setEnabled(true);
    		
    		}
    		human.setEnabled(true); 
    		comp.setEnabled(true);
    		stopped = true;
		}
		else if("Human".equals(e.getActionCommand()))
    	{
    		//Human vs Computer Selected
    		humanp = true;
    		//Enable/Disable buttons as required
    		prisList2.setEnabled(false);
    		viewBtn2.setEnabled(false);
    		Opptxt2.setText("Player 2 : HUMAN");
		}
		else if("Computer".equals(e.getActionCommand()))
    	{
    		//Computer vs Computer Selected
    		humanp = false;
    		//Enable/Disable buttons as required
    		prisList2.setEnabled(true);
    		viewBtn2.setEnabled(true);
    		Opptxt2.setText("Select Player 2 :       ");
		}	
	}
	
	
	/**
	 *Removes an element from the listbox
	 *@param i index of element to remove
	 */
	public void delete(int i)
	{		
		prisList.removeItemAt(i);
		prisList.insertItemAt("Empty",i);
		prisList2.removeItemAt(i);
		prisList2.insertItemAt("Empty",i);
	}
	
	/**
	 *Adds an element from the listbox
	 *@param i index to add element at
	 *@param s entry to add
	 */
	public void update(int i, String s)
	{
		prisList.removeItemAt(i);
		prisList.insertItemAt(s,i);
		prisList2.removeItemAt(i);
		prisList2.insertItemAt(s,i);
	}
	
	/**
	 *Set the {@link  ie.errity.pd.Rules Rules}
	 *@param r new {@link  ie.errity.pd.Rules Rules}
	 */
	public void setRules(Rules r)
	{
		rules = r;
		//Update the Payoff panel
		payoffPanel.setPayoffs(rules.getT(),rules.getR(),rules.getS(),rules.getP());
	}
	
	//Human vs Computer Play
	//Gets opponents next move and calculates game scores
	//returns opponents move
	private boolean move(boolean P1move)
	{
		boolean P2move = true;
			
		P2move = opponent.play(iteration,history);
		
		if(P1move && P2move) //CC
		{
			P1Score = rules.getR();
			P2Score = rules.getR();
		}
		else if(P1move && !P2move) //CD
		{
			P1Score = rules.getS();
			P2Score = rules.getT();
		
		}
		else if(!P1move && P2move) //DC
		{
			P1Score = rules.getT();
			P2Score = rules.getS();
		
		}
		else if(!P1move && !P2move) //DD
		{
			P1Score = rules.getP();
			P2Score = rules.getP();
		
		}
				
		//Update histories
		if(P1move)
			history.set((iteration*2)+1);
		else
			history.clear((iteration*2)+1);
		if(P2move)
			history.set((iteration*2));
		else
			history.clear((iteration*2));
		
		P1Total += P1Score;
		P2Total += P2Score;
		iteration++;	
		opponent.setScore(P2Score);		
		return P2move;
	}
	
	
	//Computer vs Computer Play
	//Plays all iterations of the PD between the two selected players
	//Play is performed in a background thread so GUI remains responsive
	//Method is responsible for updating the GUI as required
	private void move()
	{
		if(stopped)	//not already playing a game
		{
			//Create a seperate thread to play the IPD in
			final SwingWorker worker = new SwingWorker() 
			{
				public Object construct()
		        {
					boolean P1move, P2move;
					
					//Play IPD and update GUI as neccesary
					for(iteration = 0; iteration < itMax; iteration++)
					{	
						if(stopped)
							break;
						P1move = opponent.play(iteration,history);
						P2move = opponent2.play(iteration,P2History);
						
						if(P1move && P2move) //CC
						{
							P1Score = rules.getR();
							P2Score = rules.getR();
							opponentMove.setText("<html><font color=#FF0000 size=+1>Player 1 Cooperated - Payoff: " + (new Integer(P1Score)).toString()+ "</font>");
							yourMove.setText("<html><font color=#0000FF size=+1>Player 2 Cooperated - Payoff: " + (new Integer(P2Score)).toString()+ "</font>");
	
							hist1[iteration] = "(Player 1)    C   vs   C    (Player 2)";
							list1.setListData(hist1);
						}
						else if(P1move && !P2move) //CD
						{
							P1Score = rules.getS();
							P2Score = rules.getT();
							opponentMove.setText("<html><font color=#FF0000 size=+1>Player 1 Cooperated - Payoff: " + (new Integer(P1Score)).toString()+ "</font>");
							yourMove.setText("<html><font color=#0000FF size=+1>Player 2 Defected - Payoff: " + (new Integer(P2Score)).toString()+ "</font>");
							
							hist1[iteration] = "(Player 1)    C   vs   D    (Player 2)";
							list1.setListData(hist1);
						}
						else if(!P1move && P2move) //DC
						{
							P1Score = rules.getT();
							P2Score = rules.getS();
							opponentMove.setText("<html><font color=#FF0000 size=+1>Player 1 Defected - Payoff: " + (new Integer(P1Score)).toString()+ "</font>");
							yourMove.setText("<html><font color=#0000FF size=+1>Player 2 Cooperated - Payoff: " + (new Integer(P2Score)).toString()+ "</font>");
						
							hist1[iteration] = "(Player 1)    D   vs   C    (Player 2)";
							list1.setListData(hist1);
						}
						else if(!P1move && !P2move) //DD
						{
							P1Score = rules.getP();
							P2Score = rules.getP();
							opponentMove.setText("<html><font color=#FF0000 size=+1>Player 1 Defected - Payoff: " + (new Integer(P1Score)).toString()+ "</font>");
							yourMove.setText("<html><font color=#0000FF size=+1>Player 2 Defected - Payoff: " + (new Integer(P2Score)).toString()+ "</font>");
							
							hist1[iteration] = "(Player 1)    D   vs   D    (Player 2)";
							list1.setListData(hist1);
						}
								
						//Update player histories
						if(P1move)
						{
							history.set(iteration*2);
							P2History.set((iteration*2)+1);
						}
						else
						{
							history.clear(iteration*2);
							P2History.clear((iteration*2)+1);
						}
						if(P2move)
						{			
							history.set((iteration*2)+1);
							P2History.set((iteration*2));
						}
						else
						{
							history.clear((iteration*2)+1);
							P2History.clear((iteration*2));
						}
						//Update Scores
						P1Total += P1Score;							
						P2Total += P2Score;	
						opponentScore.setText("<html><font color=#FF0000 size=+2>Player 1's Total Score: " +  (new Integer(P1Total)).toString() + "</font>");
						yourScore.setText("<html><font color=#0000FF size=+2>Player 2's Total Score: " +  (new Integer(P2Total)).toString() + "</font>");						
					}
					//When all iterations completed
					opponentMove.setText("<html><font color=#FFFFFF size=+1>Game over!</font>");
					yourMove.setText("<html><font color=#0000FF size=+1> </font>");
					//Enable/Disable buttons as required
					cBtn.setEnabled(false);
					dBtn.setEnabled(false);
					stopBtn.setEnabled(false);
					playBtn.setEnabled(true);
					prisList.setEnabled(true);
					if(!humanp)
					{
						prisList2.setEnabled(true);
					
					}
					human.setEnabled(true); 
					comp.setEnabled(true);
					stopped = true; //Game has ended
					return opponent;
				}
			};
			worker.start();	//Start the game thread
		}
	}
}