package nl.fw.swing;

import java.awt.Canvas;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import nl.fw.swing.component.TLabel;
import nl.fw.swing.hvlayout.CForm;
import nl.fw.swing.hvlayout.HBox;
import nl.fw.swing.hvlayout.VBox;

/**
 * Shows memory used/committed/max. for current JVM in small window.
 * Usage: {@code new MemoryInfoWindow().build().show(null)}
 * <br>Uses a timer to update memory usage each second (see also {@link #setUpdateDelayMs(long)}.
 * @author fred
 *
 */
public class MemoryInfoWindow {

	public String getLabel(String labelKey) {
		
		switch (labelKey) {
		case "WindowTitle": return "Memory usage";
		case "MemUsed": return "Used";
		case "MemClaimed": return "Claimed";
		case "MemMax": return "Max";
		default:
			return labelKey;
		}
	}

	private JFrame frame;
	private MemoryMXBean memBean;
	private TLabel memUsed, memClaimed, memMax;
	private VBox mainPane;
	private Timer timer;
	private long updateDelayMs = 1000L;

	/**
	 * Loads text and configures components.
	 */
	public MemoryInfoWindow build() {
		
		memBean = ManagementFactory.getMemoryMXBean();
		
		CForm form = new CForm(mainPane = new VBox(CForm.MAIN_BOX_INSETS));
		form.add(new Canvas()).csize().setLineSize().setPrefWidthButton(3.0f);
		memUsed = addLabel(form, "MemUsed");
		memClaimed = addLabel(form, "MemClaimed");
		memMax = addLabel(form, "MemMax");
		return this;
	}

	private TLabel addLabel(CForm form, String label) {
		
		TLabel l = null;
		form.addChild(new HBox());
		form.add(new TLabel(getLabel(label), HBox.TRAILING)).csize().setButtonSize();
		form.add(l = new TLabel("", HBox.TRAILING)).csize().setButtonSize();
		l.setFont(SwingUtils.getUIFontMonoSpaced());
		form.up();
		return l;
	}

	public long getUpdateDelayMs() {
		return updateDelayMs;
	}

	public void setUpdateDelayMs(long updateDelayMs) {
		this.updateDelayMs = updateDelayMs;
	}

	public void show(ImageIcon windowIcon) {
		
		frame = new JFrame(getLabel("WindowTitle"));
		frame.setContentPane(mainPane);
		if (windowIcon != null) {
			frame.setIconImage(windowIcon.getImage());
		}
		timer = new Timer();
		frame.addWindowListener(new WindowAdapter() {
			
			@Override public void windowClosing(WindowEvent e) {
				
				timer.cancel();
				frame.dispose();
			}
		});
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
		timer.schedule(new MemInfoUpdateTask(), 1L);
	}
	
	public void showMemInfo() {

		SwingUtils.runLater(new Runnable() {
			@Override public void run() {
				MemoryUsage heapMem = memBean.getHeapMemoryUsage();
				MemoryUsage nonHeapMem = memBean.getNonHeapMemoryUsage();
				long used = heapMem.getUsed() + nonHeapMem.getUsed();
				long claimed = heapMem.getCommitted() + nonHeapMem.getCommitted();
				long max = heapMem.getMax() + nonHeapMem.getMax();
				memUsed.setText(SwingUtils.humanReadableByteCount(used));
				memClaimed.setText(SwingUtils.humanReadableByteCount(claimed));
				memMax.setText(SwingUtils.humanReadableByteCount(max));
				timer.schedule(new MemInfoUpdateTask(), getUpdateDelayMs());
			}
		});
	}
	
	class MemInfoUpdateTask extends TimerTask {
		
		@Override public void run() {
			showMemInfo();
		}
	}

	public static void main(String args[]) {
		
		SwingUtils.setDefaultUILookAndFeel();
		SwingUtils.runLater(new Runnable() {
			@Override public void run() {
				
				MemoryInfoWindow minfo = new MemoryInfoWindow();
				minfo.build().show(null);
				System.out.println("Resize the window to see \"used\" memory get updated.");
			}
		});
	}

}
