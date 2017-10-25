package br.unicentro.jah.ai;

import br.unicentro.jah.eyes.Trackable;

public class HelperAI 
{
	public static double[] getDistance(Trackable t1, Trackable t2)
	{
		double[] vector = new double[3];
		vector[0] = Math.abs(t2.getX() - t1.getX());  // dx
		vector[1] = Math.abs(t2.getY() - t1.getY());  // dy
		vector[2] = Math.sqrt(vector[0] + vector[1]);  // hipotenusa
		
		return vector;
	}
}
