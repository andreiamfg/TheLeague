package com.vfs.theleague.tasks;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Base64;
import com.vfs.theleague.MainActivity;
import com.vfs.theleague.TheLeagueWidget;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class GetWidgetImageTask extends AsyncTask<String, Void, Bitmap>
{
    public GetWidgetImageTask()
    {

    }

    // Do process in background open a stream and decode it as a bitmap

    protected Bitmap doInBackground(String... urls)
    {
        Bitmap bm = null;
        String url = urls[0];
        try
        {
            InputStream in = new java.net.URL(url).openStream();
            bm = BitmapFactory.decodeStream(in);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    // Return the bitmap when the background process is done
        return bm;
    }

    protected void onPostExecute(Bitmap result)
    {

        // Encode a bitmap and save it as a string in the shared preferences
        // This way the widget will be able to access it easily
        Bitmap immagex=result;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        SharedPreferences sharedDataBase = PreferenceManager.getDefaultSharedPreferences(MainActivity.getActivity().getApplicationContext());
        SharedPreferences.Editor editor = sharedDataBase.edit();

        editor.putString("bitmapEncoded", imageEncoded);

        editor.apply();

        //Update the widget when done requesting the image
        Intent intent = new Intent(MainActivity.getActivity(),TheLeagueWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        int[] ids = AppWidgetManager.getInstance(MainActivity.getActivity()).getAppWidgetIds(new ComponentName(MainActivity.getActivity().getApplication(), TheLeagueWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);

        MainActivity.getActivity().sendBroadcast(intent);
    }
}
