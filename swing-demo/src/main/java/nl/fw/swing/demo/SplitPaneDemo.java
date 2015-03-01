package nl.fw.swing.demo;

import java.awt.Font;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import nl.fw.swing.SwingUtils;
import nl.fw.swing.component.TLabel;
import nl.fw.swing.component.TableSortHeader;
import nl.fw.swing.component.TableSortHeaderButton;
import nl.fw.swing.hvlayout.CForm;
import nl.fw.swing.hvlayout.CSize;
import nl.fw.swing.hvlayout.CSizeUtils;
import nl.fw.swing.hvlayout.HVSize;
import nl.fw.swing.hvlayout.VBox;

/**
 * Runnable demo form to demonstrate HVLayout with a split-pane.
 * <br>The split-pane component itself needs to be given a maximum size (as demonstrated in the source code)
 * in order to let the split-pane grow bigger.
 * The components in the split-pane should be inside a scroll-pane to have a flexible size.
 * The size of the components in the split-pane is set on the top-level container 
 * added to the split-pane, or on the scroll-pane(s) inside the top-level container.
 * <br>In this manner the split-pane has a (predictable) "default" size that is the sum of the preferred sizes in both panes.
 * And the complete split-pane can grow and shrink.
 *  
 * @author fred
 *
 */
@SuppressWarnings("serial")
public class SplitPaneDemo extends JFrame {

	public static void main(String[] args) {
		
		SwingUtils.setDefaultUILookAndFeel();
		SwingUtils.setUIFont("Arial", Font.PLAIN, 12);
		HVSize.alignLineHeight();
		SwingUtils.showFrame(new SplitPaneDemo().build());
	}

	public SplitPaneDemo() {
		super("Split Pane demo");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	public Window build() {
		
		VBox splitPaneLeft;
		CForm form = new CForm(splitPaneLeft = new VBox());
		form.csize().set(splitPaneLeft).setAreaSize(0.5f, 10.0f).setMinWidthButton();
		form.add(new TLabel("A list of items")).csize().setLineSize().setPrefWidthButton();
		form.add(new JScrollPane(createList()));
		
		VBox splitPaneRight;
		form = new CForm(splitPaneRight = new VBox());
		form.csize().set(splitPaneRight).setAreaSize(1.5f, 10.0f).setMinWidthButton(2.0f);
		form.add(new TLabel("A table of items")).csize().setLineSize().setPrefWidthButton();
		form.add(new JScrollPane(createTable(form.csize())));
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				splitPaneLeft, splitPaneRight);
		
		// Must specify max-size on splitpane, else split-pane will not grow (by default split-pane max-size is pref-size).
		form.csize().set(splitPane).setMax(form.csize().dimMax());
		
		// For demo purpose, let the list width grow a little on resize.
		splitPane.setResizeWeight(0.25d);
		splitPane.setContinuousLayout(true);
		
		form = new CForm(new VBox(CForm.MAIN_BOX_INSETS));
		form.add(splitPane);
		setContentPane(form.getRoot());
		pack();
		setLocationByPlatform(true);
		return this;
	}
	
	private JList<String> createList() {
		
		List<String> items = new ArrayList<String>();
		for (int i = 0; i < 100; i++) {
			String item = "item " + i + " ";
			for (int j = 0; j < i; j++) {
				item += ".";
			}
			item += "|";
			items.add(item);
		}
		JList<String> l = new JList<String>(items.toArray(new String[items.size()]));
		return l;
	}
	
	private JTable createTable(CSize cs) {
		
		TableDemoModel tmodel;
		JTable table = new JTable(tmodel = new TableDemoModel());
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		// For demo, make table sortable by clicking on table header columns.
		table.setRowSorter(new TableRowSorter<TableDemoModel>(tmodel));
		TableSortHeader tsortheader;
		table.setTableHeader(tsortheader = new TableSortHeader(table.getColumnModel()));
		
		// Set height of table-header to line-height.
		cs.set((TableSortHeaderButton)tsortheader.getRenderer()).setFixedHeightLine();
		
		// For demo, set default column sizes from small to large.
		for (int i = 0; i < tmodel.getColumnCount(); i++) {
			int demoCWidth = Math.max(cs.getHvsize().getButtonWidth() / 2, 
					CSizeUtils.multiply(cs.getHvsize().getButtonWidth(), ((float)(i + 1)) / (tmodel.getColumnCount())));
			table.getColumnModel().getColumn(i).setPreferredWidth(demoCWidth);
		}
		return table;
	}
	
	/** A simple table model. */
	class TableDemoModel extends AbstractTableModel {

		int columns = 10;
		int rows = 100;
		
		@Override public int getColumnCount() {
			return columns;
		}

		@Override public int getRowCount() {
			return rows;
		}

		@Override public Object getValueAt(int rowIndex, int columnIndex) {
			return "c " + columnIndex + " r " + rowIndex;
		}
		
		@Override public Class<?> getColumnClass(int columnIndex) {
			return String.class;
		}
	}

}
