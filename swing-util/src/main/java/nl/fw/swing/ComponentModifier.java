package nl.fw.swing;

import java.awt.Component;
import java.awt.Container;

public abstract class ComponentModifier {

	private boolean modifyContainer;
	private Component c;
	private boolean containerType;
	
	public ComponentModifier() {
		this(false);
	}
	public ComponentModifier(boolean modifyContainer) {
		super();
		setModifyContainer(modifyContainer);
	}
	
	public boolean isModifyContainer() {
		return modifyContainer;
	}
	public void setModifyContainer(boolean modifyContainer) {
		this.modifyContainer = modifyContainer;
	}
	
	public Component getComponent() {
		return c;
	}
	public void setComponent(Component c) {
		this.c = c;
		containerType = false;
	}
	public void setComponent(Container c) {
		this.c = c;
		containerType = true;
	}
	
	public boolean isContainer() {
		return containerType;
	}
	
	/**
	 * Use {@link #getComponent()} to get the component to modify.
	 */
	public abstract void modify();
}

