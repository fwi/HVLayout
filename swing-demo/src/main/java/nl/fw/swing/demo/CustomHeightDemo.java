package nl.fw.swing.demo;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

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

@SuppressWarnings("serial")
public class CustomHeightDemo extends JFrame {

	protected static Logger log = LoggerFactory.getLogger(CustomHeightDemo.class);

	public static void main(String[] args) {
		
		SwingUtils.setDefaultUILookAndFeel();
		SwingUtils.setUIFontSize(12);
		HVSize.alignLineHeight();
		SwingUtils.showFrame(new CustomHeightDemo().build());
	}
	
	public CustomHeightDemo() {
		super(CustomHeightDemo.class.getName());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	public CustomHeightDemo build() {
		
		CForm form = new CForm(new VBox(CForm.MAIN_BOX_INSETS));
		CSize cs = form.csize();
		
		form.add(new TLabel("Buttons with font-size derived sizes.")).csize().setLineSize();

		form.addChild(new HBox());
		
		form.add(new TButton("Default")).csize().setButtonSize();
		
		TButton b = new TButton("Larger");
		
		// Set extra large font.
		b.setFont(b.getFont().deriveFont((float)b.getFont().getSize() + 8));

		// Calculate different (preferred) size for button with extra large font.
		int cheight = HVSize.getComponentHeight(b);

		// Make width proportional to height
		int w = (int)(cs.getHvsize().getButtonWidthFactor() * cheight);
		
		// TButton has no insets, add 4 to height to keep some room below text.
		int h = cheight + 4;
		
		// Apply calculated sizes
		form.add(b).csize().setFixed(w, h);
		
		// Characters "gjfF" demonstrate there is enough room (line-height) to display characters in full.
		b = new TButton("Bigger gjfF");
		b.setFont(b.getFont().deriveFont((float)b.getFont().getSize() + 16));
		cheight = HVSize.getComponentHeight(b);
		h = cheight + 4;
		w = (int)(cs.getHvsize().getButtonWidthFactor() * cheight);
		form.add(b).csize().setFixed(w, h);

		form.up().add(new JTextField()).csize().setLineSize();

		setContentPane(form.getRoot());
		
		pack();
		setLocationByPlatform(true);
		log.debug(getClass().getName() + " build.");
		return this;
	}
}