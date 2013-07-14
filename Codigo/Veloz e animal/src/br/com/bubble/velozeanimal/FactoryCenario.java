package br.com.bubble.velozeanimal;

import com.jme3.audio.AudioNode;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.PlaneCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import java.util.ArrayList;

/**
 *
 * @author atybmx
 */
public class FactoryCenario {
    
    public static Cenario festaAnimal(BubbleGame game){
        Node cena = (Node) game.getAssetManager().loadModel("Scenes/FestaAnimal/FestaAnimal.j3o");
        
        /**
         * BUSCANDO NODES FILHOS DA CENA, TODAS AS PARTES QUE FORMAM A CENA
         */
        Spatial arquibancada = cena.getChild("arquibancada");
        Spatial arquibancada2 = cena.getChild("arquibancada 2");
        Spatial cercaCentral = cena.getChild("cercaCentral");
        Spatial cercaUm = cena.getChild("cercaUm");
        Spatial cercaDois = cena.getChild("cercaDois");
        Spatial pista = cena.getChild("pista");
        Spatial terreno = cena.getChild("terreno");
        
        /**
         * CRIANDO OS COLISIONSHAPE E OS CONTROLES DE CADA NODE
         */
        //ARQUIBANCADA
        CollisionShape arquibancadaShape = new BoxCollisionShape(new Vector3f(25f,25,115f));
        RigidBodyControl arquibancadaControl = new RigidBodyControl( arquibancadaShape , 0 );
        arquibancada.addControl(arquibancadaControl);
        game.getPhysicsSpace().add(arquibancadaControl);
        
        //ARQUIBANCADA 2
        CollisionShape arquibancada2Shape = new BoxCollisionShape(new Vector3f(25f,25,115f));
        RigidBodyControl arquibancada2Control = new RigidBodyControl( arquibancada2Shape , 0 );
        arquibancada2.addControl(arquibancada2Control);
        game.getPhysicsSpace().add(arquibancada2Control);
        
        //CERCA CENTRAL
        CollisionShape cercaCentralShape = CollisionShapeFactory.createMeshShape(cercaCentral);
        RigidBodyControl cercaCentralControl = new RigidBodyControl( cercaCentralShape , 0 );
        cercaCentral.addControl(cercaCentralControl);
        game.getPhysicsSpace().add(cercaCentralControl);
        
        //CERCA UM
        CollisionShape cercaUmShape = CollisionShapeFactory.createMeshShape(cercaUm);
        RigidBodyControl cercaUmControl = new RigidBodyControl( cercaUmShape , 0 );
        cercaUm.addControl(cercaUmControl);
        game.getPhysicsSpace().add(cercaUmControl);
        
        //CERCA DOIS
        CollisionShape cercaDoisShape = CollisionShapeFactory.createMeshShape(cercaDois);
        RigidBodyControl cercaDoisControl = new RigidBodyControl( cercaDoisShape , 0 );
        cercaDois.addControl(cercaDoisControl);
        game.getPhysicsSpace().add(cercaDoisControl);
        
        //PISTA
        CollisionShape pistaShape = CollisionShapeFactory.createMeshShape(pista);
        RigidBodyControl pistaControl = new RigidBodyControl( pistaShape , 0 );
        pista.addControl(pistaControl);
        pistaControl.setFriction(1f);
        game.getPhysicsSpace().add(pistaControl);
        //pistaControl.setPhysicsLocation(new Vector3f(0,-.0f,0));
        //pista.move(0, -.3f, 0);
        
        //TERRENO
        CollisionShape terrenoShape = CollisionShapeFactory.createDynamicMeshShape(terreno);
        RigidBodyControl terrenoControl = new RigidBodyControl( terrenoShape , 0 );
        terreno.addControl(terrenoControl);
        terrenoControl.setFriction(0f);
        game.getPhysicsSpace().add(terrenoControl);
        
        
        /**
         * Criando o CENARIO
         */
        Cenario cenario = new Cenario(game,cena);
        
        /**
         * AJUSTANDO AS POSICOES INICIAIS NA CENA
         */
        float anguloRotacaoPosicaoInicial = 270f / 180f * FastMath.PI;
        
        cenario.setPosicaoInicial1(new Vector3f(10.46f , .3f, -95.11f));
        cenario.setRotacaoInicial1(new Quaternion().fromAngleAxis(anguloRotacaoPosicaoInicial, Vector3f.UNIT_Y));
        
        cenario.setPosicaoInicial2(new Vector3f(24.49f , .3f, -106.67f));
        cenario.setRotacaoInicial2(new Quaternion().fromAngleAxis(anguloRotacaoPosicaoInicial, Vector3f.UNIT_Y));
        
        cenario.setPosicaoInicial3(new Vector3f(40.82f , .3f, -95.11f));
        cenario.setRotacaoInicial3(new Quaternion().fromAngleAxis(anguloRotacaoPosicaoInicial, Vector3f.UNIT_Y));
        
        cenario.setPosicaoInicial4(new Vector3f(54.86f , .3f, -106.67f));
        cenario.setRotacaoInicial4(new Quaternion().fromAngleAxis(anguloRotacaoPosicaoInicial, Vector3f.UNIT_Y));
        
        /**
         * CRIANDO O SKY
         */
        Texture west = game.getAssetManager().loadTexture("Textures/skybox/arcoIris/arcoIrisLeft.jpg");
        Texture east = game.getAssetManager().loadTexture("Textures/skybox/arcoIris/arcoIrisRight.jpg");
        Texture north = game.getAssetManager().loadTexture("Textures/skybox/arcoIris/arcoIrisFront.jpg");
        Texture south = game.getAssetManager().loadTexture("Textures/skybox/arcoIris/arcoIrisBack.jpg");
        Texture up = game.getAssetManager().loadTexture("Textures/skybox/arcoIris/arcoIrisTop.jpg");
        Texture down = game.getAssetManager().loadTexture("Textures/skybox/arcoIris/arcoIrisBot.jpg");
        
        Spatial sky = SkyFactory.createSky(game.getAssetManager(), west, east, north, south, up, down);
        cenario.setSky(sky);
        
        /**
         * CRIANDO A BGM DA CORRIDA
         */
        AudioNode bgm = new AudioNode(game.getAssetManager(), "Sounds/BGM/festaAnimal.ogg", true);
        cenario.setMusica(bgm);
        
        /**
         * CRIANDO O MAPA DE PONTOS DE POSICAO DO VEICULO
         */
        ArrayList<Vector3f> mapaPontos = new ArrayList<Vector3f>();
        mapaPontos.add(new Vector3f(-19.601479f, 0.115657985f, -110.81742f));
        mapaPontos.add(new Vector3f(-43.77218f, 0.11565866f, -110.59435f));
        mapaPontos.add(new Vector3f(-85.09774f, 0.1156586f, -110.239876f));
        mapaPontos.add(new Vector3f(-110.110466f, 0.114731185f, -109.46389f));
        mapaPontos.add(new Vector3f(-140.80212f, 0.11565864f, -97.23197f));
        mapaPontos.add(new Vector3f(-163.53792f, 0.11565866f, -77.06397f));
        mapaPontos.add(new Vector3f(-178.47926f, 0.115659624f, -48.929688f));
        mapaPontos.add(new Vector3f(-181.0497f, 0.1156575f, -31.179634f));
        mapaPontos.add(new Vector3f(-180.88806f, 0.11565982f, 13.925032f));
        mapaPontos.add(new Vector3f(-180.97566f, 0.114859626f, 37.017246f));
        mapaPontos.add(new Vector3f(-180.85645f, 0.11565859f, 77.70923f));
        mapaPontos.add(new Vector3f(-173.12476f, 0.11565867f, 113.74165f));
        mapaPontos.add(new Vector3f(-148.36551f, 0.11565706f, 141.69064f));
        mapaPontos.add(new Vector3f(-109.433136f, 0.11565347f, 146.32672f));
        mapaPontos.add(new Vector3f(-67.85325f, 0.11565809f, 146.58824f));
        mapaPontos.add(new Vector3f(-43.96286f, 0.115645766f, 137.06358f));
        mapaPontos.add(new Vector3f(-24.612482f, 0.11565805f, 108.25723f));
        mapaPontos.add(new Vector3f(-20.06039f, 0.115509525f, 72.002144f));
        mapaPontos.add(new Vector3f(-12.794688f, 0.1154121f, 44.36909f));
        mapaPontos.add(new Vector3f(0.74372655f, 0.11504517f, 30.994148f));
        mapaPontos.add(new Vector3f(20.69236f, 0.115658596f, 35.56947f));
        mapaPontos.add(new Vector3f(32.79676f, 0.115657866f, 55.273956f));
        mapaPontos.add(new Vector3f(35.13487f, 0.115658574f, 89.91097f));
        mapaPontos.add(new Vector3f(43.899475f, 0.11565862f, 116.602486f));
        mapaPontos.add(new Vector3f(61.17688f, 0.11565787f, 137.2427f));
        mapaPontos.add(new Vector3f(92.961975f, 0.11565861f, 147.69568f));
        mapaPontos.add(new Vector3f(148.99112f, 0.11565904f, 147.00757f));
        mapaPontos.add(new Vector3f(174.60414f, 0.11566014f, 134.42812f));
        mapaPontos.add(new Vector3f(189.63945f, 0.115662046f, 107.648865f));
        mapaPontos.add(new Vector3f(195.99602f, 0.115588546f, 55.61645f));
        mapaPontos.add(new Vector3f(195.8731f, 0.11565122f, -12.017117f));
        mapaPontos.add(new Vector3f(191.46214f, 0.11565815f, -52.583076f));
        mapaPontos.add(new Vector3f(170.90178f, 0.1156593f, -87.67024f));
        mapaPontos.add(new Vector3f(129.527f, 0.11561854f, -109.315186f));
        mapaPontos.add(new Vector3f(55.443115f, 0.11564887f, -110.208275f));
        mapaPontos.add(new Vector3f(15.0956955f, 0.115657836f, -110.36252f));
        cenario.addMapaPontos(mapaPontos);
        
        //MAPA DE PONTOS 2
        ArrayList<Vector3f> mapaPontos2 = new ArrayList<Vector3f>();
        mapaPontos2.add(new Vector3f(-14.363189f, 0.11565861f, -100.60694f));
        mapaPontos2.add(new Vector3f(-28.795446f, 0.11558632f, -100.79085f));
        mapaPontos2.add(new Vector3f(-82.101616f, 0.11565859f, -101.53317f));
        mapaPontos2.add(new Vector3f(-124.01296f, 0.1156585f, -98.29263f));
        mapaPontos2.add(new Vector3f(-156.67148f, 0.11562674f, -69.99881f));
        mapaPontos2.add(new Vector3f(-171.0456f, 0.11566411f, -42.61123f));
        mapaPontos2.add(new Vector3f(-172.8063f, 0.1155114f, -16.315674f));
        mapaPontos2.add(new Vector3f(-172.46223f, 0.1156586f, 64.72657f));
        mapaPontos2.add(new Vector3f(-170.37425f, 0.11542434f, 100.328926f));
        mapaPontos2.add(new Vector3f(-157.75308f, 0.11564584f, 124.560265f));
        mapaPontos2.add(new Vector3f(-136.20691f, 0.11563283f, 137.48819f));
        mapaPontos2.add(new Vector3f(-108.37572f, 0.11566313f, 139.35834f));
        mapaPontos2.add(new Vector3f(-75.03466f, 0.11565855f, 139.21898f));
        mapaPontos2.add(new Vector3f(-52.188232f, 0.11563093f, 132.67987f));
        mapaPontos2.add(new Vector3f(-39.09508f, 0.11564321f, 119.70594f));
        mapaPontos2.add(new Vector3f(-30.349255f, 0.11563247f, 92.46682f));
        mapaPontos2.add(new Vector3f(-26.966597f, 0.115737125f, 59.821804f));
        mapaPontos2.add(new Vector3f(-12.274006f, 0.11565736f, 35.2357f));
        mapaPontos2.add(new Vector3f(3.8149576f, 0.11565653f, 23.35045f));
        mapaPontos2.add(new Vector3f(21.925068f, 0.11565041f, 28.066273f));
        mapaPontos2.add(new Vector3f(38.554092f, 0.11566028f, 51.25905f));
        mapaPontos2.add(new Vector3f(43.64738f, 0.11565318f, 80.945946f));
        mapaPontos2.add(new Vector3f(50.23948f, 0.115658335f, 110.83028f));
        mapaPontos2.add(new Vector3f(62.533978f, 0.11566027f, 129.20291f));
        mapaPontos2.add(new Vector3f(92.20164f, 0.11575769f, 138.92918f));
        mapaPontos2.add(new Vector3f(134.0891f, 0.11565859f, 140.13109f));
        mapaPontos2.add(new Vector3f(156.18787f, 0.1140955f, 135.42754f));
        mapaPontos2.add(new Vector3f(185.46094f, 0.11572659f, 94.34296f));
        mapaPontos2.add(new Vector3f(186.77205f, 0.115762435f, 61.239872f));
        mapaPontos2.add(new Vector3f(187.55705f, 0.11565594f, 2.3020706f));
        mapaPontos2.add(new Vector3f(184.32181f, 0.115658596f, -44.309776f));
        mapaPontos2.add(new Vector3f(171.98393f, 0.11565869f, -74.50261f));
        mapaPontos2.add(new Vector3f(142.50905f, 0.11565863f, -96.3233f));
        mapaPontos2.add(new Vector3f(93.59516f, 0.11576236f, -101.339424f));
        mapaPontos2.add(new Vector3f(-1.4633558f, 0.11553803f, -103.00561f));
        cenario.addMapaPontos(mapaPontos2);
        
        //MAPA DE PONTOS 3
        ArrayList<Vector3f> mapaPontos3 = new ArrayList<Vector3f>();
        mapaPontos3.add(new Vector3f(-20.798765f,0.11564911f,-93.307205f));
        mapaPontos3.add(new Vector3f(-41.446747f,0.11565989f,-93.55811f));
        mapaPontos3.add(new Vector3f(-94.31014f,0.11565858f,-92.029144f));
        mapaPontos3.add(new Vector3f(-132.72276f,0.115621395f,-85.00168f));
        mapaPontos3.add(new Vector3f(-149.8203f,0.1156585f,-69.55548f));
        mapaPontos3.add(new Vector3f(-164.9771f,0.1147027f,-40.48887f));
        mapaPontos3.add(new Vector3f(-163.84232f,0.1156586f,-7.29994f));
        mapaPontos3.add(new Vector3f(-162.78755f,0.115658626f,49.094204f));
        mapaPontos3.add(new Vector3f(-158.27528f,0.115658574f,99.99687f));
        mapaPontos3.add(new Vector3f(-148.68546f,0.11505438f,121.75286f));
        mapaPontos3.add(new Vector3f(-126.08356f,0.11558683f,129.6444f));
        mapaPontos3.add(new Vector3f(-95.899574f,0.1156581f,129.95312f));
        mapaPontos3.add(new Vector3f(-69.95518f,0.115658f,128.98637f));
        mapaPontos3.add(new Vector3f(-55.0275f,0.115658924f,124.221886f));
        mapaPontos3.add(new Vector3f(-41.59112f,0.115658f,105.4023f));
        mapaPontos3.add(new Vector3f(-37.336327f,0.11563538f,81.55893f));
        mapaPontos3.add(new Vector3f(-34.939953f,0.11565861f,58.431118f));
        mapaPontos3.add(new Vector3f(-17.865604f,0.11566082f,27.101154f));
        mapaPontos3.add(new Vector3f(15.523681f,0.1156586f,16.851793f));
        mapaPontos3.add(new Vector3f(42.27782f,0.11563167f,36.189278f));
        mapaPontos3.add(new Vector3f(53.56898f,0.11565179f,65.072044f));
        mapaPontos3.add(new Vector3f(55.57115f,0.115658596f,93.52986f));
        mapaPontos3.add(new Vector3f(61.83243f,0.115637966f,115.57017f));
        mapaPontos3.add(new Vector3f(78.45361f,0.11505812f,128.9857f));
        mapaPontos3.add(new Vector3f(99.62889f,0.115662284f,130.72554f));
        mapaPontos3.add(new Vector3f(139.1209f,0.11566184f,129.73608f));
        mapaPontos3.add(new Vector3f(155.66988f,0.11565993f,127.13036f));
        mapaPontos3.add(new Vector3f(172.20888f,0.11565993f,110.13508f));
        mapaPontos3.add(new Vector3f(176.44997f,0.11564839f,78.10389f));
        mapaPontos3.add(new Vector3f(177.77834f,0.115658656f,51.25429f));
        mapaPontos3.add(new Vector3f(178.02142f,0.11563024f,-10.947517f));
        mapaPontos3.add(new Vector3f(177.55618f,0.11565859f,-36.80026f));
        mapaPontos3.add(new Vector3f(173.16331f,0.11565856f,-56.74493f));
        mapaPontos3.add(new Vector3f(157.24464f,0.115658015f,-78.52003f));
        mapaPontos3.add(new Vector3f(137.63014f,0.11565863f,-89.54949f));
        mapaPontos3.add(new Vector3f(116.697296f,0.115576915f,-92.958305f));
        mapaPontos3.add(new Vector3f(70.75347f,0.11565847f,-92.20877f));
        mapaPontos3.add(new Vector3f(6.45759f,0.115658134f,-91.855606f));
        cenario.addMapaPontos(mapaPontos3);
        
        /**
         * CRIANDO MAPA DE CONTROLE DA POSICAO DOS VEICULOS
         */
        ArrayList<Vector3f> mapaControlePosicoes = new ArrayList<Vector3f>();
        mapaControlePosicoes.add(new Vector3f(-66.62703f,0.115523234f,-102.0935f));
mapaControlePosicoes.add(new Vector3f(-72.12775f,0.11565632f,-101.90974f));
mapaControlePosicoes.add(new Vector3f(-80.37882f,0.11565859f,-101.63412f));
mapaControlePosicoes.add(new Vector3f(-89.85228f,0.11565861f,-101.317665f));
mapaControlePosicoes.add(new Vector3f(-99.32574f,0.115658596f,-101.00121f));
mapaControlePosicoes.add(new Vector3f(-109.71598f,0.11565859f,-100.65414f));
mapaControlePosicoes.add(new Vector3f(-127.74971f,0.11561929f,-97.03901f));
mapaControlePosicoes.add(new Vector3f(-131.69287f,0.11477911f,-94.7862f));
mapaControlePosicoes.add(new Vector3f(-135.48004f,0.11565968f,-91.9424f));
mapaControlePosicoes.add(new Vector3f(-145.48552f,0.11472022f,-83.84756f));
mapaControlePosicoes.add(new Vector3f(-151.61395f,0.11568423f,-77.127785f));
mapaControlePosicoes.add(new Vector3f(-158.70197f,0.115661405f,-67.25884f));
mapaControlePosicoes.add(new Vector3f(-164.23965f,0.115658656f,-57.685856f));
mapaControlePosicoes.add(new Vector3f(-169.75824f,0.11563198f,-45.894283f));
mapaControlePosicoes.add(new Vector3f(-172.7035f,0.11565259f,-33.191357f));
mapaControlePosicoes.add(new Vector3f(-173.0517f,0.115658015f,-26.59891f));
mapaControlePosicoes.add(new Vector3f(-173.15326f,0.11565863f,-23.879915f));
mapaControlePosicoes.add(new Vector3f(-173.48766f,0.11575128f,-14.983601f));
mapaControlePosicoes.add(new Vector3f(-173.75958f,0.11576242f,-7.9875107f));
mapaControlePosicoes.add(new Vector3f(-174.26114f,0.11576243f,4.434176f));
mapaControlePosicoes.add(new Vector3f(-173.27823f,0.115786664f,44.874325f));
mapaControlePosicoes.add(new Vector3f(-173.45302f,0.115759805f,58.57087f));
mapaControlePosicoes.add(new Vector3f(-172.03195f,0.115644135f,79.0021f));
mapaControlePosicoes.add(new Vector3f(-172.16483f,0.115658626f,86.19248f));
mapaControlePosicoes.add(new Vector3f(-170.46371f,0.11565277f,98.65898f));
mapaControlePosicoes.add(new Vector3f(-163.49886f,0.115651324f,115.69263f));
mapaControlePosicoes.add(new Vector3f(-157.57083f,0.11557245f,123.430016f));
mapaControlePosicoes.add(new Vector3f(-149.55038f,0.115188584f,130.69797f));
mapaControlePosicoes.add(new Vector3f(-141.98505f,0.115686275f,135.44547f));
mapaControlePosicoes.add(new Vector3f(-134.07443f,0.11569827f,138.61009f));
mapaControlePosicoes.add(new Vector3f(-98.84996f,0.11565326f,136.98607f));
mapaControlePosicoes.add(new Vector3f(-76.442505f,0.11565839f,138.16208f));
mapaControlePosicoes.add(new Vector3f(-64.662155f,0.11581067f,136.78128f));
mapaControlePosicoes.add(new Vector3f(-45.74216f,0.11576439f,125.32702f));
mapaControlePosicoes.add(new Vector3f(-33.91043f,0.11576517f,108.193596f));
mapaControlePosicoes.add(new Vector3f(-30.699348f,0.11572853f,93.70429f));
mapaControlePosicoes.add(new Vector3f(-28.594408f,0.115762405f,77.27055f));
mapaControlePosicoes.add(new Vector3f(-25.748535f,0.115762435f,55.31938f));
mapaControlePosicoes.add(new Vector3f(-12.888272f,0.11576555f,39.77672f));
mapaControlePosicoes.add(new Vector3f(3.1970649f,0.1157624f,25.40009f));
mapaControlePosicoes.add(new Vector3f(22.74852f,0.11383204f,25.44287f));
mapaControlePosicoes.add(new Vector3f(32.589825f,0.115710035f,40.656513f));
mapaControlePosicoes.add(new Vector3f(38.1575f,0.11576255f,50.684998f));
mapaControlePosicoes.add(new Vector3f(40.095165f,0.1157675f,68.77122f));
mapaControlePosicoes.add(new Vector3f(40.859535f,0.11433421f,84.008514f));
mapaControlePosicoes.add(new Vector3f(46.289433f,0.11576675f,100.44163f));
mapaControlePosicoes.add(new Vector3f(54.447273f,0.11450158f,117.80385f));
mapaControlePosicoes.add(new Vector3f(65.819595f,0.11576047f,130.09631f));
mapaControlePosicoes.add(new Vector3f(86.98215f,0.115765475f,138.26915f));
mapaControlePosicoes.add(new Vector3f(111.083145f,0.11563217f,139.29134f));
mapaControlePosicoes.add(new Vector3f(137.42789f,0.11576242f,135.9334f));
mapaControlePosicoes.add(new Vector3f(156.9949f,0.115762435f,133.46617f));
mapaControlePosicoes.add(new Vector3f(175.29713f,0.11576055f,119.06661f));
mapaControlePosicoes.add(new Vector3f(182.43806f,0.11551743f,97.1677f));
mapaControlePosicoes.add(new Vector3f(181.96725f,0.11583097f,77.167496f));
mapaControlePosicoes.add(new Vector3f(185.38814f,0.11581908f,23.652056f));
mapaControlePosicoes.add(new Vector3f(186.56876f,0.115705445f,-7.662896f));
mapaControlePosicoes.add(new Vector3f(185.4722f,0.115534455f,-33.647278f));
mapaControlePosicoes.add(new Vector3f(181.931f,0.115494095f,-57.21968f));
mapaControlePosicoes.add(new Vector3f(173.58461f,0.115002684f,-72.21659f));
mapaControlePosicoes.add(new Vector3f(148.46706f,0.1157619f,-87.92464f));
mapaControlePosicoes.add(new Vector3f(110.523224f,0.115757f,-98.05222f));
mapaControlePosicoes.add(new Vector3f(71.52951f,0.11554448f,-100.38663f));
mapaControlePosicoes.add(new Vector3f(22.594555f,0.11571798f,-103.52089f));
mapaControlePosicoes.add(new Vector3f(9.081168f,0.115761936f,-101.4933f));
        cenario.setMapaControlePosicoes(mapaControlePosicoes);
        
        /**
         * CRIANDO O MAPA DE PONTOS PARA CRIACAO DOS ITENS
         */
        ArrayList<Vector3f> mapaItens = new ArrayList<Vector3f>();
        mapaItens.add(new Vector3f(16.068121f, 1, -94.96794f));
        mapaItens.add(new Vector3f(-7.741846f, 1, -95.45972f));
        mapaItens.add(new Vector3f(-23.833014f, 1, -100.17931f));
        mapaItens.add(new Vector3f(-163.53224f, 1, -37.725624f));
        mapaItens.add(new Vector3f(-168.01314f, 1, -17.123127f));
        mapaItens.add(new Vector3f(-177.95082f, 1, -0.18365109f));
        mapaItens.add(new Vector3f(-174.71455f, 1, 48.388687f));
        mapaItens.add(new Vector3f(-129.1284f, 1, 137.38806f));
        mapaItens.add(new Vector3f(-93.66581f, 1, 138.34274f));
        mapaItens.add(new Vector3f(-29.798517f, 1, 115.55469f));
        mapaItens.add(new Vector3f(-22.088335f, 1, 77.61598f));
        mapaItens.add(new Vector3f(52.476673f, 1, 54.63384f));
        mapaItens.add(new Vector3f(44.419113f, 1, 92.40849f));
        mapaItens.add(new Vector3f(89.75621f, 1, 145.51f));
        mapaItens.add(new Vector3f(118.37575f, 1, 140.6712f));
        mapaItens.add(new Vector3f(181.39948f, 1, 76.594345f));
        mapaItens.add(new Vector3f(186.95113f, 1, 51.657402f));
        mapaItens.add(new Vector3f(187.2413f, 1, -5.8223658f));
        mapaItens.add(new Vector3f(103.05027f, 1, -107.81743f));
        mapaItens.add(new Vector3f(73.91862f, 1, -105.114975f));
        mapaItens.add(new Vector3f(43.14968f, 1, -92.61026f));
        cenario.setMapaItens(mapaItens);
        
        /**
         * CRIANDO A LINHA DE CHEGADA
         */
        BoxCollisionShape chegadaShape = new BoxCollisionShape(new Vector3f(1f,3f,20f));
        GhostControl chegadaControl = new GhostControl(chegadaShape);
        Node chegada = new Node("chegada");
        chegada.addControl(chegadaControl);   
        cenario.setChegada(chegada);
        game.getPhysicsSpace().add(chegadaControl);
        chegada.setLocalTranslation(new Vector3f(-8f,0f,-100.5f));
        
        /**
         * Retornando o cenario com todos os componentes
         */
        return cenario;
    }
}
