package br.unicentro.jah.client;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.opencv.core.Scalar;

public class TrackWindow extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	// player 1.
	public static Scalar HSV_LOWER_RED = new Scalar(0, 91, 93);
	public static Scalar HSV_UPPER_RED = new Scalar(219, 255, 255);
	
	// player 2.
	public static Scalar HSV_LOWER_BLUE = new Scalar(84, 94, 60);
	public static Scalar HSV_UPPER_BLUE = new Scalar(255, 255, 171);
		
	// ball.
	public static Scalar HSV_LOWER_GREEN = new Scalar(36, 67, 0);//new Scalar(34, 143, 35);
	public static Scalar HSV_UPPER_GREEN = new Scalar(80, 255, 255);//new Scalar(73, 255, 91);
	
	// table.
	public static Scalar HSV_LOWER_TABLE = new Scalar(0, 24, 76);
	public static Scalar HSV_UPPER_TABLE = new Scalar(255, 219, 255);
	
	public static Scalar lower = new Scalar(0, 0, 0);
	public static Scalar upper = new Scalar(100, 255, 255);
	
	public TrackWindow(JFrame parent)
	{
		super("JairHockey - Objetcs");
		setSize(new Dimension(360, 320));
		setLocationRelativeTo(parent);
		setLayout(new GridLayout(7, 2));
		
		JLabel lbLower = new JLabel("LOWER");
		add(lbLower);
		
		JLabel lbUpper = new JLabel("UPPER");
		add(lbUpper);
		
		JSlider slideLH = new JSlider(0, 255, 0);
		JSlider slideLS = new JSlider(0, 255, 0);
		JSlider slideLV = new JSlider(0, 255, 0);
		
		JSlider slideUH = new JSlider(0, 255, 0);
		JSlider slideUS = new JSlider(0, 255, 0);
		JSlider slideUV = new JSlider(0, 255, 0);
		
		ChangeListener changeListener = new ChangeListener() {
			@Override public void stateChanged(ChangeEvent e) 
			{
				lower.set(new double[] {slideLH.getValue(), slideLS.getValue(), slideLV.getValue()});
				upper.set(new double[] {slideUH.getValue(), slideUS.getValue(), slideUV.getValue()});
				
				lbLower.setText(lower.toString());
				lbUpper.setText(upper.toString());
			}
		};
		
		slideLH.addChangeListener(changeListener);
		slideLS.addChangeListener(changeListener);
		slideLV.addChangeListener(changeListener);
		
		slideUH.addChangeListener(changeListener);
		slideUS.addChangeListener(changeListener);
		slideUV.addChangeListener(changeListener);
		
		add(new JLabel("Low H"));
		add(slideLH);
		
		add(new JLabel("Low S"));
		add(slideLS);
		
		add(new JLabel("Low V"));
		add(slideLV);
		
		add(new JLabel("High H"));
		add(slideUH);
		
		add(new JLabel("High S"));
		add(slideUS);
		
		add(new JLabel("High V"));
		add(slideUV);
		
		// Nodes.
	}
}
