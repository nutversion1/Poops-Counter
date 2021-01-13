//Start project at 7 / February / 2017

package com.nutslaboratory.poopscounter.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Interpolation;
import com.nutslaboratory.nutengine.audio.AudioManager;
import com.nutslaboratory.nutengine.game.BaseGame;
import com.nutslaboratory.nutengine.screen.BaseScreen;
import com.nutslaboratory.nutengine.screen.transition.ScreenTransition;
import com.nutslaboratory.nutengine.screen.transition.ScreenTransitionSlideBoth;
import com.nutslaboratory.poopscounter.screen.LoadingScreen;
import com.nutslaboratory.poopscounter.util.Asset;
import com.nutslaboratory.poopscounter.util.GamePreferences;
import com.nutslaboratory.poopscounter.util.IActivityRequestHandler;

public class PoopsCounter extends BaseGame {
	private static final int WORLD_WIDTH = 480;
	private static final int WORLD_HEIGHT = 800;

	private IActivityRequestHandler handler;
	private boolean isAndroid;

	private Preferences infoPrefs;

	private boolean clearGameSetting = false;
	private boolean clearGameSettingWhenUpdate = true;

	public PoopsCounter(IActivityRequestHandler IARH){
		super(WORLD_WIDTH, WORLD_HEIGHT);

		handler = IARH;
	}

	@Override
	public void create() {
		//set log level
		Gdx.app.setLogLevel(Application.LOG_NONE);

		//Interfacing-with-platform-specific-code
		switch(Gdx.app.getType()){
			case Android:
				isAndroid = true;
				break;
			case Applet:
				break;
			case Desktop:
				break;
			case HeadlessDesktop:
				break;
			case WebGL:
				break;
			case iOS:
				break;
			default:
				break;
		}

		//create info prefs
		infoPrefs = Gdx.app.getPreferences("gameInfo");

		//clear game setting
		if(clearGameSetting) {
			GamePreferences.instance.clear();
			Gdx.app.log("test", "clear game setting");
		}

		//clear game setting when update game to next version
		if(clearGameSettingWhenUpdate) {
			if (isAndroid) {
				//check game setting is last version
				if (!isLastGameSettingVersion()) {
					//clear game setting (old version)
					GamePreferences.instance.clear();
					Gdx.app.log("test", "clear game setting");
				}

				//save game setting version
				infoPrefs.putInteger("gameSettingVersion", handler.getVersionCode());
				infoPrefs.flush();
			}
		}

		//load game prefs
		GamePreferences.instance.load();

		//music & sound
		AudioManager.instance.setMusicVolume(GamePreferences.instance.volMusic);
		AudioManager.instance.setSoundVolume(GamePreferences.instance.volSound);

		if(!GamePreferences.instance.music){
			AudioManager.instance.muteMusic();
		}
		if(!GamePreferences.instance.sound){
			AudioManager.instance.muteSound();
		}

		//set debug mode
		setDebugMode(false);

		//go to loading screen
		setScreen(new LoadingScreen(this), null);



	}

	public void transferScreen(BaseScreen screen, int direction){
		ScreenTransition transition = ScreenTransitionSlideBoth.init(0.7f, direction, Interpolation.pow4);
		setScreen(screen, transition);

		AudioManager.instance.playSound(Asset.instance.sound.changeScreen);
	}

	@Override
	public void dispose(){
        super.dispose();

        Asset.instance.dispose();
		AudioManager.instance.dispose();

		Gdx.app.log("", "game dispose");
	}

	private boolean isLastGameSettingVersion(){
		boolean isLast;

		int gameSettingVersion = infoPrefs.getInteger("gameSettingVersion", 1);

		//game setting version is old version
		if(handler.getVersionCode() > gameSettingVersion){
			isLast = false;
			Gdx.app.log("test", "game setting is old version");
		//game setting version is last version
		}else{
			isLast = true;
			Gdx.app.log("test", "game setting is last version");
		}

		return isLast;
	}

	public IActivityRequestHandler getHandler(){
		return handler;
	}

	public boolean isAndroid(){
		return isAndroid;
	}


}
