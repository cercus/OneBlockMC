package fr.cercusmc.oneblockmc.utils;

public class ValidateUtil {
	
	private ValidateUtil() {}
	
	public static <E extends Enum<E>> boolean checkEnum(Class<E> aEnum, String value) {
		if (value == null || !aEnum.isEnum())
			return false;
		try {
			Enum.valueOf(aEnum, value.toUpperCase());
			return true;
		} catch (IllegalArgumentException | NullPointerException e) {
			return false;
		}
	}

}
