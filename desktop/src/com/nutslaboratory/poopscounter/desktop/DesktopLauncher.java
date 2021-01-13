package com.nutslaboratory.poopscounter.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.nutslaboratory.poopscounter.game.PoopsCounter;

public class DesktopLauncher {
	private static boolean rebuildAtlas = false;

	public static void main (String[] arg) {
		if (rebuildAtlas) {
			Settings settings = new Settings();
			settings.maxWidth = 2048;
			settings.maxHeight = 2048;
			settings.duplicatePadding = false;
			settings.filterMin = Texture.TextureFilter.Linear;
			settings.filterMag = Texture.TextureFilter.Linear;
			settings.debug = false;


			TexturePacker.process(settings, "../../desktop/assets-raw/images", "../../android/assets/images", "game_assets");
			TexturePacker.process(settings, "../../desktop/assets-raw/images-loading", "../../android/assets/images", "loading_assets");
			TexturePacker.process(settings, "../../desktop/assets-raw/images-ui", "../../android/assets/skins/", "game_ui_skin");
		}


		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//config.width = 400;
		//config.height = 640;

		//config.width = 480;
		//config.height = 640;

		//config.width = 320;
		//config.height = 480;

		config.width = 432;
		config.height = 720;

		//config.width = 250;
		//config.height = 400;

		//config.width = 360;
		//config.height = 640;

		new LwjglApplication(new PoopsCounter(null), config);
	}
}
