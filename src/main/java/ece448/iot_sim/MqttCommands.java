package ece448.iot_sim;

import org.eclipse.paho.client.mqttv3.MqttMessage;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.TreeMap;

import ece448.iot_sim.PlugSim;

public class MqttCommands {
    private final TreeMap<String, PlugSim> plugs = new TreeMap<>();
    private final String topicPrefix;
    private static final Logger logger = LoggerFactory.getLogger(Main.class);


    public MqttCommands(List<PlugSim> plugs, String topicPrefix) {
        for (PlugSim plug : plugs) {
            this.plugs.put(plug.getName(), plug);
        }
        this.topicPrefix = topicPrefix;
    }
    public String getTopic() {
        return topicPrefix + "/action/#";
    }

    public void handleMessage(String topic, MqttMessage msg) {
        try {
            logger.info("MqttCmd {} ", topic);
            // Switch on/off/toggle logic here
            String[] valuesArray = topic.split("/");
            String action = valuesArray[valuesArray.length - 1];
            String plug_name = valuesArray[valuesArray.length - 2];
            
            if (action.equals("toggle")){
                logger.info("MqttCmd {} {}", "toggle", plug_name);
                this.plugs.get(plug_name).toggle();
            }
            if (action.equals("on")){
                logger.info("MqttCmd {} {}", "on", plug_name);
                this.plugs.get(plug_name).switchOn();
            }
            if (action.equals("off")){
                logger.info("MqttCmd {} {}", "off", plug_name);
                this.plugs.get(plug_name).switchOff();
            }
        } catch (Throwable th) {
            // Otherwise, Mqtt client will disconnect without error information
            logger.error("MqttCmd {}: {} ", topic, th.getMessage(), th);
        }
    }
}
