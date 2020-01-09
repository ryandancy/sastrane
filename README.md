A board game engine and player. Supports chess, tic-tac-toe, xiangqi, reversi
(Othello), and go. The implementation of each board game extends from a common
base, which includes AI and basic gameplay. The base is completely decoupled
from the implementation and new games can be added smoothly.

This is a legacy project and is no longer updated. Therefore, to build this
project, use [SDKMAN!](sdkman.io) to use Gradle 2.14 and use Oracle's official
Java 8 (OpenJDK will not work due to weirdness with JavaFX). Then, run
`gradle run`, and it *should* work.
