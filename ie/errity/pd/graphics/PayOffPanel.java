package ie.errity.pd.graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 *Custom JPanel which displays the current payoff matrix
 *as specified by the current {@link  ie.errity.pd.Rules Rules}
 *<BR>Payoff matrix is displayed as:
 *<BR>
 *<PRE>
 *          -------------------------
 * *Labels* |Cooperate    |Defect   |
 *-----------------------------------
 *|Cooperate|     R,R     |   S,T   |
 *-----------------------------------
 *|Defect   |     T,S     |   P,P   |
 *-----------------------------------
 *</PRE>
 *@author Andrew Errity 99086921
 */ 
public class PayOffPanel extends JPanel
{
	//Payoffs and their names
    private int t,r,s,p;
    private String tLbl, rLbl, sLbl, pLbl;
    
    /**
     *Create new panel which displays the current payoff matrix 
     *as specified by the current {@link  ie.errity.pd.Rules Rules}
     */
    public PayOffPanel()
    {
    	super();
    	//Default Payoffs and their names
    	t = 5;
    	r = 3;
    	s = 0;
    	p = 1;
    	tLbl = "T = Temptation to defect";
    	rLbl = "R = Reward for cooperation";
    	sLbl = "S = Sucker's payoff";
    	pLbl = "P = Defection punishment";

    }

	/**
	 *Called whenever the pane has to be repainted. 
	 *Draws the current payoff matrix.
	 */
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g); //paint background
	
		//Get coordinate limits
		Insets insets = getInsets();
      	int x0 = insets.left;
      	int y0 = insets.top;
	    int currentWidth = getWidth() - insets.left - insets.right;
	    int currentHeight = getHeight() - insets.top - insets.bottom;
 		int currentWidth3 = currentWidth/3;
 		int currentHeight3 = currentHeight/3;
 		
 		//Convert payoffs to strings
 		String T = (new Integer(t).toString());
 		String R = (new Integer(r).toString());
 		String S = (new Integer(s).toString());
 		String P = (new Integer(p).toString());
 		
 		//Draw matrix boxes
	    g.setColor(Color.WHITE);
	    g.fillRect(x0,y0+currentHeight3,currentWidth3*3,currentHeight3*2);
	    g.fillRect(x0+currentWidth3,y0,currentWidth3*2,currentHeight3*3);
	    g.setColor(Color.BLACK);
	    g.drawRect(x0,y0+currentHeight3,currentWidth3*3,currentHeight3*2);
	    g.drawRect(x0+currentWidth3,y0,currentWidth3*2,currentHeight3*3);  
	    g.drawLine(x0,y0+(currentHeight3*2),x0+(currentWidth3*3),y0+(currentHeight3*2));
	    g.drawLine(x0+ (currentWidth3*2),y0,x0+(currentWidth3*2),y0+(currentHeight3*3));
	    
	    //Draw Labels
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(new Font("Arial",Font.PLAIN,9));
		g2.drawString(rLbl,(x0+5),y0+15);
		g2.drawString(tLbl,(x0+5),y0+30);
		g2.drawString(sLbl,(x0+5),y0+45);
		g2.drawString(pLbl,(x0+5),y0+60);
		
		g2.setFont(new Font("Arial",Font.BOLD,12));
		g2.drawString("Co-operate",(x0+5)+currentWidth3,y0+15);
		g2.drawString("Defect",(x0+5)+(currentWidth3*2),y0+15);
		g2.drawString("Co-operate",(x0+5),(y0+15)+currentHeight3);
		g2.drawString("Defect",(x0+5),(y0+15)+(currentHeight3*2));
		g2.drawString("R=" + R + ", R=" + R,(x0+5)+currentWidth3,(y0+15)+currentHeight3);
		g2.drawString("S="+S+", T="+T,(x0+5)+(currentWidth3*2),(y0+15)+currentHeight3);
		g2.drawString("T="+T+", S="+S,(x0+5)+currentWidth3,(y0+15)+(currentHeight3*2));
		g2.drawString("P="+P+", P="+P,(x0+5)+(currentWidth3*2),(y0+15)+(currentHeight3*2));	
    }
    
    /**
     *Set payoffs
     *@param t1 the T value for the payoff matrix
	 *@param r1 the R value for the payoff matrix
	 *@param s1 the S value for the payoff matrix
	 *@param p1 the P value for the payoff matrix
	 */
    public void setPayoffs(int t1,int r1, int s1, int p1)
    {
    	t = t1;
    	r = r1;
    	s = s1;
    	p = p1;
    }
 
 	/**
 	 *Set names to describe the payoffs
 	 *@param t1 the T value name for the payoff matrix
	 *@param r1 the R value name for the payoff matrix
	 *@param s1 the S value name for the payoff matrix
	 *@param p1 the P value name for the payoff matrix
	 */
 	public void setLabels(String t1,String r1, String s1, String p1)
    {
    	tLbl = t1;
    	rLbl = r1;
    	sLbl = s1;
    	pLbl = p1;
    }
}

