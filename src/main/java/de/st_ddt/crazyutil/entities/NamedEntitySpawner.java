package de.st_ddt.crazyutil.entities;

import java.util.Map;
import java.util.TreeMap;

import de.st_ddt.crazyutil.Named;

public interface NamedEntitySpawner extends Named, EntitySpawner
{

	public final static Map<String, NamedEntitySpawner> NAMEDENTITYSPAWNERS = new TreeMap<String, NamedEntitySpawner>(String.CASE_INSENSITIVE_ORDER);
}
