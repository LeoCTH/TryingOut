package com.leocth.tryingout.blocks.energy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.leocth.tryingout.List;
import com.leocth.tryingout.energy.EnergyPool;
import com.leocth.tryingout.items.IChargable;

import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ChargingStationTE extends TileEntity implements ITickableTileEntity {
	private static final Logger LOGGER = LogManager.getLogger();
	private final EnergyPool pool;
	private final float current = 750.0f;
	private float amount;
	public ItemStackHandler items = new ItemStackHandler(1);
	
	public ChargingStationTE() {
		this(Integer.MAX_VALUE);
	}
	public ChargingStationTE(float capacity) {
		super(List.CHARGING_STATION_TE_TYPE);
		this.pool = new EnergyPool(capacity);
	}
	
	public IItemHandler getItems() {
		return items;
	}
	
	// replace with direct manipulation on items.insertItem/extractItem
	@Deprecated
	public boolean feedItem(ItemStack stack) {
		if (items.getStackInSlot(0).isEmpty() && (stack.getItem() instanceof IChargable)) {
			items.setStackInSlot(0, stack);
			return true;
		}
		return false;
	}
	
	@Deprecated
	public ItemStack retrieveItem() {
		if (!items.getStackInSlot(0).isEmpty()) {
			ItemStack stack = items.getStackInSlot(0);
			items.setStackInSlot(0, ItemStack.EMPTY);
			return stack;
		}
		return ItemStack.EMPTY;
	}
	
	
	@Override
	public void tick() {
		ItemStack itemToBeCharged = items.getStackInSlot(0);
		if (!this.world.isRemote) {
			if (!itemToBeCharged.isEmpty() && (itemToBeCharged.getItem() instanceof IChargable)) {
				//LOGGER.info("prev | item damage: {} | pool amount: {}", itemToBeCharged.getDamage(), pool.amount);
				if (itemToBeCharged.getDamage() - current > 0) {
					itemToBeCharged.setDamage((int) (itemToBeCharged.getDamage() - current));
				} else {
					itemToBeCharged.setDamage(0);
				}
				this.pool.transfer(((IChargable) itemToBeCharged.getItem()).getPool(), current);
				//LOGGER.info("post | item damage: {} | pool amount: {}", itemToBeCharged.getDamage(), pool.amount);
				this.markDirty();
			}
		}
	}
	
	@Override
	public final CompoundNBT getUpdateTag() {
		return write(new CompoundNBT());
	}
	
	public void read(CompoundNBT compound) {
		super.read(compound);
		this.amount = compound.getFloat("amount");
	    this.items = new ItemStackHandler(1);
	    this.items.deserializeNBT(compound);
	    LOGGER.info("read nbt: {}",compound);
	    pool.deserializeNBT((CompoundNBT) compound.get("pool"));
	}

	public CompoundNBT write(CompoundNBT compound) {
	    super.write(compound);
	    compound.putFloat("amount", this.amount);
	    compound.merge(items.serializeNBT());
	    compound.put("pool", pool.serializeNBT());
	    LOGGER.info("write nbt: {}",compound);
	    return compound;
	}
	
	public void readPacketNBT(CompoundNBT compound) {
		LOGGER.info(compound);
		items = new ItemStackHandler(1);
		items.deserializeNBT(compound);
		this.amount = compound.getFloat("amount");
		pool.deserializeNBT((CompoundNBT) compound.get("pool"));
	}

	public void writePacketNBT(CompoundNBT compound) {
		LOGGER.info(compound);
		compound.merge(items.serializeNBT());
		compound.putFloat("amount", this.amount);
		compound.put("pool", pool.serializeNBT());
	}
	
	@Override
	public final SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT tag = new CompoundNBT();
		writePacketNBT(tag);
		return new SUpdateTileEntityPacket(pos, -999, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
		super.onDataPacket(net, packet);
		readPacketNBT(packet.getNbtCompound());
	}
}
