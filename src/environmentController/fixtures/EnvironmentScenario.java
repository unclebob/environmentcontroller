package environmentController.fixtures;

import fit.ColumnFixture;
import environmentController.EnvironmentController;
import environmentController.MockControlHardware;

public class EnvironmentScenario {
  private int Minute;
  private int Temp;
  private EnvironmentController controller;
  private MockControlHardware hw;
  private int time = 0;

  public EnvironmentScenario(String comment) {
    hw = new MockControlHardware();
    controller = new EnvironmentController(hw);
    controller.tooHot(75);
    controller.tooCold(65);
  }

  public void execute() throws Exception {
    while (time < Minute) {
      time++;
      controller.tic();
    }

    hw.setTemp(Temp);
    controller.tic();
    time++;
  }

  public String Heater() {
    return hw.heaterState() ? "X" : "-";
  }
    public String Blower() {
    return hw.blowerState() ? "X" : "-";
  }
    public String Cooler() {
    return hw.coolerState() ? "X" : "-";
  }

  public void setMinute(int minute) {
    Minute = minute;
  }

  public void setTemp(int temp) {
    Temp = temp;
  }
}
