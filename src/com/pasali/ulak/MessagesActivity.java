package com.pasali.ulak;

import java.util.ArrayList;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MessagesActivity extends Activity implements android.view.View.OnClickListener {
	
	private String id;
	private ArrayList<String> txts;
	private int sizeOfText;
	private TextView[] views;
	private EditText msg_txt;
	
	
	@Override
	/*
	 * id'yi al veritabanından ilgili numaraya ait mesajları getir
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messages);
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear);
		Button gonder = (Button) findViewById(R.id.button1);
		gonder.setOnClickListener(this);
		Bundle extras = getIntent().getExtras(); 
		if(extras != null) {
		    id = extras.getString("id");
		}
		txts = new ArrayList<String>();
		txts = new MsgDAO(getApplicationContext()).getAllMsgsByNo(id);	
		sizeOfText = txts.size();
		views = new TextView[sizeOfText];
		for (int i = 0; i < sizeOfText; i++) {
		    final TextView newTextView = new TextView(this);
		    newTextView.setText(txts.get(i) + "\n");
		    newTextView.setTextSize(30);  
		    linearLayout.addView(newTextView);
		    views[i] = newTextView;
		}
		setTitle(id);
	}
	
	public void onClick(View arg0) {
		sendMsg();
		
	}
	/*
	 * Cevap mesajını istemciye gönder
	 */
	public void sendMsg() {
		msg_txt = (EditText) findViewById(R.id.editText1);
		String pkg = msg_txt.getText().toString() + "|" + id;
		msg_txt.setText("");
		ServerService.out.print(pkg + "\n");
		ServerService.out.flush();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.messages, menu);
		return true;
	}

	

}
