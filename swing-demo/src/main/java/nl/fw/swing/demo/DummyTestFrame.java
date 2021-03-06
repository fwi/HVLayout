package nl.fw.swing.demo;

import java.awt.Canvas;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import nl.fw.swing.SwingUtils;
import nl.fw.swing.component.TButton;
import nl.fw.swing.component.TLabel;
import nl.fw.swing.hvlayout.CForm;
import nl.fw.swing.hvlayout.CSize;
import nl.fw.swing.hvlayout.HBox;
import nl.fw.swing.hvlayout.HVSize;
import nl.fw.swing.hvlayout.VBox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Try-out class, changes randomly to test / verify HVLayout features.
 * @author fred
 *
 */
@SuppressWarnings("serial")
public class DummyTestFrame extends JFrame {

	protected static Logger log = LoggerFactory.getLogger(DummyTestFrame.class);

	public static void main(String[] args) {
		
		SwingUtils.setDefaultUILookAndFeel();
		SwingUtils.setUIFontSize(12);
		HVSize.alignLineHeight();
		SwingUtils.showFrame(new DummyTestFrame().build());
	}

	public DummyTestFrame() {
		super(DummyTestFrame.class.getName());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	public DummyTestFrame build() {
		
		CSize cs = new CSize();
		CForm form = new CForm(new VBox(CForm.MAIN_BOX_INSETS));
		
		// Shrink test
		/*
		form.addChild(new HBox());
		form.add(new JTextField("Text line default")).csize().setLineSize().scaleWidth(2.0f);
		form.add(new JTextField("Text line smaller min width")).csize().setLineSize().scaleWidth(2.0f).setMinWidth(0.5f);
		form.add(new JTextField("Text line bigger min width")).csize().setLineSize().scaleWidth(2.0f).setMinWidth(1.5f);
		form.up();
		*/
		
		form.addChild(new HBox());
		form.add(new JTextField("Leading text line unlimited width")).csize().setLineSize().setPrefWidth(2.0f);
		form.addChild(new HBox(HBox.TRAILING));
		form.add(new JTextField("Trailing text line limited width")).csize().setLineSize().setMaxWidth(2.0f);
		form.toRoot();
		
		JList<String> list = new JList<String>(new String[] { "A", "B", "C", "D", "E", "F"});
		// there is a little jump hen resizing between "almost max height" to "max height".
		form.add(cs.set(list).setAreaSize(0.5f, 2.0f).fixedWidth().setMaxHeight(4.0f).get());

		HBox boxSpaced = new HBox(HBox.CENTER);
		createSpacedBordered(boxSpaced);
		form.addChild(boxSpaced);
		TLabel l = new TLabel("A label", HBox.TRAILING);
		cs.set(l).setButtonSize().setFixedWidth(0.3f);
		form.add(cs.get());
		JTextField tf = new JTextField("A centered text centered");
		cs.set(tf).setLineSize().setPrefWidth(3.0f);
		form.add(cs.get());

		form.up().addChild(new HBox());
		l = new TLabel("A label", HBox.TRAILING);
		cs.set(l).setButtonSize();
		form.add(cs.get());
		tf = new JTextField("A text field A text field A text field A text field A text field A text field A text field");
		cs.set(tf).setLineSize();
		form.add(cs.get());

		form.add(cs.set(new Canvas()).copySize(l).get());
		form.add(cs.set(new Canvas()).copySize(tf).get());
		
		form.up().addChild(new HBox());
		l = new TLabel("B label", HBox.TRAILING);
		cs.set(l).setButtonSize();
		form.add(cs.get());
		tf = new JTextField("B text field A text field A text field A text field A text field A text field A text field");
		cs.set(tf).setLineSize();
		form.add(cs.get());
		
		l = new TLabel("BA label", HBox.TRAILING);
		cs.set(l).setButtonSize();
		form.add(cs.get());
		tf = new JTextField("BA text field A text field A text field A text field A text field A text field A text field");
		cs.set(tf).setLineSize();
		form.add(cs.get());

		form.up().addChild(new HBox());
		form.add(cs.set(new JTextField("Grows width unlimited")).setLineSize(1.0f, 0.0f).get());
		form.add(cs.set(new JTextField("Grows width limited")).setLineSize(0.3f, 1.0f).setMaxWidth(1.0f).get());
		form.add(cs.set(new JTextField("Grows width limited")).setLineSize(0.3f, 1.0f).setMaxWidth(1.0f).get());
		form.add(cs.set(new JTextField("Grows width unlimited")).setLineSize(1.0f, 0.0f).get());

		form.up();
		form.add(cs.set(new JScrollPane(new JTextArea("A text area that grows limited")))
				.setAreaSize(1.0f, 2.0f).setMaxHeight(2.0f).get());

		form.addChild(new HBox());
		for (int i = 0; i < 5; i++) {
			form.add(cs.set(new TButton("Button " + i)).setButtonSize().get());
		}
		
		form.up();
		form.add(cs.set(new JScrollPane(new JTextArea("A text area that grows unlimited")))
				.setAreaSize().get());
		
		setContentPane(form.getRoot());
		
		// applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		pack();
		setLocationByPlatform(true);
		log.debug(getClass().getName() + " build.");
		return this;
	}
	
	private void createSpacedBordered(JComponent c) {
		
		c.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createBevelBorder(BevelBorder.LOWERED), createEmptyBorder()));
	}
	
	private Border createEmptyBorder() {
		return BorderFactory.createEmptyBorder(2, 10, 2, 50);
	}

}
