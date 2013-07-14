package br.com.bubble.velozeanimal;

import com.jme3.audio.AudioNode;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;


/**
 *
 * @author atybmx
 */
public class Jogador implements ActionListener, PhysicsCollisionListener{
    
    private Veiculo veiculo;
    private Node veiculoNode;
    
    public Jogador(Veiculo veiculo){
        this.veiculo = veiculo;
        this.veiculoNode = veiculo.getVeiculoNode();
        
        configuraVeiculoParaJogador();
    }
    
    public void configuraVeiculoParaJogador(){
        veiculo.setForcaDirecao(Veiculo.FORCA_DIRECAO_JOGADOR);
    }
    
    public Veiculo getVeiculo(){
        return veiculo;
    }
    
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("Esquerda")) {
            if (isPressed) {
                veiculo.virarEsquerda();
            } else {
                veiculo.centralizarDirecao();
            }
            
        } else if (name.equals("Direita")) {
            if (isPressed) {
                veiculo.virarDireita();
            } else {
                veiculo.centralizarDirecao();
            }
        
        } else if (name.equals("Acelerar")) {
            if (isPressed) {
                veiculo.acelerar();
            } else {
                veiculo.desacelerar();
            }
        
        } else if (name.equals("Frear")) {
            if (isPressed) {
                veiculo.pisarFreio();
            } else {
                veiculo.soltarFreio();
            }
            
        } else if (name.equals("Item1")) {
            if (isPressed) {
                veiculo.utilizarItemBolsa(0,true);
            }
            
        } else if (name.equals("Item2")) {
            if (isPressed) {
                veiculo.utilizarItemBolsa(1,true);
            }
            
        }else if (name.equals("Item3")) {
            if (isPressed) {
                veiculo.utilizarItemBolsa(2,true);
            }
            
        }else if (name.equals("Reset")) {
            if (isPressed) {
                //setPhysicsLocation(new Vector3f(10, 3, 0));
                //setPhysicsRotation(new Matrix3f());
                //setLinearVelocity(Vector3f.ZERO);
                //setAngularVelocity(Vector3f.ZERO);
                //resetSuspension();
               // System.out.println(veiculo.getVeiculoNode().getLocalTranslation());
                //Vector3f posicao = veiculo.getPhysicsLocation();
                //System.out.println("mapaControlePosicoes.add(new Vector3f("+posicao.getX()+"f,"+posicao.getY()+"f,"+posicao.getZ()+"f"+"));");
                //System.out.println(veiculo.getDirecao());
                
                //System.out.println(veiculo.getDirecao());
            }
        }
    }

    public void collision(PhysicsCollisionEvent colisao) {
        /**
         * Verifica se o jogador pegou um item
         */
        if((colisao.getNodeA().getName().equals("item") || colisao.getNodeB().getName().equals("item")) 
                &&
           (colisao.getNodeA() == veiculoNode || colisao.getNodeB() == veiculoNode)){
            AudioNode pegarItem = new AudioNode(veiculo.getTheGame().getBubbleGame().getAssetManager(), "Sounds/efeitosSonoros/pegarItem.ogg");
            pegarItem.play();
        }
    }
}
