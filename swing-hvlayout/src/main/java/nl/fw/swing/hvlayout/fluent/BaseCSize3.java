package nl.fw.swing.hvlayout.fluent;

import java.awt.Component;

import nl.fw.swing.hvlayout.CSizeUtils;
import static nl.fw.swing.hvlayout.CSizeUtils.*;

/**
 * Adds functions that set a component size relative to it's current size.
 * @author fred
 */
public class BaseCSize3<CSIZE extends BaseCSize3<CSIZE, CTYPE>, CTYPE extends Component> 
	extends BaseCSize2<CSIZE, CTYPE> {

	@SuppressWarnings("unchecked")
	@Override
	protected CSIZE me() { return (CSIZE) this;	}

	/* *** Fixed size options *** */

	/**
	 * Sets minimum and maximum size to preferred size.
	 * Equal to <code>setFixed(pref())</code>
	 */
	public CSIZE fixedSize() {
		return min(pref()).max(pref());
	}

	/** Ensures a component cannot shrink horizontally. */
	public CSIZE fixedMinWidth() {
		return min(pref().width, min().height);
	}

	/** Ensures a component cannot shrink vertically. */
	public CSIZE fixedMinHeight() {
		return min(min().width, pref().height);
	}
	
	/** Ensures a component cannot shrink. */
	public CSIZE fixedMinSize() {
		return min(pref());
	}

	/** Ensures a component cannot grow horizontally. */
	public CSIZE fixedMaxWidth() {
		return max(pref().width, max().height);
	}

	/** Ensures a component cannot grow vertically. */
	public CSIZE fixedMaxHeight() {
		return max(max().width, pref().height);
	}

	/** Ensures a component cannot grow. */
	public CSIZE fixedMaxSize() {
		return max(pref());
	}

	/** Ensures a component cannot change size horizontally. */
	public CSIZE fixedWidth() {
		return fixedMinWidth().fixedMaxWidth();
	}

	/** Ensures a component cannot change size vertical. */
	public CSIZE fixedHeight() {
		return fixedMinHeight().fixedMaxHeight();
	}

	/* *** Shrink options *** */
	
	/**
	 * Allow component to shrink in width to half of the current preferred width.
	 */
	public CSIZE shrinkWidth() {
		return shrinkWidth(0.5f);
	}	
	
	/**
	 * Allow component to shrink in width to given factor times the current preferred width.
	 */
	public CSIZE shrinkWidth(float factor) {
		return setMinWidth(shrink(pref().width, factor));
	}

	/**
	 * Allow component to shrink in height by half of current preferred height.
	 */
	public CSIZE shrinkHeight() {
		return shrinkWidth(0.5f);
	}	
	
	/**
	 * Allow component to shrink in height to given factor times the current preferred height.
	 */
	public CSIZE shrinkHeight(float factor) {
		return setMinHeight(shrink(pref().height, factor));
	}
	
	/* *** Grow options *** */

	/**
	 * Allow component to grow in width to two times the current preferred width.
	 */
	public CSIZE growWidth() {
		return growWidth(2.0f);
	}	
	
	/**
	 * Allow component to grow in width to given factor times the current preferred width.
	 */
	public CSIZE growWidth(float factor) {
		return setMaxWidth(grow(pref().width, factor));
	}
	
	/**
	 * Allow component to grow in height to two times the current preferred height.
	 */
	public CSIZE growHeight() {
		return growHeight(2.0f);
	}	
	
	/**
	 * Allow component to grow in height to given factor times the current preferred height.
	 */
	public CSIZE growHeight(float factor) {
		return setMaxHeight(grow(pref().height, factor));
	}

	/* *** Scale options *** */

	/**
	 * Multiply all sizes times the given factors for width and height.
	 */
	public CSIZE scale(float widthFactor, float heightFactor) {
		
		min(CSizeUtils.scale(min(), widthFactor, heightFactor));
		pref(CSizeUtils.scale(pref(), widthFactor, heightFactor));
		setMax(CSizeUtils.scaleMax(max(), widthFactor, heightFactor));
		return me();
	}

	/**
	 * Multiply all width sizes times the given factor.
	 */
	public CSIZE scaleWidth(float factor) {
		return scale(factor, 1.0f);
	}

	/**
	 * Multiply all height sizes times the given factor.
	 */
	public CSIZE scaleHeight(float factor) {
		return scale(1.0f, factor);
	}

}
