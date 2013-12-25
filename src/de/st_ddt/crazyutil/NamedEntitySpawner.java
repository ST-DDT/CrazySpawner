package de.st_ddt.crazyutil;

import java.util.Map;
import java.util.TreeMap;

public interface NamedEntitySpawner extends Named, EntitySpawner
{

	public final static Map<String, NamedEntitySpawner> SPAWNERS = new TreeMap<String, NamedEntitySpawner>(String.CASE_INSENSITIVE_ORDER);
}
