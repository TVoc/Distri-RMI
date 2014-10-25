package utilities;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import utilities.MapMerger;


public class MapMergerTest {
	
	Map<String, Integer> fromMap;
	Map<String, Integer> toMap;

	@Before
	public void setUp() throws Exception {
		fromMap = new HashMap<String, Integer>();
		fromMap.put("haha", 2);
		fromMap.put("keke", 1);
		fromMap.put("blabla", 3);
		
		toMap = new HashMap<String, Integer>();
		toMap.put("haha", 5);
		toMap.put("blabla", 1);
	}

	@Test
	public void test() {
		MapMerger<String> merger = new MapMerger<String>();
		merger.merge(toMap, fromMap);
		assertTrue(toMap.get("haha") == 7);
		assertTrue(toMap.get("keke") == 1);
		assertTrue(toMap.get("blabla") == 4);
	}

}
