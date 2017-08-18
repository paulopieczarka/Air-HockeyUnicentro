package uno;

import org.opencv.core.Core;
import org.sintef.jarduino.comm.Serial4JArduino;

public class Main 
{
	public static MotorController arduino;
	
	private final static boolean enableArduino = true;
	
	public static void main(String[] args)
	{
		String serialPort;
        if (args.length == 1) {
            serialPort = args[0];
        } else {
            serialPort = Serial4JArduino.selectSerialPort();
        }
    
        arduino = new MotorController(serialPort);
        arduino.runArduinoProcess();
        
        arduino.setSpeed(0);
        arduino.goDirLeft();
	}
}
