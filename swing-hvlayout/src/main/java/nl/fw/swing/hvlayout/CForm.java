package nl.fw.swing.hvlayout;

import java.awt.Component;
import java.awt.Container;

import nl.fw.swing.hvlayout.fluent.BaseCForm;
import nl.fw.swing.hvlayout.fluent.BaseCSize3;
import nl.fw.swing.hvlayout.fluent.BaseCSize4;

/**
 * Helper class for building a form.
 * With this class a leaf of the container tree is active
 * and can be changed by adding a child or going up the tree.
 * 
 * This class is an end-point for the fluent class {@link BaseCForm}.
 * Try not to extend this class (that will break the fluent API hierarchy), instead extend {@link BaseCForm}
 * in the same manner that {@link BaseCSize4} extends {@link BaseCSize3}.
 * <br>Then create a 'concrete' class similar to this class.
 * @author fred
 *
 */
public class CForm extends BaseCForm<CForm, CSize, Component> {

	public CForm() {
		this(null, null);
	}

	public CForm(Container root) {
		this(root, null);
	}

	public CForm(Container root, CSize cs) {
		super();
		setRoot(root);
		setCSize(cs == null ? CSize.getDefault() : cs);
	}
	
}
