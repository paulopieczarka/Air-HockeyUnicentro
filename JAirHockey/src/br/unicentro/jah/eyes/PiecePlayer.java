package br.unicentro.jah.eyes;

import java.awt.Color;

import org.opencv.core.Scalar;

public class PiecePlayer extends Piece
{
	private Color color;
	
	public PiecePlayer(String name, Scalar hsvLower, Scalar hsvUpper, Color color) 
	{
		super(name, hsvLower, hsvUpper);
		this.color = color;
	}
	
	@Override
	public Color getBorderColor() 
	{
		return color;
	}
}
