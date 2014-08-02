package nl.fw.swing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runs the given Runnable within a try-catch block.
 * Logs an error if an Exception occurs.
 * @author fred
 *
 */
public class RunCatched implements Runnable {
	
	private static final Logger log = LoggerFactory.getLogger(RunCatched.class);

	protected Runnable original;
	
	public  RunCatched(Runnable original) {
		this.original = original;
	}
	
	@Override
	public void run() {
		
		try {
			original.run();
		} catch (Exception e) {
			log.error("Runnable did not complete.", e);
		}
	}

}
