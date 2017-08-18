package br.unicentro.jah.eyes;

import java.awt.Color;

import br.unicentro.jah.client.TrackWindow;

public class PieceBall extends Piece
{
	public PieceBall() 
	{
		super("Ball", TrackWindow.HSV_LOWER_GREEN, TrackWindow.HSV_UPPER_GREEN);
	}

	@Override
	public Color getBorderColor() 
	{
		return Color.GREEN;
	}
}
