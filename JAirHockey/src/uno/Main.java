package uno;

import org.sintef.jarduino.comm.Serial4JArduino;

public class Main 
{
	public static MotorController arduino;
	
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
        
        Interface SliderBar = new Interface();
        SliderBar.SliderBar();
        
       
        
//        MotorController.setSpeed(0);
	}
}
