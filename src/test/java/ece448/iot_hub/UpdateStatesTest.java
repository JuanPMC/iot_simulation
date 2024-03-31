package ece448.iot_hub;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UpdateStatesTest {

    private UpdateStates updateStates;
    private PlugStatesStub plugStatesStub;

    @Before
    public void setUp() {
        plugStatesStub = new PlugStatesStub();
        updateStates = new UpdateStates(plugStatesStub);
    }

    @Test
    public void testHandleMessageStateOff() throws Exception {
        String topic = "prefix/action/plug/state";
        String value = "off";
        MqttMessage message = new MqttMessage(value.getBytes());

        updateStates.handleMessage(topic, message);

        assertEquals(false, plugStatesStub.getState("plug"));
    }

    @Test
    public void testHandleMessageInvalidAction() throws Exception {
        String topic = "prefix/action/plug/invalid";
        String value = "value";
        MqttMessage message = new MqttMessage(value.getBytes());

        updateStates.handleMessage(topic, message);

        // Ensure that invalid action is not processed
        assertEquals(false, plugStatesStub.getState("plug"));
        assertEquals(0, plugStatesStub.getPower("plug"));
    }

    @Test
    public void testHandleMessageInvalidValue() throws Exception {
        String topic = "prefix/action/plug/state";
        String value = "invalid";
        MqttMessage message = new MqttMessage(value.getBytes());

        updateStates.handleMessage(topic, message);

        // Ensure that invalid value is not processed
        assertEquals(false, plugStatesStub.getState("plug"));
        assertEquals(0, plugStatesStub.getPower("plug"));
    }

    // Stub implementation for PlugStates
    private static class PlugStatesStub extends PlugStates {
        private boolean state;
        private int power;

        public void setState(String name, boolean state) {
            this.state = state;
        }

        public boolean getState(String name) {
            return state;
        }

        public void setPower(String name, int power) {
            this.power = power;
        }

        public int getPower(String name) {
            return power;
        }
    }
}
