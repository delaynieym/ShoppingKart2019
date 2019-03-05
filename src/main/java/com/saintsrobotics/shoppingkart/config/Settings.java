package com.saintsrobotics.shoppingkart.config;

public class Settings {

    public double[] rightFrontLoc;
    public double[] leftFrontLoc;
    public double[] leftBackLoc;
    public double[] rightBackLoc;
    public double[] pivotLoc;

    public double armsHardstop;
    public double armsFullout;
    public double armsHatch;
    public double armsFullin;

    public double liftCargoOne;
    public double liftCargoTwo;
    public double liftCargoTree;
    public double liftHatchOne;
    public double liftHatchTwo;
    public double liftHatchTree;

    public double liftOffset;
    public double liftWait;

    public double kickerUpperbound;
    public double kickerLowerbound;

    public double cargoTranslationTarget;
    public double cargoDistanceTarget;
    public double hatchTranslationTarget;
    public double hatchDistanceTarget;

    public PidConfig wheelAnglePidConfig;
    public PidConfig headingPidConfig;
    public PidConfig liftPidConfig;
    public PidConfig armsPidConfig;
    public PidConfig dockTranslationPidConfig;
    public PidConfig dockDistancePidConfig;

    public Settings(Config robotConfig) {
        this.rightFrontLoc = buildLoc(robotConfig, "settings.location.rightFront");
        this.leftFrontLoc = buildLoc(robotConfig, "settings.location.leftFront");
        this.leftBackLoc = buildLoc(robotConfig, "settings.location.leftBack");
        this.rightBackLoc = buildLoc(robotConfig, "settings.location.rightBack");
        this.pivotLoc = buildLoc(robotConfig, "settings.location.pivot");

        this.armsHardstop = robotConfig.getDouble("settings.arms.hardstop");
        this.armsFullout = robotConfig.getDouble("settings.arms.fullout");
        this.armsHatch = robotConfig.getDouble("settings.arms.hatch");
        this.armsFullin = robotConfig.getDouble("settings.arms.fullin");

        this.liftCargoOne = robotConfig.getDouble("settings.lift.cargoOne");
        this.liftCargoTwo = robotConfig.getDouble("settings.lift.cargoTwo");
        this.liftCargoTree = robotConfig.getDouble("settings.lift.cargoTree");
        this.liftHatchOne = robotConfig.getDouble("settings.lift.hatchOne");
        this.liftHatchTwo = robotConfig.getDouble("settings.lift.hatchTwo");
        this.liftHatchTree = robotConfig.getDouble("settings.lift.hatchTree");

        this.liftOffset = robotConfig.getDouble("settings.lift.offset");
        this.liftWait = robotConfig.getDouble("settings.lift.waittime");

        this.kickerUpperbound = robotConfig.getDouble("settings.kicker.upperbound");
        this.kickerLowerbound = robotConfig.getDouble("settings.kicker.lowerbound");

        this.cargoTranslationTarget = robotConfig.getDouble("settings.dock.cargoTranslation");
        this.cargoDistanceTarget = robotConfig.getDouble("settings.dock.cargoDistance");
        this.hatchTranslationTarget = robotConfig.getDouble("settings.dock.hatchTranslation");
        this.hatchDistanceTarget = robotConfig.getDouble("settings.dock.hatchDistance");

        this.wheelAnglePidConfig = buildPidConfig(robotConfig, "settings.pids.wheelAngle");
        this.headingPidConfig = buildPidConfig(robotConfig, "settings.pids.heading");
        this.liftPidConfig = buildPidConfig(robotConfig, "settings.pids.lift");
        this.armsPidConfig = buildPidConfig(robotConfig, "settings.pids.arms");
        this.dockTranslationPidConfig = buildPidConfig(robotConfig, "settings.pids.dock.translation");
        this.dockDistancePidConfig = buildPidConfig(robotConfig, "settings.pids.dock.distance");
    }

    private static double[] buildLoc(Config robotConfig, String keyPrefix) {
        return new double[] { robotConfig.getDouble(keyPrefix + ".x"), robotConfig.getDouble(keyPrefix + ".y") };
    }

    private static PidConfig buildPidConfig(Config robotConfig, String keyPrefix) {
        return new PidConfig(robotConfig.getDouble(keyPrefix + ".kP"), robotConfig.getDouble(keyPrefix + ".kI"),
                robotConfig.getDouble(keyPrefix + ".kD"), robotConfig.getDouble(keyPrefix + ".tolerance"));
    }
}
