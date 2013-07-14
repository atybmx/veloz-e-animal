/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.bubble.velozeanimal.itens;

import br.com.bubble.velozeanimal.Veiculo;
import com.jme3.audio.AudioNode;
import com.jme3.ui.Picture;

/**
 *
 * @author atybmx
 */
public class Sorvete extends Item{
    
    public Sorvete(){
        super();
        
        //Carrega a imagem HUD do sorvete
        imagemHUD += "sorveteHUD.png";
    }

    @Override
    public void utilizarPoder(Veiculo veiculo,boolean tocarSom) {
        Veiculo veiculoAFrente = veiculo.getTheGame().getVeiculoAFrente(veiculo);
        veiculoAFrente.congelar();
        
        if(tocarSom){
            AudioNode pegarItem = new AudioNode(veiculo.getTheGame().getBubbleGame().getAssetManager(), "Sounds/efeitosSonoros/sorvete.ogg");
            pegarItem.play();
        }
    }
}
