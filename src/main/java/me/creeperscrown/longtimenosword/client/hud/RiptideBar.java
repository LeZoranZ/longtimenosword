package me.creeperscrown.longtimenosword.client.hud;

import me.creeperscrown.longtimenosword.config.Config;
import me.creeperscrown.longtimenosword.Longtimenosword;
import me.creeperscrown.longtimenosword.config.ClientConfig;
import me.creeperscrown.longtimenosword.modifiers.MModifiers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

@Mod.EventBusSubscriber(modid = Longtimenosword.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RiptideBar {
    private static final ResourceLocation RIPTIDE_BAR = new ResourceLocation("longtimenosword", "textures/gui/hud/riptide_bar.png");

    @SubscribeEvent
    public static void onOverlayPre(RenderGuiOverlayEvent.Pre e){
        if(!Config.PERFORM_RIPTIDE_ATTACK.get()) return;
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if(player==null) return;
        ItemStack stack = player.getUseItem();
        if(stack.isEmpty()) return;
        if(!(stack.getItem() instanceof ModifiableItem)) return;
        ToolStack ts = ToolStack.from(stack);
        if(ts.getModifierLevel(MModifiers.LUNGING.getId())<=0) return;

        if(e.getOverlay().id().equals(ResourceLocation.withDefaultNamespace("experience_bar"))){
            e.setCanceled(true);
        }

        int maxCharge = Config.RIPTIDE_THRESHOLD.get();
        int charge = (stack.getItem()).getUseDuration(stack) - player.getUseItemRemainingTicks();
        renderChargeBar(mc, e.getGuiGraphics(), charge, maxCharge);
    }

    private static void renderChargeBar(Minecraft mc, GuiGraphics graphics, int charge, int maxCharge) {
        int w = mc.getWindow().getGuiScaledWidth();
        int h = mc.getWindow().getGuiScaledHeight();
        int x = w/2-91;
        int y = h-32+3;

        graphics.blit(RIPTIDE_BAR, x, y, 0, 0, 182, 5);

        float f = Math.min(1F, (float) charge/maxCharge);
        float scale = f;
        switch(ClientConfig.charge_bar_scaling){
            case "linear": {scale=f; break;}
            case "quadratic": {scale=f*f; break;}
            case "cubic": {scale=f*f*f; break;}
            case "sqrt": {scale=(float)Math.sqrt(f); break;}
            case "quadratic_ease_out": {scale=1-(1-f)*(1-f); break;}
            case "ease_in_out": {scale=f*f*(3-2*f); break;}
            case "sine": {scale=(float)Math.sin((f*Math.PI)/2); break;}
            case "elastic": {scale=(float)Math.sin(10*f)*(1-f)+f; break;}

            default: {scale=f*f; break;}
        }
        int filled = (int)(scale*183.0F);
        if (filled > 0) {
            graphics.blit(RIPTIDE_BAR, x, y, 0, 5, filled, 5);
        }
    }

}
