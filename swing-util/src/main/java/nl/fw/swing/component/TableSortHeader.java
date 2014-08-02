package nl.fw.swing.component;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class TableSortHeader extends JTableHeader implements MouseListener {

	private static final long serialVersionUID = -7533271207278413058L;
	
	private TableCellRenderer renderer;

	public TableSortHeader(TableColumnModel tcmodel) {
		super(tcmodel);
		renderer = new TableSortHeaderButton();
		setDefaultRenderer(renderer);
		//javax.swing.plaf.basic.BasicTableHeaderUI
		// Following appears to have no effect.
		///for (int i = 0; i < tcmodel.getColumnCount(); i++) {
		//	getColumnModel().getColumn(i).setHeaderRenderer(renderer);
		//}
		addMouseListener(this);
	}
	
	public TableCellRenderer getRenderer() {
		return renderer;
	}
	
	public void setRenderer(TableCellRenderer renderer) {
		this.renderer = renderer;
	}
	

	/** 
	 * Shows a tooltip with the text in the table header if the
	 * text in the table header is truncated.
	 */
	@Override
	public String getToolTipText(MouseEvent e) {
		
		int col;
		String tttext = null;
		if ((col = columnAtPoint(e.getPoint())) != -1) {
			int modelCol = table.convertColumnIndexToModel(col);
			String colText = table.getModel().getColumnName(modelCol);
			int colWidth = getColumnModel().getColumn(col).getWidth();
			int textWidth = getFontMetrics(getFont()).stringWidth(colText + "m");
			if (textWidth > colWidth)
				tttext = colText;
		}
		return tttext;
	}

	/**
	 * Listens for right-clicks on table header columns and adjusts
	 * the sortkeys:
	 * <br> if the column was already being sorted on, sort order is reversed.
	 * <br> if the column was not sorted on, adds the column to the end
	 * of sort keys.
	 * <br> A left-click will remove all sort-keys and add the
	 * clicked column as only column to sort on.
	 * <br> Implementation note: even if the left-click mouse-event is 
	 * consumed here (using e.consume()), the underlying mouse-listeners still
	 * act on the mouse-event. In other words it is not possible to
	 * change the mouse behavior for underlying components. This is
	 * awkward to work with and makes the implementation for
	 * handling the left-click mouse event look like a work-around instead
	 * of a proper functional implementation. But it works for now.
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		
		int col = columnAtPoint(e.getPoint());
		RowSorter<? extends TableModel> sorter = table.getRowSorter();
		boolean unevenClicks = (e.getClickCount() % 2 == 1);
		if (sorter != null && col != -1 && unevenClicks) {
			List<? extends RowSorter.SortKey> sortKeys = sorter.getSortKeys();
			List<RowSorter.SortKey> newsort = null;
			RowSorter.SortKey sknew;
			int modelCol = getTable().convertColumnIndexToModel(col);
			if (sortKeys != null && sortKeys.size() > 0) {
				if (SwingUtilities.isLeftMouseButton(e) && sortKeys.size() > 1) {
					/* As mentioned in the comments, do not overwrite the default behavior for 1 left-click
					 * when 1 sort-column is active. When more then 1 sort-column is active the default behavior
					 * will toggle the sort for the clicked column. But this is not what we want:
					 * a new sort based on the left-clicked column should be set.
					 */
					newsort = new ArrayList<RowSorter.SortKey>();
					sknew = new RowSorter.SortKey(modelCol, SortOrder.ASCENDING);
					newsort.add(sknew);
					sorter.setSortKeys(newsort);
					e.consume();
				} else if (SwingUtilities.isRightMouseButton(e) && sortKeys.size() == 1 
						&& col == sortKeys.get(0).getColumn()) {
					// Set no sort when one column is sorted descending.
					sorter.setSortKeys(newsort);
					e.consume();
				} else if (SwingUtilities.isRightMouseButton(e)) {
					newsort = new ArrayList<RowSorter.SortKey>(sortKeys);
					int colSortKeyIndex = -1;
					int i = 0;
					while (i < newsort.size() && colSortKeyIndex == -1) {
						if (modelCol == newsort.get(i).getColumn())
							colSortKeyIndex = i;
						i++;
					}
					if (colSortKeyIndex == -1) {
						newsort.add(new RowSorter.SortKey(modelCol, SortOrder.ASCENDING));
					} else {
						RowSorter.SortKey sk = newsort.get(colSortKeyIndex);
						if (sk.getSortOrder() == SortOrder.ASCENDING) {
							sknew = new RowSorter.SortKey(
									modelCol, SortOrder.DESCENDING);
						} else {
							sknew = new RowSorter.SortKey(
									modelCol, SortOrder.ASCENDING);
						}
						newsort.remove(colSortKeyIndex);
						newsort.add(colSortKeyIndex, sknew);
					}
					sorter.setSortKeys(newsort);
					e.consume();
				}
			}
		}
	}

	/** Empty method. */
	@Override public void mouseEntered(MouseEvent e) {}
	/** Empty method. */
	@Override public void mouseExited(MouseEvent e) {}
	/** Empty method. */
	@Override public void mousePressed(MouseEvent e) {}
	/** Empty method. */
	@Override public void mouseReleased(MouseEvent e) {}
}
