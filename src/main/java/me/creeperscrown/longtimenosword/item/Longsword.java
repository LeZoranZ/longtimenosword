package me.creeperscrown.longtimenosword.item;

import me.creeperscrown.longtimenosword.Longtimenosword;
import me.creeperscrown.longtimenosword.config.ClientConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.tools.item.ModifiableSwordItem;

public class Longsword extends ModifiableSwordItem {

    public static final ToolDefinition LONGSWORD = ToolDefinition.create(TinkersItems.LONGSWORD);

    public Longsword() {
        super(Longtimenosword.defaultItemProperties().stacksTo(1), LONGSWORD);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if(attacker instanceof Player p){
            ResourceLocation soundId = ResourceLocation.tryParse(ClientConfig.swing_sound);
            SoundEvent sound = (soundId != null) ? BuiltInRegistries.SOUND_EVENT.get(soundId) : null;
            boolean flag = p.getAttackStrengthScale(0.5F) > 0.9F;
            boolean onGround = p.onGround();
            boolean flag1 = false;
            if (p.isSprinting() && flag) {flag1 = true;}
            boolean flag2 = flag && p.fallDistance > 0.0F && !p.onGround() && !p.onClimbable() && !p.isInWater() && !p.hasEffect(MobEffects.BLINDNESS) && !p.isPassenger() && target instanceof LivingEntity;
            flag2 = flag2 && !p.isSprinting();
            double d0 = (double)(p.walkDist-p.walkDistO);
            boolean condition = flag && !flag2 && !flag1 && onGround && d0<(double)p.getSpeed();
            switch(ClientConfig.play_swing_sound){
                case "always": {
                    if(sound!=null) p.level().playSound(null, p.getX(), p.getY(), p.getZ(), sound, SoundSource.PLAYERS, 1.0F, 1.0F);
                    break;
                }
                case "never": {
                    break;
                }
                default: {
                    if(condition && sound!=null) p.level().playSound(null, p.getX(), p.getY(), p.getZ(), sound, SoundSource.PLAYERS, 1.0F, 1.0F);
                    break;
                }
            }
        }
        return super.hurtEnemy(stack, target, attacker);
    }

}
