package refillchestplus.refillchestplus.cmd;


import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import refillchestplus.refillchestplus.RefillChestPlus;
import refillchestplus.refillchestplus.data.Util;

import java.util.List;

public class cmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        String name = null;

        List<Location> names;

        if (args.length != 0) {
            switch (args[0]) {
                case "add":
                    name = args[1];
                    names = (List<Location>) RefillChestPlus.chestconfig.getConfig().getList(name + ".location");
                    NBTItem nbtItem = new NBTItem(player.getItemInHand());

                    ItemStack item = nbtItem.getItem();

                    NBTCompound itemdata = nbtItem.addCompound("ItemMeta");
                    List<Integer> doubles = itemdata.getIntegerList("Chance");
                    doubles.add(-1);
                    nbtItem.applyNBT(item);

                    if (RefillChestPlus.chestconfig.addObject(name + ".items", item)) {
                        MaterialData data = item.getData();

                        player.sendMessage("§a성공적으로 " + item.getType().toString() + " 아이템을 " + data.getItemType().toString() + " 상자에 추가 하였습니다! "  + names.size() + " 째 상자입니다!");
                    } else {
                        player.sendMessage("§c해당 아이템은 이미 등록 되었습니다");
                    }
                    break;
                case "chest":
                    name = args[1];
                    names = (List<Location>) RefillChestPlus.chestconfig.getConfig().getList(name + ".location");

                    Block block = player.getTargetBlock(null, 100);
                    if (block.getType().equals(Material.CHEST)) {
                        RefillChestPlus.chestconfig.addObject("names", name);
                        RefillChestPlus.chestconfig.addObject(name + ".cooltimes", -1);
                        RefillChestPlus.chestconfig.addObject(name + ".ocooltimes", -1);
                        RefillChestPlus.chestconfig.addObject(name + ".max", -1);
                        RefillChestPlus.chestconfig.addObject(name + ".items", null);
                        RefillChestPlus.chestconfig.addObject(name + ".location", block.getLocation());

                        player.sendMessage("성공적으로 상자를 리스트에 추가 하였습니다 " + names.size() + " 째의 상자입니다!");
                    } else {
                        player.sendMessage("§c상자 바로 앞으로 가주세요!");
                    }
                    break;
                case "create":

                    name = args[1];
                    RefillChestPlus.chestconfig.newArrayList(name);
                    RefillChestPlus.chestconfig.newArrayList(name + ".max");
                    RefillChestPlus.chestconfig.newArrayList(name + ".location");
                    RefillChestPlus.chestconfig.newArrayList(name + ".cooltimes");
                    RefillChestPlus.chestconfig.newArrayList(name + ".ocooltimes");
                    RefillChestPlus.chestconfig.newArrayList(name + ".items");


                    break;

                case "remove":
                    name = args[1];

                    RefillChestPlus.chestconfig.removeObject("","");

                    break;
                case "name":
                    RefillChestPlus.chestconfig.newArrayList("names");
                    break;

                case "chance":
                    name = args[1];

                    Util.SetChance(player, name, Integer.valueOf(args[2]), Integer.valueOf(args[3]));

                    break;
                case "list":
                    name = args[1];

                    Util.GetList(name, player);
                    break;
                case "cooltime":
                    name = args[1];

                    Util.SetCoolTime(player, name, Integer.valueOf(args[2]), Integer.valueOf(args[3]));
                    break;
                case "max":
                    name = args[1];

                    Util.SetMax(player, name, Integer.valueOf(args[2]), Integer.valueOf(args[3]));
                    break;
            }
        } else {
            sendMessage(player);
        }
        return false;
    }

    public boolean args(Player player, String[] args, int index) {
        if (args.length == index) {
            return true;
        } else {
            player.sendMessage(args);
            sendMessage(player);
            return false;
        }
    }

    public void sendMessage(Player player) {
        if (player.isOp()) {
            player.sendMessage("§b순서에 맞게 명령어 목록을 적어 뒀습니다. §7회색색깔§b로 되어있는건 별로 중요하지 않다는 뜻 입니다.");
            player.sendMessage("0. §a/refill name <이름> - 등록된 이름을 초기화 & 생성 합니다. ");
            player.sendMessage("    §c초기에 한번만 쳐 주시고 그 뒤로 안치셔도 됩니다,");
            player.sendMessage("1. §a/refill create <이름> - 새로운 리스트를 생성합니다.");
            player.sendMessage("4. §a/refill chest <이름>- 보고 있는 상자를 추가합니다,");
            player.sendMessage("5. §a/refill add <이름> - 손에 들고 있는 아이템을 보고있는 상자 추가 합니다.");
            player.sendMessage("6. §a/refill max <이름> <index> <max>- 해당 이름의 index번호에 상자에 아이템 최대 등장 개수를 설정합니다.");
            player.sendMessage("7. §7/refill cooltime <이름> <index> <cooltime> - 상자의 쿨타임을 설정합니다.");
            player.sendMessage("8. §7/refill chance <이름> <index> <Chance>- 아이템의 확률을 설정합니다.");
            player.sendMessage("9. §7/refill list <이름>- 아이템 확률의 리스트를 봅니다.");
        }
    }
}
