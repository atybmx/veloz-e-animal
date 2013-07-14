/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.bubble.velozeanimal.testes;

import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingBox;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author atybmx
 */
public class BuscaExtendModel extends SimpleApplication{
     public static void main(String[] args) {
        BuscaExtendModel app = new BuscaExtendModel();
        app.start();
    }
    
    private static Geometry findGeom(Spatial spatial, String name) {
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

    @Override
    public void simpleInitApp() {
        Node node = (Node)getAssetManager().loadModel("Models/Itens/Banana/cascaBanana.j3o");
        Geometry mesh = findGeom(node.getChild("CascaBanana"), "CascaBananaMesh");
        BoundingBox box = (BoundingBox) mesh.getModelBound();
        System.out.println(box.getXExtent());
        System.out.println(box.getYExtent());
        System.out.println(box.getZExtent());
    }
    
}
