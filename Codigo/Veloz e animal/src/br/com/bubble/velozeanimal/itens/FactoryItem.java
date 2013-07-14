/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.bubble.velozeanimal.itens;

import java.util.Random;

/**
 *
 * @author atybmx
 */
public class FactoryItem {
    
    public static final int BANANA = 0;
    public static final int MELANCIA = 1;
    public static final int SORVETE = 2;
    public static final int ABACAXI = 3;
    public static final int TOMATE = 4;
    public static final int GUARANA = 5;
    
    
    /**
     * Adiciona um novo item randomicamente ao game
     * @return Um novo item aleatorio
     */
    public static Item novoItem(){
        Random numeroRandomico = new Random();
        int codigo = numeroRandomico.nextInt(Item.QUANTIDADE_ITENS);
        return novoItem(codigo);
    }
    
    /**
     * 
     * @param codigo O codigo do novo item a ser adicionado
     * @return O novo item informado pelo @param codigo
     */
    public static Item novoItem(int codigo){
        if(codigo < 0 || codigo >= Item.QUANTIDADE_ITENS){
            return novoItem();
        }
        
        switch(codigo){
            case BANANA :
                return new Banana();
            case MELANCIA :
                return new Melancia();
            case SORVETE :
                return new Sorvete();
            case ABACAXI :
                return new Abacaxi();
            case TOMATE :
                return new Tomate();
            case GUARANA :
                return new Guarana();
        }
        
        return null;
    }
}
