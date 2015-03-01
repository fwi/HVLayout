package nl.fw.swing.renderer;

import java.awt.Component;
import java.awt.Font;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import nl.fw.swing.SwingUtils;

@SuppressWarnings("serial")
public class TableCellDateRenderer extends DefaultTableCellRenderer {

	protected DateFormat dateFormat;
	
	public TableCellDateRenderer() {
		this(SwingUtils.getUIFontMonoSpaced());
	}

	public TableCellDateRenderer(Font f) {
		this(f, TRAILING);
	}

	public TableCellDateRenderer(Font f, int orientation) {
		super();
		if (f != null) {
			setFont(f);
		}
		setHorizontalAlignment(orientation);
		initDateFormat();
	}
	
	public void initDateFormat() {
		
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		setDateFormat(df);
	}

	public void setDateFormat(DateFormat df) {
		this.dateFormat = df;
	}
	
	public DateFormat getDateFormat() {
		return dateFormat;
	}

	@Override
	public Component getTableCellRendererComponent(final JTable table, final Object value,
			final boolean isSelected, final boolean hasFocus, final int row, final int column) {

		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		setText(getDateText((Date) value));
		return this;
	}
	
	public String getDateText(Date d) {
		return getDateFormat().format(d);
	}
}
