package com.yor42.projectazure.client.input;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class KeybindBuilder {
    private String description;
    private IKeyConflictContext keyConflictContext = KeyConflictContext.UNIVERSAL;
    private KeyModifier keyModifier = KeyModifier.NONE;
    private InputConstants.Key key;
    private String category = "keybind.projectazure.category";
    private BiConsumer<KeyMapping, Boolean> onKeyDown;
    private Consumer<KeyMapping> onKeyUp;
    private BooleanSupplier toggleable;
    private boolean repeating;
    public KeybindBuilder description(String description) {
        this.description = Objects.requireNonNull(description, "Description cannot be null.");
        return this;
    }

    public KeybindBuilder conflictInGame() {
        return conflictContext(KeyConflictContext.IN_GAME);
    }

    public KeybindBuilder conflictInGui() {
        return conflictContext(KeyConflictContext.GUI);
    }

    public KeybindBuilder conflictContext(IKeyConflictContext keyConflictContext) {
        this.keyConflictContext = Objects.requireNonNull(keyConflictContext, "Key conflict context cannot be null.");
        return this;
    }

    public KeybindBuilder modifier(KeyModifier keyModifier) {
        this.keyModifier = Objects.requireNonNull(keyModifier, "Key modifier cannot be null.");
        return this;
    }

    public KeybindBuilder keyCode(int keyCode) {
        return keyCode(InputConstants.Type.KEYSYM, keyCode);
    }

    public KeybindBuilder keyCode(InputConstants.Type keyType, int keyCode) {
        Objects.requireNonNull(keyType, "Key type cannot be null.");
        return keyCode(keyType.getOrCreate(keyCode));
    }

    public KeybindBuilder keyCode(InputConstants.Key key) {
        this.key = Objects.requireNonNull(key, "Key cannot be null.");
        return this;
    }

    public KeybindBuilder category(String category) {
        this.category = Objects.requireNonNull(category, "Category cannot be null.");
        return this;
    }

    public KeybindBuilder onKeyDown(BiConsumer<KeyMapping, Boolean> onKeyDown) {
        this.onKeyDown = Objects.requireNonNull(onKeyDown, "On key down cannot be null when manually specified.");
        return this;
    }

    public KeybindBuilder onKeyUp(Consumer<KeyMapping> onKeyUp) {
        this.onKeyUp = Objects.requireNonNull(onKeyUp, "On key up cannot be null when manually specified.");
        return this;
    }

    public KeybindBuilder toggleable() {
        return toggleable(() -> true);
    }

    public KeybindBuilder toggleable(BooleanSupplier toggleable) {
        this.toggleable = Objects.requireNonNull(toggleable, "Toggleable supplier cannot be null when manually specified.");
        return this;
    }

    public KeybindBuilder repeating() {
        this.repeating = true;
        return this;
    }

    public KeyMapping build() {

        Keybind keybind = new Keybind(
                Objects.requireNonNull(description, "Description has not been set."),
                keyConflictContext,
                keyModifier,
                Objects.requireNonNull(key, "Key has not been set"),
                category,
                onKeyDown,
                onKeyUp,
                toggleable,
                repeating
        );
        KeybindHandler.KEYS.add(keybind);
        return keybind;
    }
}
