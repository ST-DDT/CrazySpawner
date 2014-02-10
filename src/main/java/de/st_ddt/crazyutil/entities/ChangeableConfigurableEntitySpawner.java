package de.st_ddt.crazyutil.entities;

import java.util.Map;

import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;

public interface ChangeableConfigurableEntitySpawner extends ConfigurableEntitySpawner
{

	public ChangeableConfigurableEntitySpawner change(Map<String, ? extends TabbedParamitrisable> params);
}
