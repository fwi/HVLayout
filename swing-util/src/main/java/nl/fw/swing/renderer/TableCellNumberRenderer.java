package nl.fw.swing.renderer;

import java.awt.Component;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class TableCellNumberRenderer extends DefaultTableCellRenderer {

	public static NumberFormat numberFormat;
	static {
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setGroupingSeparator(' ');
		DecimalFormat df = new DecimalFormat();
		df.setDecimalFormatSymbols(dfs);
		numberFormat = df;
	}

	protected Font font;
	
	public TableCellNumberRenderer() {
		this(null, TRAILING);
	}

	public TableCellNumberRenderer(Font font) {
		this(font, TRAILING);
	}

	public TableCellNumberRenderer(Font f, int orientation) {
		super();
		this.font = f;
		setHorizontalAlignment(orientation);
	}

	@Override
	public Component getTableCellRendererComponent(final JTable table, final Object value,
			final boolean isSelected, final boolean hasFocus, final int row, final int column) {

		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (font != null) {
			setFont(font);
		}
		setText(getText(((Number)value).longValue()));
		return this;
	}
	
	public String getText(long l) {
		return numberFormat.format(l);
	}

}
