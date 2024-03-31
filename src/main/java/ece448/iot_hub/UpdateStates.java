package ece448.iot_hub;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import ece448.iot_hub.App;
import ece448.iot_hub.PlugStates;
import ece448.iot_sim.PlugSim;

@Component
public class UpdateStates {
    private final PlugStates plugStates;

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    @Autowired
    public UpdateStates(PlugStates plugStates) {
        this.plugStates = plugStates;
    }

    public void handleMessage(String topic, MqttMessage msg) throws Exception {
        logger.info("Update {} ", topic);
        String[] valuesArray = topic.split("/");
        String action = valuesArray[valuesArray.length - 1];
        String plug = valuesArray[valuesArray.length - 2];

        // get the value
        String value = new String(msg.getPayload());

        // show the value
        logger.info("Plug: {} Action: {} Value: {}",plug ,action, value);

        if (action.equals("state")){
            plugStates.updateState(plug, value.equals("on"));
            logger.info("MODIFICADO STATE");
        }
        if (action.equals("power")){
            try {
                plugStates.updatePower(plug, Math.round(Float.parseFloat(value)));
            } catch (Exception e) {
                logger.info(e.toString());
            }
            logger.info("MODIFICADO POWER");
        }
        
    }
}
