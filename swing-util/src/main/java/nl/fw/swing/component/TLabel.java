package nl.fw.swing.component;

import java.awt.Font;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JLabel;


/** 
 * A JLabel that shows a tooltip when the label-text is truncated.
 */ 
public class TLabel extends JLabel {
	
	private static final long serialVersionUID = 5815324549890565898L;
	/**
	 * Constant used to indicate there is no custom tooltip-text set
	 * and the label-text itself can be displayed as tooltip-text
	 * when the label-text is truncated on screen. 
	 */
	public static final String NO_TOOLTIP_TEXT = " ";

	public TLabel() {
        this(null, null, LEADING);
	}
	
	public TLabel(String text) {
        this(text, null, LEADING);
	}

	public TLabel(String text, Font font) {
        this(text, null, LEADING);
		if (font != null) setFont(font);
	}

	public TLabel(Icon image) {
        this(null, image, CENTER);
	}

	public TLabel(Icon image, int horizontalAlignment) {
        this(null, image, horizontalAlignment);
	}

	public TLabel(String text, int horizontalAlignment) {
        this(text, null, horizontalAlignment);
	}
	
	/**
	 * See {@link JLabel#JLabel(String, Icon, int)}.
	 */
	public TLabel(String text, Icon image, int horizontalAlignment) {
		super(text, image, horizontalAlignment);
		setIconTextGap(2);
		setToolTipText(NO_TOOLTIP_TEXT);
		setRequestFocusEnabled(false);
	}
	
	/**
	 * Shows a tooltip with the full text of the label
	 * when the text in the label is truncated (i.e. when the three
	 * dots appear at the end of the text a.k.a. ellipsis) and no tooltip-text is set.
	 * Copied and modified from
	 * http://forum.java.sun.com/thread.jspa?threadID=675342&messageID=3943542
	 */
	@Override
	public String getToolTipText(MouseEvent e) {
		
		if (NO_TOOLTIP_TEXT != super.getToolTipText()) {
			return super.getToolTipText();
		}
		int minWidth = getDisplayWidth();
		int currentWidth = getWidth();
		// System.out.println("Width: " + currentWidth + ", min: " + minWidth);
		return (currentWidth < minWidth ? getText() : null);
	}
	
	/**
	 * Label width required to fully display the label-text. 
	 */
	public int getDisplayWidth() {
		
		int textWidth = getFontMetrics(getFont()).stringWidth(getText());
		int iconWidth = (getIcon() == null ? 0 : getIcon().getIconWidth() + getIconTextGap());
		int displayWidth = textWidth + iconWidth + getInsets().left + getInsets().right;
		return displayWidth;
	}
}
