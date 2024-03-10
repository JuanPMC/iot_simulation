package ece448.iot_sim;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.fasterxml.jackson.databind.ObjectMapper;

import ece448.grading.GradeP3.MqttController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Simulate a smart plug with power monitoring.
 */
public class PlugSim {

	private final String name;
	private boolean on = false;
	private double power = 0; // in watts
	private static final ObjectMapper mapper = new ObjectMapper();
	private SimConfig config;

	private final List<Observer> observers = new ArrayList<>();

	public synchronized void addObserver(Observer observer) {
        observers.add(observer);

        // Notify the newly added observer about the current state and power
        observer.update(name, "state", on ? "on" : "off");
        observer.update(name, "power", String.format("%.3f", power));
    }

	public static interface Observer {
		void update(String name, String key, String value);
	}
	
    protected synchronized void updateState(boolean newState) {
        on = newState;
        // Log information about the state change
        logger.info("Plug {}: state {}", name, on ? "on" : "off");

        // Notify observers about the state change
        for (Observer observer : observers) {
            observer.update(name, "state", on ? "on" : "off");
        }
    }

	/**
	 * Measure power private.
	 */
	public void measurePower_private(Random random) {

		Double end_power;

		if (!on) {
			end_power = 0.;
		}
		else if (name.indexOf(".") != -1)
		{
			end_power = Double.parseDouble(name.split("\\.")[1]);
		}
		else if (power < 100)
		{
			end_power = power + random.nextDouble() * 100;
		}
		else if (power > 300)
		{
			end_power = power - random.nextDouble() * 100;
		}
		else
		{
			end_power = power + random.nextDouble() * 40 - 20;
		}
		updatePower(end_power);
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
        updateState(true);
	}

	/**
	 * Switch the plug off.
	 */
	synchronized public void switchOff() {
		updateState(false);
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
		logger.info("Plug {}: power {}", name, power);

		// Notify observers about the power change
		for (Observer observer : observers) {
			observer.update(name, "power", String.format("%.3f", power));
		}
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

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PlugSim.class);
}
