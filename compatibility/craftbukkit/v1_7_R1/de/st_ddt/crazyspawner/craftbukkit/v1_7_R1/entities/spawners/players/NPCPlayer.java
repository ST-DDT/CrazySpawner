package de.st_ddt.crazyspawner.craftbukkit.v1_7_R1.entities.spawners.players;

import net.minecraft.server.v1_7_R1.DamageSource;
import net.minecraft.server.v1_7_R1.EnchantmentManager;
import net.minecraft.server.v1_7_R1.Entity;
import net.minecraft.server.v1_7_R1.EntityHuman;
import net.minecraft.server.v1_7_R1.EntityLiving;
import net.minecraft.server.v1_7_R1.EntityPlayer;
import net.minecraft.server.v1_7_R1.EnumGamemode;
import net.minecraft.server.v1_7_R1.MinecraftServer;
import net.minecraft.server.v1_7_R1.PlayerInteractManager;
import net.minecraft.server.v1_7_R1.WorldServer;
import net.minecraft.util.com.mojang.authlib.GameProfile;

import org.bukkit.craftbukkit.v1_7_R1.event.CraftEventFactory;

public class NPCPlayer extends EntityPlayer
{

	public NPCPlayer(final MinecraftServer minecraftserver, final WorldServer worldserver, final GameProfile gameprofile, final PlayerInteractManager playerinteractmanager)
	{
		super(minecraftserver, worldserver, gameprofile, playerinteractmanager);
		// This player shouldn't destroy anything.
		playerinteractmanager.b(EnumGamemode.ADVENTURE);
		// Do not block sleeping
		fauxSleeping = true;
		playerConnection = new NPCPlayerConnection(minecraftserver, this);
	}

	@Override
	public void die(final DamageSource damagesource)
	{
		// Do not execute multiple times
		this.dead = true;
		// Copied from LivingEntity
		final Entity entity = damagesource.getEntity();
		final EntityLiving entityliving = aX();
		if ((this.bb >= 0) && (entityliving != null))
			entityliving.b(this, this.bb);
		if (entity != null)
			entity.a(this);
		this.aU = true;
		if (!this.world.isStatic)
		{
			int i = 0;
			if ((entity instanceof EntityHuman))
				i = EnchantmentManager.getBonusMonsterLootEnchantmentLevel((EntityLiving) entity);
			if ((aG()) && (this.world.getGameRules().getBoolean("doMobLoot")))
			{
				dropDeathLoot(this.lastDamageByPlayerTime > 0, i);
				dropEquipment(this.lastDamageByPlayerTime > 0, i);
			}
			else
				CraftEventFactory.callEntityDeathEvent(this);
		}
		this.world.broadcastEntityEffect(this, (byte) 3);
	}
}
