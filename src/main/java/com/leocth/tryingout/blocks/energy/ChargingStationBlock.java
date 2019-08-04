package com.leocth.tryingout.blocks.energy;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
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
	public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("block.tryingout.charging_station.tooltip"));
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
			TileEntity te = worldIn.getTileEntity(pos);
			if (te instanceof ChargingStationTE) {
				ItemStack stack = player.getHeldItem(handIn);
				if (!stack.isEmpty()) {
					if (((ChargingStationTE) te).feedItem(stack)) {
						player.setHeldItem(handIn, ItemStack.EMPTY);
						return true;
					} else {
						return false;
					}
				} else {
					player.setHeldItem(handIn, ((ChargingStationTE) te).retrieveItem());
					return true;
				}
			}
		}
		return true;
	}
	
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
	    return Block.makeCuboidShape(0, 0, 0, 16, 7, 16);
	}
	   
}
