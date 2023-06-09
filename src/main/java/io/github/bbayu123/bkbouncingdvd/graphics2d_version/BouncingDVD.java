/*
 * **********************************************************************
 * Copyright (C) 2023 Cyrus Mian Xi Li (bbayu/bbayu123)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 * **********************************************************************
 */
package io.github.bbayu123.bkbouncingdvd.graphics2d_version;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.Random;

import com.bergerkiller.bukkit.common.map.MapBlendMode;
import com.bergerkiller.bukkit.common.map.MapDisplay;
import com.bergerkiller.bukkit.common.map.MapDisplayProperties;
import com.bergerkiller.bukkit.common.map.MapSessionMode;
import com.bergerkiller.bukkit.common.map.MapTexture;

import io.github.bbayu123.bkbouncingdvd.Main;

/**
 * This is the main driver class for the bouncing DVD
 * <p>
 * The implementation of this class mainly use MapCanvas draw operations.
 *
 * @author Cyrus Mian Xi Li (bbayu/bbayu123)
 *
 */
public class BouncingDVD extends MapDisplay {
	/**
	 * The fixed movement speed
	 */
	private static final int MOVEMENT_SPEED = 2;

	/**
	 * The random number generator
	 */
	private final Random random = new Random();

	/**
	 * Holds the DVD logo image
	 */
	private BufferedImage logoImage = null;

	/**
	 * The current X position
	 */
	private int positionX = 0;
	/**
	 * The current Y position
	 */
	private int positionY = 0;
	/**
	 * The current color used
	 */
	private Color color = Color.WHITE;

	/**
	 * The current horizontal speed
	 */
	private int horizontalSpeed = 0;
	/**
	 * The current vertical speed
	 */
	private int verticalSpeed = 0;

	/**
	 * {@inheritDoc}
	 * <p>
	 * We use this method to initialize a {@code MapDisplay}.
	 * <p>
	 * <b>We do not use the constructor to initialize a {@code MapDisplay}.</b>
	 * <p>
	 * If we have passed properties to the display, we use the {@code properties}
	 * object and call {@link MapDisplayProperties#get(String, Class) get(String,
	 * Class)} to re-call them.
	 * <p>
	 * This method only sets up the behavior of the display. We use a separate
	 * method to handle the content of the display.
	 *
	 * @see {@link MapDisplay#properties} for more information about the properties
	 *      object
	 * @see {@link MapDisplayProperties#get(String, Class)} to get properties that
	 *      were stored
	 * @see {@link #reload()} for more information on how the content is handled
	 */
	@Override
	public void onAttached() {
		this.setGlobal(true);
		this.setUpdateWithoutViewers(false);
		this.setSessionMode(MapSessionMode.ONLINE);
		this.setMasterVolume(0.3f);
		this.getLayer().setBlendMode(MapBlendMode.NONE);

		this.horizontalSpeed = BouncingDVD.MOVEMENT_SPEED;
		this.verticalSpeed = BouncingDVD.MOVEMENT_SPEED;

		this.logoImage = Main.getDVDLogoImage();
		this.positionX = (this.getWidth() - this.logoImage.getWidth()) / 3;
		this.positionY = (this.getHeight() - this.logoImage.getHeight()) / 2;
		this.color = Color.WHITE;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * We use this method to draw the graphics, as well as check for collision with
	 * the walls, and update position and color of the logo.
	 */
	@Override
	public void onTick() {
		this.drawIcon();

		this.positionX += this.horizontalSpeed;
		this.positionY += this.verticalSpeed;

		this.checkWalls();
	}

	private void drawIcon() {
		BufferedImage master = Main.getDefaultGraphicsConfiguration().createCompatibleImage(this.getWidth(), this.getHeight(),
				Transparency.BITMASK);
		Graphics2D g = master.createGraphics();

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, master.getWidth(), master.getHeight());

		VolatileImage tinted = Main.getDefaultGraphicsConfiguration()
				.createCompatibleVolatileImage(this.logoImage.getWidth(), this.logoImage.getHeight(), Transparency.BITMASK);

		Graphics2D g2 = tinted.createGraphics();
		g2.setBackground(new Color(1, 1, 1, 0));
		g2.clearRect(0, 0, tinted.getWidth(), tinted.getHeight());
		g2.drawImage(this.logoImage, 0, 0, null);
		g2.setComposite(AlphaComposite.SrcAtop);
		g2.setColor(this.color);
		g2.fillRect(0, 0, tinted.getWidth(), tinted.getHeight());
		g2.dispose();

		g.drawImage(tinted, this.positionX, this.positionY, null);
		g.dispose();

		this.getLayer(0).draw(MapTexture.fromImage(master), 0, 0);
	}

	private void checkWalls() {
		if (this.horizontalSpeed > 0 && this.positionX + this.logoImage.getWidth() >= this.getWidth() - 1) {
			// Right edge
			this.horizontalSpeed = -BouncingDVD.MOVEMENT_SPEED;
			this.updateColor();
		}
		if (this.horizontalSpeed < 0 && this.positionX <= 0) {
			// Left edge
			this.horizontalSpeed = BouncingDVD.MOVEMENT_SPEED;
			this.updateColor();
		}

		if (this.verticalSpeed > 0 && this.positionY + this.logoImage.getHeight() >= this.getHeight() - 1) {
			// Bottom edge
			this.verticalSpeed = -BouncingDVD.MOVEMENT_SPEED;
			this.updateColor();
		}
		if (this.verticalSpeed < 0 && this.positionY <= 0) {
			// Top edge
			this.verticalSpeed = BouncingDVD.MOVEMENT_SPEED;
			this.updateColor();
		}
	}

	private void updateColor() {
		// Generate RGB between $50 and $FF inclusive
		int r = this.random.nextInt(0xFF + 1 - 0x50) + 0x50;
		int g = this.random.nextInt(0xFF + 1 - 0x50) + 0x50;
		int b = this.random.nextInt(0xFF + 1 - 0x50) + 0x50;
		this.color = new Color(r, g, b);
	}
}
