/*
 * Copyright (C) 2016 Ryan Dancy
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the 
 * specific language governing permissions and limitations under the License.
 */

package ca.keal.sastrane.gui;

import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.gui.audio.SoundEffects;
import ca.keal.sastrane.main.Main;
import ca.keal.sastrane.util.I18n;
import ca.keal.sastrane.util.Resource;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import lombok.SneakyThrows;

import javax.annotation.Nullable;
import java.io.IOException;

public final class GuiUtils {
    
    private GuiUtils() {
        throw new RuntimeException("ca.keal.sastrane.gui.GuiUtils is a utility class and cannot be instantiated.");
    }
    
    public static Parent loadFXML(Resource resource) throws IOException {
        return FXMLLoader.load(resource.get(), I18n.getBundle());
    }
    
    public static FXMLLoader getFXMLLoader(Resource resource) {
        return new FXMLLoader(resource.get(), I18n.getBundle());
    }
    
    public static Scene getScene(Parent parent, double width, double height) {
        return new Scene(parent, width, height);
    }
    
    public static Scene getScene(Parent parent, Scene previous) {
        return getScene(parent, previous.getWidth(), previous.getHeight());
    }
    
    public static Scene getScene(Parent parent) {
        return new Scene(parent);
    }
    
    public static Scene getScene(Resource resource, double width, double height) throws IOException {
        return getScene(loadFXML(resource), width, height);
    }
    
    public static Scene getScene(Resource resource, Scene previous) throws IOException {
        return getScene(loadFXML(resource), previous);
    }
    
    public static Scene getScene(Resource resource) throws IOException {
        return getScene(loadFXML(resource));
    }
    
    /** Passes title through I18n.localize("gui.title") before setting */
    public static void setTitle(String title) {
        Main.getStage().setTitle(I18n.localize("gui.title", title));
    }
    
    public static void setTitleToDefault() {
        Main.getStage().setTitle(I18n.localize("gui.title.default"));
    }
    
    /**
     * For boilerplate reduction. Usecase is to go to another scene on an event.
     */
    @SneakyThrows
    public static void goTo(Resource fxml) {
        SoundEffects.play("click");
        Main.getStage().setScene(getScene(fxml, Main.getStage().getScene()));
    }
    
    public static void loadCustom(Node custom, Resource resource) throws IOException {
        FXMLLoader loader = GuiUtils.getFXMLLoader(resource);
        loader.setRoot(custom);
        loader.setController(custom);
        loader.load();
    }
    
    // http://stackoverflow.com/a/20656861
    @Nullable
    public static Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (Integer.valueOf(col).equals(GridPane.getColumnIndex(node))
                    && Integer.valueOf(row).equals(GridPane.getRowIndex(node))) {
                return node;
            }
        }
        return null;
    }
    
    public static Node lookup(Node node, Square square) {
        return lookup(node.getScene(), square);
    }
    
    public static Node lookup(Scene scene, Square square) {
        return scene.lookup(getLookupString(square));
    }
    
    public static String getLookupString(Square square) {
        return String.format(".x%d.y%d", square.getX(), square.getY());
    }
    
}