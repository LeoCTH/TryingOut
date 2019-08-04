package com.leocth.tryingout.blocks;

/**
 * This is a Java file created by LeoC200 on 2019/7/30 in project TryingOut_1142
 * All sources are released publicly on GitHub under the MIT license.
 */

import net.minecraft.block.*;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.*;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

import com.leocth.tryingout.misc.FontUtils;

import java.util.List;

public class TesterBlock extends DirectionalBlock {
    public TesterBlock() {
        super(Block.Properties.from(Blocks.STONE));
        this.setRegistryName("tryingout:tester");
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getNearestLookingDirection().getOpposite());
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("block.tryingout.tester.tooltip"));
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote) {
            ITextComponent rclick1 = new TranslationTextComponent("block.tryingout.tester.rclick1");
            ITextComponent rclick2 = new TranslationTextComponent("block.tryingout.tester.rclick2");
            player.sendMessage(rclick1.appendSibling(FontUtils.makeWeblink(rclick2, null)));
            return true;
        }
        return true;
    }
}
