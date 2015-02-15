package nl.fw.swing.demo;

import java.awt.EventQueue;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import nl.fw.swing.SwingUtils;
import nl.fw.swing.hvlayout.CForm;
import nl.fw.swing.hvlayout.CSize;
import nl.fw.swing.hvlayout.HBox;
import nl.fw.swing.hvlayout.HVSize;
import nl.fw.swing.hvlayout.VBox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// http://stackoverflow.com/questions/25147384/stacking-inserting-jcomponents-from-bottom-up-similar-to-what-a-cli-does
@SuppressWarnings({ "serial", "unused" })
public class AddComponentInMiddle extends JFrame implements ActionListener {

	protected static Logger log = LoggerFactory.getLogger(AddComponentInMiddle.class);

	public static void main(String[] args) {

		SwingUtils.setDefaultUILookAndFeel();
		SwingUtils.setUIFontSize(12);
		HVSize.alignLineHeight();
		SwingUtils.showFrame(new AddComponentInMiddle().build());
	}

	public AddComponentInMiddle() {
		super(AddComponentInMiddle.class.getName());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	VBox labels;
	JScrollPane labelScroller;
	JTextField labelIn;

	public AddComponentInMiddle build() {

		CSize cs = new CSize();
		CForm form = new CForm(new VBox(new Insets(2, 4, 2, 4)));
		form.add(labelScroller = new JScrollPane(labels = new VBox()));
		form.csize().setAreaSize();
		form.addChild(new HBox());
		form.add(labelIn = new JTextField()).csize().setLineSize();
		labelIn.addActionListener(this);

		setContentPane(form.getRoot());

		pack();
		setLocationByPlatform(true);
		log.debug(getClass().getName() + " build.");
		return this;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String text = labelIn.getText();
		labelIn.setText("");
		int i = Integer.MIN_VALUE;
		boolean remove = text.startsWith("r");
		if (remove) { text = text.substring(1); }
		try { i = Integer.valueOf(text); } catch (Exception ignored) {}
		JLabel l = new JLabel(text);
		if (i != Integer.MIN_VALUE) {
			if (remove) {
				labels.remove(i);
			} else {
				labels.add(l, i);
			}
		} else {
			labels.add(l);
		}
		labels.revalidate();
		labels.repaint();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				JScrollBar vertical = labelScroller.getVerticalScrollBar();
				vertical.setValue(vertical.getMaximum());
			}
		});
	}

}
