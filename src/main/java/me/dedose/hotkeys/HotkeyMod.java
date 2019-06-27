package me.dedose.hotkeys;

import me.dedose.hotkeys.command.RebindCommand;
import me.dedose.hotkeys.hotkeys.HotkeyListener;
import me.dedose.hotkeys.hotkeys.HotkeyManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Mod(modid = Reference.MOD_ID,
     name = Reference.MOD_NAME,
     version = Reference.VERSION)
public class HotkeyMod {

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        File dir = new File(Minecraft.getMinecraft().mcDataDir + "\\flexible_hotkeys");
        File f = new File(dir, "rebindings.txt");
        try{
            if(dir.mkdir()){
                System.out.println("Directory " + dir.getAbsolutePath() + " was created");
            }else{
                System.out.println("Found directory: " + dir.getAbsolutePath());
            }
            if(f.createNewFile()){
                System.out.println("Config file created successfully");
            }else{
                System.out.println("Found config! Loading data...");
                syncMap();
                System.out.println("Data loaded successfully");
            }
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("An error occured while creating the config file!");
        }
        MinecraftForge.EVENT_BUS.register(new HotkeyListener());
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event){
        event.registerServerCommand(new RebindCommand());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){

    }

    public static void syncMap(){
        List<String> bindings = new ArrayList<String>();

        try{
            BufferedReader input = new BufferedReader(new FileReader(Minecraft.getMinecraft().mcDataDir + "\\flexible_hotkeys\\rebindings.txt"));
            String s;
            while ((s = input.readLine()) != null)
                bindings.add(s);
            input.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        try {
            for (String binding : bindings) {
                int key1 = Integer.parseInt(binding.split(":")[0]);
                int key2 = Integer.parseInt(binding.split(":")[1]);
                HotkeyManager.registerKeybind(key1, key2);
            }
        }catch (NumberFormatException e){
            System.out.println("*");
            System.out.println("*");
            System.out.println("*");
            System.out.println("*");
            System.out.println("[FLEXIBLE HOTKEYS]: Your config is reading strings instead of numbers!");
            System.out.println("*");
            System.out.println("*");
            System.out.println("*");
            System.out.println("*");
        }
    }

    public static void syncFile(){
        BufferedWriter writer = null;
        try{
            clearFile();
            writer = new BufferedWriter(new FileWriter(Minecraft.getMinecraft().mcDataDir + "\\flexible_hotkeys\\rebindings.txt"));
            for (Integer integer : HotkeyManager.getRebinds().keySet()) {
                writer.write(integer + ":" + HotkeyManager.getRebindedId(integer));
                writer.newLine();
            }
            writer.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void clearFile(){
        try{
            PrintWriter writer = new PrintWriter(Minecraft.getMinecraft().mcDataDir + "\\flexible_hotkeys\\rebindings.txt");
            writer.write("");
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
