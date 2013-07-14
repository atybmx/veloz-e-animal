/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.bubble.velozeanimal;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author atybmx
 */
public class Chegada implements ScreenController {
 
  private Nifty nifty;
  private Screen screen;
  private Corrida corrida;
  private BubbleGame app;
 
  /** custom methods */ 
 
  public Chegada(Corrida corrida) { 
      this.corrida = corrida;
    /** Your custom constructor, can accept arguments */ 
  } 
 
  /** Nifty GUI ScreenControl methods */ 
 
  public void bind(Nifty nifty, Screen screen) {
    this.nifty = nifty;
    this.screen = screen;
  }
 
  public void onStartScreen() { 
        NiftyImage primeiro = nifty.getRenderEngine().createImage("Textures/chegada/"+corrida.getPrimeiroColocado()+".png", false);
        Element niftyElementPrimeiro = nifty.getCurrentScreen().findElementByName("primeiroColocado");
        niftyElementPrimeiro.getRenderer(ImageRenderer.class).setImage(primeiro);
        
        NiftyImage segundo = nifty.getRenderEngine().createImage("Textures/chegada/"+corrida.getSegundoColocado()+".png", false);
        Element niftyElementSegundo = nifty.getCurrentScreen().findElementByName("segundoColocado");
        niftyElementSegundo.getRenderer(ImageRenderer.class).setImage(segundo);
        
        NiftyImage terceiro = nifty.getRenderEngine().createImage("Textures/chegada/"+corrida.getTerceiroColocado()+".png", false);
        Element niftyElementTerceiro = nifty.getCurrentScreen().findElementByName("terceiroColocado");
        niftyElementTerceiro.getRenderer(ImageRenderer.class).setImage(terceiro);
        
        NiftyImage quarto = nifty.getRenderEngine().createImage("Textures/chegada/"+corrida.getQuartoColocado()+".png", false);
        Element niftyElementQuarto = nifty.getCurrentScreen().findElementByName("quartoColocado");
        niftyElementQuarto.getRenderer(ImageRenderer.class).setImage(quarto);
  }
 
  public void onEndScreen() { 
  
  }

    
}
