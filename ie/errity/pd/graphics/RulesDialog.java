package ie.errity.pd.graphics;

import ie.errity.pd.*;
//Import GUI components
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.plaf.metal.*;
import java.io.*;
import java.util.Properties;

/**
 *A Dialog displaying a set of {@link  ie.errity.pd.Rules  Rules} with controls
 *to modify the {@link  ie.errity.pd.Rules  Rules}
 *@author Andrew Errity 99086921
 */
public class RulesDialog extends JPanel
{
	//Swing Components
	JSpinner maxGenSp, numPlayersSp,iterationsSp, mutateSp, crossoverSp,  temptationSp, rewardSp, suckerSp, punishSp;
	JLabel maxGenLbl, numPlayersLbl,mutateLbl, crossoverLbl, iterationsLbl, temptationLbl, rewardLbl, suckerLbl, punishLbl;
	JButton defaultBtn, saveBtn, restoreBtn;
	JDialog rulesDlg;
	
	//Rule settings
	private int gen, plyrs, itr, t, r, s, p;
	private double m, c;
	
	MenuFrame mf;

	/**
	 *Create a new Dialog displaying a set of {@link  ie.errity.pd.Rules  Rules}
	 *@param frame 	application master frame
	 */
	public RulesDialog(final MenuFrame frame)
	{
		mf = frame;
		// Add border around the display panel.
		setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createTitledBorder("Rules"), 
			BorderFactory.createEmptyBorder(5,5,5,5)));
		setLayout(new GridLayout(0,2));
		
		
		File file = new File("epdProperties.txt");
		boolean exist = file.exists();
		if(exist)
		{
			//load properties from file and assign to rules
			Properties props = new Properties();
			try
			{
				FileInputStream propStream = new FileInputStream("epdProperties.txt");
				props.load(propStream);
				propStream.close();
			}
			catch(Exception e){}
			
			if(props.getProperty("gen") != null)
				gen = Integer.parseInt(props.getProperty("gen"));
			else
				gen = 250;
			if(props.getProperty("plyrs") != null)
				plyrs = Integer.parseInt(props.getProperty("plyrs"));
			else
				plyrs = 50;
				
			if(props.getProperty("itr") != null)
				itr = Integer.parseInt(props.getProperty("itr"));
			else
				itr = 100;
					
			if(props.getProperty("t") != null)
				t = Integer.parseInt(props.getProperty("t"));
			else
				t = 5;
				
			if(props.getProperty("r") != null)
				r = Integer.parseInt(props.getProperty("r"));
			else
				r = 3;
				
			if(props.getProperty("s") != null)
				s = Integer.parseInt(props.getProperty("s"));
			else
				s = 0;
				
			if(props.getProperty("p") != null)
				p = Integer.parseInt(props.getProperty("p"));
			else
				p = 1;
			if(props.getProperty("m") != null)
				m = Double.parseDouble(props.getProperty("m"));
			else
				m = .001;
			if(props.getProperty("c") != null)
				c = Double.parseDouble(props.getProperty("c"));
			else
				c = .95;
		}
		else
		{
			//Set defaults
			gen = 250;
			plyrs = 50;
			itr = 100;
			t = 5;
			r = 3;
			s = 0;
			p = 1;
			m = .001;
			c = .95;	
		}
		

		SpinnerNumberModel model; 
		//Set-up spinners - parameters(current value, min, max, increments)
		model = new SpinnerNumberModel(gen, 0, 9999999, 1); 
		maxGenSp = new JSpinner(model);
		maxGenLbl = new JLabel("Maximum Generations:", SwingConstants.LEFT);
		
		model = new SpinnerNumberModel(plyrs, 2, 300, 1); 
		numPlayersSp = new JSpinner(model);
		numPlayersLbl = new JLabel("Number of Players:", SwingConstants.LEFT);
		
		model = new SpinnerNumberModel(itr, 1, 1000, 1); 
		iterationsSp = new JSpinner(model);
		iterationsLbl = new JLabel("PD Iterations:", SwingConstants.LEFT);
		
		model = new SpinnerNumberModel(t, 0, 10000, 1); 
		temptationSp  = new JSpinner(model);
		temptationLbl = new JLabel("Temptation:", SwingConstants.LEFT);
		
		model = new SpinnerNumberModel(r, 0, 10000, 1); 
		rewardSp  = new JSpinner(model);
		rewardLbl = new JLabel("Reward:", SwingConstants.LEFT);
		
		model = new SpinnerNumberModel(p, 0, 10000, 1); 
		punishSp  = new JSpinner(model);
		punishLbl = new JLabel("Punishment:", SwingConstants.LEFT);
		
		model = new SpinnerNumberModel(s, 0, 10000, 1); 
		suckerSp  = new JSpinner(model);
		suckerLbl = new JLabel("Sucker's Payoff:", SwingConstants.LEFT);
		
		model = new SpinnerNumberModel(m, 0, 1, .001); 
		mutateSp  = new JSpinner(model);
		mutateLbl = new JLabel("Mutate Probability:", SwingConstants.LEFT);
		
		model = new SpinnerNumberModel(c, 0, 1, .01); 
		crossoverSp  = new JSpinner(model);
		crossoverLbl = new JLabel("Crossover Probability:", SwingConstants.LEFT);
		
		defaultBtn = new JButton("Defaults");
		defaultBtn.setMnemonic(KeyEvent.VK_D);
		defaultBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
            	maxGenSp.setValue(new Integer(250));
            	numPlayersSp.setValue(new Integer(50));
                iterationsSp.setValue(new Integer(100));
                temptationSp.setValue(new Integer(5));
                rewardSp.setValue(new Integer(3));
                punishSp.setValue(new Integer(1));
                suckerSp.setValue(new Integer(0));
                crossoverSp.setValue(new Double(.95));
                mutateSp.setValue(new Double(.001));
            }
        });
        
        saveBtn = new JButton("Save Changes");
        saveBtn.setMnemonic(KeyEvent.VK_S);
		saveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
            	save();
                rulesDlg.setVisible(false);
            }
        });
        
        restoreBtn = new JButton("Cancel");
        restoreBtn.setMnemonic(KeyEvent.VK_C);
		restoreBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
            	maxGenSp.setValue(new Integer(gen));
            	numPlayersSp.setValue(new Integer(plyrs));
                iterationsSp.setValue(new Integer(itr));
                temptationSp.setValue(new Integer(t));
                rewardSp.setValue(new Integer(r));
                punishSp.setValue(new Integer(p));
                suckerSp.setValue(new Integer(s));
                crossoverSp.setValue(new Double(c));
                mutateSp.setValue(new Double(m));
                rulesDlg.setVisible(false);
            }
        });

		this.add(maxGenLbl);
		this.add(maxGenSp);
		this.add(numPlayersLbl);
		this.add(numPlayersSp);
		this.add(iterationsLbl);
		this.add(iterationsSp);
		this.add(temptationLbl);
		this.add(temptationSp);
		this.add(rewardLbl);
		this.add(rewardSp);
		this.add(punishLbl);
		this.add(punishSp);
		this.add(suckerLbl);
		this.add(suckerSp);
		this.add(mutateLbl);
		this.add(mutateSp);
		this.add(crossoverLbl);
		this.add(crossoverSp);
		this.add(saveBtn);
		this.add(restoreBtn);
		this.add(defaultBtn);
		
		
		// THEMES
		// user selected theme
		MetalTheme theme = new EmeraldTheme();  
		// set the chosen theme
		MetalLookAndFeel.setCurrentTheme(theme);		
		try
		{
		    UIManager.setLookAndFeel(
			UIManager.getCrossPlatformLookAndFeelClassName());
		    SwingUtilities.updateComponentTreeUI(frame);
		}
		catch(Exception e){}
			
		JDialog.setDefaultLookAndFeelDecorated(true);
		rulesDlg = new JDialog(frame,"Rules", true);
		rulesDlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		rulesDlg.setContentPane(this);
	}

	/**
	 *Get number of players
	 *@return number of players set
	 */
	public int getNumP(){ return plyrs; }
	/**
	 *Get number of generations
	 *@return number of generations set
	 */
	public int getGen(){ return gen; }
	
	/**
	 *Get {@link  ie.errity.pd.Rules  Rules}
	 *@return {@link  ie.errity.pd.Rules  Rules}
	 */
	public Rules getRules()
	{ 
		
		return new Rules(itr, t, s, r, p, m, c);
	}
	
	/**
	 *Opens the dialog
	 */
	public void showDlg()
	{
			rulesDlg.pack();
			rulesDlg.setVisible(true);
	}
	
	/**
	 *Saves rules to epdProperties.txt in the current dir
	 */
	private void save()
	{
		gen = ((Integer)maxGenSp.getValue()).intValue();
    	plyrs = ((Integer)numPlayersSp.getValue()).intValue();
        itr = ((Integer)iterationsSp.getValue()).intValue();
        t =  ((Integer)temptationSp.getValue()).intValue();
        r =  ((Integer)rewardSp.getValue()).intValue();
        p =  ((Integer)punishSp.getValue()).intValue();
        s =  ((Integer)suckerSp.getValue()).intValue();
        c =  ((Double)crossoverSp.getValue()).doubleValue();
        m =  ((Double)mutateSp.getValue()).doubleValue();
       // Rules(int it, int t,int s, int r, int p, double m, double c, int gen, int pl)
        mf.setRules(new Rules(itr, t,s,r,p, m,c, gen, plyrs));
        
        
		//Write to properties file
		File file = new File("epdProperties.txt");
		boolean exist = file.exists();
		Properties props = new Properties();
		if(exist)
		{
			//load properties from file
			
			try
			{
				FileInputStream propStream = new FileInputStream("epdProperties.txt");
				props.load(propStream);
				propStream.close();
			}
			catch(Exception e){return;}
		}
		props.setProperty("gen", Integer.toString(gen));
		props.setProperty("plyrs", Integer.toString(plyrs));
		props.setProperty("itr", Integer.toString(itr));
		props.setProperty("t", Integer.toString(t));
		props.setProperty("r", Integer.toString(r));
		props.setProperty("s", Integer.toString(s));
		props.setProperty("p", Integer.toString(p));
		props.setProperty("m", Double.toString(m));
		props.setProperty("c", Double.toString(c));
		try
		{
			OutputStream propOut = new FileOutputStream(new File("epdProperties.txt"));
			props.store(propOut, "Evolutionary Prisoner's Dilemma Properties");
		}
		catch(Exception e){}
		
	}
	
	/**
	 *Set type of {@link  ie.errity.pd.Rules  Rules} set to allow (<code>Spatial</code> or <code>Tournament</code>)
	 *@param s	the type of {@link  ie.errity.pd.Rules  Rules} set to allow (<code>Spatial</code> or <code>Tournament</code>)
	 */
	public void type(String s)
	{
		SpinnerNumberModel model; 
		if(s.equals("Spatial"))
		{
			if(plyrs <= 60)
			{
				model = new SpinnerNumberModel(plyrs, 2, 70, 1);
				numPlayersSp.setModel(model);
			}
			else
			{
				model = new SpinnerNumberModel(50, 2, 70, 1);
				numPlayersSp.setModel(model);
				
				save();
			}
			
			numPlayersLbl.setText("Num of Players in each row & column:");
		}
		else
		{
				model = new SpinnerNumberModel(plyrs, 2, 200, 1); 
				numPlayersSp.setModel(model);
				numPlayersLbl.setText("Number of Players:");
		} 
	}	
		
}
	