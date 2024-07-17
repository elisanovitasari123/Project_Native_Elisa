package com.example.Pembersihan;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Booking.ConfigurasiBooking;
import com.example.mobilehotelgroup2.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditPembersihan extends AppCompatActivity {

    private EditText  editNamaKamar, editNamaKaryawan, editTanggal, editDeskripsi;
    private Button updateButton;
    private String id_p;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pembersihan);

        editNamaKamar = findViewById(R.id.edit_namaKamar);
        editNamaKaryawan = findViewById(R.id.edit_karyawan);
        editTanggal = findViewById(R.id.edit_tanggal);
        editDeskripsi = findViewById(R.id.deskripsi);
        updateButton = findViewById(R.id.saveBooking);

        // Mendapatkan data dari intent
        Intent intent = getIntent();
        if (intent != null) {
            id_p = intent.getStringExtra("id_p");
            editNamaKamar.setText(intent.getStringExtra("namaKamar"));
            editNamaKaryawan.setText(intent.getStringExtra("nama_karyawan"));
            editTanggal.setText(intent.getStringExtra("tanggal"));
            editDeskripsi.setText(intent.getStringExtra("deskripsi"));
        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });
    }
    // Listener untuk tanggal check-in dan check-out
//    private void setupDatePickerListeners() {
//        // Listener untuk check-in date
//        editCheckInDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDatePickerDialog(editCheckInDate);
//            }
//        });
//
//        // Listener untuk check-out date
//        editCheckOutDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDatePickerDialog(editCheckOutDate);
//            }
//        });
//    }
//
//    // Method untuk menampilkan dialog date picker
//    public void showDatePickerDialog(View view) {
//        final EditText editText = (EditText) view;
//        final Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//        DatePickerDialog datePickerDialog = new DatePickerDialog(EditPembersihan.this,
//                new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        // Month is 0-based, so add 1 to display correct month
//                        String selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
//                        editText.setText(selectedDate);
//                    }
//                }, year, month, day);
//        datePickerDialog.show();
//    }

    private void updateData() {
        final String namaKamar = editNamaKamar.getText().toString().trim();
        final String nama_karyawan = editNamaKaryawan.getText().toString().trim();
        final String tanggal = editTanggal.getText().toString().trim();
        final String deskripsi = editDeskripsi.getText().toString().trim();

        // Validasi input
        if ( namaKamar.isEmpty() || nama_karyawan.isEmpty() || tanggal.isEmpty() || deskripsi.isEmpty()) {
            Toast.makeText(EditPembersihan.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        // URL untuk skrip PHP yang akan melakukan update data
        String url = new ConfigurasiPembersihan().baseUrl() + "update_pembersihan.php";

        // Buat request baru
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tangani respons dari server
                        Log.d("EditBooking", "Response: " + response);
                        if (response.equalsIgnoreCase("success")) {
                            Toast.makeText(EditPembersihan.this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
                            sendRefreshFlag(true); // Mengirim flag refresh untuk memuat ulang data
                            finish(); // Tutup aktivitas
                        } else {
                            Toast.makeText(EditPembersihan.this, "berhasil memperbarui data", Toast.LENGTH_SHORT).show();
                            sendRefreshFlag(true); // Mengirim flag refresh untuk memuat ulang data
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tangani kesalahan
                        Log.e("EditBooking", "Error: " + error.getMessage());
                        Toast.makeText(EditPembersihan.this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Buat parameter untuk request
                Map<String, String> params = new HashMap<>();
                params.put("id_p", id_p);
                params.put("namaKamar", namaKamar);
                params.put("nama_karyawan", nama_karyawan);
                params.put("tanggal", tanggal);
                params.put("deskripsi", deskripsi); // Sesuaikan dengan nama kolom di server PHP
                Log.d("EditBooking", "Params: " + params.toString()); // Log parameter untuk debugging
                return params;
            }
        };

        // Tambahkan request ke RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void sendRefreshFlag(boolean refresh) {
        Intent intent = new Intent();
        intent.putExtra("refreshflag", refresh);
        setResult(RESULT_OK, intent);
    }
}
