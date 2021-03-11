package com.yor42.projectazure.gameobject.items.gun;

import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;

public class ItemGunBase extends Item {

    private boolean isSemisuto;
    private int minFireDelay;
    private int reloadDelay;

    private SoundEvent fireSound, reloadSound;
    private float damage, accuracy;

    private int magCap, roundsPerReload;

    public ItemGunBase(boolean semiAuto, int minFiretime, int clipsize, int reloadtime, float damage, SoundEvent firesound, SoundEvent reloadsound, int projectileLife, float accuracy, Properties properties) {
        super(properties);
    }
}
