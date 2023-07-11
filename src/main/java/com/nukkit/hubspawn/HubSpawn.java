package com.nukkit.hubspawn;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerRespawnEvent;
import cn.nukkit.level.Position;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;

public class HubSpawn extends PluginBase implements Listener {
  private Config config;

  public void onEnable() {
    saveDefaultConfig();
    this.config = getConfig();
    getServer().getPluginManager().registerEvents(this, (Plugin)this);
    getLogger().info("El plugin HubSpawn ha sido habilitado.");
  }

  public void onDisable() {
    saveConfig();
  }

  public boolean onCommand(CommandSender paramCommandSender, Command paramCommand, String paramString, String[] paramArrayOfString) {
    if (paramCommand.getName().equalsIgnoreCase("setspawnhub")) {
      if (!(paramCommandSender instanceof Player)) {
        paramCommandSender.sendMessage("Este comando solo puede ser ejecutado por un jugador.");
        return true;
      }
      Player player = (Player)paramCommandSender;
      double d1 = player.getX();
      double d2 = player.getY();
      double d3 = player.getZ();
      String str = player.getLevel().getName();
      this.config.set("hubspawn.x", Double.valueOf(d1));
      this.config.set("hubspawn.y", Double.valueOf(d2));
      this.config.set("hubspawn.z", Double.valueOf(d3));
      this.config.set("hubspawn.world", str);
      this.config.set("hubspawn.clearInventory", true);
      saveConfig();
      player.sendMessage(String.valueOf(TextFormat.GREEN) + "Las coordenadas del hub se han guardado correctamente.");
      return true;
    }
    if (paramCommand.getName().equalsIgnoreCase("hub")) {
      if (!(paramCommandSender instanceof Player)) {
        paramCommandSender.sendMessage("Este comando solo puede ser ejecutado por un jugador.");
        return true;
      }
      Player player = (Player)paramCommandSender;
      double d1 = this.config.getDouble("hubspawn.x", 0.0D);
      double d2 = this.config.getDouble("hubspawn.y", 0.0D);
      double d3 = this.config.getDouble("hubspawn.z", 0.0D);
      String str = this.config.getString("hubspawn.world", "");
      if (d1 == 0.0D && d2 == 0.0D && d3 == 0.0D) {
        player.sendMessage(String.valueOf(TextFormat.RED) + "Las coordenadas del hub no estconfiguradas.");
        return true;
      }
      if (this.config.getBoolean("hubspawn.clearInventory")) {
        player.getInventory().clearAll();
      }
      player.teleport(new Position(d1, d2, d3, getServer().getLevelByName(str)));
      player.sendMessage(String.valueOf(TextFormat.GREEN) + "has teletransportado al hub!");
      return true;
    }
    return false;
  }
  
  @EventHandler
  public void onPlayerRespawn(PlayerRespawnEvent paramPlayerRespawnEvent) {
    Player player = paramPlayerRespawnEvent.getPlayer();
    double d1 = this.config.getDouble("hubspawn.x", 0.0D);
    double d2 = this.config.getDouble("hubspawn.y", 0.0D);
    double d3 = this.config.getDouble("hubspawn.z", 0.0D);
    String str = this.config.getString("hubspawn.world", "");
    if (d1 == 0.0D && d2 == 0.0D && d3 == 0.0D)
      return; 
    paramPlayerRespawnEvent.setRespawnPosition(new Position(d1, d2, d3, getServer().getLevelByName(str)));
  }
}