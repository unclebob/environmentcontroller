package environmentController;

public class MockControlHardware implements EnvironmentControlHardware {
  private boolean heater;
  private boolean cooler;
  private boolean blower;
  private boolean hiTempAlarm;
  private boolean loTempAlarm;
  private int temp;

  public boolean blowerState() {
    return blower;
  }

  public boolean coolerState() {
    return cooler;
  }

  public boolean heaterState() {
    return heater;
  }

  public boolean hiTempAlarm() {
    return hiTempAlarm;
  }

  public boolean loTempAlarm() {
    return loTempAlarm;
  }

  public void setHeater(boolean heater) {
    this.heater = heater;
  }

  public void setCooler(boolean cooler) {
    this.cooler = cooler;
  }

  public void setBlower(boolean blower) {
    this.blower = blower;
  }

  public void setHiTempAlarm(boolean hiTempAlarm) {
    this.hiTempAlarm = hiTempAlarm;
  }

  public void setLoTempAlarm(boolean loTempAlarm) {
    this.loTempAlarm = loTempAlarm;
  }

  public int getTemp() {
    return temp;
  }

  public void setTemp(int temp) {
    this.temp = temp;
  }

  public String getState() {
    String state = "";
    state += heater ? "H" : "h";
    state += blower ? "B" : "b";
    state += cooler ? "C" : "c";
    state += hiTempAlarm ? "H" : "h";
    state += loTempAlarm ? "L" : "l";
    return state;
  }
}
