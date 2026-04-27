package me.creeperscrown.longtimenosword.item;

import me.creeperscrown.longtimenosword.Config;
import me.creeperscrown.longtimenosword.Longtimenosword;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.tools.item.ModifiableSwordItem;

public class Longsword extends ModifiableSwordItem {

    public static final ToolDefinition LONGSWORD = ToolDefinition.create(TinkersItems.LONGSWORD);

    public Longsword() {
        super(Longtimenosword.defaultItemProperties().stacksTo(1), LONGSWORD);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if(attacker instanceof Player p){
            ResourceLocation soundId = ResourceLocation.tryParse(Config.swing_sound);
            SoundEvent sound = (soundId != null) ? BuiltInRegistries.SOUND_EVENT.get(soundId) : null;
            boolean flag = p.getAttackStrengthScale(0.5F) > 0.9F;
            boolean onGround = p.onGround();
            boolean flag1 = false;
            if (p.isSprinting() && flag) {flag1 = true;}
            boolean flag2 = flag && p.fallDistance > 0.0F && !p.onGround() && !p.onClimbable() && !p.isInWater() && !p.hasEffect(MobEffects.BLINDNESS) && !p.isPassenger() && target instanceof LivingEntity;
            flag2 = flag2 && !p.isSprinting();
            double d0 = (double)(p.walkDist-p.walkDistO);
            boolean condition = flag && !flag2 && !flag1 && onGround && d0<(double)p.getSpeed();
            switch(Config.play_swing_sound){
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

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player p, InteractionHand h) {
        ItemStack stack = p.getItemInHand(h);
        p.startUsingItem(h);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        switch(Config.charge_animation){
            case "block": return UseAnim.BLOCK;
            case "drink": return UseAnim.DRINK;
            case "none": return UseAnim.NONE;
            case "custom": return UseAnim.CUSTOM;
            case "bow": return UseAnim.BOW;
            case "brush": return UseAnim.BRUSH;
            case "crossbow": return UseAnim.CROSSBOW;
            case "eat": return UseAnim.EAT;
            case "trident": return UseAnim.SPEAR;
            case "spyglass": return UseAnim.SPYGLASS;
            case "horn": return UseAnim.TOOT_HORN;
            default: return UseAnim.BOW;
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return Config.lunge_max_hold;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
        int time = this.getUseDuration(stack) - timeLeft;
        ResourceLocation smallId = ResourceLocation.tryParse(Config.small_lunge);
        SoundEvent small = (smallId != null) ? BuiltInRegistries.SOUND_EVENT.get(smallId) : null;
        ResourceLocation bigId = ResourceLocation.tryParse(Config.big_lunge);
        SoundEvent big = (bigId != null) ? BuiltInRegistries.SOUND_EVENT.get(bigId) : null;
        ResourceLocation riptideId = ResourceLocation.tryParse(Config.riptide_sound);
        SoundEvent riptide = (riptideId != null) ? BuiltInRegistries.SOUND_EVENT.get(riptideId) : null;
        SoundEvent sound = null;

        if(Config.play_lunge_sounds) sound = small;

        float increase = (float) (Config.lunge_jump_mod * time + 0.2);
        float speed = (float) Config.lunge_speed_mod * time;

        if(increase > (float) Config.lunge_jump_cap) {
            increase = (float) Config.lunge_jump_cap;
        }
        if(speed > (float) Config.lunge_speed_cap) {
            speed = (float) Config.lunge_speed_cap;
            sound = big;
        }

        if(time > Config.lunge_min_hold && time < Config.riptide_threshold && Config.perform_riptide_attack) {
            if(entityLiving instanceof Player) {
                Player entityPlayer = ((Player) entityLiving);
                if(!entityPlayer.isCreative()){
                    entityPlayer.causeFoodExhaustion((float) Config.lunge_exhaustion);
                    entityPlayer.getCooldowns().addCooldown(stack.getItem(), Config.lunge_cooldown);
                    if(Config.damage_on_lunge){
                        ToolDamageUtil.handleDamageItem(stack, Config.lunge_damage_amount, entityPlayer, p->p.broadcastBreakEvent(entityPlayer.getUsedItemHand()));
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

            if(Config.play_lunge_sounds && sound!=null && entityLiving instanceof Player p){
                worldIn.playSound(null, p.getX(), p.getY(), p.getZ(), sound, SoundSource.PLAYERS, 1.0F, 1.0F);
            }

        }

        if(time >= Config.riptide_threshold && Config.perform_riptide_attack){
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
                    player.getCooldowns().addCooldown(stack.getItem(), Config.riptide_cooldown);
                    if(Config.damage_on_riptide){
                        ToolDamageUtil.handleDamageItem(stack, Config.riptide_damage_amount, player, p->p.broadcastBreakEvent(player.getUsedItemHand()));
                    }
                }
                if (player.onGround()) {
                    player.move(MoverType.SELF, new Vec3(0.0D, 1.2D, 0.0D));
                }
                if(Config.play_riptide_sounds && riptide!=null){
                    worldIn.playSound(null, player, riptide, SoundSource.PLAYERS, 1.0F, 1.0F);
                }

            }
        }

        super.releaseUsing(stack, worldIn, entityLiving, timeLeft);
    }

}
