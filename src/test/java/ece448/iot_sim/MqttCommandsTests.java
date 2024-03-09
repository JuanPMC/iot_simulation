package ece448.iot_sim;

import org.eclipse.paho.client.mqttv3.MqttMessage;


import static org.junit.Assert.*;

import org.junit.Test;

import java.beans.Transient;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MqttCommandsTests {

	private PlugSim plug;
	private MqttCommands a;
	List<PlugSim> plugs;

	public MqttCommandsTests(){
		plug = new PlugSim("a");
		plug.switchOff();

		plugs = List.of(plug);
		a = new MqttCommands(plugs,"pepe");
	}

	@Test
	public void testTopic() {

		assertTrue(("pepe/action/#").equals(a.getTopic()));

	}

	@Test
	public void testOn(){
		plug.switchOff();
		try {
			a.handleMessage("sdfasf/a/on",new MqttMessage("pepe".getBytes()));
		} catch (Exception e) {
		}
		assert(plug.isOn());
	}
	@Test
	public void testOff(){
		plug.switchOn();
		try {
			a.handleMessage("sdfasf/a/off",new MqttMessage("pepe".getBytes()));
		} catch (Exception e) {
		}
		assert(!plug.isOn());
	}

	@Test
	public void testToggle(){
		plug.switchOn();
		try {
			a.handleMessage("sdfasf/a/toggle",new MqttMessage("pepe".getBytes()));
		} catch (Exception e) {
		}
		assert(!plug.isOn());
	}

}
