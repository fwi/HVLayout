package nl.fw.swing.renderer;

import java.awt.Component;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class TableCellDateRenderer extends DefaultTableCellRenderer {

	public static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

	protected Font font;
	
	public TableCellDateRenderer() {
		this(null, TRAILING);
	}

	public TableCellDateRenderer(Font font) {
		this(font, TRAILING);
	}

	public TableCellDateRenderer(Font font, int orientation) {
		super();
		this.font = font;
		setHorizontalAlignment(orientation);
	}

	@Override
	public Component getTableCellRendererComponent(final JTable table, final Object value,
			final boolean isSelected, final boolean hasFocus, final int row, final int column) {

		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (font != null) {
			setFont(font);
		}
		setText(getText((Date) value));
		return this;
	}
	
	public String getText(Date d) {
		return dateTimeFormat.format(d);
	}
}
