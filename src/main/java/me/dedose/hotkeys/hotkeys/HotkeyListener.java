package me.dedose.hotkeys.hotkeys;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class HotkeyListener {

    @SubscribeEvent
    public void onKeyPressed(InputEvent.KeyInputEvent event){
        if(!Minecraft.getMinecraft().inGameHasFocus) return;

        for (Integer integer : HotkeyManager.getRebinds().keySet()) {
            if(!Keyboard.isKeyDown(integer)) continue;
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            KeyBinding.onTick(HotkeyManager.getRebindedId(integer));
        }
    }
}
