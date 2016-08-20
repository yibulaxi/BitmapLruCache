package zhengliang.com.bitmaplrucache;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by zhengliang on 2016/8/4 0004.
 * 图片缓存类
 */
public class MemoryLruCacheBitmap {

    private static MemoryLruCacheBitmap memoryLruCacheBitmap;

    public MemoryLruCacheBitmap() {
    }

    public static MemoryLruCacheBitmap getMemoryLruCacheBitmap(){
        if (memoryLruCacheBitmap==null){
            memoryLruCacheBitmap = new MemoryLruCacheBitmap();
        }
        return memoryLruCacheBitmap;
    }

    private LruCache<String,Bitmap> lruCache;

    public  void lruCacheBitmap(Context context){

        // 获取到可用内存的最大值，使用内存超出这个值会引起OutOfMemory异常。
        // LruCache通过构造函数传入缓存值，以KB为单位。
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // 使用最大可用内存值的1/8作为缓存的大小。
        int cacheSize = maxMemory / 8;
        lruCache = new LruCache<>(cacheSize);
    }

    //添加缓存的对象
    public  void addBitmapToCache(String key,Bitmap bitmap){
        if (getBitmapFromMenCache(key)==null){
            lruCache.put(key,bitmap);
        }
    }

    //从缓存中获取对象
    public  Bitmap getBitmapFromMenCache(String key){
        return  lruCache.get(key);
    }
}
