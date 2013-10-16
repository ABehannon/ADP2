//Alex Behannon
//Java Week 3
//07-26-2013

package com.behannon.huntingcompanion.lib;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Web {
	
    static Boolean _connected = false;

    static String _connectionType = "Unavailable";

    public static String getConnectionType(Context context) {
        // Call newInfo method
        netInfo(context);

        return _connectionType;
    }

    public static Boolean getConnectionStatus(Context context) {
        // Call netInfo method
        netInfo(context);

        return _connected;
    }

    private static void netInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null) {
            if (ni.isConnected()) {
                _connectionType = ni.getTypeName();
                _connected = true;
            }
        }
    }

	
	public static String getURLStringResponse(URL url) {
        String response = "";

        try {
            URLConnection conn = url.openConnection();
            BufferedInputStream bin = new BufferedInputStream(conn.getInputStream());

            byte[] contentBytes = new byte[1024];
            int bytesRead = 0;
            // StringBulider is a safer call than StringBuffer
            StringBuilder responseBuffer = new StringBuilder();

            while ((bytesRead = bin.read(contentBytes)) != -1) {
                response = new String(contentBytes, 0, bytesRead);
                responseBuffer.append(response);
            }
            return responseBuffer.toString();
        } catch (Exception e) {
            Log.e("URL RESPONSE ERROR", e.toString());
        }
        return response;
    }
}