package nl.fw.swing.hvlayout.fluent;

import static nl.fw.swing.hvlayout.CSizeUtils.multiply;

import java.awt.Component;
import java.awt.Dimension;

/**
 * Adds functions that set a component size and update related sizes so that
 * after a size change, the following holds true: <code>min <= pref <= max </code>  
 * @author fred
 */
public class BaseCSize2<CSIZE extends BaseCSize2<CSIZE, CTYPE>, CTYPE extends Component> 
	extends BaseCSize<CSIZE, CTYPE> {

	@SuppressWarnings("unchecked")
	@Override
	protected CSIZE me() { 
		return (CSIZE) this;	
	}

	/**
	 * Sets minimum width to button width.
	 */
	public CSIZE setMinWidthButton() { 
		return setMinWidth(props().getButtonWidth()); 
	}
	
	/**
	 * Sets minimum width scaled by button-width with given factor.
	 */
	public CSIZE setMinWidthButton(float factor) { 
		return setMinWidth(getButtonWidth(factor)); 
	}
	
	/** 
	 * Scales current minimum width, updates other sizes if needed. 
	 */
	public CSIZE setMinWidth(float factor) { 
		return setMinWidth(multiply(min().width, factor));	
	}
	
	public CSIZE setMinWidth(int l) {
		
		Dimension d = min();
		min(l, d.height);
		if ((d = pref()).width < l) {
			pref(l, d.height);
		}
		if ((d = max()).width < l) {
			max(l, d.height);
		}
		return me();
	}

	/**
	 * Sets minimum height to line-height.
	 */
	public CSIZE setMinHeightLine() { 
		return setMinHeight(props().getLineHeight()); 
	}
	
	/**
	 * Sets minimum height scaled by line-height with given factor.
	 */
	public CSIZE setMinHeightLine(float factor) { 
		return setMinHeight(getLineHeight(factor)); 
	}

	/** 
	 * Scales current minimum height, updates other sizes if needed. 
	 */
	public CSIZE setMinHeight(float factor) { 
		return setMinHeight(multiply(min().height, factor)); 
	}

	public CSIZE setMinHeight(int l) {
		
		Dimension d = min();
		min(d.width, l);
		if ((d = pref()).height < l) {
			pref(d.width, l);
		}
		if ((d = max()).height < l) {
			max(d.width, l);
		}
		return me();
	}

	public CSIZE setMinButtonSize() { 
		return setMinWidthButton().setMinHeightLine(); 
	}
		
	public CSIZE setMinButtonSize(float widthFactor, float heightFactor) {
		return setMinWidthButton(widthFactor).setMinHeightLine(heightFactor);
	}

	public CSIZE setMin(float widthFactor, float heightFactor) {
		return setMinWidth(widthFactor).setMinHeight(heightFactor);
	}

	public CSIZE setMin(Dimension d) { 
		return setMin(d.width, d.height); 
	}

	public CSIZE setMin(int w, int h) {
		return setMinWidth(w).setMinHeight(h);
	}

	/**
	 * Sets preferred width to button width.
	 */
	public CSIZE setPrefWidthButton() { 
		return setPrefWidth(props().getButtonWidth()); 
	}

	/**
	 * Sets preferred width scaled by button-width with given factor.
	 */
	public CSIZE setPrefWidthButton(float factor) {
		return setPrefWidth(getButtonWidth(factor));
	}
	
	/** 
	 * Scales current preferred width, updates other sizes if needed. 
	 */
	public CSIZE setPrefWidth(float factor) { 
		return setPrefWidth(multiply(pref().width, factor));	
	}

	public CSIZE setPrefWidth(int l) {
		
		Dimension d = pref();
		pref(l, d.height);
		if ((d = max()).width < l) {
			max(l, d.height);
		}
		if ((d = min()).width > l) {
			min(l, d.height);
		}
		return me();
	}
	
	/**
	 * Sets preferred height to line-height.
	 */
	public CSIZE setPrefHeightLine() { 
		return setPrefHeight(props().getLineHeight()); 
	}

	/**
	 * Sets preferred height scaled by line-height with given factor.
	 */
	public CSIZE setPrefHeightLine(float factor) {
		return setPrefHeight(getLineHeight(factor));
	}

	/** 
	 * Scales current preferred height, updates other sizes if needed. 
	 */
	public CSIZE setPrefHeight(float factor) { 
		return setPrefHeight(multiply(pref().height, factor));	
	}

	public CSIZE setPrefHeight(int l) {
		
		Dimension d = pref();
		pref(d.width, l);
		if ((d = max()).height < l) {
			max(d.width, l);
		}
		if ((d = min()).height > l) {
			min(d.width, l);
		}
		return me();
	}
	
	public CSIZE setPrefButtonSize() {
		return setPrefWidthButton().setPrefHeightLine();
	}
	
	public CSIZE setPrefButtonSize(float widthFactor, float heightFactor) {
		return setPrefWidthButton(widthFactor).setPrefHeightLine(heightFactor);
	}

	public CSIZE setPref(float widthFactor, float heightFactor) {
		return setPrefWidth(widthFactor).setPrefHeight(heightFactor);
	}

	public CSIZE setPref(Dimension d) {
		return setPref(d.width, d.height);
	}

	public CSIZE setPref(int w, int h) {
		return setPrefWidth(w).setPrefHeight(h);
	}

	/**
	 * Sets maximum width to button width.
	 */
	public CSIZE setMaxWidthButton() { 
		return setMaxWidth(props().getButtonWidth()); 
	}

	/**
	 * Sets maximum width scaled by button-width with given factor.
	 */
	public CSIZE setMaxWidthButton(float factor) {
		return setMaxWidth(getButtonWidth(factor));
	}

	/** 
	 * Multiplies current PREFERRED width with given factor and sets it as maximum size.
	 * Updates other sizes if needed. 
	 */
	public CSIZE setMaxWidth(float factor) { 
		return setMaxWidth(multiply(pref().width, factor));	
	}

	public CSIZE setMaxWidth(int l) {
		
		Dimension d = max();
		max(l, d.height);
		if ((d = pref()).width > l) {
			pref(l, d.height);
		}
		if ((d = min()).width > l) {
			min(l, d.height);
		}
		return me();
	}
	
	/**
	 * Sets maximum height to line-height.
	 */
	public CSIZE setMaxHeightLine() { 
		return setMaxHeight(props().getLineHeight()); 
	}

	/**
	 * Sets maximum height scaled by line-height with given factor.
	 */
	public CSIZE setMaxHeightLine(float factor) {
		return setMaxHeight(getLineHeight(factor));
	}

	/** 
	 * Multiplies current PREFERRED height with given factor and sets it as maximum size.
	 * Updates other sizes if needed. 
	 */
	public CSIZE setMaxHeight(float factor) { 
		return setMaxHeight(multiply(pref().height, factor));	
	}

	public CSIZE setMaxHeight(int l) {
		
		Dimension d = max();
		max(d.width, l);
		if ((d = pref()).height > l) {
			pref(d.width, l);
		}
		if ((d = min()).height > l) {
			min(d.width, l);
		}
		return me();
	}
	
	public CSIZE setMaxButtonSize() {
		return setMaxWidthButton().setMaxHeightLine();
	}
	
	public CSIZE setMaxButtonSize(float widthFactor, float heightFactor) {
		return setMaxWidthButton(widthFactor).setMaxHeightLine(heightFactor);
	}

	/** 
	 * Multiplies current PREFERRED size with given factors and sets it as maximum size.
	 * Updates other sizes if needed. 
	 */
	public CSIZE setMax(float widthFactor, float heightFactor) {
		return setMaxWidth(widthFactor).setMaxHeight(heightFactor);
	}

	public CSIZE setMax(Dimension d) {
		return setMax(d.width, d.height);
	}

	public CSIZE setMax(int w, int h) {
		return setMaxWidth(w).setMaxHeight(h);
	}

}
