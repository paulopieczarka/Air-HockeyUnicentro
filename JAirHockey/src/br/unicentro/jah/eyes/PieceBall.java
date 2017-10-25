package br.unicentro.jah.eyes;

import java.awt.Color;

import br.unicentro.jah.client.TrackWindow;

public class PieceBall extends Piece
{
	public PieceBall() 
	{
		super("Ball", TrackWindow.HSV_LOWER_RED, TrackWindow.HSV_UPPER_RED);
	}

	@Override
	public Color getBorderColor() 
	{
		return Color.GREEN;
	}
}
