package ie.errity.pd.graphics;

import ie.errity.pd.*;
import ie.errity.pd.genetic.*;
//Import GUI components
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.plaf.metal.*;

/**
 *A Panel displaying the GUI for viewing and manipulating 
 *{@link  ie.errity.pd.genetic.Spatial Spatial} grid Evolution.
 *<BR>Displays the current spatial grid, summary of population percentages and
 *score statistics. Also displays a 
 *{@link  ie.errity.pd.graphics.GraphPanel graph} charting scores from 
 *generation to generation. Provides user with controls to start and stop 
 *evolution, change evolution type and view 
 *{@link  ie.errity.pd.Prisoner Prisoners}.
 *@author Andrew Errity 99086921
 */
public class SpatialPanel extends JPanel implements ActionListener
{
	//Swing Components
	private JLabel maxLbl, minLbl, avgLbl;
	private JPanel actionPanel, displayPanel, subPanel,typePanel, buttonPanel, radioPanel, statsPanel; 	
	private JButton startBtn, stopBtn, fitBtn, weakBtn;
	private JRadioButton ga, ea;
	
	private StrategyDialog fitDlg, weakDlg;
	private GraphPanel graphPanel;
	private Rules rules;
	private Spatial grid;
	
	private SwingWorker worker;	//used to multithread the spatial evolution
	private boolean stopped;
	private int max, min, total;
	private double avg;
	
	
	private boolean GA;	//use genetic algorithm
	
	//Used to identify the different types of prisoner
	private double [] prisTypes;
	private JLabel type0,type1,type2,type3,type4,type5,type6,type7;
	private String [] color;
	private String [] typeNames;
	
	private MenuFrame mFrame;	//Master frame
	
	
	/**
	 *Create new Spatial grid panel
	 *@param mf 	application master frame
	 */
	public SpatialPanel(MenuFrame mf)
	{
		//init
		stopped = true;
		GA = false;
		mFrame = mf;
	
		prisTypes = new double[]{0,0,0,0,0,0,0,0};
		color = new String[]{"#FFFF00",
								"#99CC32",
								"#527F76",
								"#4D4DFF",
								"#23238E",
								"#FF7F00",
								"#FF2400",
								"#8C1717"};
		typeNames = new String[]{"Very 'Nice'",
									"Mostly Cooperative",
									"Cooperative",
									"Balanced ('nice')",
									"Balanced ('nasty')",
									"Tendency to Defect",
									"Mostly Defects",
									"Very 'Nasty'"};
		
		GridBagLayout gridbag = new GridBagLayout();
	    GridBagConstraints c = new GridBagConstraints();
		rules = new Rules();
		actionPanel = new JPanel();
		displayPanel = new JPanel();
		graphPanel = new GraphPanel();
		subPanel = new JPanel();
		subPanel.setLayout(new GridLayout(1,2,5,5));
		
		grid = new Spatial();
		
		fitDlg = new StrategyDialog(mf,"Fittest");
		weakDlg = new StrategyDialog(mf,"Weakest");
		
		//populate the sub panels
		displayP();
		graphP();
		actionP();
		
		// Create the main panel to contain the sub panels.
		setPreferredSize(new Dimension(800, 600));
		setLayout(gridbag);
		setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
	
		subPanel.add(displayPanel);
		subPanel.add(actionPanel);
	
		c.fill = GridBagConstraints.BOTH; 
		c.weightx = 1;
		c.weighty = 1;
		c.ipady = 100;
		c.gridx = 0;
		c.gridy = 0;
		gridbag.setConstraints(subPanel, c);
		add(subPanel);
		c.ipady = 200;
		c.gridx = 0;
		c.gridy = 1;
		gridbag.setConstraints(graphPanel, c);
		add(graphPanel);		
	}
	
	/**
	 *Build the Display Panel
	 */
	private void displayP()
	{
		// Add border around the display panel.
		displayPanel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createTitledBorder("Main Display"), 
			BorderFactory.createEmptyBorder(5,5,5,5)));
		displayPanel.setLayout(new BorderLayout());
		//Add the spatial grid component	
		displayPanel.add(grid,BorderLayout.CENTER);
	}
	
	/**
	 *Build the graph Panel
	 */
	private void graphP()
	{
		// Add border around the graph panel.
		graphPanel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createTitledBorder("Graph"), 
			BorderFactory.createEmptyBorder(5,5,5,5)));
		//Set graph colors and labels
		graphPanel.setLabels("Min Payoff","Max Payoff", "Average Payoff","Generation");
		graphPanel.setColors(new Color(0x6B,0x23,0x8E), new Color(0xCF,0xB5,0x3B), new Color(0x21,0x5E,0x21));//purple, blue, green
	}
	
	
	/**
	 *Build the action Panel
	 */
	private void actionP()
	{	
		actionPanel.setLayout(new BoxLayout(actionPanel,BoxLayout.Y_AXIS));
				
		//RADIO BUTTONS
		ga = new JRadioButton("Use a Genetic Algorithm");
	    ga.setMnemonic(KeyEvent.VK_G);
	    ga.setActionCommand("Genetic");
	    ga.addActionListener(this);
	    ea = new JRadioButton("Use an Evolutionary Algorithm");
	    ea.setMnemonic(KeyEvent.VK_E);
	    ea.setActionCommand("Evolutionary");
	    ea.addActionListener(this);
	    ea.setSelected(true);
	    //Group the radio buttons.
	    ButtonGroup group = new ButtonGroup();
	    group.add(ga);
	    group.add(ea);
	    
		radioPanel = new JPanel();
		// Add border around the panel.
		radioPanel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		radioPanel.setLayout(new GridLayout(2,1,2,2));
		radioPanel.add(ea);
		radioPanel.add(ga);
		
		//STATS
			
			/* Non - HTML labels - For Pre Swing 1.1.1	
				minLbl = new JLabel("Minimum Payoff: " +  (new Integer(min)).toString());
				maxLbl = new JLabel("Maximum Payoff: " + (new Integer(max)).toString());
				avgLbl = new JLabel("Average Payoff: " + (new Double(avg)).toString());
				*/

		//HTML Labels
		minLbl = new JLabel("<html><font color=#6B238E>Minimum Payoff: " +  (new Integer(min)).toString() + "</font>");
		maxLbl = new JLabel("<html><font color=#CFB53B>Maximum Payoff: " + (new Integer(max)).toString() + "</font>" );
		avgLbl = new JLabel("<html><font color=#215E21>Average Payoff: " + (new Double(avg)).toString() + "</font>");
	
		statsPanel = new JPanel();
		// Add border around the panel.
		statsPanel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createTitledBorder("Population Fitness Stats"), 
			BorderFactory.createEmptyBorder(2,2,2,2)));
		statsPanel.setLayout(new GridLayout(3,1,2,2));	
		statsPanel.add(minLbl);
		statsPanel.add(maxLbl);
		statsPanel.add(avgLbl);
		
	
		//BUTTONS
		startBtn = new JButton("Start");
		startBtn.setMnemonic(KeyEvent.VK_T);
		startBtn.setActionCommand("Start_S");
		startBtn.addActionListener(this);
		
		stopBtn = new JButton("Stop");
		stopBtn.setMnemonic(KeyEvent.VK_P);
		stopBtn.setActionCommand("Stop_S");
		stopBtn.addActionListener(this);
		
		fitBtn = new JButton("View Fittest Individual");
		fitBtn.setMnemonic(KeyEvent.VK_F);
		fitBtn.setActionCommand("Fit_S");
		fitBtn.addActionListener(this);
		
		weakBtn = new JButton("View Weakest Individual");
		weakBtn.setMnemonic(KeyEvent.VK_W);
		weakBtn.setActionCommand("Weak_S");
		weakBtn.addActionListener(this);
		
		buttonPanel = new JPanel();
		// Add border around the button panel.
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		buttonPanel.setLayout(new GridLayout(2,2,2,2));
		stopBtn.setEnabled(false);
		startBtn.setEnabled(true);
		buttonPanel.add(startBtn);
		buttonPanel.add(stopBtn);	
		weakBtn.setEnabled(false);
		fitBtn.setEnabled(false);
		buttonPanel.add(fitBtn);
		buttonPanel.add(weakBtn);
		
		
		//TYPE PERCENTAGES
		type0 = new JLabel("<html><font color=" + color[0] + ">" + typeNames[0] + ": " + (new Double(0)).toString() + " %" + "</font>");
		type1 = new JLabel("<html><font color=" + color[1] + ">" + typeNames[1] + ": " + (new Double(0)).toString() + " %" + "</font>");
		type2 = new JLabel("<html><font color=" + color[2] + ">" + typeNames[2] + ": "  + (new Double(0)).toString() + " %" + "</font>");
		type3 = new JLabel("<html><font color=" + color[3] + ">" + typeNames[3] + ": "  + (new Double(0)).toString() + " %" + "</font>");
		type4 = new JLabel("<html><font color=" + color[4] + ">" + typeNames[4] + ": "  + (new Double(0)).toString() + " %" + "</font>");
		type5 = new JLabel("<html><font color=" + color[5] + ">" + typeNames[5] + ": "  + (new Double(0)).toString() + " %" + "</font>");
		type6 = new JLabel("<html><font color=" + color[6] + ">" + typeNames[6] + ": "  + (new Double(0)).toString() + " %" + "</font>");
		type7 = new JLabel("<html><font color=" + color[7] + ">" + typeNames[7] + ": "  + (new Double(0)).toString() + " %" + "</font>");
			
		
		typePanel = new JPanel();
		// Add border around the type panel.
		typePanel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createTitledBorder("Player Type"), 
			BorderFactory.createEmptyBorder(2,2,2,2)));
		typePanel.setLayout(new BoxLayout(typePanel, BoxLayout.Y_AXIS));
		
		typePanel.add(type0);
		typePanel.add(type1);
		typePanel.add(type2);
		typePanel.add(type3);
		typePanel.add(type4);
		typePanel.add(type5);
		typePanel.add(type6);
		typePanel.add(type7);
		JPanel space = new JPanel();
		typePanel.add(space);
		
		
		//ACTION PANEL
		actionPanel.add(typePanel);
		actionPanel.add(statsPanel);
		actionPanel.add(radioPanel);
		actionPanel.add(buttonPanel);
	}
	
	/**
	 *ActionListener listens for events
	 */
	public void actionPerformed(ActionEvent e) 
    {	
    	if ("Start_S".equals(e.getActionCommand()))
		{
			start();
		}
		else if ("Stop_S".equals(e.getActionCommand()))
		{
			stop();
		}
		else if ("Fit_S".equals(e.getActionCommand()))
		{
			fitDlg.showDlg();	
		}
		else if ("Weak_S".equals(e.getActionCommand()))
		{
			weakDlg.showDlg();	
		}
		else if ("Genetic".equals(e.getActionCommand()))
		{
			GA = true;	
		}	
		else if ("Evolutionary".equals(e.getActionCommand()))
		{
			GA = false;
		}		
	}
	
	/**
	 *Start Evolution
	 *<BR>Runs in a seperated thread so GUI remains responsive
	 */
	public void start()
	{
		if(stopped == true)
		{
			//Separate thread for computationally intensive evolution
	    	worker = new SwingWorker() 
			{
				public Object construct()
		        {
		        	int maxIndexA, minIndexA, maxIndexB, minIndexB;
		        	
		        	boolean mate = GA;	//capture current settings
		        	Rules r1 = rules;	//capture current settings
					int gen = rules.getGenerations();
					int numP = rules.getNumPlayers();
					
					//Clear old data		
					grid.clear();	
					graphPanel.clear();
					graphPanel.setMax(8*r1.getIterations()*r1.getT());
					prisTypes = new double[]{0,0,0,0,0,0,0,0};
					
					/*Non HTML labels
					minLbl.setText("Minimum Payoff: " + (new Integer(0)).toString());
					maxLbl.setText("Maximum Payoff: " + (new Integer(0)).toString());
					avgLbl.setText("Average Payoff: " + (new Double(0)).toString());
					*/
					
					//HTML labels
					minLbl.setText("<html><font color=#6B238E>Minimum Payoff: " +  (new Integer(0)).toString() + "</font>");
					maxLbl.setText("<html><font color=#CFB53B>Maximum Payoff: " + (new Integer(0)).toString() + "</font>" );
					avgLbl.setText("<html><font color=#215E21>Average Payoff: " + (new Double(0)).toString() + "</font>");
		
					type0.setText("<html><font color=" + color[0] +">" + typeNames[0] + ": " + (new Double(0)).toString() + " %" + "</font>");
					type1.setText("<html><font color=" + color[1] +">" + typeNames[1] + ": " + (new Double(0)).toString() + " %" + "</font>");
					type2.setText("<html><font color=" + color[2] +">" + typeNames[2] + ": " + (new Double(0)).toString() + " %" + "</font>");
					type3.setText("<html><font color=" + color[3] +">" + typeNames[3] + ": " + (new Double(0)).toString() + " %" + "</font>");
					type4.setText("<html><font color=" + color[4] +">" + typeNames[4] + ": " + (new Double(0)).toString() + " %" + "</font>");
					type5.setText("<html><font color=" + color[5] +">" + typeNames[5] + ": " + (new Double(0)).toString() + " %" + "</font>");
					type6.setText("<html><font color=" + color[6] +">" + typeNames[6] + ": " + (new Double(0)).toString() + " %" + "</font>");
					type7.setText("<html><font color=" + color[7] +">" + typeNames[7] + ": " + (new Double(0)).toString() + " %" + "</font>");
			
					
					Prisoner[][] pris = Prisoner.getRand(numP,numP);
					
					grid.setRules(r1);
					grid.setPlayers(pris);
					grid.Play(); //initialisation **may be slow with >75^2 players
					int [][] scores;
					//Evolve the set number of generations
		           	for(int g = 0; g < gen; g++)
					{	
						if(stopped)		//if stop clicked end
						{	
							startBtn.setEnabled(true);
							ea.setEnabled(true);
							ga.setEnabled(true);		
							return pris;
						}							
	
						//***** reporting *******************
						prisTypes = new double[]{0,0,0,0,0,0,0,0};
						scores = grid.getScores();
						min = scores[0][0];
						max = scores[0][0];
						minIndexA = 0;
						minIndexB = 0;
						maxIndexA = 0;
						maxIndexB = 0;
						total = 0;
						for(int i = 0; i < scores.length; i++)
						{
							for(int j = 0; j < scores[0].length; j++)
							{
								if(stopped)		//if stop clicked end
								{	
									startBtn.setEnabled(true);
									ea.setEnabled(true);
									ga.setEnabled(true);	
									return pris;
								}
								if(scores[i][j] < min)
								{
									min = scores[i][j];
									minIndexA = i;
									minIndexB = j;
								}
								if(scores[i][j] > max)
								{
									max = scores[i][j];
									maxIndexA = i;
									maxIndexB = j;
								}
								total += scores[i][j];
								
								prisTypes[pris[i][j].getType()]	= prisTypes[pris[i][j].getType()] + 1;	
							}
						}
						avg = total/(scores.length*scores[0].length);
						graphPanel.addData(min,max,avg);
						
						type0.setText("<html><font color=" + color[0] +">" + typeNames[0] + ": " + (new Float((prisTypes[0]/(double)(numP*numP))*100)).toString() + " %" + "</font>");
						type1.setText("<html><font color=" + color[1] +">" + typeNames[1] + ": " + (new Float((prisTypes[1]/(double)(numP*numP))*100)).toString() + " %" + "</font>");
						type2.setText("<html><font color=" + color[2] +">" + typeNames[2] + ": " + (new Float((prisTypes[2]/(double)(numP*numP))*100)).toString() + " %" + "</font>");
						type3.setText("<html><font color=" + color[3] +">" + typeNames[3] + ": " + (new Float((prisTypes[3]/(double)(numP*numP))*100)).toString() + " %" + "</font>");
						type4.setText("<html><font color=" + color[4] +">" + typeNames[4] + ": " + (new Float((prisTypes[4]/(double)(numP*numP))*100)).toString() + " %" + "</font>");
						type5.setText("<html><font color=" + color[5] +">" + typeNames[5] + ": " + (new Float((prisTypes[5]/(double)(numP*numP))*100)).toString() + " %" + "</font>");
						type6.setText("<html><font color=" + color[6] +">" + typeNames[6] + ": " + (new Float((prisTypes[6]/(double)(numP*numP))*100)).toString() + " %" + "</font>");
						type7.setText("<html><font color=" + color[7] +">" + typeNames[7] + ": " + (new Float((prisTypes[7]/(double)(numP*numP))*100)).toString() + " %" + "</font>");
						
						
						//Have now calculated player fitnesses, allow user to view prisoners
						weakDlg.setStrat(pris[minIndexA][minIndexB]);
						fitDlg.setStrat(pris[maxIndexA][maxIndexB]);
						weakBtn.setEnabled(true);
						fitBtn.setEnabled(true);
						
						/*Non HTML Labels
						minLbl.setText("Minimum Payoff: " + (new Double((double)min/(8*(double)r1.getIterations()))).toString());
						maxLbl.setText("Maximum Payoff: " + (new Double((double)max/(8*(double)r1.getIterations()))).toString());
						avgLbl.setText("Average Payoff: " + (new Double((double)avg/(8*(double)r1.getIterations()))).toString());
						*/
						//HTML Labels
						minLbl.setText("<html><font color=#6B238E>Minimum Payoff: " +  (new Float((double)min/(8*(double)r1.getIterations()))).toString() + "</font>");
						maxLbl.setText("<html><font color=#CFB53B>Maximum Payoff: " + (new Float((double)max/(8*(double)r1.getIterations()))).toString() + "</font>" );
						avgLbl.setText("<html><font color=#215E21>Average Payoff: " + (new Float((double)avg/(8*(double)r1.getIterations()))).toString() + "</font>");
						//***** reporting end *******
						
						//Evolve a generation
						if(!mate)
						{
							//do a large number of increments to increase speed
							for(int m = 0; m < (numP*numP)/4; m++)
							{	
								if(stopped)		//if stop clicked end
								{	
									startBtn.setEnabled(true);
									ea.setEnabled(true);
									ga.setEnabled(true);		
									return pris;
								}
								grid.Evolve();	//use the evolutionary Algorithm
							}
						}
						else
						{
							for(int m = 0; m < (numP*numP)/10; m++)
							{
								if(stopped)		//if stop clicked end
								{	
									startBtn.setEnabled(true);
									ea.setEnabled(true);
									ga.setEnabled(true);		
									return pris;
								}
								grid.Mate(); //use the Genetic Algorithm
							}
						}
						grid.repaint();
					}
					//Finished - Enable/Disable Buttons as required
					stopped = true;
					stopBtn.setEnabled(false);
					startBtn.setEnabled(true);
					ea.setEnabled(true);
					ga.setEnabled(true);
		            return pris;
		    	}
			};
			//Starting - Enable/Disable Buttons as required
			stopBtn.setEnabled(true);
			startBtn.setEnabled(false);
			ea.setEnabled(false);
			ga.setEnabled(false);
			stopped = false;
			worker.start();	//Start evolution in background thread
		}  		
	}
    
	/**
	 *Stops Evolution
	 *<BR>Ensures background thread is closed
	 */
	public void stop()
	{
		stopped = true;
		//Finished - Enable/Disable Buttons as required
		stopBtn.setEnabled(false);
	}
	
	/**
	 *Set the {@link  ie.errity.pd.Rules Rules}
	 *@param r new {@link  ie.errity.pd.Rules Rules}
	 */
	public void setRules(Rules r)
	{
		rules = r;
	}
}