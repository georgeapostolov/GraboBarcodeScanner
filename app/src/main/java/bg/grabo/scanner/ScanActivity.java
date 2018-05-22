package bg.grabo.scanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import info.androidhive.barcode.BarcodeReader;

public class ScanActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {

    private BarcodeReader barcodeReader;
	public SharedPreferences settings;
	public String eik = "";
	public Boolean can_sound = false;
	public Boolean can_vibrate = false;
    private boolean is_loading = false;
    public Vibrator v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_activity);

	    v = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_fragment);
	    settings = getSharedPreferences(MainActivity.SAVED_VARIABLES, 0);
	  	eik = settings.getString("EIK", "");
	    can_sound = settings.getBoolean("can_sound", true);
	    can_vibrate = settings.getBoolean("can_vibrate", true);

        TextView back_button = findViewById(R.id.scan_activity_back_button);
        back_button.setTypeface(MyApplication.material_icons);

	    TextView top_text = findViewById(R.id.scan_activity_top_text);
	    top_text.setTypeface(MyApplication.roboto_regular);
	    TextView bottom_text = findViewById(R.id.scan_activity_bottom_text);
	    bottom_text.setTypeface(MyApplication.roboto_regular);

    }

	@Override
	public void onPause() {
		super.onPause();
		barcodeReader.pauseScanning();
	}

	@Override
	public void onResume() {
		super.onResume();
		barcodeReader.resumeScanning();
	}

    @Override
    public void onScanned(final Barcode barcode) {
    	if (!is_loading) {
    		if (can_sound) {
			    barcodeReader.playBeep();
		    }
		    if (can_vibrate) {
                if (null != v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(250, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        v.vibrate(250);
                    }
                }
		    }
	        runOnUiThread(new Runnable() {
	            @Override
	            public void run() {
		            String scanned = barcode.displayValue;
		            String regex = "\\d+";
		            boolean code_found = false;
		            if (scanned.matches(regex)) {
			            code_found = true;
			            is_loading = true;
			            codeFound(scanned, "", "", "barcode");
		            } else {
			            if (scanned.contains("grabo.bg/check")) {
				            Uri uri = Uri.parse(scanned);
				            String voucher_code = uri.getQueryParameter("c");
				            String voucher_secret_code = uri.getQueryParameter("s");
				            code_found = true;
				            is_loading = true;
				            codeFound("", voucher_code, voucher_secret_code, "qr");
			            }
		            }
		            if (!code_found) {
			            Toast.makeText(getApplicationContext(), R.string.scan_activity_invalid_barcode, Toast.LENGTH_SHORT).show();
		            }
	            }
	        });
	    }
    }

	@Override
	public void onScannedMultiple(List<Barcode> barcodes) {}

	@Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {}

    @Override
    public void onScanError(String errorMessage) {}

    @Override
    public void onCameraPermissionDenied() {
        Toast.makeText(getApplicationContext(), "Достъпът до камерата е забранен!", Toast.LENGTH_SHORT).show();
        finish();
    }

	public void codeFound(final String barcode, final String voucher_code, final String voucher_secret_code, final String code_type) {

		RequestQueue queue = Volley.newRequestQueue(this);

		String url = "https://grabo.bg/api/checkvoucher?";

		if (code_type.equals("barcode")) {
			url = url + "type=barcode&barcode=" + barcode;
		} else {
			url = url + "type=qr&voucher_code=" + voucher_code + "&voucher_secret_code=" + voucher_secret_code;
		}

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
						result_intent.putExtra("barcode", barcode);
						result_intent.putExtra("voucher_code", voucher_code);
						result_intent.putExtra("voucher_secret_code", voucher_secret_code);
						result_intent.putExtra("input_type", code_type);
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
				    Toast.makeText(getApplicationContext(), R.string.scan_activity_no_response, Toast.LENGTH_SHORT).show();
		       }
		    }
		);

		queue.add(getRequest);

	}

	public void closeActivity(View view) {
		finish();
	}

}