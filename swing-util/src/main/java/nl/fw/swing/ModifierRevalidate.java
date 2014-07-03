package nl.fw.swing;

import java.awt.Component;

/**
 * Component modifier that calls {@link Component#invalidate()} 
 * and then {@link Component#validate()} for components and containers.
 * @author fred
 *
 */
public class ModifierRevalidate extends ComponentModifier {

	public ModifierRevalidate() {
		super();
		setModifyContainer(true);
	}
	
	public void modify() {
		
		if (!isModifyContainer() && isContainer()) {
			return;
		}
		getComponent().invalidate();
		getComponent().validate();
	}
}

