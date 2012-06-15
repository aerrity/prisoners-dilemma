package ie.errity.pd.graphics;

import ie.errity.pd.*;
//Import GUI components
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.plaf.metal.*;


/**
 *A Dialog which allows the user to enter a strategy name as text
 *@author Andrew Errity 99086921
 */
public class SaveDialog extends JPanel 
{
	JButton closeBtn, saveBtn;
	JDialog sDlg;
	JTextField txtBox;
	String name;

	
	/**
	 *Create a new Dialog which allows the user to enter a strategy name as text
	 *@param frame	applications master frame
	 *@param d		the dialog which will open this dialog
	 *@param title	the dialog boxes title
	 */
	public SaveDialog(final JFrame frame,JDialog d, String title)
	{
		txtBox = new JTextField(20);
		name = "Please enter a strategy name";
		txtBox.setText(name);
		txtBox.selectAll();
		txtBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
                name = txtBox.getText();
                sDlg.setVisible(false);
            }
        });
		name = null;

		// Add border around the panel.
		setBorder(BorderFactory.createEmptyBorder(5,5,5,10));
		setLayout(new GridLayout(0,1));
		
		
        closeBtn = new JButton("Close");
		closeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
            	name = null;
                sDlg.setVisible(false);
            }
        });
        
        saveBtn = new JButton("Save");
        saveBtn.setActionCommand("Save");
        saveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
                name = txtBox.getText();
                sDlg.setVisible(false);
            }
        });
    
		add(txtBox);
		add(saveBtn);
		add(closeBtn);

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
		sDlg = new JDialog(d,title, true);
		sDlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		sDlg.setContentPane(this);
	}

	/**
	 *Opens the dialog
	 */
	public void showDlg()
	{
			sDlg.pack();
			sDlg.setVisible(true);
	}
	
	/**
	 *Closes the dialog
	 */
	public void hideDlg()
	{
			sDlg.setVisible(false);
	}
	
	/**
	 *Get the typed text
	 *@return the text entered
	 */
	public String getText()
	{
			return name;
	}	
}
	