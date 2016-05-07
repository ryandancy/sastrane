package ca.keal.sastrane.gui;

import ca.keal.sastrane.util.I18n;
import ca.keal.sastrane.util.Resource;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class ChangeLangController implements Initializable {
    
    @FXML private FlowPane langs;
    
    @Override
    @SneakyThrows
    public void initialize(URL location, ResourceBundle resources) {
        // Detect all supported languages from langs.properties, add to langs flowpane
        Properties supportedLangs = new Properties();
        supportedLangs.load(new Resource("ca.keal.sastrane.i18n", "langs.properties").get().openStream());
        
        supportedLangs.forEach((lang, name) -> {
            Label label = new Label((String) name);
            label.getStyleClass().add("lang");
            label.setOnMouseClicked(e -> changeLang(e, (String) lang));
            langs.getChildren().add(label);
        });
    }
    
    private void changeLang(Event e, String langCode) {
        I18n.setLocale(Locale.forLanguageTag(langCode));
        onBack(e);
    }
    
    @FXML
    @SneakyThrows
    private void onBack(Event e) {
        // Back to the main menu
        GuiUtils.getStage(e).setScene(GuiUtils.getScene(new Resource("ca.keal.sastrane.gui", "main-menu.fxml"),
                GuiUtils.getStage(e).getScene()));
    }
    
}