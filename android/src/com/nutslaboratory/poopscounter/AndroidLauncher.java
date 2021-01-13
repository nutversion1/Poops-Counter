package com.nutslaboratory.poopscounter;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.WindowManager;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.nutslaboratory.poopscounter.util.IActivityRequestHandler;

import static android.content.pm.PackageManager.NameNotFoundException;

public class AndroidLauncher extends AndroidApplication implements IActivityRequestHandler {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new com.nutslaboratory.poopscounter.game.PoopsCounter(this), config);

		//keep screen on
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	public int getVersionCode() {
		int versionCode = 0;

		try {
			PackageInfo pInfo;
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

			versionCode  = pInfo.versionCode;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return versionCode;
	}

	@Override
	public String getVersionName() {
		String versionName = null;

		try {
			PackageInfo pInfo;
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

			versionName  = pInfo.versionName;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return versionName;
	}

	@Override
	public void setTrackerScreenName(String path) {

	}

	@Override
	public void showAds(boolean show) {

	}
}
