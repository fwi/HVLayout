package nl.fw.swing.hvlayout;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

/**
 * A container that lays out components horizontally. This container
 * can be placed inside a scroller. 
 * Use the appropriate constructor methods to layout components
 * left to right, right to left or in the middle.
 * Note that this container can be placed inside another container.
 * E.g. to have a button at the left and the right, do something like:
 * {@code HBox line = new HBox();}
 * <br>{@code HBox left = new HBox();}
 * <br>{@code left.add(Button);}
 * <br>{@code HBox right = new HBox(HBox.OPPOSITELINEAXIS);}
 * <br>{@code right.add(Button);}
 * <br>{@code line.add(left);}
 * <br>{@code line.add(right);}
 * <br>{@code form.add(line);}
 * @author Fred
 *
 */
public class HBox extends JComponent implements Scrollable {
	
	private static final long serialVersionUID = 7841775222423395208L;

	public HBox() {
		this(null, SwingConstants.LEADING);
	}
	
	public HBox(HVSize props) {
		this(props, SwingConstants.LEADING);
	}

	public HBox(int orientation) {
		this(null, orientation);
	}

	/**
	 * Constructor to layout components horizontally.
	 * @param hvconf If null, {@link DefaultHvsize#getInstance()} is used.
	 * @param orientation Either {@link SwingConstants#LEADING} (normal reading direction), 
	 * {@link SwingConstants#CENTER} (layout in the middle) or {@link SwingConstants#TRAILING} (reverse reading direction).
	 */
	public HBox(HVSize props, int orientation) {
		super();
		super.setLayout(new HLayout(props, orientation));
	}

	@Override
	public HLayout getLayout() {
		return (HLayout) super.getLayout();
	}
	
	public void setHvConf(HVSize props) {
		getLayout().setHvprops(props);
	}
	
	public HVSize getHvConf() {
		return getLayout().getHvprops();
	}
	
	/**
	 * This overloaded method ensures that items aligned opposite the line axis
	 * remain orientated opposite the line axis.
	 */
	@Override
	public void applyComponentOrientation(final ComponentOrientation co) {
		// Code overloads code in Component and Container.applyComponentOrientation
		
        if (co == null) {
            throw new NullPointerException();
        }
        if (co != ComponentOrientation.LEFT_TO_RIGHT && co != ComponentOrientation.RIGHT_TO_LEFT) {
        	super.applyComponentOrientation(co);
        	return;
        }
		final boolean isLeftToRight = getComponentOrientation().isLeftToRight();
		if (co.isLeftToRight() == isLeftToRight)
			return;
		setComponentOrientation(co);
		for (int i = 0; i < getComponentCount(); i++) {
			final Component c = getComponent(i);
			final boolean cltor = c.getComponentOrientation().isLeftToRight();
			if (cltor != co.isLeftToRight())
				c.applyComponentOrientation(co);
			// If component was already aligned opposite the line-axis ...
			if (cltor == co.isLeftToRight()) {
				// ... then align it opposite the line-axis again.
				if (co.isLeftToRight())
					c.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
				else
					c.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			}
		}
	}
	
	/**
	 * To accomodate a scroller.
	 */
	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return this.getPreferredSize();
	}

	/**
	 * To accomodate a scroller.
	 */
	@Override
	public int getScrollableUnitIncrement(final Rectangle arg0, final int arg1, final int arg2) {
		return getHvConf().getLineHeightNoDepth();
	}

	/**
	 * To accomodate a scroller.
	 */
	@Override
	public int getScrollableBlockIncrement(final Rectangle arg0, final int arg1,
			final int arg2) {
		return getHvConf().getLineHeightNoDepth();
	}

	/**
	 * To accomodate a scroller.
	 */
	@Override
	public boolean getScrollableTracksViewportWidth() {
		if (this.getParent().getWidth() > this.getMinimumSize().width)
			return true;
		return false;
	}

	/**
	 * To accomodate a scroller.
	 */
	@Override
	public boolean getScrollableTracksViewportHeight() {
		if (this.getParent().getHeight() > this.getMinimumSize().height)
			return true;
		return false;
	}
	
	/**
	 * This (overloaded) convenience method allows adding components in the middle of
	 * a list of components (at the place indicated by index).
	 * @see java.awt.Container#add(java.awt.Component, int)
	 */
	public Component add(final Component comp, final int index) {
		 // The index is used as constraint and passed on to super().
		 // This makes it easier to insert a component in the HLayout
		 // or VLayout list of components.
		super.add(comp, Integer.valueOf(index));
		return comp;
	}
}
