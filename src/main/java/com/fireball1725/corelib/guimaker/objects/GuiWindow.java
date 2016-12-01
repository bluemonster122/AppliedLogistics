package com.fireball1725.corelib.guimaker.objects;

import net.minecraft.client.gui.inventory.GuiContainer;

public class GuiWindow extends GuiObject {
    public GuiWindow(int x, int y, int w, int h) {
        super(-999);
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    @Override
    public void drawGuiContainerBackgroundLayer(GuiContainer guiContainer, float partialTicks, int mouseX, int mouseY) {
        if (!visible)
            return;

        drawSlot(this.x, this.y, this.w, this.h);
    }
}