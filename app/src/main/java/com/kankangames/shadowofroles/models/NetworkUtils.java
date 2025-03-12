package com.kankangames.shadowofroles.models;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkUtils {
    public static String getHostName(String ipAddress) {
        try {
            InetAddress addr = InetAddress.getByName(ipAddress);
            return addr.getHostName(); // Cihazın hostname'ini döndürür
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "Unknown Device";
        }
    }
}
