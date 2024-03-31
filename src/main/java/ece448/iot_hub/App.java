package ece448.iot_hub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;


import ece448.iot_hub.PlugStates;
import ece448.iot_hub.UpdateStates;

@SpringBootApplication
public class App {
	@Autowired
	UpdateStates stateUpdater;

	@Autowired
	private MqttClient mqttClient;

	@Bean(destroyMethod = "disconnect")
	public MqttClient mqttClient(Environment env) throws Exception {
		String broker = env.getProperty("mqtt.broker");
		String clientId = env.getProperty("mqtt.clientId");
		MqttClient mqtt = new MqttClient(broker, clientId, new MemoryPersistence());
		mqtt.connect();
		logger.info("MqttClient {} connected: {}", clientId, broker);
		return mqtt;
	}

	private static final Logger logger = LoggerFactory.getLogger(App.class);

	@Bean
    public CommandLineRunner onStartup(Environment env) {
        return args -> {
			// subscribe to prefix
			mqttClient.subscribe(env.getProperty("mqtt.topicPrefix")+ "/update/#", (topic, msg) -> {
				stateUpdater.handleMessage(topic, msg);
			});
			logger.info("Mqtt subscribe to {}", env.getProperty("mqtt.topicPrefix")+ "/#");
        };
    }
}

