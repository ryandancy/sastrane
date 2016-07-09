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

package ca.keal.sastrane.api;

import ca.keal.sastrane.api.piece.OwnedPiece;
import ca.keal.sastrane.api.piece.OwnedPieceFactory;
import ca.keal.sastrane.api.piece.Piece;
import com.google.common.eventbus.EventBus;
import javafx.collections.FXCollections;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

@Test
public class BoardTest {
    
    @Mock private OwnedPieceFactory opfMock;
    
    @BeforeMethod(alwaysRun = true)
    public void injectMocks() {
        MockitoAnnotations.initMocks(this);
        when(opfMock.getPieceFactory()).thenReturn(r -> mock(Piece.class));
    }
    
    // Board.factory() and its building sequence
    
    public void factory_withSinglePieceType_buildsCorrectBoard() {
        Piece piece = mock(Piece.class);
        Player player = mock(Player.class);
        Board board = Board.factory()
                .row("a")
                .piece('a', new OwnedPieceFactory(() -> piece, player))
                .build();
        Map<Square, OwnedPiece> expected = new HashMap<>();
        expected.put(new Square(0, 0), new OwnedPiece(piece, player));
        assertEquals(board, new Board(FXCollections.observableMap(expected)));
    }
    
    public void factory_withTwoDifferentPieceTypes_buildsCorrectBoard() {
        Piece a = mock(Piece.class);
        Piece b = mock(Piece.class);
        Player player = mock(Player.class);
        Board board = Board.factory()
                .row("ab")
                .piece('a', new OwnedPieceFactory(() -> a, player))
                .piece('b', new OwnedPieceFactory(() -> b, player))
                .build();
        Map<Square, OwnedPiece> expected = new HashMap<>();
        expected.put(new Square(0, 0), new OwnedPiece(a, player));
        expected.put(new Square(1, 0), new OwnedPiece(b, player));
        assertEquals(board, new Board(FXCollections.observableMap(expected)));
    }
    
    public void factory_withEmptySquare_buildsCorrectBoard() {
        Board board = Board.factory().row(" ").build();
        Map<Square, OwnedPiece> expected = new HashMap<>();
        expected.put(new Square(0, 0), null);
        assertEquals(board, new Board(FXCollections.observableMap(expected)));
    }
    
    public void factory_withUnderscore_buildsEmptyBoard() {
        Board board = Board.factory().row("_").build();
        assertEquals(board, new Board(FXCollections.observableHashMap()));
    }
    
    public void factory_withAllSquareTypesAndTwoPieceTypes_buildsCorrectBoard() {
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
        Map<Square, OwnedPiece> expected = new HashMap<>();
        expected.put(new Square(0, 0), null);
        expected.put(new Square(1, 0), null);
        expected.put(new Square(2, 0), null);
        expected.put(new Square(0, 1), new OwnedPiece(a, player));
        expected.put(new Square(1, 1), new OwnedPiece(b, player));
        expected.put(new Square(2, 1), new OwnedPiece(a, player));
        expected.put(new Square(1, 2), new OwnedPiece(a, player));
        assertEquals(board, new Board(FXCollections.observableMap(expected)));
    }
    
    public void factory_withoutArgs_buildsEmptyBoard() {
        Board board = Board.factory().build();
        assertEquals(board, new Board(FXCollections.emptyObservableMap()));
    }
    
    public void factory_withoutPiecesButWithEmptySquares_buildsBoardWithAllSquaresEmpty() {
        Board board = Board.factory()
                .row("   ")
                .row("   ")
                .build();
        Map<Square, OwnedPiece> expected = new HashMap<>();
        expected.put(new Square(0, 0), null);
        expected.put(new Square(1, 0), null);
        expected.put(new Square(2, 0), null);
        expected.put(new Square(0, 1), null);
        expected.put(new Square(1, 1), null);
        expected.put(new Square(2, 1), null);
        assertEquals(board, new Board(FXCollections.observableMap(expected)));
    }
    
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void factory_withoutEnoughPieces_fails() {
        Board.factory()
                .row("abc")
                .piece('a', opfMock)
                .build();
    }
    
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void factory_withTooManyPieces_fails() {
        Board.factory()
                .row("")
                .piece('x', opfMock)
                .build();
    }
    
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void factory_withSpaceAsPiece_fails() {
        Board.factory()
                .row(" ")
                .piece(' ', opfMock)
                .build();
    }
    
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void factory_withUnderscoreAsPiece_fails() {
        Board.factory()
                .row("_")
                .piece('_', opfMock)
                .build();
    }
    
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void factory_withUnevenRows_fails() {
        Board.factory()
                .row(" ")
                .row("    ")
                .build();
    }
    
    public void factory_withBus_callsEachPieceWithBus() {
        EventBus bus = mock(EventBus.class);
        OwnedPieceFactory a = mock(OwnedPieceFactory.class);
        OwnedPieceFactory b = mock(OwnedPieceFactory.class);
        
        Answer<Void> answer = call -> {
            assertTrue(call.getArguments()[0] == bus);
            return null;
        };
        when(a.getPiece(any(EventBus.class))).thenAnswer(answer);
        when(b.getPiece(any(EventBus.class))).thenAnswer(answer);
        
        Board.factory()
                .row("ab")
                .piece('a', a)
                .piece('b', b)
                .bus(bus)
                .build();
        
        verify(a, times(1)).getPiece(any(EventBus.class));
        verify(b, times(1)).getPiece(any(EventBus.class));
    }
    
    // The copy constructor
    
    public void copyConstructor_withFieldsSet_returnsBoardEqualToParameter() {
        Board oldBoard = Board.factory()
                .row("_ _ ")
                .row("a  _")
                .row("  aa")
                .piece('a', opfMock)
                .build();
        Board newBoard = new Board(oldBoard);
        assertEquals(newBoard, oldBoard);
    }
    
    public void copyConstructor_withFieldsSet_returnsBoardNotReferenceEqualToParameter() {
        Board oldBoard = Board.factory()
                .row("_ _ ")
                .row("a  _")
                .row("  aa")
                .piece('a', opfMock)
                .build();
        Board newBoard = new Board(oldBoard);
        assertFalse(oldBoard == newBoard);
    }
    
    // getMaxX()
    
    public void getMaxX_withBasicBoardOneRow_returnsCorrectMaxX() {
        Board board = Board.factory()
                .row("     ")
                .build();
        assertEquals(board.getMaxX(), 4);
    }
    
    public void getMaxX_withBasicBoardMultipleRows_returnsCorrectMaxX() {
        Board board = Board.factory()
                .row("   ")
                .row("   ")
                .build();
        assertEquals(board.getMaxX(), 2);
    }
    
    public void getMaxX_withUnevenRows_returnsCorrectMaxX() {
        Board board = Board.factory()
                .row("   _")
                .row(" _  ")
                .row("_ __")
                .row("___ ")
                .build();
        assertEquals(board.getMaxX(), 3);
    }
    
    public void getMaxX_withEmptyBoard_returns0() {
        Board board = Board.factory().build();
        assertEquals(board.getMaxX(), 0);
    }
    
    public void getMaxX_withAllUnderscoresInRightmostColumn_doesNotCountRightmostColumn() {
        Board board = Board.factory()
                .row("  _")
                .row("  _")
                .row("  _")
                .row("  _")
                .build();
        assertEquals(board.getMaxX(), 1);
    }
    
    public void getMaxX_withAllUnderscoresInLeftmostColumn_countsLeftmostColumn() {
        Board board = Board.factory()
                .row("_  ")
                .row("_  ")
                .row("_  ")
                .row("_  ")
                .build();
        assertEquals(board.getMaxX(), 2);
    }
    
    public void getMaxX_withAllUnderscoresInMiddleColumn_countsThatColumn() {
        Board board = Board.factory()
                .row(" _ ")
                .row(" _ ")
                .row(" _ ")
                .row(" _ ")
                .build();
        assertEquals(board.getMaxX(), 2);
    }
    
    public void getMaxX_withPieces_returnsCorrectMaxX() {
        Board board = Board.factory()
                .row("aaaa")
                .row("aaaa")
                .row("aaaa")
                .piece('a', opfMock)
                .build();
        assertEquals(board.getMaxX(), 3);
    }
    
    // getMaxY()
    
    public void getMaxY_withBasicBoardOneColumn_returnsCorrectMaxY() {
        Board board = Board.factory()
                .row(" ")
                .row(" ")
                .row(" ")
                .build();
        assertEquals(board.getMaxY(), 2);
    }
    
    public void getMaxY_withBasicBoardMultipleColumns_returnsCorrectMaxY() {
        Board board = Board.factory()
                .row("   ")
                .row("   ")
                .build();
        assertEquals(board.getMaxY(), 1);
    }
    
    public void getMaxY_withUnevenColumns_returnsCorrectMaxY() {
        Board board = Board.factory()
                .row("__  ")
                .row(" _ _")
                .row("_ __")
                .row(" _  ")
                .build();
        assertEquals(board.getMaxY(), 3);
    }
    
    public void getMaxY_withEmptyBoard_returns0() {
        Board board = Board.factory().build();
        assertEquals(board.getMaxY(), 0);
    }
    
    public void getMaxY_withAllUnderscoresInBottomRow_doesNotCountBottomRow() {
        Board board = Board.factory()
                .row("    ")
                .row("    ")
                .row("____")
                .build();
        assertEquals(board.getMaxY(), 1);
    }
    
    public void getMaxY_withAllUnderscoresInTopRow_countsTopRow() {
        Board board = Board.factory()
                .row("____")
                .row("    ")
                .row("    ")
                .build();
        assertEquals(board.getMaxY(), 2);
    }
    
    public void getMaxY_withAllUnderscoresInMiddleRow_countsThatRow() {
        Board board = Board.factory()
                .row("    ")
                .row("____")
                .row("    ")
                .build();
        assertEquals(board.getMaxY(), 2);
    }
    
    public void getMaxY_withPieces_returnsCorrectMaxY() {
        Board board = Board.factory()
                .row("aaaa")
                .row("aaaa")
                .row("aaaa")
                .piece('a', opfMock)
                .build();
        assertEquals(board.getMaxY(), 2);
    }
    
    // isOn()
    
    public void isOn_withOnlySquareOnEmpty1x1Board_returnsTrue() {
        Board board = Board.factory().row(" ").build();
        assertTrue(board.isOn(new Square(0, 0)));
    }
    
    public void isOn_withSquareOff1x1Board_returnsFalse() {
        Board board = Board.factory().row(" ").build();
        assertFalse(board.isOn(new Square(0, 1)));
    }
    
    public void isOn_withEmptyBoardViaUnderscore_returnsFalse() {
        Board board = Board.factory().row("_").build();
        assertFalse(board.isOn(new Square(0, 0)));
    }
    
    public void isOn_withOnlySquareOn1x1BoardWithPiece_returnsTrue() {
        Board board = Board.factory()
                .row("x")
                .piece('x', opfMock)
                .build();
        assertTrue(board.isOn(new Square(0, 0)));
    }
    
    public void isOn_withUnderscoreInMiddleOfBoard_returnsFalse() {
        Board board = Board.factory()
                .row("   ")
                .row(" _ ")
                .row("   ")
                .build();
        assertFalse(board.isOn(new Square(1, 1)));
    }
    
    public void isOn_withPieceInMiddleOfUnderscores_returnsTrue() {
        Board board = Board.factory()
                .row("___")
                .row("_a_")
                .row("___")
                .piece('a', opfMock)
                .build();
        assertTrue(board.isOn(new Square(1, 1)));
    }
    
    // get()
    
    public void get_withPieceCorrectlyAddressed_getsPiecePutOnBoard() {
        Piece piece = mock(Piece.class);
        Player player = mock(Player.class);
        Board board = Board.factory()
                .row("x")
                .piece('x', new OwnedPieceFactory(() -> piece, player))
                .build();
        assertEquals(board.get(new Square(0, 0)), new OwnedPiece(piece, player));
    }
    
    @Test(expectedExceptions = NoSuchElementException.class)
    public void get_withSquareOffBoard_fails() {
        Board.factory().row(" ").build().get(new Square(0, 1));
    }
    
    @Test(expectedExceptions = NoSuchElementException.class)
    public void get_withEmptyBoardViaUnderscore_fails() {
        Board.factory().row("_").build().get(new Square(0, 0));
    }
    
    public void get_withOnlySquareOnEmptyBoard_returnsNull() {
        Board board = Board.factory().row(" ").build();
        assertEquals(board.get(new Square(0, 0)), null);
    }
    
    // set()
    
    public void set_pieceOnEmptySquare_succeeds() {
        Board board = Board.factory().row(" ").build();
        OwnedPiece op = mock(OwnedPiece.class);
        board.set(new Square(0, 0), op);
        Map<Square, OwnedPiece> expected = new HashMap<>();
        expected.put(new Square(0, 0), op);
        assertEquals(board, new Board(FXCollections.observableMap(expected)));
    }
    
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void set_pieceOffBoard_fails() {
        Board.factory().row(" ").build().set(new Square(0, 1), mock(OwnedPiece.class));
    }
    
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void set_pieceOffBoardViaUnderscore_fails() {
        Board.factory().row("_").build().set(new Square(0, 0), mock(OwnedPiece.class));
    }
    
    public void set_nullOnNull_succeeds() {
        Board board = Board.factory().row(" ").build();
        board.set(new Square(0, 0), null);
        Map<Square, OwnedPiece> expected = new HashMap<>();
        expected.put(new Square(0, 0), null);
        assertEquals(board, new Board(FXCollections.observableMap(expected)));
    }
    
    public void set_nullOnPiece_succeeds() {
        Board board = Board.factory().row("a").piece('a', opfMock).build();
        board.set(new Square(0, 0), null);
        Map<Square, OwnedPiece> expected = new HashMap<>();
        expected.put(new Square(0, 0), null);
        assertEquals(board, new Board(FXCollections.observableMap(expected)));
    }
    
    // equals()
    
    public void equals_withTwoBoardsWithSamePiece_returnsTrue() {
        Board b1 = Board.factory().row("a").piece('a', opfMock).build();
        Board b2 = Board.factory().row("a").piece('a', opfMock).build();
        assertEquals(b1, b2);
    }
    
    public void equals_withOneBoardWithPieceOneWithout_returnsFalse() {
        Board b1 = Board.factory().row("a").piece('a', opfMock).build();
        Board b2 = Board.factory().row(" ").build();
        assertNotEquals(b1, b2);
    }
    
    public void equals_withTwoEmptyBoards_returnsTrue() {
        Board b1 = Board.factory().row(" ").build();
        Board b2 = Board.factory().row(" ").build();
        assertEquals(b1, b2);
    }
    
    public void equals_withEmptyBoardsOfDifferentSizes_returnsFalse() {
        Board b1 = Board.factory().row("  ").build();
        Board b2 = Board.factory().row(" ").build();
        assertNotEquals(b1, b2);
    }
    
    public void equals_withTwoIdenticalBoardsWithPieceAndEmptySquares_returnsTrue() {
        Board b1 = Board.factory().row(" a ").piece('a', opfMock).build();
        Board b2 = Board.factory().row(" a ").piece('a', opfMock).build();
        assertEquals(b1, b2);
    }
    
    public void equals_withTwoBoardsSameDimensAndPieceCountsButDifferentPositions_returnsFalse() {
        Board b1 = Board.factory().row(" a ").piece('a', opfMock).build();
        Board b2 = Board.factory().row("a  ").piece('a', opfMock).build();
        assertNotEquals(b1, b2);
    }
    
    public void equals_withTwoIdenticalBoardsWithUnderscores_returnsTrue() {
        Board b1 = Board.factory().row("__a").piece('a', opfMock).build();
        Board b2 = Board.factory().row("__a").piece('a', opfMock).build();
        assertEquals(b1, b2);
    }
    
    public void equals_withTwoBoardsSamePiecesWithDifferentUnderscorePositions_returnsFalse() {
        Board b1 = Board.factory().row("_a_").piece('a', opfMock).build();
        Board b2 = Board.factory().row("__a").piece('a', opfMock).build();
        assertNotEquals(b1, b2);
    }
    
    public void equals_withMultipleRowsMixedSameBoards_returnsTrue() {
        Board b1 = Board.factory().row(" a ").row("a a").piece('a', opfMock).build();
        Board b2 = Board.factory().row(" a ").row("a a").piece('a', opfMock).build();
        assertEquals(b1, b2);
    }
    
    public void equals_withMultipleRowsMixedBoardsOnlyDifferentByRowPosition_returnsFalse() {
        Board b1 = Board.factory().row(" a ").row("a a").piece('a', opfMock).build();
        Board b2 = Board.factory().row("a a").row(" a ").piece('a', opfMock).build();
        assertNotEquals(b1, b2);
    }
    
}