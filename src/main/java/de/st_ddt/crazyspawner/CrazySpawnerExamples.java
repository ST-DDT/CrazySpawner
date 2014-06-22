package de.st_ddt.crazyspawner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Art;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.Rotation;
import org.bukkit.block.BlockFace;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffectType;

import de.st_ddt.crazyspawner.entities.properties.EquipmentProperties;
import de.st_ddt.crazyspawner.entities.properties.PotionProterty;
import de.st_ddt.crazyspawner.entities.spawners.CustomizedParentedSpawner;
import de.st_ddt.crazyspawner.entities.spawners.RandomParentedSpawner;
import de.st_ddt.crazyspawner.entities.spawners.NamedParentedSpawner;
import de.st_ddt.crazyspawner.tasks.options.Thunder;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.Drop;
import de.st_ddt.crazyutil.entities.EntitySpawnerHelper;
import de.st_ddt.crazyutil.entities.NamedEntitySpawner;
import de.st_ddt.crazyutil.paramitrisable.EnumParamitrisable;

final class CrazySpawnerExamples
{

	public static void saveExampleFiles(final File dataFolder)
	{
		final File exampleFolder = new File(dataFolder, "example");
		exampleFolder.mkdirs();
		// Example - Entities (Properties)
		final YamlConfiguration allCustomEntityProperties = new YamlConfiguration();
		allCustomEntityProperties.options().header("CrazySpawner example EntityALL.yml\n" + "For more information visit\n" + "https://github.com/ST-DDT/Crazy/blob/master/CrazySpawner/docs/example/Entity.yml\n" + "Custom entities have to be defined inside config.yml");
		for (final EntityType type : EntitySpawnerHelper.getSpawnableEntityTypes())
		{
			final YamlConfiguration customEntityProperties = new YamlConfiguration();
			customEntityProperties.options().header("CrazySpawner example Entity" + type.name() + ".yml\n" + "For more information visit\n" + "https://github.com/ST-DDT/Crazy/blob/master/CrazySpawner/docs/example/Entity.yml\n" + "Custom entities have to be defined inside config.yml");
			final CustomizedParentedSpawner spawner = new CustomizedParentedSpawner(type);
			spawner.dummySave(customEntityProperties, "example" + type.name() + ".");
			spawner.dummySave(allCustomEntityProperties, "exampleALL.");
			customEntityProperties.set("example" + type.name() + ".type", type.name());
			saveExampleFile(exampleFolder, "Entity" + type.name(), customEntityProperties);
		}
		saveExampleFile(exampleFolder, "EntityALL", allCustomEntityProperties);
		// Example - EntityType (supported only)
		final YamlConfiguration entityTypes = new YamlConfiguration();
		entityTypes.set("exampleEntityType", EnumParamitrisable.getEnumNames(EntitySpawnerHelper.getSpawnableEntityTypes()));
		saveExampleFile(exampleFolder, "EntityType", entityTypes);
		// Example - CustomEntityNames
		final YamlConfiguration customTypes = new YamlConfiguration();
		customTypes.set("exampleCustomEntityNames", new ArrayList<String>(NamedEntitySpawner.NAMEDENTITYSPAWNERS.keySet()));
		saveExampleFile(exampleFolder, "CustomEntityNames", customTypes);
		// Example - Item
		final YamlConfiguration item = new YamlConfiguration();
		item.options().header("CrazySpawner example Item.yml\n" + "For more information visit\n" + "https://github.com/ST-DDT/Crazy/blob/master/CrazySpawner/docs/example/Item.yml\n" + "Items have to be defined inside config.yml (in the custom customEntity inventory slots)");
		item.set("exampleItem.type", "Material");
		item.set("exampleItem.damage", "short (0 (full) - 528 (broken, upper limit may differ (mostly below)))");
		item.set("exampleItem.amount", "int (1-64)");
		item.set("exampleItem.meta.==", "ItemMeta");
		item.set("exampleItem.meta.meta-type", "UNSPECIFIC");
		item.set("exampleItem.meta.display-name", "String");
		item.set("exampleItem.meta.lore", new String[] { "Line1", "Line2", "..." });
		item.set("exampleItem.meta.enchants.ENCHANTMENT1", "int (1-255)");
		item.set("exampleItem.meta.enchants.ENCHANTMENT2", "int (1-255)");
		item.set("exampleItem.meta.enchants.ENCHANTMENTx", "int (1-255)");
		saveExampleFile(exampleFolder, "Item", item);
		// Example - Enchantment
		final YamlConfiguration enchantments = new YamlConfiguration();
		final List<String> exampleEnchantments = new ArrayList<String>();
		for (final Enchantment enchantment : Enchantment.values())
			if (enchantment != null)
				exampleEnchantments.add(enchantment.getName());
		enchantments.set("exampleEnchantment", exampleEnchantments);
		saveExampleFile(exampleFolder, "Enchantment", enchantments);
		// Example - PotionEffectType
		final YamlConfiguration potionEffectTypes = new YamlConfiguration();
		for (final PotionEffectType potionEffectType : PotionEffectType.values())
			if (potionEffectType != null)
			{
				@SuppressWarnings("deprecation")
				final int id = potionEffectType.getId();
				potionEffectTypes.set("examplePotionEffectType." + potionEffectType.getName(), id);
			}
		saveExampleFile(exampleFolder, "PotionEffectType", potionEffectTypes);
		// Example - ItemMeta
		final YamlConfiguration meta = new YamlConfiguration();
		meta.set("exampleItemMeta.==", "ItemMeta");
		meta.set("exampleItemMeta.meta-type", "UNSPECIFIC");
		meta.set("exampleItemMeta.display-name", "String");
		meta.set("exampleItemMeta.lore", new String[] { "Line1", "Line2", "..." });
		meta.set("exampleItemMeta.enchants.ENCHANTMENT1", "int (1-255)");
		meta.set("exampleItemMeta.enchants.ENCHANTMENT2", "int (1-255)");
		meta.set("exampleItemMeta.enchants.ENCHANTMENTx", "int (1-255)");
		saveExampleFile(exampleFolder, "ItemMeta", meta);
		// Example - BookMeta
		final YamlConfiguration book = new YamlConfiguration();
		book.set("exampleBookMeta.==", "ItemMeta");
		book.set("exampleBookMeta.meta-type", "BOOK");
		book.set("exampleBookMeta.title", "String");
		book.set("exampleBookMeta.author", "String");
		book.set("exampleBookMeta.pages", new String[] { "Page1", "Page2", "..." });
		saveExampleFile(exampleFolder, "BookMeta", book);
		// Example - EnchantmentStorageMeta
		final YamlConfiguration enchantmentStorage = new YamlConfiguration();
		enchantmentStorage.set("exampleBookMeta.==", "ItemMeta");
		enchantmentStorage.set("exampleBookMeta.meta-type", "ENCHANTED");
		enchantmentStorage.set("exampleEnchantmentStorageMeta.stored-enchants.ENCHANTMENT1", "int (1-255)");
		enchantmentStorage.set("exampleEnchantmentStorageMeta.stored-enchants.ENCHANTMENT2", "int (1-255)");
		enchantmentStorage.set("exampleEnchantmentStorageMeta.stored-enchants.ENCHANTMENTx", "int (1-255)");
		saveExampleFile(exampleFolder, "EnchantmentStorageMeta", enchantmentStorage);
		// Example - FireworkMeta
		final YamlConfiguration firework = new YamlConfiguration();
		firework.set("exampleFireworkMeta.==", "ItemMeta");
		firework.set("exampleFireworkMeta.meta-type", "FIREWORK");
		final List<Map<String, Object>> effectsList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 2; i++)
		{
			final Map<String, Object> effectMap = new LinkedHashMap<String, Object>();
			effectMap.put("==", "Firework");
			effectMap.put("flicker", "boolean (true/false)");
			effectMap.put("trail", "boolean (true/false)");
			for (final String colorSection : new String[] { "colors", "fade-colors" })
			{
				final List<Map<String, Object>> colorList = new ArrayList<Map<String, Object>>();
				for (int j = 0; j < 2; j++)
				{
					final Map<String, Object> colorMap = new LinkedHashMap<String, Object>();
					colorMap.put("==", "Color");
					colorMap.put("RED", "int (0-255)");
					colorMap.put("GREEN", "int (0-255)");
					colorMap.put("BLUE", "int (0-255)");
					colorList.add(colorMap);
				}
				effectMap.put(colorSection, colorList);
			}
			effectMap.put("type", ChatHelper.listingString(" | ", EnumParamitrisable.getEnumNames(FireworkEffect.Type.values())));
			effectsList.add(effectMap);
		}
		firework.set("exampleFireworkMeta.firework-effects", effectsList);
		firework.set("exampleFireworkMeta.power", "int (0-127)");
		saveExampleFile(exampleFolder, "FireworkMeta", firework);
		// Example - MapMeta
		final YamlConfiguration map = new YamlConfiguration();
		map.set("exampleMapMeta.==", "ItemMeta");
		map.set("exampleMapMeta.meta-type", "MAP");
		map.set("exampleMapMeta.scaling", "boolean (true/false)");
		saveExampleFile(exampleFolder, "MapMeta", map);
		// Example - PotionMeta
		final YamlConfiguration potion = new YamlConfiguration();
		potion.set("examplePotionMeta.==", "ItemMeta");
		potion.set("examplePotionMeta.meta-type", "POTION");
		final List<Map<String, String>> potionList = new ArrayList<>();
		for (int i = 0; i < 3; i++)
		{
			final Map<String, String> potionMap = new LinkedHashMap<>();
			potionMap.put("==", "PotionEffect");
			potionMap.put("effect", "PotionEffectTypeId (int)");
			potionMap.put("duration", "ticks/int (1...)");
			potionMap.put("amplifier", "int (1...)");
			potionMap.put("ambient", "boolean (true/false)");
		}
		potion.set("examplePotionMeta.custom-effects", potionList);
		saveExampleFile(exampleFolder, "PotionMeta", potion);
		// Example - SkullMeta
		final YamlConfiguration skull = new YamlConfiguration();
		skull.set("exampleSkullMeta.==", "ItemMeta");
		skull.set("exampleSkullMeta.meta-type", "SKULL");
		skull.set("exampleSkullMeta.skull-owner", "String (0-16)");
		saveExampleFile(exampleFolder, "SkullMeta", skull);
		// Example - Enums
		saveExampleEnum(exampleFolder, Art.values(), "Art");
		saveExampleEnum(exampleFolder, BlockFace.values(), "BlockFace");
		saveExampleEnum(exampleFolder, DyeColor.values(), "DyeColor");
		saveExampleEnum(exampleFolder, Horse.Color.values(), "HorseColor");
		saveExampleEnum(exampleFolder, Horse.Style.values(), "HorseStyle");
		saveExampleEnum(exampleFolder, Horse.Variant.values(), "HorseVariant");
		saveExampleEnum(exampleFolder, Material.values(), "Material");
		saveExampleEnum(exampleFolder, Ocelot.Type.values(), "CatType");
		saveExampleEnum(exampleFolder, Rotation.values(), "Rotation");
		saveExampleEnum(exampleFolder, Skeleton.SkeletonType.values(), "SkeletonType");
		saveExampleEnum(exampleFolder, Thunder.values(), "Thunder");
		saveExampleEnum(exampleFolder, Villager.Profession.values(), "Profession");
	}

	protected static <A extends Enum<A>> void saveExampleEnum(final File exampleFolder, final A[] values, final String name)
	{
		final YamlConfiguration config = new YamlConfiguration();
		config.set("example" + name, EnumParamitrisable.getEnumNames(values).toArray());
		saveExampleFile(exampleFolder, name, config);
	}

	protected static void saveExampleFile(final File exampleFolder, final String name, final YamlConfiguration config)
	{
		try
		{
			config.save(new File(exampleFolder, name + ".yml"));
		}
		catch (final IOException e)
		{
			System.err.println("[CrazySpawner] Could not save example " + name + ".yml.");
			System.err.println(e.getMessage());
		}
	}

	public static void saveExampleEntities(final CrazySpawner plugin, final String previousVersion)
	{
		final ConsoleCommandSender console = Bukkit.getConsoleSender();
		// DefaultCreatures
		// - Spider_Skeleton
		{
			final NamedParentedSpawner spiderSkeleton = new NamedParentedSpawner(new CustomizedParentedSpawner(EntityType.SPIDER, console, "passenger:SKELETON"), "SPIDER_SKELETON");
			plugin.addCustomEntity(spiderSkeleton);
		}
		// - Zombie_Skeleton
		{
			final NamedParentedSpawner spiderZombie = new NamedParentedSpawner(new CustomizedParentedSpawner(EntityType.SPIDER, console, "passenger:ZOMBIE"), "Spider_Zombie");
			plugin.addCustomEntity(spiderZombie);
		}
		// - Diamond_Zombie
		final NamedParentedSpawner diamondZombie;
		{
			diamondZombie = new NamedParentedSpawner(new CustomizedParentedSpawner(EntityType.ZOMBIE, console, "boots:DIAMOND_BOOTS", "bootsdropchance:0.01", "leggings:DIAMOND_LEGGINGS", "leggingsdropchance:0.01", "chestplate:DIAMOND_CHESTPLATE", "chestplatedropchance:0.01", "helmet:DIAMOND_HELMET", "helmetdropchance:0.01", "iteminhand:DIAMOND_SWORD", "iteminhanddropchance:0.01"), "Diamond_Zombie");
			plugin.addCustomEntity(diamondZombie);
		}
		// - Giant
		{
			final NamedParentedSpawner giant = new NamedParentedSpawner(new CustomizedParentedSpawner(EntityType.GIANT), "Some_Giant");
			plugin.addCustomEntity(giant);
			// - Healthy_Giant
			final NamedParentedSpawner healthyGiant = new NamedParentedSpawner(new CustomizedParentedSpawner(giant, console, "maxhealth:200"), "Healthy_Giant");
			plugin.addCustomEntity(healthyGiant);
		}
		// - Spider_Diamond_Zombie (Zombie as Passenger)
		{
			final NamedParentedSpawner spiderDiamondZombie = new NamedParentedSpawner(new CustomizedParentedSpawner(EntityType.SPIDER, console, "passenger:Diamond_Zombie"), "Spider_Diamond_Zombie");
			plugin.addCustomEntity(spiderDiamondZombie);
		}
		// - Spider_Diamond_Zombie2 (Spider as Vehicle)
		final NamedParentedSpawner spiderDiamondZombie2;
		{
			spiderDiamondZombie2 = new NamedParentedSpawner(new CustomizedParentedSpawner(diamondZombie, console, "vehicle:SPIDER"), "Spider_Diamond_Zombie2");
			plugin.addCustomEntity(spiderDiamondZombie2);
		}
		// - Speedy_Baby_Zombie
		final CustomizedParentedSpawner speedyZombie;
		{
			final ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
			boots.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 1);
			final LeatherArmorMeta meta = (LeatherArmorMeta) boots.getItemMeta();
			meta.setColor(Color.RED);
			boots.setItemMeta(meta);
			speedyZombie = new CustomizedParentedSpawner(EntityType.ZOMBIE, console, "baby:true");
			speedyZombie.addEntityProperty(new EquipmentProperties(boots, 0.5F, null, 0, null, 0, null, 0, null, 0, null, null));
			speedyZombie.addEntityProperty(new PotionProterty(new PotionEffectType[] { PotionEffectType.SPEED }, new int[] { 5 }));
			final NamedParentedSpawner namedSpeedyZombie = new NamedParentedSpawner(speedyZombie, "Speedy_Baby_Zombie");
			plugin.addCustomEntity(namedSpeedyZombie);
		}
		// - Healthy_Diamond_Zombie
		final NamedParentedSpawner namedHealthyDiamondZombie;
		{
			final ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
			final ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
			final ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
			chestplate.setDurability((short) 3);
			chestplate.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 5);
			chestplate.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 5);
			chestplate.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
			chestplate.addUnsafeEnchantment(Enchantment.THORNS, 3);
			final ItemMeta meta = chestplate.getItemMeta();
			meta.setDisplayName("Holy Chestplate of the Goddes");
			final List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.RED + "Blessed by the goddess kiss");
			lore.add("Manufactured by the best dwarfs known");
			meta.setLore(lore);
			chestplate.setItemMeta(meta);
			final ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
			final ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
			final List<Drop> drops = new ArrayList<Drop>();
			drops.add(new Drop(new ItemStack(Material.DIAMOND, 2), 0.5F));
			drops.add(new Drop(new ItemStack(Material.DIAMOND, 3), 0.5F));
			drops.add(new Drop(new ItemStack(Material.GOLD_INGOT, 2), 0.5F));
			drops.add(new Drop(new ItemStack(Material.GOLD_INGOT, 3), 0.5F));
			drops.add(new Drop(new ItemStack(Material.EMERALD, 2), 0.5F));
			drops.add(new Drop(boots, 1F));
			drops.add(new Drop(leggings, 1F));
			drops.add(new Drop(chestplate, 1F));
			drops.add(new Drop(helmet, 1F));
			drops.add(new Drop(sword, 1F));
			final CustomizedParentedSpawner healthyDiamondZombie = new CustomizedParentedSpawner(EntityType.ZOMBIE, console, "customName:&3Diamond_Zombie", "showNameAboveHead:true", "showHealth:true", "maxHealth:100", "minDamage:3", "maxDamage:7", "minXP:10", "maxXP:20");
			healthyDiamondZombie.addEntityProperty(new EquipmentProperties(boots, 1F, leggings, 1F, chestplate, 1F, helmet, 1F, sword, 1F, drops, false));
			namedHealthyDiamondZombie = new NamedParentedSpawner(healthyDiamondZombie, "Healthy_Diamond_Zombie");
			plugin.addCustomEntity(namedHealthyDiamondZombie);
		}
		// - Random_Special_Zombie
		{
			final RandomParentedSpawner randomSpecialZombie = new RandomParentedSpawner(diamondZombie, spiderDiamondZombie2, speedyZombie, namedHealthyDiamondZombie);
			final NamedParentedSpawner namedRandomSpecialZombie = new NamedParentedSpawner(randomSpecialZombie, "Random_Special_Zombie");
			plugin.addCustomEntity(namedRandomSpecialZombie);
		}
		plugin.saveCustomEntities();
	}
}
