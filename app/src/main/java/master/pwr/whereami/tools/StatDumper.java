package master.pwr.whereami.tools;

import android.content.Context;
import android.os.Environment;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;

import master.pwr.whereami.models.Stats;

/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-18.
 */
public class StatDumper
{
    private static  StatDumper instance;
    private Gson gson;
    private Context context;

    private StatDumper(Context context)
    {
        gson = new Gson();
        this.context = context;
    }

    public static void createInstance(Context context)
    {
        instance = new StatDumper(context);
    }

    public static StatDumper getInstance()
    {
        return instance;
    }

    public void dumpLog(Stats stats, String prefix)
    {
        String filename = String.format("Log-%s-%s-%d.txt",
                stats.getMethodName(),
                prefix,
                System.currentTimeMillis());

        File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File (root.getAbsolutePath() + "/Where Am I/Logs");
        dir.mkdirs();
        File file = new File(dir, filename);

        try(FileOutputStream outputStream = new FileOutputStream(file))
        {
            outputStream.write(gson.toJson(stats).getBytes());
            outputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}
