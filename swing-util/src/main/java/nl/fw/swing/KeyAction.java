/*
 * This software is licensed under the GNU Lesser GPL, see
 * http://www.gnu.org/licenses/ for details.
 * You can use this free software in commercial and non-commercial products,
 * as long as you give credit to this software when you use it.
 * This softwware is provided "as is" and the use of this software is 
 * completely at your own risk, there are absolutely no warranties.
 * 
 */

package nl.fw.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;

/**
 * An action associated with a key that can be put into an action map so that
 * an action-event is fired for the appropriate event listener
 * (usually a button). When the cancel-event listener is also 
 * related to a keyboard action (Escape key for example) via an input map,
 * a window can respond properly to a user pressing the Escape key to
 * indicate he/she wants to "close the window without storing changes". 
 * <br> See OkCancelDialog.init() for an implementation example. 
 * @author Fred
 *
 */
public class KeyAction extends AbstractAction {

	private static final long serialVersionUID = -206322771023763629L;
	
	public static final String KEY_ACTION = "KeyAction";
	private final ActionListener keyListener;
	private final ActionEvent keyEvent;

	public KeyAction(Object keySource, ActionListener keyActionlistener) {
		super();
		keyListener = keyActionlistener;
		keyEvent = new ActionEvent(keySource, ActionEvent.ACTION_PERFORMED, KEY_ACTION); 
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		keyListener.actionPerformed(keyEvent);
	}

}
