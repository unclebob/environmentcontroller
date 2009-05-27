package environmentController;

public interface EnvironmentControlHardware {

  void setHeater(boolean heater);

  void setCooler(boolean cooler);

  void setBlower(boolean blower);

  void setHiTempAlarm(boolean hiTempAlarm);

  void setLoTempAlarm(boolean loTempAlarm);

  int getTemp();

  boolean blowerState();

  boolean coolerState();

  boolean heaterState();

  boolean hiTempAlarm();

  boolean loTempAlarm();
}
