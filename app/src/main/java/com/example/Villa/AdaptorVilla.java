package com.example.Villa;

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

public class AdaptorVilla extends BaseAdapter {
    private final Context context;
    private ArrayList<GetDataVilla> modelVilla;
    private final LayoutInflater inflater;
    private final DataChangeListener dataChangeListener;
    private final ProgressBar progressBar;

    public AdaptorVilla(Context context, ArrayList<GetDataVilla> modelVilla, ProgressBar progressBar, DataChangeListener dataChangeListener) {
        this.context = context;
        this.modelVilla = modelVilla;
        this.progressBar = progressBar;
        this.dataChangeListener = dataChangeListener;
        this.inflater = LayoutInflater.from(context);
    }

    public interface DataChangeListener {
        void onDataChanged();
    }

    @Override
    public int getCount() {
        return modelVilla.size();
    }

    @Override
    public Object getItem(int position) {
        return modelVilla.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_villa, parent, false);
            holder = new ViewHolder();
            holder.namaVilla = convertView.findViewById(R.id.namaVilla);
            holder.kontak = convertView.findViewById(R.id.kontak);
            holder.email = convertView.findViewById(R.id.email);
            holder.lokasi = convertView.findViewById(R.id.lokasi);
            holder.editButton = convertView.findViewById(R.id.editVilla);
            holder.deleteButton = convertView.findViewById(R.id.deleteVilla);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final GetDataVilla data = modelVilla.get(position);
        holder.namaVilla.setText(data.getNamaVilla());
        holder.kontak.setText(data.getKontak());
        holder.email.setText(data.getEmail());
        holder.lokasi.setText(data.getLokasi());

        holder.editButton.setOnClickListener(view -> {
            Intent intent = new Intent(context, EditDataVilla.class);
            intent.putExtra("id_villa", data.getIdVilla());
            intent.putExtra("namaVilla", data.getNamaVilla());
            intent.putExtra("kontak", data.getKontak());
            intent.putExtra("email", data.getEmail());
            intent.putExtra("lokasi", data.getLokasi());
            context.startActivity(intent);
        });

        holder.deleteButton.setOnClickListener(v -> deleteData(data.getIdVilla(), position));

        return convertView;
    }

    public void updateData(ArrayList<GetDataVilla> newData) {
        this.modelVilla = newData;
        notifyDataSetChanged();
    }

    private void deleteData(final String id_villa, final int position) {
        String url = new ConfigurasiVilla().baseUrl() + "delete_villa.php";

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
                params.put("id_villa", id_villa);
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
                modelVilla.remove(position);
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
        TextView namaVilla, kontak, email, lokasi;
        ImageButton editButton, deleteButton;
    }
}
