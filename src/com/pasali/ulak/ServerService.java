package com.pasali.ulak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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
			msgdao = new MsgDAO(getApplicationContext());
			try {
				serverSocket = new ServerSocket(1238);
			} catch (IOException e) {
				System.err.println("I/O exception: " + e.getMessage());
				System.exit(1);
			}
			h = new Handler(getApplicationContext().getMainLooper());
			h.post(new Runnable() {
		        @Override
		        public void run() {
		             Toast.makeText(getApplicationContext(),"Bağlantı bekleniyor...",Toast.LENGTH_LONG).show();
		        }
		    });
			try {
				clientSocket = serverSocket.accept();
				h.post(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(getApplicationContext(),
								clientSocket.getLocalAddress() + " baglandı.",
								Toast.LENGTH_LONG).show();
					}
				});
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(
						clientSocket.getInputStream()));
				while ((inputLine = in.readLine()) != null) {
					inData = inputLine.split("\\|");
					System.out.println(inData);
					msgdao.msgAdd(new Message(inData[1], inData[0]));

				}
			} catch (IOException e) {
				System.err.println("Accept failed.");
				System.exit(1);
			}

		}
	}

}
