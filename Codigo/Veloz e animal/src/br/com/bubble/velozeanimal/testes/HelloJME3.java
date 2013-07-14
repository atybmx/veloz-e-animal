/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.bubble.velozeanimal.testes;

import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.audio.Environment;
import com.jme3.cinematic.Cinematic;
import com.jme3.cinematic.events.PositionTrack;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
 
/** Sample 1 - how to get started with the most simple JME 3 application.
 * Display a blue 3D cube and view from all sides by
 * moving the mouse and pressing the WASD keys. */
public class HelloJME3 extends SimpleApplication {
 
    public static void main(String[] args){
        HelloJME3 app = new HelloJME3();
        app.start(); // start the game
    }
    
        private CameraNode camNode;
 
    @Override
    public void simpleInitApp() {
        Node veiculoNode = (Node)assetManager.loadModel("Models/Itens/Melancia/melancia.j3o");
        
                Texture west = getAssetManager().loadTexture("Textures/skybox/arcoIris/arcoIrisLeft.jpg");
        Texture east = getAssetManager().loadTexture("Textures/skybox/arcoIris/arcoIrisRight.jpg");
        Texture north = getAssetManager().loadTexture("Textures/skybox/arcoIris/arcoIrisFront.jpg");
        Texture south = getAssetManager().loadTexture("Textures/skybox/arcoIris/arcoIrisBack.jpg");
        Texture up = getAssetManager().loadTexture("Textures/skybox/arcoIris/arcoIrisTop.jpg");
        Texture down = getAssetManager().loadTexture("Textures/skybox/arcoIris/arcoIrisBot.jpg");
        
        //Criando Spatial do SkyBox
        Spatial sky = SkyFactory.createSky(getAssetManager(), west, east, north, south, up, down);
     //   rootNode.attachChild(sky);
        
        rootNode.attachChild(veiculoNode);
        
    }
}