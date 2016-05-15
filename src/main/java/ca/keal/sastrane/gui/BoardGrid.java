package ca.keal.sastrane.gui;

import com.google.common.collect.ImmutableList;
import javafx.beans.property.ObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.scene.layout.GridPane;

import java.util.List;

// TODO move some (most?) functionality from GameController to BoardGrid

/** This class is simply a GridPane with extra style(s). */
public class BoardGrid extends GridPane implements Styleable {
    
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
    
    public enum Type {
        SQUARE, POINT;
    }
    
}