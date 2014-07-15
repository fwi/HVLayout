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
	 * Allow component to shrink by half of preferred width.
	 */
	public CSIZE shrinkWidth() {
		return shrinkWidth(0.5f);
	}	
	
	public CSIZE shrinkWidth(float factor) {
		return setMinWidth(shrink(pref().width, factor));
	}

	/**
	 * Allow component to shrink by half of preferred height.
	 */
	public CSIZE shrinkHeight() {
		return shrinkWidth(0.5f);
	}	
	
	public CSIZE shrinkHeight(float factor) {
		return setMinHeight(shrink(pref().height, factor));
	}
	
	/* *** Grow options *** */

	public CSIZE growWidth() {
		return growWidth(2.0f);
	}	
	
	public CSIZE growWidth(float factor) {
		return setMaxWidth(grow(pref().width, factor));
	}
	
	public CSIZE growHeight() {
		return growHeight(2.0f);
	}	
	
	public CSIZE growHeight(float factor) {
		return setMaxHeight(grow(pref().height, factor));
	}

	/* *** Scale options *** */

	public CSIZE scale(float widthFactor, float heightFactor) {
		
		min(CSizeUtils.scale(min(), widthFactor, heightFactor));
		pref(CSizeUtils.scale(pref(), widthFactor, heightFactor));
		setMax(CSizeUtils.scaleMax(max(), widthFactor, heightFactor));
		return me();
	}

	public CSIZE scaleWidth(float factor) {
		return scale(factor, 1.0f);
	}

	public CSIZE scaleHeight(float factor) {
		return scale(1.0f, factor);
	}

}
