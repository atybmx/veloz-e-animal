/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.bubble.velozeanimal.testes;

import br.com.bubble.velozeanimal.Veiculo;
import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.bullet.objects.VehicleWheel;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.shadow.BasicShadowRenderer;

public class TestFancyCar extends SimpleApplication implements ActionListener {

    private BulletAppState bulletAppState;
    private VehicleControl player;
    private Node carNode;
    private VehicleControl player2;
    private Node carNode2;
    private VehicleWheel fr, fl, br, bl;
    private Node node_fr, node_fl, node_br, node_bl;
    private float wheelRadius;
    private float steeringValue = 0;
    private float accelerationValue = 0;
    

    public static void main(String[] args) {
        TestFancyCar app = new TestFancyCar();
        app.start();
    }

    private void setupKeys() {
        inputManager.addMapping("Lefts", new KeyTrigger(KeyInput.KEY_H));
        inputManager.addMapping("Rights", new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping("Ups", new KeyTrigger(KeyInput.KEY_U));
        inputManager.addMapping("Downs", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("Space", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Reset", new KeyTrigger(KeyInput.KEY_RETURN));
        inputManager.addListener(this, "Lefts");
        inputManager.addListener(this, "Rights");
        inputManager.addListener(this, "Ups");
        inputManager.addListener(this, "Downs");
        inputManager.addListener(this, "Space");
        inputManager.addListener(this, "Reset");
    }

    @Override
    public void simpleInitApp() {
        bulletAppState = new BulletAppState();

        stateManager.attach(bulletAppState);
//        bulletAppState.getPhysicsSpace().enableDebug(assetManager);
        if (settings.getRenderer().startsWith("LWJGL")) {
            BasicShadowRenderer bsr = new BasicShadowRenderer(assetManager, 512);
            bsr.setDirection(new Vector3f(-0.5f, -0.3f, -0.3f).normalizeLocal());
            viewPort.addProcessor(bsr);
        }
        cam.setFrustumFar(150f);
        flyCam.setMoveSpeed(10);

        setupKeys();
        PhysicsTestHelper.createPhysicsTestWorld(rootNode, assetManager, bulletAppState.getPhysicsSpace());
//        setupFloor();
        buildPlayer();

        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.5f, -1f, -0.3f).normalizeLocal());
        rootNode.addLight(dl);

        dl = new DirectionalLight();
        dl.setDirection(new Vector3f(0.5f, -0.1f, 0.3f).normalizeLocal());
        rootNode.addLight(dl);
        bulletAppState.getPhysicsSpace().enableDebug(assetManager);
        
       // bulletAppState.getPhysicsSpace().setGravity(new Vector3f(0f,-30f,0f));
       // bulletAppState.getPhysicsSpace().applyGravity();
        
        constroiNodeCamera();
    }

    private PhysicsSpace getPhysicsSpace() {
        return bulletAppState.getPhysicsSpace();
    }

//    public void setupFloor() {
//        Material mat = assetManager.loadMaterial("Textures/Terrain/BrickWall/BrickWall.j3m");
//        mat.getTextureParam("DiffuseMap").getTextureValue().setWrap(WrapMode.Repeat);
////        mat.getTextureParam("NormalMap").getTextureValue().setWrap(WrapMode.Repeat);
////        mat.getTextureParam("ParallaxMap").getTextureValue().setWrap(WrapMode.Repeat);
//
//        Box floor = new Box(Vector3f.ZERO, 140, 1f, 140);
//        floor.scaleTextureCoordinates(new Vector2f(112.0f, 112.0f));
//        Geometry floorGeom = new Geometry("Floor", floor);
//        floorGeom.setShadowMode(ShadowMode.Receive);
//        floorGeom.setMaterial(mat);
//
//        PhysicsNode tb = new PhysicsNode(floorGeom, new MeshCollisionShape(floorGeom.getMesh()), 0);
//        tb.setLocalTranslation(new Vector3f(0f, -6, 0f));
////        tb.attachDebugShape(assetManager);
//        rootNode.attachChild(tb);
//        getPhysicsSpace().add(tb);
//    }

    private Geometry findGeom(Spatial spatial, String name) {
        if (spatial instanceof Node) {
            Node node = (Node) spatial;
            for (int i = 0; i < node.getQuantity(); i++) {
                Spatial child = node.getChild(i);
                Geometry result = findGeom(child, name);
                if (result != null) {
                    return result;
                }
            }
        } else if (spatial instanceof Geometry) {
            if (spatial.getName().startsWith(name)) {
                return (Geometry) spatial;
            }
        }
        return null;
    }

    private void buildPlayer() {/**
         /**
         * Massa do veiculo
         */
        float massa = 500;
        
        //Load model and get chassis Geometry
        carNode = (Node)getAssetManager().loadModel("Models/Personagens/Zapata/carro_zapata.j3o");
        carNode.setName("veiculo");
        //Cria um collisionShape baseado no chassi
        CollisionShape veiculoShapeModel = CollisionShapeFactory.createDynamicMeshShape(carNode.getChild("chassi"));
        
        CompoundCollisionShape compoundCollisionShape=new CompoundCollisionShape();
        BoxCollisionShape boxCollisionShape=new BoxCollisionShape(new Vector3f(1.5f,1f,2.3f));
        compoundCollisionShape.addChildShape(boxCollisionShape, new Vector3f(0,1f,0));
        
        //Cria uma instancia de Veiculo
        player = new VehicleControl(compoundCollisionShape, massa);
        Node node = new Node();
        carNode.addControl(player);
        
        /**
         * Configurando a suspens√£o para as rodas do veiculo.
         * Esses valores podem ser configurados separadamente em cada roda
         */
        float rigidez = 200f;//Quanto maior, mais o caro fica estavel de duro
        float compressaoValue = .2f; //(Deve ser menor que o amortecimento)
        float amortecimentoValue = .3f;
        player.setSuspensionCompression(compressaoValue * 2.0f * FastMath.sqrt(rigidez));
        player.setSuspensionDamping(amortecimentoValue * 2.0f * FastMath.sqrt(rigidez));
        player.setSuspensionStiffness(rigidez);
        player.setMaxSuspensionForce(10000.0f);

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
        Spatial wheelFrontLeft = carNode.getChild("WheelFrontLeft");
        player.addWheel(wheelFrontLeft,wheelFrontLeft.getLocalTranslation(),
                direcaoRoda, eixoRoda, tamanhoSuspensao, raio, true);

        /**
         * Roda Dianteira Direita do carro
         */
        Spatial wheelFrontRight = (Node) carNode.getChild("WheelFrontRight");
        player.addWheel(wheelFrontRight,wheelFrontRight.getLocalTranslation(),
                direcaoRoda, eixoRoda, tamanhoSuspensao, raio, true);

        /**
         * Roda Traseira Esquerda do carro
         */
        Spatial wheelBackLeft = (Node) carNode.getChild("WheelBackLeft");
        player.addWheel(wheelBackLeft,wheelBackLeft.getLocalTranslation(),
                direcaoRoda, eixoRoda, tamanhoSuspensao, raio, false);

        /**
         * Roda Traseira Direita do carro
         */
        Spatial wheelBackRight = (Node) carNode.getChild("WheelBackRight");
        player.addWheel(wheelBackRight,wheelBackRight.getLocalTranslation(),
                direcaoRoda, eixoRoda, tamanhoSuspensao, raio, false);
        
        player.setFriction(3f);
        player.setPhysicsLocation(new Vector3f(10,0,0));
        
        getRootNode().attachChild(carNode);
        getPhysicsSpace().add(player);
        player.setAngularFactor(0);
        //player.setGravity(new Vector3f(0f,-30f,0f));
    }
    

    public void onAction(String binding, boolean value, float tpf) {
        if (binding.equals("Lefts")) {
            if (value) {
                steeringValue += .5f;
            } else {
                steeringValue += -.5f;
            }
            player.steer(steeringValue);
        } else if (binding.equals("Rights")) {
            if (value) {
                steeringValue += -.5f;
            } else {
                steeringValue += .5f;
            }
            player.steer(steeringValue);
        } //note that our fancy car actually goes backwards..
        else if (binding.equals("Ups")) {
            if (value) {
                accelerationValue += 1500;
            } else {
                accelerationValue -= 1500;
            }
            player.accelerate(accelerationValue);
        } else if (binding.equals("Downs")) {
            if (value) {
                player.brake(40f);
            } else {
                player.brake(0f);
            }
        } else if (binding.equals("Reset")) {
            if (value) {
                System.out.println("Reset");
                player.setPhysicsLocation(new Vector3f(10,0,10));
                player.setPhysicsRotation(new Matrix3f());
                player.setLinearVelocity(Vector3f.ZERO);
                player.setAngularVelocity(Vector3f.ZERO);
                player.resetSuspension();
            } else {
            }
        }
    }
    
     private void constroiNodeCamera(){
        // Desabilitando a default FlyCamera
        getFlyByCamera().setEnabled(false);
        
        //CRIANDO A CAMERA NODE
        CameraNode camNode = new CameraNode("Camera Node", cam);
        //Copia todos os movimentos do jogador para a camera
        camNode.setControlDir(ControlDirection.SpatialToCamera);
        //Anexa a camera Node no jogador
        carNode.attachChild(camNode);
        //Posiciona a camera Node para atras do jogador
        camNode.setLocalTranslation(new Vector3f(0, 7, -20));
        //Rotaciona a camera Node olhando na direcao do jogador
        camNode.lookAt(carNode.getLocalTranslation().add(0, 6, 0), Vector3f.UNIT_Y);
    }


    @Override
    public void simpleUpdate(float tpf) {
        //cam.lookAt(carNode.getWorldTranslation(), Vector3f.UNIT_Y);
    }
}