package master.pwr.whereami.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import master.pwr.whereami.R;
import master.pwr.whereami.models.StatProperties;
import master.pwr.whereami.models.Stats;

/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-19.
 */
public class StatsAdapter extends BaseAdapter
{
    private static final String HEADER = "header";
    private final LayoutInflater layoutInflater;
    private List<String> data;
    private int fieldsNumber;

    public StatsAdapter(Context context, final List<Stats> statistics)
    {
        layoutInflater = LayoutInflater.from(context);

        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... params)
            {
                Field[] fields = Stats.class.getDeclaredFields();
                fieldsNumber = fields.length;
                data = new ArrayList<>(fieldsNumber * statistics.size());

                String[] temp = new String[fieldsNumber];
                for (Stats s : statistics)
                {
                    Arrays.fill(temp, null);

                    for (Field f : fields)
                    {
                        boolean originalFlag = f.isAccessible();
                        try
                        {
                            if (f.isAnnotationPresent(StatProperties.class))
                            {
                                StatProperties props = f.getAnnotation(StatProperties.class);
                                f.setAccessible(true);
                                temp[props.Order()] = String.format(props.StringFormat(), f.get(s));
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        finally
                        {
                            f.setAccessible(originalFlag);
                        }
                    }

                    data.add(HEADER);
                    data.addAll(Arrays.asList(temp));

                    /*
                    data.add(String.format("Metoda: %s", s.getMethodName()));
                    data.add(String.format("Dokładność: %s [m]", s.getAccuracy()));
                    data.add(String.format("Pozycja: %s", s.getPosition()));
                    data.add(String.format("Próba: %s", s.getAttempt()));
                    data.add(String.format("Czas operacji: %s [ms]" , s.getExecutionTime()));
                    data.add(String.format("Poziom baterii: %s [%%]", s.getBatteryLevel()));
                    data.add(String.format("Napięcie baterii:%s [mV]", s.getBatteryVoltage()));
                    */
                }
                return null;
            }
        }.execute();
    }

    @Override
    public int getItemViewType(int position)
    {
        return data.get(position).equals(HEADER) ? 0 : 1;
    }

    @Override
    public int getViewTypeCount()
    {
        return 2;
    }

    @Override
    public int getCount()
    {
        return data == null ? 0 : data.size();
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
        boolean isHeader = getItemViewType(position) == 0;
        if(convertView == null)
        {
            int layoutId = isHeader ? R.layout.list_item_header : android.R.layout.activity_list_item;
            convertView = layoutInflater.inflate(layoutId, parent, false);
            vh = new ViewHolder();
            vh.tv = ((TextView) convertView.findViewById(android.R.id.text1));
            convertView.setTag(vh);
        }
        else
        {
            vh = (ViewHolder) convertView.getTag();
        }

        if (isHeader)
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
