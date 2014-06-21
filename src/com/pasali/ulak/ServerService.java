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
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class ServerService extends Service {

	private ServerSocket serverSocket = null;
	private Socket clientSocket = null;
	static PrintWriter out = null;
	private BufferedReader in = null;
	private String inputLine;
	private static final int PORT = 5353;
	private String[] inData;
	private MsgDAO msgdao;
	private Handler h;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new Thread(new runServer()).start();

		return super.onStartCommand(intent, flags, startId);
	}

	class runServer implements Runnable {

		public void run() {
			/*
			 * Sunucuyu çalıştır
			 */
			msgdao = new MsgDAO(getApplicationContext());
			try {
				serverSocket = new ServerSocket(PORT);
			} catch (IOException e) {
				System.err.println("I/O : " + e.getMessage());
				System.exit(1);
			}
			h = new Handler(getApplicationContext().getMainLooper());
			h.post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(),
							"Bağlantı bekleniyor...", Toast.LENGTH_LONG).show();
				}
			});
			try {
				/*
				 * Bağlantıları kabul et
				 */
				clientSocket = serverSocket.accept();
				h.post(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(getApplicationContext(),
								clientSocket.getLocalAddress() + " baglandı.",
								Toast.LENGTH_LONG).show();
					}
				});
				/*
				 * Gelen ve giden veri yollarını oluştur
				 */
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(
						clientSocket.getInputStream()));
				/*
				 * İstemciden gelen mesajları al, veritabanına kaydet - bildirim oluştur
				 */
				while ((inputLine = in.readLine()) != null) {
					inData = inputLine.split("\\|");
					msgdao.addMsg(new Message(inData[1], inData[0]));
					createNotification();
				}
			} catch (IOException e) {
				System.err.println("Bağlantı hatasi.");
				System.exit(1);
			}

		}
	}

	public void createNotification() {
		/*
		 * Listeyi güncelle
		 */
		Intent intent = new Intent("UpdateListView");
	    this.sendBroadcast(intent);
	    /*
	     * Son mesajın id'sini öğren
	     */
		int last_id = msgdao.getLastId();
		Message msg = msgdao.getMsg(last_id);
		/*
		 * Bildiri oluştur, oku ve sil butonlarını gerçekle
		 */
		Intent Oku_intent = new Intent(this, MessagesActivity.class);
		Oku_intent.putExtra("id", msg.getNo());
		
		Intent Sil_intent = new Intent();
		Sil_intent.setAction("com.pasali.ulak.DEL_INTENT");
		Sil_intent.putExtra("id", String.valueOf(last_id));
		Sil_intent.putExtra("not_id", 0);
		
		PendingIntent p_oku = PendingIntent.getActivity(this, 0, Oku_intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		PendingIntent p_sil = PendingIntent.getBroadcast(this, 0, Sil_intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		
		Notification noti = new Notification.Builder(this)
				.setContentTitle("Ulak:" + inData[1]).setContentText(inData[0])
				.setSmallIcon(R.drawable.ulak).setContentIntent(p_oku)
				.addAction(R.drawable.del, "Sil", p_sil).build();
		
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		noti.flags |= Notification.FLAG_AUTO_CANCEL;
		noti.defaults |= Notification.DEFAULT_SOUND;
		notificationManager.notify(0, noti);

	}

}
