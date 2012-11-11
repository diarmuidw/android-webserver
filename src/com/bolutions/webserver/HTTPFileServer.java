
package com.bolutions.webserver;


import java.io.*;
import java.util.*;
import android.util.Log;
import android.content.Context;
import android.content.res.AssetManager;




public class HTTPFileServer extends NanoHTTPD
{
	private static final String TAG = "HTTPFileServer";
	
	private AssetManager assetManager = null;
	
	public HTTPFileServer(AssetManager am) throws IOException
	{
		
		super(8000, new File("."));
		assetManager = am;
	}

	public Response serve( String uri, String method, Properties header, Properties parms, Properties files )
	{
		Log.d(TAG,  method + " '" + uri + "' ");
		
		String fileName = uri.substring(1);
		String msg = ReadFromfile(fileName);
	    InputStream input = null;

	    	try {
				input = assetManager.open("osimage.jpg");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    
		
	    //return new NanoHTTPD.Response( HTTP_OK, MIME_JPG, input );
		return new NanoHTTPD.Response( HTTP_OK, MIME_JPG, input );
		
	}

	    private String ReadFromfile(String fileName) {
	        StringBuilder ReturnString = new StringBuilder();
	        InputStream fIn = null;
	        InputStreamReader isr = null;
	        BufferedReader input = null;
	        try {
	            fIn = assetManager.open(fileName);
	            isr = new InputStreamReader(fIn);
	            input = new BufferedReader(isr);
	            String line = "";
	            while ((line = input.readLine()) != null) {
	                ReturnString.append(line);
	            }
	        } catch (Exception e) {
	            e.getMessage();
	        } finally {
	            try {
	                if (isr != null)
	                    isr.close();
	                if (fIn != null)
	                    fIn.close();
	                if (input != null)
	                    input.close();
	            } catch (Exception e2) {
	                e2.getMessage();
	            }
	        }
	        return ReturnString.toString();
	    }


}