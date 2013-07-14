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
public class Abacaxi extends Item{
    
    /**
     * Forca aplicada no veiculo no sentido do eixo Y, para fazer o veiculo saltar
     */
    private Vector3f forcaPulo = new Vector3f(0, 3000, 0);
    
    public Abacaxi(){
        super();
        
        //Carrega a imagem HUD do abacaxi
        imagemHUD += "abacaxiHUD.png";
    }

    @Override
    public void utilizarPoder(Veiculo veiculo,boolean tocarSom) {
        veiculo.applyImpulse(forcaPulo, Vector3f.ZERO);
        
        if(tocarSom){
            AudioNode pegarItem = new AudioNode(veiculo.getTheGame().getBubbleGame().getAssetManager(), "Sounds/efeitosSonoros/abacaxi.ogg");
            pegarItem.setVolume(100);
            pegarItem.play();
        }
    }
}
