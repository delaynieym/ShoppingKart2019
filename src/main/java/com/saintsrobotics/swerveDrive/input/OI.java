package com.saintsrobotics.swerveDrive.input;

import com.github.dozer.input.OI.XboxInput;

public class OI {
	public XboxInput xboxInput = new XboxInput(0);

	public OI() {
		this.xboxInput.init();
	}
}
