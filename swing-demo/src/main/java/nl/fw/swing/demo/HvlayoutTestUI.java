package nl.fw.swing.demo;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import nl.fw.swing.SwingUtils;
import nl.fw.swing.component.JMultiLineLabel;
import nl.fw.swing.component.TButton;
import nl.fw.swing.component.TLabel;
import nl.fw.swing.hvlayout.CSize;
import nl.fw.swing.hvlayout.HBox;
import nl.fw.swing.hvlayout.CForm;
import nl.fw.swing.hvlayout.HVSize;
import nl.fw.swing.hvlayout.VBox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({ "serial"})
public class HvlayoutTestUI extends JFrame {

	protected static Logger log = LoggerFactory.getLogger(AddressBookDemo.class);

	public static final String lf = System.getProperty("line.separator");

	public static void main(String[] args) {
		
		SwingUtils.smartRevalidate(true);
		SwingUtils.tooltipLinger(0);
		SwingUtils.setDefaultUILookAndFeel();
		SwingUtils.showFrame(new HvlayoutTestUI().buildAndSetLocation());
	}
	
	private boolean rightToLeft, showBigFont;
	private boolean showTextScroller = false;
	private Container textWithOptionalScroller;
	private final Font sysFont, bigFont;
	private JFrame frameInstance;
	// Need fixed pointer to checkboxes, else runnables in actions get null-references.
	private final ActionCB rtolACB = new ActionCB();
	private final ActionCB showTextScrollerACB = new ActionCB();
	private final ActionCB showBigFontACB = new ActionCB();
	
	public HvlayoutTestUI() {
		super("HVLayout test UI");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		sysFont = SwingUtils.getUIFont();
		bigFont = sysFont.deriveFont(16.0f);
		frameInstance = this;
	}
	
	private void build() {
		
		CSize cs = new CSize();
		CForm form = new CForm(new VBox(new Insets(2, 4, 2, 4)), cs);
		form.addChild(new HBox());
		addTitledBorder(form.get(), "Leading box", Color.PINK);
		// keep original preferred width (cs.pref().width) so that all text next to button shows
		form.add(buildOrientationCheckBox()).csize().setLineSize(cs.pref().width);
		form.add(buildBigFontCheckBox()).csize().setLineSize();
		
		form.up().addChild(new HBox(HBox.CENTER));
		//addTitledBorder(form.get(), "Centered box", Color.ORANGE);
		form.add(new JMultiLineLabel(CENTERED_MULTI_LINE)).csize().setMinButtonSize();
		
		form.up().addChild(textWithOptionalScroller = new VBox());
		addTitledBorder(form.get(), "Text with optional scroller", Color.GREEN);
		buildTextScroller(cs);
		
		form.up().addChild(new HBox());
		addTitledBorder(form.get(), "Buttons and textfields 1", Color.BLUE);
		form.addChild(new HBox());
		form.add(new TLabel("Status", HBox.TRAILING)).csize().setFixedButtonSize(0.5f, 1.0f);
		form.add(new JTextField("Testin' HVLayout")).csize().setLineSize();
		form.up().addChild(new HBox());
		int lh = cs.getHvsize().getLineHeight();
		form.add(new TButton("1")).csize().setFixed(lh, lh);
		form.add(new JTextField("<- one text field two buttons ->"))
			.csize().setLineSize().setMinWidth(lh);
		form.add(new TButton("2")).csize().setFixed(lh, lh);
		
		form.up().up().addChild(new HBox());
		addTitledBorder(form.get(), "Buttons and textfields copy", Color.BLUE);
		form.addChild(new HBox());
		form.add(new TLabel("Details", HBox.TRAILING)).csize().setFixedButtonSize(0.5f, 1.0f);
		form.add(new JTextField("^ Same kind of row as above ^")).csize().setLineSize();
		form.up().addChild(new HBox());
		form.add(new TButton("3")).csize().setFixed(lh, lh);
		form.add(new JTextField("^ Still the same kind of row as above ^"))
			.csize().setLineSize().setMinWidth(lh);
		form.add(new TButton("4")).csize().setFixed(lh, lh);
		form.up().up();
		
		form.add(new JScrollPane(new JMultiLineLabel(DEMO_TEXT, true)))
			.csize().setAreaSize(1.0f, 3.3f).fixedMinHeight();
		
		form.add(new JMultiLineLabel(CENTER_REVERSE_TEXT)).csize().setMinWidthButton();
		form.addChild(new HBox(HBox.CENTER));
		addTitledBorder(form.get(), "Centered box", Color.ORANGE);
		form.add(new TButton("CenterD")).csize().setFixedButtonSize();
		form.add(new JTextField("Centered demo")).csize().setLineSize();
		form.add(new TButton("Dc")).csize().setFixed(lh, lh);
		
		form.up().add(new TLabel("Below the same line but not centered")).csize().setLineSize();
		form.addChild(new HBox());
		form.add(new TButton("NoCenterD")).csize().setFixedButtonSize();
		form.add(new JTextField("Not centered demo")).csize().setLineSize();
		form.add(new TButton("Nc")).csize().setFixed(lh, lh);
		
		form.up().add(new TLabel("Below three horizontal-boxes with different orientations and containing some buttons")).csize().setLineSize();
		form.addChild(new HBox()).addChild(new HBox());
		addTitledBorder(form.get(), "Leading box", Color.PINK);
		form.add(new TButton("Leading")).csize().setFixedButtonSize();
		form.up().addChild(new HBox(HBox.CENTER));
		addTitledBorder(form.get(), "Centered box", Color.ORANGE);
		form.add(new TButton("Center")).csize().setFixedButtonSize();
		form.up().addChild(new HBox(HBox.TRAILING));
		addTitledBorder(form.get(), "Trailing box", Color.MAGENTA);
		form.add(new TButton("Trailing")).csize().setFixedButtonSize();
		setContentPane(new JScrollPane(form.getRoot()));
		
		log.debug(getTitle() + " build.");
	}
	
	public HvlayoutTestUI buildAndSetLocation() {
		
		build();
		pack();
		setMaximumSize(new Dimension(CSize.MAX_WIDTH, CSize.MAX_HEIGHT));
		setLocationByPlatform(true);
		return this;
	}
	
	private void rebuild() {
		
		getContentPane().removeAll();
		build();
		frameInstance.applyComponentOrientation(getComponentOrientation());
		revalidate();
		repaint();
	}
	
	@Override
	public ComponentOrientation getComponentOrientation() {
		return (rightToLeft ? ComponentOrientation.RIGHT_TO_LEFT : ComponentOrientation.LEFT_TO_RIGHT);
	}
	
	private JCheckBox buildOrientationCheckBox() {
		
		return buildActionCB(rtolACB, rightToLeft, "Component orientation right to left",
				// Need to use Java 8 Lambda expressions badly.
				new Runnable() { @Override public void run() { 
					rightToLeft = rtolACB.cb.isSelected();
				}}, 
				new Runnable() { @Override public void run() {
					frameInstance.applyComponentOrientation(getComponentOrientation());
					frameInstance.repaint();
					SwingUtils.requestFocus(rtolACB.cb);
				}});
	}
	
	private JCheckBox buildBigFontCheckBox() {
		
		return buildActionCB(showBigFontACB, showBigFont, "Big font",
				new Runnable() { @Override public void run() { 
					showBigFont = showBigFontACB.cb.isSelected();
				}}, 
				new Runnable() { @Override public void run() {
					if (showBigFont) {
						SwingUtils.setUIFont(bigFont);
					} else {
						SwingUtils.setUIFont(sysFont);
					}
					HVSize.alignLineHeight();
					rebuild();
					SwingUtils.requestFocus(showBigFontACB.cb);
				}});
	}

	final String SCROLLER_MULTI_LINE = "This is a multi-line label."
			+ lf + "It uses a customized text area as backend."
			+ lf + "It can show a tooltip when this text is not fully readable"
			+ lf + "And if place inside a scroller, text can be any size as it will always be possible to make it visible.";

	private void buildTextScroller(CSize cs) {

		textWithOptionalScroller.add(cs.set(buildTextScrollerCheckBox()).setLineSize().get());
		cs.set(new JMultiLineLabel(SCROLLER_MULTI_LINE)).setMinButtonSize();
		if (showTextScroller) {
			textWithOptionalScroller.add(cs.set(new JScrollPane(cs.get())).setMaxHeight(5.0f).get());
		} else {
			textWithOptionalScroller.add(cs.get());
		}
	}
	
	private JCheckBox buildTextScrollerCheckBox() {
		
		return buildActionCB(showTextScrollerACB, showTextScroller, "Put text shown below inside scroller",
				new Runnable() { @Override public void run() { 
					showTextScroller = showTextScrollerACB.cb.isSelected(); 
				}}, 
				new Runnable() { @Override public void run() {
					textWithOptionalScroller.removeAll();
					buildTextScroller(new CSize());
					textWithOptionalScroller.applyComponentOrientation(getComponentOrientation());
					textWithOptionalScroller.revalidate();
					textWithOptionalScroller.repaint();
					SwingUtils.requestFocus(showTextScrollerACB.cb);
				}});
	}

	private JCheckBox buildActionCB(ActionCB acb, boolean selected, String text, 
			final Runnable action, final Runnable uiUpdate) {
		
		acb.text = text;
		acb.marked = selected;
		acb.action = action;
		acb.uiUpdate = uiUpdate;
		return acb.build().cb;
	}
	
	public static void addTitledBorder(Component c, String title, Color color) {
		SwingUtils.addTitledBorder((JComponent)c, title, color);
	}

	final String CENTERED_MULTI_LINE = "Centered multi line label example" 
			+ lf + "This label can shrink in height, so you might not see"
			+ lf + "the third and last line of multi label";


	final String DEMO_TEXT = "This is just some demo text to see what happens with the text area when resizing is active.\n" +
			"This is the second line.\n" +
			"The text area has a preferred size of 3 lines.\n" +
			"And a default width.\n" +
			"This is the 5th line, more then preferred.\n" +
			"But still visible if resized.\n";
	
	final String CENTER_REVERSE_TEXT = "Lines that are centered or reversed (opposite lines axis)\n" +
			"keep their preferred width (else you might not see the difference with normal outlined lines)";

}
