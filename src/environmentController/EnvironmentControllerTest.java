package environmentController;

import static environmentController.EnvironmentController.BLOWER_DELAY;
import static environmentController.EnvironmentController.COOLER_DELAY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;

public class EnvironmentControllerTest {
  private MockControlHardware hw;
  private EnvironmentController controller;
  private final int WAY_TOO_HOT = 80;
  private final int TOO_HOT = 70;
  private final int TOO_COLD = 60;
  private final int WAY_TOO_COLD = 50;
  private final int COMFORTABLE = (TOO_HOT + TOO_COLD) / 2;

  @Before
  public void initialize() {
    hw = new MockControlHardware();
    controller = new EnvironmentController(hw);
    controller.hotAlarm(WAY_TOO_HOT);
    controller.tooHot(TOO_HOT);
    controller.tooCold(TOO_COLD);
    controller.coldAlarm(WAY_TOO_COLD);
  }

  @Test
  public void thresholds() throws Exception {
    assertThreshold("WAY_TOO_HOT", WAY_TOO_HOT + 1);
    assertThreshold("WAY_TOO_HOT", WAY_TOO_HOT);

    assertThreshold("TOO_HOT", WAY_TOO_HOT - 1);
    assertThreshold("TOO_HOT", TOO_HOT);

    assertThreshold("COMFORTABLE", TOO_HOT - 1);
    assertThreshold("COMFORTABLE", TOO_COLD + 1);

    assertThreshold("TOO_COLD", TOO_COLD);
    assertThreshold("TOO_COLD", TOO_COLD - 1);

    assertThreshold("WAY_TOO_COLD", WAY_TOO_COLD);
    assertThreshold("WAY_TOO_COLD", WAY_TOO_COLD - 1);
  }

  private void assertThreshold(String threshold, int temp) {
    assertEquals(threshold, threshold(temp));
  }

  private String threshold(int temp) {
    if (controller.isWayTooHot(temp)) return "WAY_TOO_HOT";
    if (controller.isTooHot(temp)) return "TOO_HOT";
    if (controller.isComfortable(temp)) return "COMFORTABLE";
    if (controller.isWayTooCold(temp)) return "WAY_TOO_COLD";
    if (controller.isTooCold(temp)) return "TOO_COLD";
    return null;
  }


  @Test
  public void shouldStartUpOff() throws Exception {
    turnEverythingOn();
    controller = new EnvironmentController(hw);
    assertEverythingOff();
  }

  private void assertEverythingOff() {
    assertState("hbchl");
  }

  private void turnEverythingOn() {
    hw.setHeater(true);
    hw.setCooler(true);
    hw.setBlower(true);
    hw.setHiTempAlarm(true);
    hw.setLoTempAlarm(true);
  }

  @Test
  public void nothingTurnsOnIfTempJustRight() throws Exception {
    comfortable();
    assertEverythingOff();
  }

  @Test
  public void turnOnCoolerAndBlowerIfTooHot() throws Exception {
    tooHot();
    assertState("hBChl");
  }

  @Test
  public void turnOnHeaterAndBlowerIfTooCold() throws Exception {
    tooCold();
    assertState("HBchl");
  }

  @Test
  public void turnOnHiTempAlarmAtThreshold() throws Exception {
    wayTooHot();
    assertState("hBCHl");
  }

  @Test
  public void turnOnLoTempAlarmAtThreshold() throws Exception {
    wayTooCold();
    assertState("HBchL");
  }

  @Test
  public void hiTempAlarmResetsWhenTempGoesDown() throws Exception {
    wayTooHot();
    tooHot();
    assertState("hBChl");
  }

  @Test
  public void loTempAlarmResetsWhenTempGoesUp() throws Exception {
    wayTooCold();
    tooCold();
    assertState("HBchl");
  }

  @Test
  public void heaterTurnsOffButBlowerRemainsOnAfterHeating() throws Exception {
    tooCold();
    comfortable();
    assertState("hBchl");
  }

  @Test
  public void coolerTurnsOffIfHotAgain() throws Exception {
    tooHot();
    comfortable();
    assertState("hbchl");
  }

  @Test
  public void coolerMustRemainOffForFiveMinBeforeRestart() throws Exception {
    tooHot();
    comfortable();
    tooHot();
    delayAndCheck(COOLER_DELAY - 1, "hBchl");
    assertState("hBChl");
  }

  @Test
  public void coolerDoesNotTurnOnAfterDelayIfNotNeeded() throws Exception {
    tooHot();
    comfortable();
    tooHot();
    tooCold();
    delayAndCheck(COOLER_DELAY + 1, "HBchl");
  }

  @Test
  public void blowerDelaysAfterFurnaceGoesOff() throws Exception {
    tooCold();
    comfortable();
    delayAndCheck(BLOWER_DELAY - 1, "hBchl");
    controller.regulate();
    assertState("hbchl");
  }

  @Test
  public void blowerStaysOnWhenCoolingAfterHeating() throws Exception {
    tooCold();
    tooHot();
    delayAndCheck(BLOWER_DELAY+1, "hBChl");
  }


  private void delayAndCheck(int delay, String state) {
    for (int i = 0; i < delay; i++) {
      assertState(state);
      controller.regulate();
    }
  }

  private void comfortable() {
    hw.setTemp(COMFORTABLE);
    controller.regulate();
  }

  private void tooHot() {
    hw.setTemp(TOO_HOT);
    controller.regulate();
  }

  private void tooCold() {
    hw.setTemp(TOO_COLD);
    controller.regulate();
  }

  private void wayTooHot() {
    hw.setTemp(WAY_TOO_HOT);
    controller.regulate();
  }

  private void wayTooCold() {
    hw.setTemp(WAY_TOO_COLD);
    controller.regulate();
  }

  private void assertState(String state) {
    assertEquals(state, hw.getState());
  }
}
