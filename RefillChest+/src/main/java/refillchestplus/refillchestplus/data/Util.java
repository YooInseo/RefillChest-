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
import refillchestplus.refillchestplus.RefillChestPlus;

import java.util.*;


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
        List<ItemStack> itemsdata = (List<ItemStack>) RefillChestPlus.chestconfig.getConfig().getList(name + ".items");
        List<ItemStack> newitems = new ArrayList<>();


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

                    /***
                     *  모든 아이템에서 Max의 개수만큼 골라와야함.
                     *  이 때 모든 아이템중 확률이 100%인 것은 무조건 포함.
                     *
                     *
                     *  100% 인 아이템들은 Sure 리스트에 저장이 되며
                     *  불러올 때 Sure리스트를 우선적으로 불러온다.
                     *  Sure리스트에 값이 없을 경우 items리스트의 값을 불러온다.
                     *  이러면 100%의 문제를 해결 할 수 있음과 동시에
                     *  아이템을 불러올 때, 별도의 아이템정보저장용 리스트를 만들어
                     *  해당 사이즈가 max값보다 작을 때 While문을 돌려
                     *  items리스트에서 랜덤으로 값을 뽑아오면 된다.
                     *
                     */


                    /**
                     * 기존의 for문으로 불러오는 것은 랙을 유발할 가능성이 있어
                     * While로 조건문을 걸어 불러옴.
                     * 근데 이 문제를 RandomInt로만 처리를 할 시 확률이 100%인 것들을
                     * 선택을 안 할 경우의 수가 생김. 이를 방지하기 위해
                     *
                     */



                    while (newitems.size() < max) {
                        int test = GetRandomInt(0, itemsdata.size());
                        newitems.add(itemsdata.get(test));
                        System.out.print(itemsdata.get(test));

//                        if(!newitems.contains(itemsdata.get(test))){
//                            newitems.add(itemsdata.get(test));
//                            System.out.print(itemsdata.get(test) +""  + newitems.size());
//                            newitems.remove(itemsdata.get(test));
//                            break;
//                         } else{
//
//                            System.out.print(itemsdata.get(test) +""  + newitems.size());
//                            newitems.remove(itemsdata.get(test));
//                            continue;
//                        }
                    }


//                    while (newitems.size() < max) {
//                        int test = GetRandomInt(0, itemsdata.size());
//                         if(!newitems.contains(itemsdata.get(test))){
//                            if(Chance(itemsdata.get(test))){
//                                newitems.add(itemsdata.get(test));
//                                continue;
//                            }
//                            break;
//                        }
//                    }


//                    for (ItemStack item : itemsdata) {
//                        int RandomSize = Random(inv.getSize(), 0);
//                        if (newitems.size() < max) {
//
//                            if (Chance(item)) {
//                                newitems.add(item);
//                                inv.setItem(RandomSize, item);
//                                if (inv.getItem(RandomSize) == null) {
//                                    System.out.print("해당 아이템은 존재하지 않습니다!");
//                                }
//                                for (ItemStack items : inv.getContents()) {
//                                    if (items != null) {
//                                        NBTItem nbtItem = new NBTItem(items);
//                                        nbtItem.clearCustomNBT();
//                                        nbtItem.applyNBT(items);
//                                    }
//                                }
//                            }
//                        }
//                        newitems.remove(item);
//                    }
                    SetCoolTime(name, index, getOCoolTime(name, index));
                    System.out.print("§b테스트");
                    RefillChestPlus.chestconfig.saveConfig();
                }
            }
        }
    }

    public static int GetRandomInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    public static int[] Max(int array, int max) {
        Random random = new Random();
        int[] numArray = new int[array];
        for (int i = 0; i < max; i++) {
            numArray[i] = random.nextInt(array) + 1;
            for (int j = 0; j < i; j++) {
                if (numArray[i] == numArray[j]) {
                    i--;
                    break;
                }
            }
        }
        return numArray;
    }

    public static void GetChestList(String name, Player player) {
        List<Location> items = (List<Location>) RefillChestPlus.chestconfig.getConfig().getList(name + ".location");
        List<Integer> maxes = (List<Integer>) RefillChestPlus.chestconfig.getConfig().getList(name + ".max");
        List<Integer> cooltimes = (List<Integer>) RefillChestPlus.chestconfig.getConfig().getList(name + ".cooltimes");

        for (Location location : items) {
            int index = items.indexOf(location);
            player.sendMessage("§7index(§f" + items.indexOf(location) + ") location X " + location.getX() + " Y " + location.getY() + " Z " + location.getZ());
            player.sendMessage("    §e쿨타임 : " + cooltimes.get(index) + " 아이템 등장 갯수 : " + maxes.get(index));
        }
    }

    public static void RemoveChestList(Player player, String name, int index) {
        List<Location> items = (List<Location>) RefillChestPlus.chestconfig.getConfig().getList(name + ".location");
        RefillChestPlus.chestconfig.removeObject(name + ".location", items.get(index));
        player.sendMessage("§a성공적으로 " + index + " 번호의 상자를 지웠습니다!");
        player.sendMessage("§7남은 상자 리스트 ");
        GetChestList(name, player);
    }

    public static void RemoveItemList(Player player, String name, int index) {
        List<ItemStack> items = (List<ItemStack>) RefillChestPlus.chestconfig.getConfig().getList(name + ".items");
        RefillChestPlus.chestconfig.removeObject(name + ".items", items.get(index));
        player.sendMessage("§a성공적으로 " + index + " 번호의 아이템을 지웠습니다!");
        player.sendMessage("§7남은 아이템 리스트 ");
        GetItemList(name, player);
    }

    public static void GetItemList(String name, Player player) {
        List<ItemStack> itemsdata = (List<ItemStack>) RefillChestPlus.chestconfig.getConfig().getList(name + ".items");



        for (ItemStack itemStack : itemsdata) {
            NBTItem nbtItem = new NBTItem(itemStack);

            itemStack = nbtItem.getItem();
            nbtItem.applyNBT(itemStack);
            NBTCompound itemdata = nbtItem.addCompound("ItemMeta");
            List<Integer> doubles = itemdata.getIntegerList("Chance");
            player.sendMessage("§7index(§f" + itemsdata.indexOf(itemStack) + "§7) §b확률: §7" + doubles.get(0) + "% " + " §b타입: §7" + itemStack.getType());
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
