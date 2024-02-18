package ece448.iot_sim;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class HTTPCommandsTests {

	private PlugSim plug;
	private HTTPCommands a;
	List<PlugSim> plugs;

	public HTTPCommandsTests(){
		plug = new PlugSim("a");
		plug.switchOff();

		plugs = List.of(plug);
		a = new HTTPCommands(plugs);
	}

	@Test
	public void testON() {
		// initialize plug
		plug.switchOff();

		// Create parameters
		Map<String, String> params = Map.of("action", "on");
		a.handleGet("/a",params);

		assert(plug.isOn());
	}

	@Test
	public void testOff() {
		// initialize plug
		plug.switchOn();

		// Create parameters
		Map<String, String> params = Map.of("action", "off");
		a.handleGet("/a",params);

		assert(!plug.isOn());
	}

	@Test
	public void testToggel() {
		// initialize plug
		plug.switchOn();

		// Create parameters
		Map<String, String> params = Map.of("action", "toggle");
		a.handleGet("/a",params);

		assert(!plug.isOn());
	}

	@Test
	public void testNoPLug() {
		// Create parameters
		Map<String, String> params = Map.of("action", "toggle");

		assert(a.handleGet("/pepepe",params) == null);
	}

	@Test
	public void testRootDir() {
		// Create parameters
		Map<String, String> params = Map.of("xx", "xx");

		assert(a.handleGet("/",params).contains(plug.getName()));
	}

	@Test
	public void testActionNoExist() {
		// Create parameters
		Map<String, String> params = Map.of("action", "xx");

		assert(a.handleGet("/a",params).equals(report(plug)));
	}

	@Test
	public void testNullAction() {
		// Create parameters
		Map<String, String> params = Map.of("xx", "xx");

		assert(a.handleGet("/a",params).equals(report(plug)));
	}

	protected String report(PlugSim plug) {
		String name = plug.getName();
		return String.format("<html><body>"
			+"<p>Plug %s is %s.</p>"
			+"<p>Power reading is %.3f.</p>"
			+"<p><a href='/%s?action=on'>Switch On</a></p>"
			+"<p><a href='/%s?action=off'>Switch Off</a></p>"
			+"<p><a href='/%s?action=toggle'>Toggle</a></p>"
			+"</body></html>",
			name,
			plug.isOn()? "on": "off",
			plug.getPower(), name, name, name);
	}
}
