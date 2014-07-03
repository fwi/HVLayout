package nl.fw.swing.component;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTextArea;
import javax.swing.JToolTip;
import javax.swing.JViewport;

import nl.fw.swing.SwingUtils;

public class JMultiLineLabel extends JTextArea {

	private static final long serialVersionUID = 4364793853178467906L;
	
	public JMultiLineLabel(String text) {
		this(text, false);
	}

	public JMultiLineLabel(String text, boolean monospaced) {
		this(text, monospaced ? SwingUtils.getUIFontMonoSpaced() : null);
	}

	public JMultiLineLabel(String text, Font font) {
		super(text);
		setEditable(false);
		setLineWrap(false);
		setWrapStyleWord(true);
		if (font != null) {
			setFont(font);
		}
		boolean monospaced = (font == null ? false : 
			font.getName().equals(Font.MONOSPACED));
		if (monospaced) {
			if (font == null) { 
				setFont(SwingUtils.getUIFontMonoSpaced());
			}
		} else {
			if (font == null) { 
				setFont(SwingUtils.getUIFont());
			}
			setOpaque(false);
			setToolTipText(text);
		}
		addWrapMouseListener();
		Dimension d = getPreferredSize();
		setMinimumSize(d);
		setMaximumSize(d);
	}
	
	private void addWrapMouseListener() {
		
		addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent event) {
				
				if (event.getButton() == MouseEvent.BUTTON1 
						&& event.getSource() instanceof JTextArea) {
					JTextArea source = (JTextArea) event.getSource();
					source.setLineWrap(!source.getLineWrap());
				}
			}
		});
	}


	/**
	 * Overload to use JMultiLineToolTip which shows text just
	 * like text area does.
	 */
	@Override
	public JToolTip createToolTip()	{
		return new JMultiLineToolTip(getFont());
	}

	/**
	 * Only show a tooltip when the text area is not fully shown
	 * AND the text area is not inside a scroller (because then you
	 * can scroll to see the text, a tooltip is then only annoying).
	 */
	@Override 
	public String getToolTipText(final MouseEvent e) {
		
		if (getParent() instanceof JViewport) {
			return null;
		}
		Rectangle v = getVisibleRect();
		Dimension p = getPreferredSize();
		if (v.width < p.width || v.height < p.height) {
			return getText();
		}
		return null;
	}

}
