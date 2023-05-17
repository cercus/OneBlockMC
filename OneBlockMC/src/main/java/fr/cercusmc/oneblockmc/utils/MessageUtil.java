package fr.cercusmc.oneblockmc.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.cercusmc.oneblockmc.utils.enums.PlaceHolderType;
import net.md_5.bungee.api.ChatColor;

public class MessageUtil {
	
	private static final Pattern patternHex = Pattern.compile("#[a-fA-F0-9]{6}");
	private static Logger logger = new Logger();
	
	private MessageUtil() {}

	/**
	 * Translate a message given with a conversion of hex code and color minecraft
	 * in real colors and replace placeholders given with values given <br />
	 * Hex code format : #[a-fA-F0-9]{6} - Exemple : #A777E2, #000000, #FFFFFF
	 * <br />
	 * Color minecraft : &amp;[4c6e2ab319d5f780] - Exemple : &amp;e : yellow <br />
	 * List of colors minecraft : https://htmlcolorcodes.com/minecraft-color-codes/
	 * 
	 * @param message      The message to format
	 * @param placeholders List of placeholders in message (e.g %player%,
	 *                     %entity%...) - Can be null
	 * @param values       List of values for each placeholder to replace - Can be
	 *                     null
	 * @return A String with formatted message
	 */
	public static String format(String message, @Nullable Collection<String> placeholders,
			@Nullable Collection<String> values) {
		message = format(message);
		if (placeholders != null && values != null && placeholders.size() == values.size()) {
			for (int index = 0; index < placeholders.size(); index++)
				message = message.replaceAll(placeholders.stream().toList().get(index),
						values.stream().toList().get(index));
		}

		return ChatColor.translateAlternateColorCodes('&', message);

	}

	/**
	 * Translate a message given with a conversion of hex code and color minecraft
	 * in real colors Hex code format : #[a-fA-F0-9]{6} - Exemple : #A777E2,
	 * #000000, #FFFFFF <br />
	 * Color minecraft : &amp;[4c6e2ab319d5f780] - Exemple : &amp;e : yellow <br />
	 * List of colors minecraft : https://htmlcolorcodes.com/minecraft-color-codes/
	 * 
	 * @param message The message to format
	 * @return A String with formatted message
	 */
	public static String format(String message) {
		Matcher matcher = patternHex.matcher(message);
		while (matcher.find()) {
			String color = message.substring(matcher.start(), matcher.end());
			String tmp = message.replace(color, ChatColor.of(color) + "");
			message = tmp;
			matcher = patternHex.matcher(message);
		}
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	public static String format(String message, Map<PlaceHolderType, String> values) {
		String messagesFormatted = format(message);

		for (Map.Entry<PlaceHolderType, String> entries : values.entrySet()) {
			messagesFormatted = messagesFormatted.replaceAll(entries.getKey().getPlaceHolderName(), entries.getValue());
		}

		return messagesFormatted;

	}

	public static List<String> format(Collection<String> messages,
			Map<PlaceHolderType, String> values) {
		List<String> messagesFormatted = format(messages);
		List<String> messagesReplaces = new ArrayList<>();
		for (String m : messagesFormatted) {
			messagesReplaces.add(format(m, values));
		}

		return messagesReplaces;

	}

	/**
	 * Translate a collection of messages given with a conversion of hex code and
	 * color minecraft in real colors and replace placeholders given with values
	 * given <br />
	 * Hex code format : #[a-fA-F0-9]{6} - Exemple : #A777E2, #000000, #FFFFFF
	 * <br />
	 * Color minecraft : &amp;[4c6e2ab319d5f780] - Exemple : &amp;e : yellow <br />
	 * List of colors minecraft : https://htmlcolorcodes.com/minecraft-color-codes/
	 * 
	 * @param messages     List of messages to format
	 * @param placeholders List of placeholders in message (e.g %player%,
	 *                     %entity%...) - Can be null
	 * @param values       List of values for each placeholder to replace - Can be
	 *                     null
	 * @return List of String with formatted message
	 */
	public static List<String> format(Collection<String> messages,
			@Nullable Collection<String> placeholders, @Nullable Collection<String> values) {
		List<String> messagesFormatted = format(messages);
		List<String> messagesReplaces = new ArrayList<>();
		if (placeholders != null && values != null && placeholders.size() == values.size()) {
			for (String m : messagesFormatted) {
				for (int index = 0; index < placeholders.size(); index++) {
					m = m.replaceAll(placeholders.stream().toList().get(index), values.stream().toList().get(index));
				}
				messagesReplaces.add(m);
			}
		}

		messagesFormatted.clear();
		messagesReplaces.forEach(k -> messagesFormatted.add(ChatColor.translateAlternateColorCodes('&', k)));
		return messagesFormatted;

	}

	/**
	 * Translate a collection of messages given with a conversion of hex code and
	 * color minecraft in real colors Hex code format : #[a-fA-F0-9]{6} - Exemple :
	 * #A777E2, #000000, #FFFFFF <br />
	 * Color minecraft : &amp;[4c6e2ab319d5f780] - Exemple : &amp;e : yellow <br />
	 * List of colors minecraft : https://htmlcolorcodes.com/minecraft-color-codes/
	 * 
	 * @param messages The message to format
	 * @return A String with formatted message
	 */
	public static List<String> format(Collection<String> messages) {
		ArrayList<String> messagesFormatted = new ArrayList<>();
		messages.forEach(k -> messagesFormatted.add(format(k)));
		return messagesFormatted;
	}

	/**
	 * Send a message to player by his pseudo
	 * 
	 * @param player  The player
	 * @param message The message to send
	 */
	public static void sendMessage(Player player, String message) {
		if(player == null)
			logger.info(format(message, null, null));
		else
			player.sendMessage(format(message, null, null));
	}

	/**
	 * Send a message to player by his pseudo
	 * 
	 * @param player       The player
	 * @param message      The message to send
	 * @param placeholders List of placeholders in message (e.g %player%,
	 *                     %entity%...) - Can be null
	 * @param values       List of values for each placeholder to replace - Can be
	 *                     null
	 */
	public static void sendMessage(Player player, String message, Collection<String> placeholders,
			Collection<String> values) {
		if(player == null)
			logger.info(format(message, placeholders, values));
		else
			player.sendMessage(format(message, placeholders, values));
	}
	
	
	
	/**
	 * Send a message to player by his pseudo
	 * 
	 * @param player       The player
	 * @param message      The message to send
	 * @param placeholders List of placeholders in message (e.g %player%,
	 *                     %entity%...) - Can be null
	 * @param values       List of values for each placeholder to replace - Can be
	 *                     null
	 */
	public static void sendMessage(Player player, String message, Map<PlaceHolderType, String> values) {
		if(player == null)
			logger.info(format(message, values));
		else
			player.sendMessage(format(message, values));
	}
	
	public static void sendMessage(Player player, Collection<String> messages, Map<PlaceHolderType, String> values) {
		messages.forEach(k -> sendMessage(player, k, values));
	}

	/**
	 * Send a list of messages to player by his pseudo
	 * 
	 * @param player   The player
	 * @param messages A list of messages to send
	 */
	public static void sendMessage(Player player, Collection<String> messages) {
		messages.forEach(k -> sendMessage(player, k, null, null));

	}

	/**
	 * Send a list of messages to player by his pseudo
	 * 
	 * @param player       The player
	 * @param messages     The message to send
	 * @param placeholders List of placeholders in message (e.g %player%,
	 *                     %entity%...) - Can be null
	 * @param values       List of values for each placeholder to replace - Can be
	 *                     null
	 */
	public static void sendMessage(Player player, Collection<String> messages,
			@Nullable Collection<String> placeholders, @Nullable Collection<String> values) {
		messages.forEach(k -> sendMessage(player, k, placeholders, values));
	}

	/**
	 * Send a message to player by his UUID
	 * 
	 * @param uuid    The UUID of player
	 * @param message The message to send
	 */
	public static void sendMessage(UUID uuid, String message) {
		if (Bukkit.getPlayer(uuid) != null)
			Bukkit.getPlayer(uuid).sendMessage(format(message, null, null));
	}

	/**
	 * Send a message to player by his UUID
	 * 
	 * @param uuid         The UUID of player
	 * @param message      The message to send
	 * @param placeholders List of placeholders in message (e.g %player%,
	 *                     %entity%...) - Can be null
	 * @param values       List of values for each placeholder to replace - Can be
	 *                     null
	 */
	public static void sendMessage(UUID uuid, String message, @Nullable Collection<String> placeholders,
			@Nullable Collection<String> values) {
		if (Bukkit.getPlayer(uuid) != null)
			Bukkit.getPlayer(uuid).sendMessage(format(message, placeholders, values));
	}

	/**
	 * Send a list of messages to player by his UUID
	 * 
	 * @param uuid     The UUID of player
	 * @param messages A list of messages to send
	 */
	public static void sendMessage(UUID uuid, Collection<String> messages) {
		messages.forEach(k -> sendMessage(uuid, k));

	}

	/**
	 * Send a list of messages to player by his UUID
	 * 
	 * @param uuid         The UUID of player
	 * @param messages     The message to send
	 * @param placeholders List of placeholders in message (e.g %player%,
	 *                     %entity%...) - Can be null
	 * @param values       List of values for each placeholder to replace - Can be
	 *                     null
	 */
	public static void sendMessage(UUID uuid, Collection<String> messages,
			@Nullable Collection<String> placeholders, @Nullable Collection<String> values) {
		messages.forEach(k -> sendMessage(uuid, k, placeholders, values));
	}

	/**
	 * Broadcast a list of messages to all player
	 * 
	 * @param messages     : List of messages to send to the player
	 * @param placeholders : List of placeholder in message (e.g %player%) - Can be
	 *                     null
	 * @param values:      List of values associated in placeholder - Can be null
	 */
	public static void broadcastMessage(Collection<String> messages,
			@Nullable Collection<String> placeholders, @Nullable Collection<String> values) {
		messages.forEach(k -> broadcastMessage(k, placeholders, values));
	}

	/**
	 * Broadcast a message to all player
	 * 
	 * @param message      : List of messages to send to the player
	 * @param placeholders : List of placeholder in message (e.g %player%) - Can be
	 *                     null
	 * @param values:      List of values associated in placeholder - Can be null
	 */
	public static void broadcastMessage(String message, @Nullable Collection<String> placeholders,
			@Nullable Collection<String> values) {
		Bukkit.broadcastMessage(format(message, placeholders, values));
	}

	/**
	 * Broadcast a message to all player
	 * 
	 * @param message : List of messages to send to the player
	 * 
	 */
	public static void broadcastMessage(String message) {
		Bukkit.broadcastMessage(format(message, null, null));
	}

}
