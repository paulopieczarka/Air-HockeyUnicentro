
package uno;

import org.sintef.jarduino.DigitalPin;
import org.sintef.jarduino.DigitalState;
import org.sintef.jarduino.JArduino;
import org.sintef.jarduino.PWMPin;
import org.sintef.jarduino.PinMode;

import br.unicentro.jah.ai.PlayerAIBeta;
import br.unicentro.jah.eyes.ViewCamera;

public class MotorController extends JArduino
{
	
	private DigitalPin esqPinA = DigitalPin.PIN_3;
	private DigitalPin dirPinA = DigitalPin.PIN_2;
	private PWMPin speedPinA = PWMPin.PWM_PIN_9;
	
	private static MotorDirection motorDirection;
	private static int desiredSpeed;
	
	private static int minSpeed = 500;
	
	// TODO: Move this
	private PlayerAIBeta playerAI;
	
	public MotorController(String port) 
	{
		super(port);
		
		this.playerAI = new PlayerAIBeta();
	}

	@Override
	protected void setup() 
	{
		desiredSpeed = 0;
		motorDirection = MotorDirection.NONE;
		
		pinMode(esqPinA, PinMode.OUTPUT);
		pinMode(dirPinA, PinMode.OUTPUT);
	}
	
	@Override
	protected void loop() 
	{
		if(ViewCamera.getPlayer1() != null) 
		{
			playerAI.onUpdate(
					ViewCamera.getDisk(), 
					ViewCamera.getPlayer1(), 
					this
				);
		}
		
		int outputValue = map(desiredSpeed, 0, 1023, 0, 255);
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
	
	/** 500...1024 */
	public static void setSpeed(int amount)
	{
		if(amount == 0) {
			motorDirection = MotorDirection.NONE;
		}
		else if(amount < 0) {
			motorDirection = MotorDirection.LEFT;
		}
		else {
			motorDirection = MotorDirection.RIGHT;
		}
		
		// Set minimum speed for the motor.
		if(amount > 0 && amount < minSpeed) {
			amount = minSpeed;
		}
		
		desiredSpeed = Math.abs(amount);
	}
}
