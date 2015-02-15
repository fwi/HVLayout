package nl.fw.swing.hvlayout;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.Scrollable;

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

	/**
	 * See {@link HVLayout#LEADING}
	 */
	public static final int LEADING = HVLayout.LEADING; // 10
	/**
	 * See {@link HVLayout#TRAILING}
	 */
	public static final int TRAILING = HVLayout.TRAILING; // 11
	/**
	 * See {@link HVLayout#CENTER}
	 */
	public static final int CENTER = HVLayout.CENTER; // 0
	
	public HBox() {
		this(null, LEADING);
	}

	public HBox(HVSize props) {
		this(props, LEADING);
	}

	public HBox(int orientation) {
		this(null, orientation);
	}

	/**
	 * Constructor to layout components horizontally.
	 * @param props If null, {@link HVSize#getDefault()} is used.
	 * @param orientation Either {@link #LEADING} (normal reading direction), 
	 * {@link #CENTER} (layout in the middle) or {@link #TRAILING} (reverse reading direction).
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
	 * @param index if -1 component is added at he end of the components-list.
	 * @see java.awt.Container#add(java.awt.Component, Object, int)
	 */
	@Override
	public Component add(final Component comp, final int index) {
		super.add(comp, Integer.valueOf(index), index);
		return comp;
	}
}
