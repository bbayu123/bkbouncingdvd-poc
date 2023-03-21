/* **********************************************************************
 * Copyright (C) 2023 Cyrus Mian Xi Li (bbayu/bbayu123)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 * **********************************************************************
 */
package io.github.bbayu123.bkbouncingdvd;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.bergerkiller.bukkit.common.map.MapDisplay;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.nbt.CommonTagCompound;
import com.bergerkiller.bukkit.common.utils.ItemUtil;

/**
 * This is the main plugin class
 * <p>
 * Contains boiler-plate code for the plugin to work.
 *
 * @author Cyrus Mian Xi Li (bbayu/bbayu123)
 *
 */
public class Main extends JavaPlugin {
	private static BufferedImage dvdLogoImage = null;
	private static MapTexture dvdLogoTexture = null;

	/**
	 * {@inheritDoc}
	 * <p>
	 * What we are doing here is linking the command executor to our plugin, as well
	 * as loading some map textures that we will be using later. See
	 * {@link #loadTexture(String)} as to how the textures are loaded.
	 */
	@Override
	public void onEnable() {
		this.getCommand("bouncingdvd-mc").setExecutor(this);
		this.getCommand("bouncingdvd-mw").setExecutor(this);
		this.getCommand("bouncingdvd-jg").setExecutor(this);

		try (InputStream stream = this.getResource("dvd_logo.png")) {
			Main.dvdLogoImage = ImageIO.read(stream);
			Main.dvdLogoImage.setAccelerationPriority(1);
			Main.dvdLogoTexture = MapTexture.fromImage(Main.dvdLogoImage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Since only players can hold items, we check if it is a player before
	 * continuing.
	 * <p>
	 * When the player does {@code /<command> get}, then we create the map item
	 * using {@link MapDisplay#createMapItem(Class)}, and give this to the player.
	 * <p>
	 * If we need to pass in parameters/properties to the display, we use
	 * {@link ItemUtil#getMetaTag(ItemStack)} and then call
	 * {@link CommonTagCompound#putValue(String, Object) putValue(String, Object)}
	 * on the tag to add properties.
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("You must be a player in order to do this!");
			return true;
		}

		Player player = (Player) sender;

		if (args.length == 0) {
			return false;
		}
		if (args[0].equalsIgnoreCase("get")) {
			Class<? extends MapDisplay> clazz = null;
			String title = null;
			switch (cmd.getName()) {
			case "bouncingdvd-mc":
				clazz = io.github.bbayu123.bkbouncingdvd.mapcanvas_version.BouncingDVD.class;
				title = "Bouncing DVD (MapCanvas)";
				break;
			case "bouncingdvd-mw":
				clazz = io.github.bbayu123.bkbouncingdvd.mapwidget_version.BouncingDVD.class;
				title = "Bouncing DVD (MapWidget)";
				break;
			case "bouncingdvd-jg":
				clazz = io.github.bbayu123.bkbouncingdvd.graphics2d_version.BouncingDVD.class;
				title = "Bouncing DVD (Graphics2D)";
				break;
			default:
				return true;
			}

			ItemStack item = MapDisplay.createMapItem(clazz);
			ItemUtil.getMetaTag(item).putValue("owner", player.getUniqueId());
			ItemUtil.setDisplayName(item, title);
			player.getInventory().addItem(item);
			player.sendMessage(ChatColor.GREEN + "Obtained Bouncing DVD");
		}
		return true;
	}

	/**
	 * Gets the DVD logo texture
	 * <p>
	 * This is the texture that was loaded in {@link #onEnable()}.
	 *
	 * @return the DVD logo texture
	 */
	public static MapTexture getDVDLogoTexture() {
		return Main.dvdLogoTexture;
	}

	/**
	 * Gets the DVD logo image
	 * <p>
	 * This is the image that was loaded in {@link #onEnable()}.
	 *
	 * @return the DVD logo image
	 */
	public static BufferedImage getDVDLogoImage() {
		return Main.dvdLogoImage;
	}

	public static GraphicsConfiguration getDefaultGraphicsConfiguration() {
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
	}
}
