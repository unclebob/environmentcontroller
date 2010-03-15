package environmentController;

public class EnvironmentController {
  private EnvironmentControlHardware hw;
  private int hotAlarm;
  private int tooHot;
  private int tooCold;
  private int coldAlarm;
  private int coolerOnDelay;
  private int blowerOffDelay;
  public final static int COOLER_DELAY = 3;
  public final static int BLOWER_DELAY = 5;

  public EnvironmentController(EnvironmentControlHardware hw) {
    this.hw = hw;
    hw.setHeater(false);
    hw.setCooler(false);
    hw.setBlower(false);
    hw.setHiTempAlarm(false);
    hw.setLoTempAlarm(false);
  }

  public void hotAlarm(int temp) {
    hotAlarm = temp;
  }

  public void tooHot(int temp) {
    tooHot = temp;
  }

  public void tooCold(int temp) {
    tooCold = temp;
  }

  public void coldAlarm(int temp) {
    coldAlarm = temp;
  }

  public boolean isTooCold(int temp) {
    return temp <= tooCold;
  }

  public boolean isComfortable(int temp) {
    return temp < tooHot && temp > tooCold;
  }

  public boolean isTooHot(int temp) {
    return temp >= tooHot;
  }

  public boolean isWayTooHot(int temp) {
    return temp >= hotAlarm;
  }

  public boolean isWayTooCold(int temp) {
    return temp <= coldAlarm;
  }

  public void regulate() {
    decrementTimers();
    int temp = hw.getTemp();
    if (isTooHot(temp)) {
      processTooHot(temp);
    } else if (isComfortable(temp)) {
      processComfortable();
    } else if (isTooCold(temp)) {
      processTooCold(temp);
    }
  }

  private void decrementTimers() {
    if (coolerOnDelay > 0)
      coolerOnDelay--;
    if (blowerOffDelay > 0)
      blowerOffDelay--;
  }

  private void processTooCold(int temp) {
    turnOnBlower();
    turnOnHeater();
    turnOffCooler();
    checkLoTempAlarm(temp);
  }

  private void processComfortable() {
    turnOffHeater();
    turnOffCooler();
    turnOffBlower();
  }

  private void processTooHot(int temp) {
    turnOffHeater();
    turnOnBlower();
    turnOnCooler();
    checkHiTempAlarm(temp);
  }

  private void turnOnBlower() {
    hw.setBlower(true);
  }

  private void turnOffBlower() {
    if (blowerOffDelay == 0)
      hw.setBlower(false);
  }

  private void turnOnHeater() {
    hw.setHeater(true);
  }

  private void turnOffHeater() {
    if (hw.heaterState()) {
      blowerOffDelay = BLOWER_DELAY;
      hw.setHeater(false);
    }
  }

  private void turnOnCooler() {
    if (coolerOnDelay == 0)
      hw.setCooler(true);
  }


  private void turnOffCooler() {
    if (hw.coolerState()) {
      coolerOnDelay = COOLER_DELAY;
      hw.setCooler(false);
    }
  }

  private void checkLoTempAlarm(int temp) {
    if (isWayTooCold(temp)) {
      hw.setLoTempAlarm(true);
    } else {
      hw.setLoTempAlarm(false);
    }
  }

  private void checkHiTempAlarm(int temp) {
    if (isWayTooHot(temp)) {
      hw.setHiTempAlarm(true);
    } else {
      hw.setHiTempAlarm(false);
    }
  }
}
