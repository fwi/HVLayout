package nl.fw.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwingUtils {

	private SwingUtils() {}

	private static final Logger log = LoggerFactory.getLogger(SwingUtils.class);
	
	public static Dimension SCREEN_SIZE_PRIMARY = Toolkit.getDefaultToolkit().getScreenSize();
	public static Dimension SCREEN_SIZE_TOTAL = calculateTotalScreenSize();

	public static Dimension calculateTotalScreenSize() {
		
		Rectangle virtualBounds = new Rectangle();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		for (int j = 0; j < gs.length; j++) { 
			GraphicsDevice gd = gs[j];
			GraphicsConfiguration[] gc = gd.getConfigurations();
			for (int i = 0; i < gc.length; i++) {
				virtualBounds =	virtualBounds.union(gc[i].getBounds());
			}
		}
		Dimension d = new Dimension();
		d.width = virtualBounds.width;
		d.height = virtualBounds.height;
		return d;
	}
	
	public static void setDefaultUILookAndFeel() {
		
		try {
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
			log.debug("System look and feel not availalbe: " + e);
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

	public static JComponent addTitledBorder(JComponent c, String title) {
		return addTitledBorder(c, title, Color.BLACK);
	}
	
	public static JComponent addTitledBorder(JComponent c, String title, Color color) {
		return addTitledBorder(c, title, color, 2, borderFont);
	}

	/**
	 * @param color If null, the default Swing border is used.
	 */
	public static JComponent addTitledBorder(JComponent c, String title, Color color, int lineBorderSize, Font borderFont) {
		
		JComponent jc = (JComponent)c;
		TitledBorder tb;
		if (color == null) {
			tb = new TitledBorder(null, title, TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, borderFont);
		} else {
			tb = new TitledBorder(new LineBorder(color, lineBorderSize), title, TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, borderFont);
		}
		jc.setBorder(tb);
		return jc;
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
	
}
