package me.dedose.hotkeys.command;

import me.dedose.hotkeys.gui.GuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class RebindCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "rebind";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        Minecraft.getMinecraft().displayGuiScreen(new GuiScreen());
    }
}
