package me.creeperscrown.longtimenosword;

import me.creeperscrown.longtimenosword.config.ClientConfig;
import me.creeperscrown.longtimenosword.config.Config;
import me.creeperscrown.longtimenosword.item.TinkersItems;
import me.creeperscrown.longtimenosword.modifiers.MModifiers;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.library.client.model.TinkerItemProperties;
import slimeknights.tconstruct.library.tools.helper.ToolBuildHandler;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.part.IMaterialItem;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Mod(Longtimenosword.MODID)
public class Longtimenosword {

    public static final String MODID = "longtimenosword";

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<CreativeModeTab> LONGTIMENOSWORD = CREATIVE_MODE_TABS.register("longtimenosword",
            ()-> CreativeModeTab.builder()
                    .icon(()-> TinkersItems.LONGSWORD.get().getRenderTool())
                    .title(Component.translatable("itemGroup.longtimenosword"))
                    .displayItems(Longtimenosword::addItems)
                    .build());

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> TinkerItemProperties.registerBrokenProperty(TinkersItems.LONGSWORD.get()));
    }

    public Longtimenosword() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(Longtimenosword::clientSetup);
        MModifiers.MODIFIERS.register(modEventBus);

        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        TinkersItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                ()-> new ConfigScreenHandler.ConfigScreenFactory(
                        (mc, parent) -> new LTNSConfig(parent)
                )
        );
    }

    public static Item.Properties defaultItemProperties() {
        return new Item.Properties();
    }

    private static void addItems(CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output tab){
        Consumer<ItemStack> output = tab::accept;
        acceptPart(output, TinkersItems.HAND_GUARD);
        acceptCast(tab, CastItemObject::get, TinkersItems.HAND_GUARD_CAST);
        acceptCast(tab, CastItemObject::getSand, TinkersItems.HAND_GUARD_CAST);
        acceptCast(tab, CastItemObject::getRedSand, TinkersItems.HAND_GUARD_CAST);
        acceptTool(output, TinkersItems.LONGSWORD);
    }

    private static void acceptPart(Consumer<ItemStack> output, Supplier<? extends IMaterialItem> item){
        item.get().addVariants(output, "");
    }
    private static void acceptCast(CreativeModeTab.Output output, Function<CastItemObject, ItemLike> getter, CastItemObject cast){
        output.accept(getter.apply(cast));
    }
    private static void acceptTool(Consumer<ItemStack> output, Supplier<? extends IModifiable> tool){
        ToolBuildHandler.addVariants(output, tool.get(), "");
    }

}
