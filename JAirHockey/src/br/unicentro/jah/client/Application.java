package br.unicentro.jah.client;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.sintef.jarduino.comm.Serial4JArduino;

import br.unicentro.jah.eyes.OpenCamera;
import br.unicentro.jah.eyes.ViewCamera;
import uno.MotorController;

public class Application extends JFrame implements WindowListener
{
	private static final long serialVersionUID = 1L;
	
	private OpenCamera camera;
	private ViewCamera canvas;
	public static MotorController arduino;
	
	/* Create App window. */
	public Application() 
	{
		super("JAir-Hockey");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(new Dimension(1080, 600));
		setLocationRelativeTo(null);
		
		String serialPort;
		serialPort = Serial4JArduino.selectSerialPort();
    
        arduino = new MotorController(serialPort);
        arduino.runArduinoProcess();
		
		JFrame frame = this;

		JMenuItem miTrackWindow = new JMenuItem("Track Window");
		miTrackWindow.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) 
			{
				TrackWindow trackWindow = new TrackWindow(frame);
				trackWindow.setVisible(true);
			}
		});
		
		JMenu mView = new JMenu("View");
		mView.add(miTrackWindow);
		
		JCheckBoxMenuItem miTrackPlayer1 = new JCheckBoxMenuItem("Player 1", true);
		JCheckBoxMenuItem miTrackPlayer2 = new JCheckBoxMenuItem("Player 2", true);
		JCheckBoxMenuItem miTrackBall = new JCheckBoxMenuItem("Ball", true);

		JMenu mTrack = new JMenu("Track");
		mTrack.add(miTrackPlayer1);
		mTrack.add(miTrackPlayer2);
		mTrack.add(miTrackBall);
		
		JMenuItem miMotor = new JMenuItem("Manual Motor");
		miMotor.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) 
			{
				
			}
		});
		
		JCheckBoxMenuItem miFollow = new JCheckBoxMenuItem("Follow disk", true);
		
		JMenu mArduino = new JMenu("Arduino");
		mArduino.add(miMotor);
		mArduino.add(miFollow);
		
		JMenuBar menubar = new JMenuBar();
		menubar.add(mView);
		menubar.add(mTrack);
		menubar.add(mArduino);
		
		setJMenuBar(menubar);
		
		// Make this a window listener.
		addWindowListener(this);
	}
	
	/* Start Arduino and Webcam then render JFrame. */
	public void start()
	{
		startWebcam();
		startArduino();
		setVisible(true);
	}
	
	public void startWebcam()
	{
		System.out.println("Starting webcam..");
		
		camera = new OpenCamera();
		camera.start();
		
		canvas = new ViewCamera(camera);
		setContentPane(canvas);
	}
	
	public void startArduino()
	{
		System.out.println("Starting arduino..");
	}

	@Override
	public void windowOpened(WindowEvent e) {
		
	}

	@Override
	public void windowClosing(WindowEvent e) 
	{
		canvas.stop();
		camera.close();
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}
}
