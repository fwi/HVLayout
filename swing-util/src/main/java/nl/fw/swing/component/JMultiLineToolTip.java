/*
 * This software is licensed under the GNU Lesser GPL, see
 * http://www.gnu.org/licenses/ for details.
 * You can use this free software in commercial and non-commercial products,
 * as long as you give credit to this software when you use it.
 * This softwware is provided "as is" and the use of this software is 
 * completely at your own risk, there are absolutely no warranties.
 * 
 */

package nl.fw.swing.component;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JToolTip;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToolTipUI;


/**
 * A tooltip generator needed JTextArea is used as a multi-line-label.
 * 
 * Copied from http://www.codeguru.com/java/articles/122.shtml
 * Modified to remove unneeded variables.
 * 
 * @author Zafir Anjum
 */
public class JMultiLineToolTip extends JToolTip {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final String uiClassID = "ToolTipUI";
	public transient Font font;

	public JMultiLineToolTip(Font font) {
		super();
		this.font = font;
		updateUI();
	}

	@Override public void updateUI() {
		setUI(MultiLineToolTipUI.createUI(this));
	}

	public void setColumns(final int columns)	{
		this.columns = columns;
		this.fixedwidth = 0;
	}

	public int getColumns()	{
		return columns;
	}

	public void setFixedWidth(final int width) {
		this.fixedwidth = width;
		this.columns = 0;
	}

	public int getFixedWidth() {
		return fixedwidth;
	}

	protected transient int columns = 0;
	protected transient int fixedwidth = 0;
}

class MultiLineToolTipUI extends BasicToolTipUI {

	private static MultiLineToolTipUI sharedInstance = new MultiLineToolTipUI();
	//	private static JToolTip tip;
	protected transient CellRendererPane rendererPane;

	private static JTextArea textArea ;

	public static ComponentUI createUI(final JComponent c) {
		return sharedInstance;
	}

	@Override public void installUI(final JComponent c) {
		super.installUI(c);
		// JToolTip tip = (JToolTip)c;
		rendererPane = new CellRendererPane();
		c.add(rendererPane);
	}

	@Override public void uninstallUI(final JComponent c) {
		super.uninstallUI(c);
		c.remove(rendererPane);
		rendererPane = null;
	}

	@Override public void paint(final Graphics g, final JComponent c) {
		final Dimension size = c.getSize();
		textArea.setBackground(c.getBackground());
		rendererPane.paintComponent(g, textArea, c, 1, 1,
				size.width - 1, size.height - 1, true);
	}

	@Override public Dimension getPreferredSize(final JComponent c) {
		final String tipText = ((JToolTip)c).getTipText();
		if (tipText == null || tipText.equals(""))
			return new Dimension(0,0);
		textArea = new JTextArea(tipText);
		textArea.setFont(((JMultiLineToolTip)c).font);
		rendererPane.removeAll();
		rendererPane.add(textArea );
		textArea.setWrapStyleWord(true);
		final int width = ((JMultiLineToolTip)c).getFixedWidth();
		final int columns = ((JMultiLineToolTip)c).getColumns();

		if( columns > 0 )
		{
			textArea.setColumns(columns);
			textArea.setSize(0,0);
			textArea.setLineWrap(true);
			textArea.setSize( textArea.getPreferredSize() );
		}
		else if( width > 0 )
		{
			textArea.setLineWrap(true);
			Dimension d = textArea.getPreferredSize();
			d.width = width;
			d.height++;
			textArea.setSize(d);
		}
		else
			textArea.setLineWrap(false);

		final Dimension dim = textArea.getPreferredSize();
		dim.height += 1;
		dim.width += 1;
		return dim;
	}

	@Override public Dimension getMinimumSize(final JComponent c) {
		return getPreferredSize(c);
	}

	@Override public Dimension getMaximumSize(final JComponent c) {
		return getPreferredSize(c);
	}
}