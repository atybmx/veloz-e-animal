package br.com.bubble.velozeanimal;

import br.com.bubble.velozeanimal.itens.Item;
import br.com.bubble.velozeanimal.itens.controle.MelanciaControl;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import com.jme3.system.NanoTimer;
import com.jme3.ui.Picture;

/**
 *
 * @author atybmx
 */
public class Veiculo extends VehicleControl implements PhysicsCollisionListener{
    /**
     * Variavel de referencia do ambiente do game
     */
    private Corrida theGame;
    
    /**
     * Dados de corrida do veiculo
     */
    private StatusCorrida statusCorrida;
    
    /**
     * O Node que representa o veiculo na cena
     * As rodas do veiculo
     */
    private Node veiculoNode;
    
    /**
     * O Node que representa o modelo do personagem
     */
    private Node personagem;
    
    /**
     * Vetor que indica a direcao do veiculo
     */
    public Vector3f direcaoVeiculo = new Vector3f();
    
    /**
     * Valor da forca de aceleracao que esta sendo aplicada no veiculo
     */
    private float valorAceleracao = 0;
    
    /**
     * Forca de aceleracao do veiculo, este valor é somado quando o veiculo esta
     * em processo de aceleracao
     */
    private static final float FORCA_ACELERACAO = 1500.0f;
    
    
    /**
     * Forca de frenagem do carro
     */
    private static final float FORCA_FREIO = 100.0f;
    
    /**
     * Valor da direção indica o sentido que o veiculo esta se movendo
     * Quando igual a ZERO significa que o carro esta em linha reta
     * Quando maior que ZERO significa que o carro esta em sentido a esquerda
     * Quando menor que ZERO significa que o carro esta em sentido a direita
     */
    private float valorDirecao = 0;
    public static final float FORCA_DIRECAO_NPC = .5f;
    public static final float FORCA_DIRECAO_JOGADOR = .3f;
    
    private float forcaDirecao = FORCA_DIRECAO_NPC;
    public float distanciaVeiculoPonto = 100f;
    
    /**
     * Os itens que o veiculo pega durante a corrida
     * É possivel armazenar 3 itens
     */
    private Item[] bolsa = new Item[QTDE_MAX_ITENS_NA_BOLSA];
    public static int QTDE_MAX_ITENS_NA_BOLSA = 3;
    
    
    private boolean veiculoParado;
    private NanoTimer tempoParado = new NanoTimer();
    private static final int TEMPO_LIMITE_PARADO = 4;
    
    private String nome;
    private String hudPosicao;
    
    /**
     * Atributos que tratam do poder do sorvete sobre o veiculo
     */
    private boolean congelado;
    private NanoTimer tempoCongelado = new NanoTimer();
    private static final int TEMPO_CONGELADO = 5;
    private Picture HUDCongelado;
    
    /**
     * 
     * @param shape
     * @param mass
     * @param theGame 
     */
    public Veiculo(CollisionShape shape, float mass, Corrida theGame) {
        super(shape, mass);
        
        this.theGame = theGame;
        //Cria o status Corrida
        statusCorrida = new StatusCorrida(theGame.getCenario());
    }
    
    @Override
    public void update(float tpf) {
        super.update(tpf);
        
        //Obtem a direcao do veiculo
        getForwardVector(direcaoVeiculo);
        
        verificaSeEstaParado();
        
        if(estaForaDeControle())
            retomarControle();
        
        //Verifica se o veiculo esta congelado e se o tempo congelado ja excedeu
        if(congelado && tempoCongelado.getTimeInSeconds() >= TEMPO_CONGELADO)
            descongelar();
        
        //Obtem a distancia do ponto atual do circuito com o veiculo
        //E verifica se o veiculo esta perto desse ponto
        if(getPhysicsLocation().distanceSquared(statusCorrida.getPontoAtual()) <= distanciaVeiculoPonto) {
            //atualiza a posicao do ponto atual
            statusCorrida.proximoPonto();
        }
        
        //Atualiza a distancia para o proximo ponto de controle de posicoes
        statusCorrida.setDistanciaProximaPosicao(getPhysicsLocation().distanceSquared(statusCorrida.getPontoPosicao()));
        
        //Verifica e atualiza o ponto de controle de posicao do veiculo
        if(statusCorrida.getDistanciaProximaPosicao() <= StatusCorrida.DISTANCIA_VEICULO_PONTO_POSICAO){
            statusCorrida.proximoPontoPosicao();
        }
        
        //Atualiza a Posicao do veiculo
        //theGame.getPosicao(this);
    }
    
    
    
    public void verificaSeEstaParado(){
        if(getCurrentVehicleSpeedKmHour() < 10 && !veiculoParado && !congelado && statusCorrida.isIniciouCorrida()){
            veiculoParado = true;
            tempoParado.reset();
        }
        
        if(getCurrentVehicleSpeedKmHour() >= 10 && veiculoParado){
            veiculoParado = false;
        }
        
        //Se o veiculo estiver com velocidade igual a 0 parar o freio
        if(getCurrentVehicleSpeedKmHour() <= 0){
            brake(0);
        }
    }
    
    public boolean estaForaDeControle(){
        if(congelado || !statusCorrida.isIniciouCorrida())
            return false;
        
        if(getPhysicsLocation().getY() > 5)
            return true;
        
        if(veiculoParado && tempoParado.getTimeInSeconds() >= TEMPO_LIMITE_PARADO)
            return true;
            
        return false;
    }
    
    public void retomarControle(){
        clearForces();
        resetSuspension();
        setPhysicsLocation(statusCorrida.getPontoAnterior());
        Quaternion rotacao = new Quaternion();
        rotacao.lookAt(statusCorrida.getPontoAtual().subtract(statusCorrida.getPontoAnterior()), Vector3f.UNIT_Y);
        setPhysicsRotation(rotacao);
        setLinearVelocity(Vector3f.ZERO);
        setAngularVelocity(Vector3f.ZERO);
        
        veiculoParado = false;
    }
    
    public Node getVeiculoNode() {
        return veiculoNode;
    }

    public void setVeiculoNode(Node veiculoNode) {
        this.veiculoNode = veiculoNode;
    }
    
    public void setTheGame(Corrida theGame){
        this.theGame = theGame;
    }
    
    public Corrida getTheGame(){
        return theGame;
    }
    
    /**
     * Utilizar um item armazenado na bolsa
     * @param posicaoNaBolsa Um codigo entre 0 e a quantidade maxima de itens que podem ser armazenados na bolsa
     */
    public void utilizarItemBolsa(int posicaoNaBolsa,boolean iteragir){
        if(!congelado && statusCorrida.isIniciouCorrida()){
            //Se a posicao é inexistente na bolsa
            if( posicaoNaBolsa < 0 || posicaoNaBolsa >= QTDE_MAX_ITENS_NA_BOLSA)
                return;
        
            //Se a posicao é valida na bolsa e existe um item na posicao pedida
            if( bolsa[posicaoNaBolsa] != null){
                bolsa[posicaoNaBolsa].utilizarPoder(this,iteragir);
                bolsa[posicaoNaBolsa] = null;
            
                //Se o veiculo for jogador atualiza o HUD
                theGame.atualizaHUDItem(this);
            }
        }
    }    
    
    /**
     * Armazenar um novo item na bolsa
     * @param item o Item a ser armazenado
     * @return se tinha posicao livre na bolsa para armazenar o novo item
     */
    private boolean guardarItemNaBolsa(Item item){      
        for(int x=0 ; x < QTDE_MAX_ITENS_NA_BOLSA ; x++){
            if(bolsa[x] == null){
                bolsa[x] = item;
                return true;
            }
                
        }
        
        return false;
    }
    
    public void acelerar(){
        if(!congelado && statusCorrida.isIniciouCorrida()){
            valorAceleracao = FORCA_ACELERACAO;
        
            accelerate(valorAceleracao);
        }
    }

    public void desacelerar(){
        valorAceleracao = 0;
        
        accelerate(valorAceleracao);
    }
    
    public void pisarFreio(){
        brake(FORCA_FREIO);
    }
    
    public void soltarFreio(){
        brake(0);
    }
    
    public void virarDireita(){
        valorDirecao = -forcaDirecao;
        
        steer(valorDirecao);
    }
    
    public void virarEsquerda(){
        valorDirecao = forcaDirecao;
        
        steer(valorDirecao);
    }
    
    public void centralizarDirecao(){
        valorDirecao = 0;
        
        steer(valorDirecao);
    }
    
    public void setForcaDirecao(float forcaDirecao){
        this.forcaDirecao = forcaDirecao;
    }
    
    public Vector3f getDirecao(){
        return direcaoVeiculo;
    }
    
    public void atualizaDirecao(){
        getForwardVector(direcaoVeiculo);
    }
    
    public void setPersonagem(Node personagem){
        this.personagem = personagem;
        getVeiculoNode().attachChild(this.personagem);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getHudPosicao() {
        return hudPosicao;
    }

    public void setHudPosicao(String hudPosicao) {
        this.hudPosicao = hudPosicao;
    }
    
    public StatusCorrida getStatusCorrida() {
        return statusCorrida;
    }

    public void setStatusCorrida(StatusCorrida statusCorrida) {
        this.statusCorrida = statusCorrida;
    }

    public Item[] getBolsa(){
        return bolsa;
    }
    
    public boolean bolsaEstaCheia(){
        for(int x=0 ; x < QTDE_MAX_ITENS_NA_BOLSA ; x++){
            if(bolsa[x] == null)
                return false;
        }
        
        return true;
    }
    
    public void rodar(){
        Vector3f forcaTorque = new Vector3f(0,59000,0);
        applyTorqueImpulse(forcaTorque);
    }
    
    public void parar(){
        setLinearVelocity(Vector3f.ZERO);
    }
    
    public boolean isCongelado(){
        return congelado;
    }
    
    public void descongelar(){
        congelado = false;
        tempoCongelado.reset();
        
        if(HUDCongelado != null){
            HUDCongelado.removeFromParent();
            HUDCongelado = null;
        }
    }
    
    public void congelar(){
        congelado = true;
        tempoCongelado.reset();
        parar();
        
        if(theGame.isJogador(this))
            mostrarIndexHUDCongelado();
    }
    
    public void mostrarIndexHUDCongelado(){
        HUDCongelado = new Picture("CONGELADO");
        HUDCongelado.setImage(theGame.getAssetManager(), "Textures/HUD/congeladoHUD.png", true);
        HUDCongelado.setWidth(150);
        HUDCongelado.setHeight(143);
        HUDCongelado.setPosition((theGame.getBubbleGame().getContext().getSettings().getWidth() / 2) - (HUDCongelado.getLocalScale().x / 2),
                                 (theGame.getBubbleGame().getContext().getSettings().getHeight() / 2) - (HUDCongelado.getLocalScale().y / 2));
        theGame.getGuiNode().attachChild(HUDCongelado);
    }
    
    /**
     * Verifica se o veiculo capturou algum item na corrida
     * @param colisao 
     */
    public void collision(PhysicsCollisionEvent colisao) {
        /**
         * Verifica se o veiculo pegou um item
         */
        if((colisao.getNodeA().getName().equals("item") || colisao.getNodeB().getName().equals("item")) 
                &&
           (colisao.getNodeA() == veiculoNode || colisao.getNodeB() == veiculoNode)){
            
            Item item;
                
                if(colisao.getNodeA().getName().equals("item"))
                    item = (Item) colisao.getNodeA();
                else
                    item = (Item) colisao.getNodeB();
                
            if(guardarItemNaBolsa(item)){
                //Se o veiculo for jogador atualiza o HUD
                theGame.atualizaHUDItem(this);
                
                GhostControl controle;
                
                
                if(colisao.getObjectA() != this)
                    controle = (GhostControl) colisao.getObjectA();
                else
                    controle = (GhostControl) colisao.getObjectB();
                
                controle.setEnabled(false);
                controle.destroy();
                item.removeFromParent();
            }
                
        }
        
        /**
         * Verifica se o veiculo chegou na linha de chegada
         */
        if((colisao.getNodeA().getName().equals("chegada") || colisao.getNodeB().getName().equals("chegada")) 
                &&
           (colisao.getNodeA() == veiculoNode || colisao.getNodeB() == veiculoNode)){
            
            if(statusCorrida.terminouVolta()){
                statusCorrida.adicionarVolta();
                
                if(!statusCorrida.terminouCircuito() && theGame.isJogador(this))
                    theGame.adicionarHUDVolta(statusCorrida.getQtdeVoltas() - statusCorrida.getVolta());
            }
            
            if(statusCorrida.terminouCircuito())
                statusCorrida.setTerminouCorrida(true);
        }
        
        /**
         * Verifica se o veiculo encostou em uma casca de banana
         */
        if((colisao.getNodeA().getName().equals("cascaBanana") || colisao.getNodeB().getName().equals("cascaBanana")) 
                &&
           (colisao.getNodeA() == veiculoNode || colisao.getNodeB() == veiculoNode)){
                rodar();
                parar();
         
                Node cascaBanana;
                if(colisao.getNodeA().getName().equals("cascaBanana"))
                    cascaBanana = (Node) colisao.getNodeA();
                else
                    cascaBanana = (Node) colisao.getNodeB();
            
            
                GhostControl controle;
                
                if(colisao.getObjectA() != this)
                    controle = (GhostControl) colisao.getObjectA();
                else
                    controle = (GhostControl) colisao.getObjectB();
                
                controle.setEnabled(false);
                controle.destroy();
                cascaBanana.removeFromParent();
        }
        
    }
    
}
