package com.leocth.tryingout.energy;

/**
 * This is a Java file created by LeoC200 on 2019/8/2 in project TryingOut_1142
 * All sources are released publicly on GitHub under the MIT license.
 */

import com.leocth.tryingout.List;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class EnergyPool implements INBTSerializable<CompoundNBT> {
	public float capacity;
	public float amount;
	private boolean isInfinite = false;
	
	public EnergyPool() {
		this(Integer.MAX_VALUE);
	}
	
	public EnergyPool(float capacity) {
		this.capacity = capacity;
		this.amount = capacity;
		if (capacity >= Integer.MAX_VALUE) {
			this.isInfinite = true;
		}
	}
	
	public void set(float amount) {
		this.amount = amount;
		if (this.amount > this.capacity) {
			this.amount = this.capacity;
		}
	}
	public boolean rawAdd(float amount) {
		if (this.isInfinite) {
			List.LOGGER.warn("ж▓(бузебу; You're attempting to charge a infinitely full energy pool!! Contact the author if you see this message!!!");
			return true;
		} else {
			if (this.amount >= this.capacity) {
				this.amount = this.capacity;
				return false;
			}
			if (this.amount + amount >= this.capacity) {
				this.amount = this.capacity;
				return true;
			}
			this.amount += amount;
			return true;
		}
	}
	
	public boolean rawSubtract(float amount) {
		if (this.isInfinite) {
			return true;
		} else {
			if (this.amount <= 0) {
				this.amount = 0;
				return false;
			}
			if (this.amount - amount <= 0) {
				this.amount = 0;
				return true;
			}
			this.amount -= amount;
			return true;
		}
	}
	
	public boolean transfer(EnergyPool target, float amount) {
		boolean step1 = this.rawSubtract(amount);
		boolean step2 = target.rawAdd(amount);
		return (step1 && step2);
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putFloat("capacity", capacity);
		nbt.putFloat("amount", amount);
		nbt.putBoolean("isInfinite", isInfinite);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		if (nbt == null)
			return;
		boolean isinf = nbt.getBoolean("isInfinite");
		if (isinf) {
			this.capacity = Integer.MAX_VALUE;
			this.amount = Integer.MAX_VALUE;
		} else {
			this.capacity = nbt.getFloat("capacity");
			this.amount = nbt.getFloat("amount");
		}
		this.isInfinite = isinf;
	}
}
