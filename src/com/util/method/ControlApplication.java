package com.util.method;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
/**����Activity����Ҫ��ȫ�˳�����ʱ��������Ե���
 * ControlApplication.getInstance().exit()������finish���еĻ
 * Ҫ��finish�Ļ��Ҫ��onCreate��ʱ�����ControlApplication.
 * getInstance().addActivity(this)������벢����
 * @author ������
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
