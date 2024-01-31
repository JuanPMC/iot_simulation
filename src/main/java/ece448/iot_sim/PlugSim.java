package ece448.iot_sim;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * Simulate a smart plug with power monitoring.
 */
public class PlugSim {

	private final String name;
	private boolean on = false;
	private double power = 0; // in watts


	/**
	 * Measure power private.
	 */
	public void measurePower_private(Random random) {
		if (!on) {
			updatePower(0);
			return;
		}

		// a trick to help testing
		if (name.indexOf(".") != -1)
		{
			updatePower(Integer.parseInt(name.split("\\.")[1]));
		}
		// do some random walk
		else if (power < 100)
		{
			updatePower(power + random.nextDouble() * 100);
		}
		else if (power > 300)
		{
			updatePower(power - random.nextDouble() * 100);
		}
		else
		{
			updatePower(power + random.nextDouble() * 40 - 20);
		}
	}

	public PlugSim(String name) {
		this.name = name;
	}

	/**
	 * No need to synchronize if read a final field.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Switch the plug on.
	 */
	synchronized public void switchOn() {
		this.on = true;
	}

	/**
	 * Switch the plug off.
	 */
	synchronized public void switchOff() {
		this.on = false;
	}

	/**
	 * Toggle the plug.
	 */
	synchronized public void toggle() {
		if (this.on){
			this.switchOff();
		}else{
			this.switchOn();
		}
	}

	/**
	 * Measure power.
	 */
	synchronized public void measurePower() {
		this.measurePower_private( new Random() );
	}

	protected void updatePower(double p) {
		power = p;
		logger.debug("Plug {}: power {}", name, power);
	}

	/**
	 * Getter: current state
	 */
	synchronized public boolean isOn() {
		return on;
	}

	/**
	 * Getter: last power reading
	 */
	synchronized public double getPower() {
		return power;
	}

	private static final Logger logger = LoggerFactory.getLogger(PlugSim.class);
}
