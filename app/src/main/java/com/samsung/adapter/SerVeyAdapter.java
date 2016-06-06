package com.samsung.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.samsung.object.Dealer;
import com.samsung.object.ServeyOject;

import java.util.ArrayList;

import samsung.com.suveyapplication.R;

/**
 * Created by SamSunger on 5/14/2015.
 */
public class SerVeyAdapter extends ArrayAdapter<ServeyOject> {
    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    private Context mContext;
    private ArrayList<ServeyOject> mListDealer ;

    public SerVeyAdapter(Context context, int resource, ArrayList<ServeyOject> objects) {
        super(context, resource, objects);
              mListDealer =objects;
        mContext=context;
    }

    class ViewHolder{
        TextView ServeyName;
        TextView ServeyDescription;
        TextView TimeDone ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=convertView;
        ViewHolder holder=null;

        if(v==null){
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.servey_item_layout, parent, false);
            holder = new ViewHolder();
            holder.ServeyName = (TextView) v.findViewById(R.id.txtServeyName);
            holder.ServeyDescription = (TextView) v.findViewById(R.id.txtDescription);
            holder.TimeDone=(TextView) v.findViewById(R.id.txttimedone);
            v.setTag(holder);
        }else{
            holder = (ViewHolder) v.getTag();
        }
        final ServeyOject serveyOject =mListDealer.get(position);
        holder.ServeyName.setText(serveyOject.getNOMBRE());
        holder.ServeyDescription.setText(serveyOject.getDESCRIPCION());
        return v;
    }
}
