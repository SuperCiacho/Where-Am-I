package master.pwr.whereami.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import master.pwr.whereami.enums.LocationStrategyType;

/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-16.
 */
public class LocationStrategiesAdapter extends BaseAdapter
{
    private LayoutInflater inflater;

    public LocationStrategiesAdapter(Context context)
    {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount()
    {
        return LocationStrategyType.values().length;
    }

    @Override
    public Object getItem(int position)
    {
        return LocationStrategyType.values()[position];
    }

    @Override
    public long getItemId(int position)
    {
        return LocationStrategyType.values()[position].hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder vh;

        if(convertView == null)
        {
            convertView = inflater.inflate(android.R.layout.simple_list_item_activated_1, parent, false);
            vh = new ViewHolder();
            vh.textView = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(vh);
        }
        else
        {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.textView.setText(LocationStrategyType.values()[position].getDescription());

        return convertView;
    }

    private static final class ViewHolder
    {
        TextView textView;
    }
}
