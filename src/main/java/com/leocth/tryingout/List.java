package com.leocth.tryingout;

import com.leocth.tryingout.blocks.energy.ChargingStationTE;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

/**
 * This is a Java file created by LeoC200 on 2019/7/30 in project TryingOut_1142
 * All sources are released publicly on GitHub under the MIT license.
 */
@ObjectHolder("tryingout")
public class List {
    public static final Block TESTER = null;
    public static final Block CHARGING_STATION = null;
    
    public static final Item TASER = null;
    
    @ObjectHolder("charging_station")
    public static final TileEntityType<ChargingStationTE> CHARGING_STATION_TE_TYPE = null;
}
