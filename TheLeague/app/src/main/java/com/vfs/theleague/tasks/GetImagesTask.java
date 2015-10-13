package com.vfs.theleague.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.vfs.theleague.Cache;

import java.io.InputStream;

// This class takes the url of an image and updates de UI
public class GetImagesTask extends AsyncTask<String, Void, Bitmap>
{
    private ImageView imageView;
    private String imageId;

    public GetImagesTask(ImageView bmImage, String imageId)
    {
        this.imageView = bmImage;   // Reference to UIElement
        this.imageId = imageId;     // ID of image to retrieve
    }

    // Opens url connection to api and retrieves and decodes information into bitmap
    protected Bitmap doInBackground(String... urls)
    {
        String url = urls[0];
        Bitmap imageIcon = null;

        try
        {
            InputStream in = new java.net.URL(url).openStream();
            imageIcon = BitmapFactory.decodeStream(in);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        // Save bitmap in cache
        Cache.getInstance().getLruCache().put(imageId, imageIcon);

        return imageIcon;
    }

    // After retrieving image, update the ui element
    protected void onPostExecute(Bitmap result)
    {
        imageView.setImageBitmap(result);
    }
}