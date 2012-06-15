package ie.errity.pd.graphics;

import ie.errity.pd.*;
import ie.errity.pd.graphics.*;
import ie.errity.pd.genetic.*;
//Import GUI Components
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.plaf.metal.*;

/**
 *A Panel displaying the GUI for viewing and manipulating IPD 
 *{@link  ie.errity.pd.Tournament Tournament} Evolution.
 *<BR>Displays an abstract view of the current tournament competitors, 
 *a summary of population percentages and score statistics. Also displays a 
 *{@link  ie.errity.pd.graphics.GraphPanel graph} charting scores from 
 *generation to generation. Provides user with controls to start and stop 
 *evolution and view {@link  ie.errity.pd.Prisoner Prisoners}.
 *@author Andrew Errity 99086921
 */
public class TournamentPanel extends JPanel implements ActionListener
{
	//Swing Components
	private JPanel actionPanel, displayPanel, subPanel, typePanel, buttonPanel, radioPanel, statsPanel; 	
	private JLabel maxLbl, minLbl, avgLbl;
	private JButton startBtn, stopBtn, fitBtn, weakBtn;
	private StrategyDialog fitDlg, weakDlg;
	private GraphPanel graphPanel;
	private SwingWorker worker;
	
	private Rules rules;

	private boolean stopped;
	private int max, min, total;
	private double avg;
	private Breeder b1;	//responsible for displaying the prisoners and evolution
	
	private double [] prisTypes;
	private JLabel type0,type1,type2,type3,type4,type5,type6,type7;
	private String [] color;
	private String [] typeNames;
	
	private MenuFrame mFrame; //Master Frame
	
	/**
	 *Create new Tournament panel
	 *@param mf 	application master frame
	 */
	public TournamentPanel(MenuFrame mf)
	{
		//init
		mFrame = mf;
		stopped = true;
		
		GridBagLayout gridbag = new GridBagLayout();
	    GridBagConstraints c = new GridBagConstraints();
		rules = new Rules();
		actionPanel = new JPanel();
		
		displayPanel = new JPanel();
		graphPanel = new GraphPanel();
		subPanel = new JPanel();
		subPanel.setLayout(new GridLayout(1,2,5,5));
		
		b1 = new Breeder();
		max = 0;
		min = 0;
		avg = 0;
		
		fitDlg = new StrategyDialog(mf,"Fittest");
		weakDlg = new StrategyDialog(mf,"Weakest");
		
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
		//Add the prisoner display component
		displayPanel.add(b1,BorderLayout.CENTER);
	}
	
	/**
	 *Build the Graph Panel
	 */
	private void graphP()
	{
		// Add border around the display panel.
		graphPanel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createTitledBorder("Graph"), 
			BorderFactory.createEmptyBorder(5,5,5,5)));
		graphPanel.setLabels("Min Payoff","Max Payoff", "Average Payoff","Generation");
		graphPanel.setColors(new Color(0x6B,0x23,0x8E), new Color(0xCF,0xB5,0x3B), new Color(0x21,0x5E,0x21));//purple, blue, green
	}
	
	/**
	 *Build the Action Panel
	 */
	private void actionP()
	{		
		actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
			
		
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
		startBtn.setActionCommand("Start_T");
		startBtn.addActionListener(this);	
		stopBtn = new JButton("Stop");
		stopBtn.setMnemonic(KeyEvent.VK_P);
		stopBtn.setActionCommand("Stop_T");
		stopBtn.addActionListener(this);	
		fitBtn = new JButton("View Fittest Individual");
		fitBtn.setMnemonic(KeyEvent.VK_F);
		fitBtn.setActionCommand("Fit_T");
		fitBtn.addActionListener(this);	
		weakBtn = new JButton("View Weakest Individual");
		weakBtn.setMnemonic(KeyEvent.VK_W);
		weakBtn.setActionCommand("Weak_T");
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
		actionPanel.add(buttonPanel);
	}
	
	/**
	 *ActionListener listens for events
	 */
    public void actionPerformed(ActionEvent e) 
    {	
    	if ("Start_T".equals(e.getActionCommand()))
		{
			start();
		}
		else if ("Stop_T".equals(e.getActionCommand()))
		{
			stop();
		}
		else if ("Fit_T".equals(e.getActionCommand()))
		{
			fitDlg.showDlg();	
		}
		else if ("Weak_T".equals(e.getActionCommand()))
		{
			weakDlg.showDlg();	
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
		        	//init        	
		        	int maxIndex, minIndex;
		        	//capture current settings
					int gen = rules.getGenerations();
					int numP = rules.getNumPlayers();
					Rules r1 = rules;
					graphPanel.setMax(numP*r1.getIterations()*r1.getT());
					//Clear old data	
					graphPanel.clear();
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
			
					b1.setRules(r1);
					Prisoner[] pris = Prisoner.getRand(numP);
				
					Tournament t1;
		           	for(int i = 0; i < gen; i++)
					{	
						if(stopped)		//if stop clicked end
						{	
							startBtn.setEnabled(true);		
							return pris;
						}
	
						t1 = new Tournament(pris,r1);
						t1.Play();
						
						//***** reporting *******************
						prisTypes = new double[]{0,0,0,0,0,0,0,0};
						prisTypes[pris[0].getType()] = prisTypes[pris[0].getType()] + 1;
						maxIndex = 0;
						minIndex = 0;
						min = pris[0].getScore();
						max = pris[0].getScore();
						total = pris[0].getScore();
						for(int j = 1; j < numP; j++)
						{
							if(stopped)		//if stop clicked end
							{	
								startBtn.setEnabled(true);			
								return pris;
							}
							if(pris[j].getScore() < min)
							{
								min = pris[j].getScore();
								minIndex = j;
							}
							if(pris[j].getScore() > max)
							{
								max = pris[j].getScore();
								maxIndex = j;
							}
							total += pris[j].getScore();
						
							prisTypes[pris[j].getType()]	= prisTypes[pris[j].getType()] + 1;
						}
						avg = total/numP;
						graphPanel.addData(min,max,avg);
						
						
						type0.setText("<html><font color=" + color[0] +">" + typeNames[0] + ": " + (new Float((prisTypes[0]/(double)numP)*100)).toString() + " %" + "</font>");
						type1.setText("<html><font color=" + color[1] +">" + typeNames[1] + ": " + (new Float((prisTypes[1]/(double)numP)*100)).toString() + " %" + "</font>");
						type2.setText("<html><font color=" + color[2] +">" + typeNames[2] + ": " + (new Float((prisTypes[2]/(double)numP)*100)).toString() + " %" + "</font>");
						type3.setText("<html><font color=" + color[3] +">" + typeNames[3] + ": " + (new Float((prisTypes[3]/(double)numP)*100)).toString() + " %" + "</font>");
						type4.setText("<html><font color=" + color[4] +">" + typeNames[4] + ": " + (new Float((prisTypes[4]/(double)numP)*100)).toString() + " %" + "</font>");
						type5.setText("<html><font color=" + color[5] +">" + typeNames[5] + ": " + (new Float((prisTypes[5]/(double)numP)*100)).toString() + " %" + "</font>");
						type6.setText("<html><font color=" + color[6] +">" + typeNames[6] + ": " + (new Float((prisTypes[6]/(double)numP)*100)).toString() + " %" + "</font>");
						type7.setText("<html><font color=" + color[7] +">" + typeNames[7] + ": " + (new Float((prisTypes[7]/(double)numP)*100)).toString() + " %" + "</font>");
									
						//Have now calculated player fitnesses, allow user to view prisoners
						weakDlg.setStrat(pris[minIndex]);
						fitDlg.setStrat(pris[maxIndex]);
						weakBtn.setEnabled(true);
						fitBtn.setEnabled(true);
						
						/*Non HTML labels
						minLbl.setText("Minimum Payoff: " + (new Double((double)min/(numP*(double)r1.getIterations()))).toString());
						maxLbl.setText("Maximum Payoff: " + (new Double((double)max/(numP*(double)r1.getIterations()))).toString());
						avgLbl.setText("Average Payoff: " + (new Double((double)avg/(numP*(double)r1.getIterations()))).toString());
						*/
						
						//HTML Labels
						minLbl.setText("<html><font color=#6B238E>Minimum Payoff: " +  (new Float((double)min/(numP*(double)r1.getIterations()))).toString() + "</font>");
						maxLbl.setText("<html><font color=#CFB53B>Maximum Payoff: " + (new Float((double)max/(numP*(double)r1.getIterations()))).toString() + "</font>" );
						avgLbl.setText("<html><font color=#215E21>Average Payoff: " + (new Float((double)avg/(numP*(double)r1.getIterations()))).toString() + "</font>");
							
						//**** reporting end *************	
						//Evolve a generation
						pris = b1.Breed(pris);
					}
					//Finished - Enable/Disable Buttons as required
					stopped = true;
					stopBtn.setEnabled(false);
					startBtn.setEnabled(true);
		            return pris;
		    	}
		    };
			//Starting - Enable/Disable Buttons as required
			stopBtn.setEnabled(true);
			startBtn.setEnabled(false);
			stopped = false;
			worker.start(); //Start evolution in background thread
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

