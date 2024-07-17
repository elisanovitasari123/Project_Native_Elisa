package com.example.K_Fasilitas;

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
import com.example.K_Fasilitas.ConfigurasiKFasilitas;
import com.example.K_Fasilitas.EditKFasilitas;
import com.example.K_Fasilitas.GetDataKFasilitas;
import com.example.mobilehotelgroup2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdaptorKFasilitas extends BaseAdapter {
    private Context context;
    private ArrayList<GetDataKFasilitas> model;
    private LayoutInflater inflater;
    private ProgressBar progressBar;
    private DataChangeListener dataChangeListener;

    public interface DataChangeListener {
        void onDataChanged();
    }

    public AdaptorKFasilitas(Context context, ArrayList<GetDataKFasilitas> model, ProgressBar progressBar, DataChangeListener dataChangeListener) {
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
            convertView = inflater.inflate(R.layout.list_k_fasilitas, parent, false);
            holder = new ViewHolder();
            holder.namaVilla = convertView.findViewById(R.id.namaVilla);
            holder.nama_fasilitas =convertView.findViewById(R.id.nama_fasilitas);
            holder.namaKamar = convertView.findViewById(R.id.namaKamar);
            holder.status_fasilitas = convertView.findViewById(R.id.status_fasilitas);
            holder.deleteButton = convertView.findViewById(R.id.deleteBooking);
            holder.editButton = convertView.findViewById(R.id.editBooking);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final GetDataKFasilitas data = model.get(position);
        holder.namaVilla.setText(data.getNamaVilla());
        holder.nama_fasilitas.setText(data.getNamaFasilitas());
        holder.namaKamar.setText(data.getNamaKamar());
        holder.status_fasilitas.setText(data.getStatusFasilitas().toString());


        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditKFasilitas.class);
                intent.putExtra("id_k_fasilitas", data.getIdKFasilitas());
                intent.putExtra("namaVilla", data.getNamaVilla());
                intent.putExtra("nama_fasilitas", data.getNamaFasilitas());
                intent.putExtra("namaKamar", data.getNamaKamar());
                intent.putExtra("status_fasilitas", data.getStatusFasilitas());
                context.startActivity(intent);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData(data.getIdKFasilitas(), position);
            }
        });

        return convertView;
    }

    private void deleteData(final String id_k_fasilitas, final int position) {
        String url = new ConfigurasiKFasilitas().baseUrl() + "delete_k_fasilitas.php";

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
                form.put("id_k_fasilitas", String.valueOf(id_k_fasilitas));
                return form;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static class ViewHolder {
        TextView namaVilla, nama_fasilitas, namaKamar, status_fasilitas;
        ImageButton editButton, deleteButton;
        FrameLayout frameL;
    }
}
