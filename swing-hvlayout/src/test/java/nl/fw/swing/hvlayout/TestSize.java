package nl.fw.swing.hvlayout;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Dimension;

import javax.swing.JTextField;

import org.junit.Test;

public class TestSize {
	
	@Test
	public void testMaxSize() {
		
		CSize cs = new CSize(new JTextField());
		cs.setLineSize();
		assertEquals(HVSize.MAX_WIDTH, cs.max().width);
		int prefWidth = cs.pref().width;
		int prefHeight = cs.pref().height;
		cs.scaleWidth(2.0f);
		assertEquals("Height should not have changed." , prefHeight, cs.pref().height);
		assertEquals("Pref width should have doubled.", prefWidth * 2, cs.pref().width);
		assertEquals("Max width should not grow beyond max screen size.", HVSize.MAX_WIDTH, cs.max().width);
		cs.scaleHeight(HVSize.MAX_HEIGHT);
		assertEquals("Max height should not grow beyond max screen size.", HVSize.MAX_HEIGHT, cs.max().height);
		assertEquals("Pref height not larger than max height.", HVSize.MAX_HEIGHT, cs.pref().height);
	}

	@Test
	public void testComponentWithStubbornWidth() {
		
		// HVLayout Protection against JTextField with column size. 
		CSize cs = new CSize(new JTextField(1)).setLineSize().scaleWidth(2.0f);
		//System.out.println(CSizeUtils.sizesToString(cs.get()));
		assertTrue("JTextField with column ignores set preferred width.", cs.min().width > cs.pref().width);
		
		VBox vb = new VBox();
		vb.add(cs.get());
		//System.out.println(CSizeUtils.sizesToString(vb));
		Dimension dmin = vb.getLayout().minimumLayoutSize(vb);
		Dimension dpref = vb.getLayout().preferredLayoutSize(vb);
		assertEquals(dmin.width, dpref.width);
	}

}
