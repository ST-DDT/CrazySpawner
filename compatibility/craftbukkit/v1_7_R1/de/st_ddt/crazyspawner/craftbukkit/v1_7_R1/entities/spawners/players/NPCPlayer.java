package de.st_ddt.crazyspawner.craftbukkit.v1_7_R1.entities.spawners.players;

import java.lang.reflect.Field;

import net.minecraft.server.v1_7_R1.DamageSource;
import net.minecraft.server.v1_7_R1.EnchantmentManager;
import net.minecraft.server.v1_7_R1.Entity;
import net.minecraft.server.v1_7_R1.EntityHuman;
import net.minecraft.server.v1_7_R1.EntityLiving;
import net.minecraft.server.v1_7_R1.EntityPlayer;
import net.minecraft.server.v1_7_R1.EnumGamemode;
import net.minecraft.server.v1_7_R1.ItemStack;
import net.minecraft.server.v1_7_R1.MinecraftServer;
import net.minecraft.server.v1_7_R1.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_7_R1.PlayerInteractManager;
import net.minecraft.server.v1_7_R1.WorldServer;
import net.minecraft.util.com.mojang.authlib.GameProfile;

import org.bukkit.craftbukkit.v1_7_R1.event.CraftEventFactory;

final class NPCPlayer extends EntityPlayer
{

	private final static Field living_g;
	static
	{
		final Class<? extends EntityLiving> living_clazz = EntityLiving.class;
		Field _g = null;
		try
		{
			_g = living_clazz.getDeclaredField("g");
			_g.setAccessible(true);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			living_g = _g;
		}
	}

	private static ItemStack[] getLiving_g(final EntityLiving living)
	{
		try
		{
			return (ItemStack[]) living_g.get(living);
		}
		catch (final Exception e)
		{
			System.err.println("[CrazySpawner_1_7_R1] WARNING: Serious Bug detected, please report this!");
			e.printStackTrace();
			return null;
		}
	}

	private final ItemStack[] g;

	public NPCPlayer(final MinecraftServer minecraftserver, final WorldServer worldserver, final GameProfile gameprofile, final PlayerInteractManager playerinteractmanager)
	{
		super(minecraftserver, worldserver, gameprofile, playerinteractmanager);
		// This player shouldn't destroy anything.
		playerinteractmanager.b(EnumGamemode.ADVENTURE);
		// Do not block sleeping
		fauxSleeping = true;
		playerConnection = new NPCPlayerConnection(minecraftserver, this);
		this.g = getLiving_g(this);
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

	// Required for Gravitation
	@Override
	public void h()
	{
		super.h();
		if (!this.world.isStatic)
		{
			final int i = aZ();
			if (i > 0)
			{
				if (this.aw <= 0)
					this.aw = (20 * (30 - i));
				this.aw -= 1;
				if (this.aw <= 0)
					p(i - 1);
			}
			for (int j = 0; j < 5; j++)
			{
				final ItemStack itemstack = this.g[j];
				final ItemStack itemstack1 = getEquipment(j);
				if (!ItemStack.matches(itemstack1, itemstack))
				{
					((WorldServer) this.world).getTracker().a(this, new PacketPlayOutEntityEquipment(getId(), j, itemstack1));
					if (itemstack != null)
						// this.d = EntityLiving.bc();
						// this.d.a(itemstack.D());
						bc().a(itemstack.D());
					if (itemstack1 != null)
						// this.d = EntityLiving.bc();
						// this.d.a(itemstack.D());
						bc().b(itemstack1.D());
					this.g[j] = (itemstack1 == null ? null : itemstack1.cloneItemStack());
				}
			}
		}
		e();
		final double d0 = this.locX - this.lastX;
		final double d1 = this.locZ - this.lastZ;
		final float f = (float) (d0 * d0 + d1 * d1);
		float f1 = this.aN;
		float f2 = 0.0F;
		this.aW = this.aX;
		float f3 = 0.0F;
		if (f > 0.0025F)
		{
			f3 = 1.0F;
			f2 = (float) Math.sqrt(f) * 3.0F;
			f1 = (float) org.bukkit.craftbukkit.v1_7_R1.TrigMath.atan2(d1, d0) * 180.0F / 3.141593F - 90.0F;
		}
		if (this.aE > 0.0F)
			f1 = this.yaw;
		if (!this.onGround)
			f3 = 0.0F;
		this.aX += (f3 - this.aX) * 0.3F;
		this.world.methodProfiler.a("headTurn");
		f2 = f(f1, f2);
		this.world.methodProfiler.b();
		this.world.methodProfiler.a("rangeChecks");
		while (this.yaw - this.lastYaw < -180.0F)
			this.lastYaw -= 360.0F;
		while (this.yaw - this.lastYaw >= 180.0F)
			this.lastYaw += 360.0F;
		while (this.aN - this.aO < -180.0F)
			this.aO -= 360.0F;
		while (this.aN - this.aO >= 180.0F)
			this.aO += 360.0F;
		while (this.pitch - this.lastPitch < -180.0F)
			this.lastPitch -= 360.0F;
		while (this.pitch - this.lastPitch >= 180.0F)
			this.lastPitch += 360.0F;
		while (this.aP - this.aQ < -180.0F)
			this.aQ -= 360.0F;
		while (this.aP - this.aQ >= 180.0F)
			this.aQ += 360.0F;
		this.world.methodProfiler.b();
		this.aY += f2;
	}
}
