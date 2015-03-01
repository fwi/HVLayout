package nl.fw.swing.demo;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import nl.fw.swing.SwingUtils;
import nl.fw.swing.component.JMultiLineLabel;
import nl.fw.swing.component.TableSortHeader;
import nl.fw.swing.hvlayout.CForm;
import nl.fw.swing.hvlayout.HVSize;
import nl.fw.swing.hvlayout.VBox;

@SuppressWarnings("serial")
public class TableSortDemo extends JFrame {

	public static void main(String[] args) {
		
		SwingUtils.setDefaultUILookAndFeel();
		SwingUtils.setUIFontSize(12);
		HVSize.alignLineHeight();
		SwingUtils.showFrame(new TableSortDemo().build());
	}

	public TableSortDemo() {
		super(TableSortDemo.class.getName());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	public TableSortDemo build() {
		
		TableSortDemoModel tmodel;
		JTable table = new JTable(tmodel = new TableSortDemoModel());
		
		// The following two statements are needed to allow sorting
		table.setRowSorter(new TableRowSorter<TableSortDemoModel>(tmodel));
		table.setTableHeader(new TableSortHeader(table.getColumnModel()));

		CForm form = new CForm(new VBox(CForm.MAIN_BOX_INSETS));
		form.add(new JMultiLineLabel("Left click table header to set first sort column, "
				+ "\nright click to add another sort column "
				+ "\nor right click again to reverse sort order." ));
		form.add(new JScrollPane(table)).csize().setAreaSize(1.5f, 12.0f);
		
		setContentPane(form.getRoot());
		pack();
		setLocationByPlatform(true);
		return this;
	}
	
	/** A simple table model that uses tdata. */
	class TableSortDemoModel extends AbstractTableModel {

		/** The amount of columns in tdata. */
		int columns = 4;
		
		/** The table data that is used by the table model and displayed in the table. */
		Boolean[][] tdata = 
			{{true, true, true, true},
			{true, true, true, false},
			{true, true, false, false},
			{true, false, false, false},
			{false, false, false, false},
			{false, true, true, true},
			{false, false, true, true},
			{false, false, false, true},
			{false, true, false, true},
			{true, false, true, false},
			{false, true, true, false},
			{true, false, false, true}};

		@Override public int getColumnCount() {
			return columns;
		}

		@Override public int getRowCount() {
			return tdata.length;
		}

		@Override public Object getValueAt(int rowIndex, int columnIndex) {
			return tdata[rowIndex][columnIndex];
		}
		
		@Override public Class<?> getColumnClass(int columnIndex) {
			return Boolean.class;
		}
	}

}
