package ie.errity.pd.graphics;

import ie.errity.pd.*;
//Import GUI components
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.plaf.metal.*;


/**
 *A Dialog displaying a list with functions to allow the user to manage the list
 *@author Andrew Errity 99086921
 */
public class PrisonerDialog extends JPanel implements ActionListener
{
	//Swing Components
	JButton closeBtn, viewBtn, delBtn;
	JDialog prisDlg;
	JComboBox prisList;

	MenuFrame mframe; //Master frame

	/**
	 *Create a new {@link  ie.errity.pd.Prisoner Prisoner}  dialog
	 *@param frame 	the frame opening this dialog
	 *@param title	the dialog title
	 *@param s		the entries in the list
	 */
	public PrisonerDialog(final JFrame frame, String title, String[] s)
	{
		mframe = (MenuFrame) frame;
		String prisStrings []= s;
		
		// Create panel to display in the dialog
		setBorder(BorderFactory.createEmptyBorder(5,5,5,10));
		setLayout(new GridLayout(0,1));
		
		
        closeBtn = new JButton("Close");
        closeBtn.setMnemonic(KeyEvent.VK_C);
		closeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
            	mframe.saveStrats();
                prisDlg.setVisible(false);	//hide the dialog
            }
        });
        
        delBtn = new JButton("Delete");
        delBtn.setActionCommand("Delete");
        delBtn.setMnemonic(KeyEvent.VK_D);
        delBtn.addActionListener(this);
        
        viewBtn = new JButton("Edit");
        viewBtn.setActionCommand("Edit");
        viewBtn.setMnemonic(KeyEvent.VK_V);
        viewBtn.addActionListener(this);

		prisList = new JComboBox(prisStrings);
		prisList.setSelectedIndex(0);
		prisList.addActionListener(this);
		
		
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
		prisDlg = new JDialog(frame,title, true);
		prisDlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		prisDlg.setContentPane(this);
		
		//Organise the componets on the dialog
		GridBagLayout gridbag = new GridBagLayout();
	    GridBagConstraints c = new GridBagConstraints();
	    setLayout(gridbag);
	    c.fill = GridBagConstraints.BOTH; 
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 0;
		add(prisList);
		c.gridwidth = 0;
		c.gridx = 0;
		c.gridy = 1;
		add(viewBtn);
		c.gridwidth = 0;
		c.gridx = 1;
		c.gridy = 1;
		add(delBtn);
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 2;
		add(closeBtn);
	}


	/**
	 *Opens the dialog
	 */
	public void showDlg()
	{
			prisDlg.pack();
			prisDlg.setVisible(true);
	}
	
	/**
	 *Closes the dialog
	 */
	public void hideDlg()
	{
			prisDlg.setVisible(false);
	}
	
	/**
	 *An ActionListener listening for button presses
	 */	
	public void actionPerformed(ActionEvent e) 
    {	
    	if ("Delete".equals(e.getActionCommand()))
    	{
    		//Remove selected prisoner
    		int i = prisList.getSelectedIndex();
    		mframe.delete(i);	
    	}
    	else if("Edit".equals(e.getActionCommand()))
    	{
    		//View selected prisoner
    		mframe.view(prisList.getSelectedIndex(),prisDlg);
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
	}
}
	