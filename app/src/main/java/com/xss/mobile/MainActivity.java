package com.xss.mobile;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.xss.mobile.activity.BitmapTestActivity;
import com.xss.mobile.activity.BookListActivity;
import com.xss.mobile.activity.ComposeViewTestActivity;
import com.xss.mobile.activity.GetPictureActivity;
import com.xss.mobile.activity.IntentServiceTestActivity;
import com.xss.mobile.activity.LooperTestActivity;
import com.xss.mobile.activity.ViewStubTestActivity;
import com.xss.mobile.activity.jnitest.JniTestActivity;
import com.xss.mobile.activity.launchmode.FirstActivity;
import com.xss.mobile.activity.network.NetWorkTestActivity;
import com.xss.mobile.activity.animation.ViewAnimationActivity;
import com.xss.mobile.activity.bitmap.GridViewBitmapTestActivity;
import com.xss.mobile.activity.bitmap.ViewPagerBitmapTestActivity;
import com.xss.mobile.activity.WebViewActivity;
import com.xss.mobile.activity.XmlParserActivity;
import com.xss.mobile.activity.okhttp.OkHttpTestActivity;
import com.xss.mobile.activity.opengl.OpenGLES20Activity;
import com.xss.mobile.activity.volley.VolleyPackagingActivity;
import com.xss.mobile.activity.volley.VolleyTestActivity;
import com.xss.mobile.adapter.BaseRecyclerAdapter;
import com.xss.mobile.adapter.BaseViewHolder;
import com.xss.mobile.service.HelloIntentService;
import com.xss.mobile.utils.DensityUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        itemAdapter = new ItemAdapter(this);
        itemAdapter.setItems(getData());
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(itemAdapter);


        initItemClick();

        initClick();
    }

    private void useDexClassLoader() {
        Intent intent = new Intent("com.fangdd.net.retrofitapplication", null);
        PackageManager pm = getPackageManager();
        List<ResolveInfo> plugins = pm.queryIntentActivities(intent, PackageManager.MATCH_ALL);
        ResolveInfo resolveInfo = plugins.get(0);
        ActivityInfo activityInfo = resolveInfo.activityInfo;

        String div = System.getProperty("path.separator");
        String packageName = activityInfo.packageName;
        String dexPath = activityInfo.applicationInfo.sourceDir;
        String dexOutputDir = getApplicationInfo().dataDir;
        String libPath = activityInfo.applicationInfo.nativeLibraryDir;

        DexClassLoader dexClassLoader = new DexClassLoader(dexPath, dexOutputDir, libPath, this.getClass().getClassLoader());

        try {
            Class clazz = dexClassLoader.loadClass(packageName + ".PluginUtil");
            Object obj = clazz.newInstance();
            Class[] params = new Class[2];
            params[0] = Integer.TYPE;
            params[1] = Integer.TYPE;

            Method action = clazz.getMethod("function1", params);
            Integer ret = (Integer) action.invoke(obj, 12, 34);
            Log.d(TAG, "return value is " + ret);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    private void initItemClick() {
        itemAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object object) {
                Intent intent = null;
                switch (position) {
                    case 0:
                        intent = new Intent(MainActivity.this, ViewAnimationActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        // // TODO: 2016/11/5  未测试周一测试  源码：NetworkUsage.zip
                        intent = new Intent(MainActivity.this, NetWorkTestActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(MainActivity.this, VolleyTestActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
//                        intent = new Intent(MainActivity.this, IntentServiceTestActivity.class);
//                        startActivity(intent);


                        try {
                            Log.e(TAG, "use Java Reflection before: " + System.currentTimeMillis());


                            Class c = Class.forName("com.xss.mobile.activity.IntentServiceTestActivity");
//                            IntentServiceTestActivity act = (IntentServiceTestActivity) c.newInstance();
//                            act.toHere(MainActivity.this);

                            Method m = c.getMethod("toHere", Context.class);
                            m.invoke(null, MainActivity.this);

                            Log.e(TAG, "use Java Reflection after: " + System.currentTimeMillis());


                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
//                        catch (InstantiationException e) {
//                            e.printStackTrace();
//                        }
                        catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        break;

                    case 4:
                        intent = new Intent(MainActivity.this, ViewStubTestActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(MainActivity.this, VolleyPackagingActivity.class);
                        startActivity(intent);
                        break;
                    case 6:
                        intent = new Intent(MainActivity.this, OkHttpTestActivity.class);
                        startActivity(intent);
                        break;
                    case 7:
                        intent = new Intent(MainActivity.this, LooperTestActivity.class);
                        startActivity(intent);
                        break;
                    case 8:
                        intent = new Intent(MainActivity.this, JniTestActivity.class);
                        startActivity(intent);
                        break;
                    case 9:
                        intent = new Intent(MainActivity.this, FirstActivity.class);
                        Log.d(TAG, "0-1 hash = " + intent.hashCode());
                        startActivity(intent);
                        break;
                    case 10:
                        intent = new Intent(MainActivity.this, WebViewActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    private List<String> getData() {
        List<String> list = new ArrayList<>();
        list.add(0, "To View Animation");
        list.add(1, "To Network Test");
        list.add(2, "To Volley Test");
        list.add(3, "To IntentService Test");
        list.add(4, "To ViewStub Test");
        list.add(5, "To Volley Packaging Test");
        list.add(6, "To OkHttp3 Test");
        list.add(7, "To Lopper Test");
        list.add(8, "To Jni Test");
        list.add(9, "To First Activity");
        list.add(10, "To WebView Activity");

        return list;
    }
    
    private void initClick() {
        findViewById(R.id.btn_test).setOnClickListener(this);
        findViewById(R.id.btn_to_bind_service).setOnClickListener(this);
        findViewById(R.id.btn_to_parser_xml).setOnClickListener(this);
        findViewById(R.id.btn_to_get_picture).setOnClickListener(this);
        findViewById(R.id.btn_test_bitmap).setOnClickListener(this);
        findViewById(R.id.btn_book_list).setOnClickListener(this);
        findViewById(R.id.btn_compose_label_edit_view).setOnClickListener(this);
        findViewById(R.id.btn_load_bitmap_at_viewpager).setOnClickListener(this);
        findViewById(R.id.btn_load_bitmap_at_gridview).setOnClickListener(this);
        findViewById(R.id.btn_to_show_glface_view).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent;
        switch (id) {
            case R.id.btn_test:
                intent = new Intent(MainActivity.this, HelloIntentService.class);  // HelloService
                startService(intent);
                break;
            case R.id.btn_to_bind_service:
//                intent = new Intent(MainActivity.this, WebViewActivity.class);
//                startActivity(intent);

                useDexClassLoader();

                break;
            case R.id.btn_to_parser_xml:
                intent = new Intent(MainActivity.this, XmlParserActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_to_get_picture:
                intent = new Intent(MainActivity.this, GetPictureActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_test_bitmap:
                intent = new Intent(MainActivity.this, BitmapTestActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_book_list:
                intent = new Intent(MainActivity.this, BookListActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_compose_label_edit_view:
                intent = new Intent(MainActivity.this, ComposeViewTestActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_load_bitmap_at_viewpager:
                intent = new Intent(MainActivity.this, ViewPagerBitmapTestActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_load_bitmap_at_gridview:
                intent = new Intent(MainActivity.this, GridViewBitmapTestActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_to_show_glface_view:
                intent = new Intent(MainActivity.this, OpenGLES20Activity.class);
                startActivity(intent);
                break;
        }
    }

    public class ItemAdapter extends BaseRecyclerAdapter<String> {

        public ItemAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            Button btn = new Button(context);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context, 42)
            );
            btn.setLayoutParams(lp);

            return new ItemViewHolder(btn);
        }

        @Override
        public void onBindViewHolder(BaseViewHolder baseViewHolder, final int i) {
            ItemViewHolder holder = (ItemViewHolder) baseViewHolder;
            if (holder.itemView instanceof Button) {
                Button btn = (Button)holder.itemView;
                btn.setText(itemList.get(i));
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.onItemClick(i, itemList.get(i));
                        }
                    }
                });
            }
        }

        class ItemViewHolder extends BaseViewHolder {

            public ItemViewHolder(View itemView) {
                super(itemView);

            }
        }

    }
}
