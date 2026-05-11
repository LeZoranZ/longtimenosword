package me.creeperscrown.longtimenosword.modifiers;

import me.creeperscrown.longtimenosword.config.Config;
import me.creeperscrown.longtimenosword.config.ClientConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.tools.modifiers.ability.interaction.BlockingModifier;
import slimeknights.tconstruct.tools.modifiers.ability.sling.SlingModifier;

public class LungingModifier extends SlingModifier {

    @Override
    public int getUseDuration(IToolStackView tool, ModifierEntry modifier){return Config.LUNGE_MAX_HOLD.get();}

    @Override
    public InteractionResult onToolUse(IToolStackView tool, ModifierEntry modifier, Player player, InteractionHand hand, InteractionSource source) {
        if(source!=InteractionSource.RIGHT_CLICK){return InteractionResult.PASS;}
        if(Config.PREVENT_LUNGE.get() && tool.isBroken()){return InteractionResult.PASS;}
        GeneralInteractionModifierHook.startUsing(tool, modifier.getId(), player, hand);

        return InteractionResult.CONSUME;
    }

    @Override
    public UseAnim getUseAction(IToolStackView tool, ModifierEntry modifier){
        switch(ClientConfig.charge_animation){
            case "block": return BlockingModifier.blockWhileCharging(tool, UseAnim.BLOCK);
            case "drink": return BlockingModifier.blockWhileCharging(tool, UseAnim.DRINK);
            case "none": return BlockingModifier.blockWhileCharging(tool, UseAnim.NONE);
            case "custom": return BlockingModifier.blockWhileCharging(tool, UseAnim.CUSTOM);
            case "bow": return BlockingModifier.blockWhileCharging(tool, UseAnim.BOW);
            case "brush": return BlockingModifier.blockWhileCharging(tool, UseAnim.BRUSH);
            case "crossbow": return BlockingModifier.blockWhileCharging(tool, UseAnim.CROSSBOW);
            case "eat": return BlockingModifier.blockWhileCharging(tool, UseAnim.EAT);
            case "trident": return BlockingModifier.blockWhileCharging(tool, UseAnim.SPEAR);
            case "spyglass": return BlockingModifier.blockWhileCharging(tool, UseAnim.SPYGLASS);
            case "horn": return BlockingModifier.blockWhileCharging(tool, UseAnim.TOOT_HORN);
            default: return BlockingModifier.blockWhileCharging(tool, UseAnim.BOW);
        }
    }

    @Override
    public void beforeReleaseUsing(IToolStackView stack, ModifierEntry modifier, LivingEntity entityLiving, int useDuration, int timeLeft, ModifierEntry activeModifier){
        Level worldIn = entityLiving.level();
        int time = useDuration - timeLeft;
        ResourceLocation smallId = ResourceLocation.tryParse(ClientConfig.small_lunge);
        SoundEvent small = (smallId != null) ? BuiltInRegistries.SOUND_EVENT.get(smallId) : null;
        ResourceLocation bigId = ResourceLocation.tryParse(ClientConfig.big_lunge);
        SoundEvent big = (bigId != null) ? BuiltInRegistries.SOUND_EVENT.get(bigId) : null;
        ResourceLocation riptideId = ResourceLocation.tryParse(ClientConfig.riptide_sound);
        SoundEvent riptide = (riptideId != null) ? BuiltInRegistries.SOUND_EVENT.get(riptideId) : null;
        SoundEvent sound = null;

        if(Config.PLAY_LUNGE_SOUNDS.get()) sound = small;

        float increase = (float) (Config.LUNGE_JUMP_MOD.get() * time + 0.2);
        float speed = (float) (Config.LUNGE_SPEED_MOD.get() * time);

        if(increase > (float)(double)Config.LUNGE_JUMP_CAP.get()) {
            increase = (float)(double)Config.LUNGE_JUMP_CAP.get();
        }
        if(speed > (float)(double)Config.LUNGE_SPEED_CAP.get()) {
            speed = (float)(double)Config.LUNGE_SPEED_CAP.get();
            sound = big;
        }

        if(time > Config.LUNGE_MIN_HOLD.get() && time < Config.RIPTIDE_THRESHOLD.get() && Config.PERFORM_RIPTIDE_ATTACK.get()) {
            if(entityLiving instanceof Player) {
                Player entityPlayer = ((Player) entityLiving);
                if(!entityPlayer.isCreative()){
                    entityPlayer.causeFoodExhaustion((float)(double)Config.LUNGE_FOOD_EXHAUSTION.get());
                    entityPlayer.getCooldowns().addCooldown(stack.getItem(), Config.LUNGE_CD.get());
                    if(Config.DAMAGE_ON_LUNGE.get()){
                        ToolDamageUtil.damageAnimated(stack, Config.LUNGE_DAMAGE_AMOUNT.get(), entityPlayer);
                    }
                }
            }
            entityLiving.setSprinting(true);

            double y = entityLiving.getDeltaMovement().y;

            double motionX = (-Mth.sin(entityLiving.getYRot() / 180.0F * (float) Math.PI) * Mth.cos(entityLiving.getXRot() / 180.0F * (float) Math.PI) * speed);
            double motionY = y + increase;
            double motionZ = (Mth.cos(entityLiving.getYRot() / 180.0F * (float) Math.PI) * Mth
                    .cos(entityLiving.getXRot() / 180.0F * (float) Math.PI) * speed);

            entityLiving.setDeltaMovement(motionX, motionY, motionZ);

            if(Config.PLAY_LUNGE_SOUNDS.get() && sound!=null && entityLiving instanceof Player p){
                worldIn.playSound(null, p.getX(), p.getY(), p.getZ(), sound, SoundSource.PLAYERS, 1.0F, 1.0F);
            }

        }

        if(time >= Config.RIPTIDE_THRESHOLD.get() && Config.PERFORM_RIPTIDE_ATTACK.get()){
            if (entityLiving instanceof Player player) {
                float f7 = player.getYRot();
                float f = player.getXRot();
                float f1 = -Mth.sin(f7 * ((float) Math.PI / 180F)) * Mth.cos(f * ((float) Math.PI / 180F));
                float f2 = -Mth.sin(f * ((float) Math.PI / 180F));
                float f3 = Mth.cos(f7 * ((float) Math.PI / 180F)) * Mth.cos(f * ((float) Math.PI / 180F));
                float f4 = Mth.sqrt(f1 * f1 + f2 * f2 + f3 * f3);
                float f5 = 3.0F * ((1.0F+(float) (0.4F+speed)) / 4.0F); //(time/20) or (0.3F+speed) used to be riptide level = replace w 1-3 if wonky
                f1 *= f5 / f4;
                f2 *= f5 / f4;
                f3 *= f5 / f4;

                player.push((double) f1, (double) f2, (double) f3);
                player.startAutoSpinAttack(20);

                if(!player.isCreative()){
                    player.getCooldowns().addCooldown(stack.getItem(), Config.RIPTIDE_CD.get());
                    if(Config.DAMAGE_ON_RIPTIDE.get()){
                        ToolDamageUtil.damageAnimated(stack, Config.RIPTIDE_DAMAGE_AMOUNT.get(), player);
                    }
                }
                if (player.onGround()) {
                    player.move(MoverType.SELF, new Vec3(0.0D, 1.2D, 0.0D));
                }
                if(Config.PLAY_RIPTIDE.get() && riptide!=null){
                    worldIn.playSound(null, player, riptide, SoundSource.PLAYERS, 1.0F, 1.0F);
                }

            }
        }

        super.beforeReleaseUsing(stack, modifier, entityLiving, useDuration, timeLeft, activeModifier);
    }

}
