package br.unicentro.jah.ai;

import java.awt.Point;
import java.util.ArrayList;

import br.unicentro.jah.eyes.Trackable;
import uno.MotorController;

public class PlayerAIBeta 
{
	private ArrayList<Point> puckCoordHistory;
	
	public void onUpdate(Trackable target, Trackable player, MotorController motorController)
	{
		// getBouncePoint(target, player);
		if(HelperAI.getDistance(player, target)[2] > 11)
		{
			if(player.getY() < target.getY()) {
				MotorController.setSpeed(-700);
			}
			else if(player.getY() > target.getY()) {
				MotorController.setSpeed(700);
			}
		}
		else {
			MotorController.setSpeed(0);
		}
	}
	
	protected void getBouncePoint(Trackable target, Trackable player)
	{
		if(puckCoordHistory == null) {
			puckCoordHistory = new ArrayList<>();
		}
		
		puckCoordHistory.add( new Point(target.getX(), target.getY()) );
		
		if(puckCoordHistory.size() < 2) {
			return;
		}
		
		int dx = Math.abs(puckCoordHistory.get(1).x - puckCoordHistory.get(0).x);
		int dy = Math.abs(puckCoordHistory.get(1).y - puckCoordHistory.get(0).y);
		double theta = Math.atan2(dy, dx);
		
		puckCoordHistory.remove(0);
		
		double bounceX = Math.cos(theta) * 100;
		double bounceY = Math.sin(theta) * 100;
		
		System.out.println(theta+" => "+bounceX+", "+bounceY);
	}
}
