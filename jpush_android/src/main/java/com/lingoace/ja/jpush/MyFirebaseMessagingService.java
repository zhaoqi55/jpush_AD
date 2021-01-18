package com.lingoace.ja.jpush;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.lingoace.ja.R;


/**
 * Created by HongJay on 2016/12/10.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String TAG = "MyFirebaseMessagingService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Service", "消息服务已启动");
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
//        Log.i("Service", "===============通知来啦=================");
//        Log.i("Service", "onMessageReceived: " + remoteMessage.getFrom());
//        if (remoteMessage.getData().size() > 0) {
//            Log.i("Service", "" + remoteMessage.getData());
//        }
//        if (remoteMessage.getNotification() != null) {
//            Log.i("Service", "" + remoteMessage.getNotification().getBody());
//            sendNotification(remoteMessage.getNotification().getBody());
//        }
        Log.d(TAG, "收到推送 From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "收到推送 Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "收到通知 Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendSubscribeMsg(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
        }
       // showNotify();

    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
        Log.i("Service", "onMessageSent" + s);
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("FCM Message")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setPriority(10)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void showNotify() {

        //第一步：实例化通知栏构造器Notification.Builder：
        Notification.Builder builder = new Notification.Builder(this);//实例化通知栏构造器Notification.Builder，参数必填（Context类型），为创建实例的上下文
//第二步：获取状态通知栏管理：
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);//获取状态栏通知的管理类（负责发通知、清除通知等操作）
//第三步：设置通知栏PendingIntent（点击动作事件等都包含在这里）:
        Intent push = new Intent(this, MainActivity.class);//新建一个显式意图，第一个参数 Context 的解释是用于获得 package name，以便找到第二个参数 Class 的位置
//PendingIntent可以看做是对Intent的包装，通过名称可以看出PendingIntent用于处理即将发生的意图，而Intent用来用来处理马上发生的意图
//本程序用来响应点击通知的打开应用,第二个参数非常重要，点击notification 进入到activity, 使用到pendingIntent类方法，PengdingIntent.getActivity()的第二个参数，即请求参数，实际上是通过该参数来区别不同的Intent的，如果id相同，就会覆盖掉之前的Intent了
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, push, 0);
//第四步：对Builder进行配置：
        builder
                .setContentTitle("My notification")//标题
                .setContentText("Hello World!")// 详细内容
                .setContentIntent(contentIntent)//设置点击意图
                .setTicker("New message")//第一次推送，角标旁边显示的内容
             //   .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))//设置大图标
                .setDefaults(Notification.DEFAULT_ALL);//打开呼吸灯，声音，震动，触发系统默认行为
/*Notification.DEFAULT_VIBRATE  //添加默认震动提醒 需要VIBRATE permission
Notification.DEFAULT_SOUND  //添加默认声音提醒
Notification.DEFAULT_LIGHTS//添加默认三色灯提醒
Notification.DEFAULT_ALL//添加默认以上3种全部提醒*/
//.setLights(Color.YELLOW, 300, 0)//单独设置呼吸灯，一般三种颜色：红，绿，蓝，经测试，小米支持黄色
//.setSound(url)//单独设置声音
//.setVibrate(new long[] { 100, 250, 100, 250, 100, 250 })//单独设置震动
//比较手机sdk版本与Android 5.0 Lollipop的sdk
        if (android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.LOLLIPOP) {
            builder
/*android5.0加入了一种新的模式Notification的显示等级，共有三种：
VISIBILITY_PUBLIC只有在没有锁屏时会显示通知
VISIBILITY_PRIVATE任何情况都会显示通知
VISIBILITY_SECRET在安全锁和没有锁屏的情况下显示通知*/
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
                    .setCategory(Notification.CATEGORY_MESSAGE)//设置通知类别
//.setColor(context.getResources().getColor(R.color.small_icon_bg_color))//设置smallIcon的背景色
                    .setFullScreenIntent(contentIntent, true)//将Notification变为悬挂式Notification
                    .setSmallIcon(R.drawable.ic_launcher_background);//设置小图标
        } else {
            builder
                    .setSmallIcon(R.drawable.ic_launcher_background);//设置小图标
        }
//第五步：发送通知请求：
        Notification notify = builder.build();//得到一个Notification对象
        mNotifyMgr.notify(1, notify);//发送通知请求
    }

    public void sendSubscribeMsg(String title,String content) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this, "subscribe")
                .setContentTitle(title)
                .setContentText(content)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_stat_ic_notification))
                .setAutoCancel(true)
                .build();
        manager.notify(2, notification);
    }
}
