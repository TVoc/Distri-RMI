package utilities;
import java.util.Map;

/**
 * Class that serves to merge numerical relations from one map into another.
 * 
 * @author Thomas Vochten
 *
 * @param <T>
 */
public class MapMerger<T> {
	
	/**
	 * Merges "from" into "to" such that "to" contains the keys it had before calling this method and all keys from "from"
	 * that "to" did not initially possess. Furthermore, for all keys in "from" that "to" already contained, the associated value
	 * in "to" after calling this method will be the associated value in "to" before calling this method added to the associated value
	 * in "from".
	 * 
	 * @param to
	 * 		The map to merge into
	 * @param from
	 * 		The map to merge from
	 * @throws IllegalArgumentException
	 * 		Either to or from are null
	 */
	public void merge(Map<T, Integer> to, Map<T, Integer> from) throws IllegalArgumentException {
		if (to == null || from == null) {
			throw new IllegalArgumentException("to, from or both were null");
		}
		for (T key : from.keySet()) {
			if (! to.containsKey(key)) {
				to.put(key, from.get(key));
			} else {
				to.put(key, to.get(key) + from.get(key));
			}
		}
	}
}
