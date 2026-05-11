package me.creeperscrown.longtimenosword.config;

import me.creeperscrown.longtimenosword.Longtimenosword;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Longtimenosword.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue PREVENT_LUNGE = BUILDER
            .comment("Should broken tools prevent you from using the lunge ability?")
            .comment("Default: false")
            .comment("Old Tinkers': false")
            .define("prevent_lunge", false);
    public static final ForgeConfigSpec.IntValue LUNGE_MIN_HOLD = BUILDER
            .comment("The minimum amount of time a player needs to hold right click to perform a lunge attack (in ticks)")
            .comment("Default: 5")
            .comment("Old Tinkers': 5")
            .comment("NOTE: Set no higher than 'lunge_max_hold' or 'riptide_threshold' (if using 'perform_riptide_attack')")
            .defineInRange("lunge_min_hold", 5, 0, Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue LUNGE_MAX_HOLD = BUILDER
            .comment("How long the player can hold right click before the longsword resets (in ticks)")
            .comment("Default: 200")
            .comment("Old Tinkers': 200")
            .defineInRange("lunge_max_hold", 200, 0, Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue LUNGE_CD = BUILDER
            .comment("Longsword lunge ability cooldown (in ticks)")
            .comment("Default: 3")
            .comment("Old Tinkers': 3")
            .defineInRange("lunge_cooldown", 3, 0, Integer.MAX_VALUE);
    public static final ForgeConfigSpec.DoubleValue LUNGE_SPEED_MOD = BUILDER
            .comment("What modifier to use in lunge speed calculations (horizontal velocity)")
            .comment("Default: 0.05")
            .comment("Old Tinkers': 0.05")
            .defineInRange("lunge_speed_mod", 0.05, 0, Double.MAX_VALUE);
    public static final ForgeConfigSpec.DoubleValue LUNGE_SPEED_CAP = BUILDER
            .comment("The value to cap the lunging speed to")
            .comment("Default: 0.925")
            .comment("Old Tinkers': 0.925")
            .defineInRange("lunge_speed_cap", 0.925, 0, Double.MAX_VALUE);
    public static final ForgeConfigSpec.DoubleValue LUNGE_JUMP_MOD = BUILDER
            .comment("Vertical velocity modifier for the lunge attack")
            .comment("Default: 0.02")
            .comment("Old Tinkers': 0.02")
            .defineInRange("lunge_jump_mod", 0.02, 0, Double.MAX_VALUE);
    public static final ForgeConfigSpec.DoubleValue LUNGE_JUMP_CAP = BUILDER
            .comment("The value to cap the jump boost of the lunge attack to")
            .comment("Default: 0.56")
            .comment("Old Tinkers': 0.56")
            .defineInRange("lunge_jump_cap", 0.56, 0, Double.MAX_VALUE);
    public static final ForgeConfigSpec.DoubleValue LUNGE_FOOD_EXHAUSTION = BUILDER
            .comment("Food exhaustion value to apply after using the lunge attack")
            .comment("Default: 0.2")
            .comment("Old Tinkers': 0.2")
            .defineInRange("lunge_exhaustion", 0.2, 0, Double.MAX_VALUE);
    public static final ForgeConfigSpec.BooleanValue PERFORM_RIPTIDE_ATTACK = BUILDER
            .comment("Should the longsword allow the user to perform riptide attacks?")
            .comment("Default: true")
            .define("perform_riptide_attack", true);
    public static final ForgeConfigSpec.IntValue RIPTIDE_CD = BUILDER
            .comment("The cooldown applied to the longsword after executing a riptide attack (in ticks)")
            .comment("Default: 200")
            .comment("Only effective if 'perform_riptide_attack' is true")
            .defineInRange("riptide_cooldown", 200, 0, Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue RIPTIDE_THRESHOLD = BUILDER
            .comment("Defines the threshold (in ticks) past which charging the lunge attack will result in riptide")
            .comment("Default: 45")
            .comment("Only effective if 'perform_riptide_attack' is true")
            .defineInRange("riptide_threshold", 45, 0, Integer.MAX_VALUE);
    public static final ForgeConfigSpec.BooleanValue DAMAGE_ON_LUNGE = BUILDER
            .comment("Should the longsword take damage when the player uses its lunge ability?")
            .comment("Default: false")
            .define("damage_on_lunge", false);
    public static final ForgeConfigSpec.IntValue LUNGE_DAMAGE_AMOUNT = BUILDER
            .comment("Defines the amount of damage the longsword receives when its lunge ability is used")
            .comment("Only effective if 'damage_on_lunge' is set to true")
            .defineInRange("lunge_damage_amount", 1, 0, Integer.MAX_VALUE);
    public static final ForgeConfigSpec.BooleanValue DAMAGE_ON_RIPTIDE = BUILDER
            .comment("Should the longsword also take damage when the player uses its riptide ability even if the attack misses?")
            .comment("Default: false")
            .define("damage_on_riptide", true);
    public static final ForgeConfigSpec.IntValue RIPTIDE_DAMAGE_AMOUNT = BUILDER
            .comment("Defines the amount of damage the longsword takes when its riptide ability is used")
            .comment("Only effective if 'damage_on_riptide' is set to true")
            .defineInRange("riptide_damage_amount", 1, 0, Integer.MAX_VALUE);
    public static final ForgeConfigSpec.BooleanValue PLAY_LUNGE_SOUNDS = BUILDER
            .comment("Controls whether a sound should be played when lunging")
            .comment("Default: true")
            .define("play_lunge_sounds", true);
    public static final ForgeConfigSpec.BooleanValue PLAY_RIPTIDE = BUILDER
            .comment("Whether to play a sound when performing riptide attacks with the longsword")
            .comment("Default: true")
            .define("play_riptide_sounds", true);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

}
