package com.vfs.theleague;

import android.support.v4.util.LruCache;

// This class provides access to the device caching memory
public class Cache
{
    private static Cache instance;          // Instance of this applications cache
    private LruCache<Object, Object> lruCache;   // Make it so that any object can be stored in cache

    // Setup the cache settings
    private Cache()
    {
        // Set up the cache maximum memory
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        lruCache = new LruCache<Object, Object>(cacheSize);
    }

    // Singleton access for this applications cache
    public static Cache getInstance()
    {
        // If there is no instance,
        if (instance == null)
        {
            // Create one
            instance = new Cache();
        }
        // Return it
        return instance;
    }

    // Function used to get object stored in cache <key, value>
    public LruCache<Object, Object> getLruCache()
    {
        return lruCache;
    }
}