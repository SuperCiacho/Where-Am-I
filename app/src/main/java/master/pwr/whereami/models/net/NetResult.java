package master.pwr.whereami.models.net;

import java.net.HttpURLConnection;

/**
 * Created by Bartosz on 2015-03-28.
 */
public class NetResult<T>
{
    private boolean success;
    private int statusCode;
    private T data;

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    public int getStatusCode()
    {
        return statusCode;
    }

    public void setStatusCode(int statusCode)
    {
        this.statusCode = statusCode;
    }

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }

    public boolean isSuccessStatusCode()
    {
        return statusCode >= HttpURLConnection.HTTP_OK && statusCode <= 299;
    }
}
