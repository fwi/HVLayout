package nl.fw.swing.renderer;

import java.awt.Component;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import nl.fw.swing.SwingUtils;

@SuppressWarnings("serial")
public class TableCellNumberRenderer extends DefaultTableCellRenderer {

	protected NumberFormat numberFormat;
	
	public TableCellNumberRenderer() {
		this(SwingUtils.getUIFontMonoSpaced());
	}

	public TableCellNumberRenderer(Font font) {
		this(font, TRAILING);
	}

	public TableCellNumberRenderer(Font f, int orientation) {
		super();
		if (f != null) {
			setFont(f);
		}
		setHorizontalAlignment(orientation);
		initNumberFormat();
	}
	
	public void initNumberFormat() {
		
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setGroupingSeparator(' ');
		DecimalFormat df = new DecimalFormat();
		df.setDecimalFormatSymbols(dfs);
		setNumberFormat(df);
	}
	
	public void setNumberFormat(NumberFormat nf) {
		this.numberFormat = nf;
	}
	
	public NumberFormat getNumberFormat() {
		return numberFormat;
	}
	
	@Override
	public Component getTableCellRendererComponent(final JTable table, final Object value,
			final boolean isSelected, final boolean hasFocus, final int row, final int column) {

		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		setText(getNumberText(((Number)value).longValue()));
		return this;
	}
	
	public String getNumberText(long l) {
		return getNumberFormat().format(l);
	}

}
