package nl.fw.swing.hvlayout;

import java.awt.Component;

import nl.fw.swing.hvlayout.fluent.*;

/**
 * Adjust Component Size based on {@link HVSize} defaults
 * so that HVlayout can layout components properly.
 * <p>
 * This class is an end-point for the fluent class {@link BaseCSize4}.
 * Try not to extend this class (that will break the fluent API hierarchy), instead extend {@link BaseCSize4}
 * in the same manner that {@link BaseCSize4} extends {@link BaseCSize3}.
 * <br>Then create a 'concrete' class similar to this class.
 * @author fred
 *
 */
public class CSize extends BaseCSize4<CSize, Component> {

	private static CSize defaultInstance = new CSize();
	
	public static CSize getDefault() {
		return defaultInstance;
	}
	
	public static void setDefault(CSize defaultInstance) {
		if (defaultInstance != null) {
			CSize.defaultInstance = defaultInstance;
		}
	}

	public CSize() {
		this(null, null);
	}

	public CSize(Component c) {
		this(null, c);
	}

	public CSize(HVSize props) {
		this(props, null);
	}

	public CSize(HVSize props, Component c) {
		super();
		if (props != null) {
			setHvsize(props);
		}
		set(c);
	}
	
}
