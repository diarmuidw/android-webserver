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
 * @version $Id: ServerHandler.java 727 2011-01-02 13:04:32Z markus $
 */
package com.bolutions.webserver;
import java.io.*;
import java.net.*;

import android.content.res.AssetManager;

class ServerHandler extends Thread {
  private BufferedReader in;
  private PrintWriter out;
  private Socket toClient;
  private AssetManager assetManager = null;
  
  ServerHandler(Socket s, AssetManager am) {
    toClient = s;
    assetManager = am;
    
    
  }

  @Override
public void run() {
	String dokument = "";
	
	

    try {
      in = new BufferedReader(new InputStreamReader(toClient.getInputStream()));

      // Receive data
      while (true) {
        String s = in.readLine().trim();

        if (s.equals("")) {
          break;
        }
        
        if (s.substring(0, 3).equals("GET")) {
         int leerstelle = s.indexOf(" HTTP/");
         dokument = s.substring(5,leerstelle);
         dokument = dokument.replaceAll("[/]+","/");
        }
      }
    }
    catch (Exception e) {
     Server.remove(toClient);
     try
{
     toClient.close();
}
     catch (Exception ex){}
    }
    
	if (dokument.equals("")) dokument = "index.html";

	// Don't allow directory traversal
	if (dokument.indexOf("..") != -1) dokument = "403.html";



	String headerBase = "HTTP/1.1 %code%\n"+
	"Server: Bolutions/1\n"+
	"Content-Length: %length%\n"+
	"Connection: close\n"+
	"Content-Type: text/html; charset=iso-8859-1\n\n";

	String header = headerBase;

    header = headerBase.replace("%code%", "200 OK");

	
    
    InputStream input;
    try{
    	input = assetManager.open(dokument);

    	
  	  BufferedInputStream in = new BufferedInputStream(input);
  	  BufferedOutputStream out = new BufferedOutputStream(toClient.getOutputStream());
  	  ByteArrayOutputStream tempOut = new ByteArrayOutputStream();
  	  
  	  byte[] buf = new byte[4096];
  	  int count = 0;
  	  while ((count = in.read(buf)) != -1){
  		  tempOut.write(buf, 0, count);
  	  }

  	  tempOut.flush();
  	  header = header.replace("%length%", ""+tempOut.size());

  	  out.write(header.getBytes());
  	  out.write(tempOut.toByteArray());
  	  out.flush();
      Server.remove(toClient);
	  toClient.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
 
  }
}
