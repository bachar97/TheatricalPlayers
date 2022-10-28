import java.util.HashMap;
import java.util.Map;

public class PlaysFactory {
	public static Integer AudiencePerPlay(Integer audience, String type) {
		Map<String, StatCalculator> types = new HashMap<>();
		types.put("comedy", new ComedyStats());
		types.put("tragedy", new TragedyStats());
		
		if(!types.containsKey(types)) {
			throw new Error("Unkown type: " + type);
		}else {
			return types.get(type).calculate(audience);
		}
	}
}