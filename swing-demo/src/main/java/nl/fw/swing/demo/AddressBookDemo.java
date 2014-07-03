package nl.fw.swing.demo;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import nl.fw.swing.SwingUtils;
import nl.fw.swing.component.TButton;
import nl.fw.swing.component.TLabel;
import nl.fw.swing.hvlayout.CSize;
import nl.fw.swing.hvlayout.HBox;
import nl.fw.swing.hvlayout.CForm;
import nl.fw.swing.hvlayout.HVSize;
import nl.fw.swing.hvlayout.VBox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runnable demo form to demonstrate HVLayout. 
 * See {@code http://weblogs.java.net/blog/joconner/archive/2006/10/more_informatio.html}
 * for the intention of this form.
 * <br> This form is NOT a good example of how to design a GUI, but it does
 * provide a baseline to compare this layout manager with other layout managers. 
 * <br> Note that this implementation is a bit awkward due to the constraints given
 * by John O'Conner. A (better) re-designed form would be easier to implement.
 * E.g. to accomodate a proper tab-order, some components are lined out in one
 * horizontal container where using two horizontal containers would have been easier.
 */
@SuppressWarnings({ "serial", "unused" })
public class AddressBookDemo extends JFrame {

	protected static Logger log = LoggerFactory.getLogger(AddressBookDemo.class);
	
	/**
	 * Set to true to use the font set by fontName, fontStyle and fontSize.
	 * And see how HVLayout adapts to it.
	 */
	public static final boolean useCustomFont = true;
	public static final String fontName = "Arial";
	public static final int fontStyle = Font.PLAIN;
	public static final int fontSize = 12;

	public static final String lf = System.getProperty("line.separator");

	public static void main(String[] args) {
		
		SwingUtils.setDefaultUILookAndFeel();
		if (useCustomFont) {
			SwingUtils.setUIFont(fontName, fontStyle, fontSize);
			HVSize.alignLineHeight();
		}
		SwingUtils.showFrame(new AddressBookDemo().build());
	}

	public static final String names[] = { "Bunny, Bugs", "Cat, Sylvester", 
		"Coyote, Wile", "Devil, Tasmanian", "Duck, Daffy", "Fudd, Elmer",
		"Le Pew, Pepé", "Martian, Marvin" };

	public AddressBookDemo() {
		super("Address Book demo");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	public AddressBookDemo build() {
		
		CSize cs = new CSize(); // used to size components
		JList<String> namesList;
		CForm form = new CForm(new VBox(new Insets(2, 4, 2, 4)), cs);
		addTitledBorder(form.get(), "Main vertical box", Color.BLACK);
		form.addChild(new HBox());
		addTitledBorder(form.get(), "General horizontal box", Color.RED);
		
		JScrollPane scroller = new JScrollPane(namesList = new JList<String>(names));
		form.add(scroller);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		cs.set(scroller).setAreaSize(0.8f, 2.0f).fixedWidth();
		
		form.addChild(new VBox()); // the main vertical box putting all Hbox/lines below each other
		addTitledBorder(form.get(), "Address vertical box", Color.GREEN);
		
		form.addChild(new HBox()); // the current Hbox/line to add component
		form.add(createLabelTextLinePair(cs, "Last name", "Martian"));
		form.add(createLabelTextLinePair(cs, "First name", "Marvin"));
		
		// Street and house-number all on the left.
		// Make sure street and house-number fields are in their own Hbox, see addMirrorLine further on. 
		form.up().addChild(new HBox()).addChild(new HBox());
		form.add(createTextFieldLabel(cs, "Street & house number"));
		
		// Scaling is required, else preferred size shows too wide (3 times line-width 
		// (street text-field, house-number text-field and mirror-line) instead of the intended 2 times line-width). 
		form.add(createTextField(cs, "Dust Drive")).csize().scaleWidth(0.75f);
		form.add(createTextField(cs, "42")).csize().scaleWidth(0.25f);
		
		// Keep street and number to the left
		// The mirror line must be in a separate Hbox, just like the street and house-number fields,
		// else the house-number field will not align with other text-fields when growing/shrinking the window.
		addMirrorLine(cs, form.up().get());
		
		// Address 2 goes full width
		form.up().addChild(new HBox());		
		form.add(createLabelTextLinePair(cs, "Address 2", ""));
		
		form.up().addChild(new HBox());		
		form.add(createLabelTextLinePair(cs, "City", "Red Rock"));
		addMirrorLine(cs, form.get());

		form.up().addChild(new HBox());		
		form.add(createLabelTextLinePair(cs, "State", "Looney Tunes"));
		form.add(createLabelTextLinePair(cs, "Postal Code", "ZZY BRBR"));
		
		form.up().addChild(new HBox());		
		form.add(createLabelTextLinePair(cs, "Country", "Warner Bros"));
		addMirrorLine(cs, form.get());

		// Add a notes text area
		form.up().add(new JLabel("Notes:")).csize().setFixed().growWidth();
		form.add(new JScrollPane(new JTextArea(getMarvinNotes()))).csize()
				.setAreaSize(1.0f, 2.0f).fixedMinHeight().setMaxHeight(4.0f);

		// Add empty separator line
		//form.add(cs.set(new Canvas()).setLineSize().get());
		
		// Add some buttons
		form.addChild(new HBox());
		addTitledBorder(form.get(), "Button box", Color.BLUE);
		form.addChild(new HBox());
		addTitledBorder(form.get(), "Normal box", Color.CYAN);
		TButton b = null;
		String[] blabels = new String[] {"Add", "Modify", "View details" };
		for (int i = 0; i < blabels.length; i ++) {
			b = new TButton(blabels[i]);
			form.add(b).csize().setFixed();
		}
		
		// Add a floating delete button 
		form.up().addChild(new HBox(SwingConstants.CENTER));		
		addTitledBorder(form.get(), "Centered box", Color.ORANGE);
		form.add(new TButton("Delete")).csize().setFixed();
		// Add canvas so that delete-button is not (always) in the right-bottom corner
		// A "new Canvas()" does not show a TitledBorder, an empty label does. 
		form.add(new TLabel("")).csize().setFixed().setMinWidth(1);
		addTitledBorder(form.getLast(), "Canvas", Color.GRAY);

		// Add a scroller around the complete window
		scroller = new JScrollPane(form.getRoot());
		setContentPane(scroller);
		//setContentPane(form.getRoot());
		
		pack();
		// Put initial focus on the list with Marvin the Martian selected.
	    namesList.setSelectedValue(names[names.length-1], true);
		namesList.requestFocusInWindow();
		setLocationByPlatform(true);
		//applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		log.debug(getTitle() + " build.");
		return this;
	}
	
	/* *** static helper methods *** */
	
	public static final Font borderFont = SwingUtils.getUIFont().deriveFont(10.0f);

	public static void addTitledBorder(Component c, String title, Color color) {
		SwingUtils.addTitledBorder((JComponent)c, title, color);
	}
	
	public static TLabel createTextFieldLabel(CSize cs, String labelText) {
		
		TLabel c = new TLabel(labelText, SwingConstants.TRAILING);
		cs.set(c).setFixed();
		return c;
	}
	
	public static JTextField createTextField(CSize cs, String lineText) {
		
		JTextField c = new JTextField(lineText);
		cs.set(c).setLineSize();
		return c;
	}
	
	public static HBox createLabelTextLinePair(CSize cs, String labelText, String lineText) {
		return createLabelTextLinePair(createTextFieldLabel(cs, labelText), createTextField(cs, lineText));
	}
	
	public static HBox createLabelTextLinePair(JLabel label, JTextField tfield) {
		
		HBox b = new HBox();
		b.add(label);
		b.add(tfield);
		return b;
	}
	
	public static void addMirrorLine(CSize cs, Container c) {

		c.add(cs.set(new HBox()).copySize(c).get());
	}
	
	public static String getMarvinNotes() {
		
		StringBuilder sb = new StringBuilder("Did secret missions for the M3 Squad.");
		sb.append(lf).append('.');
		sb.append(lf).append("..");
		sb.append(lf).append("...");
		sb.append(lf).append("....");
		sb.append(lf).append(".....");
		sb.append(lf).append("But is mostly evil.");
		sb.append(lf).append("Has a dog called K-9.");
		return sb.toString();
	}
	
}

/*
// Buttons of different sizes
// This demonstrates that the H/Vlayout calculations do not depends on one line height for all,
// all calculations are based on (preferred) component sizes.

box.add(addressLine = new Hbox());

b = new TButton("Modify");

// Set extra large font.
b.setFont(b.getFont().deriveFont((float)b.getFont().getSize() + 8));

// Calculate different (preferred) size for button with extra large font.
int cheight = Hvsize.getComponentHeight(b);
// Make width proportional to height
int w = (int)(cs.getHvsize().getButtonWidthFactor() * cheight);
// TButton has no insets, add 4 to height to keep some room below text.
int h = cheight + 4;

addressLine.add(cs.set(b).setFixedSize(w, h).get());

// Characters "gjfF" demonstrate there is enough room (line-height) to display characters in full.
b = new TButton("gjfF Delete");
b.setFont(b.getFont().deriveFont((float)b.getFont().getSize() + 16));
cheight = Hvsize.getComponentHeight(b);
h = cheight + 4;
w = (int)(cs.getHvsize().getButtonWidthFactor() * cheight);
addressLine.add(cs.set(b).setFixedSize(w, h).get());
*/

