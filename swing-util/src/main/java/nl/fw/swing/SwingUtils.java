package nl.fw.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.Enumeration;

import javax.swing.JComponent;
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
	 * Applies component orientation to a component and all it's children (in containers).
	 * A call to {@link #revalidate(Component)} migth be required for a call to
	 * {@link Component#repaint()} to work in case the given component is not a top-level component. 
	 * @see {@link ModifierOrientation}, {@link #modifyComponentTree(Component, ComponentModifier)}
	 */
	public static void applyComponentOrientation(Component c, ComponentOrientation cor) {
		modifyComponentTree(c, new ModifierOrientation(cor));
	}

	/**
	 * Calls {@link Component#invalidate()} and {@link Component#validate()}
	 * for the given component and all it's children (in containers).
	 * The given component should be repainted {@link Component#repaint()} afterwards.
	 * @see {@link ModifierRevalidate}, {@link #modifyComponentTree(Component, ComponentModifier)}
	 */
	public static void revalidate(Component c) {
		modifyComponentTree(c, new ModifierRevalidate());
	}
	
	/**
	 * Walks the component tree and applies the modifier.
	 * Modifier is applier to root/parent first, child/leaf first gives blank screens.
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
		return addTitledBorder(c, title, color, 2);
	}

	/**
	 * @param color If null, the default Swing border is used.
	 */
	public static JComponent addTitledBorder(JComponent c, String title, Color color, int lineBorderSize) {
		
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


}