package com.example.Karyawan;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Room.ConfigurasiRoom;
import com.example.Room.EditDataRoom;
import com.example.Room.GetDataRoom;
import com.example.mobilehotelgroup2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdaptorKaryawan extends BaseAdapter {
    private final Context context;
    private ArrayList<GetDataKaryawan> modelKaryawan;
    private final LayoutInflater inflater;
    private final DataChangeListener dataChangeListener;
    private final ProgressBar progressBar;

    public AdaptorKaryawan(Context context, ArrayList<GetDataKaryawan> modelKaryawan, ProgressBar progressBar, DataChangeListener dataChangeListener) {
        this.context = context;
        this.modelKaryawan = modelKaryawan;
        this.progressBar = progressBar;
        this.dataChangeListener = dataChangeListener;
        this.inflater = LayoutInflater.from(context);
    }

    public interface DataChangeListener {
        void onDataChanged();
    }

    @Override
    public int getCount() {
        return modelKaryawan.size();
    }

    @Override
    public Object getItem(int position) {
        return modelKaryawan.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_karyawan, parent, false);
            holder = new ViewHolder();
            holder.nama_karyawan = convertView.findViewById(R.id.namaKaryawan);
            holder.no_hp = convertView.findViewById(R.id.no_hp);
            holder.alamat = convertView.findViewById(R.id.alamat);
            holder.editButton = convertView.findViewById(R.id.editKaryawan);
            holder.deleteButton = convertView.findViewById(R.id.deleteKaryawan);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final GetDataKaryawan data = modelKaryawan.get(position);
        holder.nama_karyawan.setText(data.getNamaKaryawan());
        holder.no_hp.setText(data.getNoHp());
        holder.alamat.setText(data.getAlamat());

        holder.editButton.setOnClickListener(view -> {
            Intent intent = new Intent(context, EditDataKaryawan.class);
            intent.putExtra("id_karyawan", data.getIdKaryawan());
            intent.putExtra("nama_karyawan", data.getNamaKaryawan());
            intent.putExtra("no_hp", data.getNoHp());
            intent.putExtra("alamat", data.getAlamat());
            context.startActivity(intent);
        });

        holder.deleteButton.setOnClickListener(v -> deleteData(data.getIdKaryawan(), position));

        return convertView;
    }

    public void updateData(ArrayList<GetDataKaryawan> newData) {
        this.modelKaryawan = newData;
        notifyDataSetChanged();
    }

    private void deleteData(final String id_karyawan, final int position) {
        String url = new ConfigurasiKaryawan().baseUrl() + "delete_karyawan.php";

        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }
                    handleDeleteResponse(response, position);
                },
                error -> {
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }
                    String errorMessage = (error.getMessage() != null) ? error.getMessage() : error.toString();
                    showToast("Error deleting data: " + errorMessage);
                    notifyDataChanged();
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("id_karyawan", id_karyawan);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private void handleDeleteResponse(String response, int position) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.optString("status", "error");
            if ("success".equals(status)) {
                showToast("Data Berhasil Di Hapus");
                modelKaryawan.remove(position);
                notifyDataSetChanged();
            } else {
                String message = jsonObject.optString("message", "Data Gagal Di Hapus");
                showToast(message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            showToast("Error parsing response");
        }
        notifyDataChanged();
    }

    private void notifyDataChanged() {
        if (dataChangeListener != null) {
            dataChangeListener.onDataChanged();
        }
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private static class ViewHolder {
        TextView nama_karyawan, no_hp, alamat;
        ImageButton editButton, deleteButton;
    }
}
