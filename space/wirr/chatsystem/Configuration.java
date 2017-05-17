package space.wirr.chatsystem;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

/**
 * This project "ChatSystem" was created by Logan Miller (WiRR) on 4/14/2017.
 */
public class Configuration {
    private final String fileName;
    private final JavaPlugin plugin;
    private File configFile;
    private FileConfiguration fileConfiguration;

    @SuppressWarnings("deprecation")
    public Configuration(final String fileName, final JavaPlugin plugin) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }

        if (!plugin.isEnabled()) throw new IllegalArgumentException("Plugin must be initiaized");

        this.plugin = plugin;
        this.fileName = fileName;
        final File dataFolder = plugin.getDataFolder();
        if (dataFolder == null) {
            throw new IllegalStateException();
        }
        this.configFile = new File(plugin.getDataFolder(), fileName);
        this.saveDefaultYaml();
    }

    @Deprecated
    public void reloadConfig() {
        this.fileConfiguration = YamlConfiguration.loadConfiguration(this.configFile);
        final InputStream defConfigStream = this.plugin.getResource(this.fileName);
        if (defConfigStream != null) {
            final YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            this.fileConfiguration.setDefaults(defConfig);
        }
    }

    public void reloadYaml() {
        this.fileConfiguration = YamlConfiguration.loadConfiguration(this.configFile);
        final InputStream defConfigStream = this.plugin.getResource(this.fileName);
        if (defConfigStream != null) {
            final YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            this.fileConfiguration.setDefaults(defConfig);
        }
    }

    public FileConfiguration getYaml() {
        if (this.fileConfiguration == null) {
            this.reloadYaml();
        }
        return this.fileConfiguration;
    }

    public void saveYaml() {
        if (this.fileConfiguration == null || this.configFile == null) {
            return;
        }
        try {
            this.getYaml().save(this.configFile);
        } catch (IOException ex) {
            this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, ex);
        }
    }

    @Deprecated
    public void saveDefaultConfig() {
        if (!this.configFile.exists()) {
            this.plugin.saveResource(this.fileName, false);
        }
    }

    public void saveDefaultYaml() {
        if (!this.configFile.exists()) {
            this.plugin.saveResource(this.fileName, false);
        }
    }

    public void modifyYaml() {
        this.saveYaml();
        this.reloadYaml();
    }
}
