package com.themisdarelis.processtool;

import com.themisdarelis.processtool.ui.MainFrame;
import javax.swing.UIManager;
import java.awt.Font;
import javax.swing.plaf.FontUIResource;

public class App
{
    private static void setUIFont(FontUIResource font) {
        var keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, font);
            }
        }
    }

    public static void main( String[] args )
    {
        // Set default font size for all Swing components
        FontUIResource font = new FontUIResource("SansSerif", Font.PLAIN, 24);
        setUIFont(font);
        new MainFrame();
    }
}
