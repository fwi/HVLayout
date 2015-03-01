package nl.fw.swing.component;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JTextField;

/**
 * A text-field that can show an icon just to the left of where user text-input starts.
 * See {@link #setIcon(ImageIcon)}.
 * @author fred
 */
public class JTextFieldIcon extends JTextField {
	
	private static final long serialVersionUID = 2940790958422198713L;
	
	public static int ICON_TEXT_GAP = 2;
	public static int ICON_MARGIN_HEIGHT = 2;
	
	private Image image;
	private int borderLeft;
	private int iconMarginHeight;
	
	public JTextFieldIcon() {
		super();
	}

	public JTextFieldIcon(String text) {
		super(text);
	}

	/**
	 * Call this method AFTER setting the preferred size of the component.
	 * Text input starts after the displayed icon, 
	 * this is arranged by setting an appropriate {@link #setMargin(Insets)}.
	 */
	public void setIcon(ImageIcon icon) {
		
		iconMarginHeight = ICON_MARGIN_HEIGHT;
		int height = getPreferredSize().height - (2 * ICON_MARGIN_HEIGHT);
		if (height < 2) {
			height = 2 * ICON_MARGIN_HEIGHT;
			iconMarginHeight = 0;
		}
		float aspectRatio = icon.getIconHeight() / ((float)icon.getIconWidth());
		int width = (int)(aspectRatio * height);
		image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
		borderLeft = getInsets().left;
        setMargin(new Insets(0, borderLeft + width + ICON_TEXT_GAP, 0, 0));
	}
	
	@Override
	protected void paintComponent(Graphics g) {
        
		super.paintComponent(g);
		if (image != null) {
			g.drawImage(image, borderLeft, iconMarginHeight, this);
		}
    }

}
