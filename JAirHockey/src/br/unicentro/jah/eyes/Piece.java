package br.unicentro.jah.eyes;

import java.awt.Color;

import org.opencv.core.Scalar;

import br.unicentro.jah.client.TrackWindow;

public abstract class Piece 
{
	private final String name;
	
	private double x;
	private double y;
	
	private Scalar hsvLower;
	private Scalar hsvUpper;
	
	public Piece(String name, Scalar hsvLower, Scalar hsvUpper)
	{
		this.name = name;
		this.x = 0;
		this.y = 0;
		this.hsvLower = hsvLower;
		this.hsvUpper = hsvUpper;
	}
	
	public String getName() {
		return this.name;
	}
	
	public abstract Color getBorderColor();
	
	public void setX(double x){
		this.x = x;
	}
	
	public void setY(double y){
		this.y = y;
	}
	
	public void setPosition(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public double getX(){
		return this.x * ViewCanvas.RESOLUTION_FACTOR;
	}
	
	public double getY(){
		return this.y * ViewCanvas.RESOLUTION_FACTOR + 100;
	}
	
	public Scalar getHSVLower() {
		return this.hsvLower;
	}
	
	public Scalar getHSVUpper() {
		return this.hsvUpper;
	}
	
	@Override
	public String toString() 
	{
		return "Object["+name+", "+(int)x+", "+(int)y+"]";
	}
}
