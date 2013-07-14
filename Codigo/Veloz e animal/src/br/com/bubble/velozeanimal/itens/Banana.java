/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.bubble.velozeanimal.itens;

import br.com.bubble.velozeanimal.Veiculo;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author atybmx
 */
public class Banana extends Item{
    
    public Banana(){
        super();
        
        //Carrega a imagem HUD da banana
        imagemHUD += "bananaHUD.png";
    }

    @Override
    public void utilizarPoder(Veiculo veiculo,boolean tocarSom) {
        Node cascaBananaGeometria = (Node)veiculo.getTheGame().getBubbleGame().getAssetManager().loadModel("Models/Itens/Banana/cascaBanana.j3o");
        cascaBananaGeometria.setName("cascaBanana");
        cascaBananaGeometria.setLocalTranslation(veiculo.getPhysicsLocation().add(veiculo.getDirecao().mult(-5)));
        
        Vector3f collisionShape = new Vector3f(1.0000001f,1.0f,0.9510567f);
        GhostControl cascaBananaControle = new GhostControl(new BoxCollisionShape(collisionShape));
        cascaBananaGeometria.addControl(cascaBananaControle);
        
        
        veiculo.getTheGame().getBubbleGame().getRootNode().attachChild(cascaBananaGeometria);
        veiculo.getTheGame().getBubbleGame().getPhysicsSpace().add(cascaBananaControle);
        
        if(tocarSom){
            AudioNode pegarItem = new AudioNode(veiculo.getTheGame().getBubbleGame().getAssetManager(), "Sounds/efeitosSonoros/atirarBanana.ogg");
            pegarItem.play();
        }
    }
    
}
