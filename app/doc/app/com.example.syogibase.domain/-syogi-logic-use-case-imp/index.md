[app](../../index.md) / [com.example.syogibase.domain](../index.md) / [SyogiLogicUseCaseImp](./index.md)

# SyogiLogicUseCaseImp

`class SyogiLogicUseCaseImp : `[`SyogiLogicUseCase`](../-syogi-logic-use-case/index.md)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `SyogiLogicUseCaseImp(board: `[`Board`](../../com.example.syogibase.data.entity/-board/index.md)` = Board())` |

### Functions

| Name | Summary |
|---|---|
| [cancel](cancel.md) | `fun cancel(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>動かせるマスの設定をリセットする |
| [getCellInformation](get-cell-information.md) | `fun getCellInformation(x: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, y: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Cell`](../../com.example.syogibase.data.entity/-cell/index.md)<br>指定したマスの情報を返す |
| [getGameLog](get-game-log.md) | `fun getGameLog(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`GameLog`](../../com.example.syogibase.data.entity/-game-log/index.md)`>`<br>現在の棋譜を取得する |
| [getPieceHand](get-piece-hand.md) | `fun getPieceHand(turn: `[`Turn`](../../com.example.syogibase.data.value/-turn/index.md)`): `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`Piece`](../../com.example.syogibase.data.entity/-piece/index.md)`, `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`>`<br>指定した手番の持ち駒を返す |
| [getTurn](get-turn.md) | `fun getTurn(): `[`Turn`](../../com.example.syogibase.data.value/-turn/index.md)<br>現在の手番を返す |
| [isCompulsionEvolution](is-compulsion-evolution.md) | `fun isCompulsionEvolution(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>強制的に成らないといけないかの判定を行う |
| [isEvolution](is-evolution.md) | `fun isEvolution(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>駒が成れるかの判定を行う |
| [isGameEnd](is-game-end.md) | `fun isGameEnd(): `[`GameResult`](../../com.example.syogibase.data.value/-game-result/index.md)<br>対局終了判定を行う |
| [isRepetitionMove](is-repetition-move.md) | `fun isRepetitionMove(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>千日手判定を行う |
| [isSelectEvolution](is-select-evolution.md) | `fun isSelectEvolution(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>駒を任意で成るかの選択ができるか判定を行う |
| [isTryKing](is-try-king.md) | `fun isTryKing(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>トライルールの判定を行う |
| [reset](reset.md) | `fun reset(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>盤面をまっさらにする |
| [resetBoard](reset-board.md) | `fun resetBoard(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>盤面を初期化する |
| [setBackFirstMove](set-back-first-move.md) | `fun setBackFirstMove(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>最初まで戻る |
| [setBackMove](set-back-move.md) | `fun setBackMove(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>１手戻す |
| [setBoard](set-board.md) | `fun setBoard(customBoard: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`Cell`](../../com.example.syogibase.data.entity/-cell/index.md)`>>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>指定した盤面を設定 |
| [setEvolution](set-evolution.md) | `fun setEvolution(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>駒を成らせる |
| [setGameLog](set-game-log.md) | `fun setGameLog(logList: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`GameLog`](../../com.example.syogibase.data.entity/-game-log/index.md)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>棋譜を設定する |
| [setGoLastMove](set-go-last-move.md) | `fun setGoLastMove(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>最後まで進む |
| [setGoMove](set-go-move.md) | `fun setGoMove(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>１手進む |
| [setHandicap](set-handicap.md) | `fun setHandicap(turn: `[`Turn`](../../com.example.syogibase.data.value/-turn/index.md)`, handicap: `[`Handicap`](../../com.example.syogibase.data.value/-handicap/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>駒落ちの設定 |
| [setHintHoldPiece](set-hint-hold-piece.md) | `fun setHintHoldPiece(piece: `[`Piece`](../../com.example.syogibase.data.entity/-piece/index.md)`, turn: `[`Turn`](../../com.example.syogibase.data.value/-turn/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>持ち駒を使う場合の駒をおける場所の検索 |
| [setHoldPiece](set-hold-piece.md) | `fun setHoldPiece(holdPiece: `[`MutableMap`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)`<`[`Piece`](../../com.example.syogibase.data.entity/-piece/index.md)`, `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`>, turn: `[`Turn`](../../com.example.syogibase.data.value/-turn/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>持ち駒の設定 |
| [setMove](set-move.md) | `fun setMove(x: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, y: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, evolution: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>指定した位置に駒を動かす |
| [setTouchHint](set-touch-hint.md) | `fun setTouchHint(x: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, y: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>指定したマスの駒の動けるマスを検索 |
