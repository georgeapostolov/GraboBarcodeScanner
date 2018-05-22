package bg.grabo.scanner;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

	public SharedPreferences settings;
	public EditText eik_input;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_activity);

		settings = getSharedPreferences(MainActivity.SAVED_VARIABLES, 0);
		eik_input = findViewById(R.id.settings_activity_input);

		eik_input.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

		String eik = settings.getString("EIK", "");
		if (!eik.equals("")) {
			eik_input.setText(eik);
		}

		Boolean can_sound = settings.getBoolean("can_sound", true);
		if (can_sound) {
			CheckBox sound_checkbox = findViewById(R.id.settings_activity_sound_checkbox);
			sound_checkbox.setChecked(true);
		}

		Boolean can_vibrate = settings.getBoolean("can_vibrate", true);
		if (can_vibrate) {
			CheckBox vibrate_checkbox = findViewById(R.id.settings_activity_vibrate_checkbox);
			vibrate_checkbox.setChecked(true);
		}

		TextView back_button = findViewById(R.id.settings_activity_back_button);
		back_button.setTypeface(MyApplication.material_icons);

		TextView text_description = findViewById(R.id.settings_activity_text_description);
		text_description.setTypeface(MyApplication.roboto_regular);

		eik_input.setTypeface(MyApplication.roboto_light);

		TextView save_button_icon = findViewById(R.id.settings_activity_save_button_icon);
		save_button_icon.setTypeface(MyApplication.font_awesome);
		TextView save_button_text = findViewById(R.id.settings_activity_save_button_text);
		save_button_text.setTypeface(MyApplication.roboto_regular);

	}

	public void closeActivity(View view) {
		finish();
	}

	public void saveSettings(View view) {

		String eik = eik_input.getText().toString();

		Boolean can_sound = false;
		Boolean can_vibrate = false;

		CheckBox sound_checkbox = findViewById(R.id.settings_activity_sound_checkbox);
		if (sound_checkbox.isChecked()) {
			can_sound = true;
		}

		CheckBox vibrate_checkbox = findViewById(R.id.settings_activity_vibrate_checkbox);
		if (vibrate_checkbox.isChecked()) {
			can_vibrate = true;
		}

		SharedPreferences.Editor editor = settings.edit();

		editor.putString("EIK", eik);
		editor.putBoolean("can_sound", can_sound);
		editor.putBoolean("can_vibrate", can_vibrate);

		editor.commit();

		finish();

	}

}
