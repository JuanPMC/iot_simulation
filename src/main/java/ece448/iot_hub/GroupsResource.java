package ece448.iot_hub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import ece448.iot_hub.PlugStates;

@RestController
public class GroupsResource {

	@Autowired
    private PlugStates plugStates;

	@Autowired
    private MqttClient mqttClient; // Autowire the MQTT client bean
	
    private final Topic topic;
	
	private final GroupsModel groups;

	public GroupsResource(GroupsModel groups,Topic topic) {
		this.groups = groups;
		this.topic = topic;
		if (this.plugStates == null){
			this.plugStates = new PlugStates();
		}
	}
	
	@GetMapping("/api/groups")
	public Collection<Object> getGroups() {
		ArrayList<Object> ret = new ArrayList<>();
		for (String group: groups.getGroups()) {
			ret.add(makeGroup(group));
		}
		logger.info("Groups: {}", ret);
		return ret;
	}

	@GetMapping("/api/groups/{group}")
	public Object getGroup(@PathVariable("group") String group, @RequestParam(value = "action", required = false) String action) {
		if (action == null) {
			Object ret = makeGroup(group);
			logger.info("Group {}: {}", group, ret);

			return ret;
		}

		// modify code below to control plugs by publishing messages to MQTT broker
		List<String> members = groups.getGroupMembers(group);

		for (String plugName : members) {
			publishAction(plugName, action);
		}

		logger.info("Group {}: action {}, {}", group, action, members);
		return null;
	}

	@PostMapping("/api/groups/{group}")
	public void createGroup( @PathVariable("group") String group, @RequestBody List<String> members) {
		groups.setGroupMembers(group, members);
		logger.info("Group {}: created {}", group, members);
	}
	

	@DeleteMapping("/api/groups/{group}")
	public void removeGroup(
		@PathVariable("group") String group) {
		groups.removeGroup(group);
		logger.info("Group {}: removed", group);
	}

	synchronized public String publishAction(String plugName, String action) {
        String topic = this.topic.getTopic()+"/action/"+plugName+"/"+action;
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

	protected Object makeGroup(String group) {
		// modify code below to include plug states
		HashMap<String, Object> ret = new HashMap<>();
		ret.put("name", group);

		// Create an ArrayList of objects
		ArrayList<Object> objectList = new ArrayList<>();

		for (String plugName : groups.getGroupMembers(group)) {
			objectList.add(plugStates.getPlugJSON(plugName));
		}


		ret.put("members", objectList);
		return ret;
	}

	private static final Logger logger = LoggerFactory.getLogger(GroupsResource.class);	
}
