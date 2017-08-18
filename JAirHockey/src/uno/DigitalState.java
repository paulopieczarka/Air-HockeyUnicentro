package uno;



import java.util.HashMap;
import java.util.Map;

public enum DigitalState {
	LOW((byte)0),
	HIGH((byte)127);

	private final byte value;
	
	private DigitalState(byte value){
		this.value = value;
	}
	
	public byte getValue(){
		return value;
	}
	
	private static final Map<Byte, DigitalState> map;
	
	static {
		map = new HashMap<Byte, DigitalState>();
		map.put((byte)0, DigitalState.LOW);
		map.put((byte)1, DigitalState.HIGH);
	}
	
	public static DigitalState fromValue(byte b) {
		return map.get(b);
	}
	
}