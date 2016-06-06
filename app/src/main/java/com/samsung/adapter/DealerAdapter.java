package com.samsung.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.samsung.object.Dealer;

import java.util.ArrayList;
import java.util.List;

import samsung.com.suveyapplication.R;

/**
 * Created by SamSunger on 5/14/2015.
 */
public class DealerAdapter extends ArrayAdapter<Dealer> {
    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     * instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    private Context mContext;
    private ArrayList<Dealer> mListDealer;
    private Filter filter;

    public DealerAdapter(Context context, int resource, ArrayList<Dealer> objects) {
        super(context, resource, objects);
        mListDealer = objects;
        mContext = context;
    }

    class ViewHolder {
        TextView DealerName;
        TextView Address;
        TextView City;
        TextView Dia_de;
        ImageView Status;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder = null;

        if (v == null) {

            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.dealeritem, parent, false);
            holder = new ViewHolder();

            holder.DealerName = (TextView) v.findViewById(R.id.txtDearlerName);
            holder.Address = (TextView) v.findViewById(R.id.txtAdress);

            holder.City = (TextView) v.findViewById(R.id.txt_city);
            holder.Dia_de = (TextView) v.findViewById(R.id.txt_de);

            holder.Status = (ImageView) v.findViewById(R.id.imgstatus);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        final Dealer dealer = mListDealer.get(position);
        holder.DealerName.setText(dealer.getDealerName());
        holder.Address.setText(dealer.getAdress());
        holder.City.setText(dealer.getCity() + "," + dealer.getDistric());
        holder.Dia_de.setText("Dia de Cita");

        Log.e("DealerAdapter: ", dealer.toString());
        if (dealer.getStatus().trim().equals("False")) {
            holder.Status.setImageDrawable(mContext.getResources().getDrawable(R.drawable.less));
        } else {
            holder.Status.setImageDrawable(mContext.getResources().getDrawable(R.drawable.many));
        }
        return v;
    }

    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new AppFilter<Dealer>(mListDealer);
        return filter;
    }

    private class AppFilter<T> extends Filter {

        private ArrayList<T> sourceObjects;

        public AppFilter(List<T> objects) {
            sourceObjects = new ArrayList<T>();
            synchronized (this) {
                sourceObjects.addAll(objects);
            }
        }

        @Override
        protected FilterResults performFiltering(CharSequence chars) {
            String filterSeq = chars.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (filterSeq != null && filterSeq.length() > 0) {
                ArrayList<T> filter = new ArrayList<T>();

                for (T object : sourceObjects) {
                    // the filtering itself:
                    if (object.toString().toLowerCase().contains(filterSeq))
                        filter.add(object);
                }
                result.count = filter.size();
                result.values = filter;
            } else {
                // add all objects
                synchronized (this) {
                    result.values = sourceObjects;
                    result.count = sourceObjects.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            // NOTE: this function is *always* called from the UI thread.
            ArrayList<T> filtered = (ArrayList<T>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = filtered.size(); i < l; i++)
                add((Dealer) filtered.get(i));
            notifyDataSetInvalidated();
        }
    }


}
