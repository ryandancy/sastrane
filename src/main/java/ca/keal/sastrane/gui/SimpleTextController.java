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

import ca.keal.sastrane.util.I18n;
import ca.keal.sastrane.util.Resource;
import com.google.common.io.Resources;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;

public class SimpleTextController extends GoBacker {
    
    @FXML private Label title;
    @FXML private Text text;
    
    public SimpleTextController() {
        super(new Resource("ca.keal.sastrane.gui", "settings.fxml"));
    }
    
    /** i18nTitle is passed through I18n.localize before setting the title */
    public void setTitle(String i18nTitle) {
        title.setText(I18n.localize(i18nTitle));
    }
    
    /**
     * To find the package of the resource, i18nFilename + ".package" is passed through I18n.localize. If that doesn't
     * exist, I18n.localize("text.package.default") is used.
     * <p>
     * To find the filename of the resource, i18nFilename is passed through I18n.localize.
     * <p>
     * These are combined into a Resource. All text is read from it and that text is used. The charset is assumed to be
     * UTF-8.
     */
    @SneakyThrows
    public void setText(String i18nFilename) {
        String pkg = I18n.localize(i18nFilename + ".package");
        if (pkg.equals(i18nFilename + ".package")) {
            pkg = I18n.localize("text.package.default");
        }
        
        String filename = I18n.localize(i18nFilename);
        
        Resource resource = new Resource(pkg, filename);
        text.setText(Resources.toString(resource.get(), StandardCharsets.UTF_8));
    }
    
}