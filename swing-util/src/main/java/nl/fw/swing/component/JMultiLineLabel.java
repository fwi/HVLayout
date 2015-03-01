package nl.fw.swing.component;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.StringReader;

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
		if (font == null) {
			// JTextArea shows monospaced font by default.
			font = SwingUtils.getUIFont();
		}
		setFont(font);
		if (!font.getName().equals(Font.MONOSPACED)) {
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
	 * Preferred size calculated from display test.
	 * <br>Only works if text is pre-formatted with line-breaks.
	 */
	public Dimension getPreferredSizeDisplay() {
		
		int width = getPreferredDisplayWidth();
		int height = getFontMetrics(getFont()).getHeight() * getTextLines() 
				+ getInsets().top + getInsets().bottom;
		return new Dimension(width, height);
	}

	/**
	 * Width calculated from widest text-line plus insets.
	 * <br>Only works if text is pre-formatted with line-breaks.
	 */
	public int getPreferredDisplayWidth() {
		
		int maxTextWidth = 0;
		try (StringReader reader = new StringReader(getText());
				BufferedReader lineReader = new BufferedReader(reader)) {
			String l = null;
			while((l = lineReader.readLine()) != null) {
				int textWidth = getFontMetrics(getFont()).stringWidth(l);
				maxTextWidth = Math.max(maxTextWidth, textWidth);
			}
		} catch (Exception ignored) {}
		int displayWidth = maxTextWidth + getInsets().left + getInsets().right;
		return displayWidth;
	}
	
	/**
	 * Number of text-lines in text.
	 */
	public int getTextLines() {
		
		int count = 0;
		try (StringReader reader = new StringReader(getText());
				BufferedReader lineReader = new BufferedReader(reader)) {
			while(lineReader.readLine() != null) {
				count++;
			}
		} catch (Exception ignored) {}
		return count;
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
