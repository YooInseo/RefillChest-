
package refillchestplus.refillchestplus;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import refillchestplus.refillchestplus.cmd.cmd;
import refillchestplus.refillchestplus.data.ConfigManager;
import refillchestplus.refillchestplus.data.Util;
import java.util.List;

public final class RefillChestPlus extends JavaPlugin {

    public static ConfigManager chestconfig = new ConfigManager("config");
    public static RefillChestPlus plugin;

    public void onEnable() {
        chestconfig = new ConfigManager("config");
        getCommand("refill").setExecutor(new cmd());
        plugin = this;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {

                List<String> name = (List<String>) chestconfig.getConfig().getList( "names");
                if(name != null){
                    for(String names : name){
                        Util.setCoolTime(names);
                    }
                }
            }
        },1,20);
    }
}
