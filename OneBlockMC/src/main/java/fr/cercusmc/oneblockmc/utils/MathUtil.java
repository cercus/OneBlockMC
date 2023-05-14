package fr.cercusmc.oneblockmc.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MathUtil {

	private MathUtil() {
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
	    return map.entrySet().stream()
	        .sorted(Map.Entry.comparingByValue())
	        .collect(Collectors.toMap(
	            Map.Entry::getKey,
	            Map.Entry::getValue,
	            (oldValue, newValue) -> oldValue,
	            LinkedHashMap::new
	        ));
	}

}
