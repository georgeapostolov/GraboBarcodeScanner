package bg.grabo.scanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class InputActivity extends AppCompatActivity {

	public String eik = "";
	public boolean is_loading = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.input_activity);

		SharedPreferences settings = getSharedPreferences(MainActivity.SAVED_VARIABLES, 0);
		eik = settings.getString("EIK", "");

		TextView back_button = findViewById(R.id.input_activity_back_button);
		back_button.setTypeface(MyApplication.material_icons);

		TextView text_description = findViewById(R.id.input_activity_text_description);
		text_description.setTypeface(MyApplication.roboto_regular);

		TextView voucher_code = findViewById(R.id.input_activity_voucher_code_input);
		voucher_code.setTypeface(MyApplication.roboto_light);
		TextView voucher_secret_code = findViewById(R.id.input_activity_voucher_secret_code_input);
		voucher_secret_code.setTypeface(MyApplication.roboto_light);

		TextView check_button_text = findViewById(R.id.input_activity_check_button_text);
		check_button_text.setTypeface(MyApplication.roboto_regular);
		TextView check_button_icon = findViewById(R.id.input_activity_check_button_icon);
		check_button_icon.setTypeface(MyApplication.font_awesome);

	}

	public void closeActivity(View view) {
		finish();
	}

	public void checkVoucher(View view) {
		if (!is_loading) {
			EditText voucher_code_input = findViewById(R.id.input_activity_voucher_code_input);
			EditText voucher_secret_code_input = findViewById(R.id.input_activity_voucher_secret_code_input);
			String voucher_code = voucher_code_input.getText().toString();
			String voucher_secret_code = voucher_secret_code_input.getText().toString();
			if (voucher_code.equals("")) {
				Toast.makeText(getApplicationContext(), R.string.input_activity_empty_voucher_code_text, Toast.LENGTH_SHORT).show();
			}
			else if (voucher_secret_code.equals("")) {
				Toast.makeText(getApplicationContext(), R.string.input_activity_empty_voucher_secret_code_text, Toast.LENGTH_SHORT).show();
			}
			else {
				is_loading = true;
				sendToApi(voucher_code, voucher_secret_code);
			}
		}
	}

	public void sendToApi(final String voucher_code, final String voucher_secret_code) {

		RequestQueue queue = Volley.newRequestQueue(this);

		String url = "https://grabo.bg/api/checkvoucher?type=input&voucher_code=" + voucher_code + "&voucher_secret_code=" + voucher_secret_code;

		if (!eik.equals("")) {
			url = url + "&eik=" + eik;
		}

		JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
			new Response.Listener<JSONObject>()
			{
				@Override
				public void onResponse(JSONObject response) {
					is_loading = false;
					String result_type = "";
					try {
						result_type = response.getString("result");
					} catch (JSONException e) {
						// error
					}
					Intent result_intent = null;
					if (result_type.equals("OK")) {
						result_intent = new Intent(getApplicationContext(), SuccessActivity.class);
					} else if (result_type.equals("ERROR")) {
						result_intent = new Intent(getApplicationContext(), ErrorActivity.class);
					}
					if (null != result_intent) {
						result_intent.putExtra("voucher_code", voucher_code);
						result_intent.putExtra("voucher_secret_code", voucher_secret_code);
						result_intent.putExtra("input_type", "input");
						result_intent.putExtra("response", response.toString());
						startActivity(result_intent);
						finish();
					}
		        }
		    },
		    new Response.ErrorListener()
		    {
			    @Override
		         public void onErrorResponse(VolleyError error) {
				    is_loading = false;
				    Toast.makeText(getApplicationContext(), R.string.input_activity_no_response, Toast.LENGTH_SHORT).show();
		       }
		    }
		);

		queue.add(getRequest);

	}

}