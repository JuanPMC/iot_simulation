package ece448.iot_hub;

public class Plug {
    private int power;
    private boolean state; // true for on, false for off

    // Constructor
    public Plug(int power, boolean state) {
        this.power = power;
        this.state = state;
    }

    // Getter and setter for power
    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    // Getter and setter for state
    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    // Method to turn the plug on
    public void turnOn() {
        this.state = true;
    }

    // Method to turn the plug off
    public void turnOff() {
        this.state = false;
    }

}