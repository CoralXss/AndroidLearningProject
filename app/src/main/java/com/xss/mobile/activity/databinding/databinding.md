1. 绑定的 PoJo 数据必须只读并且不被修改；

2. BindingClass 是通过layout布局名称而生成的，布局中需要用 <layout></layout> 包围才能在编译期生成BindingClass；
   例如，activity_main.xml 对应 ActivityMainBinding；
   作用：通过 binding表达式 绑定 PoJo 数据到 layout view 。

3. 如何获取 BindClass ?
   Activity 中： ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

   ListView 或 RecyclerView 中，两种方式：
   1）通过编译生成的BindClass获取实例：
      AdapterListItemBinding bd = AdapterListItemBinding.inflate(inflater, viewGroup, false);
   2）通过 DataBindingUtil 加载布局文件得到：
      AdapterListItemBinding bd = DataBindingUtil.inflate(inflater,
                                               R.layout.adapter_list_item, viewGroup, false);

4. 如何给View绑定点击事件 ？
   两种方式：一是方法引用，设置 android:onClick="@{clickEvent::clickMethod}"；
            二是 Listeners binding ...

-> 如何在 RecyclerView 中使用 Databinding ?
   详见 DataBindingAdapter .

5. 使用 <import> 标签 import 相应类，在布局文件中可以直接引用该类及其属性。

   注：java.lang.* 包中的类会自动导入，所以若 PoJo 的属性不是String 时，可以设置： android:text="@{String.valueOf(user.age)}" 。
   其他的类则需要 import 引入后才可以调用其方法设置。

6. Variables 变量的使用
   1）在 <data></data> 元素之间可以包含任意数量的 <variable>，每个变量使用时都需要调用 set 方法设置赋值；
   2）variable 变量会在编译期尽心检查，若变量实现了 Observable 或者 observable collection ，就需要映射为具体类型；
   3）若变量是 没有实现 Observable 接口的基类或者接口，则变量不会被通知将属性值绑定到 View；
   4）生成的 BindingClass 具有设置的 Variable变量的 setter 和 getter，变量最开始是默认值，当 setter 方法被调用时才会具有真正的值；
   5）特殊变量如 Context，会在使用 binding 表达式时生成，context 变量的值来自 rootView 的 getContext() ；

7. 自定义 BindingClass 名称
   BindingClass 生成规则：根据 layout 布局文件名，删除下划线，驼峰命名规则，后缀"Binding"。
   BindingClass 可能会重命名或者移到其他包下，可以通过 class 属性确定生成的位置，避免后期更换位置出现的编译错误：

   设定当前 modulePackage = "com.xss.mobile"：
   1）<data class="UserBinding">...</data>
      此种写法生成的 UserBinding 在 modulePackage + .databinding 下。

   2）<data class=".UserBinding">...</data>
      此种写法生成的 UserBinding 在 modulePackage 下。

   3）<data class="com.xss.mobile.custom.UserBinding">...</data>
     此种写法生成的 UserBinding 在固定包名 com.xss.mobile.custom 下。

   默认的没有设置 class 属性时，同写法一，生成在 modulePackage.databinding 下。

8. <include> 布局时，如何传递 variable 到 include 的布局中？
   1）在根布局添加 res-auto 引用 app:xmlns，给 <include> 添加 app:variableName="@{variableName}";
   2）include 布局中也要添加同样的 <data> <variable>.
   注：主布局和 <include> 子布局不能设置同一个 BindingClass，也即是 data class 属性不能设置同一个值，否则编译报错。

9. 绑定表达式
   1）算法表达式：+ - * / %
   2）String 连接运算符：+
   3）逻辑表达式： && ||
   4）位运算：& | ^
   5）三目运算符 ?:
   6）数组下标取值：[]
   7）类型转换 instanceof
   8）方法调用

   三目运算新写法：android:text="@{user.firstName ?? ""}"
          等价于：android:text="@{user.firstName != null ? user.firstName : ""}"

   android:text='@{"年龄: " + user.username}'

   android:text="@{user.name}" 在生成BindingClass时会自动检查空指针异常，当该表达式中 user = null 时，会直接取user.name得默认值null.

10. 集合数据绑定：arrays list sparseArray map，可以通过 [] 获取值。
        <import type="android.util.SparseArray"/>
        <import type="java.util.Map"/>
        <import type="java.util.List"/>
        <variable name="list" type="List&lt;String&gt;"/>
        <variable name="sparse" type="SparseArray&lt;String&gt;"/>
        <variable name="map" type="Map&lt;String, String&gt;"/>
        <variable name="index" type="int"/>
        <variable name="key" type="String"/>

    取值： @{list[index]} @{sparse[index]} @{map[key]}
    注：map取值两种正确写法：android:text='@{map["firstName"]}'  android:text="@{map['firstName']}"

11. 资源数据绑定：
    android:text="@{@string/nameStrRes(firstName, lastName)}"
    android:padding="@{large? @dimen/largePadding : @dimen/smallPadding}"

12. 任何 PoJo 数据均可用于数据绑定，但是修改 PoJo 并不会更新 UI ，如何在数据修改后更新 UI 呢？
    三种数据 notifyChanged 机制：Observable objects, observable fields, observable collections .
    给UI绑定上述三种数据，可以实现数据更新时，UI自动更新。

    Observable objects 机制：
        1）创建 PoJo extends BaseObservable;
        2) 给需要时常更新的属性 get 方法添加 @Bindable 注解，并在 set 方法调用 notifyPropertyChanged() 方法；
        3）上述步骤可以在数据更新时更新UI，没有设置的属性则改变则不会更新UI。

    observable fields 机制：
        1）

13. 高级绑定 - 动态的变量
    1）例子：RecyclerView.Adapter 在处理 layout 时并不知道具体的 BindingClass，必须在 onBindViewHolder() 方法中手动设置绑定的数据。
    `````````````````````````
    public void onBindViewHolder(BindingHolder holder, int position) {
       final T item = mItems.get(position);
       holder.getBinding().setVariable(BR.item, item);
       holder.getBinding().executePendingBindings();   // 立即绑定
    }
    `````````````````````````
    2）立即绑定：executePendingBindings()；
    3）非集合数据可以在后台线程中改变数据，DataBinding 对每个 variable/field 做了局部化处理可以避免任何并发问题。

其他用法：
1. 若绑定的是自定义 View，没有具体的 layout 文件，此时不会自动生成 BindingClass，则可以自定义 LayoutBinding进行绑定；
   （详见 Napos，EvaluateRiderView）







官网：https://developer.android.com/topic/libraries/data-binding/index.html#studio_support