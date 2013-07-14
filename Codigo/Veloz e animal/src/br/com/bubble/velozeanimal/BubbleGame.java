package br.com.bubble.velozeanimal;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;

/**
 *
 * @author atybmx
 */
public class BubbleGame extends SimpleApplication{
    private BulletAppState espacoFisicaAppState;
    private boolean fisica = true;

    @Override
    public void simpleInitApp() {
        espacoFisicaAppState = new BulletAppState();
        stateManager.attach(espacoFisicaAppState);
        //espacoFisicaAppState.getPhysicsSpace().enableDebug(assetManager);
    }
    
    public void habilitaFisica(boolean habilitar){
        if(habilitar){
            if(!stateManager.hasState(espacoFisicaAppState))
                stateManager.attach(espacoFisicaAppState);
        }else{
            if(stateManager.hasState(espacoFisicaAppState))
                stateManager.detach(espacoFisicaAppState);
        }
            
        fisica = habilitar;
    }
    
    public PhysicsSpace getPhysicsSpace() {
        return espacoFisicaAppState.getPhysicsSpace();
    }
    
    public boolean hasFisica(){
        return fisica;
    }
}
