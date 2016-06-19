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
import ca.keal.sastrane.util.Resource;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

import javax.annotation.Nullable;
import java.io.IOException;

public interface GuiUtils {
    
    Parent loadFXML(Resource resource) throws IOException;
    
    FXMLLoader getFXMLLoader(Resource resource);
    
    Scene getScene(Parent parent, double width, double height);
    
    Scene getScene(Parent parent, Scene previous);
    
    Scene getScene(Parent parent);
    
    Scene getScene(Resource resource, double width, double height) throws IOException;
    
    Scene getScene(Resource resource, Scene previous) throws IOException;
    
    Scene getScene(Resource resource) throws IOException;
    
    void setTitle(String title);
    
    void setTitleToDefault();
    
    void goTo(Resource fxml);
    
    void loadCustom(Node custom, Resource resource) throws IOException;
    
    // http://stackoverflow.com/a/20656861
    @Nullable Node getNodeFromGridPane(GridPane gridPane, int col, int row);
    
    Node lookup(Node node, Square square);
    
    Node lookup(Scene scene, Square square);
    
    String getLookupString(Square square);
}
