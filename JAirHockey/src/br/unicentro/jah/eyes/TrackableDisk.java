package br.unicentro.jah.eyes;

import java.awt.Color;

import org.opencv.core.Rect;

import br.unicentro.jah.client.TrackWindow;

public class TrackableDisk extends Trackable
{
	public TrackableDisk(Rect startingPoint) 
	{
		super("Disk", startingPoint, Color.RED);
		setHSVBounds(TrackWindow.HSV_LOWER_RED, TrackWindow.HSV_UPPER_RED);
	}
}
