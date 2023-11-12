package fr.cercusmc.oneblockmc.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import fr.cercusmc.oneblockmc.utils.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import fr.cercusmc.oneblockmc.utils.enums.Enchantments;

public class ValidateUtil {
	
	private static final String ENCHANTMENTS = "enchantments";
	private static final String LORE = "lore";
	private static final String NAME = "name";
	private static final String COUNT = "count";
	private static final String MATERIAL = "material";

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
	
	public static boolean stringIsItemValid(String str) {
		Pattern pattern  = Pattern.compile("(^[a-zA-Z_]+$)|(^[a-zA-Z_]+:\\d+$)|(^[a-zA-Z_]+:\\d+\\{name=(\\[\".+\"]|\\[\\]|\\[\"\\s*\"\\]),\\s*lore=(\\[\".+\"]|\\[\\]|\\[\"\\s*\"\\]),\\s*enchantments=((\\[\\])|(\\[\"\"\\])|(\\[\"[A-Za-z_]+:\\d+\"\\]})|\\[(\"[A-Za-z_]+:\\d+\",\\s*)+\"[A-Za-z_]+:\\d+\"]\\}))");
		
		return pattern.matcher(str).find();
	}
	
	public static Map<String, String> convertStringItemToHashMap(String str) {
		if(!stringIsItemValid(str)) return new HashMap<>();
		
		HashMap<String, String> res = new HashMap<>();
		
		if(str.matches("^[a-zA-Z_]+$") && !str.contains(":")) {
			res.put(MATERIAL, str);
			res.put(COUNT, "1");
			res.put(NAME, "");
			res.put(LORE, "");
			res.put(ENCHANTMENTS, "");
			return res;
		}
		
		if(!str.contains("{") && !str.contains("}")) {
			res.put(MATERIAL, str.substring(0, str.indexOf(':')));
			res.put(COUNT, str.substring(str.indexOf(':')+1));
			res.put(NAME, "");
			res.put(LORE, "");
			res.put(ENCHANTMENTS, "");
			return res;
		}
		res.put(MATERIAL, str.substring(0, str.indexOf(':')));
		res.put(COUNT, str.substring(str.indexOf(':')+1, str.indexOf('{')));
		if(str.contains("name=["))
			res.put(NAME, str.substring(str.indexOf('[')+2, str.indexOf(']')-1));
		else
			res.put(NAME, "");
		if(str.contains("lore=["))
			res.put(LORE, str.substring(str.indexOf("lore=[")+5, str.indexOf("enchant")-2).replace("\"", ""));
		else
			res.put(LORE, "");
		if(str.contains("enchantments=["))
			res.put(ENCHANTMENTS, str.substring(str.indexOf("enchantments=[")+13, str.length()-1).replace("\"", ""));
		else
			res.put(ENCHANTMENTS, "");
		
		return res;
	}
	

	
	public static ItemStack convertStringItemToItemStack(String str) {
		if(!stringIsItemValid(str)) return new ItemStack(Material.AIR);
		
		Map<String, String> splitMap = convertStringItemToHashMap(str);
		if(splitMap.isEmpty()) return new ItemStack(Material.AIR);
		
		if(!checkEnum(Material.class, splitMap.get(MATERIAL))) return new ItemStack(Material.AIR);
		
		ItemBuilder it = new ItemBuilder(Material.valueOf(splitMap.get(MATERIAL).toUpperCase()), Integer.parseInt(splitMap.get(COUNT)));
		if(!splitMap.get(NAME).isBlank()) it.setName(MessageUtil.format(splitMap.get(NAME)));
		
		if(!splitMap.get(LORE).isBlank()) {
			List<String> lores = treatmentLore(splitMap);
			
			it.setLores(lores);
		}
		
		if(!splitMap.get(ENCHANTMENTS).isBlank()) {
			Map<Enchantment, Integer> enchantments = treatmentEnchantments(splitMap);
			it.setEnchantments(enchantments);
			if(it.getMaterial().equals(Material.ENCHANTED_BOOK)) {
				return it.toEnchantedBook();
			}
		} else {
			if(it.getMaterial().equals(Material.ENCHANTED_BOOK)) {
				
				return it.toEnchantedBook();
			}
		}
		return it.toItemStack();
		
		
	}

	private static Map<Enchantment, Integer> treatmentEnchantments(Map<String, String> splitMap) {
		Map<Enchantment, Integer>  enchantments = new HashMap<>();
		String[] splitEnch = splitMap.get(ENCHANTMENTS).replaceAll("[\\[\\]]", "") .split("\",\\s+");
		
		for(String i : splitEnch) {
			if(!checkEnum(Enchantments.class, i.split(":")[0])) continue;
			
			enchantments.put(Enchantments.valueOf(i.split(":")[0].toUpperCase()).getEnchantment(), Integer.parseInt(i.split(":")[1]));
			
		}
		return enchantments;
	}

	private static List<String> treatmentLore(Map<String, String> splitMap) {
		String[] splitLore = splitMap.get(LORE).replaceAll("[\\[\\]]", "") .split("\",\\s+");
		List<String> lores = new ArrayList<>();
		for(String l : splitLore) {
			if(l.equalsIgnoreCase("null"))
				lores.add(l.replaceAll("^[nN][uU][lL][lL]$", ""));
			else
				lores.add(l);
		}
		return lores;
	}
	
	public static boolean stringIsDigit(String str) {
		
		try {
			Integer.parseInt(str);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}

}
