package nl.fw.swing.hvlayout.fluent;

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
	protected CSIZE me() { return (CSIZE) this;	}

	/**
	 * Sets minimum width to button width.
	 */
	public CSIZE setMinWidthButton() { return setMinWidth(props().getButtonWidth()); }
	
	/**
	 * Sets minimum width scaled by button-width with given factor.
	 */
	public CSIZE setMinWidth(float factor) {
		return setMinWidth(getButtonWidth(factor));
	}
	
	public CSIZE setMinWidth(int l) {
		
		Dimension d = min();
		min(l, d.height);
		if ((d = pref()).width < l) {
			pref(l, d.height);
			if ((d = max()).width < l) {
				max(l, d.height);
			}
		}
		return me();
	}

	/**
	 * Sets minimum height to line-height.
	 */
	public CSIZE setMinHeightLine() { return setMinHeight(props().getLineHeight()); }
	
	/**
	 * Sets minimum height scaled by line-height with given factor.
	 */
	public CSIZE setMinHeight(float factor) {
		return setMinHeight(getLineHeight(factor));
	}

	public CSIZE setMinHeight(int l) {
		
		Dimension d = min();
		min(d.width, l);
		if ((d = pref()).height < l) {
			pref(d.width, l);
			if ((d = max()).height < l) {
				max(d.width, l);
			}
		}
		return me();
	}

	public CSIZE setMin() { 
		return setMinWidthButton().setMinHeightLine(); 
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
	public CSIZE setPrefWidthButton() { return setPrefWidth(props().getButtonWidth()); }

	/**
	 * Sets preferred width scaled by button-width with given factor.
	 */
	public CSIZE setPrefWidth(float factor) {
		return setPrefWidth(getButtonWidth(factor));
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
	public CSIZE setPrefHeightLine() { return setPrefHeight(props().getLineHeight()); }

	/**
	 * Sets preferred height scaled by line-height with given factor.
	 */
	public CSIZE setPrefHeight(float factor) {
		return setPrefHeight(getLineHeight(factor));
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
	
	public CSIZE setPref() {
		return setPrefWidthButton().setPrefHeightLine();
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
	public CSIZE setMaxWidthButton() { return setMaxWidth(props().getButtonWidth()); }

	/**
	 * Sets maximum width scaled by button-width with given factor.
	 */
	public CSIZE setMaxWidth(float factor) {
		return setMaxWidth(getButtonWidth(factor));
	}

	public CSIZE setMaxWidth(int l) {
		
		Dimension d = max();
		max(l, d.height);
		if ((d = pref()).width > l) {
			pref(l, d.height);
			if ((d = min()).width > l) {
				min(l, d.height);
			}
		}
		return me();
	}
	
	/**
	 * Sets maximum height to line-height.
	 */
	public CSIZE setMaxHeightLine() { return setMaxHeight(props().getLineHeight()); }

	/**
	 * Sets maximum height scaled by line-height with given factor.
	 */
	public CSIZE setMaxHeight(float factor) {
		return setMaxHeight(getLineHeight(factor));
	}

	public CSIZE setMaxHeight(int l) {
		
		Dimension d = max();
		max(d.width, l);
		if ((d = pref()).height > l) {
			pref(d.width, l);
			if ((d = min()).height > l) {
				min(d.width, l);
			}
		}
		return me();
	}
	
	public CSIZE setMax() {
		return setMaxWidthButton().setMaxHeightLine();
	}
	
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
