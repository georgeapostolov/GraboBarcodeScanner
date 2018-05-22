package bg.grabo.scanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_activity);

		TextView back_button = findViewById(R.id.about_activity_back_button);
		back_button.setTypeface(MyApplication.material_icons);

		TextView text_description = findViewById(R.id.about_activity_text_description);
		text_description.setTypeface(MyApplication.roboto_regular);

	}

	public void closeActivity(View view) {
		finish();
	}

}