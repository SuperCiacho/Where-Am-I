package master.pwr.whereami.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import master.pwr.whereami.models.Stats;

/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-18.
 */
public class StatDumper
{
    private static StatDumper instance;
    private final Gson gson;
    private final SimpleDateFormat sdf;

    private StatDumper()
    {
        gson = new GsonBuilder().setPrettyPrinting().create();
        sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault());
    }

    public static StatDumper getInstance()
    {
        if (instance == null)
        {
            instance = new StatDumper();
        }
        return instance;
    }

    public void dumpLog(List<Stats> stats, String methodName, String tag)
    {
        File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/Where Am I/Logs");
        dir.mkdirs();

        String filename = String.format("Log [%s][%s]%s.txt", methodName, sdf.format(new Date()), tag);

        File file = new File(dir, filename);

        try (FileOutputStream outputStream = new FileOutputStream(file))
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