package com.ccand99.serialport;

public class Platform {

    static boolean isWindows = false;
    static boolean isLinux = false;

    static {
        String osName = System.getProperties().getProperty("os.name");
        if(osName.equals("Linux")) {
            isLinux = true;
        } else {
            isWindows = true;
        }
    }
}
