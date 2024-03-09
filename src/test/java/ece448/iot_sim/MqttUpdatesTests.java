package ece448.iot_sim;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import ece448.iot_sim.MqttUpdates;

import static org.junit.Assert.*;

import org.junit.Test;

import java.beans.Transient;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MqttUpdatesTests {
	private MqttUpdates a;

	public MqttUpdatesTests(){
		a = new MqttUpdates("topicPrefix");
	}

	@Test
	public void testGetTopic() {

		assertTrue(("topicPrefix" + "/update/" + "name" + "/" + "key").equals(a.getTopic("name","key")));
        return ;

	}

	@Test
	public void getMessage() {
		MqttMessage msg = new MqttMessage("pepe".getBytes());
        msg.setRetained(true);

		assertTrue(msg.toString().equals(a.getMessage("pepe").toString()));

	}

}
