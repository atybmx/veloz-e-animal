package br.com.bubble.velozeanimal;

import br.com.bubble.velozeanimal.menu.Introducao;
import br.com.bubble.velozeanimal.menu.MenuPrincipal;
import com.jme3.app.SimpleApplication;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * Classe Principal
 * @author atybmx
 */
public class Main extends BubbleGame {
    

    public static void main(String[] args) {
        Main app = new Main();
        AppSettings settings = new AppSettings(true);
        settings.setResolution(800,600);
        settings.setTitle("Veloz e Animal");
        settings.setSettingsDialogImage("Textures/logo.png");
        app.setSettings(settings);
        app.start();
        
    }

    @Override
    public void simpleInitApp() {
        super.simpleInitApp();
        
        setDisplayFps(false);
        setDisplayStatView(false);
        
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager,
                                                          inputManager,
                                                          audioRenderer,
                                                          guiViewPort);
        Nifty nifty = niftyDisplay.getNifty();
        //nifty.setDebugOptionPanelColors(true);
        nifty.fromXml("Interface/menuPrincipal.xml", "start", new Introducao(niftyDisplay,this));

        // attach the nifty display to the gui view port as a processor
        guiViewPort.addProcessor(niftyDisplay);
        
         inputManager.setCursorVisible(true);
         flyCam.setDragToRotate(true);
    }

    @Override
    public void simpleUpdate(float tpf) {
        super.simpleUpdate(tpf);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    
}
