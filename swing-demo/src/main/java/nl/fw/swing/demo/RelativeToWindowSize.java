package nl.fw.swing.demo;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nl.fw.swing.SwingUtils;
import nl.fw.swing.component.TButton;
import nl.fw.swing.hvlayout.CSize;
import nl.fw.swing.hvlayout.HBox;
import nl.fw.swing.hvlayout.CForm;
import nl.fw.swing.hvlayout.HVSize;
import nl.fw.swing.hvlayout.VBox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http://stackoverflow.com/questions/24462297/layout-relative-to-screensize/
 * @author fred
 *
 */
@SuppressWarnings({ "serial", "unused" })
public class RelativeToWindowSize extends JFrame implements ListSelectionListener {

	protected static Logger log = LoggerFactory.getLogger(DummyTestFrame.class);

	public static void main(String[] args) {
		
		SwingUtils.setDefaultUILookAndFeel();
		SwingUtils.setUIFontSize(12);
		HVSize.alignLineHeight();
		SwingUtils.showFrame(new RelativeToWindowSize().build());
	}

	public RelativeToWindowSize() {
		super(RelativeToWindowSize.class.getName());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	private JList<String> vlist;
	private JTextArea tfield;
	
	public RelativeToWindowSize build() {
		
		CSize cs = new CSize();
		CForm form = new CForm(new VBox(CForm.MAIN_BOX_INSETS), cs);
		addTitledBorder(form.get(), "Vertical box", Color.BLACK);
		form.add(new JScrollPane(
					tfield = new JTextArea("Value that changes with value choosen from list.\nhttp://stackoverflow.com/questions/24462297/layout-relative-to-screensize/")
				)).csize().setAreaSize(1.0f, 2.5f).fixedMinHeight().setMaxHeightLine(4.0f);
		// tfield shows mono-spaced font by default.
		tfield.setFont(SwingUtils.getUIFont());
		form.add(new JScrollPane(vlist = new JList<String>(getListValues())))
				.csize().setAreaSize(1.0f, 5.0f);
		
		form.addChild(new HBox());
		addTitledBorder(form.get(), "Horizontal box", Color.RED);
		form.addChild(new HBox(HBox.CENTER));
		addTitledBorder(form.get(), "Centered box.", Color.BLUE);
		form.add(createButton(cs, "Add"));
		form.add(createButton(cs, "Modify"));
		form.up();
		form.addChild(new HBox(HBox.TRAILING));
		addTitledBorder(form.get(), "Trailing box", Color.GREEN);
		form.add(createButton(cs, "Delete"));
		
		setContentPane(form.getRoot());
		pack();
		setLocationByPlatform(true);
		//applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		vlist.addListSelectionListener(this);
		log.debug(getClass().getName() + " build.");
		return this;
	}
	
	private Component createButton(CSize cs, String text) {
		// For purpose of demo, let button shrink in width.
		return cs.set(new TButton(text)).setButtonSize().shrinkWidth(0.33f).get();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		
		String v = vlist.getSelectedValue();
		StringBuilder sb = new StringBuilder();
		StringBuilder line = new StringBuilder(v);
		for (int i = 0; i < 5; i ++) {
			sb.append(i + 1).append(' ').append(line.toString()).append('\n'); 
			line.append(" ").append(v);
		}
		tfield.setText(sb.toString());
		tfield.setCaretPosition(0);
	}
	
	public static String[] getListValues() {
		
		List<String> l = new ArrayList<String>();
		for (int i = 0; i < 20; i++) {
			l.add("value " + i);
		}
		return l.toArray(new String[l.size()]);
	}
	
	public static final Font borderFont = SwingUtils.getUIFont().deriveFont(10.0f);

	public static void addTitledBorder(Component c, String title, Color color) {
		
		if (c instanceof JComponent) {
			JComponent jc = (JComponent)c;
			TitledBorder tb = new TitledBorder(new LineBorder(color, 2), title, TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, borderFont);
			jc.setBorder(tb);
		}
	}

}
