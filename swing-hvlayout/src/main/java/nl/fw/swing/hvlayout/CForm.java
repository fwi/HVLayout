package nl.fw.swing.hvlayout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;

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

	/**
	 * The root-level box in a content-pane needs insets to keep text from sticking
	 * to the edges. The insets here give components a little distance from the window edges/borders.
	 * <br>Example code: {@code CForm form = new CForm(new VBox(CForm.MAIN_BOX_INSETS));}
	 *  
	 */
	public static Insets MAIN_BOX_INSETS = new Insets(2, 4, 2, 4);
	
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
