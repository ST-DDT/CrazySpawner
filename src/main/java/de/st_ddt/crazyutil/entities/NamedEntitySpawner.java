package de.st_ddt.crazyutil.entities;

import java.util.Map;
import java.util.TreeMap;

import org.bukkit.metadata.MetadataValue;

import de.st_ddt.crazyutil.Named;

public interface NamedEntitySpawner extends Named, EntitySpawner, MetadataValue
{

	public final static String METAHEADER = "CustomEntityMeta";
	public final static Map<String, NamedEntitySpawner> NAMEDENTITYSPAWNERS = new TreeMap<String, NamedEntitySpawner>(String.CASE_INSENSITIVE_ORDER);
}
