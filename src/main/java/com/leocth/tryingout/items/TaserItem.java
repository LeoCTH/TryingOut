package com.leocth.tryingout.items;

import com.leocth.tryingout.misc.TaserDamageSource;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.List;

/**
 * This is a Java file created by LeoC200 on 2019/7/31 in project TryingOut_1142
 * All sources are released publicly on GitHub under the MIT license.
 */
public class TaserItem extends Item {
    private static final Logger LOGGER = LogManager.getLogger();
    public TaserItem() {
        super(new Item.Properties().maxDamage(137));
        this.setRegistryName("tryingout:taser");
    }

    /**
     *  Some tips for future self to reminder:
     *  @var look is just the look vector (preferably normalized) of the player.
     *  @var start is the head position + look.
     *  @var end reaches to the end of the range
     *  @var aabb is a 3 * 3 * dis bounding box.
     *  It works by searching entities that are within the AABB to recieve a damage, which I will call it "actual" damage.
     *  The basic principle of determining the value of the actual damage is that *more entities means less damage each*.
     *  The max damage (i.e. the max an entity can recieve) is hardcoded (currently) and set to 25.0 half-hearts (12.5 full hearts)
     *  The min damage is also hardcoded and set to 7.0 half-hearts. (3.5 full hearts)
     *  The "density" of the area is determined by dividing the amount of entities by the value of the"entity cramming" game rule.
     *  (which has a default value of 24)
     *  Then, the actual damage is *interpolated* using
     *  @see net.minecraft.util.math.MathHelper#lerp
     *  which is sadly composed of Srg names waiting to be named.
     *  But it is equivalent to the lambda (t, a, b) -> t*(b-a)+a .
     *  But anyway, then the taser releases the damage to the entities, and reduces durability by the number of entities, or 1 if no entities was hurt.
     *
     *  TODO:
     *  Custom GUIs
     *  TEISR (idk, battery display?)
     *  BETTER 3D MODEL!!!
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {

        // P.S. there are a lot of individual variables to probe here.
        // take it easy

        if (!worldIn.isRemote) {
            ItemStack itemstack = playerIn.getHeldItem(handIn);
            if (itemstack.getDamage() < itemstack.getMaxDamage() - 1) {

                double dis = playerIn.getAttribute(PlayerEntity.REACH_DISTANCE).getValue() - 0.5;

                Vec3d look = playerIn.getLook(1.0f);
                Vec3d start = playerIn.getEyePosition(1.0f).add(look);
                Vec3d end = start.add(look.x * dis, look.y * dis, look.z * dis);
                AxisAlignedBB aabb = new AxisAlignedBB(start, end).offset(-1.0, -1.0, -1.0).expand(2, 2, 2);

                /*
                LOGGER.info(look);
                LOGGER.info(start);
                LOGGER.info(end);
                LOGGER.info(aabb);
                */

                List<Entity> entities = worldIn.getEntitiesWithinAABB((EntityType<?>) null, aabb, Entity::isLiving);
                entities.removeIf(entity -> entity == playerIn); // DONT KILL THE USER!!!

                double minDamage = 7.0;
                double maxDamage = 25.0;
                int amount = entities.size();
                int maxEntities = worldIn.getGameRules().get("maxEntityCramming").getInt();
                double density = (amount > maxEntities) ? 1.0 : ((float) amount / (float) maxEntities);
                // NOTE Minecraft lerp takes (t, a, b) instead of (a, b, t)
                double actualDamage = MathHelper.floor(MathHelper.lerp(density, maxDamage, minDamage));

                /*
                LOGGER.info(amount);
                LOGGER.info(maxEntities);
                LOGGER.info(density);
                LOGGER.info(actualDamage);
                */

                for (Entity e : entities) {
                    e.attackEntityFrom(new TaserDamageSource(playerIn), (float) actualDamage);
                }

                itemstack.damageItem((amount > 0 ? amount : 1), playerIn, player -> {
                    // TODO GUI alerts?
                    player.sendBreakAnimation(player.getActiveHand());
                });

                //LOGGER.info(itemstack.getDamage());
                return ActionResult.newResult(ActionResultType.SUCCESS, itemstack);
            }
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("item.tryingout.taser.tooltip"));
        if (stack.getDamage() >= stack.getMaxDamage() - 1) {
            tooltip.add(new TranslationTextComponent("item.tryingout.taser.lowpower"));
        }
    }
}
