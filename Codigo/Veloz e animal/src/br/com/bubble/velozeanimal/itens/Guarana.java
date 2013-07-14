/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.bubble.velozeanimal.itens;

import br.com.bubble.velozeanimal.Veiculo;
import com.jme3.audio.AudioNode;
import com.jme3.math.Vector3f;

/**
 *
 * @author atybmx
 */
public class Guarana extends Item{
    
    /**
     * Forca aplicada no carro, para fazer o carro dar um impulso para a frente
     */
    private static final float valorForcaGuarana = 20000;
    private Vector3f forcaGuarana = new Vector3f(valorForcaGuarana, 0, valorForcaGuarana);
    private Vector3f forcaEmpinar = new Vector3f(0,600,0);
    
    public Guarana(){
        super();
        
        //Carrega a imagem HUD do guarana
        imagemHUD += "guaranaHUD.png";
    }

    @Override
    public void utilizarPoder(Veiculo veiculo, boolean tocarSom) {
        forcaGuarana.set(valorForcaGuarana, 0, valorForcaGuarana);
        forcaGuarana.multLocal(veiculo.getDirecao());
        veiculo.applyImpulse(forcaGuarana, Vector3f.ZERO);
        veiculo.applyImpulse(forcaEmpinar, veiculo.getDirecao().multLocal(2, 0, 2));
        
        if(tocarSom){
            AudioNode pegarItem = new AudioNode(veiculo.getTheGame().getBubbleGame().getAssetManager(), "Sounds/efeitosSonoros/guarana.ogg");
            pegarItem.play();
        }
    }
}
