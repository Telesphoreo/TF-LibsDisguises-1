package me.libraryaddict.disguise.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import me.totalfreedom.libsdisguise.DisallowedDisguises;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.libraryaddict.disguise.utilities.DisguiseParser;
import me.libraryaddict.disguise.utilities.DisguiseParser.DisguisePerm;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 * @author libraryaddict
 */
public abstract class DisguiseBaseCommand implements CommandExecutor {

    protected ArrayList<String> filterTabs(ArrayList<String> list, String[] origArgs) {
        if (origArgs.length == 0)
            return list;

        Iterator<String> itel = list.iterator();
        String label = origArgs[origArgs.length - 1].toLowerCase();

        while (itel.hasNext()) {
            String name = itel.next();

            if (name.toLowerCase().startsWith(label))
                continue;

            itel.remove();
        }

        return new ArrayList<>(new HashSet<>(list));
    }

    protected String getDisplayName(CommandSender player) {
        Team team = ((Player) player).getScoreboard().getEntryTeam(player.getName());

        return (team == null ? "" : team.getPrefix()) + player.getName() + (team == null ? "" : team.getSuffix());
    }

    protected ArrayList<String> getAllowedDisguises(
            HashMap<DisguisePerm, HashMap<ArrayList<String>, Boolean>> hashMap) {
        ArrayList<String> allowedDisguises = new ArrayList<>();

        for (DisguisePerm type : hashMap.keySet()) {
            if (type.isUnknown())
                continue;

            if (DisallowedDisguises.isAllowed(type.getType()) && !type.isUnknown())
            {
                allowedDisguises.add(type.toReadable().replaceAll(" ", "_"));
            }
        }

        Collections.sort(allowedDisguises, String.CASE_INSENSITIVE_ORDER);

        return allowedDisguises;
    }

    protected String[] getArgs(String[] args) {
        ArrayList<String> newArgs = new ArrayList<>();

        for (int i = 0; i < args.length - 1; i++) {
            String s = args[i];

            if (s.trim().isEmpty())
                continue;

            newArgs.add(s);
        }

        return newArgs.toArray(new String[0]);
    }

    public final String getPermNode() {
        if (this instanceof DisguiseCommand) {
            return "disguise";
        } else if (this instanceof DisguiseEntityCommand) {
            return "disguiseentity";
        } else if (this instanceof DisguisePlayerCommand) {
            return "disguiseplayer";
        } else if (this instanceof DisguiseRadiusCommand) {
            return "disguiseradius";
        } else if (this instanceof DisguiseModifyCommand) {
            return "disguisemodify";
        } else if (this instanceof DisguiseModifyEntityCommand) {
            return "disguisemodifyentity";
        } else if (this instanceof DisguiseModifyPlayerCommand) {
            return "disguisemodifyplayer";
        } else if (this instanceof DisguiseModifyRadiusCommand) {
            return "disguisemodifyradius";
        } else {
            throw new UnsupportedOperationException("Unknown disguise command, perm node not found");
        }
    }

    protected HashMap<DisguisePerm, HashMap<ArrayList<String>, Boolean>> getPermissions(CommandSender sender) {
        return DisguiseParser.getPermissions(sender, "libsdisguises." + getPermNode() + ".");
    }

    protected boolean isNumeric(String string) {
        try {
            Integer.parseInt(string);
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }

    public boolean passesCheck(CommandSender sender, HashMap<ArrayList<String>, Boolean> theirPermissions,
            ArrayList<String> usedOptions) {
        return DisguiseParser.passesCheck(sender, theirPermissions, usedOptions);
    }

    protected abstract void sendCommandUsage(CommandSender sender,
            HashMap<DisguisePerm, HashMap<ArrayList<String>, Boolean>> map);
}
