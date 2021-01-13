package com.nutslaboratory.poopscounter.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ArrayMap;

public class Asset {
	public static final Asset instance = new Asset();

	private AssetManager assetManager;

	public AssetTexture texture;
	public AssetMusic music;
	public AssetSound sound;
	public AssetFont font;
	public AssetSkin skin;
	public AssetAnimation animation;



	private Asset(){

	}

	public void loadAssets(){
		//System.out.println("load assets");
		
		//
		assetManager = new AssetManager();

		//
		loadTextureAssets();
		loadMusicAssets();
		loadSoundAssets();
		loadFontAssets();
	}

	private void loadTextureAssets() {
		assetManager.load("images/game_assets.atlas", TextureAtlas.class);
		assetManager.load("skins/game_ui_skin.atlas", TextureAtlas.class);
	}

	private void loadMusicAssets() {
		assetManager.load("musics/Doobly Doo.ogg", Music.class);
		assetManager.load("musics/Upbeat Forever.ogg", Music.class);
		assetManager.load("musics/Voice Over Under.ogg", Music.class);

	}

    private void loadSoundAssets() {
        assetManager.load("sounds/cartoon_throw.ogg", Sound.class);
        assetManager.load("sounds/error.ogg", Sound.class);
        assetManager.load("sounds/beep_space_button.ogg", Sound.class);
        assetManager.load("sounds/button_off.ogg", Sound.class);
        assetManager.load("sounds/sci_fi_button_click.ogg", Sound.class);
        assetManager.load("sounds/button_press.ogg", Sound.class);
        assetManager.load("sounds/throwing_whip_effect.ogg", Sound.class);
        assetManager.load("sounds/pop4.ogg", Sound.class);
        assetManager.load("sounds/race_countdown1_ready.ogg", Sound.class);
        assetManager.load("sounds/race_countdown1_go.ogg", Sound.class);
        assetManager.load("sounds/swosh_22.ogg", Sound.class);
        assetManager.load("sounds/blip_2.ogg", Sound.class);
        assetManager.load("sounds/sniper_scope_zoom_in.ogg", Sound.class);
        assetManager.load("sounds/locking_a_door_no_key.ogg", Sound.class);
        assetManager.load("sounds/flies.ogg", Sound.class);
        assetManager.load("sounds/syntheticwind_hektorsound.ogg", Sound.class);
        assetManager.load("sounds/magic_sfx_for_games.ogg", Sound.class);
        assetManager.load("sounds/squash.ogg", Sound.class);
        assetManager.load("sounds/game_powerup.ogg", Sound.class);
        assetManager.load("sounds/jump_sound_or_power_up_sound.ogg", Sound.class);
    }

	private void loadFontAssets(){
		//set loader
		FileHandleResolver resolver = new InternalFileHandleResolver();
		assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

		//load font asset
		loadFontAsset("fonts/whitrabt.ttf", 40, "white_rabbit_40.ttf");
		loadFontAsset("fonts/whitrabt.ttf", 25, "white_rabbit_25.ttf");
		loadFontAsset("fonts/whitrabt.ttf", 20, "white_rabbit_20.ttf");
		loadFontAsset("fonts/DS-DIGIB.TTF", 36, "ds_digital_36.ttf");
        loadFontAsset("fonts/Borders Divide, But Hearts Shall Conquer.ttf", 28, "borders_divide_28.ttf");
		loadFontAsset("fonts/Borders Divide, But Hearts Shall Conquer.ttf", 23, "borders_divide_23.ttf");
        loadFontAsset("fonts/Borders Divide, But Hearts Shall Conquer.ttf", 18, "borders_divide_18.ttf");
        loadFontAsset("fonts/DroidSans.ttf", 23, "droid_sans_23.ttf");
        loadFontAsset("fonts/DroidSans.ttf", 18, "droid_sans_18.ttf");
		loadFontAsset("fonts/DroidSans.ttf", 14, "droid_sans_14.ttf");
		loadFontAsset("fonts/DroidSans-Bold.ttf", 18, "droid_sans_bold_18.ttf");




	}

	private void loadFontAsset(String pathName, int size, String fileName){
		FreeTypeFontLoaderParameter parameter = new FreeTypeFontLoaderParameter();
		parameter.fontFileName = pathName;
		parameter.fontParameters.size = size;

		assetManager.load(fileName, BitmapFont.class, parameter);
	}
	
	public void getAssets(){
		//System.out.println("get assets");

		texture = new AssetTexture();
		music = new AssetMusic();
		sound = new AssetSound();
		font = new AssetFont();
		skin = new AssetSkin();
		animation = new AssetAnimation();
		
	}


	public class AssetTexture{
		public TextureAtlas gameAtlas;

		public AssetTexture(){
			gameAtlas = assetManager.get("images/game_assets.atlas");
		}
	}

	public class AssetMusic{
		public Music main;
		public Music match;
        public Music matchBreak;

		public AssetMusic(){;
			main = assetManager.get("musics/Doobly Doo.ogg");
			match = assetManager.get("musics/Upbeat Forever.ogg");
			matchBreak = assetManager.get("musics/Voice Over Under.ogg");

		}
	}

	public class AssetSound{
		public Sound throwCharacter;
		public Sound touchFoul;
		public Sound mainButton;
		public Sound electricButton;
		public Sound digitalButton;
		public Sound lightButton;
		public Sound changeScreen;
		public Sound pop;
		public Sound countdownReady;
		public Sound countdownGo;
		public Sound poopSpin;
		public Sound specialEventSelect;
		public Sound hint;
		public Sound lock;
		public Sound bug;
		public Sound fog;
		public Sound shield;
		public Sound mud;
		public Sound spin;
		public Sound flip;

		public AssetSound(){
			throwCharacter = assetManager.get("sounds/cartoon_throw.ogg");
			touchFoul = assetManager.get("sounds/error.ogg");
			mainButton = assetManager.get("sounds/button_off.ogg");
			digitalButton = assetManager.get("sounds/beep_space_button.ogg");
			lightButton = assetManager.get("sounds/sci_fi_button_click.ogg");
			electricButton = assetManager.get("sounds/button_press.ogg");
			changeScreen = assetManager.get("sounds/throwing_whip_effect.ogg");
			pop = assetManager.get("sounds/pop4.ogg");
			countdownReady = assetManager.get("sounds/race_countdown1_ready.ogg");
			countdownGo = assetManager.get("sounds/race_countdown1_go.ogg");
			poopSpin = assetManager.get("sounds/swosh_22.ogg");
			specialEventSelect = assetManager.get("sounds/blip_2.ogg");
			hint = assetManager.get("sounds/sniper_scope_zoom_in.ogg");
			lock = assetManager.get("sounds/locking_a_door_no_key.ogg");
			bug = assetManager.get("sounds/flies.ogg");
			fog = assetManager.get("sounds/syntheticwind_hektorsound.ogg");
			shield = assetManager.get("sounds/magic_sfx_for_games.ogg");
			mud = assetManager.get("sounds/squash.ogg");
			spin = assetManager.get("sounds/game_powerup.ogg");
			flip = assetManager.get("sounds/jump_sound_or_power_up_sound.ogg");


		}
	}

	public class AssetFont {
		public BitmapFont whiteRabbit40, whiteRabbit25, whiteRabbit20;
		public BitmapFont dsDigital36;
		public BitmapFont bordersDivide28, bordersDivide23, bordersDivide18;
        public BitmapFont droidSans23, droidSans18, droidSans14;
		public BitmapFont droidSansBold18;

		public AssetFont(){
			whiteRabbit40 = Asset.instance.getAssetManager().get("white_rabbit_40.ttf");
			whiteRabbit40.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

			whiteRabbit25 = Asset.instance.getAssetManager().get("white_rabbit_25.ttf");
			whiteRabbit25.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

			whiteRabbit20 = Asset.instance.getAssetManager().get("white_rabbit_20.ttf");
			whiteRabbit20.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

			dsDigital36 = Asset.instance.getAssetManager().get("ds_digital_36.ttf");
			dsDigital36.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

			bordersDivide28 = Asset.instance.getAssetManager().get("borders_divide_28.ttf");
			bordersDivide28.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

            bordersDivide23 = Asset.instance.getAssetManager().get("borders_divide_23.ttf");
            bordersDivide23.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

            bordersDivide18 = Asset.instance.getAssetManager().get("borders_divide_18.ttf");
            bordersDivide18.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

            droidSans23 = Asset.instance.getAssetManager().get("droid_sans_23.ttf");
            droidSans23.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

            droidSans18 = Asset.instance.getAssetManager().get("droid_sans_18.ttf");
            droidSans18.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

			droidSans14 = Asset.instance.getAssetManager().get("droid_sans_14.ttf");
			droidSans14.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

			droidSansBold18 = Asset.instance.getAssetManager().get("droid_sans_bold_18.ttf");
			droidSansBold18.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		}
	}

	public class AssetSkin{
		public Skin gameUISkin;


		public AssetSkin(){
			gameUISkin = new Skin(Gdx.files.internal("skins/game_ui_skin.json"));

			gameUISkin.add("white_rabbit_40", new LabelStyle(Asset.instance.font.whiteRabbit40, Color.WHITE));
			gameUISkin.add("white_rabbit_25", new LabelStyle(Asset.instance.font.whiteRabbit25, Color.WHITE));
			gameUISkin.add("white_rabbit_20", new LabelStyle(Asset.instance.font.whiteRabbit20, Color.WHITE));
			gameUISkin.add("ds_digital_36", new LabelStyle(Asset.instance.font.dsDigital36, Color.WHITE));
			gameUISkin.add("borders_divide_28", new LabelStyle(Asset.instance.font.bordersDivide28, Color.WHITE));
            gameUISkin.add("borders_divide_23", new LabelStyle(Asset.instance.font.bordersDivide23, Color.WHITE));
            gameUISkin.add("borders_divide_18", new LabelStyle(Asset.instance.font.bordersDivide18, Color.WHITE));
            gameUISkin.add("droid_sans_23", new LabelStyle(Asset.instance.font.droidSans23, Color.WHITE));
            gameUISkin.add("droid_sans_18", new LabelStyle(Asset.instance.font.droidSans18, Color.WHITE));
			gameUISkin.add("droid_sans_14", new LabelStyle(Asset.instance.font.droidSans14, Color.WHITE));
            gameUISkin.add("droid_sans_bold_18", new LabelStyle(Asset.instance.font.droidSansBold18, Color.WHITE));

		}
	}

	public class AssetAnimation {
		public ArrayMap<String, Animation> characters;
		public Animation specialEventArrowUp;
		public Animation specialEventArrowDown;
		public Animation shield;
		public Animation fly;
		public Animation cockroach;

		public AssetAnimation(){

			//
			characters = new ArrayMap<String, Animation>();
			for(int i = 0; i < 24; i++){
				String index = "";
				if(i < 10){
					index = "0"+i;
				}else{
					index = ""+i;
				}
				characters.put("m_"+index, new Animation(0.22f, texture.gameAtlas.findRegions("play/m"+index), Animation.PlayMode.LOOP));
			}

			//
			specialEventArrowUp = new Animation(0.4f, Asset.instance.skin.gameUISkin.getAtlas().findRegions("event_arrow_up"), Animation.PlayMode.LOOP);
			specialEventArrowDown = new Animation(0.4f, Asset.instance.skin.gameUISkin.getAtlas().findRegions("event_arrow_down"), Animation.PlayMode.LOOP);

			shield = new Animation(0.18f, Asset.instance.texture.gameAtlas.findRegions("play/shield"), Animation.PlayMode.LOOP);
			fly = new Animation(0.1f, Asset.instance.texture.gameAtlas.findRegions("play/fly"), Animation.PlayMode.LOOP_PINGPONG);
			cockroach = new Animation(0.1f, Asset.instance.texture.gameAtlas.findRegions("play/cockroach"), Animation.PlayMode.LOOP_PINGPONG);
		}

	}

	public void dispose(){
		//System.out.println("dispose assets");

		assetManager.clear();
	}

	public AssetManager getAssetManager(){
		return assetManager;

	}
}
