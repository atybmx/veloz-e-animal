/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.bubble.velozeanimal.menu;

import br.com.bubble.velozeanimal.BubbleGame;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author atybmx
 */
public class Introducao implements ScreenController{
    
    private Nifty nifty;
        NiftyJmeDisplay niftyDisplay;
    BubbleGame app;

    public Introducao(NiftyJmeDisplay niftyDisplay, BubbleGame app) {
        this.niftyDisplay = niftyDisplay;
        this.app = app;
    }
    
    

    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
    }

    public void onStartScreen() {
        nifty.fromXml("Interface/menuPrincipal.xml", "menuPrincipal", new MenuPrincipal(niftyDisplay,app));        
    }

    public void onEndScreen() {
        
    }
    
}
