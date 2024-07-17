package com.example.customer;

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

public class Adaptor extends BaseAdapter {
    private Context context;
    private ArrayList<GetData> model;
    private LayoutInflater inflater;
    private DataChangeListener dataChangeListener;
    private ProgressBar progressBar;

    public Adaptor(Context context, ArrayList<GetData> model, ProgressBar progressBar, DataChangeListener dataChangeListener) {
        this.context = context;
        this.model = model;
        this.progressBar = progressBar;
        this.dataChangeListener = dataChangeListener;
        this.inflater = LayoutInflater.from(context);
    }

    public interface DataChangeListener {
        void onDataChanged();
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
            convertView = inflater.inflate(R.layout.list_customer, parent, false);
            holder = new ViewHolder();
            holder.nama = convertView.findViewById(R.id.nama);
            holder.Phone = convertView.findViewById(R.id.Phone);
            holder.addres = convertView.findViewById(R.id.addres);
            holder.editButton = convertView.findViewById(R.id.edit);
            holder.deleteButton = convertView.findViewById(R.id.delete);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final GetData data = model.get(position);
        holder.nama.setText(data.getNama());
        holder.Phone.setText(data.getPhone());
        holder.addres.setText(data.getAddres());

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(context, EditDataCustomer.class);
                intent.putExtra("id", data.getId());
                intent.putExtra("nama", data.getNama());
                intent.putExtra("phone", data.getPhone());
                intent.putExtra("addres", data.getAddres());

                context.startActivity(intent);
            }
        });

        holder.deleteButton.setOnClickListener(v -> deleteData(data.getId(), position));

        return convertView;
    }

    public void reloadData() {
        notifyDataSetChanged();
    }

    private void deleteData(final String id, final int position) {
        String url = new Configurasi().baseUrl() + "deletecustomer.php";

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
                    showToast("Error deleting data");
                    notifyDataChanged();
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> form = new HashMap<>();
                form.put("id", id);
                return form;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private void handleDeleteResponse(String response, int position) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            if ("success".equals(status)) {
                showToast("Data Berhasil Di Hapus");
                model.remove(position);
                notifyDataSetChanged();
            } else {
                showToast("Data Gagal Di Hapus");
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
        TextView nama, Phone, addres;
        ImageButton editButton, deleteButton;
    }
}
