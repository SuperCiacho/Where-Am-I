package master.pwr.whereami;

import android.app.Application;

import master.pwr.whereami.tools.ServiceHelper;

/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-16.
 */
public class WhereAmI extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        ServiceHelper.getInstance().setContext(this);
    }
}
