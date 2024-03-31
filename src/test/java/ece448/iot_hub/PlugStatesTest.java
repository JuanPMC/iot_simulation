package ece448.iot_hub;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

public class PlugStatesTest {

    private PlugStates plugStates;

    @Before
    public void setUp() {
        plugStates = new PlugStates();
    }

    @Test
    public void testUpdateState() {
        plugStates.updateState("plug1", true);
        Plug plug = plugStates.getPlug("plug1");
        assertNotNull(plug);
        assertTrue(plug.isState());
    }

    @Test
    public void testUpdatePower() {
        plugStates.updatePower("plug2", 100);
        Plug plug = plugStates.getPlug("plug2");
        assertNotNull(plug);
        assertEquals(100, plug.getPower());
    }

    @Test
    public void testRemovePlug() {
        plugStates.updateState("plug3", true);
        plugStates.removePlug("plug3");
        assertNull(plugStates.getPlug("plug3"));
    }

    @Test
    public void testGetAllPlugsJSON() {
        plugStates.updateState("plug4", true);
        plugStates.updatePower("plug4", 50);
        plugStates.updateState("plug5", false);
        plugStates.updatePower("plug5", 75);

        List<Map<String, Object>> allPlugsJSON = plugStates.getAllPlugsJSON();
        assertEquals(2, allPlugsJSON.size());

        Map<String, Object> plug4JSON = allPlugsJSON.get(0);
        assertEquals("plug5", plug4JSON.get("name"));
        assertEquals("off", plug4JSON.get("state"));
        assertEquals(75, plug4JSON.get("power"));

        Map<String, Object> plug5JSON = allPlugsJSON.get(1);
        assertEquals("plug4", plug5JSON.get("name"));
        assertEquals("on", plug5JSON.get("state"));
        assertEquals(50, plug5JSON.get("power"));
    }

    @Test
    public void testGetPlugJSON() {
        plugStates.updateState("plug6", true);
        plugStates.updatePower("plug6", 80);

        Map<String, Object> plugJSON = plugStates.getPlugJSON("plug6");
        assertNotNull(plugJSON);
        assertEquals("plug6", plugJSON.get("name"));
        assertEquals("on", plugJSON.get("state"));
        assertEquals(80, plugJSON.get("power"));
    }
}
