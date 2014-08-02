package nl.fw.swing.component;

import java.awt.Dimension;

import javax.swing.plaf.basic.BasicArrowButton;

import nl.fw.swing.ComponentSize;

/**
 * {@link BasicArrowButton} does not respect given sizes.
 * This arrow-button does.
 * <br>See also {@link ComponentSize}.
 * @author fred
 *
 */
@SuppressWarnings("serial")
public class ArrowButton extends BasicArrowButton {
	
	private ComponentSize sizer;
	
	/**
	 * See {@link BasicArrowButton#BasicArrowButton(int)}.
	 * @param direction
	 */
	public ArrowButton(int direction) {
		super(direction);
		sizer = new ComponentSize();
	}

	@Override
    public void setPreferredSize(Dimension d) {
        sizer.setPreferredSize(d);
    }

	@Override
    public void setMinimumSize(Dimension d) {
        sizer.setMinimumSize(d);
    }

	@Override
    public void setMaximumSize(Dimension d) {
        sizer.setMaximumSize(d);
    }

	@Override
    public Dimension getPreferredSize() {
        return sizer.getPreferredSize();
    }

	@Override
    public Dimension getMinimumSize() {
        return sizer.getMinimumSize();
    }

	@Override
    public Dimension getMaximumSize() {
        return sizer.getMaximumSize();
    }

	@Override
    public boolean isFocusTraversable() {
        return sizer.isFocusTraversable();
    }

    public void setFocusTraversable(boolean focusTraversable) {
    	sizer.setFocusTraversable(focusTraversable);
    }
    
}
