package refillchestplus.refillchestplus.data;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import refillchestplus.refillchestplus.RefillChestPlus;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    public FileConfiguration config;
    public File file;
    private String name;

    public ConfigManager(String name){
        this.name = name;

    }
    public void reloadConfig(){
        if(this.config == null)
            this.file = new File(RefillChestPlus.plugin.getDataFolder(), name + ".yml");
        this.config = YamlConfiguration.loadConfiguration(this.file);

        InputStream inputStream = RefillChestPlus.plugin.getResource(name + ".yml");
        if(inputStream != null) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(new InputStreamReader(inputStream));
            this.config.setDefaults(config);
        }
    }

    public FileConfiguration getConfig() {
        if(this.config == null) reloadConfig();
        return config;
    }

    public boolean saveConfig(){
        if(this.config == null || this.file == null) return false;
        try {
            getConfig().save(this.file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void saveDefualtConfig(){
        if(this.file == null)
            this.file = new File(RefillChestPlus.plugin.getDataFolder(), name + ".yml");
        if(!this.file.exists()){
            file = new File(RefillChestPlus.plugin.getDataFolder(), name + ".yml");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean Delete(){
        this.file = new File(RefillChestPlus.plugin.getDataFolder(), name + ".yml");

        return this.file.delete();
    }

    public Object get(String path){
            return this.config.get(path);
    }

    public void removeObject(String path, Object obj ){
        List<Object> list = (List<Object>) this.getConfig().getList(path);
        list.remove(obj);
        this.saveConfig();
    }

    public void Value(String path, Object value){
        this.getConfig().set(path, value);
        this.saveConfig();
    }
    public void newArrayList(String path ){
        this.getConfig().set(path, new ArrayList<>());
        this.saveConfig();
    }

    /***
     *@implSpec {@code This method Get A}
     * <pre>{@code
     *
     *   ConfigManager config;
     *   public void CreatePlayer(Player player){
     *   config = new ConfigManager(player.getUniqueId().toString());
     *   config.newArrayList("player", player);
     *   }

     *   public void addPlayer(Player player){

     *   config.addObject("player", player);
     *   }
     *
     * }</pre>
     * @return If the object is contained in List from path
     */
    public boolean addObject(String path, Object obj ){
        List<Object> list = (List<Object>) this.getConfig().getList(path);
        if(!list.contains(obj) && obj != null){
            list.add(obj);
            this.saveConfig();
            return list.contains(obj);
        }
        return false;
    }
}
