/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.bubble.velozeanimal.itens.controle;

import br.com.bubble.velozeanimal.Veiculo;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author atybmx
 */
public class MelanciaControl extends RigidBodyControl implements PhysicsCollisionListener{
    
    /**
     * Valor referente a velocidade LINEAR que a melancia vai ter quando adicionada na cena
     */
    private final float VELOCIDADE_LINEAR = 100f;
    
    private Vector3f direcao = new Vector3f();
    private Spatial melanciaNode;
    
    /**
     * 
     * @param shape
     * @param mass
     * @param melanciaNode O Node que o control está controlando
     * @param direcao A direção que a melancia vai seguir
     */
    public MelanciaControl(CollisionShape shape, float mass,Spatial melanciaNode,Vector3f direcao) {
        super(shape, mass);
        
        this.melanciaNode = melanciaNode;
        this.direcao = direcao;
    }

    
    
    @Override
    public void update(float tpf) {
        super.update(tpf);
        
        setLinearVelocity(direcao.mult(VELOCIDADE_LINEAR).add(0,1.7f,0));
    }

    public void collision(PhysicsCollisionEvent colisao) {
        /**
         * Verifica se o veiculo foi atingido por uma melancia
         */
        if((colisao.getNodeA().getName().equals("veiculo") || colisao.getNodeB().getName().equals("veiculo")) 
                &&
           (colisao.getNodeA() == melanciaNode || colisao.getNodeB() == melanciaNode)){
                Veiculo veiculo;
                
                if(colisao.getObjectA() != this)
                    veiculo = (Veiculo) colisao.getObjectA();
                else
                    veiculo = (Veiculo) colisao.getObjectB();
                
                
                veiculo.rodar();
                veiculo.parar();
                
                
        }
        
        /**
         * Se a melancia colidir com algo ela é removida da cena
         */
        if(colisao.getNodeA() == melanciaNode || colisao.getNodeB() == melanciaNode){
            setEnabled(false);
            destroy();
            melanciaNode.removeFromParent();
        }
    }
    
}
