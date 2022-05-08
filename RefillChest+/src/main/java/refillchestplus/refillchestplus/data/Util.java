package refillchestplus.refillchestplus.data;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import refillchestplus.refillchestplus.RefillChestPlus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class Util {

    public static int getCoolTime(String name, int index) {
        List<Integer> cooltime = (List<Integer>) RefillChestPlus.chestconfig.getConfig().getList(name + ".cooltimes");
        return cooltime.get(index) == -1 ? -1 : cooltime.get(index);
    }


    public static int getOCoolTime(String name, int index) {
        List<Integer> cooltime = (List<Integer>) RefillChestPlus.chestconfig.getConfig().getList(name + ".ocooltimes");

        return cooltime.get(index) == -1 ? -1 : cooltime.get(index);
    }

    public static void SetCoolTime(String name, int index, int cooltime) {
        List<Integer> cooltimes = (List<Integer>) RefillChestPlus.chestconfig.getConfig().getList(name + ".cooltimes");
        cooltimes.set(index, cooltime);
    }

    public static int Random(int max, int min) {

        Random random = new Random();
        int value = random.nextInt(max + min) + min;
        return value;
    }

    /**
     * name 안에 있는 location을 불러와 해당 위치에 상자로 치환시킴.
     * 상자에 아이템을 설정 시키는데, 위치는 랜덤, 상자를 초기화 후 설정
     * 설정할 때 아이템은 items에 있는 아이템을 불러오고,
     * for문을 통해 불러 올 때 location.indexof 값으로 불러온 max의 개수 만큼 불러온다.
     * 예:) max - 5
     * location - 5,5,5
     * <p>
     * location 5,5,5의 max 값 = 5
     * 해당 5의 변수로 items에서 불러올 max값으로 설정
     *
     * @param name
     */
    public static void setCoolTime(String name) {
        List<Location> locs = (List<Location>) RefillChestPlus.chestconfig.getConfig().getList(name + ".location");
        List<Integer> maxes = (List<Integer>) RefillChestPlus.chestconfig.getConfig().getList(name + ".max");

        for (Location location : locs) {
            Block block = location.getBlock();
            int index = locs.indexOf(location);
            int max = maxes.get(index);

            if (getCoolTime(name, index) != -1) {
                if (getCoolTime(name, index) != 0) {
                    int cooltime = getCoolTime(name, index);
                    cooltime--;
                    SetCoolTime(name, index, cooltime);
                    RefillChestPlus.chestconfig.saveConfig();
                } else {
                    Chest chest = (Chest) block.getState();
                    Inventory inv = chest.getInventory();

                    inv.clear();
                    for (int i = 0; i < max; i++) {
                        int RandomSize = Random(inv.getSize(), 0);

                        for (ItemStack item : Max(name, index)) {
                            if(Chance(item)){

                                inv.setItem(RandomSize, item);
                                System.out.print(item);
                                for (ItemStack items : inv.getContents()) {
                                    if (items != null) {
                                        NBTItem nbtItem = new NBTItem(items);
                                        nbtItem.clearCustomNBT();
                                        nbtItem.applyNBT(items);

                                    }
                                }
                            }
                        }
                    }
                    SetCoolTime(name, index, getOCoolTime(name, index));
                    Bukkit.getConsoleSender().sendMessage(name + " 상자 §b아이템이 리필 되었습니다! " + index);
                    RefillChestPlus.chestconfig.saveConfig();
                }
            }
        }
    }

    /**
     * select = 3;
     * if(Items.get(Random());
     *
     * @param name  Chest name
     * @param index Getting max value from index ex:) maxes.get(index);
     */
    public static List<ItemStack> Max( String name, int index) {
        List<Integer> maxes = (List<Integer>) RefillChestPlus.chestconfig.getConfig().getList(name + ".max");
        List<ItemStack> Items = (List<ItemStack>) RefillChestPlus.chestconfig.getConfig().getList(name + ".items");
        List<ItemStack> selected = new ArrayList<>();

        for (int i = selected.size(); i <= maxes.get(index); i++) {
            ItemStack select = Items.get(Random(Items.size() - 1, 0));
            selected.add(select);
            return selected;
        }
        return null;
    }
    public static void GetList(String name, Player player) {
        List<ItemStack> items = (List<ItemStack>) RefillChestPlus.chestconfig.getConfig().getList(name + ".items");
        for (ItemStack itemStack : items) {
            NBTItem nbtItem = new NBTItem(itemStack);

            itemStack = nbtItem.getItem();
            nbtItem.applyNBT(itemStack);
            NBTCompound itemdata = nbtItem.addCompound("ItemMeta");
            List<Integer> doubles = itemdata.getIntegerList("Chance");
            player.sendMessage("§7index(§f" + items.indexOf(itemStack) + "§7) §b확률: §7" + doubles.get(0) + "% " + " §b타입: §7" + itemStack.getType());
        }
    }

    public static void SetChance(Player player, String name, int index, int Chance) {
        List<ItemStack> items = (List<ItemStack>) RefillChestPlus.chestconfig.getConfig().getList(name + ".items");
        ItemStack itemStack = items.get(index);
        NBTItem nbtItem = new NBTItem(itemStack);
        NBTCompound itemdata = nbtItem.addCompound("ItemMeta");
        List<Integer> doubles = itemdata.getIntegerList("Chance");
        doubles.set(0, Chance);
        nbtItem.applyNBT(itemStack);
        RefillChestPlus.chestconfig.saveConfig();
        player.sendMessage("§a성공적으로 " + itemStack.getType() + " §a아이템의 등장 확률이 §7" + Chance + "% §a로 변경되었습니다.");
    }


    public static int GetChance(ItemStack item) {
        ItemStack itemStack = item;
        NBTItem nbtItem = new NBTItem(itemStack);
        NBTCompound itemdata = nbtItem.addCompound("ItemMeta");
        List<Integer> chance = itemdata.getIntegerList("Chance");

        return chance.get(0);
    }


    public static boolean Chance(ItemStack item) {
        int Random = Random(100, 0);

        return Random <= GetChance(item);
    }


    public static void SetCoolTime(Player player, String name, int index, int CoolTime) {
        List<Integer> cooltimes = (List<Integer>) RefillChestPlus.chestconfig.getConfig().getList(name + ".cooltimes");
        List<Integer> ocooltimes = (List<Integer>) RefillChestPlus.chestconfig.getConfig().getList(name + ".ocooltimes");
        ocooltimes.set(index, CoolTime);
        cooltimes.set(index, CoolTime);
        RefillChestPlus.chestconfig.saveConfig();
        player.sendMessage("§a성공적으로 " + name + " §a아이템의 등장 시간이§7" + CoolTime + "s §a로 변경되었습니다.");
    }

    public static void SetMax(Player player, String name, int index, int Max) {
        List<Integer> max = (List<Integer>) RefillChestPlus.chestconfig.getConfig().getList(name + ".max");
        List<ItemStack> items = (List<ItemStack>) RefillChestPlus.chestconfig.getConfig().getList(name + ".items");
        ItemStack itemStack = items.get(index);
        NBTItem nbtItem = new NBTItem(itemStack);
        NBTCompound itemdata = nbtItem.addCompound("ItemMeta");

        if (Max > items.size()) {
            player.sendMessage("§c해당 범위는 아이템 갯수에 벗어납니다. ");
        } else {
            max.set(index, Max);
            RefillChestPlus.chestconfig.saveConfig();
            player.sendMessage("§a성공적으로 " + name + " §a상자 아이템 최대 종류가 §7" + Max + " §a개로 변경되었습니다.");
        }

    }
}
