package com.example.Fasilitas;

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

public class AdaptorFasilitas extends BaseAdapter {
    private final Context context;
    private ArrayList<GetDataFasilitas> modelFasilitas;
    private final LayoutInflater inflater;
    private final DataChangeListener dataChangeListener;
    private final ProgressBar progressBar;

    public AdaptorFasilitas(Context context, ArrayList<GetDataFasilitas> modelFasilitas, ProgressBar progressBar, DataChangeListener dataChangeListener) {
        this.context = context;
        this.modelFasilitas = modelFasilitas;
        this.progressBar = progressBar;
        this.dataChangeListener = dataChangeListener;
        this.inflater = LayoutInflater.from(context);
    }

    public interface DataChangeListener {
        void onDataChanged();
    }

    @Override
    public int getCount() {
        return modelFasilitas.size();
    }

    @Override
    public Object getItem(int position) {
        return modelFasilitas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_fasilitas, parent, false);
            holder = new ViewHolder();
            holder.nama_fasilitas = convertView.findViewById(R.id.namaFasilitas);
            holder.kategori = convertView.findViewById(R.id.kategori);
            holder.editButton = convertView.findViewById(R.id.editFasilitas);
            holder.deleteButton = convertView.findViewById(R.id.deleteFasilitas);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final GetDataFasilitas data = modelFasilitas.get(position);
        holder.nama_fasilitas.setText(data.getNamaFasilitas());
        holder.kategori.setText(data.getKategori());

        holder.editButton.setOnClickListener(view -> {
            Intent intent = new Intent(context, EditDataFasilitas.class);
            intent.putExtra("id_fasilitas", data.getIdFasilitas());
            intent.putExtra("nama_fasilitas", data.getNamaFasilitas());
            intent.putExtra("kategori", data.getKategori());
            context.startActivity(intent);
        });

        holder.deleteButton.setOnClickListener(v -> deleteData(data.getIdFasilitas(), position));

        return convertView;
    }

    public void updateData(ArrayList<GetDataFasilitas> newData) {
        this.modelFasilitas = newData;
        notifyDataSetChanged();
    }

    private void deleteData(final String id_fasilitas, final int position) {
        String url = new ConfigurasiFasilitas().baseUrl() + "delete_fasilitas.php";

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
                params.put("id_fasilitas", id_fasilitas);
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
                modelFasilitas.remove(position);
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
        TextView nama_fasilitas, kategori;
        ImageButton editButton, deleteButton;
    }
}
