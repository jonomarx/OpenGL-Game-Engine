/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.starter;

import java.io.IOException;
import java.net.URLDecoder;

/**
 * forces executation with arguments
 * @author Jon
 */
public class Starter {
    public static void main(String[] args) throws IOException {
        String path = Starter.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if(path.endsWith("/")) path += "*";
        path = path.replace("%20", " ");
        System.out.println(System.getProperty("os.name"));
        if(System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0) {
            Runtime.getRuntime().exec("javaw -XStartOnFirstThread -cp " + path + " com.jonmarx.core.Main");
            System.out.println("javaw -XStartOnFirstThread -cp \"" + path + "\" com.jonmarx.core.Main");
        } else {
            com.jonmarx.core.Main.main(new String[0]);
        }
    }
}
