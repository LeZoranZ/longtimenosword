package me.creeperscrown.longtimenosword.client.gui;

import me.creeperscrown.longtimenosword.LTNSConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.ArrayList;
import java.util.List;

public class SoundBox extends LargeEditBox {
    private final LTNSConfig skrinche;

    private final Font fontche;

    private final Minecraft mc = Minecraft.getInstance();
    private final List<String> allSounds = BuiltInRegistries.SOUND_EVENT.keySet().stream().map(ResourceLocation::toString).sorted().toList();
    private final List<String> filteredSounds = new ArrayList<>();
    private boolean showSuggestions = false;
    private int selectedSuggestion = 0;
    private int scrollOffset = 0;
    private final int visibleSuggestionCount = 4;

    private int hovered = -1;
    private boolean userTyping = false;

    public SoundBox(LTNSConfig parent, Font font, int x, int y, int width, int height, Component message) {
        super(font, x, y, width, height, message);

        this.skrinche = parent;
        this.fontche = font;
        this.setResponder(this::onTextChanged);
    }

    private void onTextChanged(String value) {
        setTextColor(soundExists(value) ? 0x55FF55 : 0xFF5555);

        if(userTyping){
            filteredSounds.clear();
            for(String s : allSounds){if(s.contains(value.toLowerCase())) filteredSounds.add(s);}
            showSuggestions = !filteredSounds.isEmpty() && this.isFocused();
            selectedSuggestion=0;
            scrollOffset=0;
        }

    }

    private boolean soundExists(String id) {
        ResourceLocation rl = ResourceLocation.tryParse(id);
        if(rl==null) return false;
        return mc.getSoundManager().getAvailableSounds().contains(rl);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean handled = false;
        if(showSuggestions){
            //down
            if(keyCode==264){
                selectedSuggestion = Math.min(selectedSuggestion+1, filteredSounds.size()-1);
                if(selectedSuggestion >= scrollOffset+visibleSuggestionCount) scrollOffset++;
                return true;
            }
            //up
            if(keyCode==265){
                selectedSuggestion = Math.max(selectedSuggestion-1, 0);
                if(selectedSuggestion<scrollOffset) scrollOffset--;
                return true;
            }
            //enter(s) or tab
            if(keyCode==257 || keyCode==335 || keyCode==258){
                setValue(filteredSounds.get(selectedSuggestion));
                showSuggestions = false;
                moveCursorToEnd();
                onTextChanged(getValue());
                return true;
            }
        }
        boolean changed = super.keyPressed(keyCode, scanCode, modifiers);

        if(!userTyping && changed){userTyping=true;}
        if(userTyping){onTextChanged(getValue());}

        return changed || handled;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        if (showSuggestions) {
            int x = this.getX();
            int y = this.getY() + this.getHeight()+2;
            int width = this.getWidth();

            int end = Math.min(scrollOffset+visibleSuggestionCount, filteredSounds.size());

            for(int i=scrollOffset; i<end; i++){
                int yy = y+(i-scrollOffset)*12;
                if (mouseX>=x && mouseX <= x+width && mouseY>=yy && mouseY <= yy+12) {
                    setValue(filteredSounds.get(i));
                    showSuggestions=false;
                    moveCursorToEnd();
                    return true;
                }
            }

            if(mouseX<x || mouseX>x+width || mouseY<y || mouseY>y+height){showSuggestions=false;}
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void renderWidget(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        updateHover(mouseX, mouseY);
        super.renderWidget(g, mouseX, mouseY, partialTick);

        if (showSuggestions) {
            int x = getX();
            int y = getY() + getHeight()+2;
            int width = getWidth();
            int end = Math.min(scrollOffset+visibleSuggestionCount, filteredSounds.size());

            for (int i=scrollOffset; i<end; i++) {
                int yy = y+(i-scrollOffset)*12;

                boolean isSelected = (i==selectedSuggestion);
                boolean isHovered = (i==hovered);

                int bg = isHovered ? 0x66555555 : (isSelected ? 0x66555555 : 0x66222222);
                int txt = isHovered ? 0xFFFF55 : (isSelected ? 0x00FFFF : 0xFFFFFF);

                int index = i;
                if(index<0||index>=filteredSounds.size()) continue;
                g.fill(x, yy, x+width, yy+12, bg);
                g.drawString(fontche, filteredSounds.get(i), x+2, yy+2, txt);
            }
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if(showSuggestions){
            int x = getX();
            int y = getY() + getHeight()+2;
            int width = getWidth();
            int height = visibleSuggestionCount*12;

            if(mouseX>=x && mouseX <= x+width && mouseY>=y && mouseY <= y+height){
                scroll(delta);
                return true;
            }

        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    public boolean isMouseOver(double mouseX, double mouseY){
        int x = getX();
        int y = getY() + getHeight()+2;
        int width = getWidth();
        int height = visibleSuggestionCount*12;
        return mouseX>=x && mouseX <= x+width && mouseY >= y && mouseY <= y+height;
    }

    public void scroll(double delta){
        if(filteredSounds.isEmpty()) return;
        if(delta>0){
            if(scrollOffset>0){
                scrollOffset = Math.max(scrollOffset-1, 0);
                selectedSuggestion = Math.max(selectedSuggestion-1, 0);
            }
        } else {
            if(scrollOffset+visibleSuggestionCount<filteredSounds.size()){
                scrollOffset = Math.min(scrollOffset+1, filteredSounds.size()-visibleSuggestionCount);
                selectedSuggestion = Math.min(selectedSuggestion+1, filteredSounds.size()-1);
            }
        }
    }

    public boolean isShowingSuggestions() {
        return showSuggestions && this.isFocused();
    }

    @Override
    public void setFocused(boolean focused) {
        super.setFocused(focused);
        if(focused){
            skrinche.setActiveSoundBox(this);
            onTextChanged(getValue());
        }else{
            showSuggestions=false;
        }
    }

    public void updateHover(double mouseX, double mouseY) {
        if (!showSuggestions) {
            hovered = -1;
            return;
        }

        int x = getX();
        int y = getY() + getHeight()+2;
        int end = Math.min(scrollOffset + visibleSuggestionCount, filteredSounds.size());

        hovered = -1;
        for(int i=scrollOffset; i<end; i++){
            int yy = y+(i-scrollOffset)*12;
            if (mouseX>=x && mouseX <= x+getWidth() && mouseY>=yy && mouseY <= yy+12) {
                hovered=i;
                break;
            }
        }
    }

    @Override
    public void insertText(String s) {
        super.insertText(s);
        if(!userTyping) userTyping=true;
        onTextChanged(getValue());
    }

}