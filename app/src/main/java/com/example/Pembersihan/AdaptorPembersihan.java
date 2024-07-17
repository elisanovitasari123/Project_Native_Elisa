package com.example.Pembersihan;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Pembersihan.ConfigurasiPembersihan;
import com.example.Pembersihan.EditPembersihan;
import com.example.Pembersihan.GetDataPembersihan;
import com.example.mobilehotelgroup2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdaptorPembersihan extends BaseAdapter {
    private Context context;
    private ArrayList<GetDataPembersihan> model;
    private LayoutInflater inflater;
    private ProgressBar progressBar;
    private DataChangeListener dataChangeListener;

    public interface DataChangeListener {
        void onDataChanged();
    }

    public AdaptorPembersihan(Context context, ArrayList<GetDataPembersihan> model, ProgressBar progressBar, DataChangeListener dataChangeListener) {
        this.context = context;
        this.model = model;
        this.progressBar = progressBar;
        this.dataChangeListener = dataChangeListener;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return model.size();
    }

    @Override
    public Object getItem(int position) {
        return model.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_pembersihan, parent, false);
            holder = new ViewHolder();
            holder.namaKamar = convertView.findViewById(R.id.namaKamar);
            holder.nama_karyawan = convertView.findViewById(R.id.namaKaryawan);
            holder.tanggal = convertView.findViewById(R.id.tanggal);
            holder.deskripsi = convertView.findViewById(R.id.deskripsi);
            holder.deleteButton = convertView.findViewById(R.id.deleteBooking);
            holder.editButton = convertView.findViewById(R.id.editBooking);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final GetDataPembersihan data = model.get(position);
        holder.namaKamar.setText(data.getNamaKamar());
        holder.nama_karyawan.setText(data.getNamaKaryawan().toString());
        holder.tanggal.setText(data.getTanggal().toString());
        holder.deskripsi.setText(data.getDeskripsi().toString());

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditPembersihan.class);
                intent.putExtra("id_p", data.getIdP());
                intent.putExtra("namaKamar", data.getNamaKamar());
                intent.putExtra("nama_karyawan", data.getNamaKaryawan());
                intent.putExtra("tanggal", data.getTanggal());
                intent.putExtra("deskripsi", data.getDeskripsi().toString());
                context.startActivity(intent);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData(data.getIdP(), position);
            }
        });

        return convertView;
    }

    private void deleteData(final String id_p, final int position) {
        String url = new ConfigurasiPembersihan().baseUrl() + "delete_pembersihan.php";

        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if ("success".equals(status)) {
                                Toast.makeText(context, "Data Berhasil Di Hapus", Toast.LENGTH_SHORT).show();
                                model.remove(position);
                                notifyDataSetChanged();
                                if (dataChangeListener != null) {
                                    dataChangeListener.onDataChanged();
                                }
                            } else {
                                Toast.makeText(context, "Data Gagal Di Hapus", Toast.LENGTH_SHORT).show();
                                if (dataChangeListener != null) {
                                    dataChangeListener.onDataChanged();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error parsing response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            if (dataChangeListener != null) {
                                dataChangeListener.onDataChanged();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(context, "Error deleting data: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                        if (dataChangeListener != null) {
                            dataChangeListener.onDataChanged();
                        }
                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> form = new HashMap<>();
                form.put("id_p", String.valueOf(id_p));
                return form;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static class ViewHolder {
        TextView  namaKamar, nama_karyawan, tanggal, deskripsi;

        ImageButton editButton, deleteButton;
        FrameLayout frameL;
    }
}
