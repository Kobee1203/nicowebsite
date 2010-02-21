package org.nds.johndog;

import java.awt.Color;

import org.junit.Test;


public class GarbageTest {

	@Test
	public void testColor() {
		Color c = new Color(0, 255, 0);
		System.out.println(c.getGreen());
	}
}
