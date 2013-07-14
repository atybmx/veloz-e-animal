package br.com.bubble.velozeanimal;

import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

/**
 *
 * @author atybmx
 */
public class NPC extends AbstractControl implements PhysicsCollisionListener{
    
    private Veiculo veiculo;
    private BubbleGame game;
    
    /**
     * Variaveis estaticas que armazenam valores configuraveis ao NPC
     */
    private static final int PRECISAO_RETA = 5; //Indica qto o veiculo precisa estar alinhado ao ponto para considerar estar em linha reta
    private static final int PRECISAO_OBJETO_EM_FRENTE = 6;//Indica qto o veiculo precisa estar a algum ao objeto para considerar que esta em sua frente
    
    //Vetor que armazena a posicao relativa entre o NPC e o ponto que esta seguindo
    private Vector3f relacaoMeP1 = new Vector3f();
    
    //Vetor que armazena a posicao relativa entre o NPC e o item que esta tentando capturar
    private Vector3f relacaoMeItem = new Vector3f();
    
    //Vetor que armazena a posicao de algum item que o NPC viu e pretende pegar
    private Vector3f posicaoBuscarItem = new Vector3f();
    
    //Vetor que armazena a posicao relativa entre o NPC e o item que esta tentando capturar
    private Vector3f relacaoMeObjeto = new Vector3f();
    
    //Vetor que armazena a posicao de algum item que o NPC viu e pretende pegar
    private Vector3f posicaoObjeto = new Vector3f();
    
    /**
     * Um colisor fantasma que representa a visao do NPC
     */
    private GhostControl visao;
    private Node nodeVisao;
    
    /**
     * Os estados que o NPC pode estar durante a corrida
     */
    private static final int ESTADO_SEGUIR_CAMINHO = 0;//O NPC segue os pontos com objetivo de finalizar a corrida
    private static final int ESTADO_DESVIAR_OBJETO = 1;//O NPC tenta de qualquer maneira desviar de algum objeto em sua frente
    private static final int ESTADO_BUSCAR_ITEM = 2;//O NPC tenta obter algum item no percurso
    
    /**
     * O estado inicia-se em seguir o caminho
     */
    private int estado = ESTADO_SEGUIR_CAMINHO;
    
    private int qtdeAdequadaItensNaBolsa = 1;
            
    /**
     * 
     * @param veiculo 
     */    
    public NPC(Veiculo veiculo, BubbleGame game){
        this.veiculo = veiculo;
        this.game = game;
        
        constroiVisao();
        configuraVeiculoParaNPC();
    }
    
    public void configuraVeiculoParaNPC(){
        veiculo.setForcaDirecao(Veiculo.FORCA_DIRECAO_NPC);
    }

    @Override
    protected void controlUpdate(float tpf) {
        /**
         * DURANTE O CICLO DE ATUALIZACAO DO NPC,
         * É SEMPRE VERIFICADO O ESTADO ATUAL DELE
         */
        switch(estado){
            case ESTADO_SEGUIR_CAMINHO:
                sigaSeuCaminho();
                break;
            case ESTADO_DESVIAR_OBJETO:
                desviarObjeto();
                break;
            case ESTADO_BUSCAR_ITEM:
                buscarItem();
                break;
        }
    }

    private void sigaSeuCaminho() {
        //Obtem a relacao da posicao do veiculo com o ponto
        veiculo.getVeiculoNode().worldToLocal(veiculo.getStatusCorrida().getPontoAtual(), relacaoMeP1);
        
        if(veiculo.getCurrentVehicleSpeedKmHour() <= 0)
           veiculo.acelerar();
        
        //Sempre alinhar o veiculo ao ponto
        if(relacaoMeP1.getX() > NPC.PRECISAO_RETA || relacaoMeP1.getX() < -NPC.PRECISAO_RETA){
            //Se o ponto esta a sua esquerda, entao vira a esquerda, senao vira a direita	
            if(relacaoMeP1.getX() > 0){
        	veiculo.virarEsquerda();
            }else{
        	veiculo.virarDireita();
            }
                
            //veiculo.desacelerar();
        }else{
            veiculo.centralizarDirecao();
        }
    }
    
    private void desviarObjeto() {
        //Obtem a relacao da posicao do veiculo com o objeto
        veiculo.getVeiculoNode().worldToLocal(posicaoObjeto, relacaoMeObjeto);
        
        //Sempre acelerar o veiculo caso esteja parado
        if(veiculo.getCurrentVehicleSpeedKmHour() <= 0)
            veiculo.acelerar();
        
        //Para desviar do objeto é ao contrario
        //Se o objeto esta a sua esquerda, entao vira a direita, e vice-versa
        if(relacaoMeObjeto.getX() > 0){
            veiculo.virarDireita();
        }else{
            veiculo.virarEsquerda();
        }
    }

    private void buscarItem() {
        //Obtem a relacao da posicao do veiculo com o item
        veiculo.getVeiculoNode().worldToLocal(posicaoBuscarItem, relacaoMeItem);
        
        //Sempre acelerar o veiculo caso esteja parado
        if(veiculo.getCurrentVehicleSpeedKmHour() <= 0)
            veiculo.acelerar();
        
        //Sempre alinhar o veiculo ao ponto
        if(relacaoMeItem.getX() > NPC.PRECISAO_RETA || relacaoMeItem.getX() < -NPC.PRECISAO_RETA){
                //Se o item esta a sua esquerda, entao vira a esquerda, senao vira a direita
        	if(relacaoMeItem.getX() > 0){
        		veiculo.virarEsquerda();
        	}else{
        		veiculo.virarDireita();
        	}
                
                //veiculo.desacelerar();
        }else{
            veiculo.centralizarDirecao();
        }
    }
    
    /**
     * Controi um colisor fantasma em frente ao veiculo funcionando como visao do NPC
     */
    private void constroiVisao(){
        visao = new GhostControl(new BoxCollisionShape(new Vector3f(10,.7f,15)));
        nodeVisao = new Node("visaoNPC");
        nodeVisao.setLocalTranslation(0, 1, 20f);
        nodeVisao.addControl(visao);  
        veiculo.getVeiculoNode().attachChild(nodeVisao);
        game.getPhysicsSpace().add(visao);
    }
    
    /**
     * Verificam se o NPC deseja buscar um item visto ou nao
     */
    private boolean desejaBuscarItem() {
        //SE O NPC ESTIVER COM A BOLSA CHEIA DE ITENS NUNCA BUSCAR
        if(veiculo.bolsaEstaCheia())
            return false;
        //SE O NPC ESTIVER NA ULTIMA VOLTA, VAI DAR PREFERENCIA EM ULTRAPASSAGEM, NAO EM PEGAR ITENS
        else if(veiculo.getStatusCorrida().ultimaVolta())
            return false;
        else if(veiculo.getStatusCorrida().getPosicao() == 1)
            return false;
        //SE O NPC NAO TIVER A QTDE ADEQUADADE PARA ELE DE ITEM NA BOLSA, ENTAO SEMPRE VAI BUSCAR
        if(veiculo.getBolsa().length < qtdeAdequadaItensNaBolsa)
            return true;
        //SE O NPC ESTIVER EM ULTIMO COLOCADO VAI DAR PREFERENCIA EM BUSCAR ITENS
        else if(veiculo.getStatusCorrida().ultimoColocado())
            return true;
        
        else
            return false;
    }
    
    /**
     * Verificam se o NPC deseja desviar de algum objeto em sua frente
     */
    private boolean desejaDesviarObjeto() {
        //SE O NPC ESTIVER EM ULTIMO COLOCADO, SEMPRE TENTA DESVIAR VISANDO ULTRAPASSAGEM
        if(veiculo.getStatusCorrida().ultimoColocado())
            return true;
        //SE O NPC ESTIVER NA ULTIMA VOLTA, SEMPRE DAR PREFERENCIA A ULTRAPASSAGEM
        if(veiculo.getStatusCorrida().ultimaVolta())
            return true;
        else
            return false;
    }
    
    /**
     * Verifica se o objeto que o NPC esta vendo esta a sua frente, para entao verificar se deseja desviar ou nao
     * @return 
     */
    private boolean consideraObjetoEstarEmSuaFrente(){
        //Obtem a relacao da posicao do veiculo com o objeto
        veiculo.getVeiculoNode().worldToLocal(posicaoObjeto, relacaoMeObjeto);
        
        //Somente considera o objeto estar realmente em sua frente, caso estiver entre a precisao
        if(relacaoMeObjeto.getX() < NPC.PRECISAO_OBJETO_EM_FRENTE && relacaoMeObjeto.getX() > -NPC.PRECISAO_OBJETO_EM_FRENTE){
            return desejaDesviarObjeto();
        }else{
            return false;
        }
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    public Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void collision(PhysicsCollisionEvent colisao) {
        /**
         * Verifica se o NPC viu um item
         */
        if((colisao.getNodeA().getName().equals("item") || colisao.getNodeB().getName().equals("item")) 
                &&
           (colisao.getNodeA() == nodeVisao || colisao.getNodeB() == nodeVisao)){
                if(desejaBuscarItem()){
                    if(colisao.getNodeA().getName().equals("item"))
                        posicaoBuscarItem.set(colisao.getNodeA().getLocalTranslation());
                    else
                        posicaoBuscarItem.set(colisao.getNodeB().getLocalTranslation());
                    
                    //TROCA PARA O ESTADO BUSCAR ITEM
                    estado = ESTADO_BUSCAR_ITEM;
                }else{
                    //CASO O NPC NAO DESEJE BUSCAR O ITEM 
                    //ENTAO ELE SEGUE OS PONTOS
                    estado = ESTADO_SEGUIR_CAMINHO;
                }
        }
        else
        /**
         * Verifica se o NPC viu adversario
         */
        if((colisao.getNodeA().getName().equals("veiculo") || colisao.getNodeB().getName().equals("veiculo")) 
                &&
           (colisao.getNodeA() == nodeVisao || colisao.getNodeB() == nodeVisao)){
                if(colisao.getNodeA().getName().equals("veiculo"))
                    posicaoObjeto.set(colisao.getNodeA().getLocalTranslation());
                else
                    posicaoObjeto.set(colisao.getNodeB().getLocalTranslation());
                
                if(consideraObjetoEstarEmSuaFrente()){
                    //TROCA PARA O ESTADO BUSCAR ITEM
                    estado = ESTADO_DESVIAR_OBJETO;
                }else{
                    //CASO O OBJETO NAO ESTIVER REALMENTE EM SUA FRENTE 
                    //ENTAO ELE SEGUE OS PONTOS
                    estado = ESTADO_SEGUIR_CAMINHO;
                }
                
        }
        else
        {
            //CASO O NPC NAO ESTIVER VENDO NADA EM SEU CAMINHO
            //ENTAO ELE SEGUE OS PONTOS
            estado = ESTADO_SEGUIR_CAMINHO;
        }
    }

}
