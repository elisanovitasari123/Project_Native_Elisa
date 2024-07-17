package com.example.mobilehotelgroup2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.configurasi.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistActivity extends AppCompatActivity {

    private EditText EditUsername, EditPassword;
    private ProgressDialog pDialog;

    private static final String TAG = "RegistActivity";
    private static final String REGIST_URL = Config.baseUrl()+"/e-HotelMobile/regist.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        pDialog = new ProgressDialog(RegistActivity.this);
        EditUsername = findViewById(R.id.EditUsername);
        EditPassword = findViewById(R.id.EditPassword);

        Button buttonRegist = findViewById(R.id.buttonRegist);
        buttonRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDataVolley();
            }
        });

        TextView textLogin = findViewById(R.id.textLogin);
        textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void saveDataVolley() {
        final String Username = EditUsername.getText().toString().trim();
        final String Password = EditPassword.getText().toString().trim();

        if (Username.isEmpty() || Password.isEmpty()) {
            Toast.makeText(RegistActivity.this, "Username atau Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        pDialog.setMessage("Proses Registrasi...");
        pDialog.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, REGIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        Log.d(TAG, "Response: " + response);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");
                            String message = jsonResponse.getString("message");

                            Toast.makeText(RegistActivity.this, message, Toast.LENGTH_SHORT).show();

                            if (status.equals("success")) {
                                // Registrasi berhasil, kembali ke halaman login
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RegistActivity.this, "Kesalahan parsing respons", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        error.printStackTrace();
                        Toast.makeText(RegistActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("NamaUser", Username);
                params.put("Pass", Password);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(postRequest);
    }
}
