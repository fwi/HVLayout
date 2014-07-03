package nl.fw.swing.demo;

import java.awt.Canvas;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

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
		CForm form = new CForm(new VBox(new Insets(2, 4, 2, 4)));
		
		JList<String> list = new JList<String>(new String[] { "A", "B", "C", "D", "E", "F"});
		form.add(cs.set(list).setAreaSize(0.5f, 2.0f).fixedWidth().setMaxHeight(4.0f).get());

		form.addChild(new HBox());
		TLabel l = new TLabel("A label", SwingConstants.TRAILING);
		cs.set(l).setFixed();
		form.add(cs.get());
		JTextField tf = new JTextField("A text field A text field A text field A text field A text field A text field A text field");
		cs.set(tf).setLineSize();
		form.add(cs.get());
		
		form.add(cs.set(new Canvas()).copySize(l).get());
		form.add(cs.set(new Canvas()).copySize(tf).get());
		
		form.up().addChild(new HBox());
		l = new TLabel("B label", SwingConstants.TRAILING);
		cs.set(l).setFixed();
		form.add(cs.get());
		tf = new JTextField("B text field A text field A text field A text field A text field A text field A text field");
		cs.set(tf).setLineSize();
		form.add(cs.get());
		
		l = new TLabel("BA label", SwingConstants.TRAILING);
		cs.set(l).setFixed();
		form.add(cs.get());
		tf = new JTextField("BA text field A text field A text field A text field A text field A text field A text field");
		cs.set(tf).setLineSize();
		form.add(cs.get());

		form.up().addChild(new HBox());
		form.add(cs.set(new JTextField("Grows width unlimited")).setLineSize(1.0f, 0.0f).get());
		form.add(cs.set(new JTextField("Grows width limited")).setLineSize(0.3f, 1.0f).setMaxWidth(2.0f).get());
		form.add(cs.set(new JTextField("Grows width limited")).setLineSize(0.3f, 1.0f).setMaxWidth(2.0f).get());
		form.add(cs.set(new JTextField("Grows width unlimited")).setLineSize(1.0f, 0.0f).get());

		form.up();
		form.add(cs.set(new JScrollPane(new JTextArea("A text area that grows limited")))
				.setAreaSize(1.0f, 2.0f).setMaxHeight(3.0f).get());

		form.addChild(new HBox());
		for (int i = 0; i < 5; i++) {
			form.add(cs.set(new TButton("Button " + i)).setFixed().get());
		}
		
		form.up();
		form.add(cs.set(new JScrollPane(new JTextArea("A text area that grows unlimited")))
				.setAreaSize().get());
		
		setContentPane(form.getRoot());
		
		pack();
		setLocationByPlatform(true);
		log.debug(getClass().getName() + " build.");
		return this;
	}
}
