package bg.grabo.scanner;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

	public static final String SAVED_VARIABLES = "SavedVariables";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		TextView settings_button = findViewById(R.id.main_activity_settings_button);
		settings_button.setTypeface(MyApplication.material_icons);

		TextView scan_button_icon = findViewById(R.id.main_activity_scan_button_icon);
		scan_button_icon.setTypeface(MyApplication.material_icons);
		TextView scan_button_text_top = findViewById(R.id.main_activity_scan_button_text_top);
		scan_button_text_top.setTypeface(MyApplication.roboto_regular);
		TextView scan_button_text_bottom = findViewById(R.id.main_activity_scan_button_text_bottom);
		scan_button_text_bottom.setTypeface(MyApplication.roboto_light);

		TextView input_button_icon = findViewById(R.id.main_activity_input_button_icon);
		input_button_icon.setTypeface(MyApplication.material_icons);
		TextView input_button_text_top = findViewById(R.id.main_activity_input_button_text_top);
		input_button_text_top.setTypeface(MyApplication.roboto_regular, Typeface.BOLD);
		TextView input_button_text_bottom = findViewById(R.id.main_activity_input_button_text_bottom);
		input_button_text_bottom.setTypeface(MyApplication.roboto_light);

	}

	public void openScanActivity(View view) {
		Intent scan_intent = new Intent(getApplicationContext(), ScanActivity.class);
		startActivity(scan_intent);
	}

	public void openInputActivity(View view) {
		Intent input_intent = new Intent(getApplicationContext(), InputActivity.class);
		startActivity(input_intent);
	}

	public void openSettingsActivity(View view) {
		Intent settings_intent = new Intent(getApplicationContext(), SettingsActivity.class);
		startActivity(settings_intent);
	}

	public void openAboutActivity(View view) {
		Intent about_intent = new Intent(getApplicationContext(), AboutActivity.class);
		startActivity(about_intent);
	}

}