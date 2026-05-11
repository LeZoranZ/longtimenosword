package me.creeperscrown.longtimenosword;

import me.creeperscrown.longtimenosword.client.gui.LargeEditBox;
import me.creeperscrown.longtimenosword.client.gui.SoundBox;
import me.creeperscrown.longtimenosword.config.ClientConfig;
import me.creeperscrown.longtimenosword.config.Config;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.ArrayList;
import java.util.List;

public class LTNSConfig extends Screen {
    private final Screen parent;

    private List<String> allSounds;
    private List<String> filteredSounds = new ArrayList<>();
    private boolean showSuggestions = false;
    private int selectedSuggestion = 0;

    private EditBox minCharge;
    private EditBox maxCharge;
    private EditBox lungeCD;
    private EditBox riptideCD;
    private EditBox lungeEx;
    private EditBox riptideThreshold;
    private EditBox speedMod;
    private EditBox speedCap;
    private EditBox jumpMod;
    private EditBox jumpCap;
    private EditBox smallLunge;
    private EditBox bigLunge;
    private EditBox swingSound;
    private EditBox riptideSound;

    private EditBox soundBox;

    private boolean preventLungeV;
    private boolean riptideAttackV;
    private boolean playLungeSoundsV;
    private boolean playRiptideSoundsV;

    private boolean dirty = false;

    public LTNSConfig(Screen parent) {
        super(Component.translatable("config.longtimenosword.title").withStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.YELLOW)).withBold(true)));
        this.parent = parent;
    }

    @Override
    protected void init() {
        allSounds = BuiltInRegistries.SOUND_EVENT.keySet().stream().map(ResourceLocation::toString).sorted().toList();

        preventLungeV = Config.PREVENT_LUNGE.get();
        riptideAttackV = Config.PERFORM_RIPTIDE_ATTACK.get();
        playLungeSoundsV = Config.PLAY_LUNGE_SOUNDS.get();
        playRiptideSoundsV = Config.PLAY_RIPTIDE.get();
        int centerX = this.width/2;
        int buttonWidth = 140;
        int fieldWidth = 65;
        int spacing = 10;
        int totalWidth = buttonWidth*2 + spacing;
        int quadWidth = fieldWidth*4 + spacing*3;
        int startX = (this.width-quadWidth)/2;
        int leftX = (this.width-totalWidth)/2;
        int rightX = leftX+buttonWidth+spacing;
        int col1=startX; int col2=col1+fieldWidth+spacing; int col3=col2+fieldWidth+spacing; int col4=col3+fieldWidth+spacing;

        //Charge bar
        this.addRenderableWidget(Button.builder(Component.translatable("config.longtimenosword.boolean", Component.translatable("config.longtimenosword.scaling"),
                        ClientConfig.CHARGE_BAR_SCALING.get().toUpperCase()),
                btn -> {
                    cycleScaling();
                    dirty = true;
                    btn.setMessage(Component.translatable("config.longtimenosword.boolean", Component.translatable("config.longtimenosword.scaling"), ClientConfig.CHARGE_BAR_SCALING.get().toUpperCase()));
                }
        ).bounds(leftX, 20, 140, 20)
                .tooltip(Tooltip.create(Component.translatable("tooltip.longtimenosword.scaling").withStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.YELLOW)))))
                .build());
        //Tool Animation
        this.addRenderableWidget(Button.builder(Component.translatable("config.longtimenosword.boolean", Component.translatable("config.longtimenosword.animation"), ClientConfig.CHARGE_ANIMATION.get().toUpperCase()),
                btn -> {
                    cycleAnimation();
                    dirty = true;
                    btn.setMessage(Component.translatable("config.longtimenosword.boolean", Component.translatable("config.longtimenosword.animation"), ClientConfig.CHARGE_ANIMATION.get().toUpperCase()));
                }
        ).bounds(rightX, 20, 140, 20)
                .tooltip(Tooltip.create(Component.translatable("tooltip.longtimenosword.animation").withStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.YELLOW)))))
                .build());
        //Prevent Lunge
        this.addRenderableWidget(
                Button.builder(Component.translatable("config.longtimenosword.boolean", Component.translatable("config.longtimenosword.prevent_lunge"),
                                preventLungeV ? Component.translatable("options.on").withStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)) : Component.translatable("options.off").withStyle(Style.EMPTY.withColor(ChatFormatting.RED))),
                        btn -> {preventLungeV = !preventLungeV; Config.PREVENT_LUNGE.set(preventLungeV);
                    btn.setMessage(Component.translatable("config.longtimenosword.boolean", Component.translatable("config.longtimenosword.prevent_lunge"),
                            preventLungeV ? Component.translatable("options.on").withStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)) : Component.translatable("options.off").withStyle(Style.EMPTY.withColor(ChatFormatting.RED))));
                }
        ).bounds(leftX, 40, 140, 20)
                        .tooltip(Tooltip.create(Component.translatable("tooltip.longtimenosword.prevent_lunge").withStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.YELLOW)))))
                        .build());
        this.addRenderableWidget(
                Button.builder(Component.translatable("config.longtimenosword.boolean", Component.translatable("config.longtimenosword.lunge_sounds"), (playLungeSoundsV ? Component.translatable("options.on").withStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)) : Component.translatable("options.off").withStyle(Style.EMPTY.withColor(ChatFormatting.RED)))),
                                btn -> {playLungeSoundsV = !playLungeSoundsV; Config.PLAY_LUNGE_SOUNDS.set(playLungeSoundsV);
                                    btn.setMessage(Component.translatable("config.longtimenosword.boolean", Component.translatable("config.longtimenosword.lunge_sounds"), (playLungeSoundsV ? Component.translatable("options.on").withStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)) : Component.translatable("options.off").withStyle(Style.EMPTY.withColor(ChatFormatting.RED)))
                                    ));
                                }
                        ).bounds(rightX, 40, 140, 20)
                        .tooltip(Tooltip.create(Component.translatable("tooltip.longtimenosword.lunge_sounds").withStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.YELLOW)))))
                        .build());
        this.addRenderableWidget(
                Button.builder(Component.translatable("config.longtimenosword.boolean", Component.translatable("config.longtimenosword.riptide_attack"), (riptideAttackV ? Component.translatable("options.on").withStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)) : Component.translatable("options.off").withStyle(Style.EMPTY.withColor(ChatFormatting.RED)))),
                        btn -> {riptideAttackV = !riptideAttackV; Config.PERFORM_RIPTIDE_ATTACK.set(riptideAttackV);
                            btn.setMessage(Component.translatable("config.longtimenosword.boolean", Component.translatable("config.longtimenosword.riptide_attack"), (riptideAttackV ? Component.translatable("options.on").withStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)) : Component.translatable("options.off").withStyle(Style.EMPTY.withColor(ChatFormatting.RED)))
                            ));
                        }
                ).bounds(leftX, 60, 140, 20)
                        .tooltip(Tooltip.create(Component.translatable("tooltip.longtimenosword.riptide_attack").withStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.YELLOW)))))
                        .build());
        this.addRenderableWidget(
                Button.builder(Component.translatable("config.longtimenosword.boolean", Component.translatable("config.longtimenosword.riptide_sounds"), (playRiptideSoundsV ? Component.translatable("options.on").withStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)) : Component.translatable("options.off").withStyle(Style.EMPTY.withColor(ChatFormatting.RED)))),
                        btn -> {playRiptideSoundsV = !playRiptideSoundsV; Config.PLAY_RIPTIDE.set(playRiptideSoundsV);
                            btn.setMessage(Component.translatable("config.longtimenosword.boolean", Component.translatable("config.longtimenosword.riptide_sounds"), (playRiptideSoundsV ? Component.translatable("options.on").withStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)) : Component.translatable("options.off").withStyle(Style.EMPTY.withColor(ChatFormatting.RED)))
                            ));
                        }
                ).bounds(rightX, 60, 140, 20)
                        .tooltip(Tooltip.create(Component.translatable("tooltip.longtimenosword.riptide_sounds").withStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.YELLOW)))))
                        .build());
        this.addRenderableWidget(
                Button.builder(Component.translatable("config.longtimenosword.boolean", Component.translatable("config.longtimenosword.play_swing_sound"), (Component.translatable("option.longtimenosword."+ClientConfig.PLAY_SWING_SOUND.get().toLowerCase()).withStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)))),
                                btn -> {cycleSwingSound();
                                    btn.setMessage(Component.translatable("config.longtimenosword.boolean", Component.translatable("config.longtimenosword.play_swing_sound"), (Component.translatable("option.longtimenosword."+ClientConfig.PLAY_SWING_SOUND.get().toLowerCase()).withStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)))));
                                }
                        ).bounds(leftX, 80, 140, 20)
                        .tooltip(Tooltip.create(Component.translatable("tooltip.longtimenosword.play_swing_sound").withStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.YELLOW)))))
                        .build());
        swingSound = new SoundBox(this, this.font, rightX, 80, 140, 20, Component.translatable("config.longtimenosword.swing_sound"));
        swingSound.setMaxLength(128);
        swingSound.setHint(Component.literal("minecraft:entity.player.attack.sweep")
                .withStyle(ChatFormatting.DARK_GRAY));
        swingSound.setTooltip(Tooltip.create(Component.translatable("tooltip.longtimenosword.swing_sound").withStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.YELLOW)))));
        swingSound.setValue(ClientConfig.SWING_SOUND.get());
        swingSound.setResponder(v->dirty=true);
        this.addRenderableWidget(swingSound);
        this.addRenderableWidget(
                Button.builder(Component.literal("▶"),
                                btn -> playSound(swingSound.getValue()))
                        .bounds(col4+fieldWidth, 80, 20, 20)
                        .tooltip(Tooltip.create(Component.translatable("config.longtimenosword.play_sound").withStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.YELLOW)))))
                        .build()
        );

        minCharge = new EditBox(this.font, leftX, 100, 140, 20, Component.translatable("config.longtimenosword.min_charge"));
        minCharge.setValue(String.valueOf(Config.LUNGE_MIN_HOLD.get()));
        minCharge.setFilter(s -> s.matches("\\d*"));
        minCharge.setResponder(v->dirty=true);
        this.addRenderableWidget(minCharge).setTooltip(Tooltip.create(Component.translatable("tooltip.longtimenosword.min_charge").withStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.YELLOW)))));

        maxCharge = new EditBox(this.font, rightX, 100, 140, 20, Component.translatable("config.longtimenosword.max_charge"));
        maxCharge.setValue(String.valueOf(Config.LUNGE_MAX_HOLD.get()));
        maxCharge.setFilter(s -> s.matches("\\d*"));
        maxCharge.setResponder(v->dirty=true);
        this.addRenderableWidget(maxCharge).setTooltip(Tooltip.create(Component.translatable("tooltip.longtimenosword.max_charge").withStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.YELLOW)))));

        lungeCD = new EditBox(this.font, col1, 120, fieldWidth, 20, Component.translatable("config.longtimenosword.lunge_cd"));
        lungeCD.setValue(String.valueOf(Config.LUNGE_CD.get()));
        lungeCD.setFilter(s -> s.matches("\\d*"));
        lungeCD.setResponder(v->dirty=true);
        this.addRenderableWidget(lungeCD).setTooltip(Tooltip.create(Component.translatable("tooltip.longtimenosword.lunge_cd").withStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.YELLOW)))));

        riptideCD = new EditBox(this.font, col2, 120, fieldWidth, 20, Component.translatable("config.longtimenosword.riptide_cd"));
        riptideCD.setValue(String.valueOf(Config.RIPTIDE_CD.get()));
        riptideCD.setFilter(s -> s.matches("\\d*"));
        riptideCD.setResponder(v->dirty=true);
        this.addRenderableWidget(riptideCD).setTooltip(Tooltip.create(Component.translatable("tooltip.longtimenosword.riptide_cd").withStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.YELLOW)))));

        lungeEx = new EditBox(this.font, col3, 120, fieldWidth, 20, Component.translatable("config.longtimenosword.lunge_exhaustion"));
        lungeEx.setValue(String.valueOf(Config.LUNGE_FOOD_EXHAUSTION.get()));
        lungeEx.setFilter(s -> s.matches("\\d*(\\.\\d*)?"));
        lungeEx.setResponder(v->dirty=true);
        this.addRenderableWidget(lungeEx).setTooltip(Tooltip.create(Component.translatable("tooltip.longtimenosword.lunge_exhaustion").withStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.YELLOW)))));

        riptideThreshold = new EditBox(this.font, col4, 120, fieldWidth, 20, Component.translatable("config.longtimenosword.riptide_threshold"));
        riptideThreshold.setValue(String.valueOf(Config.RIPTIDE_THRESHOLD.get()));
        riptideThreshold.setFilter(s -> s.matches("\\d*"));
        riptideThreshold.setResponder(v->dirty=true);
        this.addRenderableWidget(riptideThreshold).setTooltip(Tooltip.create(Component.translatable("tooltip.longtimenosword.riptide_threshold").withStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.YELLOW)))));

        speedMod = new EditBox(this.font, col1, 140, fieldWidth, 20, Component.translatable("config.longtimenosword.lunge_speed_mod"));
        speedMod.setValue(String.valueOf(Config.LUNGE_SPEED_MOD.get()));
        speedMod.setFilter(s -> s.matches("\\d*(\\.\\d*)?"));
        speedMod.setResponder(v->dirty=true);
        this.addRenderableWidget(speedMod).setTooltip(Tooltip.create(Component.translatable("tooltip.longtimenosword.lunge_speed_mod").withStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.YELLOW)))));

        speedCap = new EditBox(this.font, col2, 140, fieldWidth, 20, Component.translatable("config.longtimenosword.lunge_speed_cap"));
        speedCap.setValue(String.valueOf(Config.LUNGE_SPEED_CAP.get()));
        speedCap.setFilter(s -> s.matches("\\d*(\\.\\d*)?"));
        speedCap.setResponder(v->dirty=true);
        this.addRenderableWidget(speedCap).setTooltip(Tooltip.create(Component.translatable("tooltip.longtimenosword.lunge_speed_cap").withStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.YELLOW)))));

        jumpMod = new EditBox(this.font, col3, 140, fieldWidth, 20, Component.translatable("config.longtimenosword.lunge_jump_mod"));
        jumpMod.setValue(String.valueOf(Config.LUNGE_JUMP_MOD.get()));
        jumpMod.setFilter(s -> s.matches("\\d*(\\.\\d*)?"));
        jumpMod.setResponder(v->dirty=true);
        this.addRenderableWidget(jumpMod).setTooltip(Tooltip.create(Component.translatable("tooltip.longtimenosword.lunge_jump_mod").withStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.YELLOW)))));

        jumpCap = new EditBox(this.font, col4, 140, fieldWidth, 20, Component.translatable("config.longtimenosword.lunge_jump_cap"));
        jumpCap.setValue(String.valueOf(Config.LUNGE_JUMP_CAP.get()));
        jumpCap.setFilter(s -> s.matches("\\d*(\\.\\d*)?"));
        jumpCap.setResponder(v->dirty=true);
        this.addRenderableWidget(jumpCap).setTooltip(Tooltip.create(Component.translatable("tooltip.longtimenosword.lunge_jump_cap").withStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.YELLOW)))));

        smallLunge = new SoundBox(this, this.font, leftX, 160, 140, 20, Component.translatable("config.longtimenosword.small_lunge"));
        smallLunge.setMaxLength(128);
        smallLunge.setHint(Component.literal("minecraft:entity.player.attack.sweep")
                .withStyle(ChatFormatting.DARK_GRAY));
        smallLunge.setTooltip(Tooltip.create(Component.translatable("tooltip.longtimenosword.small_lunge").withStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.YELLOW)))));
        smallLunge.setValue(ClientConfig.SMALL_LUNGE.get());
        smallLunge.setResponder(v->dirty=true);
        this.addRenderableWidget(smallLunge);
        this.addRenderableWidget(
                Button.builder(Component.literal("▶"),
                                btn -> playSound(smallLunge.getValue()))
                        .bounds(leftX-20, 160, 20, 20)
                        .tooltip(Tooltip.create(Component.translatable("config.longtimenosword.play_sound").withStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.YELLOW)))))
                        .build()
        );

        bigLunge = new SoundBox(this, this.font, rightX, 160, 140, 20, Component.translatable("config.longtimenosword.big_lunge"));
        bigLunge.setMaxLength(128);
        bigLunge.setHint(Component.literal("minecraft:entity.player.attack.sweep")
                .withStyle(ChatFormatting.DARK_GRAY));
        bigLunge.setTooltip(Tooltip.create(Component.translatable("tooltip.longtimenosword.big_lunge").withStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.YELLOW)))));
        bigLunge.setValue(ClientConfig.BIG_LUNGE.get());
        bigLunge.setResponder(v->dirty=true);
        this.addRenderableWidget(bigLunge);
        this.addRenderableWidget(
                Button.builder(Component.literal("▶"),
                                btn -> playSound(bigLunge.getValue()))
                        .bounds(col4+fieldWidth, 160, 20, 20)
                        .tooltip(Tooltip.create(Component.translatable("config.longtimenosword.play_sound").withStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.YELLOW)))))
                        .build()
        );

        riptideSound = new SoundBox(this, this.font, leftX, 180, 140, 20, Component.translatable("config.longtimenosword.big_lunge"));
        riptideSound.setMaxLength(128);
        riptideSound.setHint(Component.literal("minecraft:entity.player.attack.sweep")
                .withStyle(ChatFormatting.DARK_GRAY));
        riptideSound.setTooltip(Tooltip.create(Component.translatable("tooltip.longtimenosword.riptide_sound").withStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.YELLOW)))));
        riptideSound.setValue(ClientConfig.RIPTIDE_SOUND.get());
        riptideSound.setResponder(v->dirty=true);
        this.addRenderableWidget(riptideSound);
        this.addRenderableWidget(
                Button.builder(Component.literal("▶"),
                                btn -> playSound(riptideSound.getValue()))
                        .bounds(leftX-20, 180, 20, 20)
                        .tooltip(Tooltip.create(Component.translatable("config.longtimenosword.riptide_sound").withStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.YELLOW)))))
                        .build()
        );

        this.addRenderableWidget(Button.builder(
                Component.translatable("gui.done"),
                btn -> this.onClose()
        ).bounds(centerX-100, this.height-20, 200, 20).build());
    }

    private void cycleAnimation(){
        String current = ClientConfig.CHARGE_ANIMATION.get();
        String next = switch (current.toLowerCase()) {
            case "block" -> "none";
            case "none" -> "drink";
            case "drink" -> "custom";
            case "custom" -> "bow";
            case "bow" -> "brush";
            case "brush" -> "crossbow";
            case "crossbow" -> "eat";
            case "eat" -> "trident";
            case "trident" -> "spyglass";
            case "spyglass" -> "horn";
            case "horn" -> "block";
            default -> "block";
        };
        ClientConfig.CHARGE_ANIMATION.set(next);
    }

    private void cycleScaling(){
        String current = ClientConfig.CHARGE_BAR_SCALING.get();
        String next = switch (current.toLowerCase()) {
            case "linear" -> "quadratic";
            case "quadratic" -> "cubic";
            case "cubic" -> "sqrt";
            case "sqrt" -> "quadratic_ease_out";
            case "quadratic_ease_out" -> "ease_in_out";
            case "ease_in_out" -> "sine";
            case "sine" -> "elastic";
            case "elastic" -> "linear";
            default -> "quadratic";
        };
        ClientConfig.CHARGE_BAR_SCALING.set(next);
    }

    private void cycleSwingSound(){
        String current = ClientConfig.PLAY_SWING_SOUND.get();
        String next = switch (current.toLowerCase()) {
            case "vanilla" -> "always";
            case "always" -> "never";
            case "never" -> "vanilla";
            default -> "vanilla";
        };
        ClientConfig.PLAY_SWING_SOUND.set(next);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(g);
        g.drawCenteredString(this.font, this.title, this.width/2, 10, 0xFFFFFF);
        super.render(g, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        try{
            dirty=false;
            int min = Integer.parseInt(minCharge.getValue());
            min = Math.max(0, min);
            if(min!=Config.LUNGE_MIN_HOLD.get()){Config.LUNGE_MIN_HOLD.set(min); dirty=true;}

            int max = Integer.parseInt(maxCharge.getValue());
            max = Math.max(min, max);
            if(max!=Config.LUNGE_MAX_HOLD.get()){Config.LUNGE_MAX_HOLD.set(max); dirty=true;}

            int lungeCooldown = Integer.parseInt(lungeCD.getValue());
            lungeCooldown = Math.max(0, lungeCooldown);
            if(lungeCooldown!=Config.LUNGE_CD.get()){Config.LUNGE_CD.set(lungeCooldown); dirty=true;}

            int riptideCooldown = Integer.parseInt(riptideCD.getValue());
            riptideCooldown = Math.max(0, riptideCooldown);
            if(riptideCooldown!=Config.RIPTIDE_CD.get()){Config.RIPTIDE_CD.set(riptideCooldown); dirty=true;}

            double lungeExhaustion = Double.parseDouble(lungeEx.getValue());
            lungeExhaustion = Math.max(0, lungeExhaustion);
            if(lungeExhaustion!=Config.LUNGE_FOOD_EXHAUSTION.get()){Config.LUNGE_FOOD_EXHAUSTION.set(lungeExhaustion); dirty=true;}

            int riptideTh = Integer.parseInt(riptideThreshold.getValue());
            riptideTh = Math.max(min, riptideTh);
            if(riptideTh!=Config.RIPTIDE_THRESHOLD.get()){Config.RIPTIDE_THRESHOLD.set(riptideTh); dirty=true;}

            double lungeSpeedMod = Double.parseDouble(speedMod.getValue());
            lungeSpeedMod = Math.max(0, lungeSpeedMod);
            if(lungeSpeedMod!=Config.LUNGE_SPEED_MOD.get()){Config.LUNGE_SPEED_MOD.set(lungeSpeedMod); dirty=true;}

            double lungeSpeedCap = Double.parseDouble(speedCap.getValue());
            lungeSpeedCap = Math.max(0, lungeSpeedCap);
            if(lungeSpeedCap!=Config.LUNGE_SPEED_CAP.get()){Config.LUNGE_SPEED_CAP.set(lungeSpeedCap); dirty=true;}

            double lungeJumpMod = Double.parseDouble(jumpMod.getValue());
            lungeJumpMod = Math.max(0, lungeJumpMod);
            if(lungeJumpMod!=Config.LUNGE_JUMP_MOD.get()){Config.LUNGE_JUMP_MOD.set(lungeJumpMod); dirty=true;}

            double lungeJumpCap = Double.parseDouble(jumpCap.getValue());
            lungeJumpCap = Math.max(0, lungeJumpCap);
            if(lungeJumpCap!=Config.LUNGE_JUMP_CAP.get()){Config.LUNGE_JUMP_CAP.set(lungeJumpCap); dirty=true;}

            if(!swingSound.getValue().equals(ClientConfig.SWING_SOUND.get())){
                if(ResourceLocation.tryParse(swingSound.getValue())!=null){ClientConfig.SWING_SOUND.set(swingSound.getValue()); dirty=true;}
            }
            if(!smallLunge.getValue().equals(ClientConfig.SMALL_LUNGE.get())){
                if(ResourceLocation.tryParse(smallLunge.getValue())!=null){ClientConfig.SMALL_LUNGE.set(smallLunge.getValue()); dirty=true;}
            }
            if(!bigLunge.getValue().equals(ClientConfig.BIG_LUNGE.get())){
                if(ResourceLocation.tryParse(bigLunge.getValue())!=null){ClientConfig.BIG_LUNGE.set(bigLunge.getValue()); dirty=true;}
            }
            if(!riptideSound.getValue().equals(ClientConfig.RIPTIDE_SOUND.get())){
                if(ResourceLocation.tryParse(riptideSound.getValue())!=null){ClientConfig.RIPTIDE_SOUND.set(bigLunge.getValue()); dirty=true;}
            }
        }catch(NumberFormatException ex){}
        if(dirty){
            ClientConfig.SPEC.save();
            Config.SPEC.save();
        }
        this.minecraft.setScreen(parent);
    }

    private void playSound(String id){
        ResourceLocation rl = ResourceLocation.tryParse(id);
        if(rl == null) return;
        SoundEvent sound = BuiltInRegistries.SOUND_EVENT.get(rl);
        if(sound != null){
            minecraft.getSoundManager().play(
                    SimpleSoundInstance.forUI(sound, 1.0F)
            );
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if(soundBox!=null && soundBox instanceof SoundBox sb && sb.isShowingSuggestions() && sb.isMouseOver(mouseX, mouseY)) {
            sb.scroll(delta);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    public void setActiveSoundBox(SoundBox box){this.soundBox = box;}

}
