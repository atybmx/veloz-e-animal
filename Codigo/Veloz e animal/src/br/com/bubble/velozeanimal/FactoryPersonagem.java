package br.com.bubble.velozeanimal;

import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author atybmx
 */
public class FactoryPersonagem {
    
    private static final float VALOR_ATRITO = 4f;

    public static NPC constroiNPC(Veiculo veiculo,BubbleGame game){
        NPC npc = new NPC(veiculo,game);
        
        veiculo.getVeiculoNode().addControl(npc);
        game.getPhysicsSpace().addCollisionListener(npc);
        
        return npc;
    }
    
    public static Veiculo vino(Corrida corrida,BubbleGame game,boolean jogador){
        /**
         * Massa do veiculo
         */
        float massa = 500;
        
        //Load model and get chassis Geometry
        Node veiculoNode = (Node)game.getAssetManager().loadModel("Models/Personagens/Vino/carro_vino.j3o");
        veiculoNode.setName("veiculo");
        
        //Cria um collisionShape baseado no chassi
        CollisionShape veiculoShapeModel = CollisionShapeFactory.createDynamicMeshShape(veiculoNode.getChild("chassi"));
        
        CompoundCollisionShape compoundCollisionShape=new CompoundCollisionShape();
        BoxCollisionShape boxCollisionShape=new BoxCollisionShape(new Vector3f(2.3f,1f,2.3f));
        compoundCollisionShape.addChildShape(boxCollisionShape, new Vector3f(0,.9f,0));
        
        //Cria uma instancia de Veiculo
        Veiculo veiculo = new Veiculo(compoundCollisionShape, massa,corrida);
        veiculo.setNome("vino");
        if(jogador){
            //ACERTANDO O ATRITO
            veiculo.setFrictionSlip(VALOR_ATRITO);
        
            veiculo.setHudPosicao("Textures/HUD/vinoPosicaoJogadorHUD.png");
        }else
            veiculo.setHudPosicao("Textures/HUD/vinoPosicaoHUD.png");
        
        veiculoNode.addControl(veiculo);
        veiculo.setVeiculoNode(veiculoNode);
        
        /**
         * Configurando a suspens√£o para as rodas do veiculo.
         * Esses valores podem ser configurados separadamente em cada roda
         */
        float rigidez = 200f;//Quanto maior, mais o caro fica estavel de duro
        float compressaoValue = .2f; //(Deve ser menor que o amortecimento)
        float amortecimentoValue = .3f;
        veiculo.setSuspensionCompression(compressaoValue * 2.0f * FastMath.sqrt(rigidez));
        veiculo.setSuspensionDamping(amortecimentoValue * 2.0f * FastMath.sqrt(rigidez));
        veiculo.setSuspensionStiffness(rigidez);
        veiculo.setMaxSuspensionForce(10000.0f);

        /**
         * Criando quatro rodas
         * Duas dianteiras e duas traseiras
         */
        Vector3f direcaoRoda = new Vector3f(0, -1, 0);
        Vector3f eixoRoda = new Vector3f(-1, 0, 0);
        float raio = 0.8f;
        float tamanhoSuspensao = 0.3f;

        /**
         * Roda Dianteira Esquerda do carro
         */
        Spatial wheelFrontLeft = veiculoNode.getChild("WheelFrontLeft");
        veiculo.addWheel(wheelFrontLeft,wheelFrontLeft.getLocalTranslation(),
                direcaoRoda, eixoRoda, tamanhoSuspensao, raio, true);

        /**
         * Roda Dianteira Direita do carro
         */
        Spatial wheelFrontRight = (Node) veiculoNode.getChild("WheelFrontRight");
        veiculo.addWheel(wheelFrontRight,wheelFrontRight.getLocalTranslation(),
                direcaoRoda, eixoRoda, tamanhoSuspensao, raio, true);

        /**
         * Roda Traseira Esquerda do carro
         */
        Spatial wheelBackLeft = (Node) veiculoNode.getChild("WheelBackLeft");
        veiculo.addWheel(wheelBackLeft,wheelBackLeft.getLocalTranslation(),
                direcaoRoda, eixoRoda, tamanhoSuspensao, raio, false);

        /**
         * Roda Traseira Direita do carro
         */
        Spatial wheelBackRight = (Node) veiculoNode.getChild("WheelBackRight");
        veiculo.addWheel(wheelBackRight,wheelBackRight.getLocalTranslation(),
                direcaoRoda, eixoRoda, tamanhoSuspensao, raio, false);
        
        /**
         * Carregando o personagem
         */
        Node personagemNode = (Node)game.getAssetManager().loadModel("Models/Personagens/Vino/vinoGAME_BETTER.j3o");
        veiculo.setPersonagem(personagemNode);
        
        game.getRootNode().attachChild(veiculoNode);
        game.getPhysicsSpace().add(veiculo);
        game.getPhysicsSpace().addCollisionListener(veiculo);
        
        veiculo.setGravity(new Vector3f(0f,-30f,0f));
        
        return veiculo;
    }
    
    public static Veiculo liminha(Corrida corrida,BubbleGame game,boolean jogador){
        /**
         * Massa do veiculo
         */
        float massa = 500;
        
        //Load model and get chassis Geometry
        Node veiculoNode = (Node)game.getAssetManager().loadModel("Models/Personagens/Liminha/carro_liminha.j3o");
        veiculoNode.setName("veiculo");
        //Cria um collisionShape baseado no chassi
        CollisionShape veiculoShapeModel = CollisionShapeFactory.createDynamicMeshShape(veiculoNode.getChild("chassi"));
        
        CompoundCollisionShape compoundCollisionShape=new CompoundCollisionShape();
        BoxCollisionShape boxCollisionShape=new BoxCollisionShape(new Vector3f(1.5f,1f,2.3f));
        compoundCollisionShape.addChildShape(boxCollisionShape, new Vector3f(0,1.2f,0));
        //Cria uma instancia de Veiculo
        Veiculo veiculo = new Veiculo(compoundCollisionShape, massa,corrida);
        veiculo.setNome("liminha");
        if(jogador){
            //ACERTANDO O ATRITO
            veiculo.setFrictionSlip(VALOR_ATRITO);
            
            veiculo.setHudPosicao("Textures/HUD/liminhaPosicaoJogadorHUD.png");
        }else
            veiculo.setHudPosicao("Textures/HUD/liminhaPosicaoHUD.png");
        
        veiculoNode.addControl(veiculo);
        veiculo.setVeiculoNode(veiculoNode);
        
        /**
         * Configurando a suspens√£o para as rodas do veiculo.
         * Esses valores podem ser configurados separadamente em cada roda
         */
        float rigidez = 200f;//Quanto maior, mais o caro fica estavel de duro
        float compressaoValue = .2f; //(Deve ser menor que o amortecimento)
        float amortecimentoValue = .3f;
        veiculo.setSuspensionCompression(compressaoValue * 2.0f * FastMath.sqrt(rigidez));
        veiculo.setSuspensionDamping(amortecimentoValue * 2.0f * FastMath.sqrt(rigidez));
        veiculo.setSuspensionStiffness(rigidez);
        veiculo.setMaxSuspensionForce(10000.0f);

        /**
         * Criando quatro rodas
         * Duas dianteiras e duas traseiras
         */
        Vector3f direcaoRoda = new Vector3f(0, -1, 0);
        Vector3f eixoRoda = new Vector3f(-1, 0, 0);
        float raio = 0.5f;
        float tamanhoSuspensao = 0.3f;

        /**
         * Roda Dianteira Esquerda do carro
         */
        Spatial wheelFrontLeft = veiculoNode.getChild("WheelFrontLeft");
        veiculo.addWheel(wheelFrontLeft,wheelFrontLeft.getLocalTranslation(),
                direcaoRoda, eixoRoda, tamanhoSuspensao, raio, true);

        /**
         * Roda Dianteira Direita do carro
         */
        Spatial wheelFrontRight = (Node) veiculoNode.getChild("WheelFrontRight");
        veiculo.addWheel(wheelFrontRight,wheelFrontRight.getLocalTranslation(),
                direcaoRoda, eixoRoda, tamanhoSuspensao, raio, true);

        /**
         * Roda Traseira Esquerda do carro
         */
        Spatial wheelBackLeft = (Node) veiculoNode.getChild("WheelBackLeft");
        veiculo.addWheel(wheelBackLeft,wheelBackLeft.getLocalTranslation(),
                direcaoRoda, eixoRoda, tamanhoSuspensao, raio, false);

        /**
         * Roda Traseira Direita do carro
         */
        Spatial wheelBackRight = (Node) veiculoNode.getChild("WheelBackRight");
        veiculo.addWheel(wheelBackRight,wheelBackRight.getLocalTranslation(),
                direcaoRoda, eixoRoda, tamanhoSuspensao, raio, false);
        
        
        /**
         * Carregando o personagem
         */
        Node personagemNode = (Node)game.getAssetManager().loadModel("Models/Personagens/Liminha/liminhaGAME_BETTER.j3o");
        veiculo.setPersonagem(personagemNode);
        
        game.getRootNode().attachChild(veiculoNode);
        game.getPhysicsSpace().add(veiculo);
        game.getPhysicsSpace().addCollisionListener(veiculo);
        
        veiculo.setGravity(new Vector3f(0f,-30f,0f));
        
        return veiculo;
    }
    
    public static Veiculo zapata(Corrida corrida,BubbleGame game,boolean jogador){
        /**
         * Massa do veiculo
         */
        float massa = 500;
        
        //Load model and get chassis Geometry
        Node veiculoNode = (Node)game.getAssetManager().loadModel("Models/Personagens/Zapata/carro_zapata.j3o");
        veiculoNode.setName("veiculo");
        //Cria um collisionShape baseado no chassi
        CollisionShape veiculoShapeModel = CollisionShapeFactory.createDynamicMeshShape(veiculoNode.getChild("chassi"));
        
        CompoundCollisionShape compoundCollisionShape=new CompoundCollisionShape();
        BoxCollisionShape boxCollisionShape=new BoxCollisionShape(new Vector3f(1.5f,1f,2.3f));
        compoundCollisionShape.addChildShape(boxCollisionShape, new Vector3f(0,1,0));
        
        //Cria uma instancia de Veiculo
        Veiculo veiculo = new Veiculo(compoundCollisionShape, massa,corrida);
        veiculo.setNome("zapata");
        if(jogador){
            //ACERTANDO O ATRITO
            veiculo.setFrictionSlip(VALOR_ATRITO);
            
            veiculo.setHudPosicao("Textures/HUD/zapataPosicaoJogadorHUD.png");
        }else
            veiculo.setHudPosicao("Textures/HUD/zapataPosicaoHUD.png");
        
        veiculoNode.addControl(veiculo);
        veiculo.setVeiculoNode(veiculoNode);
        
        /**
         * Configurando a suspens√£o para as rodas do veiculo.
         * Esses valores podem ser configurados separadamente em cada roda
         */
        float rigidez = 200f;//Quanto maior, mais o caro fica estavel de duro
        float compressaoValue = .2f; //(Deve ser menor que o amortecimento)
        float amortecimentoValue = .3f;
        veiculo.setSuspensionCompression(compressaoValue * 2.0f * FastMath.sqrt(rigidez));
        veiculo.setSuspensionDamping(amortecimentoValue * 2.0f * FastMath.sqrt(rigidez));
        veiculo.setSuspensionStiffness(rigidez);
        veiculo.setMaxSuspensionForce(10000.0f);

        /**
         * Criando quatro rodas
         * Duas dianteiras e duas traseiras
         */
        Vector3f direcaoRoda = new Vector3f(0, -1, 0);
        Vector3f eixoRoda = new Vector3f(-1, 0, 0);
        float raio = 0.45f;
        float tamanhoSuspensao = 0.3f;

        /**
         * Roda Dianteira Esquerda do carro
         */
        Spatial wheelFrontLeft = veiculoNode.getChild("WheelFrontLeft");
        veiculo.addWheel(wheelFrontLeft,wheelFrontLeft.getLocalTranslation(),
                direcaoRoda, eixoRoda, tamanhoSuspensao, raio, true);

        /**
         * Roda Dianteira Direita do carro
         */
        Spatial wheelFrontRight = (Node) veiculoNode.getChild("WheelFrontRight");
        veiculo.addWheel(wheelFrontRight,wheelFrontRight.getLocalTranslation(),
                direcaoRoda, eixoRoda, tamanhoSuspensao, raio, true);

        /**
         * Roda Traseira Esquerda do carro
         */
        Spatial wheelBackLeft = (Node) veiculoNode.getChild("WheelBackLeft");
        veiculo.addWheel(wheelBackLeft,wheelBackLeft.getLocalTranslation(),
                direcaoRoda, eixoRoda, tamanhoSuspensao, raio, false);

        /**
         * Roda Traseira Direita do carro
         */
        Spatial wheelBackRight = (Node) veiculoNode.getChild("WheelBackRight");
        veiculo.addWheel(wheelBackRight,wheelBackRight.getLocalTranslation(),
                direcaoRoda, eixoRoda, tamanhoSuspensao, raio, false);
        
        
        /**
         * Carregando o personagem
         */
        Node personagemNode = (Node)game.getAssetManager().loadModel("Models/Personagens/Zapata/zapataGAME_BETTER.j3o");
        veiculo.setPersonagem(personagemNode);
        
        game.getRootNode().attachChild(veiculoNode);
        game.getPhysicsSpace().add(veiculo);
        game.getPhysicsSpace().addCollisionListener(veiculo);
        
        veiculo.setGravity(new Vector3f(0f,-30f,0f));
        
        return veiculo;
    }
    
    public static Veiculo ademir(Corrida corrida,BubbleGame game,boolean jogador){
        /**
         * Massa do veiculo
         */
        float massa = 500;
        
        //Load model and get chassis Geometry
        Node veiculoNode = (Node)game.getAssetManager().loadModel("Models/Personagens/Ademir/carro_ademir.j3o");
        veiculoNode.setName("veiculo");
        //Cria um collisionShape baseado no chassi
        CollisionShape veiculoShapeModel = CollisionShapeFactory.createDynamicMeshShape(veiculoNode.getChild("chassi"));
        
        CompoundCollisionShape compoundCollisionShape=new CompoundCollisionShape();
        BoxCollisionShape boxCollisionShape=new BoxCollisionShape(new Vector3f(1.5f,1f,2.3f));
        compoundCollisionShape.addChildShape(boxCollisionShape, new Vector3f(0,1,0));
        
        
        //Cria uma instancia de Veiculo
        Veiculo veiculo = new Veiculo(compoundCollisionShape, massa,corrida);
        veiculo.setNome("ademir");
        if(jogador){
            //ACERTANDO O ATRITO
            veiculo.setFrictionSlip(VALOR_ATRITO);
            
            veiculo.setHudPosicao("Textures/HUD/ademirPosicaoJogadorHUD.png");
        }else
            veiculo.setHudPosicao("Textures/HUD/ademirPosicaoHUD.png");
        
        veiculoNode.addControl(veiculo);
        veiculo.setVeiculoNode(veiculoNode);
        
        /**
         * Configurando a suspens√£o para as rodas do veiculo.
         * Esses valores podem ser configurados separadamente em cada roda
         */
        float rigidez = 200f;//Quanto maior, mais o caro fica estavel de duro
        float compressaoValue = .2f; //(Deve ser menor que o amortecimento)
        float amortecimentoValue = .3f;
        veiculo.setSuspensionCompression(compressaoValue * 2.0f * FastMath.sqrt(rigidez));
        veiculo.setSuspensionDamping(amortecimentoValue * 2.0f * FastMath.sqrt(rigidez));
        veiculo.setSuspensionStiffness(rigidez);
        veiculo.setMaxSuspensionForce(10000.0f);

        /**
         * Criando quatro rodas
         * Duas dianteiras e duas traseiras
         */
        Vector3f direcaoRoda = new Vector3f(0, -1, 0);
        Vector3f eixoRoda = new Vector3f(-1, 0, 0);
        float raio = 0.5f;
        float tamanhoSuspensao = 0.3f;

        /**
         * Roda Dianteira Esquerda do carro
         */
        Spatial wheelFrontLeft = veiculoNode.getChild("WheelFrontLeft");
        veiculo.addWheel(wheelFrontLeft,wheelFrontLeft.getLocalTranslation(),
                direcaoRoda, eixoRoda, tamanhoSuspensao, raio, true);

        /**
         * Roda Dianteira Direita do carro
         */
        Spatial wheelFrontRight = (Node) veiculoNode.getChild("WheelFrontRight");
        veiculo.addWheel(wheelFrontRight,wheelFrontRight.getLocalTranslation(),
                direcaoRoda, eixoRoda, tamanhoSuspensao, raio, true);

        /**
         * Roda Traseira Esquerda do carro
         */
        Spatial wheelBackLeft = (Node) veiculoNode.getChild("WheelBackLeft");
        veiculo.addWheel(wheelBackLeft,wheelBackLeft.getLocalTranslation(),
                direcaoRoda, eixoRoda, tamanhoSuspensao, raio, false);

        /**
         * Roda Traseira Direita do carro
         */
        Spatial wheelBackRight = (Node) veiculoNode.getChild("WheelBackRight");
        veiculo.addWheel(wheelBackRight,wheelBackRight.getLocalTranslation(),
                direcaoRoda, eixoRoda, tamanhoSuspensao, raio, false);
        
        
        /**
         * Carregando o personagem
         */
        Node personagemNode = (Node)game.getAssetManager().loadModel("Models/Personagens/Ademir/ademir_GAME_BETTER.j3o");
        veiculo.setPersonagem(personagemNode);
        
        game.getRootNode().attachChild(veiculoNode);
        game.getPhysicsSpace().add(veiculo);
        game.getPhysicsSpace().addCollisionListener(veiculo);
        
        veiculo.setGravity(new Vector3f(0f,-30f,0f));
        
        return veiculo;
    }
    
}
