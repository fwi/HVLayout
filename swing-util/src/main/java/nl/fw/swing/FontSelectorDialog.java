package nl.fw.swing;

import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nl.fw.swing.component.JMultiLineLabel;
import nl.fw.swing.component.TButton;
import nl.fw.swing.component.TCheckBox;
import nl.fw.swing.component.TLabel;
import nl.fw.swing.hvlayout.CForm;
import nl.fw.swing.hvlayout.HBox;
import nl.fw.swing.hvlayout.VBox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A modal dialog for selecting a font size and type.
 * When a different font size is selected, a UI update is usually only
 * partially succesfull. A complete restart (exit and launch again) is required
 * to re-draw the existing windows.
 * <p> 
 * Note that many people require a large font to easily read the text shown
 * on screen. In fact, not being able to show text using a large font
 * could seriously impact the usability of your program. Many people have
 * less then perfect eyesight and a big font can prevent many sore eyes and
 * headaches. 
 * <p>
 * Part of the source code was copied from 
 * <br>http://www.java2s.com/Code/Java/Tiny-Application/Afontselectiondialog.htm
  * @author Fred
 *
 */
public class FontSelectorDialog implements ActionListener,
									ListSelectionListener{

	/**
	 * If this source file is not properly edited with UTF-8 encoding,
	 * this test-text will become unusable.
	 */
	public static String TEST_TEXT = "English: The Quick Brown Fox Jumped Over The Lazy Dog \n" +
			"Japanese: 日本語 \n" + // "Japanese"
			"Korean: 한글 \n" + // "Hangul" - the Korean alphabet
			"Simplified Chinese: 简体汉字，汉字(简体) \n" + // "Simplified Chinese characters, Chinese (Simplified)"
			"Traditional Chinese: 繁體中文, 中文(繁體) \n" + // "Traditional Chinese, Chinese (Traditional)"
			"European: Ç€ü¿é \n" + // Various European characters.
			"Hebrew: אְבֱּגֲדֳהִוֵזֶחַטָיֹךְכֱּלֲםֳמִןֻנסעףפְץֱצֲקֳרִשת \n" + // Alphabet ? "Abgdaosehtichclmmnnsa'fftztzkrst"
			"Arabic: الحروف العربية \n"; // "Arabic characters"

	public static final String FONT_SIZE_NAMES[] = { "8", "10", "11", "12", "14", "16", "18",
	      "20", "24", "30", "36", "40", "48", "60", "72" };
	
	public static final int FONT_SIZES[] = { 8, 10, 11, 12, 14, 16, 18,
	      20, 24, 30, 36, 40, 48, 60, 72 };

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private JList<String> fontSizeList, fontNameList;
	private JButton ok, cancel;
	private TCheckBox resetFont, sizeOnly;
	private boolean cancelled;
	private JTextArea exampleText;
	private JScrollPane exampleTextScroller;
	private JDialog dialog;
	
	/** The list of Fonts */
	protected String fontNames[];

	private int selectedFontSize;
	private String selectedFontName;
	private VBox mainPane;
	
	public String getLabel(String labelKey) {
		
		switch (labelKey) {
		case "WindowTitle": return "Font selector";
		case "ResetFont": return "Reset font to system default";
		case "SizeOnly": return "Use selected font size only, ignore font name";
		case "TestText": return TEST_TEXT;
		case "FontName": return "Font name";
		case "FontSize": return "Font size";
		case "FontMessage": return "Changes in the default font may require a restart to take full effect.";
		case "FontMessageText": return "An example text is shown below with the selected font size and name \n(if blocks appear, the font probably does not support the characters):";
		case "OK": return "OK";
		case "Cancel": return "Cancel";
		default:
			return labelKey;
		}
	}
	
	/**
	 * Loads text and configures components.
	 */
	public FontSelectorDialog build() {
		
		CForm form = new CForm(mainPane = new VBox(CForm.MAIN_BOX_INSETS));
		
		JMultiLineLabel jml;
		jml = new JMultiLineLabel(getLabel("FontMessage"));
		form.add(jml).csize().setPref(jml.getPreferredSizeDisplay()).shrinkSize();
		form.add(resetFont = new TCheckBox(getLabel("ResetFont"))).csize().setLineSize();
		form.add(sizeOnly = new TCheckBox(getLabel("SizeOnly"))).csize().setLineSize();
		
		form.addChild(new HBox());
		form.addChild(new VBox());
		form.add(new TLabel(getLabel("FontSize"), HBox.CENTER)).csize().setButtonSize();
		fontSizeList = new JList<String>(FONT_SIZE_NAMES);
		fontSizeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		fontSizeList.addListSelectionListener(this);
		form.add(new JScrollPane(fontSizeList)).csize()
			.setAreaSize(0.5f, 6.0f).setMinHeightLine(3.0f).setFixedWidth().setMaxHeightLine(10.0f);

		form.up().addChild(new VBox());
		
		form.add(new TLabel(getLabel("FontName"), HBox.CENTER)).csize().setButtonSize();
	    fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment()
	        .getAvailableFontFamilyNames();
	    fontNameList = new JList<String>(fontNames);
	    fontNameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    fontNameList.addListSelectionListener(this);
	    // fontNameList.setCellRenderer(new FontNameCellRenderer());
		form.add(new JScrollPane(fontNameList)).csize()
			.setAreaSize(1.0f, 6.0f).setMinHeightLine(3.0f).setMaxWidth(1.5f).setMaxHeightLine(10.0f);
		form.up().up();
		
		jml = new JMultiLineLabel(getLabel("FontMessageText"));
		form.add(jml).csize().setPref(jml.getPreferredSizeDisplay()).shrinkSize();
		exampleTextScroller = new JScrollPane(exampleText = new JTextArea(getLabel("TestText")));
		exampleText.setEditable(false);
		exampleText.setOpaque(true);
		form.add(exampleTextScroller).csize()
			.setAreaSize().setPrefHeightLine(7.0f);
		
		form.addChild(new HBox(HBox.TRAILING));
		form.add(ok = new TButton(getLabel("OK"))).csize().setButtonSize();
		form.add(cancel = new TButton(getLabel("Cancel"))).csize().setButtonSize();
		form.up();
		return this;
	}
	
	/**
	 * Shows the font selector dialog.
	 */
	public void show(Frame owner, ImageIcon windowIcon) {
		
		if (mainPane == null) {
			build();
		}
		dialog = new JDialog(owner, true);
		dialog.setTitle(getLabel("WindowTitle"));
		if (windowIcon != null) {
			dialog.setIconImage(windowIcon.getImage());
		}
		dialog.setContentPane(mainPane);
		SwingUtils.applyOkCancelListener(dialog, ok, cancel, this);
		selectDefaultFont();
		dialog.pack();
		dialog.setLocationRelativeTo(owner);
		dialog.setVisible(true);
		dialog.dispose();
	}
	
	protected void selectDefaultFont() {
		
		selectedFontSize = SwingUtils.getUIFont().getSize();
		selectedFontName = SwingUtils.getUIFont().getFamily();
		
		// Find the current font (size) and select them in the list.
		int selectedPos = 0;
		while ((selectedPos < FONT_SIZES.length
				&& FONT_SIZES[selectedPos] != selectedFontSize)) {
			selectedPos++;
		}
		if (selectedPos < FONT_SIZES.length) {
			fontSizeList.setSelectedIndex(selectedPos);
			fontSizeList.ensureIndexIsVisible(fontSizeList.getSelectedIndex());
		} else {
			log.warn("Could not find current default font size [" + selectedFontSize + "] in graphics environment.");
		}
	    selectedPos = 0;
	    while ((selectedPos < fontNames.length)
	    		&& !fontNames[selectedPos].equals(selectedFontName))
	    		selectedPos++;
		if (selectedPos < fontNames.length) { 
			fontNameList.setSelectedIndex(selectedPos);
			fontNameList.ensureIndexIsVisible(fontNameList.getSelectedIndex());
		} else {
			log.warn("Could not find current default font family name [" + selectedFontName + "] in graphics environment.");
		}
	}
	
	/**
	 * True if font selection was cancelled.
	 */
	public boolean wasCancelled() { return cancelled; }
	
	/**
	 * True if font should be reset to system defaults.
	 */
	public boolean isResetFont() { return resetFont.isSelected(); }

	/**
	 * True if onlu font size should be used and font name ignored.
	 */
	public boolean isSizeOnly() { return sizeOnly.isSelected(); }
		

	/**
	 * The selected font size. See also {@link #wasCancelled()} and {@link #isResetFont()}.
	 */
	public int getSelectedFontSize() { return selectedFontSize; }

	/**
	 * The selected font (family) name. See also {@link #wasCancelled()} and {@link #isSizeOnly()}.
	 */
	public String getSelectFontName() { return selectedFontName; }
	
	/**
	 * Updates the example text with selected font and size.
	 */
	private void updateExampleText() {

		exampleTextScroller.remove(exampleText);
		Font newFont = new Font(selectedFontName, Font.PLAIN, selectedFontSize);
		exampleText = new JTextArea(getLabel("TestText"));
		exampleText.setFont(newFont);
		exampleText.setOpaque(true);
		exampleText.setEditable(false);
		exampleTextScroller.setViewportView(exampleText);
	}

	/**
	 * Listens for OK and Cancel events.
	 */
	@Override
	public void actionPerformed(ActionEvent action) {
		
		Object as = action.getSource();
		if (as == cancel) {
			cancelled = true;
			dialog.setVisible(false);
		} else if (as == ok) {
			// SwingUtils.setUIFont(new Font(selectedFontName, Font.PLAIN, selectedFontSize));
			dialog.setVisible(false);
		}
	}

	/**
	 * Listens for selected font (size) and updates
	 * the example text to use the selected font (size).
	 */
	public void valueChanged(ListSelectionEvent event) {
		
		if (event.getSource() == fontSizeList) {
			int selectedPos = fontSizeList.getSelectedIndex();
			if (selectedPos > -1) {
				selectedFontSize = FONT_SIZES[selectedPos];
				updateExampleText();
			}
		} else if (event.getSource() == fontNameList) {
			int selectedPos = fontNameList.getSelectedIndex();
			if (selectedPos > -1) {
				selectedFontName = fontNames[selectedPos];
				updateExampleText();
			}
		}
	}
	
	/**
	 * Renders the font name using the font.
	 * Unfortunately, some font names become unreadable this way.
	 * @author fred
	 *
	 */
	static class FontNameCellRenderer extends DefaultListCellRenderer {

		private static final long serialVersionUID = 3701150157524647091L;

		@Override
		public Component getListCellRendererComponent(
		        JList<?> list,
		        Object value,
		        int index,
		        boolean isSelected,
		        boolean cellHasFocus) {
			
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			String fontName = value.toString();
			Font f = getFont();
			setFont(new Font(fontName, f.getStyle(), f.getSize()));
			return this;
		}
	}
	
	public static void main(String args[]) {
		
		SwingUtils.setDefaultUILookAndFeel();
		SwingUtils.runLater(new Runnable() {
			@Override public void run() {
				
				FontSelectorDialog fs = new FontSelectorDialog();
				fs.show(null, null);
				System.out.println("Reset font: " + fs.isResetFont() + ", size only: " + fs.isSizeOnly());
				System.out.println("Font size: " + fs.getSelectedFontSize() + ", font name: " + fs.getSelectFontName());
			}
		});
	}
}
