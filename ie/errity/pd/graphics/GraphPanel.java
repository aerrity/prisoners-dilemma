package ie.errity.pd.graphics;

//Import graphics components
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 *A 2D Line graph, displaying data from 3 sources
 *@author	Andrew Errity 99086921
 */
public class GraphPanel extends JPanel
{
	//Maximum number of x values
    final private int length;
    
    //Maximum data value (used to scale the data)
    private int max;
    
    //Data displayed
    private int [] data0;
    private int [] data1;
    private double [] data2;
	
	//Labelling
	private Color col0,col1,col2;
	private String Label0,Label1,Label2, Labelx;
	
	//Used for graphing
    private int lastHeight0;
    private int lastHeight1;
    private int lastHeight2;
    private int pos;
	private int count;
    
    /**
     *Create new Graph
     */
    public GraphPanel()
    {
    	super();
    	count = 0;
    	max = 5;
    	length = 51;
    	lastHeight0 = 0;
    	lastHeight1 = 0;
    	lastHeight2 = 0;
    	pos =-1;	//index of current data being displayed
    	data0 = new int[length];
    	data1 = new int[length];
    	data2 = new double[length];
    	for(int i = 0; i < length; i++)
		{    		
    		data0[i] = -1; //-1 = no value
    		data1[i] = -1; //-1 = no value
    		data2[i] = -1; //-1 = no value
    	}
    	col0 = Color.RED;
    	col1 = Color.BLUE;
    	col2 = Color.GREEN;
    	Label0 = null;
    	Label1 = null;
    	Label2 = null;
    	Labelx = null;
    }


	/**
	 *Called anytime the Graph needs to be redrawn
	 */
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g); //paint background
    
    	//Get view limits
    	double frac,val, newHeight;
      	Insets insets = getInsets();
      	int x0 = insets.left;
      	int y0 = insets.top;
	    int currentWidth = getWidth() - insets.left - insets.right;
	    int currentHeight = getHeight() - insets.top - insets.bottom;
 
 		//Axis
 		g.setColor(Color.WHITE);
	    g.fillRect(x0,y0,((currentWidth/(length-1))*(length-1)),currentHeight-y0); //background
	    g.setColor(Color.BLACK);
		g.drawLine(x0,y0,x0,currentHeight);//y-axis
		g.drawLine(x0,currentHeight,x0+((currentWidth/(length-1))*(length-1)),currentHeight);//x-axis
		
		//Grid & Labels
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(new Font("Arial",Font.PLAIN,9));
		//X labels and lines
		for(int i = 1; i < length-1; i++)
		{
			g.setColor(Color.LIGHT_GRAY);
			g.drawLine(x0+((currentWidth/(length-1))*(i)),y0,x0+((currentWidth/(length-1))*(i)),currentHeight);
			g2.setColor(Color.BLACK);
			g2.drawString((new Integer(i+((length-1)*count))).toString(),(int)(x0/1.5)+((currentWidth/(length-1))*(i)),currentHeight+insets.bottom+3);
		}
		g2.setColor(Color.BLACK);
		g2.drawString((new Integer(length-1+((length-1)*count))).toString(),(int)(x0/1.5)+((currentWidth/(length-1))*(length-1)),currentHeight+insets.bottom+3);
		g2.setFont(new Font("Arial",Font.BOLD,14));
		g2.drawString(Labelx,(currentWidth/2)-25,currentHeight+insets.bottom+18);
	
		//Draw Data labels
		g2.setFont(new Font("Arial",Font.BOLD,11));
		if(Label0 != null)
		{
			g2.setColor(col0);
			g2.drawString(Label0,x0+((currentWidth/(length-1))*(length-7)),insets.top+12);
		}
		if(Label1 != null)
		{
			g2.setColor(col1);
			g2.drawString(Label1,x0+((currentWidth/(length-1))*(length-7)),insets.top+24);
		}
		if(Label2 != null)
		{
			g2.setColor(col2);
			g2.drawString(Label2,x0+((currentWidth/(length-1))*(length-7)),insets.top+36);
		}
	
		//Graph the data
		val = data0[0];
		frac = val/max;
		frac = frac * (currentHeight- insets.top);
		newHeight = currentHeight - frac ;
		lastHeight0 = (int) newHeight;
		
		val = data1[0];
		frac = val/max;
		frac = frac * (currentHeight- insets.top);
		newHeight = currentHeight - frac ;
		lastHeight1 = (int) newHeight;
		
		val = data2[0];
		frac = val/max;
		frac = frac * (currentHeight- insets.top);
		newHeight = currentHeight - frac ;
		lastHeight2 = (int) newHeight;
		
		for(int i = 1; i < length; i++)
		{
			if(data0[i] >= 0)
			{	
				g.setColor(col0);
				val = data0[i];
				frac = val/max;
				frac = frac * (currentHeight- insets.top);
				newHeight = currentHeight - frac ;
				
				g.drawLine(x0+((currentWidth/(length-1))*(i-1)),lastHeight0,x0+((currentWidth/(length-1))*(i)),(int) newHeight);
				lastHeight0 = (int) newHeight;
			}
			
			if(data1[i] >= 0)
			{	
				g.setColor(col1);
				val = data1[i];
				frac = val/max;
				frac = frac * (currentHeight- insets.top);
				newHeight = currentHeight - frac ;
				
				g.drawLine(x0+((currentWidth/(length-1))*(i-1)),lastHeight1,x0+((currentWidth/(length-1))*(i)),(int) newHeight);
				lastHeight1 = (int) newHeight;
			}
			
			if(data2[i] >= 0)
			{	
				g.setColor(col2);
				val = data2[i];
				frac = val/max;
				frac = frac * (currentHeight- insets.top);
				newHeight = currentHeight - frac ;
				
				g.drawLine(x0+((currentWidth/(length-1))*(i-1)),lastHeight2,x0+((currentWidth/(length-1))*(i)),(int) newHeight);
				lastHeight2 = (int) newHeight;
			}
		}
    }
    
    /**
     *Add values to the graph
     *@param d0	data to add to First plot
     *@param d1 data to add to Second plot
     *@param d2 data to add to Third plot
     */
    public void addData(int d0,int d1, double d2)
    {
    	pos++; //next x position
    	if(pos >= length) //if exceeding bounds
    	{
    		//Go to next 'length' x values
    		count++;
    		pos = 0;
    		data0[0] = data0[length-1]; //swap last element to front
    		data1[0] = data1[length-1]; //swap last element to front
    		data2[0] = data2[length-1]; //swap last element to front
    		//clear the rest
    		for(int i = 1; i < length; i++)
    		{
    			data0[i] = -1;
    			data1[i] = -1;
    			data2[i] = -1;
    		}	
    		pos++;
    	}
    	//Add the data  	
    	data0[pos] = d0;
    	data1[pos] = d1;
    	data2[pos] = d2;
    	
    	repaint(); //Redraw graph to reflect the changes
    }
    
    /**
     *Reset the graph
     */
    public void clear()
    {
    	pos =-1;
    	for(int i = 0; i < length; i++)
		{    		
    		data0[i] = -1; //-1 = no value
    		data1[i] = -1; //-1 = no value
    		data2[i] = -1; //-1 = no value
    	}
    	count = 0;
    	lastHeight0 = 0;
    	lastHeight1 = 0;
    	lastHeight2 = 0;
    	repaint(); //Redraw graph to reflect the changes
    }
    
    /**
     *Set the Y bounds, maximum data value possible
     *@param m 	maximum data value possible
     */
    public void setMax(int m)
    {
    	max = m;
    }
    
    /**
     *Set Colors used to display the data
     *@param c0	color of First plot
     *@param c1 color of Second plot
     *@param c2 color of Third plot
     */
    public void setColors(Color c0, Color c1, Color c2)
    {
    	col0 = c0;
    	col1 = c1;
    	col2 = c2;
    }
    
    /**
     *Set Labels used to describe the data
     *@param L0	Label for the First plot
     *@param L1 Label for the Second plot
     *@param L2 Label for the Third plot
     *@param x 	Label for X-axis
     */
    public void setLabels(String L0, String L1, String L2, String x)
    {
    	Label0 = L0;
    	Label1 = L1;
    	Label2 = L2;
    	Labelx = x;
    }
}

