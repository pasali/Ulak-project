package com.pasali.ulak;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity implements OnItemClickListener {

	private MsgDAO msgDao;
	private ConcurrentHashMap<String, String> numbers;
	static ListView lv;
	private ArrayList<String> keys;
	private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		this.registerReceiver(new BroadcastReceiver() {
		    @Override
		    public void onReceive(Context context, Intent intent) {
		        onResume();
		    }
		}, new IntentFilter("UpdateListView"));
		
		msgDao = new MsgDAO(this);
		lv = (ListView) findViewById(R.id.list);
		numbers = msgDao.getAllNo();
		keys = new ArrayList<String>(numbers.keySet());
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, keys);
		lv.setAdapter(adapter);
		lv.setTextFilterEnabled(true);
		lv.setClickable(true);
		lv.setOnItemClickListener(this);
		registerForContextMenu(lv);
	}

	@Override
	protected void onResume() {
		super.onResume();
		numbers = msgDao.getAllNo();
		lv = (ListView) findViewById(R.id.list);
		keys = new ArrayList<String>(numbers.keySet());
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, keys);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				adapter.notifyDataSetChanged();
			}
		});
		lv.setAdapter(adapter);
	}

	private boolean isServerRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (ServerService.class.getName().equals(
					service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Listedeki elemana klik işlemini ekle
	 */
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent showIntent = new Intent(getApplicationContext(),
				MessagesActivity.class);
		String value = lv.getItemAtPosition(position).toString();
		showIntent.putExtra("id", value);
		startActivity(showIntent);
	}

	/*
	 * Option menusü oluştur
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	/*
	 * Option menusunden tıklanınca sunucuyu çalıştır
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.start:
			if (!isServerRunning()) {
				Intent serviceIntent = new Intent(this, ServerService.class);
				startService(serviceIntent);
			}
			return true;
		default:
			return false;
		}
	}

	/*
	 * Basılı tutunca menü çıksın
	 */
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
	}

	/*
	 * Basılı tutma işlemini kontrol et "sil" seçeneğini gerçekle
	 */
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.delete:
			String value = lv.getItemAtPosition(info.position).toString();
			msgDao.delAllMsg(value);
			onResume();
			return true;
		}
		return false;
	}
}
