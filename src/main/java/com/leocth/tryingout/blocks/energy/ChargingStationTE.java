package com.leocth.tryingout.blocks.energy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.leocth.tryingout.List;
import com.leocth.tryingout.energy.EnergyPool;
import com.leocth.tryingout.items.IChargable;

import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;

public class ChargingStationTE extends TileEntity implements ITickableTileEntity {
	private static final Logger LOGGER = LogManager.getLogger();
	private final EnergyPool pool;
	private final float current = 750.0f;
	private float amount;
	protected NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
	
	public ChargingStationTE() {
		this(Integer.MAX_VALUE);
	}
	public ChargingStationTE(float capacity) {
		super(List.CHARGING_STATION_TE_TYPE);
		this.pool = new EnergyPool(capacity);
	}
	
	public boolean feedItem(ItemStack stack) {
		if (items.get(0).isEmpty() && (stack.getItem() instanceof IChargable)) {
			items.set(0, stack);
			return true;
		}
		return false;
	}
	
	public ItemStack retrieveItem() {
		if (!items.get(0).isEmpty()) {
			ItemStack stack = items.get(0);
			items.set(0, ItemStack.EMPTY);
			return stack;
		}
		return ItemStack.EMPTY;
	}
	
	
	@Override
	public void tick() {
		ItemStack itemToBeCharged = items.get(0);
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
	
	public void read(CompoundNBT compound) {
		super.read(compound);
		this.amount = compound.getInt("amount");
	    this.items = NonNullList.withSize(this.items.size(), ItemStack.EMPTY);
	    ItemStackHelper.loadAllItems(compound, this.items);
	    //LOGGER.info("read nbt: {}",compound);
	    pool.deserializeNBT((CompoundNBT) compound.get("pool"));
	}

	public CompoundNBT write(CompoundNBT compound) {
	    super.write(compound);
	    compound.putFloat("amount", this.amount);
	    ItemStackHelper.saveAllItems(compound, this.items);
	    compound.put("pool", pool.serializeNBT());
	    //LOGGER.info("write nbt: {}",compound);
	    return compound;
	}
	
}
