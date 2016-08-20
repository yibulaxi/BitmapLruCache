package zhengliang.com.bitmaplrucache;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import libcore.io.DiskLruCacheUtils;

public class MainActivity extends AppCompatActivity{


    private List<String> data;
    private DiskLruCacheUtils diskLruCacheUtils;//创建对象
    private static final String DISK_CACHE_SUBDIR = "temp"; //设置图片缓存文件名
    private static final int DISK_CACHE_SIZE= 100*1024*1024; //设置SD卡缓存大小
    private RecyclerView rlvlist;

    private MyAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        getImageUrl("http://image.baidu.com/channel/listjson?pn=0&rn=200&tag1=美女&tag2=小清新&ie=utf8");
//        getImageUrl2("http://image.baidu.com/search/avatarjson?tn=resultjsonavatarnew&ie=utf-8&word=美女图片&cg=girl&pn=60&rn=60&itg=0&z=0&fr=&width=&height=&lm=-1&ic=0&s=0&st=-1&gsm=1e000000001e");
//        getImageUrl2("http://image.baidu.com/search/avatarjson?tn=resultjsonavatarnew&ie=utf-8&word=美女图片&cg=girl&pn=120&rn=60&itg=0&z=0&fr=&width=&height=&lm=-1&ic=0&s=0&st=-1&gsm=1e000000001e");
//        getImageUrl2("http://image.baidu.com/search/avatarjson?tn=resultjsonavatarnew&ie=utf-8&word=美女图片&cg=girl&pn=180&rn=60&itg=0&z=0&fr=&width=&height=&lm=-1&ic=0&s=0&st=-1&gsm=1e000000001e");
//        getImageUrl2("http://image.baidu.com/search/avatarjson?tn=resultjsonavatarnew&ie=utf-8&word=美女图片&cg=girl&pn=240&rn=60&itg=0&z=0&fr=&width=&height=&lm=-1&ic=0&s=0&st=-1&gsm=1e000000001e");
//        getImageUrl2("http://image.baidu.com/search/avatarjson?tn=resultjsonavatarnew&ie=utf-8&word=美女图片&cg=girl&pn=300&rn=60&itg=0&z=0&fr=&width=&height=&lm=-1&ic=0&s=0&st=-1&gsm=1e000000001e");
//        getImageUrl2("http://image.baidu.com/search/avatarjson?tn=resultjsonavatarnew&ie=utf-8&word=美女图片&cg=girl&pn=360&rn=60&itg=0&z=0&fr=&width=&height=&lm=-1&ic=0&s=0&st=-1&gsm=1e000000001e");
//        getImageUrl2("http://image.baidu.com/search/avatarjson?tn=resultjsonavatarnew&ie=utf-8&word=美女图片&cg=girl&pn=420&rn=60&itg=0&z=0&fr=&width=&height=&lm=-1&ic=0&s=0&st=-1&gsm=1e000000001e");
//        getImageUrl2("http://image.baidu.com/search/avatarjson?tn=resultjsonavatarnew&ie=utf-8&word=美女图片&cg=girl&pn=480&rn=60&itg=0&z=0&fr=&width=&height=&lm=-1&ic=0&s=0&st=-1&gsm=1e000000001e");
//        getImageUrl2("http://image.baidu.com/search/avatarjson?tn=resultjsonavatarnew&ie=utf-8&word=美女图片&cg=girl&pn=540&rn=60&itg=0&z=0&fr=&width=&height=&lm=-1&ic=0&s=0&st=-1&gsm=1e000000001e");
//        getImageUrl2("http://image.baidu.com/search/avatarjson?tn=resultjsonavatarnew&ie=utf-8&word=美女图片&cg=girl&pn=600&rn=60&itg=0&z=0&fr=&width=&height=&lm=-1&ic=0&s=0&st=-1&gsm=1e000000001e");




    }


    private void initViews() {
        data = new ArrayList<String>();
        this.rlvlist = (RecyclerView) findViewById(R.id.rlv_list);
        rlvlist.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
    }

    @Override
    protected void onResume() {
        super.onResume();
        diskLruCacheUtils = DiskLruCacheUtils.getInstance();
        diskLruCacheUtils.open(this,DISK_CACHE_SUBDIR,DISK_CACHE_SIZE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        diskLruCacheUtils.flush();
    }

    @Override
    protected void onStop() {
        super.onStop();
        diskLruCacheUtils.close();
    }

    public void getImageUrl(String url){

        final RequestQueue mQueue = Volley.newRequestQueue(this);

        JsonObjectRequest stringRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
//                        System.out.println(jsonObject);
                        try {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i <jsonArray.length() ; i++) {
                                JSONObject item = jsonArray.getJSONObject(i);
                                String url = item.getString("image_url");
                                String name = item.getString("tags");
                                data.add(url);

                                myAdapter = new MyAdapter(data,MainActivity.this,diskLruCacheUtils);
                                rlvlist.setAdapter(myAdapter);
                                myAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }
        );

        mQueue.add(stringRequest);

        if (data.size()==200){
            getImageUrl("http://image.baidu.com/channel/listjson?pn=0&rn=200&tag1=美女&tag2=全部&ie=utf8");
        }

    }
    public void getImageUrl2(String url){

        final RequestQueue mQueue = Volley.newRequestQueue(this);

        JsonObjectRequest stringRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
//                        System.out.println(jsonObject);
                        try {
                            JSONArray jsonArray = jsonObject.getJSONArray("imgs");
                            for (int i = 0; i <jsonArray.length() ; i++) {
                                JSONObject item = jsonArray.getJSONObject(i);
                                String url = item.getString("hoverURL");
                                String name = item.getString("fromPageTitle");
                                data.add(url);

                                myAdapter = new MyAdapter(data,MainActivity.this,diskLruCacheUtils);
                                rlvlist.setAdapter(myAdapter);
                                myAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }
        );

        mQueue.add(stringRequest);


    }



}
