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
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Date;
import java.util.Enumeration;

//import com.bolutions.webserver.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;



public class StartActivity extends Activity {
    private static final String TAG = null;
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
    		String ipAddress = "0.0.0.0";
    		
            AssetManager assetManager = getAssets();

    		log("Starting server "+ipAddress + ":" + port + ".");
		    server = new Server(ipAddress,port,mHandler, assetManager);
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