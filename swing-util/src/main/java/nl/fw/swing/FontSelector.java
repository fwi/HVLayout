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
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nl.fw.swing.component.JMultiLineLabel;
import nl.fw.swing.component.TButton;
import nl.fw.swing.component.TLabel;
import nl.fw.swing.hvlayout.CForm;
import nl.fw.swing.hvlayout.HBox;
import nl.fw.swing.hvlayout.VBox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A modal dialog for selecting a font size and type. The selected font size and
 * type are stored in Settings (via Settings.defaultFontSize and
 * Settings.defaultFontName) and the UI is updated.
 * When a different font size is selected, a UI update is usually only
 * partially succesfull. A complete restart (exit and launch again) is required
 * to re-draw the existing windows.
 * <p> 
 * Settings.setDefaultFont() is called during startup so that the font
 * size and type selected in this dialog is applied during startup. Note
 * that if only a different font size is selected, all font types are
 * unchanged (i.e. whatever UIManager has loaded as font type remains, just
 * the size is updated). 
 * <p>
 * Note that many people require a large font to easily read the text shown
 * on screen. In fact, not being able to show text using a large font
 * could seriously impact the usability of your program. Many people have
 * less then perfect eyesight and a big font can prevent many sore eyes and
 * headaches. 
 * <p>
 * FontSelector uses the hvlayout package and the OkCancelDialog.
 * <br> The FontSelector dialog can be resized, see the method 
 * buildSelectorPane() on how this is achieved using the hvlayout package.
 * <br> This class can be executed stand-alone to get an impression of how this
 * font selection dialog looks (see bin\demofontselector.bat).
 * <br> The example text is loaded via Labels since UTF-8 is required to 
 * load the various international text examples. I found that on Windows, 
 * only Dialog, Monospaced and (Sans)Serif font type could display all languages.
 * <p>
 * Part of the source code was copied from 
 * <br>http://www.java2s.com/Code/Java/Tiny-Application/Afontselectiondialog.htm
  * @author Fred
 *
 */
public class FontSelector implements ActionListener,
									ListSelectionListener{

	public static String TEST_TEXT = "English: The Quick Brown Fox Jumped Over The Lazy Dog \n" +
			"Japanese: 日本語 \n" + 
			"Korean: 한글 \n" +
			"Simplified Chinese: 简体汉字，汉字(简体) \n" +
			"Traditional Chinese: 繁體中文, 中文(繁體) \n" +
			"European: Ç€ü¿é \n" +
			"Hebrew: אְבֱּגֲדֳהִוֵזֶחַטָיֹךְכֱּלֲםֳמִןֻנסעףפְץֱצֲקֳרִשת \n" +
			"Arabic: حذرت السلطات \n";

	public static final String FONT_SIZE_NAMES[] = { "8", "10", "11", "12", "14", "16", "18",
	      "20", "24", "30", "36", "40", "48", "60", "72" };
	
	public static final int FONT_SIZES[] = { 8, 10, 11, 12, 14, 16, 18,
	      20, 24, 30, 36, 40, 48, 60, 72 };

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private JList<String> fontSizeList, fontNameList;
	private JButton ok, cancel, reset;
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
		case "ResetFont": return "Reset font";
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
	public void build() {
		
		CForm form = new CForm(mainPane = new VBox());
		form.add(new JLabel(getLabel("FontMessage"))).csize().setLineSize().setPrefWidthButton(6.0f);
		reset = new TButton(getLabel("ResetFont"));
		reset.addActionListener(this);
		form.add(reset).csize().setFixedButtonSize();
		
		form.addChild(new HBox());
		form.addChild(new VBox());
		form.add(new TLabel(getLabel("FontSize"), HBox.TRAILING)).csize().setFixedButtonSize();
		fontSizeList = new JList<String>(FONT_SIZE_NAMES);
		fontSizeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		fontSizeList.addListSelectionListener(this);
		form.add(new JScrollPane(fontSizeList)).csize().setAreaSize(1.0f, 10.0f).setFixedHeight();
		form.up().addChild(new VBox());
		form.add(new TLabel(getLabel("FontName"), HBox.TRAILING)).csize().setFixedButtonSize();
		
		// For JDK 1.1: returns about 10 names (Serif, SansSerif, etc.)
	    // fontList = toolkit.getFontList();
	    // For JDK 1.2: a much longer list; most of the names that come
	    // with your OS (e.g., Arial), plus the Sun/Java ones (Lucida,
	    // Lucida Bright, Lucida Sans...)
	    fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment()
	        .getAvailableFontFamilyNames();
	    fontNameList = new JList<String>(fontNames);
	    fontNameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    fontNameList.addListSelectionListener(this);
	    // fontNameList.setCellRenderer(new FontNameCellRenderer());
		form.add(new JScrollPane(fontNameList)).csize().setAreaSize(1.0f, 10.0f).setFixedHeight();
		form.up().up();
		
		form.add(new JMultiLineLabel(getLabel("FontMessageText"))).csize().setAreaSize().setPrefHeightLine(2.0f);
		exampleTextScroller = new JScrollPane(exampleText = new JTextArea(getLabel("TestText")));
		exampleText.setEditable(false);
		exampleText.setOpaque(true);
		form.add(exampleTextScroller).csize().setAreaSize().setPrefHeightLine(6.0f);
		
		form.addChild(new HBox(HBox.TRAILING));
		form.add(ok = new TButton(getLabel("OK"))).csize().setFixedButtonSize();
		form.add(cancel = new TButton(getLabel("Cancel"))).csize().setFixedButtonSize();
		form.up();
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
	 * The selected font size. See also {@link #wasCancelled()}.
	 */
	public int getSelectedFontSize() { return selectedFontSize; }

	/**
	 * The selected font (family) name. See also {@link #wasCancelled()}.
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
		} else if (as == reset) {
			selectDefaultFont();
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
				new FontSelector().show(null, null);
			}
		});
	}
}
