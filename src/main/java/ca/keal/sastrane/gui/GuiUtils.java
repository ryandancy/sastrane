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

import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.gui.audio.SoundEffects;
import ca.keal.sastrane.main.Main;
import ca.keal.sastrane.util.I18n;
import ca.keal.sastrane.util.Resource;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.util.Builder;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.ToString;

import javax.annotation.Nullable;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@EqualsAndHashCode
@ToString
public class GuiUtils {
    
    private final I18n i18n;
    private final SoundEffects soundFX;
    private final Injector injector;
    
    public Parent loadFXML(Resource resource) throws IOException {
        return getFXMLLoader(resource).load();
    }
    
    public FXMLLoader getFXMLLoader(Resource resource) {
        FXMLLoader loader = guicifyLoader(new FXMLLoader());
        loader.setLocation(resource.get());
        loader.setResources(i18n.getBundle());
        return loader;
    }
    
    /**
     * Makes {@code loader} safe for Guice. Thanks, 
     * <a href="http://pscheidl.cz/index.php/blog/6-using-javafx8-with-dependency-injection">Pavel
     * Pscheidl</a>.
     */
    private FXMLLoader guicifyLoader(FXMLLoader loader) {
        loader.setControllerFactory(injector::getInstance);
        loader.setBuilderFactory(cls -> {
            if (cls != null && cls.isAnnotationPresent(FXMLComponent.class)) {
                try {
                    // We use fx-guice's FXMLComponentBuilder to guicify the builder
                    // Unfortunately it's package-private, so reflection is needed
                    // TODO find some non-reflection way of doing this
                    @SuppressWarnings("unchecked")
                    Class<Builder<?>> builderCls = (Class<Builder<?>>) Class.forName(
                            "com.cathive.fx.guice.fxml.FXMLComponentBuilder");
                    Constructor<Builder<?>> builderCtr = builderCls.getDeclaredConstructor(Injector.class, Class.class);
                    builderCtr.setAccessible(true);
                    return builderCtr.newInstance(injector, cls);
                } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
                        IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            } else {
                return new JavaFXBuilderFactory().getBuilder(cls);
            }
        });
        return loader;
    }
    
    public Scene getScene(Parent parent, double width, double height) {
        return new Scene(parent, width, height);
    }
    
    public Scene getScene(Parent parent, Scene previous) {
        return getScene(parent, previous.getWidth(), previous.getHeight());
    }
    
    public Scene getScene(Parent parent) {
        return new Scene(parent);
    }
    
    public Scene getScene(Resource resource, double width, double height) throws IOException {
        return getScene(loadFXML(resource), width, height);
    }
    
    public Scene getScene(Resource resource, Scene previous) throws IOException {
        return getScene(loadFXML(resource), previous);
    }
    
    public Scene getScene(Resource resource) throws IOException {
        return getScene(loadFXML(resource));
    }
    
    /** Passes title through I18n.localize("gui.title") before setting */
    public void setTitle(String title) {
        Main.getStage().setTitle(i18n.localize("gui.title", title));
    }
    
    public void setTitleToDefault() {
        Main.getStage().setTitle(i18n.localize("gui.title.default"));
    }
    
    /**
     * For boilerplate reduction. Usecase is to go to another scene on an event.
     */
    @SneakyThrows
    public void goTo(Resource fxml) {
        soundFX.play("click");
        Main.getStage().setScene(getScene(fxml, Main.getStage().getScene()));
    }
    
    public void loadCustom(Node custom, Resource resource) throws IOException {
        FXMLLoader loader = getFXMLLoader(resource);
        loader.setRoot(custom);
        loader.setController(custom);
        loader.load();
    }
    
    // http://stackoverflow.com/a/20656861
    @Nullable
    public Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (Integer.valueOf(col).equals(GridPane.getColumnIndex(node))
                    && Integer.valueOf(row).equals(GridPane.getRowIndex(node))) {
                return node;
            }
        }
        return null;
    }
    
    public Node lookup(Node node, Square square) {
        return lookup(node.getScene(), square);
    }
    
    public Node lookup(Scene scene, Square square) {
        return scene.lookup(getLookupString(square));
    }
    
    public String getLookupString(Square square) {
        return String.format(".square.x%d.y%d", square.getX(), square.getY());
    }
    
    public NumberBinding getCellSizeBinding(Board board, GridPane grid) {
        return Bindings.min(grid.widthProperty().divide(board.getMaxX() + 1),
                grid.heightProperty().divide(board.getMaxY() + 1));
    }
    
}