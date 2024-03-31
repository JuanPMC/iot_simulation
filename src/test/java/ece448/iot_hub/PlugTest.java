package ece448.iot_hub;

import org.junit.Test;
import static org.junit.Assert.*;

public class PlugTest {

    @Test
    public void testConstructor() {
        Plug plug = new Plug(100, false);
        assertEquals(100, plug.getPower());
        assertFalse(plug.isState());
    }

    @Test
    public void testGetSetPower() {
        Plug plug = new Plug(50, false);
        assertEquals(50, plug.getPower());
        
        plug.setPower(75);
        assertEquals(75, plug.getPower());
    }

    @Test
    public void testGetSetState() {
        Plug plug = new Plug(60, false);
        assertFalse(plug.isState());
        
        plug.setState(true);
        assertTrue(plug.isState());
    }

    @Test
    public void testTurnOn() {
        Plug plug = new Plug(70, false);
        plug.turnOn();
        assertTrue(plug.isState());
    }

    @Test
    public void testTurnOff() {
        Plug plug = new Plug(80, true);
        plug.turnOff();
        assertFalse(plug.isState());
    }
}
