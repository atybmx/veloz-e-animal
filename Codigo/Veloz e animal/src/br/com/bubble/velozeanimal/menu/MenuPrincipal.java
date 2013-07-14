/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.bubble.velozeanimal.menu;

import br.com.bubble.velozeanimal.BubbleGame;
import br.com.bubble.velozeanimal.Corrida;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author atybmx
 */
public class MenuPrincipal implements ScreenController{
    
    private Nifty nifty;
    private Screen screen;
    NiftyJmeDisplay niftyDisplay;
    BubbleGame app;

    public MenuPrincipal(NiftyJmeDisplay niftyDisplay,BubbleGame app) {
        this.niftyDisplay = niftyDisplay;
        this.app = app;
    }

    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    public void onStartScreen() {

    }

    public void onEndScreen() {

    }
    
    public void jogar(){
        app.getViewPort().removeProcessor(niftyDisplay);
        nifty.exit();
        Corrida corrida = new Corrida();
        app.getStateManager().attach(corrida);
    }
    
    public void sobre(){
        nifty.gotoScreen("sobre");
    }
    
    public void menu(){
        nifty.gotoScreen("menuPrincipal");
    }
    
}
