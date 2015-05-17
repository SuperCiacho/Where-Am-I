package master.pwr.whereami.tools;

import android.content.ContextWrapper;
import android.test.mock.MockContext;

import junit.framework.Assert;
import junit.framework.TestCase;

import master.pwr.whereami.enums.LocationStrategyType;
import master.pwr.whereami.interfaces.LocationStrategy;
import master.pwr.whereami.models.locators.BaseLocator;
import master.pwr.whereami.models.locators.GPSLocator;

/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-16.
 */
public class LocationStrategyFactoryTest extends TestCase
{
    public void testGetLocationStrategy() throws Exception
    {
        BaseLocator ls = LocatorFactory.getLocationStrategy(new MockContext(), LocationStrategyType.GPS);

        Assert.assertNotNull(ls);
        Assert.assertTrue(ls instanceof GPSLocator);
    }

    public void testResetAll() throws Exception
    {

    }

    public void testReset() throws Exception
    {
        LocationStrategyType type = LocationStrategyType.GPS;

        Object before = LocatorFactory.getLocationStrategy(new MockContext(), type);
        LocatorFactory.reset(type);
        Object after = LocatorFactory.getLocationStrategy(new MockContext(), type);

        Assert.assertNotSame(before, after);
    }
}