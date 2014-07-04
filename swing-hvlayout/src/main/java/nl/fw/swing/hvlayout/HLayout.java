package nl.fw.swing.hvlayout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingConstants;

/**
 * A layout manager that aligns components horizontally. Components can be
 * aligned left to right, right to left and centered.
 * HLayout is a modified copy of VLayout. See VLayout for more information.
 * <br> VLayout and HLayout need to be kept in sync to work.  
 * @author Fred
 *
 */
public class HLayout extends HVLayout {
	
	protected transient boolean reverse;
	protected transient boolean center;

	/**
	 * Creates a layout manager that aligns components in the reading direction,
	 * uses the {@link DefaultHvsize#getInstance()}.
	 */
	public HLayout() {
		this(null);
	}

	/**
	 * Creates a layout manager that aligns components in the reading direction.
	 */
	public HLayout(HVSize props) {
		this(null, SwingConstants.LEADING);
	}

	/**
	 * Creates a layout manager that aligns components horizontally,
	 * uses the {@link DefaultHvsize#getInstance()}.
	 * @param orientation Either {@link SwingConstants#LEADING} (normal reading direction), 
	 * {@link SwingConstants#CENTER} (layout in the middle) or {@link SwingConstants#TRAILING} (reverse reading direction).
	 */
	public HLayout(int orientation) {
		this(null, orientation);
	}

	/**
	 * Creates a layout manager that aligns components horizontally.
	 * @param props Used for {@link HVSize#getHorizontalGap()} between components.
	 * @param orientation Either {@link SwingConstants#LEADING} (normal reading direction), 
	 * {@link SwingConstants#CENTER} (layout in the middle) or {@link SwingConstants#TRAILING} (reverse reading direction).
	 */
	public HLayout(HVSize props, int orientation) {
		super(props);
		switch (orientation) {
			case SwingConstants.CENTER: center = true; break;
			case SwingConstants.TRAILING: reverse = true; break;
			default: break;
		}
	}

	 /** 
	  * @retrun Either {@link SwingConstants#LEADING} (normal reading direction), 
	  *{@link SwingConstants#CENTER} (layout in the middle) or {@link SwingConstants#TRAILING} (reverse reading direction).
	  */
	public int getOrientation() {
		
		if (center) {
			return SwingConstants.CENTER;
		}
		if (reverse) {
			return SwingConstants.TRAILING;
		}
		return SwingConstants.LEADING;
	}

	/**
	 * Used by other layout managers to get an indication of the 
	 * relative place of this layout.
	 */
	@Override
	public float getLayoutAlignmentX(Container target) {

		final boolean leftToRight = target.getComponentOrientation().isLeftToRight();
		float f = 0.25f;
		if (leftToRight && reverse) {
			f = 0.75f;
		} else if (!leftToRight && !reverse) {
			f = 0.75f;
		} else if (center) {
			f = 0.5f;
		}
		return f;
	}

	/**
	 * Returns a reasonable value for this layout.
	 */
	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0.25f;
	}

	/**
	 * The minimum size of this layout calculated by summing up
	 * all minimum sizes of the components in this layout.
	 */
	@Override
	public Dimension minimumLayoutSize(Container target) {

		return minimumLayoutSize(target, false);
	}

	/**
	 * The preferred size of this layout calculated by summing up
	 * all preferred sizes of the components in this layout.
	 */
	@Override
	public Dimension preferredLayoutSize(Container target) {
		
		return preferredLayoutSize(target, false);
	}

	/**
	 * The maximum size of this layout calculated by summing up
	 * all maximum sizes of the components in this layout.
	 * <br> Note that for resizing calculations in layoutContainer(), 
	 * the maximum width is always set to screenSize.width.
	 */
	@Override
	public Dimension maximumLayoutSize(Container target) {

		if (dmax != null)
			return new Dimension(dmax.width, dmax.height);

		int maxHeight = 0;
		synchronized (target.getTreeLock()) {
			Dimension cmax;
			for(int i = 0; i < parts.size(); i++) {
				cmax = parts.get(i).getMaximumSize();
				maxHeight = Math.max(maxHeight, Math.min(cmax.height, HVSize.MAX_HEIGHT));
			}
		}
		maxHeight += getInsets(target, true);
		// Set max width always to (fixed) max possible size so that mixed line-boxes like
		// left 	middle 		right
		// always have the correct space between them.
		final Dimension d = new Dimension(HVSize.MAX_WIDTH, maxHeight);
		dmax = new Dimension(d.width, d.height);
		return d;
	}

	/**
	 * Lays out the components as specified by their sizes.
	 * <br> Always tries to make the most use of the given area.
	 * <br> Some small calculations are done for vertical size to 
	 * allow components to use all available vertical room when it is available.
	 */
	@Override
	public void layoutContainer(Container target) {

		/*
		 * TODO: un-duplicate code with Vlayout#layoutContainer.
		 * A lot of code is similar, but not easy to share.
		 */

		if (center)
			reverse = false;
		boolean ltor = target.getComponentOrientation().isLeftToRight();
		if (reverse)
			ltor ^= true; // Faster than ltor = !ltor

		final Insets targetInsets = target.getInsets();
		//final int insetsWidth = targetInsets.right + targetInsets.left;
		final int insetsHeight = targetInsets.top + targetInsets.bottom;
		int maxWidth = target.getWidth();
		int maxHeight = target.getHeight() - insetsHeight;
		
		synchronized (target.getTreeLock()) {
			final Dimension dminimum = minimumLayoutSize(target); 
			final Dimension dpreferred = preferredLayoutSize(target);
			// respect the minimum sizes
			maxWidth = Math.max(maxWidth, dminimum.width);
			maxHeight = Math.max(maxHeight, dminimum.height - insetsHeight);
			final boolean smallest = maxWidth <= dminimum.width;
			final boolean shrink = !smallest && (maxWidth < dpreferred.width) && (varMinSize > 0);
			boolean grow = !smallest && !shrink && (maxWidth > dpreferred.width) && (varMaxSize > 0);

			Dimension cmin, cpref, cmax;
			int varMaxWidthUsed = varMaxSize;
			int maxWidthStatic = 0;
			final Map<Component, Integer> grownMaxWidth = new HashMap<Component, Integer>();
			if (grow) {
				for (int i = 0; i < parts.size(); i++) {
					final Component c = parts.get(i);
					cpref = c.getPreferredSize();
					cmax = c.getMaximumSize();
					if (cmax.width > cpref.width) {
						int extra = (int)((maxWidth - dpreferred.width) * ((float)cpref.width / varMaxSize));
						if (extra > cmax.width - cpref.width) {
							varMaxWidthUsed -= cpref.width;
							grownMaxWidth.put(c, cmax.width);
							maxWidthStatic += (cmax.width - cpref.width);
						}
					}
				} // for parts
				grow = (varMaxWidthUsed > 0);
			}
			final int maxGrowWidthToDivide = maxWidth - dpreferred.width - maxWidthStatic;

			int lineWidth = (ltor ? targetInsets.left : maxWidth - targetInsets.left);
			if (center && (maxWidth > dpreferred.width)) {
				if (ltor)
					lineWidth = (maxWidth / 2) - (dpreferred.width / 2) + targetInsets.left;
				else
					lineWidth = (maxWidth / 2) + (dpreferred.width / 2) - targetInsets.left;
			}
			
			Component c;
			for (int i = 0; i < parts.size(); i++) {
				if (reverse)
					c = parts.get(parts.size() - i - 1);
				else
					c = parts.get(i);
				cmin = c.getMinimumSize();
				cpref = c.getPreferredSize();
				cmax = c.getMaximumSize();
				
				int cheight = cmin.height;
				if (maxHeight > cmin.height) {
					if (maxHeight > cpref.height) {
						if (cmax.height > cpref.height) {
							cheight = Math.min(maxHeight, cmax.height);
						} else {
							cheight = cpref.height;
						}
					} else {
						cheight = Math.min(maxHeight, cpref.height); 
					}
				}
				
				int width = (smallest ? cmin.width : 
						grownMaxWidth.get(c) != null ? grownMaxWidth.get(c) : cpref.width);
				boolean hasStaticWidth = true;
				if (!smallest) {
					if (shrink && cmin.width < cpref.width) { 
						hasStaticWidth = false;
					} else if (grow && !(reverse || center) 
							&& cmax.width > cpref.width && !grownMaxWidth.containsKey(c)) {
						hasStaticWidth = false;
					}
				}
				if (!hasStaticWidth) {
					if (shrink) {
						// extra = space to divide * percentage of variable width of this component
						int extra = (int)((maxWidth - dminimum.width) * ((cpref.width - cmin.width) / (float)varMinSize));
						width = cmin.width + extra;
					}
					if (grow) {
						// extra = space to divide * percentage of variable width of this component
						int extra = (int)(maxGrowWidthToDivide * (cpref.width / (float)varMaxWidthUsed));
						width = cpref.width + extra;
					}
				}
				
				if (ltor) {
					c.setBounds(lineWidth, targetInsets.top, width, cheight);
					lineWidth += width + props.getHorizontalGap();
				} else {
					c.setBounds(lineWidth - width, targetInsets.top, width, cheight);
					lineWidth -= width + props.getHorizontalGap();
				}
			}
		}
	}

}