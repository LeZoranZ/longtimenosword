package me.creeperscrown.longtimenosword.item;

import me.creeperscrown.longtimenosword.Longtimenosword;
import net.minecraft.world.item.Item;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.common.registration.ItemDeferredRegisterExtension;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;
import slimeknights.tconstruct.tools.stats.StatlessMaterialStats;

public class TinkersItems {

    public static final ItemDeferredRegisterExtension ITEMS = new ItemDeferredRegisterExtension(Longtimenosword.MODID);

    public static final ItemObject<ToolPartItem> HAND_GUARD = ITEMS.register("hand_guard",
                    ()-> new ToolPartItem(Longtimenosword.defaultItemProperties(), StatlessMaterialStats.BINDING.getIdentifier())); //GripMaterialStats.ID
    public static final CastItemObject HAND_GUARD_CAST = ITEMS.registerCast("hand_guard", new Item.Properties());
    public static final ItemObject<Longsword> LONGSWORD = ITEMS.register("longsword", Longsword::new);

}
