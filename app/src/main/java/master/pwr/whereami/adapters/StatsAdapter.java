package master.pwr.whereami.adapters;

import android.content.Context;
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
    private Field[] fields;
    private String[] temp;


    public StatsAdapter(Context context)
    {
        layoutInflater = LayoutInflater.from(context);
        data = new ArrayList<>();

        fields = Stats.class.getDeclaredFields();
        fieldsNumber = fields.length;
        temp = new String[fieldsNumber];
    }

    @Override
    public int getItemViewType(int position)
    {
        return data != null && data.size() > position && data.get(position).equals(HEADER) ? 0 : 1;
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
        return data == null ? null : data.get(position);
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
        if (convertView == null)
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
            vh.tv.setText("Pomiar " + ((position / fieldsNumber) + 1));
        }
        else
        {
            vh.tv.setText(data.get(position));
        }

        return convertView;
    }

    public void add(Stats stats)
    {
        add(stats, true);
    }

    private void add(Stats stats, boolean shouldNotify)
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
                    temp[props.Order()] = String.format(props.StringFormat(), f.get(stats));
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

        if (shouldNotify) notifyDataSetChanged();
    }

    public void addRange(List<Stats> statsList)
    {
        for (Stats stats : statsList)
        {
            add(stats, false);
        }

        notifyDataSetChanged();
    }

    public void clear()
    {
        if (data != null)
        {
            data.clear();
            notifyDataSetChanged();
        }
    }


    private static class ViewHolder
    {
        TextView tv;
    }
}
