package ece448.iot_sim;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Random;

public class PlugSimTests {

	@Test
	public void testInit() {
		PlugSim plug = new PlugSim("a");

		assertFalse(plug.isOn());
	}

	@Test
	public void testSwitchOn() {
		PlugSim plug = new PlugSim("a");

		plug.switchOn();

		assertTrue(plug.isOn());
	}

	@Test
	public void testSwitchOff() {
		PlugSim plug = new PlugSim("a");

		plug.switchOn();
		plug.switchOff();

		assertTrue(!plug.isOn());
	}

	@Test
	public void testGetName() {
		PlugSim plug = new PlugSim("a");

		assertTrue(plug.getName() == "a");
	}

	@Test
	public void testToggle() {
		PlugSim plug = new PlugSim("a");


		boolean estado1 = plug.isOn();
		plug.toggle();
		assertTrue(plug.isOn() != estado1);
		plug.toggle();
		boolean estado2 = plug.isOn();
		assertTrue(estado1 == estado2);

	}

	@Test
	public void testUpdatePower() {
		PlugSim plug = new PlugSim("a");
		plug.updatePower(3);
		assertTrue(plug.getPower() == 3);

		plug.updatePower(5);
		assertTrue(plug.getPower() == 5);

	}

	@Test
	public void testMesurePower() {
		PlugSim plug = new PlugSim("a");
		Random random_in = new Random(1);
		Random random_out = new Random(1);

		// Test the debugging case
		PlugSim plug2 = new PlugSim("a.1");
		plug2.switchOn();
		plug2.updatePower(99);
		plug2.measurePower();
		assertTrue( plug2.getPower() == 1);

		// Test for power off
		plug.switchOff();
		plug.updatePower(1);
		plug.measurePower();
		assertTrue( plug.getPower() == 0);

		// smaller than 100
		plug.switchOn();
		plug.updatePower(1);
		plug.measurePower_private(random_in);
		assertTrue( plug.getPower() == 1 + random_out.nextDouble() * 100 );

		// bigger than 300
		plug.switchOn();
		plug.updatePower(400);
		plug.measurePower_private(random_in);
		assertTrue( plug.getPower() == 400 - random_out.nextDouble() * 100 );

		// in (100,300)
		plug.switchOn();
		plug.updatePower(200);
		plug.measurePower_private(random_in);
		assertTrue( plug.getPower() == 200 + random_out.nextDouble() * 40 - 20 );
	}
}
