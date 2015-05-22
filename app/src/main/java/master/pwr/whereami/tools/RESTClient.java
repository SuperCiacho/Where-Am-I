package master.pwr.whereami.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import master.pwr.whereami.models.MslResult;
import master.pwr.whereami.models.MslSearchRecord;
import master.pwr.whereami.models.net.NetResult;

public class RESTClient
{
    private Gson gson;
    private String serverAddress;
    private NetworkInfo activeNetworkInfo;

    public RESTClient(Context context, String serverAddress)
    {
        this.serverAddress = serverAddress;

        gson = new Gson();

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    }

    /**
     * Method checks if user is connected to the network.
     * NOTE:
     * To use this method you have invoke initialize method before at least once!
     *
     * @return whether network is available
     */
    public boolean isNetworkAvailable()
    {
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private synchronized HttpsURLConnection CreateHttpsConnection(String endpoint) throws IOException
    {
        HttpsURLConnection client = (HttpsURLConnection) new URL(serverAddress + endpoint).openConnection();
        client.setUseCaches(false);
        client.setDoInput(true);
        client.setDoOutput(true);

        return client;
    }

    public NetResult<MslResult> checkLocation(MslSearchRecord msr)
    {
        NetResult<MslResult> result = new NetResult<>();
        HttpURLConnection client = null;

        try
        {
            client = CreateHttpsConnection("");
            String postData = gson.toJson(msr);

            OutputStream os = client.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(postData);
            writer.flush();
            writer.close();
            os.close();

            client.connect();

            result.setStatusCode(client.getResponseCode());
            if (result.isSuccessStatusCode())
            {
                result.setData(getObjectFromJson(client.getInputStream(), MslResult.class));
                result.setSuccess(true);
            }
        }
        catch (MalformedURLException | ProtocolException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            System.out.print("IO Exception occurs.");
            e.printStackTrace();
        }
        finally
        {
            if (client != null) client.disconnect();
        }

        return result;
    }

    private String getJsonFromResponse(InputStream content) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(content, "UTF-8"));
        StringBuilder builder = new StringBuilder();
        for (String line; (line = reader.readLine()) != null; ) builder.append(line).append("\n");
        reader.close();
        return builder.toString();
    }

    private <T> T getObjectFromJson(InputStream response, Class<T> clazz) throws IOException
    {
        String json = getJsonFromResponse(response);
        response.close();
        return gson.fromJson(json, clazz);
    }
}
