package bg.grabo.scanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.InputType;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class SuccessActivity extends AppCompatActivity {

	public SharedPreferences settings;
	public String eik = "";
	public String barcode = "";
	public String voucher_code = "";
	public String voucher_secret_code = "";
	public String input_type = "";
	private boolean is_loading = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.success_activity);

		settings = getSharedPreferences(MainActivity.SAVED_VARIABLES, 0);
		eik = settings.getString("EIK", "");

		LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

		TextView back_button = findViewById(R.id.success_activity_back_button);
		back_button.setTypeface(MyApplication.material_icons);

		TextView top_header_icon = findViewById(R.id.success_activity_top_header_icon);
		top_header_icon.setTypeface(MyApplication.font_awesome5free_regular);

		TextView top_header_text = findViewById(R.id.success_activity_text_result_header);
		top_header_text.setTypeface(MyApplication.roboto_light);

		RelativeLayout mark_as_used_button = findViewById(R.id.success_activity_mark_as_used);

		TextView mark_as_used_icon = findViewById(R.id.success_activity_mark_as_used_icon);
		mark_as_used_icon.setTypeface(MyApplication.font_awesome);
		TextView mark_as_used_text = findViewById(R.id.success_activity_mark_as_used_text);
		mark_as_used_text.setTypeface(MyApplication.roboto_regular);

		TextView scan_new_voucher_icon = findViewById(R.id.success_activity_scan_new_voucher_icon);
		scan_new_voucher_icon.setTypeface(MyApplication.material_icons);
		TextView scan_new_voucher_text = findViewById(R.id.success_activity_scan_new_voucher_text);
		scan_new_voucher_text.setTypeface(MyApplication.roboto_regular);

		TextView input_new_voucher_icon = findViewById(R.id.success_activity_input_new_voucher_icon);
		input_new_voucher_icon.setTypeface(MyApplication.material_icons);
		TextView input_new_voucher_text = findViewById(R.id.success_activity_input_new_voucher_text);
		input_new_voucher_text.setTypeface(MyApplication.roboto_regular);

		Intent this_intent = getIntent();

		if (this_intent.getExtras() != null) {

			final ViewGroup nullParent = null;

			barcode = this_intent.getStringExtra("barcode");
			voucher_code = this_intent.getStringExtra("voucher_code");
			voucher_secret_code = this_intent.getStringExtra("voucher_secret_code");
			input_type = this_intent.getStringExtra("input_type");

			String response = this_intent.getStringExtra("response");
			//TextView success_text = findViewById(R.id.success_result);
			//success_text.setText(response);

			try {

				JSONObject result = new JSONObject(response);

				// warnings
				JSONArray warnings = result.getJSONArray("warnings");
				TextView warnings_textview = findViewById(R.id.success_activity_warnings_title);
				LinearLayout warnings_layout = findViewById(R.id.success_activity_warnings_text);
				if (0 < warnings.length()) {
					top_header_icon.setTextColor(getResources().getColor(R.color.colorError));
					top_header_icon.setTypeface(MyApplication.material_icons);
					top_header_icon.setText(R.string.icon_rounded_info);
					top_header_text.setTextColor(getResources().getColor(R.color.colorError));
					warnings_textview.setTypeface(MyApplication.roboto_light);
					System.out.print(warnings);
					for (int i = 0; i < warnings.length(); i++) {
						String warning = warnings.getString(i);
						//Log.d("warning #" + i + ": ", warning);
						View rl1 = inflater.inflate(R.layout.warnings_text, nullParent, false);
						TextView d_uslovie_txt = rl1.findViewById(R.id.warning_text);
						d_uslovie_txt.setText(warning);
						d_uslovie_txt.setTypeface(MyApplication.roboto_regular);
						warnings_layout.addView(rl1);
					}
				} else {
					warnings_textview.setVisibility(View.GONE);
					warnings_layout.setVisibility(View.GONE);
				}

				JSONObject voucher = result.getJSONObject("voucher");

				String voucher_status = voucher.getString("status");
				if (voucher_status.equals("NOT_USED")) {
					mark_as_used_button.setVisibility(View.VISIBLE);
				}

				// voucher title
				String voucher_title = voucher.getString("title");
				TextView voucher_title_textview = findViewById(R.id.success_activity_voucher_title);
				voucher_title_textview.setTypeface(MyApplication.roboto_regular);
				voucher_title_textview.setText(voucher_title);

				// company name
				TextView company_name_textview = findViewById(R.id.success_activity_company_name_title);
				company_name_textview.setTypeface(MyApplication.roboto_light);
				String company_name = voucher.getString("company_name");
				String company_firm_name = voucher.getString("company_firm_name");
				TextView company_name_text_textview = findViewById(R.id.success_activity_company_name_text);
				company_name_text_textview.setTypeface(MyApplication.roboto_regular);
				String final_company_name = company_name + " (" + company_firm_name + ")";
				company_name_text_textview.setText(final_company_name);

				// username
				TextView username_title_textview = findViewById(R.id.success_activity_username_title);
				username_title_textview.setTypeface(MyApplication.roboto_light);
				String username = voucher.getString("user_name");
				TextView username_textview = findViewById(R.id.success_activity_username_text);
				username_textview.setTypeface(MyApplication.roboto_regular);
				username_textview.setText(username);

				// prices
				LinearLayout voucher_prices_layout = findViewById(R.id.success_activity_voucher_prices);
				View vpl = inflater.inflate(R.layout.voucher_prices, nullParent, false);
				voucher_prices_layout.addView(vpl);

				String price_regular = voucher.getString("price_regular");
				String price_promo = voucher.getString("price_promo");
				String amount_paid = voucher.getString("amount_paid");
				String amount_to_surcharge = voucher.getString("amount_to_surcharge");

				TextView price_title_textview = findViewById(R.id.success_activity_price_title);
				price_title_textview.setTypeface(MyApplication.roboto_light);
				if (price_regular.equals(price_promo)) {
					price_title_textview.setText("Топ цена:");
					String price = price_promo + "лв.";
					TextView price_textview = findViewById(R.id.success_activity_price_text);
					price_textview.setTypeface(MyApplication.roboto_regular);
					price_textview.setText(price);
				} else {
					String price = price_promo + "лв." + " " + price_regular + "лв.";
					TextView price_textview = findViewById(R.id.success_activity_price_text);
					price_textview.setTypeface(MyApplication.roboto_regular);
					price_textview.setText(price, TextView.BufferType.SPANNABLE);
					Spannable spannable = (Spannable) price_textview.getText();
					spannable.setSpan(new StrikethroughSpan(), price.length() - 3 - price_regular.length(), price.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
				}

				TextView amount_paid_title_textview = findViewById(R.id.success_activity_amount_paid_title);
				TextView amount_paid_textview = findViewById(R.id.success_activity_amount_paid_text);
				TextView amount_to_surcharge_title_textview = findViewById(R.id.success_activity_amount_to_surcharge_title);
				TextView amount_to_surcharge_textview = findViewById(R.id.success_activity_amount_to_surcharge_text);
				if (amount_paid.equals(price_promo)) {
					amount_paid_title_textview.setVisibility(View.GONE);
					amount_paid_textview.setVisibility(View.GONE);
					amount_to_surcharge_title_textview.setVisibility(View.GONE);
					amount_to_surcharge_textview.setVisibility(View.GONE);
				} else {
					amount_paid_title_textview.setTypeface(MyApplication.roboto_light);
					amount_paid_textview.setTypeface(MyApplication.roboto_regular);
					String amount_paid_text = amount_paid + "лв.";
					amount_paid_textview.setText(amount_paid_text);
					amount_to_surcharge_title_textview.setTypeface(MyApplication.roboto_light);
					amount_to_surcharge_textview.setTypeface(MyApplication.roboto_regular);
					String amount_to_surcharge_text = amount_to_surcharge + "лв.";
					amount_to_surcharge_textview.setText(amount_to_surcharge_text);
				}

				// date valid
				TextView date_valid_title_textview = findViewById(R.id.success_activity_date_valid_title);
				date_valid_title_textview.setTypeface(MyApplication.roboto_light);
				String date_valid = voucher.getString("date_valid");
				TextView date_valid_textview = findViewById(R.id.success_activity_date_valid_text);
				date_valid_textview.setTypeface(MyApplication.roboto_regular);

				int unixTime = Integer.parseInt(date_valid);

				long timestamp = unixTime * 1000L;  // msec
				java.util.Date d = new java.util.Date(timestamp);

				SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd MMMM yyyy", new Locale("bg"));

				date_valid_textview.setText(sdf.format(d));
				date_valid_textview.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
				if (timestamp < System.currentTimeMillis()) {
					date_valid_textview.setTextColor(getResources().getColor(R.color.colorAccent));
				}

				// deal rules
				JSONArray deal_rules = voucher.getJSONArray("deal_rules");
				TextView deal_rules_textview = findViewById(R.id.success_activity_deal_rules_title);
				LinearLayout deal_rules_layout = findViewById(R.id.success_activity_deal_rules_text);
				if (0 < deal_rules.length()) {
					deal_rules_textview.setTypeface(MyApplication.roboto_light);
					for (int i = 0; i < deal_rules.length(); i++) {
						String rule = deal_rules.getString(i);
						View rl1 = inflater.inflate(R.layout.deal_rules_text, nullParent, false);
						TextView d_uslovie_txt = rl1.findViewById(R.id.deal_rules_text_);
						rule = rule.replace("\n", "<br />");
						d_uslovie_txt.setText(Html.fromHtml(rule));
						d_uslovie_txt.setTypeface(MyApplication.roboto_regular);
						deal_rules_layout.addView(rl1);
					}
				} else {
					deal_rules_textview.setVisibility(View.GONE);
					deal_rules_layout.setVisibility(View.GONE);
				}

				// deal extras
				JSONArray deal_extras = voucher.getJSONArray("deal_extras");
				TextView deal_extras_textview = findViewById(R.id.success_activity_deal_extras_title);
				LinearLayout deal_extras_layout = findViewById(R.id.success_activity_deal_extras_text);
				if (0 < deal_extras.length()) {
					deal_extras_textview.setTypeface(MyApplication.roboto_light);
					for (int i = 0; i < deal_extras.length(); i++) {
						String rule = deal_extras.getString(i);
						View rl1 = inflater.inflate(R.layout.deal_rules_text, nullParent, false);
						TextView d_uslovie_txt = rl1.findViewById(R.id.deal_rules_text_);
						rule = rule.replace("\n", "<br />");
						d_uslovie_txt.setText(Html.fromHtml(rule));
						d_uslovie_txt.setTypeface(MyApplication.roboto_regular);
						deal_extras_layout.addView(rl1);
					}
				} else {
					deal_extras_textview.setVisibility(View.GONE);
					deal_extras_layout.setVisibility(View.GONE);
				}

				RelativeLayout scan_image = findViewById(R.id.success_activity_scan_new_voucher);
				RelativeLayout input_image = findViewById(R.id.success_activity_input_new_voucher);

				if (input_type.equals("qr") || input_type.equals("barcode")) {
					scan_image.setVisibility(View.VISIBLE);
					input_image.setVisibility(View.INVISIBLE);
				} else {
					top_header_text.setText("Въвеждането е успешно!");
					scan_image.setVisibility(View.INVISIBLE);
					input_image.setVisibility(View.VISIBLE);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

	}

	public void closeActivity(View view) {
		finish();
	}

	public void markAsUsed(View view) {

		if (!is_loading) {

			is_loading = true;

			RequestQueue queue = Volley.newRequestQueue(this); // this = context

			String url = "https://grabo.bg/api/checkvoucher?mark_as_used=true&";

			if (input_type.equals("barcode")) {
				url = url + "type=barcode&barcode=" + barcode;
			} else {
				url = url + "type=qr&voucher_code=" + voucher_code + "&voucher_secret_code=" + voucher_secret_code;
			}

			if (!eik.equals("")) {
				url = url + "&eik=" + eik;
			}

			// prepare the Request
			JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
				new Response.Listener<JSONObject>()
				{
					@Override
					public void onResponse(JSONObject response) {
						is_loading = false;
						// display response
						String result_type = "";
						try {
							result_type = response.getString("result");
						} catch (JSONException e) {
							// error
						}

						if (result_type.equals("OK")) {
							Intent end_intent = new Intent(getApplicationContext(), EndActivity.class);
							end_intent.putExtra("input_type", input_type);
							startActivity(end_intent);
							finish();
						}

			        }
			    },
			    new Response.ErrorListener()
			    {
				    @Override
			         public void onErrorResponse(VolleyError error) {
					    is_loading = false;
			            Log.d("Error.Response", error.toString());
					    //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
					    Toast.makeText(getApplicationContext(), "Няма връзка със сървъра на Grabo.bg.", Toast.LENGTH_LONG).show();
			       }
			    }
			);

			// add it to the RequestQueue
			queue.add(getRequest);

		}

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
