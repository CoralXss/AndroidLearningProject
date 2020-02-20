package com.xss.mobile;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.core.activity.FileLoadActivity;
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
import com.xss.mobile.activity.alarm.AlarmManagerTestActivity;
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
import com.xss.mobile.activity.databinding.senior.CustomSetterBindingAdapterTestActivity;
import com.xss.mobile.activity.databinding.senior.PermissionCenter;
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
import com.xss.mobile.adapter.BaseRecyclerAdapter;
import com.xss.mobile.adapter.BaseViewHolder;
import com.xss.mobile.fragment.tab.HomeTabActivity;
import com.xss.mobile.handler.CrashHandler;
import com.xss.mobile.hook.HookClickListenerActivity;
import com.xss.mobile.service.HelloIntentService;
import com.xss.mobile.utils.DensityUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dalvik.system.DexClassLoader;

//import com.xss.mobile.activity.jnitest.JniTestActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;

    private List<ViewModel> items;

    private TextView tv_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionCenter.setPermissions(Arrays.asList("p_del", "p_update"));
        CrashHandler.getInstance().init(MainActivity.this);

        tv_test = (TextView) findViewById(R.id.tv_test);
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

        initItemClick();
    }


    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
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

    private void initItemClick() {
        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = Message.obtain();
                msg.obj = "handler 1";
                handler1.sendMessage(msg);

//                Message msg1 = Message.obtain();
//                msg1.obj = "handler 2";
//                handler2.sendMessage(msg1);

//                testReflect();
//                useDexClassLoader();
            }
        });
        findViewById(R.id.btn_to_bind_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Service vs IntentService
                Intent intent = new Intent(MainActivity.this, HelloIntentService.class);  // HelloService
                startService(intent);
            }
        });
        itemAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object object) {
                Class clazz = items.get(position).clazz;
                Intent intent =  new Intent(MainActivity.this, clazz);
                intent.putExtra("MainMsg", "from main");
                startActivity(intent);
            }
        });
    }

    private List<ViewModel> getData() {
        List<ViewModel> list = new ArrayList<>();
        list.add(new ViewModel("启动模式", FirstActivity.class));

        list.add(new ViewModel("控件学习：View 事件传递", ViewEventDispatchActivity.class));
        list.add(new ViewModel("控件学习：OpenGL/SurfaceView", OpenGLES20Activity.class));
        list.add(new ViewModel("控件学习：WebView", WebViewActivity.class));
        list.add(new ViewModel("控件学习：FrameLayout", FrameLayoutTestActivity.class));
        list.add(new ViewModel("控件学习：ViewStub", ViewStubTestActivity.class));
        list.add(new ViewModel("控件学习：ConstraintLayout", ConstraintLayoutLearnActivity.class));
        list.add(new ViewModel("控件学习：ListView 多数据卡顿", ListViewTestActivity.class));
        list.add(new ViewModel("控件学习：组合控件及自定义属性", ComposeViewTestActivity.class));
        list.add(new ViewModel("控件学习：自定义 LeanTextView", LeanTextViewActivity.class));
        list.add(new ViewModel("控件学习：自定义标签库", MultiLabelActivity.class));

        list.add(new ViewModel("Bitmap 压缩", BitmapTestActivity.class));
        list.add(new ViewModel("ViewPager加载Bitmap", ViewPagerBitmapTestActivity.class));
        list.add(new ViewModel("GridView加载Bitmap", GridViewBitmapTestActivity.class));

        list.add(new ViewModel("ScrollView：滑动冲突", BookListActivity.class));
        list.add(new ViewModel("ScrollView：嵌套 RecyclerView", ScrollViewAndRecyclerViewActivity.class));
        list.add(new ViewModel("ScrollView：嵌套 Fragment 滑动冲突", ScrollViewTestActivity.class));
        list.add(new ViewModel("ScrollView：嵌套 EditText", Main2Activity.class)); //ViewAnimationActivity


        list.add(new ViewModel("基础知识：BroadcastReceiver", BroadcastTestActivity.class));
        list.add(new ViewModel("基础知识：Tab 中Fragment 生命周期", HomeTabActivity.class));
        list.add(new ViewModel("基础知识：IntentService", IntentServiceTestActivity.class));
        list.add(new ViewModel("基础知识：Lopper", LooperTestActivity.class));
//        list.add(new ViewModel("To Jni Test", JniTestActivity.class));
        list.add(new ViewModel("基础知识：注解", AnnotationTestActivity.class));
        list.add(new ViewModel("基础知识：枚举", IntDefTestActivity.class));
        list.add(new ViewModel("基础知识：内存", MemoryTabTestActivity.class));
        list.add(new ViewModel("基础知识：Xml 解析", XmlParserActivity.class));


        list.add(new ViewModel("功能实现：下载", NetWorkTestActivity.class));
        list.add(new ViewModel("功能实现：断点续传", AutoResumeDownloadActivity.class));
        list.add(new ViewModel("功能实现：上传", FileLoadActivity.class));
        list.add(new ViewModel("功能实现：倒计时", TimeCountDownActivity.class));
        list.add(new ViewModel("功能实现：定时器 AlarmManager", AlarmManagerTestActivity.class));
        list.add(new ViewModel("功能实现：Click 事件 Hook", HookClickListenerActivity.class));
        list.add(new ViewModel("功能实现：选择图片", GetPictureActivity.class)); // ChoosePhotoActivity

        list.add(new ViewModel("框架学习：OkHttp3", OkHttpTestActivity.class));
        list.add(new ViewModel("框架学习：EventBus", EventbusTestActivity.class));
        list.add(new ViewModel("框架学习：Zxing QrCode", ZxingCodeActivity.class));
        list.add(new ViewModel("框架学习：Retrofit", RetrofitTestActivity.class));
        list.add(new ViewModel("框架学习：Retrofit Interceptor", InterceptorTestActivity.class));
        list.add(new ViewModel("框架学习：RxJava", RxJavaTestActivity.class));
        list.add(new ViewModel("框架学习：Dagger2.0", com.xss.mobile.activity.dagger2.DaggerTestActivity.class));
        list.add(new ViewModel("框架学习：Dagger2.0 Scope", DaggerTestScopeActivity.class));
        list.add(new ViewModel("框架学习：DataBinding", DatabindingTestActivity.class));
        list.add(new ViewModel("框架学习：DataBinding 与列表控件", CustomSetterBindingAdapterTestActivity.class));
        list.add(new ViewModel("框架学习：DataBinding 自定义名称", CustomBindingActivity.class));

        return list;
    }

    private void testReflect() {
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
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void useDexClassLoader() {
        Intent intent = new Intent("com.xss.net.retrofitapplication", null);
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
            btn.setGravity(Gravity.LEFT);
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
