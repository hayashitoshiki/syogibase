[app](../../../index.md) / [com.example.syogibase.presentation.contact](../../index.md) / [GameViewContact](../index.md) / [Presenter](./index.md)

# Presenter

`interface Presenter`

### Functions

| Name | Summary |
|---|---|
| [checkGameEnd](check-game-end.md) | `abstract fun checkGameEnd(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>対局終了判定 |
| [computeCellSize](compute-cell-size.md) | `abstract fun computeCellSize(width: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, height: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>マスのサイズを計算 |
| [drawView](draw-view.md) | `abstract fun drawView(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>描画ロジック |
| [evolutionPiece](evolution-piece.md) | `abstract fun evolutionPiece(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>駒を成らせる |
| [getGameLog](get-game-log.md) | `abstract fun getGameLog(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`GameLog`](../../../com.example.syogibase.data.entity/-game-log/index.md)`>`<br>棋譜を取得 |
| [onTouchDownEventByReplayModeLogic](on-touch-down-event-by-replay-mode-logic.md) | `abstract fun onTouchDownEventByReplayModeLogic(x: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, center: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>感想戦のタッチダウンイベントのロジック |
| [onTouchEventByGameMode](on-touch-event-by-game-mode.md) | `abstract fun onTouchEventByGameMode(touchX: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, touchY: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>対局モードのタッチイベントのロジック |
| [onTouchEventLogic](on-touch-event-logic.md) | `abstract fun onTouchEventLogic(x: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, y: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, mode: `[`BoardMode`](../../../com.example.syogibase.data.value/-board-mode/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>画面をタッチしたときのモード判定ロジック |
| [onTouchUpEventByReplayModeLogic](on-touch-up-event-by-replay-mode-logic.md) | `abstract fun onTouchUpEventByReplayModeLogic(x: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, center: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>感想戦のタッチアップイベントのロジック |
| [reset](reset.md) | `abstract fun reset(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>初期状態に戻す |
| [setBackFirstMove](set-back-first-move.md) | `abstract fun setBackFirstMove(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>最初まで戻る |
| [setBackMove](set-back-move.md) | `abstract fun setBackMove(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>１手戻る |
| [setEnableHint](set-enable-hint.md) | `abstract fun setEnableHint(enable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>駒を動かせる場所の表示設定 |
| [setEnableTouchSound](set-enable-touch-sound.md) | `abstract fun setEnableTouchSound(enable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>駒音を有無の設定 |
| [setGameLog](set-game-log.md) | `abstract fun setGameLog(logList: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`GameLog`](../../../com.example.syogibase.data.entity/-game-log/index.md)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>棋譜設定 |
| [setGoLastMove](set-go-last-move.md) | `abstract fun setGoLastMove(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>最後まで進む |
| [setGoMove](set-go-move.md) | `abstract fun setGoMove(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>１手進む |
| [setHandicap](set-handicap.md) | `abstract fun setHandicap(turn: `[`Turn`](../../../com.example.syogibase.data.value/-turn/index.md)`, handicap: `[`Handicap`](../../../com.example.syogibase.data.value/-handicap/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>駒落ち設定 |

### Inheritors

| Name | Summary |
|---|---|
| [GameLogicPresenter](../../../com.example.syogibase.presentation.presenter/-game-logic-presenter/index.md) | `class GameLogicPresenter : `[`Presenter`](./index.md) |
