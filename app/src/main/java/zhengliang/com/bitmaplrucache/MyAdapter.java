package zhengliang.com.bitmaplrucache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.List;

import libcore.io.DiskLruCacheUtils;

/**
 * Created by zhengliang on 2016/8/4 0004.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<String> list;
    private Context context;
    private DiskLruCacheUtils diskLruCacheUtils;

    public MyAdapter(List<String> list, Context context, DiskLruCacheUtils diskLruCacheUtils) {
        this.list = list;
        this.context = context;
        this.diskLruCacheUtils = diskLruCacheUtils;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.pic.setTag(list.get(position));
        loadBitmap(list.get(position),holder.pic);
//        System.out.println(position);
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView pic;

        public ViewHolder(View itemView) {
            super(itemView);
            pic = (ImageView) itemView.findViewById(R.id.pic);

        }
    }

    private void loadBitmap(String url, final ImageView imageView) {
        if (imageView.getTag().equals(url)) {
            //从内存缓存中取图片
            Bitmap bitmap = diskLruCacheUtils.getBitmapFromMenCache(url);
            if (bitmap == null) {
                //如果内存中为空 从磁盘缓存中取
                InputStream in = diskLruCacheUtils.getDiskCache(url);

                if (in == null) {
                    //如果缓存中都为空,就通过网络加载,并加入缓存
                    diskLruCacheUtils.putCache(url, new DiskLruCacheUtils.CallBack<Bitmap>() {
                        @Override
                        public void response(Bitmap entity) {
                            System.out.println("网络中下载...");
                            imageView.setImageBitmap(entity);
                        }
                    });
                } else {
                    System.out.println("磁盘中取出...");
                    bitmap = BitmapFactory.decodeStream(in);
                    diskLruCacheUtils.addBitmapToCache(url, bitmap);
                    imageView.setImageBitmap(bitmap);
                }
            } else {
                System.out.println("内存中取出...");
                imageView.setImageBitmap(bitmap);
            }
        }
    }

}
