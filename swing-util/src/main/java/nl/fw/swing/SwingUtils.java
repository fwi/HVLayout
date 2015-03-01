package nl.fw.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwingUtils {

	private SwingUtils() {}

	private static final Logger log = LoggerFactory.getLogger(SwingUtils.class);
	
	static {
		calculateScreenSizes();
	}
	
	/**
	 * The bounds of the primary screen (adjusted for insets).
	 */
	public static Rectangle SCREEN_SIZE_PRIMARY;
	
	/**
	 * The virtual bounds of all screens together (adjusted for insets).
	 */
	public static Rectangle SCREEN_SIZE_TOTAL;

	/**
	 * Updates {@link #SCREEN_SIZE_PRIMARY } and {@link #SCREEN_SIZE_TOTAL}
	 */
	public static void calculateScreenSizes() {
		
		Rectangle totalBounds = new Rectangle();
		Rectangle primaryBounds = null;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		for (int j = 0; j < gs.length; j++) { 
			GraphicsDevice gd = gs[j];
			if (gd == ge.getDefaultScreenDevice()){
				primaryBounds = new Rectangle();
			}
			GraphicsConfiguration[] gc = gd.getConfigurations();
			for (int i = 0; i < gc.length; i++) {
				Rectangle bounds = gc[i].getBounds();
				Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc[i]);
				bounds.x += screenInsets.left;
			    bounds.y += screenInsets.top;
			    bounds.height -= screenInsets.bottom;
			    bounds.width -= screenInsets.right;
			    totalBounds =totalBounds.union(bounds);
			    if (primaryBounds != null) {
			    	primaryBounds = primaryBounds.union(bounds);
			    }
			}
			if (primaryBounds != null) {
				SCREEN_SIZE_PRIMARY = primaryBounds;
				primaryBounds = null;
			}
		}
		SCREEN_SIZE_TOTAL = totalBounds;
	}
	
	public static void setDefaultUILookAndFeel() {
		
		try {
			// http://stackoverflow.com/questions/179955/how-do-you-enable-anti-aliasing-in-arbitrary-java-apps
			// All these properties should already have an appropriate value, see also
			// https://docs.oracle.com/javase/8/docs/technotes/guides/2d/flags.html#aaFonts
			/*
			System.setProperty("sun.java2d.xrender", "true");
			System.setProperty("swing.aatext", "true");
			System.setProperty("awt.useSystemAAFontSettings", "lcd");
			*/
			String lfClassName = UIManager.getSystemLookAndFeelClassName();
			if (lfClassName.contains("Metal")) {
				for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
					if (laf.getClassName().contains("Nimbus")) {
						lfClassName = laf.getClassName();
						break;
					}
	            }
			}
			UIManager.setLookAndFeel(lfClassName);
			log.debug("Using look and feel " + lfClassName);
		} catch (Exception e) {
			log.debug("System look and feel not available: " + e);
		}
	}

	/**
	 * By default a tooltip will disappear after 4 seconds.
	 * @param dismissDelayMs How long to wait for a tooltip to disappear in milliseconds.
	 * Value 0 or less is converted to "forever" (i.e. tooltip only disappears when mouse is moved).
	 */
	public static void tooltipLinger(int dismissDelayMs) {
		javax.swing.ToolTipManager.sharedInstance().setDismissDelay(
				dismissDelayMs < 1 ? Integer.MAX_VALUE : dismissDelayMs);
	}

	/**
	 * Set smart revalidate to true to incur less overhead when the method
	 * {@link Component#revalidate()} is called.
	 * <br>Revalidate calls {@link Container#isValidateRoot()} which 
	 * normally results in calling {@link Component#validate()} for the top Windows/Frame/Dialog container.
	 * With smart revalidate the "validate root" can be a scroll-pane for example
	 * (see also {@link JComponent#isValidateRoot()}.
	 */
	public static void smartRevalidate(boolean beSmart) {
		System.setProperty("java.awt.smartInvalidate", Boolean.toString(beSmart));
	}
	
	/**
	 * Makes a frame visible via a runnable for {@link SwingUtilities}.
	 */
	public static void showFrame(final Window window) {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				window.setVisible(true);
			}
		});
	}

	/** 
	 * Returns the default "Label.font" as used by Swing's UIManager. 
	 */
	public static Font getUIFont() {
		return (Font)UIManager.get("Label.font");
		// and NOT: return (Font)UIManager.getLookAndFeelDefaults().get("Label.font");
	}
	
	/** 
	 * Returns an instance of a mono-spaced font (like Courier) with the default font size.
	 */
	public static Font getUIFontMonoSpaced() {
		return new Font("Monospaced", 0, getUIFont().getSize());
	}

	/** 
	 * Adjust all fonts for displaying to the given size.
	 * <br>Use with care!
	 */ 
	public static void setUIFontSize(final int size) {

		final java.util.Enumeration<Object> keys = UIManager.getLookAndFeelDefaults().keys();
		while (keys.hasMoreElements()) {
			final Object key = keys.nextElement();
			final Object value = UIManager.get(key);
			if (value instanceof Font) {
				final Font ifont = (Font)value;
				final Font ofont = ifont.deriveFont((float)size);
				UIManager.put(key, ofont);
			}
		}    
	}
	
	/** 
	 * Returns a string with all fonts used by Swing's UIManager.
	 */
	public static String getUIFonts() {
		
		final StringBuffer fonts = new StringBuffer(128);
		fonts.append("Default font: ");
		fonts.append(getUIFont().toString());
		final Enumeration<Object> keys = UIManager.getLookAndFeelDefaults().keys();
		String lf = System.getProperty("line.separator");
		while (keys.hasMoreElements()) {
			final Object key = keys.nextElement();
			final Object value = UIManager.get(key);
			if (value instanceof Font) {
				final Font ifont = (Font)value;
				fonts.append(lf + key.toString() + " " + ifont.getName() + " " + ifont.getStyle() + " " + ifont.getSize());				
			}
		}
		return fonts.toString();
	}

	/** Convenience method for {@link #setUIFont(Font)} */
	public static void setUIFont(String name, int style, int size) {
		final Font f = new Font(name, style, size);
		setUIFont(f);
	}

	/** 
	 * Sets all used fonts for displaying to the specified font df
	 * <br>Use with care! 
	 */
	public static void setUIFont(final Font df) {

		setUIFontSize(df.getSize());
		final Enumeration<Object> keys = UIManager.getLookAndFeelDefaults().keys();
		while (keys.hasMoreElements()) {
			final Object key = keys.nextElement();
			final Object value = UIManager.get(key);
			if (value instanceof Font) {
				final Font ifont = (Font)value;
				final Font ofont = new FontUIResource(df.getName(), ifont.getStyle(), df.getSize());
				UIManager.put(key, ofont);
			}
		} 
	}
	
	public static void applyOkCancelListener(JDialog dialog, JButton OK, JButton cancel, ActionListener listener) {
		
		dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		dialog.addWindowListener(new WindowCloseAdapter(cancel, listener));
		dialog.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancel);
		dialog.getRootPane().getActionMap().put(cancel, new KeyAction(cancel, listener));
		dialog.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), OK);
		dialog.getRootPane().getActionMap().put(OK, new KeyAction(OK, listener));
		OK.addActionListener(listener);
		cancel.addActionListener(listener);
	}
	
	/**
	 * Requesting focus in a Window should always be done last, after repainting etc.
	 * This method calls {@link Component#requestFocusInWindow()} via "invokeLater" 
	 * which should make it the last UI action to perform and make it more likely
	 * that the focus request works.
	 * <br>See also {@link #requestFocusSelectAll(JTextField)}.
	 */
	public static void requestFocus(final Component c) {
		SwingUtilities.invokeLater(new Runnable() { @Override public void run() { c.requestFocusInWindow(); }});
	}
	
	/**
	 * Same as {@link #requestFocus(Component)} but also selects all text in the text-field.
	 */
	public static void requestFocusSelectAll(final JTextField c) {
		
		SwingUtilities.invokeLater(new Runnable() { 
			@Override public void run() { 
				c.requestFocusInWindow();
				int l = c.getDocument().getLength();
				if (l > 0) {
					c.setSelectionStart(0);
					c.setSelectionEnd(l);
				}
			}
		});
	}

	
	/**
	 * Walks the component tree and applies the modifier.
	 * Modifier is applied to root/parent first (child/leaf first can give blank screens).
	 * @param target the component or container to modify
	 * @param modifier the component modifier
	 */
	public static void modifyComponentTree(Component target, ComponentModifier modifier) {

		if (target instanceof Container) {
			synchronized(target.getTreeLock()) {
				modifyContainerTreeRecursive((Container)target, modifier);
			}
		} else {
			modifier.setComponent(target);
			modifier.modify();
		}
	}

	/**
	 * Separate recursive function, lock on component tree is only needed once for root component/container.
	 */
	private static void modifyContainerTreeRecursive(Container target, final ComponentModifier modifier) {
		
		modifier.setComponent(target);
		modifier.modify();
		for (Component c : target.getComponents()) {
			modifier.setComponent(c);
			modifier.modify();
			if (c instanceof Container) {
				modifyContainerTreeRecursive((Container)c, modifier);
			}
		}
	}

	public static final Font borderFont = SwingUtils.getUIFont().deriveFont(10.0f);

	public static <T extends JComponent> T addTitledBorder(T c, String title) {
		return addTitledBorder(c, title, Color.BLACK);
	}
	
	public static <T extends JComponent> T addTitledBorder(T c, String title, Color color) {
		return addTitledBorder(c, title, color, 2, borderFont);
	}

	/**
	 * @param color If null, the default Swing border is used.
	 */
	public static <T extends JComponent> T addTitledBorder(T c, String title, Color color, int lineBorderSize, Font borderFont) {
		
		TitledBorder tb;
		if (color == null) {
			tb = new TitledBorder(null, title, TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, borderFont);
		} else {
			tb = new TitledBorder(new LineBorder(color, lineBorderSize), title, TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, borderFont);
		}
		c.setBorder(tb);
		return c;
	}
	
	/**
	 * If icon is an instance of {@link ImageIcon}, 
	 * returns an icon scaled to the height of the component.
	 * Else return the icon given.
	 */
	public static Icon scaleToHeight(JComponent c, Icon icon) {
		
		Icon sizedIcon = icon;
		if (icon instanceof ImageIcon) {
			int h = c.getPreferredSize().height - c.getInsets().top	- c.getInsets().bottom;
			sizedIcon = new ImageIcon(((ImageIcon)icon).getImage().getScaledInstance(-1, h, Image.SCALE_SMOOTH));
		}
		return sizedIcon;
	}

	/**
	 * Registers an action for a key-stroke in a component.
	 * @param actionName The name for the action related to the keystroke. If null, the keystroke description is used.
	 * @param focusType e.g. {@link JComponent#WHEN_FOCUSED}
	 * @param ks The keystroke that activates the action.
	 */
	public static void setKeyAction(JComponent component, Action action, String actionName, 
			int focusType, KeyStroke ks) {
		
		if (actionName == null) {
			actionName = ks.toString();
		}
		component.getInputMap(focusType).put(ks, actionName);
		component.getActionMap().put(actionName, action);
	}

	/**
	 * Creates a {@link RelayAction} with the action-listener set for the keystroke in origin.
	 * @return Can return null when there is no action-listener for the keystroke.
	 */
	public static Action createRelayActionForKey(JComponent origin, KeyStroke ks) {
		
		Action action = null;
		ActionListener al = origin.getActionForKeyStroke(ks);
		if (al == null) {
			log.info("No action for " + ks + " in component " + origin);
		} else {
			action = new RelayAction(origin, al);
		}
		return action;
	}
	
	/**
	 * Creates and registers an action for the relayer component 
	 * that forwards action-events for the specified keystroke to the origin component.
	 * The origin component must have an action-listener for the specified keystroke.
	 */
	public static void relayKeyAction(JComponent origin, JComponent relayer, KeyStroke ks) {
		relayKeyAction(origin, relayer, JComponent.WHEN_FOCUSED, ks);
	}
	
	/**
	 * Creates and registers an action for the relayer component 
	 * that forwards action-events for the specified keystroke to the origin component.
	 * The origin component must have an action-listener for the specified keystroke.
	 * @param focusType The {@link JComponent}'s focus-type (e.g. {@link JComponent#WHEN_FOCUSED})
	 * for the relayer component.
	 */
	public static void relayKeyAction(JComponent origin, JComponent relayer, int focusType, KeyStroke ks) {
		
		Action action = createRelayActionForKey(origin, ks);
		if (action != null) {
			String actionKey = ks.toString();
			relayer.getInputMap(focusType).put(ks, actionKey);
			relayer.getActionMap().put(actionKey, action);
		}
	}
	
	/**
	 * Wraps the given runnable in a {@link RunCatched}
	 * and invokes it later on the {@link EventQueue}.
	 */
	public static void runLater(Runnable r) {
		
		EventQueue.invokeLater(new RunCatched(r));
	}
	
	/** 
	 * Uses the value returned via the component's getText() method to set a Mnemonic key.
	 * The character following the first '&' charcater is used as Mnemonic key,
	 * but this only works for characters in the range a..Z
	 * If a Mnemonic key is found, the '&' character is removed from the text.
	 * @param textComponent
	 */
	public static void setMnemonic(AbstractButton textComponent) {
		
		String label = textComponent.getText();
		if (label == null || label.isEmpty() 
				|| !label.contains("&")
				|| label.indexOf('&') == label.length()-1) {
			
			return;
		}
		char ch = label.charAt(label.indexOf('&')+1);
		if (!Character.isLetter(ch)) {
			return;
		}
		int ke = getKeyEvent(ch);
		if (ke != Integer.MIN_VALUE) {
			label = label.substring(0, label.indexOf('&')) + label.substring(label.indexOf('&')+1, label.length());
			textComponent.setText(label);
			textComponent.setMnemonic(ke);
		}
	}
	
	/** 
	 * Returns the KeyEvent-code (only for VK_a..Z).
	 * If no key-event could be found, {@link Integer#MIN_VALUE} is returned. 
	 */
	public static int getKeyEvent(Character ch) {
		
		int ke = Integer.MIN_VALUE;
		try {
			Field f = KeyEvent.class.getField("VK_" + Character.toUpperCase(ch));
		    f.setAccessible(true);
		    ke = (Integer) f.get(null); 
		} catch (Exception e) {}
		return ke;
	}
	
	/**
	 * Unit-size set to 1024.
	 * Copied from http://stackoverflow.com/questions/3758606/how-to-convert-byte-size-into-human-readable-format-in-java
	 */
	public static String humanReadableByteCount(long bytes) {
		return humanReadableByteCount(bytes, false);
	}

	/**
	 * Copied from http://stackoverflow.com/questions/3758606/how-to-convert-byte-size-into-human-readable-format-in-java
	 * @param si if true 1000 is used as unit, else 1024.
	 */
	public static String humanReadableByteCount(long bytes, boolean si) {

		int unit = si ? 1000 : 1024;
		if (bytes < unit) return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
	
	/**
	 * Calls {@link #ensureVisibleBounds(Window, boolean)} with intersect-only set to false.
	 */
	public static void ensureBoundsOnDisplay(Window w) {
		ensureVisibleBounds(w, false);
	}

	/**
	 * Calls {@link #ensureVisibleBounds(Window, boolean)} with intersect-only set to true.
	 */
	public static void ensureBoundsPartialOnDisplay(final Window w) {
		ensureVisibleBounds(w, true);
	}

	/**
	 * Checks that the window-bounds are (partially) within {@link #SCREEN_SIZE_TOTAL}.
	 * This is needed in case window-bounds from loaded preferences are used but user has changed from multi-display
	 * to single-display. Any window on the second display will show off-screen and is not visible.
	 * <br>If window-bounds are (partially) outside the display-screen,
	 * the window is packed (size set to preferred size) and location is set to middle of the screen. 
	 * @param w The window to check the bounds on.
	 * @param intersectOnly If true, window bounds only need to overlap partially with display-screen.
	 */
	public static void ensureVisibleBounds(final Window w, final boolean intersectOnly) {
		
		runLater(new Runnable() {
			@Override public void run() {
				Rectangle r = w.getBounds();
				boolean validBounds = (intersectOnly ? SCREEN_SIZE_TOTAL.intersects(r) : SCREEN_SIZE_TOTAL.contains(r));
				if (!validBounds) {
					w.pack();
					w.setLocationRelativeTo(null);
				}
			}
		});
	}

}
