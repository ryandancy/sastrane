package ca.keal.sastrane.gui;

import com.google.common.collect.ImmutableList;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Consumer;

// TODO move some (most?) functionality from GameController to BoardGrid

/** This class is simply a GridPane with extra style(s). */
public class BoardGrid extends GridPane {
    
    private ObjectProperty<Type> gridType = new StyleableObjectProperty<Type>() {
        
        @Override
        public Object getBean() {
            return BoardGrid.this;
        }
        
        @Override
        public String getName() {
            return "gridType";
        }
        
        @Override
        public CssMetaData<BoardGrid, Type> getCssMetaData() {
            return GRID_TYPE;
        }
        
        @Override
        public void set(Type v) {
            super.set(v);
            v.update(BoardGrid.this);
        }
        
    };
    
    @SuppressWarnings("unchecked")
    private static final CssMetaData<BoardGrid, Type> GRID_TYPE = new CssMetaData<BoardGrid, Type>(
            "-sastrane-grid-type", (StyleConverter<String, Type>) StyleConverter.getEnumConverter(Type.class),
            Type.SQUARE) {
        
        @Override
        public boolean isSettable(BoardGrid styleable) {
            return !styleable.gridType.isBound();
        }
        
        @Override
        public StyleableProperty<Type> getStyleableProperty(BoardGrid styleable) {
            return (StyleableProperty<Type>) styleable.gridTypeProperty();
        }
        
    };
    
    private static final List<CssMetaData<? extends Styleable, ?>> CSS_META_DATA = ImmutableList.of(GRID_TYPE);
    
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return CSS_META_DATA;
    }
    
    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }
    
    public final Type getGridType() {
        return gridType.get();
    }
    
    public final void setGridType(Type gridType) {
        this.gridType.set(gridType);
    }
    
    public final ObjectProperty<Type> gridTypeProperty() {
        return gridType;
    }
    
    @RequiredArgsConstructor
    public enum Type {
        
        SQUARE(grid -> {}),
        POINT(grid -> {
            DoubleBinding gridTranslate = ((StackPane) grid.getChildren().get(0)).widthProperty().negate();
            DoubleBinding translate = gridTranslate.divide(2);
            
            for (Node child : grid.getChildren()) {
                Node img = child.lookup(".img");
                if (img == null) continue;
                img.translateXProperty().bind(translate);
                img.translateYProperty().bind(translate);
                
                Node overlay = child.lookup(".overlay");
                overlay.translateXProperty().bind(translate);
                overlay.translateYProperty().bind(translate);
            }
            
            grid.translateXProperty().bind(gridTranslate);
            grid.translateYProperty().bind(gridTranslate);
            
            grid.lookupAll(".square.maxx").forEach(s -> s.setStyle("-fx-border-color: transparent transparent "
                    + "transparent black; -fx-background-color: transparent;"));
            grid.lookupAll(".square.maxy").forEach(s -> s.setStyle("-fx-border-color: black transparent transparent "
                    + "transparent; -fx-background-color: transparent;"));
            grid.lookup(".square.maxx.maxy").setStyle("-fx-border-color: transparent;");
        });
        
        private final Consumer<BoardGrid> update;
        
        public void update(BoardGrid grid) {
            update.accept(grid);
        }
        
    }
    
}