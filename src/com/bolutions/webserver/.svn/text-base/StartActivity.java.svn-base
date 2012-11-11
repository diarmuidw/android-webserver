/*
 * Copyright (C) 2009,2010 Markus Bode Internetlšsungen (bolutions.com)
 * 
 * Licensed under the GNU General Public License v3
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @author Markus Bode
 * @version $Id: StartActivity.java 727 2011-01-02 13:04:32Z markus $
 */

package com.bolutions.webserver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;

import com.bolutions.webserver.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class StartActivity extends Activity {
    private ToggleButton mToggleButton;
    private EditText port;
    private Server server;
    private static TextView mLog;
    private static ScrollView mScroll;
    private NotificationManager mNotificationManager;
    
    final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Bundle b = msg.getData();
			log(b.getString("msg"));
		}
    };

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        
        mToggleButton = (ToggleButton) findViewById(R.id.toggle);
        port = (EditText) findViewById(R.id.port);
        mLog = (TextView) findViewById(R.id.log);
        mScroll = (ScrollView) findViewById(R.id.ScrollView01);
        
        String pfad = "/sdcard/com.bolutions.webserver/";
        
        boolean exists = (new File(pfad)).exists();
        try {
	        if (!exists) {
	        	(new File(pfad)).mkdir();
	         	BufferedWriter bout = new BufferedWriter(new FileWriter(pfad+"index.html"));
	         	bout.write("<html><head><title>Android Webserver powered by bolutions.com</title>");
	         	bout.write("</head>");
	         	bout.write("<body>Willkommen auf dem Android Webserver powered by <a href=\"http://www.bolutions.com\">bolutions.com</a>.");
	         	bout.write("<br><br>Die HTML-Dateien liegen in /sdcard/com.bolutions.webserver/</body></html>");
	         	bout.flush();
	         	bout.close();
	         	bout = new BufferedWriter(new FileWriter(pfad+"403.html"));
	         	bout.write("<html><head><title>Error 403 powered by bolutions.com</title>");
	         	bout.write("</head>");
	         	bout.write("<body>403 - Forbidden</body></html>");
	         	bout.flush();
	         	bout.close();
	         	bout = new BufferedWriter(new FileWriter(pfad+"404.html"));
	         	bout.write("<html><head><title>Error 404 powered by bolutions.com</title>");
	         	bout.write("</head>");
	         	bout.write("<body>404 - File not found</body></html>");
	         	bout.flush();
	         	bout.close();
	        }
        }catch (Exception e) {
        	Log.v("ERROR",e.getMessage());
        }
        mToggleButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if( mToggleButton.isChecked() ) {
					startServer(new Integer(port.getText().toString()));
				} else {
					stopServer();
				}
			}
		});
        log("");
        log("Please mail suggestions to bolutions@googlemail.com");
    }

    private void stopServer() {
    	if( server != null ) {
    		server.stopServer();
    		server.interrupt();
    		log("Server was killed.");
    		mNotificationManager.cancelAll();
    	}
    	else
    	{
    		log("Cannot kill server!? Please restart your phone.");
    	}
    }
    
    public static void log( String s ) {
    	mLog.append(s + "\n");
    	mScroll.fullScroll(ScrollView.FOCUS_DOWN);
    }
    
    public static String intToIp(int i) {
        return ((i       ) & 0xFF) + "." +
               ((i >>  8 ) & 0xFF) + "." +
               ((i >> 16 ) & 0xFF) + "." +
               ( i >> 24   & 0xFF);
    }
    
    private void startServer(int port) {
    	try {
    		WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
    		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
    		
    		String ipAddress = intToIp(wifiInfo.getIpAddress());

    		if( wifiInfo.getSupplicantState() != SupplicantState.COMPLETED) {
    			new AlertDialog.Builder(this).setTitle("Error").setMessage("Please connect to a WIFI-network for starting the webserver.").setPositiveButton("OK", null).show();
    			throw new Exception("Please connect to a WIFI-network.");
    		}
            
    		log("Starting server "+ipAddress + ":" + port + ".");
		    server = new Server(ipAddress,port,mHandler);
		    server.start();
		    
	        Intent i = new Intent(this, StartActivity.class);
	        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, i, 0);

	        Notification notif = new Notification(R.drawable.icon, "Webserver is running", System.currentTimeMillis());
	        notif.setLatestEventInfo(this, "Webserver", "Webserver is running", contentIntent);
	        notif.flags = Notification.FLAG_NO_CLEAR;
	        mNotificationManager.notify(1234, notif);
    	} catch (Exception e) {
    		log(e.getMessage());
    		mToggleButton.setChecked(false);
    	}
    	
    }
}