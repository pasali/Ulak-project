package com.pasali.ulak;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class DeleteReceiver extends BroadcastReceiver{

	@Override
	/*
	 * Veritabanından mesaj sil, listView'i güncelle
	 */
	public void onReceive(Context arg0, Intent arg1) {
		String id = null;
		int not_id = 0;
		Bundle extras = arg1.getExtras(); 
		if(extras != null) {
		    id = extras.getString("id");
		    not_id = extras.getInt("not_id");
		    MsgDAO del = new MsgDAO(arg0);
			del.delMsg(Long.valueOf(id));
		}
		Intent intent = new Intent("UpdateListView");
	    arg0.sendBroadcast(intent);
		NotificationManager nManager = (NotificationManager) arg0.getSystemService(Service.NOTIFICATION_SERVICE);
		nManager.cancel(not_id);
	}
}
