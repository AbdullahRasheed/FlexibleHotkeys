package me.dedose.hotkeys.gui;

import me.dedose.hotkeys.HotkeyMod;
import me.dedose.hotkeys.hotkeys.HotkeyManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;

public class GuiScreen extends net.minecraft.client.gui.GuiScreen {

    //Minecraft.getMinecraft().displayGuiScreen(new GuiScreen());
    //Minecraft.getMinecraft().displayGuiScreen(null);

    GuiButton cancelButton;

    final int CANCEL_BUTTON = 0;
    public static String title = "Rebind Key";

    public static boolean rebinding = false;
    public static boolean rebinding1 = false;
    public static int key1 = -1;
    public static int key2 = -1;

    private int tick = 0;
    private int beginningTick;
    private boolean rising = false;

    int rWidth = 100;
    int rHeight = 100;
    int bWidth = 100;
    int bHeight = 30;

    int xOffset;
    int yOffset;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int baseOffset = fontRendererObj.getStringWidth("Rebind key");
        int difference = fontRendererObj.getStringWidth(title) - baseOffset;
        rWidth = 100 + difference;

        xOffset = (rWidth/2);
        yOffset = (rHeight/2);

        drawRect((width/2) - xOffset, (height/2) - yOffset, (width/2) + xOffset, (height/2) + yOffset, 0xB3000000);
        if(mouseOver(mouseX, mouseY, (width/2) - xOffset, (height/2) - yOffset, rWidth, rHeight)){
            if(!rising){
                rising = true;
                beginningTick = tick;
            }
            double currentTick = (tick - beginningTick) * 0.01;
            if(currentTick >= 1) currentTick = 1;
            drawRect((width/2) - xOffset, ((height/2) + yOffset) - ((int)(rHeight * currentTick)), (width/2) + xOffset, (height/2) + yOffset, 0xB3FFFFFF);
        }else{
            rising = false;
        }
        drawString(fontRendererObj, title, (width/2) - fontRendererObj.getStringWidth(title)/2, (height/2), 0xd91e18);

        drawRect((width/2) - (bWidth/2), (height/2) + yOffset + 30, (width/2) + (bWidth/2), ((height/2) + yOffset + 30) + bHeight, 0xB3000000);
        if (mouseOver(mouseX, mouseY, (width / 2) - (bWidth / 2), (height / 2) + yOffset + 30, bWidth, bHeight)) {
            drawRect((width/2) - (bWidth/2), (height/2) + yOffset + 30, (width/2) + (bWidth/2), ((height/2) + yOffset + 30) + bHeight, 0xB3FFFFFF);
        }
        drawString(fontRendererObj, "Cancel", (width/2) - fontRendererObj.getStringWidth("Cancel")/2, (height/2) + yOffset + 30 + ((bHeight/2) - 5), 0xd91e18);
        tick += 3;
    }

    private boolean mouseOver(int mx, int my, int x, int y, int width, int height){
        if(mx >= x && mx <= x + width)
            if(my >= y && my <= y + height)
                return true;
        return false;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void onGuiClosed() {
        rebinding = false;
        rebinding1 = false;
        key1 = -1;
        key2 = -1;
        title = "Rebind Key";
    }

    @Override
    public void handleKeyboardInput() throws IOException {
        if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
            mc.thePlayer.closeScreen();
            return;
        }

        Map<String, Integer> keyMap = keyMap();
        int key = -1;
        for (String s : keyMap.keySet()) {
            int currentKey = keyMap.get(s);
            if(Keyboard.isKeyDown(currentKey)) key = currentKey;
        }
        if(key == -1) return;
        System.out.println(2);

        if(GuiScreen.rebinding){
            System.out.println(3);
            GuiScreen.key1 = key;
            GuiScreen.rebinding = false;
            GuiScreen.rebinding1 = true;
            GuiScreen.title = "Press the key to bind to";
            return;
        }else if(GuiScreen.rebinding1){
            System.out.println(4);
            GuiScreen.key2 = key;
            GuiScreen.rebinding1 = false;
            HotkeyManager.registerKeybind(GuiScreen.key1, GuiScreen.key2);
            GuiScreen.title = HotkeyManager.keyToString(GuiScreen.key1) + " binded with " + HotkeyManager.keyToString(GuiScreen.key2);
            HotkeyMod.syncFile();
            return;
        }
    }

    public Map<String, Integer> keyMap(){
        try {
            Class keyboardClass = Keyboard.class;
            Constructor[] constructors = keyboardClass.getDeclaredConstructors();
            constructors[0].setAccessible(true);
            Object o = constructors[0].newInstance();
            Keyboard keyboard = (Keyboard)o;
            constructors[0].setAccessible(false);

            Field keyMapField = Keyboard.class.getDeclaredField("keyMap");
            keyMapField.setAccessible(true);
            Map<String, Integer> keyMap = (Map<String, Integer>) keyMapField.get(keyboard);
            keyMapField.setAccessible(false);
            return keyMap;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if(mouseOver(mouseX, mouseY, (width/2) - xOffset, (height/2) - yOffset, rWidth, rHeight)){
            if(!rebinding && !rebinding1) {
                title = "Press the first key";
                rebinding = true;
                return;
            }else return;
        }
        if (mouseOver(mouseX, mouseY, (width / 2) - (bWidth / 2), (height / 2) + yOffset + 30, bWidth, bHeight)){
            mc.thePlayer.closeScreen();
            return;
        }
    }

    @Override
    public void initGui() {
        super.initGui();
    }
}
