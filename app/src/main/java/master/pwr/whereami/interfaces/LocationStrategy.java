package master.pwr.whereami.interfaces;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-16.
 */
public interface LocationStrategy
{
    String getName();

    void dumpStats(boolean beforeLocation);

    void prepare();
    void localize(Fragment fragment);
    void showMap(FragmentManager fm);

    void stop();
    void reset();
    void restart();

    void update(Intent data);

    void measureTime(boolean start);
    long getExecutionTime();

    void setStatsUpdater(StatsUpdater callback);
}
