package master.pwr.whereami.adapters;

import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import master.pwr.whereami.R;
import master.pwr.whereami.models.Stats;

/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-19.
 */
public class StatsAdapter extends BaseAdapter
{
    List<String> data;

    public StatsAdapter(final List<Stats> statistics)
    {
        data = new ArrayList<>(7 * statistics.size());

        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... params)
            {
                for (Stats s : statistics)
                {
                    data.add("header");
                    data.add(String.format("Metoda: %s", s.getMethodName()));
                    data.add(String.format("Pozycja: %s", s.getPosition()));
                    data.add(String.format("Dokładność: %s [m]", s.getPosition()));
                    data.add(String.format("Czas operacji: %s [ms]" , s.getExecutionTime()));
                    data.add(String.format("Poziom baterii: %s [%%]", s.getBatteryLevel()));
                    data.add(String.format("Napięcie baterii:%s [mV]", s.getBatteryVoltage()));
                }
                return null;
            }
        }.execute();
    }

    @Override
    public int getItemViewType(int position)
    {
        return data.get(position).equals("header") ?
                R.layout.list_item_header :
                android.R.layout.activity_list_item;
    }

    @Override
    public int getViewTypeCount()
    {
        return 2;
    }

    @Override
    public int getCount()
    {
        return data.size();
    }

    @Override
    public Object getItem(int position)
    {
        return data.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return data.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder vh;
        if(convertView == null)
        {
            convertView = LayoutInflater.from(parent.getContext())
                                        .inflate(getItemViewType(position), parent, false);
            vh = new ViewHolder();
            vh.tv = ((TextView) convertView.findViewById(android.R.id.text1));
            convertView.setTag(vh);
        }
        else
        {
            vh = (ViewHolder) convertView.getTag();
        }

        if (getItemViewType(position) == R.layout.list_item_header)
        {
            vh.tv.setText("Pomiar " + ((position / 7) + 1));
        }
        else
        {
            vh.tv.setText(data.get(position));
        }

        return convertView;
    }

    private static class ViewHolder
    {
        TextView tv;
    }
}
