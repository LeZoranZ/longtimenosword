package me.creeperscrown.longtimenosword;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = Longtimenosword.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue LUNGE_MIN_HOLD = BUILDER
            .comment("The minimum amount of time a player needs to hold right click to perform a lunge attack (in ticks)")
            .comment("Default: 5")
            .comment("Old Tinker's: 5")
            .comment("NOTE: Set no higher than 'lunge_max_hold' or 'riptide_threshold' (if using 'perform_riptide_attack')")
            .defineInRange("lunge_min_hold", 5, 0, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.IntValue LUNGE_MAX_HOLD = BUILDER
            .comment("How long the player can hold right click before the longsword resets (in ticks)")
            .comment("Default: 200")
            .comment("Old Tinker's: 200")
            .defineInRange("lunge_max_hold", 200, 0, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.IntValue LUNGE_CD = BUILDER
            .comment("Longsword lunge ability cooldown (in ticks)")
            .comment("Default: 0")
            .comment("Old Tinker's: 3")
            .defineInRange("lunge_cooldown", 0, 0, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.DoubleValue LUNGE_SPEED_MOD = BUILDER
            .comment("What modifier to use in lunge speed calculations (horizontal velocity)")
            .comment("Default: 0.05")
            .comment("Old Tinker's: 0.05")
            .defineInRange("lunge_speed_mod", 0.05, 0, Double.MAX_VALUE);
    private static final ForgeConfigSpec.DoubleValue LUNGE_SPEED_CAP = BUILDER
            .comment("The value to cap the lunging speed to")
            .comment("Default: 0.925")
            .comment("Old Tinker's: 0.925")
            .defineInRange("lunge_speed_cap", 0.925, 0, Double.MAX_VALUE);
    private static final ForgeConfigSpec.DoubleValue LUNGE_JUMP_MOD = BUILDER
            .comment("Vertical velocity modifier for the lunge attack")
            .comment("Default: 0.02")
            .comment("Old Tinker's: 0.02")
            .defineInRange("lunge_jump_mod", 0.02, 0, Double.MAX_VALUE);
    private static final ForgeConfigSpec.DoubleValue LUNGE_JUMP_CAP = BUILDER
            .comment("The value to cap the jump boost of the lunge attack to")
            .comment("Default: 0.56")
            .comment("Old Tinker's: 0.56")
            .defineInRange("lunge_jump_cap", 0.56, 0, Double.MAX_VALUE);
    private static final ForgeConfigSpec.DoubleValue LUNGE_FOOD_EXHAUSTION = BUILDER
            .comment("Food exhaustion value to apply after using the lunge attack")
            .comment("Default: 0.2")
            .comment("Old Tinker's: 0.2")
            .defineInRange("lunge_exhaustion", 0.2, 0, Double.MAX_VALUE);
    private static final ForgeConfigSpec.ConfigValue<String> CHARGE_ANIMATION = BUILDER
            .comment("The animation to use when charging the lunge attack")
            .comment("Options: BLOCK, NONE, DRINK, CUSTOM, BOW, BRUSH, CROSSBOW, EAT, TRIDENT, SPYGLASS, HORN")
            .comment("Default/Old Tinker's: BOW")
            .define("charge_animation", "bow");
    private static final ForgeConfigSpec.BooleanValue PERFORM_RIPTIDE_ATTACK = BUILDER
            .comment("Should the longsword allow the user to perform riptide attacks?")
            .comment("Default: true")
            .define("perform_riptide_attack", true);
    private static final ForgeConfigSpec.IntValue RIPTIDE_CD = BUILDER
            .comment("The cooldown applied to the longsword after executing a riptide attack (in ticks)")
            .comment("Default: 200")
            .comment("Only effective if 'perform_riptide_attack' is true")
            .defineInRange("riptide_cooldown", 200, 0, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.IntValue RIPTIDE_THRESHOLD = BUILDER
            .comment("Defines the threshold (in ticks) past which charging the lunge attack will result in riptide")
            .comment("Default: 45")
            .comment("Only effective if 'perform_riptide_attack' is true")
            .defineInRange("riptide_threshold", 45, 0, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.BooleanValue DAMAGE_ON_LUNGE = BUILDER
            .comment("Should the longsword take damage when the player uses its lunge ability?")
            .comment("Default: false")
            .define("damage_on_lunge", true);
    private static final ForgeConfigSpec.IntValue LUNGE_DAMAGE_AMOUNT = BUILDER
            .comment("Defines the amount of damage the longsword receives when its lunge ability is used")
            .comment("Only effective if 'damage_on_lunge' is set to true")
            .defineInRange("lunge_damage_amount", 1, 0, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.BooleanValue DAMAGE_ON_RIPTIDE = BUILDER
            .comment("Should the longsword also take damage when the player uses its riptide ability even if the attack misses?")
            .comment("Default: false")
            .define("damage_on_riptide", true);
    private static final ForgeConfigSpec.IntValue RIPTIDE_DAMAGE_AMOUNT = BUILDER
            .comment("Defines the amount of damage the longsword takes when its riptide ability is used")
            .comment("Only effective if 'damage_on_riptide' is set to true")
            .defineInRange("riptide_damage_amount", 1, 0, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.BooleanValue PLAY_LUNGE_SOUNDS = BUILDER
            .comment("Controls whether a sound should be played for fully charged lunge attacks")
            .comment("Default: true")
            .define("play_lunge_sounds", true);
    private static final ForgeConfigSpec.ConfigValue<String> SMALL_LUNGE = BUILDER
            .comment("Sound to use for lunges that aren't fully charged")
            .comment("Requires 'play_lunge_sounds' to be true")
            .comment("Default = minecraft:entity.shulker.hurt_closed")
            .define("small_lunge", "minecraft:entity.shulker.hurt_closed");
    private static final ForgeConfigSpec.ConfigValue<String> BIG_LUNGE = BUILDER
            .comment("Sound to use for fully charged lunges")
            .comment("Requires 'play_lunge_sounds' to be true")
            .comment("Default = minecraft:entity.shulker_bullet.hit")
            .define("big_lunge", "minecraft:entity.shulker_bullet.hit");
    private static final ForgeConfigSpec.BooleanValue PLAY_RIPTIDE = BUILDER
            .comment("Whether to play a sound when performing riptide attacks with the longsword")
            .comment("Default: true")
            .define("play_riptide_sounds", true);
    private static final ForgeConfigSpec.ConfigValue<String> RIPTIDE_SOUND = BUILDER
            .comment("The sound to play when performing a riptide attack with a longsword")
            .comment("Requires 'play_riptide_sounds' to be true")
            .comment("Default = minecraft:item.trident.riptide_1")
            .define("riptide_sound", "minecraft:item.trident.riptide_1");
    private static final ForgeConfigSpec.ConfigValue<String> PLAY_SWING_SOUND = BUILDER
            .comment("When to play the swing sound")
            .comment("Imitates vanilla behaviour by default ('sweeping attack' conditions)")
            .comment("Options: ALWAYS, NEVER, VANILLA")
            .define("play_swing_sound", "vanilla");
    private static final ForgeConfigSpec.ConfigValue<String> SWING_SOUND = BUILDER
            .comment("Sound to play when swinging")
            .comment("Default = minecraft:entity.player.attack.sweep")
            .define("swing_sound", "minecraft:entity.player.attack.sweep");
    private static final ForgeConfigSpec.ConfigValue<String> CHARGE_BAR_SCALING = BUILDER
            .comment("The type of scaling to use for the charge bar")
            .comment("Options: LINEAR, QUADRATIC, CUBIC, SQRT, QUADRATIC_EASE_OUT, EASE_IN_OUT, SINE, ELASTIC")
            .comment("Default: QUADRATIC")
            .define("charge_bar_scaling", "ease-in-out");

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int lunge_min_hold;
    public static int lunge_max_hold;
    public static int lunge_cooldown;
    public static double lunge_speed_cap;
    public static double lunge_jump_cap;
    public static double lunge_speed_mod;
    public static double lunge_jump_mod;
    public static double lunge_exhaustion;
    public static String charge_animation;
    public static boolean perform_riptide_attack;
    public static int riptide_cooldown;
    public static int riptide_threshold;
    public static boolean damage_on_lunge;
    public static int lunge_damage_amount;
    public static boolean damage_on_riptide;
    public static int riptide_damage_amount;
    public static boolean play_lunge_sounds;
    public static String small_lunge;
    public static String big_lunge;
    public static String riptide_sound;
    public static boolean play_riptide_sounds;
    public static String play_swing_sound;
    public static String swing_sound;
    public static String charge_bar_scaling;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        lunge_max_hold = LUNGE_MAX_HOLD.get();
        lunge_cooldown = LUNGE_CD.get();
        lunge_speed_cap = LUNGE_SPEED_CAP.get();
        lunge_jump_cap = LUNGE_JUMP_CAP.get();
        lunge_exhaustion = LUNGE_FOOD_EXHAUSTION.get();
        charge_animation = CHARGE_ANIMATION.get().toLowerCase();
        perform_riptide_attack = PERFORM_RIPTIDE_ATTACK.get();
        riptide_cooldown = RIPTIDE_CD.get();
        riptide_threshold = RIPTIDE_THRESHOLD.get();
        damage_on_lunge = DAMAGE_ON_LUNGE.get();
        lunge_damage_amount = LUNGE_DAMAGE_AMOUNT.get();
        play_lunge_sounds = PLAY_LUNGE_SOUNDS.get();
        small_lunge = SMALL_LUNGE.get();
        big_lunge = BIG_LUNGE.get();
        lunge_speed_mod = LUNGE_SPEED_MOD.get();
        riptide_sound = RIPTIDE_SOUND.get();
        play_riptide_sounds = PLAY_RIPTIDE.get();
        lunge_jump_mod = LUNGE_JUMP_MOD.get();
        lunge_min_hold = LUNGE_MIN_HOLD.get();
        damage_on_riptide = DAMAGE_ON_RIPTIDE.get();
        riptide_damage_amount = RIPTIDE_DAMAGE_AMOUNT.get();
        play_swing_sound = PLAY_SWING_SOUND.get().toLowerCase();
        swing_sound = SWING_SOUND.get();
        charge_bar_scaling = CHARGE_BAR_SCALING.get().toLowerCase();
    }

}
