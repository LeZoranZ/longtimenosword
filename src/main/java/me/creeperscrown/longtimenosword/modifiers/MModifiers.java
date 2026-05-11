package me.creeperscrown.longtimenosword.modifiers;

import me.creeperscrown.longtimenosword.Longtimenosword;
import net.minecraftforge.eventbus.api.IEventBus;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierManager;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

public class MModifiers {
    public static final ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(Longtimenosword.MODID);
    public static final StaticModifier<Modifier> LUNGING = MODIFIERS.register("lunging", LungingModifier::new);
}
