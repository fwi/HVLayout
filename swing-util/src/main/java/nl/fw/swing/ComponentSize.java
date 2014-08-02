package nl.fw.swing;

import java.awt.Dimension;

/**
 * Stores component size in separate variable dimensions.
 * This can be used to overrule Swing components that return calculated (fixed) sizes.
 * One example is {@link nl.fw.swing.component.ArrowButton}
 * (copy code from there, lots of boilerplate). 
 * @author fred
 *
 */
public class ComponentSize {

	private Dimension pref, min, max;
	private boolean focusTraversable;
	
	public ComponentSize() {
		super();
		pref = min = max = pref = new Dimension(10, 10);
	}
	
    public void setPreferredSize(Dimension d) {
       pref = new Dimension(d.width, d.height);
    }

    public void setMinimumSize(Dimension d) {
        min = new Dimension(d.width, d.height);
    }

    public void setMaximumSize(Dimension d) {
        max = new Dimension(d.width, d.height);
    }

    public Dimension getPreferredSize() {
        return new Dimension(pref.width, pref.height);
    }

    public Dimension getMinimumSize() {
        return new Dimension(min.width, min.height);
    }

    public Dimension getMaximumSize() {
        return new Dimension(max.width, max.height);
    }
    
    public void setFocusTraversable(boolean focusTraversable) {
    	this.focusTraversable = focusTraversable;
    }

    public boolean isFocusTraversable() {
        return focusTraversable;
    }
}
