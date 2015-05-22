package master.pwr.whereami.models;

/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-20.
 */
public class MslSearchRecord
{
    /**
     * The client must check the WiFi SSID for a _nomap suffix. WiFi networks with such a suffix must not be submitted to the server. WiFi networks with a hidden SSID should not be submitted to the server either.
     * The key is the BSSID of the WiFi network. So for example a valid key would look similar to 01:23:45:67:89:ab.
     */
    private String key;
    /**
     * The channel is a number specified by the IEEE which represents a small band of frequencies.
     */
    private int channel;
    /**
     * The frequency in MHz of the channel over which the client is communicating with the access point.
     */
    private int frequency;
    /**
     * The received signal strength (RSSI) in dBm, typically in the range of -51 to -113.
     */
    private int signal;
    /**
     * The current signal to noise ratio measured in dB.
     */
    private int signalToNoiseRatio;

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public int getChannel()
    {
        return channel;
    }

    public void setChannel(int channel)
    {
        this.channel = channel;
    }

    public int getFrequency()
    {
        return frequency;
    }

    public void setFrequency(int frequency)
    {
        this.frequency = frequency;
    }

    public int getSignal()
    {
        return signal;
    }

    public void setSignal(int signal)
    {
        this.signal = signal;
    }

    public int getSignalToNoiseRatio()
    {
        return signalToNoiseRatio;
    }

    public void setSignalToNoiseRatio(int signalToNoiseRatio)
    {
        this.signalToNoiseRatio = signalToNoiseRatio;
    }
}
