package com.example.Room;

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
import com.example.mobilehotelgroup2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdaptorRoom extends BaseAdapter {
    private final Context context;
    private ArrayList<GetDataRoom> modelRoom;
    private final LayoutInflater inflater;
    private final DataChangeListener dataChangeListener;
    private final ProgressBar progressBar;

    public AdaptorRoom(Context context, ArrayList<GetDataRoom> modelRoom, ProgressBar progressBar, DataChangeListener dataChangeListener) {
        this.context = context;
        this.modelRoom = modelRoom;
        this.progressBar = progressBar;
        this.dataChangeListener = dataChangeListener;
        this.inflater = LayoutInflater.from(context);
    }

    public interface DataChangeListener {
        void onDataChanged();
    }

    @Override
    public int getCount() {
        return modelRoom.size();
    }

    @Override
    public Object getItem(int position) {
        return modelRoom.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_room, parent, false);
            holder = new ViewHolder();
            holder.namaKamar = convertView.findViewById(R.id.namaRoom);
            holder.tipeKamar = convertView.findViewById(R.id.tipeRoom);
            holder.harga = convertView.findViewById(R.id.harga);
            holder.editButton = convertView.findViewById(R.id.editRoom);
            holder.deleteButton = convertView.findViewById(R.id.deleteRoom);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final GetDataRoom data = modelRoom.get(position);
        holder.namaKamar.setText(data.getNamaKamar());
        holder.tipeKamar.setText(data.getTipeKamar());
        holder.harga.setText(data.getHarga());

        holder.editButton.setOnClickListener(view -> {
            Intent intent = new Intent(context, EditDataRoom.class);
            intent.putExtra("roomid", data.getRoomId());
            intent.putExtra("namaKamar", data.getNamaKamar());
            intent.putExtra("tipeKamar", data.getTipeKamar());
            intent.putExtra("harga", data.getHarga());
            context.startActivity(intent);
        });

        holder.deleteButton.setOnClickListener(v -> deleteData(data.getRoomId(), position));

        return convertView;
    }

    public void updateData(ArrayList<GetDataRoom> newData) {
        this.modelRoom = newData;
        notifyDataSetChanged();
    }

    private void deleteData(final String roomId, final int position) {
        String url = new ConfigurasiRoom().baseUrl() + "delete_room.php";

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
                params.put("roomid", roomId);
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
                modelRoom.remove(position);
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
        TextView namaKamar, tipeKamar, harga;
        ImageButton editButton, deleteButton;
    }
}
