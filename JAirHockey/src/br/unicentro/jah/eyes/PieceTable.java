package br.unicentro.jah.eyes;

import java.awt.Color;

import br.unicentro.jah.client.TrackWindow;

public class PieceTable extends Piece
{

	public PieceTable() 
	{
		super("Table", TrackWindow.HSV_LOWER_TABLE, TrackWindow.HSV_UPPER_TABLE);
	}

	@Override
	public Color getBorderColor() {
		return Color.ORANGE;
	}

}
