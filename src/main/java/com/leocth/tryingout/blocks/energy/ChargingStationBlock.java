package com.leocth.tryingout.blocks.energy;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

/**
 * This is a Java file created by LeoC200 on 2019/8/1 in project TryingOut_1142
 * All sources are released publicly on GitHub under the MIT license.
 */
public class ChargingStationBlock extends Block {
	
	public ChargingStationBlock() {
		super(Block.Properties.from(Blocks.STONE));
		this.setRegistryName("tryingout:charging_station");
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
	    return new ChargingStationTE();
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (!worldIn.isRemote) {
			player.sendMessage(new TranslationTextComponent("misc.tryingout.wip"));
			TileEntity te = worldIn.getTileEntity(pos);
			if (te instanceof ChargingStationTE) {
				player.sendMessage(new StringTextComponent("charging station te found: only for debug pps!!! if u see this plz report!"));
			}
			return true;
		}
		return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
	}
}
