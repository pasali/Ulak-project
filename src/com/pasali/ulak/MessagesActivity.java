package com.pasali.ulak;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MessagesActivity extends Activity implements android.view.View.OnClickListener {
	
	private String id;
	private Message msg;
	private TextView msgview;
	private EditText msg_txt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messages);
		Button gonder = (Button) findViewById(R.id.button1);
		gonder.setOnClickListener(this);
		Bundle extras = getIntent().getExtras(); 
		if(extras != null) {
		    id = extras.getString("id");
		}
		msgview = (TextView) findViewById(R.id.textView1);
		msg = new MsgDAO(getApplicationContext()).getMsg(Long.valueOf(id));	
		msgview.setText(msg.getBody());
		setTitle(msg.getNo());
	}
	
	public void onClick(View arg0) {
		sendMsg();
		
	}
	
	public void sendMsg() {
		msg_txt = (EditText) findViewById(R.id.editText1);
		String pkg = msg_txt.getText().toString() + "|" + msg.getNo();
		msg_txt.setText("");
		ServerService.out.print(pkg + "\n");
		ServerService.out.flush();
		System.out.println(pkg);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.messages, menu);
		return true;
	}

	

}
