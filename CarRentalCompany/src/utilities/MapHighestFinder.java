package utilities;
import java.util.Map;
import java.util.Map.Entry;


public class MapHighestFinder<T> {
	
	public T findHighestIn(Map<T, Integer> map) {
		Entry<T, Integer> highest = null;
		for (Entry<T, Integer> entry : map.entrySet()) {
			if (highest == null || entry.getValue() > highest.getValue()) {
				highest = entry;
			}
		}
		return highest == null ? null : highest.getKey();
	}
}
