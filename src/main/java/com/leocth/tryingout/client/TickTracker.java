package com.leocth.tryingout.client;

/**
 * This is a Java file created by LeoC200 on 2019/7/30 in project TryingOut_1142
 * All sources are released publicly on GitHub under the MIT license.
 */

import com.leocth.tryingout.TryingOut;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

// Most of the following code comes from Vazkii's Botania.
// A lot of details are removed because they are not related to this mod.
// More information on github.com/Vazkii/Botania
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = TryingOut.MODID)
public final class TickTracker {

	private TickTracker() {}

	public static int ticksInGame = 0;
	public static float partialTicks = 0;
	public static float delta = 0;
	public static float total = 0;
	
	private static void calcDelta() {
		float oldTotal = total;
		total = ticksInGame + partialTicks;
		delta = total - oldTotal;
	}

	@SubscribeEvent
	public static void renderTick(RenderTickEvent event) {
		if(event.phase == Phase.START)
			partialTicks = event.renderTickTime;
		else {
			calcDelta();
		}
	}

	@SubscribeEvent
	public static void clientTickEnd(ClientTickEvent event) {
		if (event.phase == Phase.END) {
			Screen gui = Minecraft.getInstance().currentScreen;
			if (gui == null || !gui.isPauseScreen()) {
				ticksInGame++;
				partialTicks = 0;
			}
			calcDelta();
		}
	}

}
