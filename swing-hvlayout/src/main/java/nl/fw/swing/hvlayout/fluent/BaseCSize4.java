package nl.fw.swing.hvlayout.fluent;

import static nl.fw.swing.hvlayout.CSizeUtils.grow;
import static nl.fw.swing.hvlayout.CSizeUtils.shrink;

import java.awt.Component;
import java.awt.Dimension;

import nl.fw.swing.hvlayout.HVSize;

/**
 * Adds functions that set a component size relative to {@link HVSize#getLineHeight()}.
 * @author fred
 */
public class BaseCSize4 <CSIZE extends BaseCSize4<CSIZE, CTYPE>, CTYPE extends Component> 
	extends BaseCSize3<CSIZE, CTYPE> {
	
	public static int MAX_WIDTH = HVSize.SCREEN_SIZE_TOTAL.width;
	public static int MAX_HEIGHT = HVSize.SCREEN_SIZE_TOTAL.height;


	@SuppressWarnings("unchecked")
	@Override
	protected CSIZE me() { return (CSIZE) this;	}

	/* *** Fixed size layout *** */

	public CSIZE setFixedHeight() {
		return setMinHeightLine().setMaxHeightLine();
	}

	public CSIZE setFixedHeight(float factor) {
		return setFixedHeight(getLineHeight(factor));
	}

	public CSIZE setFixedHeight(int h) {
		return setMinHeight(h).setMaxHeight(h);
	}

	public CSIZE setFixedWidth() {
		return setMinWidthButton().setMaxWidthButton();
	}

	public CSIZE setFixedWidth(float factor) {
		return setFixedWidth(getButtonWidth(factor));
	}

	public CSIZE setFixedWidth(int w) {
		return setMinWidth(w).setMaxWidth(w);
	}

	/**
	 * Sets a fixed size for a component to button-width and line-height.
	 */
	public CSIZE setFixed() { 
		return setFixed(props().getButtonWidth(), props().getLineHeight()); 
	}
	
	/**
	 * Sets a fixed size for a component scaled to button-width and line-height.
	 */
	public CSIZE setFixed(float widthFactor, float heightFactor) {
		return setFixed(getButtonWidth(widthFactor), getLineHeight(heightFactor));
	}

	public CSIZE setFixed(int w, int h) {
		return setFixed(dim(w, h));
	}

	public CSIZE setFixed(Dimension d) {
		return min(d).max(d).pref(d);
	}

	/**
	 * Sets a fixed size for a component
	 * @param width if 0, components preferred width is used.
	 * @param heightFactor scaled to line-height.
	 */
	public CSIZE setFixed(int width, float heightFactor) {
		return setFixed((width == 0 ? pref().width : width), getLineHeight(heightFactor));
	}

	/**
	 * Sets a fixed size for a component
	 * @param widthFactor applied to button-width
	 * @param height if 0, components preferred height is used.
	 */
	public CSIZE setFixed(float widthFactor, int height) {
		return setFixed(getButtonWidth(widthFactor), (height == 0 ? pref().height : height));
	}
	
	/* *** Variable line-width layout *** */
	
	/**
	 * Sets fixed height to line-height 
	 * and sets variable width to line-width.
	 */
	public CSIZE setLineSize() {
		return setLineSize(props().getLineWidth());
	}

	/**
	 * Sets fixed height to component's preferred height 
	 * and sets variable width to line-width.
	 */
	public CSIZE setLineSizeWithPrefHeight() {
		return setLineSize(props().getLineWidth(), pref().height);
	}

	/**
	 * Sets fixed height to line-height 
	 * and sets variable width to given width.
	 */
	public CSIZE setLineSize(int width) {
		return setLineSize(width, 0.5f, 0.0f);
	}

	/**
	 * Sets fixed height to given height 
	 * and sets variable width to given width.
	 */
	public CSIZE setLineSize(int width, int height) {
		return setLineSize(width, 0.5f, 0.0f, height);
	}

	/**
	 * Calls {@link CSize#setLineSize(int, float, float, int)} with line-width and line-height.
	 */
	public CSIZE setLineSize(float minWidthFactor, float maxWidthFactor) {
		return setLineSize(props().getLineWidth(), minWidthFactor, maxWidthFactor);
	}
	
	/**
	 * Calls {@link CSize#setLineSize(int, float, float, int)} with line-height.
	 */
	public CSIZE setLineSize(int width, float minWidthFactor, float maxWidthFactor) {
		return setLineSize(width, minWidthFactor, maxWidthFactor, props().getLineHeight());
	}
	
	/**
	 * Sets a fixed height and a variable width for a component.
	 * @param width preferred width
	 * @param minWidthFactor factor by which component width can shrink.
	 * @param maxWidthFactor factor by which a component width can grow.
	 * If 0.0f or less, maximum width is set to {@link #MAX_WIDTH}.
	 * @param height the fixed height of the component
	 */
	public CSIZE setLineSize(int width, float minWidthFactor, float maxWidthFactor, int height) {
		
		int minWidth = shrink(width, minWidthFactor);
		int maxWidth = (maxWidthFactor <= 0.0f ? MAX_WIDTH : grow(width, maxWidthFactor));
		return setLineSize(minWidth, width, maxWidth, height);
	}

	/**
	 * Sets a fixed height for a component with a variable width.
	 * @param minWidth minimum width
	 * @param width preferred width
	 * @param maxWidth maximum width
	 * @param height fixed height
	 */
	public CSIZE setLineSize(int minWidth, int width, int maxWidth, int height) {
		return min(minWidth, height).pref(width, height).max(maxWidth, height);
	}
	
	/* *** Variable area layout *** */

	/**
	 * Sets an area of variable size with preferred size set to line-width and 4 times line-height.  
	 */
	public CSIZE setAreaSize() {
		return setAreaSize(1.0f, 4.0f);
	}

	/**
	 * Sets an area of variable size with preferred size set to line-width times given factor
	 * and line-height times given factor.  
	 */
	public CSIZE setAreaSize(float widthFactor, float heightFactor) {
		return setAreaSize(getLineWidth(widthFactor), getLineHeight(heightFactor));
	}

	public CSIZE setAreaSize(int width, int height) {
		return setAreaSize(width, height, 4.0f, 0.0f);
	}
	
	/**
	 * Sets a variable width and height for a component.
	 * @param width preferred width
	 * @param height preferred height
	 * @param minFactor factor by which component can shrink.
	 * <br>A minimum width less than button-width is ignored when preferred width is more than button-width.
	 * <br>A minimum hieght less than line-height is ignored. 
	 * @param maxFactor factor by which component can grow.
	 * If 0.0f or less, maximum width and height is set to {@link #MAX_WIDTH} and {@link #MAX_HEIGHT}
	 */
	public CSIZE setAreaSize(int width, int height, float minFactor, float maxFactor) {
		
		int minWidth = shrink(width, minFactor);
		if (width > props().getButtonWidth() && minWidth < props().getButtonWidth()) 
			minWidth = props().getButtonWidth();
		if (width <= minWidth)
			width = minWidth + 1;
		
		int minHeight = shrink(height, minFactor);
		if (minHeight < props().getLineHeight()) 
			minHeight = props().getLineHeight();
		if (height < minHeight)
			height = minHeight + 1;
		int maxWidth = (maxFactor <= 0.0f ? MAX_WIDTH : grow(width, maxFactor));
		int maxHeight = (maxFactor <= 0.0f ? MAX_HEIGHT : grow(height, maxFactor));
		return min(minWidth, minHeight).pref(width, height).max(maxWidth, maxHeight);
	}

	/* *** Align width/height to defaults *** */
	
	/**
	 * Aligns all sizes to a button-width factor and line-height factor.
	 */
	public CSIZE alignSize() {
		return min(align(min())).pref(align(pref())).max(align(max()));
	}
	
	/**
	 * Aligns dimension to a button-width factor and line-height factor.
	 */
	public Dimension align(Dimension d) {
		
		d.width = alignWidth(d.width);
		d.height = alignHeight(d.height);
		return d;
	}
	
	public int alignWidth(int width) {
		return (width + (props().getButtonWidth() - (width % props().getButtonWidth())));
	}
	
	public int alignHeight(int height) {
		return (height + (props().getLineHeight() - (height % props().getLineHeight())));
	}

}
