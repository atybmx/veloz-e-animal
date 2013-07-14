/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.bubble.velozeanimal.itens;

import br.com.bubble.velozeanimal.Veiculo;
import br.com.bubble.velozeanimal.itens.controle.TomateControl;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author atybmx
 */
public class Tomate extends Item{
    
     public Tomate(){
        super();
        
        //Carrega a imagem HUD do tomate
        imagemHUD += "tomateHUD.png";
    }

    @Override
    public void utilizarPoder(Veiculo veiculo,boolean tocarSom) {
        Node tomateGeometria = (Node)veiculo.getTheGame().getBubbleGame().getAssetManager().loadModel("Models/Itens/Tomate/tomate.j3o");
        tomateGeometria.setName("tomate");
        tomateGeometria.setLocalTranslation(veiculo.getPhysicsLocation().add(veiculo.getDirecao().mult(40).add(0,1.2f,0)));
        
        Vector3f collisionShape = new Vector3f(.6f,.6f,.6f);
        TomateControl tomateControle = new TomateControl(new BoxCollisionShape(collisionShape),1f,tomateGeometria,veiculo.getDirecao());
        tomateGeometria.addControl(tomateControle);
        
        
        veiculo.getTheGame().getBubbleGame().getRootNode().attachChild(tomateGeometria);
        veiculo.getTheGame().getBubbleGame().getPhysicsSpace().add(tomateControle);
        veiculo.getTheGame().getBubbleGame().getPhysicsSpace().addCollisionListener(tomateControle);
        
        if(tocarSom){
            AudioNode pegarItem = new AudioNode(veiculo.getTheGame().getBubbleGame().getAssetManager(), "Sounds/efeitosSonoros/atirarTomate.ogg");
            pegarItem.play();
        }
    }
}
