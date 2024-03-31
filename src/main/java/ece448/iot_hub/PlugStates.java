package ece448.iot_hub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import ece448.iot_hub.App;
import ece448.iot_hub.Plug;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component
public class PlugStates {
	private HashMap<String, Plug> plugs = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(App.class);


	synchronized public List<String> getPlugs() {
		return new ArrayList<>(plugs.keySet());
	}

	synchronized Plug getPlug(String name) {
		Plug plug = plugs.get(name);
		return plug;
	}

    synchronized public void updateState(String name, boolean state) {
        logger.info("STATE CALLED");
        if (plugs.containsKey(name)) {
            Plug plug = plugs.get(name);
            plug.setState(state);
        } else {
            plugs.put(name, new Plug(0, state));
        }
    }
    
    synchronized public void updatePower(String name, int power) {
        logger.info("POWER CALLED");
        if (plugs.containsKey(name)) {
            Plug plug = plugs.get(name);
            plug.setPower(power);
        } else {
            plugs.put(name, new Plug(power, false));
        }
    }
    synchronized public void removePlug(String name) {
        plugs.remove(name);
    }


    // Method to return JSON array of all plugs with power, state, and name
    synchronized public List<Map<String, Object>> getAllPlugsJSON() {
        List<Map<String, Object>> jsonArray = new ArrayList<>();
        for (Map.Entry<String, Plug> entry : plugs.entrySet()) {
            String name = entry.getKey();
            Plug plug = entry.getValue();
            if (plug != null) {
                Map<String, Object> json = new HashMap<>();
                json.put("name", name);
                json.put("state", plug.isState() ? "on" : "off");
                json.put("power", plug.getPower());
                jsonArray.add(json);
            }
        }
        return jsonArray;
    }
    
    // Method to return JSON representation of a single plug by name
    synchronized public Map<String, Object> getPlugJSON(String name) {
        Map<String, Object> json = new HashMap<>();
        Plug plug = plugs.get(name);
        if (plug != null) {
            json.put("name", name);
            json.put("state", plug.isState() ? "on" : "off");
            json.put("power", plug.getPower());
        }
        return json;
    }
}
