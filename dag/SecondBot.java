package dag;

import java.awt.Color;

import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;

public class SecondBot extends Robot {
	String[] modes = {"normal", "agressive", "defensive"};
	Color[] colors = {new Color(0, 0 , 100), new Color(100, 0 , 0), new Color(0, 100 , 0)};
	int mode;
	double[] scanned = new double[1];
	int i = scanned.length;
	boolean hitRobot = false;
	public void run() {
		setAdjustRadarForRobotTurn(true);
		setAdjustGunForRobotTurn(true);
		setMode("agressive");
		while (true) {
			if ((int) getEnergy() < 25)
				setMode("defensive");
			else if ((int) getEnergy() > 75)
				setMode("agressive");
			else
				setMode("normal");
			turnRadarRight(360);
		}
	}
	public void setMode(String mode) {
		for (int x = 0; x < modes.length; x++) {
			if (mode == modes[x]) {
				this.mode = x;
				setAllColors(colors[x]);
			}
		}
	}
	public void onScannedRobot(ScannedRobotEvent e) {
		double pos = getRadarHeading();
		if (i < scanned.length - 1) i++;
		else i = 0;
		scanned[i] = pos;
		if (mode == 1 && hitRobot)
			pointGun(getHeading());
		else
			pointGun(pos);
		fire(1);
		move(pos);
	}
	public void move(double targetPos) {
		if (mode == 0) {
			ahead(50);
			turnLeft(45);
		} else if (mode == 1) {
			pointRobot(targetPos);
			ahead(100);
		} else if (mode == 2) {
			retreat(targetPos);
		}
	}
	public void onHitByBullet(HitByBulletEvent e) {
		if (mode == 2) {
			retreat(e.getBearing());
		} else if (mode == 0) {
			turnLeft(180);
			ahead(75);
		}
	}
	public void onHitWall(HitWallEvent e) {
		turnLeft(45);
		ahead(50);
	}
	public void onHitRobot(HitRobotEvent e) {
		hitRobot = true;
	}
	public void retreat(double retreatFrom) {
		pointRobot(retreatFrom);
		back(1000);
	}
	public void pointGun(double targetPos) {
		while ((int) getGunHeading() != (int) targetPos) {
			double gunPos = getGunHeading();
			double offset = targetPos - gunPos;
			if (offset < 0)
				turnGunLeft(offset * -1);
			else
				turnGunRight(offset);
		}
	}
	public void pointRobot(double targetPos) {
		while ((int) getHeading() != (int) targetPos) {
			double robotPos = getHeading();
			double offset = targetPos - robotPos;
			if (offset < 0)
				turnLeft(offset * -1);
			else
				turnRight(offset);
		}
	}
}
