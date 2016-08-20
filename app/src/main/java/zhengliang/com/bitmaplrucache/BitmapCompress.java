package zhengliang.com.bitmaplrucache;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by zhengliang on 2016/8/4 0004.
 */
public class BitmapCompress {

    /**
     * 位图重新采样
     * @param res 资源
     * @param resId 图片id
     * @param view 传入view
     * @return
     */

    Bitmap bitmap;
    public static Bitmap decodeSampleadBitmapFromResource(Resources res, int resId , View view){


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//只解析边界,不加载到内存中
        BitmapFactory.decodeResource(res,resId,options);
        options.inSampleSize = calculatInSampleSize(options,view);//设置采样比为计算出的采样比例
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res,resId);//重新解析图片
    }

    /**
     * 计算位图的采样比例大小
     * @param options
     * @param view 控件
     * @return
     */
    private static int calculatInSampleSize(BitmapFactory.Options options,View view){
        //获取位图的原宽高
        final int w = options.outWidth;
        final int h = options.outHeight;

        final int reqWidth = view.getWidth();
        final int reqHeight = view.getHeight();

        int inSampleSize = 1;
        //如果原图的宽高比需要的图片宽高大
        if (w>reqWidth||h>reqHeight){
            if (w>h){
                inSampleSize = Math.round((float)h/(float)reqHeight);
            }else {
                inSampleSize = Math.round((float)w/(float)reqWidth);
            }
        }
        return  inSampleSize;
    }

//===================================================================================================================//


    /**
     * 位图重新采样
     * @param res 资源
     * @param resId 图片id
     * @param reqWidth 自定义的宽高
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampleadBitmapFromResource(Resources res, int resId , int reqWidth, int reqHeight){

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//只解析边界,不加载到内存中
        BitmapFactory.decodeResource(res,resId,options);
        options.inSampleSize = calculatInSampleSize(options,reqWidth,reqHeight);//设置采样比为计算出的采样比例
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res,resId);//重新解析图片
    }

    /**
     * 计算位图的采样比例大小
     * @param options
     * @param reqWidth 需要的宽高
     * @param reqHeight
     * @return
     */
    private static int calculatInSampleSize(BitmapFactory.Options options,int reqWidth,int reqHeight){
        //获取位图的原宽高
        final int w = options.outWidth;
        final int h = options.outHeight;
        int inSampleSize = 1;
        //如果原图的宽高比需要的图片宽高大
        if (w>reqWidth||h>reqHeight){
            if (w>h){
                inSampleSize = Math.round((float)h/(float)reqHeight);
            }else {
                inSampleSize = Math.round((float)w/(float)reqWidth);
            }
        }
        return  inSampleSize;
    }

}
