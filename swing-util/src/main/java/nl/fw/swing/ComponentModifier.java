package nl.fw.swing;

import java.awt.Component;

/**
 * Use together with {@link SwingUtils#modifyComponentTree(Component, ComponentModifier)}
 * to modify a property for all components in a component tree.
 * @author fred
 *
 */
public abstract class ComponentModifier {

	private Component c;
	
	public ComponentModifier() {
		super();
	}
	
	public Component getComponent() {
		return c;
	}

	public void setComponent(Component c) {
		this.c = c;
	}
	
	/**
	 * Use {@link #getComponent()} to get the component to modify.
	 */
	public abstract void modify();
}

