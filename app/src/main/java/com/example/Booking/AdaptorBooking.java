package com.example.Booking;

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
import com.example.mobilehotelgroup2.R;

import androidx.annotation.Nullable;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdaptorBooking extends BaseAdapter {
    private Context context;
    private ArrayList<GetDataBooking> model;
    private LayoutInflater inflater;
    private ProgressBar progressBar;
    private DataChangeListener dataChangeListener;

    public interface DataChangeListener {
        void onDataChanged();
    }

    public AdaptorBooking(Context context, ArrayList<GetDataBooking> model, ProgressBar progressBar, DataChangeListener dataChangeListener) {
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
            convertView = inflater.inflate(R.layout.list_booking, parent, false);
            holder = new ViewHolder();
            holder.nama = convertView.findViewById(R.id.nama);
            holder.namaKamar = convertView.findViewById(R.id.namaKamar);
            holder.check_in_date = convertView.findViewById(R.id.check_in_date);
            holder.check_out_date = convertView.findViewById(R.id.check_out_date);
            holder.total_price = convertView.findViewById(R.id.total_price);
            holder.deleteButton = convertView.findViewById(R.id.deleteBooking);
            holder.editButton = convertView.findViewById(R.id.editBooking);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final GetDataBooking data = model.get(position);
        holder.nama.setText(data.getNama());
        holder.namaKamar.setText(data.getNamaKamar());
        holder.check_in_date.setText(data.getCheckInDate().toString());
        holder.check_out_date.setText(data.getCheckOutDate().toString());
        holder.total_price.setText(data.getTotalPrice().toString());

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditBooking.class);
                intent.putExtra("booking_id", data.getBookingId());
                intent.putExtra("nama", data.getNama());
                intent.putExtra("namaKamar", data.getNamaKamar());
                intent.putExtra("check_in_date", data.getCheckInDate());
                intent.putExtra("check_out_date", data.getCheckOutDate());
                intent.putExtra("total_price", data.getTotalPrice().toString());
                context.startActivity(intent);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData(data.getBookingId(), position);
            }
        });

        return convertView;
    }

    private void deleteData(final String booking_id, final int position) {
        String url = new ConfigurasiBooking().baseUrl() + "delete_booking.php";

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
                form.put("booking_id", String.valueOf(booking_id));
                return form;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static class ViewHolder {
        TextView nama, namaKamar, check_in_date, check_out_date, total_price;
        ImageButton editButton, deleteButton;
        FrameLayout frameL;
    }
}
