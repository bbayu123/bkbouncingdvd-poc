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
package io.github.bbayu123.bkbouncingdvd.mapwidget_version;

import java.util.Random;

import com.bergerkiller.bukkit.common.map.MapCanvas;
import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapDisplay;
import com.bergerkiller.bukkit.common.map.MapDisplayProperties;
import com.bergerkiller.bukkit.common.map.MapSessionMode;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;

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
	 * Holds the DVD logo widget
	 */
	private DVDLogo logo = null;

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

		this.horizontalSpeed = BouncingDVD.MOVEMENT_SPEED;
		this.verticalSpeed = BouncingDVD.MOVEMENT_SPEED;

		this.getLayer().fillRectangle(0, 0, this.getWidth(), this.getHeight(), MapColorPalette.COLOR_BLACK);

		this.logo = new DVDLogo();
		this.logo.setPosition((this.getWidth() - this.logo.getWidth()) / 3, (this.getHeight() - this.logo.getHeight()) / 2);
		this.addWidget(this.logo);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * We use this method to check for collision with the walls, and update position
	 * and color of the logo.
	 */
	@Override
	public void onTick() {
		this.logo.setPosition(this.logo.getX() + this.horizontalSpeed, this.logo.getY() + this.verticalSpeed);
		this.checkWalls();
	}

	private void checkWalls() {
		if (this.horizontalSpeed > 0 && this.logo.getX() + this.logo.getWidth() >= this.getWidth() - 1) {
			// Right edge
			this.horizontalSpeed = -BouncingDVD.MOVEMENT_SPEED;
			this.updateColor();
		}
		if (this.horizontalSpeed < 0 && this.logo.getX() <= 0) {
			// Left edge
			this.horizontalSpeed = BouncingDVD.MOVEMENT_SPEED;
			this.updateColor();
		}

		if (this.verticalSpeed > 0 && this.logo.getY() + this.logo.getHeight() >= this.getHeight() - 1) {
			// Bottom edge
			this.verticalSpeed = -BouncingDVD.MOVEMENT_SPEED;
			this.updateColor();
		}
		if (this.verticalSpeed < 0 && this.logo.getY() <= 0) {
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
		this.logo.setColor(r, g, b);
	}
}

/**
 * This is the DVD logo widget class
 *
 * @author Cyrus Mian Xi Li (bbayu/bbayu123)
 *
 * @see {@link MinesweeperBoard} for the game logic widget
 */
class DVDLogo extends MapWidget {

	private final MapCanvas logoImage;

	public DVDLogo() {
		this.logoImage = Main.getDVDLogoTexture().clone();
		this.setSize(this.logoImage.getWidth(), this.logoImage.getHeight());
	}

	@Override
	public void onDraw() {
		this.view.draw(this.logoImage, 0, 0);
	}

	public void setColor(int r, int g, int b) {
		byte targetColor = MapColorPalette.getColor(r, g, b);

		for (int i = 0; i < this.logoImage.getWidth(); i++) {
			for (int j = 0; j < this.logoImage.getHeight(); j++) {
				if (this.logoImage.readPixel(i, j) != MapColorPalette.COLOR_TRANSPARENT) {
					this.logoImage.writePixel(i, j, targetColor);
				}
			}
		}
		this.invalidate();
	}
}