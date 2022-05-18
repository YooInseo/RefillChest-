
package refillchestplus.refillchestplus;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import refillchestplus.refillchestplus.cmd.cmd;
import refillchestplus.refillchestplus.data.ConfigManager;
import refillchestplus.refillchestplus.data.Util;

import java.util.ArrayList;
import java.util.HashMap;
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
                List<String> names = (List<String>) chestconfig.getConfig().getList("names");
                String name = "";
                if (names != null) {
                    int i = 0;

                    CHEST:
                    while (i < names.size()) {
                        i++;
                        name = names.get(i - 1);

                        List<ItemStack> itemsdata = (List<ItemStack>) RefillChestPlus.chestconfig.getConfig().getList(name + ".items");

                        int itemindex = 0;

                        List<Location> locs = (List<Location>) RefillChestPlus.chestconfig.getConfig().getList(name + ".location");
                        int locindex = 0;

                        List<Integer> maxes = (List<Integer>) RefillChestPlus.chestconfig.getConfig().getList(name + ".max");
                        int maxindex = 0;

                        HashMap<String, ItemStack> sure = new HashMap<>();
                        int sureindex = 0;


                        LOCATION:
                        while (locindex < locs.size()) {
                            locindex++;
                            Location loc = locs.get(locindex - 1);
                            Block block = loc.getBlock();
                            Chest chest = (Chest) block.getState();
                            Inventory inv = chest.getInventory();
                            if (Util.getCoolTime(name, locindex - 1) != -1) {
                                if (Util.getCoolTime(name, locindex - 1) != 0) {
                                    int cooltime = Util.getCoolTime(name, locindex - 1);
                                    cooltime--;
                                    Util.SetCoolTime(name, locindex - 1, cooltime);
                                    RefillChestPlus.chestconfig.saveConfig();
                                } else { // When Cooltime come to Zero
                                    while (maxindex < maxes.size()) {
                                        maxindex++;

                                    }

                                    int max = maxes.get(maxindex - 1);
                                    System.out.print(name + "최대 갯수 " + max + (maxes.get(maxindex - 1) < inv.getContents().length));
                                    inv.clear();
                                    while (itemindex < itemsdata.size()) {
                                        itemindex++;
                                        ItemStack item = itemsdata.get(itemindex - 1);
                                        if (Util.GetChance(item) == 100) {
                                            inv.setItem(Util.Random(inv.getSize(), 0), item);
                                            if (maxes.get(maxindex - 1) < inv.getContents().length) {

                                            }
                                        }
                                        if (sure.size() != 0) {
                                            while (sureindex < sure.size()) {
                                                sureindex++;

                                            }
                                        }

                                    }

                                    inv.clear();
                                    ItemStack item = itemsdata.get(itemindex - 1);

                                    if (sure.get(name) != null) {
                                        System.out.print("선택 된 아이템: " + sure.get(name) + "");
                                    }
                                    System.out.print(name + "items : " + item);


//                                    if (Util.Chance(item)) {
//                                        System.out.print(name + "items : " + item);
//                                        inv.setItem(Util.Random(inv.getSize(), 0), item);
//                                    } else {
//                                        System.out.print(Util.GetChance(item) + " 확률을 뚫지 못했습니다!");
//                                    }


//                                    while (maxindex < maxes.size()) {
//                                        maxindex++;
//
//                                        while (itemindex < itemsdata.size()) {
//                                            itemindex++;
//                                            if (maxes.get(maxindex - 1) != -1) {
//                                                ItemStack item = itemsdata.get(itemindex - 1);
//
//                                                inv.addItem(item);
//                                            }
//                                            System.out.print(name + " : " + locs.get(locindex - 1));
//                                            continue LOCATION;
//                                        }
//                                    }


                                    Util.SetCoolTime(name, locindex - 1, Util.getOCoolTime(name, locindex - 1)); //Reset the cooltime to original cooltime


                                }
                                System.out.print(name + " cooltime :  " + Util.getCoolTime(name, locindex - 1));
                                System.out.print(name + " Location : " + locs.get(locindex - 1));
                            }

                            continue CHEST;
                        }

                    }
                }
            }
        }, 1, 20);
    }
}
