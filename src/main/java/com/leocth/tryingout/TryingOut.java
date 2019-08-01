package com.leocth.tryingout;

import com.leocth.tryingout.blocks.TesterBlock;
import com.leocth.tryingout.blocks.energy.ChargingStationBlock;
import com.leocth.tryingout.blocks.energy.ChargingStationTE;
import com.leocth.tryingout.items.TaserItem;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a Java file created by LeoC200 on 2019/7/30 in project TryingOut_1142
 * All sources are released publicly on GitHub under the MIT license.
 */
@Mod("tryingout")
public class TryingOut
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public TryingOut() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // remember, this is called AFTER the registering events!!
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
    	
    	private static void registerItemBlocks(final RegistryEvent.Register<Item> e, Block... bs) {
    		for (Block b : bs) {
    			e.getRegistry().registerAll(new BlockItem(b, new Item.Properties()).setRegistryName(b.getRegistryName()));
    		}
    	}
    	
        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> e) {
            e.getRegistry().registerAll(new TaserItem());
            registerItemBlocks(e, List.TESTER, List.CHARGING_STATION);
            
        }
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> e) {
            e.getRegistry().registerAll(new TesterBlock(),
            							new ChargingStationBlock());
            
        }
        @SubscribeEvent
        public static void onTERegistry(final RegistryEvent.Register<TileEntityType<? extends TileEntity>> e) {
        	/* probing
        	for(Entry<ResourceLocation, TileEntityType<? extends TileEntity>> en : e.getRegistry().getEntries()) {
        		LOGGER.info(en.getKey() + ", " + en.getValue().toString());
        	}
        	*/
        	e.getRegistry().register(TileEntityType.Builder.<ChargingStationTE>create(ChargingStationTE::new, List.CHARGING_STATION).build(null).setRegistryName("tryingout:charging_station"));
        }
    }
}
