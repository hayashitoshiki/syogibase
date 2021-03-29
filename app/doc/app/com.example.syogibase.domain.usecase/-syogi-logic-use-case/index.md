[app](../../index.md) / [com.example.syogibase.domain.usecase](../index.md) / [SyogiLogicUseCase](./index.md)

# SyogiLogicUseCase

`interface SyogiLogicUseCase`

将棋ロジック

### Functions

| Name | Summary |
|---|---|
| [cancel](cancel.md) | `abstract fun cancel(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>動かせるマスの設定をリセットする |
| [getCellInformation](get-cell-information.md) | `abstract fun getCellInformation(x: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, y: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Cell`](../../com.example.syogibase.domain.model/-cell/index.md)<br>指定したマスの情報を返す |
| [getGameLog](get-game-log.md) | `abstract fun getGameLog(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`GameLog`](../../com.example.syogibase.domain.model/-game-log/index.md)`>`<br>現在の棋譜を取得する |
| [getPieceHand](get-piece-hand.md) | `abstract fun getPieceHand(turn: `[`Turn`](../../com.example.syogibase.domain.value/-turn/index.md)`): `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`Piece`](../../com.example.syogibase.domain.model/-piece/index.md)`, `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`>`<br>指定した手番の持ち駒を返す |
| [getTurn](get-turn.md) | `abstract fun getTurn(): `[`Turn`](../../com.example.syogibase.domain.value/-turn/index.md)<br>現在の手番を返す |
| [isCompulsionEvolution](is-compulsion-evolution.md) | `abstract fun isCompulsionEvolution(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>強制的に成らないといけないかの判定を行う |
| [isEvolution](is-evolution.md) | `abstract fun isEvolution(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>駒が成れるかの判定を行う |
| [isGameEnd](is-game-end.md) | `abstract fun isGameEnd(): `[`GameResult`](../../com.example.syogibase.domain.value/-game-result/index.md)<br>対局終了判定を行う |
| [isRepetitionMove](is-repetition-move.md) | `abstract fun isRepetitionMove(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>千日手判定を行う |
| [isSelectEvolution](is-select-evolution.md) | `abstract fun isSelectEvolution(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>駒を任意で成るかの選択ができるか判定を行う |
| [isTryKing](is-try-king.md) | `abstract fun isTryKing(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>トライルールの判定を行う |
| [reset](reset.md) | `abstract fun reset(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>盤面をまっさらにする |
| [resetBoard](reset-board.md) | `abstract fun resetBoard(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>盤面を初期化する |
| [setBackFirstMove](set-back-first-move.md) | `abstract fun setBackFirstMove(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>最初まで戻る |
| [setBackMove](set-back-move.md) | `abstract fun setBackMove(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>１手戻す |
| [setBoard](set-board.md) | `abstract fun setBoard(customBoard: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`Cell`](../../com.example.syogibase.domain.model/-cell/index.md)`>>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>指定した盤面を設定 |
| [setEvolution](set-evolution.md) | `abstract fun setEvolution(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>駒を成らせる |
| [setGameLog](set-game-log.md) | `abstract fun setGameLog(logList: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`GameLog`](../../com.example.syogibase.domain.model/-game-log/index.md)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>棋譜を設定する |
| [setGoLastMove](set-go-last-move.md) | `abstract fun setGoLastMove(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>最後まで進む |
| [setGoMove](set-go-move.md) | `abstract fun setGoMove(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>１手進む |
| [setHandicap](set-handicap.md) | `abstract fun setHandicap(turn: `[`Turn`](../../com.example.syogibase.domain.value/-turn/index.md)`, handicap: `[`Handicap`](../../com.example.syogibase.domain.value/-handicap/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>駒落ちの設定 |
| [setHintHoldPiece](set-hint-hold-piece.md) | `abstract fun setHintHoldPiece(piece: `[`Piece`](../../com.example.syogibase.domain.model/-piece/index.md)`, turn: `[`Turn`](../../com.example.syogibase.domain.value/-turn/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>持ち駒を使う場合の駒をおける場所の検索 |
| [setHoldPiece](set-hold-piece.md) | `abstract fun setHoldPiece(holdPiece: `[`MutableMap`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)`<`[`Piece`](../../com.example.syogibase.domain.model/-piece/index.md)`, `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`>, turn: `[`Turn`](../../com.example.syogibase.domain.value/-turn/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>持ち駒の設定 |
| [setMove](set-move.md) | `abstract fun setMove(x: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, y: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, evolution: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>指定した位置に駒を動かす |
| [setTouchHint](set-touch-hint.md) | `abstract fun setTouchHint(x: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, y: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>指定したマスの駒の動けるマスを検索 |

### Inheritors

| Name | Summary |
|---|---|
| [SyogiLogicUseCaseImp](../-syogi-logic-use-case-imp/index.md) | `class SyogiLogicUseCaseImp : `[`SyogiLogicUseCase`](./index.md) |
