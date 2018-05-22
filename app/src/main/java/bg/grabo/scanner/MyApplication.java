package bg.grabo.scanner;

import android.app.Application;
import android.graphics.Typeface;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class MyApplication extends Application {

	// fonts in application
	public static Typeface font_awesome;
	public static Typeface font_awesome5free_regular;
	public static Typeface roboto_light;
	public static Typeface roboto_regular;
	public static Typeface material_icons;

	@Override
	public void onCreate() {

		super.onCreate();
		Fabric.with(this, new Crashlytics());

		font_awesome = Typeface.createFromAsset(getAssets(), "fonts/FontAwesome.ttf");
		font_awesome5free_regular = Typeface.createFromAsset(getAssets(), "fonts/FontAwesome5Free-Regular.otf");
		roboto_light = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
		roboto_regular = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		material_icons = Typeface.createFromAsset(getAssets(), "fonts/MaterialIcons.ttf");

	}

}