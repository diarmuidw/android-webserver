package com.bolutions.webserver;


import java.io.*;
import java.util.*;
import android.util.Log;



public class HelloServer extends NanoHTTPD
{
	private static final String TAG = "HelloServer";
	
	public HelloServer() throws IOException
	{
		super(8000, new File("."));
	}

	public Response serve( String uri, String method, Properties header, Properties parms, Properties files )
	{
		Log.d(TAG,  method + " '" + uri + "' ");
		
		
		String msg = "<html><body><h1>Hello server</h1>\n";
		if ( parms.getProperty("username") == null )
			msg +=
			"<form action='?' method='get'>\n" +
					" <p>Your name: <input type='text' name='username'></p>\n" +
					"</form>\n";
		else
			msg += "<p>Hello, " + parms.getProperty("username") + "!</p>";

		msg += "</body></html>\n";
		return new NanoHTTPD.Response( HTTP_OK, MIME_HTML, msg );
	}


}