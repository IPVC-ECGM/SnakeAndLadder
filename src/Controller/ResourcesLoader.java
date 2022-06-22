package Controller;

import javafx.scene.image.Image;

public class ResourcesLoader {
    static ResourcesLoader resourceLoader = new ResourcesLoader();
    public static Image getImage(String filename) {
        return new Image(resourceLoader.getClass().getClassLoader().getResource("img/" + filename).toString());
    }
}