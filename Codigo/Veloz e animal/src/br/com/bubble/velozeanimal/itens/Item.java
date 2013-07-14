package br.com.bubble.velozeanimal.itens;

import br.com.bubble.velozeanimal.Veiculo;
import com.jme3.scene.Node;

/**
 *
 * @author atybmx
 */
public abstract class Item extends Node{
    
    public static final int QUANTIDADE_ITENS = 6;
    
    protected String imagemHUD = "Textures/HUD/";
    
    /**
     * Nao deixa criar instancia
     * Para instanciar somente pelo metodo novoItem
     */
    public Item(){
        super("item");
    }

    /**
     * O valor é carregado com o caminho do arquivo e o nome nas respectivas sobrecargas do Item
     * @return O caminho e o nome do arquivo do HUD do respectivo Item
     */
    public String getImagemHUD() {
        return imagemHUD;
    }
    
    /**
     * Metodo que sera sobrecarregado nas classes de itens
     * que chamara o respectivo metodo da classe veiculo que possui a instancia do item
     */
    public abstract void utilizarPoder(Veiculo veiculo,boolean tocarSom);
    
}
