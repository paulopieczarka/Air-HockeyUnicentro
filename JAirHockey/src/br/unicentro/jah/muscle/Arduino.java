package br.unicentro.jah.muscle;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Arduino extends JFrame
{
	private static final long serialVersionUID = 1L;

	public Arduino(String port)
	{
		setTitle("Arduino");
		setSize(new Dimension(200, 400));
	}
	
	public void start()
	{
		setVisible(true);
	}
}
