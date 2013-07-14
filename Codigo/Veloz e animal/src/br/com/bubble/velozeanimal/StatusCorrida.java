package br.com.bubble.velozeanimal;

import com.jme3.math.Vector3f;
import java.util.ArrayList;

/**
 *
 * @author atybmx
 */
public class StatusCorrida {
    
    private ArrayList<Vector3f> mapaPontos;
    private Vector3f pontoAtual;
    private Vector3f pontoAnterior;
    private Vector3f posicaoInicial;
    private int indicePosicaoMapaPontos = -1;
    private int volta = 0;
    private int qtdeVoltas;
    private boolean iniciouCorrida = false;
    
    public static final float DISTANCIA_NPC_PONTO = 50f;
    public static final float DISTANCIA_JOGADOR_PONTO = 2000f;
    
    private Cenario corrida;
    
    private boolean terminouVolta;
    private boolean terminouCorrida;
    
    /*
     * CONTROLAM A POSICAO DO VEICULO
     */
    private int posicao;
    private int qtdeCompetidores;
    private Vector3f pontoPosicao;
    private int indicePosicao = -1;
    private int voltaPontosPosicao = 0;
    private float distanciaProximaPosicao = 0f;
    
     
    
    public static final float DISTANCIA_VEICULO_PONTO_POSICAO = 2000f;
     
     
    
    public StatusCorrida(Cenario corrida){
        this.corrida = corrida;
        mapaPontos = corrida.getMapaPontos();
        proximoPonto();
        proximoPontoPosicao();
    }

    public Vector3f getPontoAtual() {
        return pontoAtual;
    }

    public int getQtdeVoltas() {
        return qtdeVoltas;
    }

    public Vector3f getPontoAnterior() {
        if(pontoAnterior == null)
            return posicaoInicial;
        else
            return pontoAnterior;
    }

    public void setPontoAnterior(Vector3f pontoAnterior) {
        this.pontoAnterior = pontoAnterior;
    }

    public Vector3f getPosicaoInicial() {
        return posicaoInicial;
    }

    public void setPosicaoInicial(Vector3f posicaoInicial) {
        this.posicaoInicial = posicaoInicial;
    }

    public void setQtdeVoltas(int qtdeVoltas) {
        this.qtdeVoltas = qtdeVoltas;
    }
    
    public int getVolta(){
        return volta;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    public int getQtdeCompetidores() {
        return qtdeCompetidores;
    }

    public void setQtdeCompetidores(int qtdeCompetidores) {
        this.qtdeCompetidores = qtdeCompetidores;
    }

    public boolean terminouCircuito(){
        return volta >= qtdeVoltas;
    }
    
    public void proximoPonto(){
        indicePosicaoMapaPontos++;
        if( indicePosicaoMapaPontos == mapaPontos.size() ){
            terminouVolta = true;
            indicePosicaoMapaPontos = 0;
            mapaPontos = corrida.getMapaPontos();
        }
            
        pontoAnterior = pontoAtual;
        pontoAtual = mapaPontos.get(indicePosicaoMapaPontos);
    }
    
    public Vector3f getPontoPosicao(){
        return pontoPosicao;
    }
    
    public int getIndicePosicao(){
        return indicePosicao;
    }
    
    public void proximoPontoPosicao(){
        indicePosicao++;
        if( indicePosicao > 0 && indicePosicao % corrida.getMapaControlePosicoes().size() == 0){
            voltaPontosPosicao++;
          //  indicePosicao = 0;
        }
            
        pontoPosicao = corrida.getMapaControlePosicoes().get(indicePosicao - (corrida.getMapaControlePosicoes().size() * voltaPontosPosicao));
    }

    public float getDistanciaProximaPosicao() {
        return distanciaProximaPosicao;
    }

    public void setDistanciaProximaPosicao(float distanciaProximaPosicao) {
        this.distanciaProximaPosicao = distanciaProximaPosicao;
    }
    
    public boolean ultimoColocado(){
        return posicao == qtdeCompetidores;
    }
    
    public boolean ultimaVolta(){
        return volta == qtdeVoltas - 1;
    }

    public void setTerminouCorrida(boolean terminouCorrida) {
        this.terminouCorrida = terminouCorrida;
    }

    public boolean terminouCorrida() {
        return terminouCorrida;
    }

    public boolean terminouVolta() {
        return terminouVolta;
    }

    public void adicionarVolta(){
        volta++;
        terminouVolta = false;
    }

    public boolean isIniciouCorrida() {
        return iniciouCorrida;
    }

    public void setIniciouCorrida(boolean iniciouCorrida) {
        this.iniciouCorrida = iniciouCorrida;
    }
    
}
