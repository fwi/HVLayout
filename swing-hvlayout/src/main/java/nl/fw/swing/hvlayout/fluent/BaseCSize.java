package nl.fw.swing.hvlayout.fluent;

import java.awt.Component;
import java.awt.Dimension;

import nl.fw.swing.hvlayout.HVSize;
import static nl.fw.swing.hvlayout.CSizeUtils.*;

/**
 * Base class for a fluent API for component-size related functions.
 * Contains basic functions to get or set a component size.
 * <p>
 * Copied from fluent generic design (a.k.a. "Curiously Recurring Template Pattern") described at:
 * <br>http://www.unquietcode.com/blog/2011/programming/using-generics-to-build-fluent-apis-in-java/
 * <br>and adjusted to allow composition using the class hierarchy. 
 * @author fred
 */
public class BaseCSize<CSIZE extends BaseCSize<CSIZE, CTYPE>, CTYPE extends Component> {
	
	private HVSize hvsize; 
	private CTYPE c;
	
	public BaseCSize() {
		super();
		hvsize = HVSize.getDefault();
	}

	@SuppressWarnings("unchecked")
	protected CSIZE me() { return (CSIZE) this;	}
	
	public CSIZE set(CTYPE c) { this.c = c; return me(); }
	public CTYPE c() { return get(); }
	public CTYPE get() { return c; }
	
	public CSIZE setHvsize(HVSize props) { this.hvsize = props; return me(); }
	protected HVSize props() { return getHvsize(); }
	public HVSize getHvsize() {	return hvsize; }

	public Dimension dim(int w, int h) { return new Dimension (w, h); }
	public Dimension min() { return c.getMinimumSize(); }
	public Dimension pref() { return c.getPreferredSize(); }
	public Dimension prefButton() { return dim(hvsize.getLineHeight(), hvsize.getButtonWidth()); }
	public Dimension prefLine() { return dim(hvsize.getLineHeight(), hvsize.getLineWidth()); }
	public Dimension max() { return c.getMaximumSize(); }

	public CSIZE min(Dimension d) { c.setMinimumSize(d); return me(); }
	public CSIZE pref(Dimension d) { c.setPreferredSize(d); return me(); }
	public CSIZE max(Dimension d) { c.setMaximumSize(d); return me(); }
	public CSIZE min(int w, int h) { return min(dim(w, h)); }
	public CSIZE pref(int w, int h) { return pref(dim(w, h)); }
	public CSIZE max(int w, int h) { return max(dim(w, h)); }
	public CSIZE minWidth(int w) { return min(w, min().height); }
	public CSIZE prefWidth(int w) { return pref(w, pref().height); }
	public CSIZE maxWidth(int w) { return max(w, max().height); }
	public CSIZE minHeight(int h) { return min(min().width, h); }
	public CSIZE prefHeight(int h) { return pref(pref().width, h); }
	public CSIZE maxHeight(int h) { return max(max().width, h); }
	
	public int getButtonWidth(float factor) {
		return multiply(hvsize.getButtonWidth(), factor);
	}

	public int getLineWidth(float factor) {
		return multiply(hvsize.getLineWidth(), factor);
	}

	public int getLineHeight(float factor) {
		return multiply(hvsize.getLineHeight(), factor);
	}

	/**
	 * Sets size of component to given min/preferred/max dimensions.
	 */
	public CSIZE setSize(Dimension min, Dimension pref, Dimension max) {
		return min(min).pref(pref).max(max);
	}
	
	public CSIZE copySize(Component source) {
		
		min(source.getMinimumSize());
		pref(source.getPreferredSize());
		max(source.getMaximumSize());
		return me();
	}

}
