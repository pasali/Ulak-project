package com.pasali.ulak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class ServerOnThread extends Thread {

	private ServerSocket serverSocket = null;
	private Socket clientSocket = null;
	static PrintWriter out = null;
	private BufferedReader in = null;
	private String inputLine;
	private String[] inData;
	private Context context;
	private MsgDAO msgDao;

	public ServerOnThread(Context con) {
		this.context = con;
		this.msgDao = new MsgDAO(context);
	}

	public void run() {
		try {
			serverSocket = new ServerSocket(1238);
		} catch (IOException e) {

			System.err.println("I/O : " + e.getMessage());
			System.exit(1);
		}
		System.out.println("Bağlantı bekleniyor...");
		try {
			clientSocket = serverSocket.accept();
			System.out.println(clientSocket.getLocalAddress() + " baglandı.");
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			while ((inputLine = in.readLine()) != null) {
				inData = inputLine.split("\\|");
				msgDao.addMsg(new Message(inData[1], inData[0]));
				createNotification();
			}
		} catch (IOException e) {
			System.err.println("Bağlantı hatasi.");
			System.exit(1);
		}
	}

	public void createNotification() {
		int last_id = msgDao.getLastId();
		Intent Oku_intent = new Intent(context, MessagesActivity.class);
		Oku_intent.putExtra("id", String.valueOf(last_id));
		PendingIntent p_oku = PendingIntent.getActivity(context, 0, Oku_intent,
				0);

		Intent Sil_intent = new Intent();
		Sil_intent.setAction("com.pasali.ulak.DEL_INTENT");
		Sil_intent.putExtra("id", String.valueOf(last_id));
		Sil_intent.putExtra("not_id", 0);

		PendingIntent p_sil = PendingIntent.getBroadcast(context, 0,
				Sil_intent, PendingIntent.FLAG_UPDATE_CURRENT);
		Notification noti = new Notification.Builder(context)
				.setContentTitle("Ulak:" + inData[1]).setContentText(inData[0])
				.setSmallIcon(R.drawable.ulak).setContentIntent(p_oku)
				.addAction(R.drawable.del, "Sil", p_sil).build();
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		noti.flags |= Notification.FLAG_AUTO_CANCEL;
		noti.defaults |= Notification.DEFAULT_SOUND;
		notificationManager.notify(0, noti);

	}

}
