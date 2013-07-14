package br.com.bubble.velozeanimal;

import com.jme3.animation.LoopMode;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.cinematic.Cinematic;
import com.jme3.cinematic.events.CinematicEvent;
import com.jme3.cinematic.events.CinematicEventListener;
import com.jme3.cinematic.events.PositionTrack;
import com.jme3.cinematic.events.RotationTrack;
import com.jme3.cinematic.events.SoundTrack;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.system.NanoTimer;
import com.jme3.ui.Picture;
import de.lessvoid.nifty.Nifty;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author atybmx
 */
public class Corrida extends AbstractAppState implements CinematicEventListener{
    /** 
     * ######################################
     * Atributos herdados da Aplication
     * ######################################
     */
    private BubbleGame app;
    private Node              rootNode;
    private AssetManager      assetManager;
    private AppStateManager   stateManager;
    private InputManager      inputManager;
    private ViewPort          viewPort;
    private BulletAppState    bullet;
    private Node guiNode;
    private Camera cam;
    private Camera camLargada;
    
    /*
     * LARGADA DA CORRIDA
     */
    private Cinematic largada;
    private Node largadaNode;
    
    /**
     * ######################################
     * Atributos referentes do jogo
     * ######################################
     */
    private CameraNode camNode;
    private Cenario cenario;
    private Veiculo veiculoUm;
    private Veiculo veiculoDois;
    private Veiculo veiculoTres;
    private Veiculo veiculoQuatro;
    private Jogador jogadorVeiculoUm;
    private NPC npcVeiculoDois;
    private NPC npcVeiculoTres;
    private NPC npcVeiculoQuatro;
    public static final int QUANTIDADE_VOLTAS = 5;
    
    /**
     * HUDs
     */
    private ArrayList<Picture> hudsItem = new ArrayList<Picture>();
    private ArrayList<Picture> hudsPosicao = new ArrayList<Picture>();
    private Picture voltaHUD;
    private NanoTimer tempoHUDVolta = new NanoTimer();
    private static final int TEMPO_LIMITE_HUD_VOLTA = 2;
    
    
    private ArrayList<Veiculo> posicaoVeiculos = new ArrayList<Veiculo>();
    
    private boolean finalizouCorrida = false;
    
    /**
     * 
     * @param stateManager
     * @param app 
     */
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (BubbleGame) app; // can cast Application to something more specific
        this.rootNode     = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        this.stateManager = this.app.getStateManager();
        this.inputManager = this.app.getInputManager();
        this.viewPort     = this.app.getViewPort();
        this.bullet       = this.stateManager.getState(BulletAppState.class);
        this.guiNode      = this.app.getGuiNode(); 
        this.cam          = this.app.getCamera();
        this.camLargada   = cam.clone();
        
        constroiCena();
        constroiVeiculos();
        constroiHUD();
        constroiLargada();
       // constroiNodeCamera();
       // iniciaCorrida();
    }
    
    @Override
    public void cleanup() {
      super.cleanup();
    }
 
    @Override
    public void setEnabled(boolean enabled) {
      // Pause and unpause
      super.setEnabled(enabled);
      
    }
 
    @Override
    public void update(float tpf) {
        super.update(tpf);
        
        if(voltaHUD != null && tempoHUDVolta.getTimeInSeconds() >= TEMPO_LIMITE_HUD_VOLTA){
            guiNode.detachChild(voltaHUD);
            voltaHUD = null;
        }
        
        getPosicao(veiculoUm);
        getPosicao(veiculoDois);
        getPosicao(veiculoTres);
        getPosicao(veiculoQuatro);
        
        if(veiculoUm.getStatusCorrida().terminouCorrida() && !finalizouCorrida){
            terminarCorrida();
            finalizouCorrida = true;
        }
    }
    
    public void getPosicao(Veiculo veiculo){
        if(veiculo.getStatusCorrida().getPosicao() <= 1 || veiculo.getStatusCorrida().terminouCorrida() || veiculoUm.getStatusCorrida().terminouCorrida()){
            
        }else{
            StatusCorrida statusCorrida = veiculo.getStatusCorrida();
            int indicePosicao = statusCorrida.getPosicao() - 1;
            Veiculo veiculoAFrente = posicaoVeiculos.get(indicePosicao - 1);
            
            if(veiculoAFrente.getStatusCorrida().getPosicao() <= 0 || veiculoAFrente.getStatusCorrida().terminouCorrida()){//IF USADO PARA INICIAR AS VARIAVEIS
                
            }else{
          //      if(statusCorrida.getVolta() > veiculoAFrente.getStatusCorrida().getVolta()){
          //          trocaPosicao(veiculoAFrente, veiculo);
          //      }else if(statusCorrida.getVolta() == veiculoAFrente.getStatusCorrida().getVolta() && !veiculoAFrente.getStatusCorrida().terminouVolta()){
                    
                    if(statusCorrida.getIndicePosicao() > veiculoAFrente.getStatusCorrida().getIndicePosicao()){
                        trocaPosicao(veiculoAFrente, veiculo);
                    }else if(statusCorrida.getIndicePosicao() == veiculoAFrente.getStatusCorrida().getIndicePosicao()){
                        if(statusCorrida.getDistanciaProximaPosicao() < veiculoAFrente.getStatusCorrida().getDistanciaProximaPosicao()){
                            trocaPosicao(veiculoAFrente, veiculo);
                        }
                
                    }
               // }
            }
        }
    }
    
    public void trocaPosicao(Veiculo veiculoFrente, Veiculo veiculoTras){
        int posicaoFrente = veiculoFrente.getStatusCorrida().getPosicao(); 
        int posicaoTras = veiculoTras.getStatusCorrida().getPosicao();
        
        veiculoFrente.getStatusCorrida().setPosicao(posicaoTras);
        veiculoTras.getStatusCorrida().setPosicao(posicaoFrente);
        
        posicaoVeiculos.set(posicaoFrente - 1, veiculoTras);
        posicaoVeiculos.set(posicaoTras - 1, veiculoFrente);
        
        hudsPosicao.get(posicaoFrente - 1).setImage(app.getAssetManager(), veiculoTras.getHudPosicao(), true);
        hudsPosicao.get(posicaoTras - 1).setImage(app.getAssetManager(), veiculoFrente.getHudPosicao(), true);
        
    }
    
    private void constroiCena() {
        cenario = FactoryCenario.festaAnimal(app);
        /**
         * Adicionando os elementos da cena no rootNode do game
         */
        rootNode.attachChild(cenario.getCena());
        rootNode.attachChild(cenario.getSky());
        rootNode.attachChild(cenario.getMusica());
        rootNode.attachChild(cenario.getChegada());
    }
    
    public Cenario getCenario(){
        return cenario;
    }
    
    private void constroiJogador() {
        jogadorVeiculoUm = new Jogador(veiculoUm);
        app.getPhysicsSpace().addCollisionListener(jogadorVeiculoUm);
        
        inputManager.addMapping("Esquerda", new KeyTrigger(KeyInput.KEY_LEFT),new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Direita", new KeyTrigger(KeyInput.KEY_RIGHT),new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Acelerar", new KeyTrigger(KeyInput.KEY_UP),new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Frear", new KeyTrigger(KeyInput.KEY_DOWN),new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Item1", new KeyTrigger(KeyInput.KEY_1),new KeyTrigger(KeyInput.KEY_NUMPAD1));
        inputManager.addMapping("Item2", new KeyTrigger(KeyInput.KEY_2),new KeyTrigger(KeyInput.KEY_NUMPAD2));
        inputManager.addMapping("Item3", new KeyTrigger(KeyInput.KEY_3),new KeyTrigger(KeyInput.KEY_NUMPAD3));
        inputManager.addMapping("Reset", new KeyTrigger(KeyInput.KEY_RETURN));
        inputManager.addListener(jogadorVeiculoUm, "Esquerda");
        inputManager.addListener(jogadorVeiculoUm, "Direita");
        inputManager.addListener(jogadorVeiculoUm, "Acelerar");
        inputManager.addListener(jogadorVeiculoUm, "Frear");
        inputManager.addListener(jogadorVeiculoUm, "Item1");
        inputManager.addListener(jogadorVeiculoUm, "Item2");
        inputManager.addListener(jogadorVeiculoUm, "Item3");
        inputManager.addListener(jogadorVeiculoUm, "Reset");
    }

    private void constroiVeiculos() {
        /**
         * Criando o veiculo UM
         */
        veiculoUm = FactoryPersonagem.zapata(this,app,true);
        veiculoUm.setPhysicsLocation(cenario.getPosicaoInicial4());
        veiculoUm.setPhysicsRotation(cenario.getRotacaoInicial4());
        StatusCorrida statusCorrida = veiculoUm.getStatusCorrida();
        statusCorrida.setQtdeVoltas(QUANTIDADE_VOLTAS);
        statusCorrida.setPosicao(4);
        statusCorrida.setQtdeCompetidores(4);
        statusCorrida.setPosicaoInicial(cenario.getPosicaoInicial4());
        veiculoUm.distanciaVeiculoPonto = StatusCorrida.DISTANCIA_JOGADOR_PONTO;
        constroiJogador();
        
        /**
         * Criando o veiculo DOIS
         */
        veiculoDois = FactoryPersonagem.ademir(this,app,false);
        veiculoDois.setPhysicsLocation(cenario.getPosicaoInicial1());
        veiculoDois.setPhysicsRotation(cenario.getRotacaoInicial1());
        StatusCorrida statusCorridaDois = veiculoDois.getStatusCorrida();
        statusCorridaDois.setQtdeVoltas(QUANTIDADE_VOLTAS);
        statusCorridaDois.setPosicao(1);
        statusCorridaDois.setQtdeCompetidores(4);
        statusCorridaDois.setPosicaoInicial(cenario.getPosicaoInicial1());
        veiculoDois.distanciaVeiculoPonto = StatusCorrida.DISTANCIA_NPC_PONTO;
        npcVeiculoDois = FactoryPersonagem.constroiNPC(veiculoDois,app);
        
        /**
         * Criando o veiculo TRES
         */
        veiculoTres = FactoryPersonagem.liminha(this,app,false);
        veiculoTres.setPhysicsLocation(cenario.getPosicaoInicial2());
        veiculoTres.setPhysicsRotation(cenario.getRotacaoInicial2());
        StatusCorrida statusCorridaTres = veiculoTres.getStatusCorrida();
        statusCorridaTres.setQtdeVoltas(QUANTIDADE_VOLTAS);
        statusCorridaTres.setPosicao(2);
        statusCorridaTres.setQtdeCompetidores(4);
        statusCorridaTres.setPosicaoInicial(cenario.getPosicaoInicial2());
        veiculoTres.distanciaVeiculoPonto = StatusCorrida.DISTANCIA_NPC_PONTO;
        npcVeiculoTres = FactoryPersonagem.constroiNPC(veiculoTres,app);
        
        /**
         * Criando o veiculo QUATRO
         */
        veiculoQuatro = FactoryPersonagem.vino(this,app,false);
        veiculoQuatro.setPhysicsLocation(cenario.getPosicaoInicial3());
        veiculoQuatro.setPhysicsRotation(cenario.getRotacaoInicial3());
        StatusCorrida statusCorridaQuatro = veiculoQuatro.getStatusCorrida();
        statusCorridaQuatro.setQtdeVoltas(QUANTIDADE_VOLTAS);
        statusCorridaQuatro.setPosicao(3);
        statusCorridaQuatro.setQtdeCompetidores(4);
        statusCorridaQuatro.setPosicaoInicial(cenario.getPosicaoInicial3());
        veiculoQuatro.distanciaVeiculoPonto = StatusCorrida.DISTANCIA_NPC_PONTO;
        npcVeiculoQuatro = FactoryPersonagem.constroiNPC(veiculoQuatro,app);
        
        /*
         * CRIANDO A LISTA DE POSICOES
         */
        posicaoVeiculos.add(veiculoDois);
        posicaoVeiculos.add(veiculoTres);
        posicaoVeiculos.add(veiculoQuatro);
        posicaoVeiculos.add(veiculoUm);
    }
    
    private void constroiNodeCamera(){
     //   app.getFlyByCamera().setEnabled(false);
    //    camNode = new CameraNode("Camera Node", cam);
        //Copia todos os movimentos do jogador para a camera
        camNode.setControlDir(ControlDirection.SpatialToCamera);
        //Anexa a camera Node no jogador
        veiculoUm.getVeiculoNode().attachChild(camNode);
        //Posiciona a camera Node para atras do jogador
        camNode.setLocalTranslation(new Vector3f(0, 7, -20));
        //Rotaciona a camera Node olhando na direcao do jogador
      //  camNode.lookAt(veiculoUm.getVeiculoNode().getLocalTranslation().add(0, 6, 0), Vector3f.UNIT_Y);
        camNode.setLocalRotation(new Quaternion().fromAngleAxis(FastMath.PI * 2, Vector3f.UNIT_Y));
        
    }

    private void constroiHUD() {
        //Cria bolsa sem itens
        for(int x = 0 ; x < Veiculo.QTDE_MAX_ITENS_NA_BOLSA ; x++){
            Picture pic = new Picture("Item" + x+1);
            pic.setImage(app.getAssetManager(), "Textures/HUD/itemVazioHUD.png", true);
            pic.setWidth(150);
            pic.setHeight(150);
            pic.setPosition((20 * (x+1)) + (pic.getLocalScale().x * x),
                    app.getContext().getSettings().getHeight() - pic.getLocalScale().y - 20);
            guiNode.attachChild(pic);
            hudsItem.add(pic);
        }
        
        //Cria Indice Posicoes
        for(int x = 0 ; x < posicaoVeiculos.size(); x++){
            Veiculo veiculoAtual = posicaoVeiculos.get(x);
            Picture pic = new Picture("Veiculo" + x+1);
            pic.setImage(app.getAssetManager(), veiculoAtual.getHudPosicao(), true);
            pic.setWidth(140);
            pic.setHeight(30);
            pic.setPosition(app.getContext().getSettings().getWidth() - pic.getLocalScale().x - 20,
                            (app.getContext().getSettings().getHeight() / 2) - ((20 * (x+1)) + (pic.getLocalScale().y * x)));
            guiNode.attachChild(pic);
            hudsPosicao.add(pic);
            
            Picture indicePosicao = new Picture("IndicePosicao" + x+1);
            indicePosicao.setImage(app.getAssetManager(), "Textures/HUD/"+(x+1)+"PosicaoHUD.png", true);
            indicePosicao.setWidth(30);
            indicePosicao.setHeight(30);
            indicePosicao.setPosition(app.getContext().getSettings().getWidth() - pic.getLocalScale().x - indicePosicao.getLocalScale().x - 20,
                            (app.getContext().getSettings().getHeight() / 2) - ((20 * (x+1)) + (pic.getLocalScale().y * x)));
            guiNode.attachChild(indicePosicao);
        }
    }
    
    public void atualizaHUDItem(Veiculo veiculo){
        if(veiculo == jogadorVeiculoUm.getVeiculo()){
            
            for(int x = 0 ; x < Veiculo.QTDE_MAX_ITENS_NA_BOLSA ; x++){
                if( veiculo.getBolsa()[x] == null ){
                    hudsItem.get(x).setImage(app.getAssetManager(), "Textures/HUD/itemVazioHUD.png", true);
                }else{
                    hudsItem.get(x).setImage(app.getAssetManager(), veiculo.getBolsa()[x].getImagemHUD(), true);
                }
            }
        }
        
    }
    
    public boolean isJogador(Veiculo veiculo){
        return jogadorVeiculoUm.getVeiculo() == veiculo;
    }
    
    public void adicionarHUDVolta(int volta){
        voltaHUD = new Picture("Volta" + volta);
        voltaHUD.setImage(app.getAssetManager(), "Textures/HUD/volta"+volta+".png", true);
        voltaHUD.setWidth(200);
        voltaHUD.setHeight(150);
        voltaHUD.setPosition(20,
                            (app.getContext().getSettings().getHeight() / 2) - voltaHUD.getLocalScale().y);    
        guiNode.attachChild(voltaHUD);
        tempoHUDVolta.reset();
    }
    
    public Veiculo getVeiculoAFrente(Veiculo veiculo){
        StatusCorrida statusCorrida = veiculo.getStatusCorrida();
        
        int indicePosicao = statusCorrida.getPosicao() - 1;
        
        //Se for o primeiro colocado, retorna o ultimo
        if(indicePosicao == 0)
            indicePosicao = posicaoVeiculos.size();
        
        return posicaoVeiculos.get(indicePosicao - 1);
    }
    
    public BubbleGame getBubbleGame(){
        return app;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public BulletAppState getBullet() {
        return bullet;
    }

    public Node getGuiNode() {
        return guiNode;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public Node getRootNode() {
        return rootNode;
    }

    public AppStateManager getStateManager() {
        return stateManager;
    }

    public ViewPort getViewPort() {
        return viewPort;
    }


    private void iniciaCorrida() {
        cenario.getMusica().play();
        
        adicionarHUDVolta(QUANTIDADE_VOLTAS);
        
        veiculoUm.getStatusCorrida().setIniciouCorrida(true);
        veiculoDois.getStatusCorrida().setIniciouCorrida(true);
        veiculoTres.getStatusCorrida().setIniciouCorrida(true);
        veiculoQuatro.getStatusCorrida().setIniciouCorrida(true);
        veiculoUm.acelerar();
        veiculoDois.acelerar();
        veiculoTres.acelerar();
        veiculoQuatro.acelerar();
    }

    private void constroiLargada() {
        /*
         * Cria um node INVISIVEL que fará o caminho de apresentação
         */
        Vector3f inicioLargada = new Vector3f(-10f , .3f, -100f);
        Vector3f fimLargada = new Vector3f(64.86f , .3f, -100f);
        
        largadaNode = new Node("LargadaNode");
        largadaNode.setLocalTranslation(inicioLargada);
        rootNode.attachChild(largadaNode);
        
        //Cria uma camera Node que seguira o node INVISIVEL
        app.getFlyByCamera().setEnabled(false);
        camNode = new CameraNode("Camera Node", cam);
        camNode.setControlDir(ControlDirection.SpatialToCamera);
        largadaNode.attachChild(camNode);
        camNode.setLocalTranslation(new Vector3f(0, 5, 0));
        camNode.lookAt(fimLargada, Vector3f.UNIT_Y);
        
        largada = new Cinematic(rootNode, 24);
        
        PositionTrack conhecendoOsPersonagens = new PositionTrack(
                            largadaNode, 
                            fimLargada, 
                            10, 
                            LoopMode.DontLoop);
        conhecendoOsPersonagens.setSpeed(1);
        
        SoundTrack somLargada = new SoundTrack( 
                            "Sounds/Musicas/largada.ogg", 
                            false, 
                            15, 
                            LoopMode.DontLoop );
        
        RotationTrack rotacionandoNaDirecaoCerta = new RotationTrack(
                            largadaNode, 
                            new Quaternion().fromAngleAxis(FastMath.PI, Vector3f.UNIT_Y),  
                            5, 
                            LoopMode.DontLoop);
        rotacionandoNaDirecaoCerta.setSpeed(1);
        
        PositionTrack posicionandoNoJogador = new PositionTrack(
                            largadaNode, 
                            veiculoUm.getPhysicsLocation().addLocal(20, 3, 0), 
                            5, 
                            LoopMode.DontLoop);
        posicionandoNoJogador.setSpeed(1);
        
        SoundTrack somContagem = new SoundTrack( 
                            "Sounds/Musicas/contagem.ogg", 
                            false, 
                            5, 
                            LoopMode.DontLoop );

        largada.addCinematicEvent(0, conhecendoOsPersonagens);
        largada.addCinematicEvent(0, somLargada);
        largada.addCinematicEvent(15, rotacionandoNaDirecaoCerta);
        largada.addCinematicEvent(15, posicionandoNoJogador);
        largada.addCinematicEvent(20, somContagem);
        
        stateManager.attach(largada);
        
        largada.addListener(this);
        largada.play();
    }

    public void onPlay(CinematicEvent cinematic) {
        veiculoUm.getStatusCorrida().setIniciouCorrida(false);
        veiculoDois.getStatusCorrida().setIniciouCorrida(false);
        veiculoTres.getStatusCorrida().setIniciouCorrida(false);
        veiculoQuatro.getStatusCorrida().setIniciouCorrida(false);
    }

    public void onPause(CinematicEvent cinematic) {
    }

    public void onStop(CinematicEvent cinematic) {
        largada.setEnabled(false);
        largadaNode.detachAllChildren();
        largadaNode.removeFromParent();
        
        constroiNodeCamera();
        iniciaCorrida();
    }
    
    public void terminarCorrida(){
        //Para a musica do GAME PLAY
        cenario.getMusica().stop();
        
        //Remove os HUDs
        guiNode.detachAllChildren();
        
        //Cria o placar
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
        assetManager, inputManager, app.getAudioRenderer(), app.getGuiViewPort());        
        Nifty nifty = niftyDisplay.getNifty(); 
        Logger.getLogger("de.lessvoid.nifty").setLevel(Level.SEVERE); 
        Logger.getLogger("NiftyInputEventHandlingLog").setLevel(Level.SEVERE); 
        
        nifty.registerScreenController(new Chegada(this));
        nifty.addXml("Interface/chegada.xml");
        nifty.gotoScreen("start");
        
        app.getGuiViewPort().addProcessor(niftyDisplay);
        
        //Toca a musica de finalizacao
        String nomeMusicaFinal = "";
        if(veiculoUm.getStatusCorrida().getPosicao() == 1)
            nomeMusicaFinal = "venceu";
        else
            nomeMusicaFinal = "perdeu";
        
        AudioNode musicaFinal = new AudioNode(getAssetManager(), "Sounds/Musicas/"+nomeMusicaFinal+".ogg", true);
        musicaFinal.play();
    }

    public String getPrimeiroColocado(){
        return posicaoVeiculos.get(0).getNome();
    }
    
    public String getSegundoColocado(){
        return posicaoVeiculos.get(1).getNome();
    }
    
    public String getTerceiroColocado(){
        return posicaoVeiculos.get(2).getNome();
    }
    
    public String getQuartoColocado(){
        return posicaoVeiculos.get(3).getNome();
    }
}
