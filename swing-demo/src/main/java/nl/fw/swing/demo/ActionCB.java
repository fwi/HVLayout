package nl.fw.swing.demo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import nl.fw.swing.component.TCheckBox;

/**
 * Helper class for checkboxes that have actions that update the UI.
 * During a UI update the check-boxes themselves might be re-created
 * which can result in null-references inside runnables
 * (or runnables using 'final' variables that no longer exist).
 * <br>This class can serve as a fixed pointer to the re-created checkbox
 * so that runnables never use 'stale' variables. 
 * @author fred
 *
 */
public class ActionCB {

	/** The checkbox created by {@link #build()}. */
	public TCheckBox cb;
	/** The default selection state of the checkbox. */
	public boolean marked;
	/** The text for the checkbox. */
	public String text;
	
	/** The actions to run directly when check-box is (un)marked. */
	public Runnable action;
	/** The UI actions to run asynchronous when check-box is (un)marked. */
	public Runnable uiUpdate;
	
	/**
	 * Creates the checkbox and adds an action listener.
	 * The action will run {@link #action} directly and later invokes the {@link #uiUpdate}
	 * on the EDT (event dispatcher thread).
	 * @return this
	 */
	public ActionCB build() {

		cb = new TCheckBox(text);
		cb.setSelected(marked);
		cb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				action.run();
				SwingUtilities.invokeLater(uiUpdate);
			}
		});
		return this;
	}
	
}
