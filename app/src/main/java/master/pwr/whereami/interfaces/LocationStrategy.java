package master.pwr.whereami.interfaces;

/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-16.
 */
public interface LocationStrategy
{
    String getName();

    void dumpStats();
    void localize();
    Object retrieveStats();

    void stop();
    void reset();
    void restart();
}
