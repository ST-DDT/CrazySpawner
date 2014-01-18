package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;
import java.util.Set;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import de.st_ddt.crazyutil.ConfigurationSaveable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;

public interface EntityPropertyInterface extends ConfigurationSaveable
{

	/**
	 * All registered entity properties.<br>
	 * New {@link EntityPropertyInterface} should be added using {@link EntityPropertyHelper#registerEntityProperty(Class)}
	 */
	@SuppressWarnings("unchecked")
	public final static Set<Class<? extends EntityPropertyInterface>>[] ENTITYPROPERTIES = new Set[EntityType.values().length];

	/**
	 * Checks whether this {@link EntityPropertyInterface} is applicable for the given {@link Entity}'s {@link Class}.
	 * 
	 * @param clazz
	 *            The {@link Entity}'s {@link Class} that should be checked.
	 * @return True, if this {@link EntityPropertyInterface} is applicable to the given {@link Entity}'s {@link Class}
	 */
	public boolean isApplicable(Class<? extends Entity> clazz);

	/**
	 * Apply this {@link EntityPropertyInterface} to the given {@link Entity};
	 * 
	 * @param entity
	 *            The {@link Entity} that this property should be applied to.
	 */
	public void apply(Entity entity);

	/**
	 * Shows information about this {@link EntityPropertyInterface} to the given {@link CommandSender}
	 * 
	 * @param target
	 *            The target {@link CommandSender} that should recieve the information.
	 */
	public void show(CommandSender target);

	/**
	 * Adds the command params that can be used to utilized the special Constructor.
	 * 
	 * @param params
	 *            The parameter {@link Map} where the params should be added to.
	 * @param target
	 *            The {@link CommandSender} who tries to get the command params.
	 */
	public void getCommandParams(Map<String, ? super TabbedParamitrisable> params, CommandSender target);

	@Override
	public void save(ConfigurationSection config, String path);

	public void dummySave(ConfigurationSection config, String path);

	/**
	 * Checks whether this {@link EntityPropertyInterface} matches the default one.<br>
	 * (Created using the default Constructor)
	 * 
	 * @return True, if this {@link EntityPropertyInterface} matches the default one.<br>
	 *         False, otherwise.
	 */
	public boolean equalsDefault();
}
