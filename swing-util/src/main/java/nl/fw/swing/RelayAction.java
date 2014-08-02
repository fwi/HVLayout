package nl.fw.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;

/**
 * Any {@link ActionEvent} received by this action is relayed to the 
 * specified original action-listener with the source set in the action-event
 * to the original source of the original action-listener.
 * <br>See also {@link SwingUtils#createRelayActionForKey(javax.swing.JComponent, javax.swing.KeyStroke)}.  
 * @author fred
 *
 */
@SuppressWarnings("serial")
public class RelayAction extends AbstractAction {
	
	protected ActionListener orginalAl;
	protected Object origin;
	
	public RelayAction(Object origin, ActionListener orignalAl) {
		this.orginalAl = orignalAl;
		this.origin = (origin == null ? new Object() : origin);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		e.setSource(origin);
		orginalAl.actionPerformed(e);
	}

}
