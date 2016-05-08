package ca.keal.sastrane.api;

import ca.keal.sastrane.api.piece.OwnedPiece;
import ca.keal.sastrane.api.piece.OwnedPieceFactory;
import ca.keal.sastrane.api.piece.Piece;
import javafx.collections.FXCollections;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.mock;

@Test
public class BoardTest {
    
    // Board(List<String>, Map<Character, OwnedPieceFactory>)
    
    public void testBuilderConstructor() {
        Piece a = mock(Piece.class);
        Piece b = mock(Piece.class);
        Player player = mock(Player.class);
        Board board = Board.factory()
                .row("   ")
                .row("aba")
                .row("_a_")
                .piece('a', new OwnedPieceFactory(() -> a, player))
                .piece('b', new OwnedPieceFactory(() -> b, player))
                .build();
        HashMap<Square, OwnedPiece> expected = new HashMap<>();
        expected.put(new Square(0, 0), null);
        expected.put(new Square(1, 0), null);
        expected.put(new Square(2, 0), null);
        expected.put(new Square(0, 1), new OwnedPiece(a, player));
        expected.put(new Square(1, 1), new OwnedPiece(b, player));
        expected.put(new Square(2, 1), new OwnedPiece(a, player));
        expected.put(new Square(1, 2), new OwnedPiece(a, player));
        Assert.assertEquals(board, new Board(FXCollections.observableMap(expected)));
    }
    
    public void testBuilderConstructorEmpty() {
        Board board = Board.factory().build();
        Assert.assertEquals(board, new Board(FXCollections.emptyObservableMap()));
    }
    
    public void testBuilderConstructorNoPieces() {
        Board board = Board.factory()
                .row("   ")
                .row("   ")
                .build();
        HashMap<Square, OwnedPiece> expected = new HashMap<>();
        expected.put(new Square(0, 0), null);
        expected.put(new Square(0, 1), null);
        expected.put(new Square(0, 2), null);
        expected.put(new Square(1, 0), null);
        expected.put(new Square(1, 1), null);
        expected.put(new Square(1, 2), null);
        Assert.assertEquals(board, new Board(FXCollections.observableMap(expected)));
    }
    
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testBuilderConstructorNotEnoughPieces() {
        Board.factory()
                .row("abc")
                .piece('a', mock(OwnedPieceFactory.class))
                .build();
    }
    
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testBuilderConstructorTooManyPieces() {
        Board.factory()
                .row("")
                .piece('x', mock(OwnedPieceFactory.class))
                .build();
    }
    
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testBuilderConstructorContainsSpace() {
        Board.factory()
                .row(" ")
                .piece(' ', mock(OwnedPieceFactory.class))
                .build();
    }
    
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testBuilderConstructorContainsUnderscore() {
        Board.factory()
                .row("_")
                .piece('_', mock(OwnedPieceFactory.class))
                .build();
    }
    
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testBuilderConstructorUneven() {
        Board.factory()
                .row(" ")
                .row("    ")
                .build();
    }
    
    // getMaxX()
    
    public void testGetMaxX1() {
        Board board = Board.factory()
                .row("   _")
                .row(" _  ")
                .row("_ __")
                .row("___ ")
                .build();
        Assert.assertEquals(board.getMaxX(), 3);
    }
    
    public void testGetMaxX2() {
        Board board = Board.factory().row("").build();
        Assert.assertEquals(board.getMaxX(), 0);
    }
    
    // getMaxY()
    
    public void testGetMaxY() {
        Board board = Board.factory()
                .row("__  ")
                .row(" _ _")
                .row("_ __")
                .row(" _  ")
                .build();
        Assert.assertEquals(board.getMaxY(), 3);
    }
    
    public void testGetMaxY2() {
        Board board = Board.factory().row("").build();
        Assert.assertEquals(board.getMaxY(), 0);
    }
    
    // isOn()
    
    public void testIsOn1() {
        Board board = Board.factory().row(" ").build();
        Assert.assertTrue(board.isOn(new Square(0, 0)));
    }
    
    public void testIsOn2() {
        Board board = Board.factory().row(" ").build();
        Assert.assertFalse(board.isOn(new Square(0, 1)));
    }
    
    public void testIsOn3() {
        Board board = Board.factory().row("_").build();
        Assert.assertFalse(board.isOn(new Square(0, 0)));
    }
    
    public void testIsOn4() {
        Board board = Board.factory()
                .row("x")
                .piece('x', mock(OwnedPieceFactory.class))
                .build();
        Assert.assertTrue(board.isOn(new Square(0, 0)));
    }
    
    // get()
    
    public void testGet1() {
        Piece piece = mock(Piece.class);
        Player player = mock(Player.class);
        Board board = Board.factory()
                .row("x")
                .piece('x', new OwnedPieceFactory(() -> piece, player))
                .build();
        Assert.assertEquals(board.get(new Square(0, 0)), new OwnedPiece(piece, player));
    }
    
    @Test(expectedExceptions = NoSuchElementException.class)
    public void testGet2() {
        Board.factory().row(" ").build().get(new Square(0, 1));
    }
    
    @Test(expectedExceptions = NoSuchElementException.class)
    public void testGet3() {
        Board.factory().row("_").build().get(new Square(0, 0));
    }
    
    public void testGet4() {
        Board board = Board.factory().row(" ").build();
        Assert.assertEquals(board.get(new Square(0, 0)), null);
    }
    
    // set()
    
    public void testSet1() {
        Board board = Board.factory().row(" ").build();
        OwnedPiece op = mock(OwnedPiece.class);
        board.set(new Square(0, 0), op);
        HashMap<Square, OwnedPiece> expected = new HashMap<>();
        expected.put(new Square(0, 0), op);
        Assert.assertEquals(board, new Board(FXCollections.observableMap(expected)));
    }
    
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSet2() {
        Board.factory().row(" ").build().set(new Square(0, 1), mock(OwnedPiece.class));
    }
    
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSet3() {
        Board.factory().row("_").build().set(new Square(0, 0), mock(OwnedPiece.class));
    }
    
    public void testSet4() {
        Board board = Board.factory().row(" ").build();
        board.set(new Square(0, 0), null);
        HashMap<Square, OwnedPiece> expected = new HashMap<>();
        expected.put(new Square(0, 0), null);
        Assert.assertEquals(board, new Board(FXCollections.observableMap(expected)));
    }
    
    public void testSet5() {
        Board board = Board.factory().row("a").piece('a', mock(OwnedPieceFactory.class)).build();
        board.set(new Square(0, 0), null);
        HashMap<Square, OwnedPiece> expected = new HashMap<>();
        expected.put(new Square(0, 0), null);
        Assert.assertEquals(board, new Board(FXCollections.observableMap(expected)));
    }
    
}