/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.bubble.velozeanimal.itens;

import br.com.bubble.velozeanimal.itens.controle.MelanciaControl;
import br.com.bubble.velozeanimal.Veiculo;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;

/**
 *
 * @author atybmx
 */
public class Melancia extends Item{
    
    private SphereCollisionShape melanciaCollisionShape;
    
    /**
     * Valor referente a velocidade LINEAR que a melancia vai ter quando adicionada na cena
     */
    private final float VELOCIDADE_LINEAR = 100f;
    
    public Melancia(){
        super();
        
        //Carrega a imagem HUD da melancia
        imagemHUD += "melanciaHUD.png";
        
        //Cria o collision shape da melancia
        melanciaCollisionShape = new SphereCollisionShape(1f);
    }

    @Override
    public void utilizarPoder(Veiculo veiculo,boolean tocarSom) {
        Node melanciaGeometria = (Node)veiculo.getTheGame().getBubbleGame().getAssetManager().loadModel("Models/Itens/Melancia/melancia.j3o");
        melanciaGeometria.setName("melancia");
        melanciaGeometria.setLocalTranslation(veiculo.getPhysicsLocation().add(veiculo.getDirecao().mult(40).add(0,1.7f,0)));
        
        MelanciaControl melanciaControl = new MelanciaControl(melanciaCollisionShape, 1,melanciaGeometria,veiculo.getDirecao());
        melanciaControl.setCcdMotionThreshold(0.1f);
        
        melanciaGeometria.addControl(melanciaControl);
        
        veiculo.getTheGame().getBubbleGame().getRootNode().attachChild(melanciaGeometria);
        veiculo.getTheGame().getBubbleGame().getPhysicsSpace().add(melanciaControl);
        veiculo.getTheGame().getBubbleGame().getPhysicsSpace().addCollisionListener(melanciaControl);
        
        
        if(tocarSom){
            AudioNode pegarItem = new AudioNode(veiculo.getTheGame().getBubbleGame().getAssetManager(), "Sounds/efeitosSonoros/atirarMelancia.ogg");
            pegarItem.play();
        }
    }
    
}
