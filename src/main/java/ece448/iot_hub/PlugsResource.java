package ece448.iot_hub;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import ece448.iot_hub.App;
import ece448.iot_hub.PlugStates;

import org.springframework.beans.factory.annotation.Autowired;

import org.eclipse.paho.client.mqttv3.MqttClient;

import org.springframework.core.env.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class PlugsResource {

    @Autowired
    private MqttClient mqttClient; // Autowire the MQTT client bean

    @Autowired
    private PlugStates plugStates;

    private final Environment env;
    private static final Logger logger = LoggerFactory.getLogger(App.class);


    public PlugsResource(Environment env){
        this.env = env;
    }

    @GetMapping("/api/plugs/{plug:.+}")
    public Object getPlug(@PathVariable("plug") String plugName, @RequestParam(required = false) String action) {
        if (action != null && (action.equalsIgnoreCase("toggle") || action.equalsIgnoreCase("on") || action.equalsIgnoreCase("off"))) {
            return publishAction(plugName, action);
        } else {
            return plugStates.getPlugJSON(plugName);
        }
    }

    synchronized public String publishAction(String plugName, String action) {
        String topic = env.getProperty("mqtt.topicPrefix")+"/action/"+plugName+"/"+action;
        try
        {
            if  (!(mqttClient.isConnected())){
                mqttClient.connect();
            }  
            mqttClient.publish(topic, new MqttMessage(topic.getBytes()));
            return "true";
        }
        catch (Exception e)
        {
            return e.toString();
        }
    }

    @GetMapping("/api/plugs")
    public Object getPlugs() {
        return plugStates.getAllPlugsJSON();
    }
}
