package uno;

import org.sintef.jarduino.DigitalPin;
import org.sintef.jarduino.DigitalState;
import org.sintef.jarduino.JArduino;
import org.sintef.jarduino.PWMPin;
import org.sintef.jarduino.PinMode;

public class MotorControllerOld extends JArduino
{
	private DigitalPin esqPinA = DigitalPin.PIN_3;
	private DigitalPin dirPinA = DigitalPin.PIN_2;
	private PWMPin speedPinA = PWMPin.PWM_PIN_9;
	
	private int sensorvalue;
	int outputValue = 0;
	
	private MotorDirection motorDirection;
	
	public MotorControllerOld(String port) 
	{
		super(port);
		this.sensorvalue = 0;
		this.motorDirection = MotorDirection.NONE;
	}
	
	@Override
	protected void setup() 
	{
		pinMode(esqPinA, PinMode.OUTPUT);
		pinMode(dirPinA, PinMode.OUTPUT);
	}

	@Override
	protected void loop() 
	{
//		if(motorDirection.equals(MotorDirection.LEFT)){
//			sensorvalue += 50;
//			if(sensorvalue > 1024)
//				sensorvalue = 1024;
//		}
		
		outputValue = map(sensorvalue, 0, 1023, 0, 255);
		analogWrite(speedPinA, (byte)outputValue);
		
		if(motorDirection.equals(MotorDirection.LEFT))
		{
			digitalWrite(dirPinA, DigitalState.LOW);
			digitalWrite(esqPinA, DigitalState.HIGH);
		}
		else if(motorDirection.equals(MotorDirection.RIGHT))
		{
			digitalWrite(dirPinA, DigitalState.HIGH);
			digitalWrite(esqPinA, DigitalState.LOW);
		}
		else
		{
			digitalWrite(dirPinA, DigitalState.LOW);
			digitalWrite(esqPinA, DigitalState.LOW);
		}
	}
	
	public void goDirLeft()
	{
		this.motorDirection = MotorDirection.LEFT;
	}
	
	public void goDirRight()
	{
		this.motorDirection = MotorDirection.RIGHT;
	}
	
	public void stopMotor()
	{
		this.motorDirection = MotorDirection.NONE;
	}
	
	public void setSpeed(int value)
	{
		sensorvalue = value;
	}
}
