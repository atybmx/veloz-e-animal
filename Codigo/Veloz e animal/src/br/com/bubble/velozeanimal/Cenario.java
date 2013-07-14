package br.com.bubble.velozeanimal;

import br.com.bubble.velozeanimal.itens.FactoryItem;
import br.com.bubble.velozeanimal.itens.Item;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import com.jme3.system.NanoTimer;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author atybmx
 */
public class Cenario extends AbstractControl{
    
    private BubbleGame game;
    private Node cena;
    
    private Vector3f posicaoInicial1;
    private Quaternion rotacaoInicial1;
    private Vector3f posicaoInicial2;
    private Quaternion rotacaoInicial2;
    private Vector3f posicaoInicial3;
    private Quaternion rotacaoInicial3;
    private Vector3f posicaoInicial4;
    private Quaternion rotacaoInicial4;
    
    private Spatial sky;
    
    private AudioNode musica;
    
    private Node chegada;
    
    private ArrayList<ArrayList<Vector3f>> mapaPontos = new ArrayList<ArrayList<Vector3f>>();
    private ArrayList<Vector3f> mapaItens;
    private ArrayList<Vector3f> mapaControlePosicoes;
    
    public static int TEMPO_ADICAO_NOVO_ITEM = 10;
    public static int QTDE_ITENS_SEREM_ADICIONADOS = 1;
    
    /**
     * Variaveis que controlam a criacao dos itens no cenario
     */
    private NanoTimer tempoNovoItem = new NanoTimer();

    public Spatial getSky() {
        return sky;
    }

    public void setSky(Spatial sky) {
        this.sky = sky;
    }

    public AudioNode getMusica() {
        return musica;
    }

    public void setMusica(AudioNode musica) {
        this.musica = musica;
        //this.musica.setLooping(true);
    }
    
    public Cenario(BubbleGame game,Node cena){
        this.game = game;
        this.cena = cena;
        this.cena.addControl(this);
    }

    public Vector3f getPosicaoInicial1() {
        return posicaoInicial1;
    }

    public void setPosicaoInicial1(Vector3f posicaoInicial1) {
        this.posicaoInicial1 = posicaoInicial1;
    }

    public Vector3f getPosicaoInicial2() {
        return posicaoInicial2;
    }

    public void setPosicaoInicial2(Vector3f posicaoInicial2) {
        this.posicaoInicial2 = posicaoInicial2;
    }

    public Vector3f getPosicaoInicial3() {
        return posicaoInicial3;
    }

    public void setPosicaoInicial3(Vector3f posicaoInicial3) {
        this.posicaoInicial3 = posicaoInicial3;
    }

    public Vector3f getPosicaoInicial4() {
        return posicaoInicial4;
    }

    public void setPosicaoInicial4(Vector3f posicaoInicial4) {
        this.posicaoInicial4 = posicaoInicial4;
    }

    public Quaternion getRotacaoInicial1() {
        return rotacaoInicial1;
    }

    public void setRotacaoInicial1(Quaternion rotacaoInicial1) {
        this.rotacaoInicial1 = rotacaoInicial1;
    }

    public Quaternion getRotacaoInicial2() {
        return rotacaoInicial2;
    }

    public void setRotacaoInicial2(Quaternion rotacaoInicial2) {
        this.rotacaoInicial2 = rotacaoInicial2;
    }

    public Quaternion getRotacaoInicial3() {
        return rotacaoInicial3;
    }

    public void setRotacaoInicial3(Quaternion rotacaoInicial3) {
        this.rotacaoInicial3 = rotacaoInicial3;
    }

    public Quaternion getRotacaoInicial4() {
        return rotacaoInicial4;
    }

    public void setRotacaoInicial4(Quaternion rotacaoInicial4) {
        this.rotacaoInicial4 = rotacaoInicial4;
    }

    public Node getCena() {
        return cena;
    }

    public void setCena(Node cena) {
        this.cena = cena;
    }

    public Node getChegada() {
        return chegada;
    }

    public void setChegada(Node chegada) {
        this.chegada = chegada;
    }
    
    public void addMapaPontos(ArrayList<Vector3f> mapaPontos){
        this.mapaPontos.add(mapaPontos);
    }
    
    public ArrayList<Vector3f> getMapaPontos(){
        Random numeroRandomico = new Random();
        
        return mapaPontos.get(numeroRandomico.nextInt(mapaPontos.size() - 1));
    }

    public ArrayList<Vector3f> getMapaItens() {
        return mapaItens;
    }

    public void setMapaItens(ArrayList<Vector3f> mapaItens) {
        this.mapaItens = mapaItens;
    }

    public ArrayList<Vector3f> getMapaControlePosicoes() {
        return mapaControlePosicoes;
    }

    public void setMapaControlePosicoes(ArrayList<Vector3f> mapaControlePosicoes) {
        this.mapaControlePosicoes = mapaControlePosicoes;
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        /**
         * De tempos em tempos segundos é adicionado itens novos ao cenario
         */
        if( tempoNovoItem.getTimeInSeconds() > TEMPO_ADICAO_NOVO_ITEM ){
            /**
             * Se tiver posicao para adicionar item entao adicione!
             */
            if(mapaItens.size() > 0)
                adicionaItem();
            
            tempoNovoItem.reset();
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    public Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void adicionaItem() {
        Random numeroRandomico = new Random();
        int posicaoItemSorteado;
        for(int x=0; x < QTDE_ITENS_SEREM_ADICIONADOS ; x++){
            posicaoItemSorteado = numeroRandomico.nextInt(mapaItens.size());
            
            /**
            * Criando o Node e carregando o modelo
            */
            Node modeloNovoItem = (Node)game.getAssetManager().loadModel("Models/Itens/CaixaSurpresa/caixaSurpresa.j3o");
            Item novoItem = FactoryItem.novoItem();
            novoItem.attachChild(modeloNovoItem);
            novoItem.setLocalTranslation(mapaItens.get(posicaoItemSorteado));//Posiciona o modelo na posicao sorteada
        
            /**
            * Criando o item e atribuindo seus valores para o retorno
            */
            Vector3f collisionShape = new Vector3f(1.0000001f,1.0f,0.9510567f);
            GhostControl novoItemControle = new GhostControl(new BoxCollisionShape(collisionShape));
            novoItem.addControl(novoItemControle);
            game.getPhysicsSpace().add(novoItemControle);
            
            mapaItens.remove(posicaoItemSorteado);
            cena.attachChild(novoItem);
        }
    }
    
    
}
