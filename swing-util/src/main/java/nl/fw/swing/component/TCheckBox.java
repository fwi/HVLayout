package nl.fw.swing.component;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;

/**
 * A check-box that shows a tooltip when text is truncated
 * and no tooltip-text is set. Also supports a button
 * with only an icon set.
 * @author Fred
 *
 */
public class TCheckBox extends JCheckBox {
	
	private static final long serialVersionUID = -706109826778954796L;

	/** 
	 * The margins are the space between the button-border and the text
	 * and/or icon. Setting to 0 has no effect on the text but does
	 * allow the icon on the button to fully display (else icon
	 * is made smaller by the margins).
	 * The buttonMargins are applied to all instances of this class.
	 */
	public static Insets BUTTON_MARGIN = new Insets(0, 0, 0, 0);

	private Icon originalIcon;
	
	public TCheckBox() {
        this((String)null);
    }

    public TCheckBox(Action a) {
        this();
        setAction(a);
    }

    /** 
     * Returns a square button showing the icon. 
     * Assumes this button ONLY shows an icon. 
     * If icon is an instance of ImageIcon,
     * the icon is scaled to fit the square button
     * and the tooltip is set to the icon-description.
     */
    public TCheckBox(Icon icon) {
        this((Icon)null, true);
    }

    public TCheckBox(Icon icon, boolean selected) {
    	super();
		setMargin(BUTTON_MARGIN);
        setIconTextGap(0);
        setIcon(icon);
		setRequestFocusEnabled(true);
		setSelected(selected);
    }

    public TCheckBox(String text) {
        this(text, null, false);
    }

    public TCheckBox(String text, boolean selected) {
        this(text, null, false);
    }

    public TCheckBox(String text, Icon icon, boolean selected) {
    	super(text);
		setMargin(BUTTON_MARGIN);
		setIconTextGap(2);
		setToolTipText(" ");
		setIcon(icon);
		setRequestFocusEnabled(true);
    }
    
	/** Similar to {@link TLabel#getToolTipText()} */
    @Override
    public String getToolTipText(MouseEvent e) {

		String text = super.getToolTipText();
		if (!" " .equals(text)) return text;
		text = getText();
		int textWidth = getFontMetrics(getFont()).stringWidth(text);
		int iconWidth = (getIcon() == null ? 0 : getIcon().getIconWidth() + getIconTextGap());
		int textSpace = getWidth() - iconWidth - getInsets().left - getInsets().right;
		return (textWidth > textSpace ? text : null);
	}
    
   /** 
    * If icon is an instance of ImageIcon,
    * the icon is scaled to fit the largest square on the button
    * and the tooltip is set to the icon-description.
    */
    @Override
	public void setIcon(Icon icon) {

    	originalIcon = icon;
		if (icon != null && icon instanceof ImageIcon) {
			rescaleIcon();
			if (getText() == null || getText().length() == 0 || getText().equals(" ")) {
				setToolTipText(((ImageIcon)icon).getDescription());
			}
		} else {
			super.setIcon(icon);
		}
	}

    @Override
    public void setPreferredSize(Dimension d) {
    	super.setPreferredSize(d);
    	rescaleIcon();
    }

    public void rescaleIcon() {
    	
    	if (originalIcon != null && originalIcon instanceof ImageIcon) {
			// Tried using borderSize here but that has the same effect as calling getInsets.
			int h = getPreferredSize().height - getInsets().top	- getInsets().bottom;
			ImageIcon sizedIcon = new ImageIcon(((ImageIcon)originalIcon).getImage().getScaledInstance(h, h, Image.SCALE_SMOOTH));
			super.setIcon(sizedIcon);
    	}
    }
    
}
