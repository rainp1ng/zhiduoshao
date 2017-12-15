package com.util.method;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**ʵ�����ڵļӼ����ܣ��Լ�������ʱ���ʽ��
 * @author ������*/
public class DateMethod {
	public static int subDay2Date(int date,int day) throws ParseException{
		Date toDate=new Date(System.currentTimeMillis());
		SimpleDateFormat formatter=new SimpleDateFormat("yyyyMMdd");
		try {
			toDate=formatter.parse(""+date);
		}catch(ParseException e){
			e.printStackTrace();
		}
		Date newDate = null;
		if (""+date != null){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(toDate);
			calendar.add(Calendar.DAY_OF_MONTH, day);
			newDate = calendar.getTime();
		}
		int myDate=Integer.parseInt(formatter.format(newDate));
		//Log.e("����"+day+"��",myDate+"");
		return myDate;
	}
	
	public static String getYMD(int date){
		String dates=""+date;
		String year=dates.substring(0,4);
		String month=dates.substring(4,6);
		String day=dates.substring(6,8);
		String myDate=year+"��"+month+"��"+day+"��";
		return myDate;
	}
}
