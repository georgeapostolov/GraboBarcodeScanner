package bg.grabo.scanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EndActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.end_activity);

		TextView back_button = findViewById(R.id.end_activity_back_button);
		back_button.setTypeface(MyApplication.material_icons);

		TextView top_header_icon = findViewById(R.id.end_activity_top_header_icon);
		top_header_icon.setTypeface(MyApplication.font_awesome5free_regular);

		TextView top_header_text = findViewById(R.id.end_activity_text_result_header);
		top_header_text.setTypeface(MyApplication.roboto_light);

		TextView scan_new_voucher_icon = findViewById(R.id.end_activity_scan_new_voucher_icon);
		scan_new_voucher_icon.setTypeface(MyApplication.material_icons);
		TextView scan_new_voucher_text = findViewById(R.id.end_activity_scan_new_voucher_text);
		scan_new_voucher_text.setTypeface(MyApplication.roboto_regular);

		TextView input_new_voucher_icon = findViewById(R.id.end_activity_input_new_voucher_icon);
		input_new_voucher_icon.setTypeface(MyApplication.material_icons);
		TextView input_new_voucher_text = findViewById(R.id.end_activity_input_new_voucher_text);
		input_new_voucher_text.setTypeface(MyApplication.roboto_regular);

		Intent this_intent = getIntent();
		if (this_intent.getExtras() != null) {
			String input_type = this_intent.getStringExtra("input_type");
			RelativeLayout scan_image = findViewById(R.id.end_activity_scan_new_voucher);
			RelativeLayout input_image = findViewById(R.id.end_activity_input_new_voucher);
			if (input_type.equals("qr") || input_type.equals("barcode")) {
				scan_image.setVisibility(View.VISIBLE);
				input_image.setVisibility(View.GONE);
			} else {
				scan_image.setVisibility(View.GONE);
				input_image.setVisibility(View.VISIBLE);
			}
		}

	}

	public void closeActivity(View view) {
		finish();
	}

	public void openScanActivity(View view) {
		Intent scan_intent = new Intent(getApplicationContext(), ScanActivity.class);
		startActivity(scan_intent);
		finish();
	}

	public void openInputActivity(View view) {
		Intent input_intent = new Intent(getApplicationContext(), InputActivity.class);
		startActivity(input_intent);
		finish();
	}

}