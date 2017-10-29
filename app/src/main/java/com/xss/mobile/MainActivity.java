package com.xss.mobile;

import android.app.ActivityManager;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xss.mobile.activity.BitmapTestActivity;
import com.xss.mobile.activity.BookListActivity;
import com.xss.mobile.activity.ComposeViewTestActivity;
import com.xss.mobile.activity.IntentServiceTestActivity;
import com.xss.mobile.activity.LooperTestActivity;
import com.xss.mobile.activity.ScrollViewTestActivity;
import com.xss.mobile.activity.ViewStubTestActivity;
import com.xss.mobile.activity.WebViewActivity;
import com.xss.mobile.activity.XmlParserActivity;
import com.xss.mobile.activity.ZxingCodeActivity;
import com.xss.mobile.activity.annotation.AnnotationTestActivity;
import com.xss.mobile.activity.annotation.IntDefTestActivity;
import com.xss.mobile.activity.bitmap.GridViewBitmapTestActivity;
import com.xss.mobile.activity.bitmap.ViewPagerBitmapTestActivity;
import com.xss.mobile.activity.broadcast.BroadcastTestActivity;
import com.xss.mobile.activity.countdown.TimeCountDownActivity;
import com.xss.mobile.activity.customview.ConstraintLayoutLearnActivity;
import com.xss.mobile.activity.customview.FrameLayoutTestActivity;
import com.xss.mobile.activity.customview.LeanTextViewActivity;
import com.xss.mobile.activity.customview.MultiLabelActivity;
import com.xss.mobile.activity.dagger2.DaggerTestScopeActivity;
import com.xss.mobile.activity.databinding.DatabindingTestActivity;
import com.xss.mobile.activity.databinding.dbinding.CustomBindingActivity;
import com.xss.mobile.activity.eventbus.EventbusTestActivity;
import com.xss.mobile.activity.launchmode.FirstActivity;
import com.xss.mobile.activity.memorytest.ListViewTestActivity;
import com.xss.mobile.activity.memorytest.MemoryTabTestActivity;
import com.xss.mobile.activity.network.AutoResumeDownloadActivity;
import com.xss.mobile.activity.network.NetWorkTestActivity;
import com.xss.mobile.activity.okhttp.InterceptorTestActivity;
import com.xss.mobile.activity.okhttp.OkHttpTestActivity;
import com.xss.mobile.activity.opengl.OpenGLES20Activity;
import com.xss.mobile.activity.photo.GetPictureActivity;
import com.xss.mobile.activity.retrofit.RetrofitTestActivity;
import com.xss.mobile.activity.rxjava.RxJavaTestActivity;
import com.xss.mobile.activity.scrollconflict.ScrollViewAndRecyclerViewActivity;
import com.xss.mobile.activity.view.ViewEventDispatchActivity;
import com.xss.mobile.activity.volley.VolleyPackagingActivity;
import com.xss.mobile.activity.volley.VolleyTestActivity;
import com.xss.mobile.adapter.BaseRecyclerAdapter;
import com.xss.mobile.adapter.BaseViewHolder;
import com.xss.mobile.handler.CrashHandler;
import com.xss.mobile.utils.DensityUtil;
import com.xss.mobile.utils.ViewUtil;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.DexClassLoader;

//import com.xss.mobile.activity.jnitest.JniTestActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;

    private List<ViewModel> items;

    private TextView tv_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_test = (TextView) findViewById(R.id.tv_test);

//        printAppHeapMemorySize();

        CrashHandler.getInstance().init(MainActivity.this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        itemAdapter = new ItemAdapter(this);
        items = getData();
        itemAdapter.setItems(items);
        LinearLayoutManager lm = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                // 解决 ScrollView 嵌套滑动冲突问题
                return false;
            }
        };
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(lm);
            recyclerView.setAdapter(itemAdapter);
        }

//        checkFile();


        initItemClick();

        initClick();
    }

    private void test() {

//        String json = "{\"name\":\"浑江区\",\"pid\":10204,\"cityId\":10205,\"namePinyin\":\"hunjiang\"}";

        City1 city1 = new City1();
        city1.city = "南山区";
        city1.pid = 1001;
        city1.namePinyin = "nanshanqu";

        String json = new Gson().toJson(city1);
        Log.e(TAG, "city1 = " + json);

        City2 city2 = new Gson().fromJson(json, City2.class);


    }

    public class City1 implements Serializable {

        public String city;
        public int pid;
        public String namePinyin;
    }

    public class City2 implements Serializable {

        public String city;
        public int pid;
        public String namePinyin;
    }

    private void checkFile() {
        File file = buildPath(Environment.getExternalStorageDirectory(), "");
        String path = "";
        try {
            path = file.getCanonicalPath();

            tv_test.append("\n" + path);
            Log.e(TAG, path);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static File buildPath(File base, String... segments) {
        File cur = base;
        for (String segment : segments) {
            if (segment != null) {
                cur = new File(cur, segment);
            }
        }
        return cur;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    private void printAppHeapMemorySize() {
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        int heapSize = am.getMemoryClass();  // 单位MB
        Log.e(TAG, "heap size = " + heapSize + " fileSize = " + getFilesDir().getAbsolutePath()
                + ", cacheSize = " + getCacheDir().getAbsolutePath());

        int screenWidth = ViewUtil.getScreenWidth(this);
        int screenHeight = ViewUtil.getScreenHeight(this);

        String str = Build.BOARD + ", " + Build.BOOTLOADER + ", " + Build.BRAND + ", " + Build.DEVICE + ", \n" +
                Build.DISPLAY + ", " + Build.HARDWARE + ", " + Build.HOST + ", " + Build.MANUFACTURER + ", \n" +
                Build.MODEL + ", " + Build.PRODUCT + ", " + Build.SERIAL + ", " + Build.TYPE;
        tv_test.setText(str);

        String apkUrl = "http://fsxf.fangdd.com/xf/loJjNBcAOcAsrIW3Ly5c1UEpcV8s.apk";
        Uri apkUri = Uri.parse(apkUrl);
        DownloadManager.Request request = new DownloadManager.Request(apkUri);
        request.setMimeType("application/vnd.android.package-archive");
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "agent-v8.3.0.apk");

//        ContentValues values = request.toContentValues("");
//        Uri downloadUri = mResolver.insert(Downloads.Impl.CONTENT_URI, values);
        long id = Long.parseLong(apkUri.getLastPathSegment());


        Log.e(TAG, str + ", \n" + "download id = " + id);
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

    private void testUri() {
        String apkName = "/download/agent-v" + ".apk";

        final String fileUrl = Environment.getExternalStorageDirectory() + apkName;
        Uri uri = Uri.fromFile(new File(fileUrl));

        File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        Uri destUri = Uri.withAppendedPath(Uri.fromFile(downloadDir), apkName);

        Log.e(TAG, uri.toString() + "\n" + destUri.toString());
    }

    private void initItemClick() {
        itemAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object object) {

                if (position == 3 && items.get(position).clazz == null) {
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
                } else {
                    Intent intent =  new Intent(MainActivity.this, items.get(position).clazz);
                    startActivity(intent);
                }
            }
        });
    }

    private List<ViewModel> getData() {
        List<ViewModel> list = new ArrayList<>();
        list.add(new ViewModel("To View Animation", Main2Activity.class)); //ViewAnimationActivity
        list.add(new ViewModel("To Network Test", NetWorkTestActivity.class));
        list.add(new ViewModel("To Volley Test", VolleyTestActivity.class));
        list.add(new ViewModel("To IntentService Test", IntentServiceTestActivity.class));
        list.add(new ViewModel("To ViewStub Test", ViewStubTestActivity.class));
        list.add(new ViewModel("To Volley Packaging Test", VolleyPackagingActivity.class));
        list.add(new ViewModel("To OkHttp3 Test", OkHttpTestActivity.class));
        list.add(new ViewModel("To Lopper Test", LooperTestActivity.class));
//        list.add(new ViewModel("To Jni Test", JniTestActivity.class));
        list.add(new ViewModel("To First Activity", FirstActivity.class));
        list.add(new ViewModel("To WebView Activity", WebViewActivity.class));
        list.add(new ViewModel("To LeanTextView Activity", LeanTextViewActivity.class));
        list.add(new ViewModel("To Zxing QrCode Activity", ZxingCodeActivity.class));
        list.add(new ViewModel("To Test Annotation Activity", AnnotationTestActivity.class));
        list.add(new ViewModel("To Test Scroll contain fragment Activity", ScrollViewTestActivity.class));
        list.add(new ViewModel("To Test FrameLayout Activity", FrameLayoutTestActivity.class));
        list.add(new ViewModel("To Test EventBus Activity", EventbusTestActivity.class));
        list.add(new ViewModel("To Test AutoResumeDownloadActivity", AutoResumeDownloadActivity.class));
//        list.add(new ViewModel("To Test FileLoadActivity", FileLoadActivity.class));
        list.add(new ViewModel("To Test ViewEventDispatchActivity", ViewEventDispatchActivity.class));
        list.add(new ViewModel("To Test BroadcastTestActivity", BroadcastTestActivity.class));
        list.add(new ViewModel("To Test MultiLabelActivity", MultiLabelActivity.class));
        list.add(new ViewModel("To Test ConstraintLayoutLearnActivity", ConstraintLayoutLearnActivity.class));
        list.add(new ViewModel("To Test ScrollConflictActivity", ScrollViewAndRecyclerViewActivity.class));
        list.add(new ViewModel("To Test MemoryTabTestActivity", MemoryTabTestActivity.class));
        list.add(new ViewModel("To Test ListViewTestActivity", ListViewTestActivity.class));
        list.add(new ViewModel("To Test TimeCountDownActivity", TimeCountDownActivity.class));
        list.add(new ViewModel("To Test RetrofitTestActivity", RetrofitTestActivity.class));
        list.add(new ViewModel("To Test InterceptorTestActivity", InterceptorTestActivity.class));
        list.add(new ViewModel("To Test RxJavaTestActivity", RxJavaTestActivity.class));
        list.add(new ViewModel("To Test DaggerTestActivity", com.xss.mobile.activity.dagger2.DaggerTestActivity.class));
        list.add(new ViewModel("TO DaggerTestScopeActivity", DaggerTestScopeActivity.class));
        list.add(new ViewModel("To Test DataBindingTestActivity", DatabindingTestActivity.class));
        list.add(new ViewModel("To Test CustomBindingActivity", CustomBindingActivity.class));
        list.add(new ViewModel("To Test DataBindingTestActivity", IntDefTestActivity.class));

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

    Handler handler1 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Log.e("h1", msg.obj.toString());
        }
    };

    Handler handler2 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Log.e("h2", msg.obj.toString());
        }
    };

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent;
        switch (id) {
            case R.id.btn_test:
//                intent = new Intent(MainActivity.this, HelloIntentService.class);  // HelloService
//                startService(intent);

                Message msg = Message.obtain();
                msg.obj = "handler 1";
                handler1.sendMessage(msg);

                break;
            case R.id.btn_to_bind_service:
//                intent = new Intent(MainActivity.this, WebViewActivity.class);
//                startActivity(intent);

//                useDexClassLoader();

                Message msg1 = Message.obtain();
                msg1.obj = "handler 2";
                handler2.sendMessage(msg1);

                break;
            case R.id.btn_to_parser_xml:
                intent = new Intent(MainActivity.this, XmlParserActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_to_get_picture:
                intent = new Intent(MainActivity.this, GetPictureActivity.class);  // ChoosePhotoActivity
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

    public class ItemAdapter extends BaseRecyclerAdapter<ViewModel> {

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
                btn.setText(itemList.get(i).label);
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

    class ViewModel {
        public String label;
        public Class clazz;

        public ViewModel(String label, Class clazz) {
            this.label = label;
            this.clazz = clazz;
        }
    }
}
