package com.leocth.tryingout.misc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * This is a Java file created by LeoC200 on 2019/8/1 in project TryingOut_1142
 * All sources are released publicly on GitHub under the MIT license.
 */
public class TaserDamageSource extends EntityDamageSource {

    public TaserDamageSource(Entity culprit) {
        super("taser", culprit);
        this.setDamageBypassesArmor().setDamageIsAbsolute();
    }

    @Override
    public ITextComponent getDeathMessage(LivingEntity entityLivingBaseIn) {
        String s = "death.attack." + this.damageType;
        return (entityLivingBaseIn instanceof PlayerEntity) ?
                new TranslationTextComponent(s, entityLivingBaseIn.getDisplayName(), this.damageSourceEntity.getDisplayName()) :
                new StringTextComponent("");
    }
}
