[app](../../index.md) / [com.example.syogibase.presentation.presenter](../index.md) / [GameLogicPresenter](./index.md)

# GameLogicPresenter

`class GameLogicPresenter : `[`Presenter`](../../com.example.syogibase.presentation.contact/-game-view-contact/-presenter/index.md)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `GameLogicPresenter(view: `[`View`](../../com.example.syogibase.presentation.contact/-game-view-contact/-view/index.md)`, useCase: `[`SyogiLogicUseCase`](../../com.example.syogibase.domain.usecase/-syogi-logic-use-case/index.md)`)` |

### Functions

| Name | Summary |
|---|---|
| [checkGameEnd](check-game-end.md) | `fun checkGameEnd(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>対局終了判定 |
| [computeCellSize](compute-cell-size.md) | `fun computeCellSize(width: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, height: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>マスのサイズを計算 |
| [drawView](draw-view.md) | `fun drawView(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>描画ロジック |
| [evolutionPiece](evolution-piece.md) | `fun evolutionPiece(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>駒を成らせる |
| [getGameLog](get-game-log.md) | `fun getGameLog(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`GameLog`](../../com.example.syogibase.domain.model/-game-log/index.md)`>`<br>棋譜を取得 |
| [onTouchDownEventByReplayModeLogic](on-touch-down-event-by-replay-mode-logic.md) | `fun onTouchDownEventByReplayModeLogic(x: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, center: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>感想戦のタッチダウンイベントのロジック |
| [onTouchEventByGameMode](on-touch-event-by-game-mode.md) | `fun onTouchEventByGameMode(touchX: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, touchY: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>対局モードのタッチイベントのロジック |
| [onTouchEventLogic](on-touch-event-logic.md) | `fun onTouchEventLogic(x: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, y: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, mode: `[`BoardMode`](../../com.example.syogibase.domain.value/-board-mode/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>画面をタッチしたときのモード判定ロジック |
| [onTouchUpEventByReplayModeLogic](on-touch-up-event-by-replay-mode-logic.md) | `fun onTouchUpEventByReplayModeLogic(x: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, center: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>感想戦のタッチアップイベントのロジック |
| [reset](reset.md) | `fun reset(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>初期状態に戻す |
| [setBackFirstMove](set-back-first-move.md) | `fun setBackFirstMove(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>最初まで戻る |
| [setBackMove](set-back-move.md) | `fun setBackMove(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>１手戻る |
| [setEnableHint](set-enable-hint.md) | `fun setEnableHint(enable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>駒を動かせる場所の表示設定 |
| [setEnableTouchSound](set-enable-touch-sound.md) | `fun setEnableTouchSound(enable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>駒音を有無の設定 |
| [setGameLog](set-game-log.md) | `fun setGameLog(logList: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`GameLog`](../../com.example.syogibase.domain.model/-game-log/index.md)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>棋譜設定 |
| [setGoLastMove](set-go-last-move.md) | `fun setGoLastMove(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>最後まで進む |
| [setGoMove](set-go-move.md) | `fun setGoMove(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>１手進む |
| [setHandicap](set-handicap.md) | `fun setHandicap(turn: `[`Turn`](../../com.example.syogibase.domain.value/-turn/index.md)`, handicap: `[`Handicap`](../../com.example.syogibase.domain.value/-handicap/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>駒落ち設定 |
