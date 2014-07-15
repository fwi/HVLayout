package nl.fw.swing.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

public class TableSortHeaderButton extends TButton implements TableCellRenderer {
	
	private static final long serialVersionUID = 6986852264005168450L;

	public TableSortHeaderButton() {
		super();
		setMargin(TButton.BUTTON_MARGIN);
		setIconTextGap(0);
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		
		Icon icon = null;
		int modelCol = table.convertColumnIndexToModel(column);
		RowSorter<? extends TableModel> sorter = table.getRowSorter();
		if (sorter != null) {
			List<? extends RowSorter.SortKey> sortKeys = sorter.getSortKeys();
			if (sortKeys.size() > 0) {
				if (modelCol == sortKeys.get(0).getColumn())
					setBoldFont();
				else
					setPlainFont();
				int colSortKeyIndex = -1;
				int i = 0;
				while (i < sortKeys.size() && colSortKeyIndex == -1) {
					if (modelCol == sortKeys.get(i).getColumn())
						colSortKeyIndex = i;
					i++;
				}
				if (colSortKeyIndex != -1) {
					icon = getSortIcon(sortKeys.get(colSortKeyIndex).getSortOrder(), 
							colSortKeyIndex+1);
				}
			} else {
				setPlainFont();
			}
		} else {
			setPlainFont();
		}
		setText((value == null ? "" : value.toString()));
		setIcon(icon);
		return this;
	}
	
	public void setBoldFont() {
		if (getFont().getStyle() != Font.BOLD)
			setFont(getFont().deriveFont(Font.BOLD));
	}

	public void setPlainFont() {
		if (getFont().getStyle() != Font.PLAIN)
			setFont(getFont().deriveFont(Font.PLAIN));
	}
	
	/** Initializes a blank image. */
	public BufferedImage getBlankImage(int width, int height) {
		
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);

		// get graphics and set hints

		Graphics2D graphics = (Graphics2D) image.getGraphics().create();
		// Appears to have no effect:
		//graphics.setComposite(AlphaComposite.Src);
		//graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setColor(new Color(0, 0, 0, 0));
		graphics.fillRect(0, 0, width, height);
		graphics.dispose();
		
		return image;
	}
	
	/** Fills a blank image with sort column number and an
	 * up or down arrow to indicate the sort order.
	 * @param sortOrder The sort-order-key containing the sort order.
	 * @param number The sort number (1 = first sorted, 2 = secondly sorted, etc)
	 * @return An ImageIcon with the sort-order icon.
	 */
	public Icon getSortIcon(SortOrder sortOrder, int number) {
		
		BufferedImage bi = getBlankImage(16, 16);
		Graphics2D graphics = (Graphics2D) bi.getGraphics().create();
		graphics.setColor(Color.black);
		Font font = new Font(Font.MONOSPACED, Font.BOLD, 11);
		graphics.setFont(font);
		int ntextWidth = getFontMetrics(font).stringWidth(Integer.toString(number));
		graphics.drawString(Integer.toString(number), 8 - (ntextWidth / 2), 11);
		if (sortOrder == SortOrder.ASCENDING) { 
			// Draw up-arrow
			graphics.drawLine(7,  0, 1, 5);
			graphics.drawLine(7,  0, 2, 5);
			graphics.drawLine(8,  0, 1, 5);
			
			graphics.drawLine(7, 0, 14, 5);
			graphics.drawLine(8, 0, 14, 5);
			graphics.drawLine(8, 0, 13, 5);
		} else { 
			// Draw down-arrow
			graphics.drawLine(1,  10, 7, 15);
			graphics.drawLine(2,  10, 7, 15);
			graphics.drawLine(1,  10, 8, 15);
			
			graphics.drawLine(14, 10, 7, 15);
			graphics.drawLine(14, 10, 8, 15);
			graphics.drawLine(13, 10, 8, 15);
		}
		graphics.dispose();
		return new ImageIcon(bi);
	}
}
