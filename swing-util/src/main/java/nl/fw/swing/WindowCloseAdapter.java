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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * On close of a window, this WindowAdapter can be triggered to fire
 * a "close" action for the closeSource object. 
 * <br> See OkCancelDialog.init() for an implementation example. 
 * @author Fred
 *
 */
public class WindowCloseAdapter extends WindowAdapter {

	public static final String WINDOW_CLOSING = "WindowClosing";
	
	private final ActionListener closeListener;
	private final ActionEvent closeEvent;
	
	public WindowCloseAdapter(Object closeSource, ActionListener closeActionlistener) {
		super();
		closeListener = closeActionlistener;
		closeEvent = new ActionEvent(closeSource, ActionEvent.ACTION_PERFORMED, WINDOW_CLOSING); 
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		closeListener.actionPerformed(closeEvent);
	}
	
	public ActionListener getCloseListener() {
		return closeListener;
	}

	public ActionEvent getCloseEvent() {
		return closeEvent;
	}

}
