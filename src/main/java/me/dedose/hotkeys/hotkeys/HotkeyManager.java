package me.dedose.hotkeys.hotkeys;

import org.lwjgl.input.Keyboard;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class HotkeyManager {

    private static Map<Integer, Integer> rebinds = new HashMap<Integer, Integer>();

    public static void registerKeybind(int key1, int key2){
        rebinds.put(key1, key2);
    }

    public static int getRebindedId(int key1){
        if(!rebinds.containsKey(key1)) return -1;
        return rebinds.get(key1);
    }

    public static Map<Integer, Integer> getRebinds(){
        return rebinds;
    }

    public static String keyToString(int key){
        return Keyboard.getKeyName(key);
    }

    public static Map<String, Integer> keyMap(){
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
}
