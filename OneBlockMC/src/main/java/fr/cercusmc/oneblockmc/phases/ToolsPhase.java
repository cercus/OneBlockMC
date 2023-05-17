package fr.cercusmc.oneblockmc.phases;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import fr.cercusmc.oneblockmc.Main;
import fr.cercusmc.oneblockmc.utils.Constantes;
import fr.cercusmc.oneblockmc.utils.FileCustom;
import fr.cercusmc.oneblockmc.utils.MessageUtil;
import fr.cercusmc.oneblockmc.utils.ValidateUtil;
import fr.cercusmc.oneblockmc.utils.enums.PlaceHolderType;

public class ToolsPhase {

	private ToolsPhase() {
	}

	public static Optional<Phase> getPhase(int id) {

		FileCustom c = Main.getFiles().get(Constantes.PHASES);

		if (!c.getConfigurationSection("").getKeys(false).contains(id + ""))
			return Optional.empty();

		ConfigurationSection section = c.getConfigurationSection(id + "");
		String icon = section.getString("icon");
		if (!ValidateUtil.checkEnum(Material.class, icon))
			return Optional.empty();
		String name = MessageUtil.format(section.getString("name"));
		int blocsToReach = section.getInt("blocs_to_reach");
		EnumMap<PlaceHolderType, String> map = new EnumMap<>(PlaceHolderType.class);
		map.put(PlaceHolderType.NB_BLOCK, blocsToReach + "");
		List<String> description = MessageUtil.format(section.getStringList("description"), map);
		Map<Material, Integer> blocs = treatmentBlocs(section.getStringList("blocs"));
		Map<EntityType, Integer> mobs = treatmentMobs(section.getStringList("mobs"));

		List<ItemStack> items = treatmentItems(section.getStringList("items"));

		
		return Optional.of(new Phase(id, Material.valueOf(icon), name, blocsToReach, description, blocs, mobs, items));
	}

	public static List<Phase> getAllPhases() {
		FileCustom c = Main.getFiles().get(Constantes.PHASES);
		List<Phase> phases = new ArrayList<>();

		for (String i : c.getConfigurationSection("").getKeys(false)) {
			getPhase(Integer.parseInt(i)).ifPresent(phases::add);
		}
		return phases;
	}

	private static List<ItemStack> treatmentItems(List<String> stringList) {
		List<ItemStack> its = new ArrayList<>();
		for (String i : stringList) {

			if (ValidateUtil.convertStringItemToHashMap(i).isEmpty())
				continue;

			its.add(ValidateUtil.convertStringItemToItemStack(i));
		}

		return its;
	}

	private static Map<Material, Integer> treatmentBlocs(List<String> stringList) {

		EnumMap<Material, Integer> mats = new EnumMap<>(Material.class);
		for (String i : stringList) {
			String[] split = i.split("\\s");
			if (!ValidateUtil.checkEnum(Material.class, split[0].toUpperCase()) || ValidateUtil.stringIsDigit(i))
				continue;

			mats.put(Material.valueOf(split[0].toUpperCase()), Integer.parseInt(split[1]));
		}

		return mats;
	}

	private static Map<EntityType, Integer> treatmentMobs(List<String> stringList) {

		EnumMap<EntityType, Integer> mats = new EnumMap<>(EntityType.class);
		for (String i : stringList) {
			String[] split = i.split("\\s");
			if (!ValidateUtil.checkEnum(EntityType.class, split[0].toUpperCase()) || ValidateUtil.stringIsDigit(i))
				continue;

			mats.put(EntityType.valueOf(split[0].toUpperCase()), Integer.parseInt(split[1]));
		}

		return mats;
	}

	/**
	 * Méthode qui consiste à attribuer à chaque élément de la liste une plage de
	 * valeurs aléatoires qui correspond à sa probabilité de sortie. Lorsque vous
	 * générez un nombre aléatoire, vous pouvez ensuite vérifier dans quelle plage
	 * de valeurs il tombe et sélectionner l'élément correspondant.
	 * 
	 * Exemple : liste = ["arbre", "branche", "feuille", "racine", "sève", "écorce"]
	 * proba = [16.6, 13.6, 16.6, 16.6, 19.6, 16.6] ==> Le mot "arbre" doit sortir
	 * dans 16,6% des cas
	 * 
	 * @param <T>
	 * @param items         Liste d'item
	 * @param probabilities Liste de probabilité
	 */
	public static <T> T selectionAleatoirePondere(Map<T, Integer> map) {
		LinkedHashMap<T, Double> cumulativeProbabilities = new LinkedHashMap<>(map.size());
		double currentSum = 0;

		for (Map.Entry<T, Integer> ent : map.entrySet()) {
			currentSum += ent.getValue();
			cumulativeProbabilities.put(ent.getKey(), currentSum);
		}
		Random random = null;
		try {
			random = SecureRandom.getInstanceStrong();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		double randomValue = random.nextDouble() * currentSum;

		for(Map.Entry<T, Double> ent : cumulativeProbabilities.entrySet()) {
			if(randomValue < ent.getValue())
				return ent.getKey();
		}

		return null;
	}

}
