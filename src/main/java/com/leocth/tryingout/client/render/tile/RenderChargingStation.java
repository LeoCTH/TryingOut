package com.leocth.tryingout.client.render.tile;

/**
 * This is a Java file created by LeoC200 on 2019/8/3 in project TryingOut_1142
 * All sources are released publicly on GitHub under the MIT license.
 */

import javax.annotation.Nonnull;

import com.leocth.tryingout.blocks.energy.ChargingStationTE;
import com.leocth.tryingout.client.TickTracker;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;

/// Thx Vazkii for inspiring this code
@SuppressWarnings("deprecation")
public class RenderChargingStation extends TileEntityRenderer<ChargingStationTE> {
	
	@Override
	public void render(@Nonnull ChargingStationTE te, double x, double y, double z, float partticks, int digProgress) {
		GlStateManager.pushMatrix();
		GlStateManager.color4f(1F, 1F, 1F, 1F);
		GlStateManager.translated(x, y, z);
		//List.LOGGER.info("x, y, z: {} {} {}", x, y, z);
		
		@SuppressWarnings("unused")
		int items = 0;
		for (int i = 0; i < te.getItems().getSlots(); i++) {
			//List.LOGGER.info("item id: {}", te.getItems().getStackInSlot(i).getItem().getRegistryName());
			if (te.items.getStackInSlot(i).isEmpty()) {
				break;
			} else {
				items++;
			}
		}
		//List.LOGGER.info("te.items.size = {}, items = {}, avg angle: {}", te.getItems().getSlots(), items, 360f / items);
		//List.LOGGER.info("Expected items = {}", te.getItems().getStackInSlot(0));
		
		double time = TickTracker.ticksInGame + partticks;
		
		Minecraft.getInstance().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.pushMatrix();
		GlStateManager.translatef(0.5f, 0.4f, 0.5f);
		GlStateManager.rotated(time * 1.25, 0, 1, 0);
		GlStateManager.scalef(1.25f, 1.25f, 1.25f);
		
		/* srry i dont need fancy rotation!!!
		GlStateManager.translatef(1.125F, 0F, 0.25F);
		GlStateManager.rotatef(90F, 0F, 0F, 0F);
		GlStateManager.translated(0D, 0.075 * Math.sin((time + i * 10) / 5D), 0F);
		*/
		
		ItemStack stack = te.getItems().getStackInSlot(0);
		Minecraft mc = Minecraft.getInstance();
		if(!stack.isEmpty()) {
			//List.LOGGER.info("attempting to render {}", stack.getItem().getRegistryName());
			mc.getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.GROUND);
		}
		GlStateManager.popMatrix();
		GlStateManager.popMatrix();
	}
}
