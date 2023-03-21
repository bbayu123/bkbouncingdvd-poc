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
package io.github.bbayu123.bkbouncingdvd.mapcanvas_version;

import java.util.Random;

import com.bergerkiller.bukkit.common.map.MapCanvas;
import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapDisplay;
import com.bergerkiller.bukkit.common.map.MapDisplayProperties;
import com.bergerkiller.bukkit.common.map.MapSessionMode;

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
	private MapCanvas logoImage = null;

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
	private byte color = 0;

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

		horizontalSpeed = MOVEMENT_SPEED;
		verticalSpeed = MOVEMENT_SPEED;

		this.getLayer(-1).fillRectangle(0, 0, this.getWidth(), this.getHeight(), MapColorPalette.COLOR_BLACK);

		this.logoImage = Main.getDVDLogoTexture().clone();
		this.positionX = (this.getWidth() - logoImage.getWidth()) / 3;
		this.positionY = (this.getHeight() - logoImage.getHeight()) / 2;
		this.color = MapColorPalette.COLOR_WHITE;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * We use this method to draw the graphics, as well as check for collision with
	 * the walls, and update position and color of the logo.
	 */
	@Override
	public void onTick() {
		drawIcon();
		
		positionX += horizontalSpeed;
		positionY += verticalSpeed;

		checkWalls();
	}
	
	private void drawIcon() {
		this.getLayer(0).clear();
		this.getLayer(0).draw(logoImage, positionX, positionY, color);
	}
	
	private void checkWalls() {
		if (horizontalSpeed > 0 && positionX + logoImage.getWidth() >= this.getWidth() - 1) {
			// Right edge
			horizontalSpeed = -MOVEMENT_SPEED;
			updateColor();
		}
		if (horizontalSpeed < 0 && positionX <= 0) {
			// Left edge
			horizontalSpeed = MOVEMENT_SPEED;
			updateColor();
		}

		if (verticalSpeed > 0 && positionY + logoImage.getHeight() >= this.getHeight() - 1) {
			// Bottom edge
			verticalSpeed = -MOVEMENT_SPEED;
			updateColor();
		}
		if (verticalSpeed < 0 && positionY <= 0) {
			// Top edge
			verticalSpeed = MOVEMENT_SPEED;
			updateColor();
		}
	}

	private void updateColor() {
		// Generate RGB between $50 and $FF inclusive
		int r = random.nextInt(0xFF + 1 - 0x50) + 0x50;
		int g = random.nextInt(0xFF + 1 - 0x50) + 0x50;
		int b = random.nextInt(0xFF + 1 - 0x50) + 0x50;
		this.color = MapColorPalette.getColor(r, g, b);
	}
}