package me.creeperscrown.longtimenosword.config;

import me.creeperscrown.longtimenosword.Longtimenosword;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = Longtimenosword.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientConfig {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.ConfigValue<String> CHARGE_ANIMATION = BUILDER
            .comment("The animation to use when charging the lunge attack")
            .comment("Options: BLOCK, NONE, DRINK, CUSTOM, BOW, BRUSH, CROSSBOW, EAT, TRIDENT, SPYGLASS, HORN")
            .comment("Default/Old Tinkers': BOW")
            .define("charge_animation", "bow");
    public static final ForgeConfigSpec.ConfigValue<String> SMALL_LUNGE = BUILDER
            .comment("Sound to use for lunges that aren't fully charged")
            .comment("Requires 'play_lunge_sounds' to be true")
            .comment("Default = minecraft:entity.shulker.hurt_closed")
            .define("small_lunge", "minecraft:entity.shulker.hurt_closed");
    public static final ForgeConfigSpec.ConfigValue<String> BIG_LUNGE = BUILDER
            .comment("Sound to use for fully charged lunges")
            .comment("Requires 'play_lunge_sounds' to be true")
            .comment("Default = minecraft:entity.shulker_bullet.hit")
            .define("big_lunge", "minecraft:entity.shulker_bullet.hit");
    public static final ForgeConfigSpec.ConfigValue<String> RIPTIDE_SOUND = BUILDER
            .comment("The sound to play when performing a riptide attack with a longsword")
            .comment("Requires 'play_riptide_sounds' to be true")
            .comment("Default = minecraft:item.trident.riptide_1")
            .define("riptide_sound", "minecraft:item.trident.riptide_1");
    public static final ForgeConfigSpec.ConfigValue<String> PLAY_SWING_SOUND = BUILDER
            .comment("When to play the swing sound")
            .comment("Imitates vanilla behaviour by default ('sweeping attack' conditions)")
            .comment("Options: ALWAYS, NEVER, VANILLA")
            .define("play_swing_sound", "vanilla");
    public static final ForgeConfigSpec.ConfigValue<String> SWING_SOUND = BUILDER
            .comment("Sound to play when swinging")
            .comment("Default = minecraft:entity.player.attack.sweep")
            .define("swing_sound", "minecraft:entity.player.attack.sweep");
    public static final ForgeConfigSpec.ConfigValue<String> CHARGE_BAR_SCALING = BUILDER
            .comment("The type of scaling to use for the charge bar")
            .comment("Options: LINEAR, QUADRATIC, CUBIC, SQRT, QUADRATIC_EASE_OUT, EASE_IN_OUT, SINE, ELASTIC")
            .comment("Default: QUADRATIC")
            .define("charge_bar_scaling", "quadratic");

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static String charge_animation;
    public static String small_lunge;
    public static String big_lunge;
    public static String riptide_sound;
    public static String play_swing_sound;
    public static String swing_sound;
    public static String charge_bar_scaling;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        charge_animation = CHARGE_ANIMATION.get().toLowerCase();
        small_lunge = SMALL_LUNGE.get();
        big_lunge = BIG_LUNGE.get();
        riptide_sound = RIPTIDE_SOUND.get();
        play_swing_sound = PLAY_SWING_SOUND.get().toLowerCase();
        swing_sound = SWING_SOUND.get();
        charge_bar_scaling = CHARGE_BAR_SCALING.get().toLowerCase();
    }
}
