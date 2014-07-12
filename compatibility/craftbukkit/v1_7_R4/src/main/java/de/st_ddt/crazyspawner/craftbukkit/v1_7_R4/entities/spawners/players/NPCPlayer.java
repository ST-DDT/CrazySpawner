package de.st_ddt.crazyspawner.craftbukkit.v1_7_R4.entities.spawners.players;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.v1_7_R4.DamageSource;
import net.minecraft.server.v1_7_R4.EnchantmentManager;
import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.EntityLiving;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.EnumGamemode;
import net.minecraft.server.v1_7_R4.ItemStack;
import net.minecraft.server.v1_7_R4.MinecraftServer;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_7_R4.PlayerInteractManager;
import net.minecraft.server.v1_7_R4.WorldServer;
import net.minecraft.util.com.mojang.authlib.GameProfile;

import org.bukkit.craftbukkit.v1_7_R4.event.CraftEventFactory;

final class NPCPlayer extends EntityPlayer
{

	private final static Field living_g;
	private final static Field living_drops;
	static
	{
		final Class<? extends EntityLiving> living_clazz = EntityLiving.class;
		Field _g = null;
		Field _drops = null;
		try
		{
			_g = living_clazz.getDeclaredField("g");
			_g.setAccessible(true);
			_drops = living_clazz.getDeclaredField("drops");
			_drops.setAccessible(true);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			living_g = _g;
			living_drops = _drops;
		}
	}

	private ItemStack[] getLiving_g()
	{
		try
		{
			return (ItemStack[]) living_g.get(this);
		}
		catch (final Exception e)
		{
			throw new IllegalStateException("[CrazySpawner_1_7_R4] WARNING: Serious Bug detected, please report this!", e);
		}
	}

	private void setDrops(final List<org.bukkit.inventory.ItemStack> drops)
	{
		try
		{
			living_drops.set(this, drops);
		}
		catch (final Exception e)
		{
			throw new IllegalStateException("[CrazySpawner_1_7_R4] WARNING: Serious Bug detected, please report this!", e);
		}
	}

	@SuppressWarnings("unchecked")
	private List<org.bukkit.inventory.ItemStack> getDrops()
	{
		try
		{
			return (List<org.bukkit.inventory.ItemStack>) living_drops.get(this);
		}
		catch (final Exception e)
		{
			throw new IllegalStateException("[CrazySpawner_1_7_R4] WARNING: Serious Bug detected, please report this!", e);
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
		this.g = getLiving_g();
	}

	@Override
	// Copied from net.minecraft.server.v1_7_R4.EntityLiving.
	public void die(final DamageSource damagesource)
	{
		this.dead = true; // Crazy - Do not execute multiple times
		final Entity entity = damagesource.getEntity();
		final EntityLiving entityliving = this.aX();
		if (this.ba >= 0 && entityliving != null)
			entityliving.b(this, this.ba);
		if (entity != null)
			entity.a(this);
		this.aT = true;
		this.aW().g();
		if (!this.world.isStatic)
		{
			int i = 0;
			if (entity instanceof EntityHuman)
				i = EnchantmentManager.getBonusMonsterLootEnchantmentLevel((EntityLiving) entity);
			if (this.aG() && this.world.getGameRules().getBoolean("doMobLoot"))
			{
				// this.drops = ... => setDrops(...)
				// this.drops = new ArrayList<org.bukkit.inventory.ItemStack>();
				setDrops(new ArrayList<org.bukkit.inventory.ItemStack>()); // CraftBukkit - Setup drop capture
				this.dropDeathLoot(this.lastDamageByPlayerTime > 0, i);
				this.dropEquipment(this.lastDamageByPlayerTime > 0, i);
				if (this.lastDamageByPlayerTime > 0)
				{
					final int j = this.random.nextInt(200) - i;
					if (j < 5)
						this.getRareDrop(j <= 0 ? 1 : 0);
				}
				// CraftBukkit start - Call death event
				// this.drops => getDrops();
				// CraftEventFactory.callEntityDeathEvent(this, this.drops);
				CraftEventFactory.callEntityDeathEvent(this, getDrops());
				// this.drops = ... => setDrops(...)
				// this.drops = new ArrayList<org.bukkit.inventory.ItemStack>();
				setDrops(null);
			}
			else
				CraftEventFactory.callEntityDeathEvent(this);
			// CraftBukkit end
		}
		this.world.broadcastEntityEffect(this, (byte) 3);
	}

	// Required for Gravitation
	// Copied from net.minecraft.server.v1_7_R4.EntityLiving.h()
	@Override
	public void h()
	{
		super.h();
		if (!this.world.isStatic)
		{
			final int i = this.aZ();
			if (i > 0)
			{
				if (this.av <= 0)
					this.av = 20 * (30 - i);
				--this.av;
				if (this.av <= 0)
					this.p(i - 1);
			}
			for (int j = 0; j < 5; ++j)
			{
				final ItemStack itemstack = this.g[j];
				final ItemStack itemstack1 = this.getEquipment(j);
				if (!ItemStack.matches(itemstack1, itemstack))
				{
					((WorldServer) this.world).getTracker().a(this, (new PacketPlayOutEntityEquipment(this.getId(), j, itemstack1)));
					if (itemstack != null)
						// this.d => net.minecraft.server.v1_7_R3.EntityLiving.getAttributeMap()
						// this.d.a(itemstack.D());
						this.getAttributeMap().a(itemstack.D());
					if (itemstack1 != null)
						// this.d => net.minecraft.server.v1_7_R3.EntityLiving.getAttributeMap()
						// this.d.a(itemstack1.D());
						this.getAttributeMap().b(itemstack1.D());
					this.g[j] = itemstack1 == null ? null : itemstack1.cloneItemStack();
				}
			}
			if (this.ticksLived % 20 == 0)
				this.aW().g();
		}
		this.e();
		final double d0 = this.locX - this.lastX;
		final double d1 = this.locZ - this.lastZ;
		final float f = (float) (d0 * d0 + d1 * d1);
		float f1 = this.aM;
		float f2 = 0.0F;
		this.aV = this.aW;
		float f3 = 0.0F;
		if (f > 0.0025000002F)
		{
			f3 = 1.0F;
			f2 = (float) Math.sqrt(f) * 3.0F;
			// CraftBukkit - Math -> TrigMath
			f1 = (float) org.bukkit.craftbukkit.v1_7_R4.TrigMath.atan2(d1, d0) * 180.0F / 3.1415927F - 90.0F;
		}
		if (this.aD > 0.0F)
			f1 = this.yaw;
		if (!this.onGround)
			f3 = 0.0F;
		this.aW += (f3 - this.aW) * 0.3F;
		this.world.methodProfiler.a("headTurn");
		f2 = this.f(f1, f2);
		this.world.methodProfiler.b();
		this.world.methodProfiler.a("rangeChecks");
		while (this.yaw - this.lastYaw < -180.0F)
			this.lastYaw -= 360.0F;
		while (this.yaw - this.lastYaw >= 180.0F)
			this.lastYaw += 360.0F;
		while (this.aM - this.aN < -180.0F)
			this.aN -= 360.0F;
		while (this.aM - this.aN >= 180.0F)
			this.aN += 360.0F;
		while (this.pitch - this.lastPitch < -180.0F)
			this.lastPitch -= 360.0F;
		while (this.pitch - this.lastPitch >= 180.0F)
			this.lastPitch += 360.0F;
		while (this.aO - this.aP < -180.0F)
			this.aP -= 360.0F;
		while (this.aO - this.aP >= 180.0F)
			this.aP += 360.0F;
		this.world.methodProfiler.b();
		this.aX += f2;
	}
}
