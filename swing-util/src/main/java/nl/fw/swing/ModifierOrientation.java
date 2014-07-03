package nl.fw.swing;

import java.awt.ComponentOrientation;

/**
 * Component modifier that applies given component orientation to components and containers.
 * @author fred
 *
 */
public class ModifierOrientation extends ComponentModifier {

	private ComponentOrientation cor;
	
	public ModifierOrientation(ComponentOrientation cor) {
		super();
		this.setComponentOrientation(cor);
		setModifyContainer(true);
	}
	
	public void modify() {
		
		if (!isModifyContainer() && isContainer()) {
			return;
		}
		// if orientation was changed, component will be set to invalid.
		getComponent().applyComponentOrientation(cor);
	}

	public ComponentOrientation getComponentOrientation() {
		return cor;
	}

	public void setComponentOrientation(ComponentOrientation cor) {
		this.cor = cor;
	}
}
