package com.saintsrobotics.shoppingkart;

import java.io.IOException;

import com.github.dozer.TaskRobot;
import com.github.dozer.coroutine.Task;
import com.github.dozer.coroutine.helpers.RunEachFrameTask;
import com.saintsrobotics.shoppingkart.config.Config;
import com.saintsrobotics.shoppingkart.config.Motors;
import com.saintsrobotics.shoppingkart.config.OI;
import com.saintsrobotics.shoppingkart.config.Sensors;
import com.saintsrobotics.shoppingkart.drive.ResetGyro;
import com.saintsrobotics.shoppingkart.drive.SwerveControl;
import com.saintsrobotics.shoppingkart.drive.SwerveInput;
import com.saintsrobotics.shoppingkart.drive.SwerveWheel;
import com.saintsrobotics.shoppingkart.drive.ToHeading;
import com.saintsrobotics.shoppingkart.lift.LiftControl;
import com.saintsrobotics.shoppingkart.lift.LiftInput;
import com.saintsrobotics.shoppingkart.manipulators.ArmsTask;
import com.saintsrobotics.shoppingkart.manipulators.IntakeWheel;
import com.saintsrobotics.shoppingkart.manipulators.Kicker;
import com.saintsrobotics.shoppingkart.util.UpdateMotors;
import com.saintsrobotics.shoppingkart.vision.DockTask;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends TaskRobot {
	private Motors motors;
	private Sensors sensors;
	private OI oi;
	private Flags flags;
	private double[] rightFrontLoc = { 12, 12 };
	private double[] leftFrontLoc = { -12, 12 };
	private double[] leftBackLoc = { -12, -12 };
	private double[] rightBackLoc = { 12, -12 };
	private double[] pivotLoc = { 0, 0 };

	@Override
	public void robotInit() {
		Config robotConfig;
		try {
			robotConfig = this.loadConfig();
		} catch (IOException ex) {
			DriverStation.reportError("Could not load config", false);
			return;
		}

		this.oi = new OI();
		this.motors = new Motors(robotConfig);
		this.sensors = new Sensors(robotConfig);
		this.sensors.gyro.calibrate();
		this.sensors.gyro.reset();
		this.flags = new Flags();

		this.flags.pdp = new PowerDistributionPanel();

		NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);

	}

	@Override
	public void autonomousInit() {
	}

	@Override
	public void teleopInit() {
		SwerveWheel rightFront = new SwerveWheel(this.motors.rightFront, this.motors.rightFrontTurner,
				this.sensors.rightFrontEncoder, this.sensors.wheelAnglePidConfig, this.rightFrontLoc, this.pivotLoc);

		SwerveWheel leftFront = new SwerveWheel(this.motors.leftFront, this.motors.leftFrontTurner,
				this.sensors.leftFrontEncoder, this.sensors.wheelAnglePidConfig, this.leftFrontLoc, this.pivotLoc);

		SwerveWheel leftBack = new SwerveWheel(this.motors.leftBack, this.motors.leftBackTurner,
				this.sensors.leftBackEncoder, this.sensors.wheelAnglePidConfig, this.leftBackLoc, this.pivotLoc);

		SwerveWheel rightBack = new SwerveWheel(this.motors.rightBack, this.motors.rightBackTurner,
				this.sensors.rightBackEncoder, this.sensors.wheelAnglePidConfig, this.rightBackLoc, this.pivotLoc);

		SwerveWheel[] wheels = { rightFront, leftFront, leftBack, rightBack };
		SwerveControl swerveControl = new SwerveControl(wheels, this.sensors.gyro, this.sensors.headingPidConfig);
		SwerveInput swerveInput = new SwerveInput(this.oi.xboxInput, this.sensors.gyro, swerveControl,
				new DockTask(this.sensors.dockPidConfig));
		LiftControl liftControl = new LiftControl(this.motors.lifter, this.sensors.liftEncoder, this.sensors.lifterUp,
				this.sensors.lifterDown, this.sensors.liftPidConfig);

		this.teleopTasks = new Task[] { new ResetGyro(() -> this.oi.xboxInput.Y(), this.sensors.gyro, swerveControl),
				swerveInput, swerveControl, liftControl,

				new ToHeading(() -> this.oi.xboxInput.DPAD_UP(), 0.0, swerveControl),
				new ToHeading(() -> this.oi.xboxInput.DPAD_RIGHT(), 90.0, swerveControl),
				new ToHeading(() -> this.oi.xboxInput.DPAD_DOWN(), 180.0, swerveControl),
				new ToHeading(() -> this.oi.xboxInput.DPAD_LEFT(), 270.0, swerveControl),

				// new ToHeight(() -> this.oi.xboxInput.B(), liftControl, 48.0),

				new LiftInput(this.oi.oppInput, liftControl),
				// new ResetLift(() -> this.oi.xboxInput.B(), this.liftControl),

				new IntakeWheel(() -> this.oi.oppInput.RB(), this.motors.intake),
				new ArmsTask(() -> this.oi.oppInput.B(), () -> this.oi.oppInput.X(), () -> this.oi.oppInput.A(),
						this.sensors.arms, this.motors.arms, this.sensors.armsPidConfig),

				new Kicker(() -> this.oi.oppInput.LB(), this.motors.kicker, this.sensors.kicker, 220, 109),

				new UpdateMotors(this.motors),

				new RunEachFrameTask() {
					@Override
					protected void runEachFrame() {
						// empty task for telemetries
					}
				} };

		super.teleopInit();
	}

	@Override
	public void disabledInit() {
		super.disabledInit();
	}

	/**
	 * This method loads the config from disk. It will default to the competition
	 * config. To use the test config, put a jumper on digital input 10.
	 * 
	 * @return The config
	 * @throws IOException
	 */
	private Config loadConfig() throws IOException {
		DigitalInput testBotJumper = new DigitalInput(10);
		Config robotConfig = Config.fromFile(testBotJumper.get());
		testBotJumper.close();
		return robotConfig;
	}
}
