package nl.fw.swing.hvlayout;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.Scrollable;
import javax.swing.border.EmptyBorder;

/**
 * A container that lays out components vertically (top to bottom). This container
 * can be placed inside a scroller.
 */
public class VBox extends JComponent implements Scrollable {
	
	private static final long serialVersionUID = -5778892883397662105L;

	/**
	 * Constructs a container that that lays out components from top to bottom,
	 * has no border and uses the {@link HVSize#getDefault()}.
	 */
	public VBox() {
		this(null, null);
	}

	/**
	 * Constructs a container that that lays out components from top to bottom,
	 * has no border.
	 */
	public VBox(HVSize props) {
		this(props, null);
	}

	public VBox(Insets borderInsets) {
		this(null, borderInsets);
	}

	/** 
	 * Constructs a container that that lays out components from top to bottom.
	 * @param borderInsets if not null then an empty border with the given insets is set.
	 */
	public VBox(HVSize props, Insets borderInsets) {
		super();
		super.setLayout(new VLayout(props));
		if (borderInsets != null) {
			setBorder(new EmptyBorder(borderInsets));
		}
	}

	@Override
	public VLayout getLayout() {
		return (VLayout) super.getLayout();
	}

	public void setHvprops(HVSize props) {
		getLayout().setHvprops(props);
	}
	
	public HVSize getHvprops() {
		return getLayout().getHvprops();
	}

	/**
	 * This overloaded method ensures that items aligned opposite the line axis
	 * remain orientated opposite the line axis.
	 */
	@Override
	public void applyComponentOrientation(ComponentOrientation co) {
		// Code overwrites code in Component and Container.applyComponentOrientation
		
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
			// Check if component is not orientated opposite the line axis
			if (cltor != co.isLeftToRight())
				c.applyComponentOrientation(co);
			if (cltor == co.isLeftToRight()) {
				// Component is orientated opposite the line axis
				if (co.isLeftToRight())
					c.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
				else
					c.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			}
		}
	}

	/** To accomodate a scroller */
	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return this.getPreferredSize();
	}

	/** To accomodate a scroller */
	@Override
	public int getScrollableUnitIncrement(final Rectangle arg0, final int arg1, 
			final int arg2) {
		return getHvprops().getLineHeightNoDepth();
	}

	/** To accomodate a scroller */
	@Override
	public int getScrollableBlockIncrement(final Rectangle arg0, final int arg1,
			final int arg2) {
		return getHvprops().getLineHeightNoDepth();
	}

	/** To accomodate a scroller */
	@Override
	public boolean getScrollableTracksViewportWidth() {
		if (this.getParent().getWidth() > this.getMinimumSize().width)
			return true;
		return false;
	}

	/** To accomodate a scroller */
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
	@Override
	public Component add(final Component comp, final int index) {
		super.add(comp, Integer.valueOf(index));
		return comp;
	}
	
}
