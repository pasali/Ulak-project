package com.pasali.ulak;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class DeleteReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		String id = null;
		Bundle extras = arg1.getExtras(); 
		if(extras != null) {
		    id = extras.getString("id");
		}
		MsgDAO del = new MsgDAO(null);
		del.msgDel(Long.valueOf(id));
	}
}
