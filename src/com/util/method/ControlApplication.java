package com.util.method;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
/**在子Activity中需要完全退出程序时，该类可以调用
 * ControlApplication.getInstance().exit()方法以finish所有的活动
 * 要被finish的活动需要在onCreate的时候调用ControlApplication.
 * getInstance().addActivity(this)将活动加入并管理
 * @author 雨中树
 * */
public class ControlApplication extends Application{
	private List<Activity> activities = null;
    private static ControlApplication instance;
 
    private ControlApplication() {
        activities = new LinkedList<Activity>();
    }
    public static ControlApplication getInstance() {
        if (null == instance) {
            instance = new ControlApplication();
        }
        return instance;
    }
    public void addActivity(Activity activity) {
        if (activities != null && activities.size() > 0) {
            if(!activities.contains(activity)){
                activities.add(activity);
            }
        }else{
            activities.add(activity);
        }
    }
    public void exit() {
        if (activities != null && activities.size() > 0) {
            for (Activity activity:activities) {
                activity.finish();
            }
        }
        System.exit(0);
    }
    public void toHome() {
        if (activities != null && activities.size() > 0) {
            for (Activity activity=activities.get(1);activities.size()>1;activity=activities.get(1)) {
                activity.finish();
            }
        }
        System.exit(0);
    }
}
