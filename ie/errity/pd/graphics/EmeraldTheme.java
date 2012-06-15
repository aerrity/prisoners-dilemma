package ie.errity.pd.graphics;

import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;

/**
 * This class implements a custom 'green' look and feel to replace the default
 * Java metal look and feel.
 * @author	Andrew Errity 99086921
 */
public class EmeraldTheme extends DefaultMetalTheme
{
	/**
	 *Returns the name of the theme. 
	 *@return	the theme's name
	 */
    public String getName() { return "Emerald"; }
    
    //green shades
    private final ColorUIResource c1 = new ColorUIResource(0x70, 0xDB, 0x93);
    private final ColorUIResource c2 = new ColorUIResource(0x2F, 0x4F, 0x2F);
    private final ColorUIResource c3 = new ColorUIResource(0x23, 0x8E, 0x23); 
    
    private final ColorUIResource c4 = new ColorUIResource(0x8F, 0xBC, 0x8F);
    private final ColorUIResource c5 = new ColorUIResource(0x99, 0xCC, 0x32);
    private final ColorUIResource c6 = new ColorUIResource(0xC0, 0xD9, 0xD9);
    
    //Functions overridden from DefaultMetalTheme class
    protected ColorUIResource getPrimary1() { return c1; }  
    protected ColorUIResource getPrimary2() { return c2; } 
    protected ColorUIResource getPrimary3() { return c3; } 
    
    protected ColorUIResource getSecondary1() { return c4; }
    protected ColorUIResource getSecondary2() { return c5; }
    protected ColorUIResource getSecondary3() { return c6; }
}
