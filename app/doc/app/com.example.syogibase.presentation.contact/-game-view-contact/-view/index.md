[app](../../../index.md) / [com.example.syogibase.presentation.contact](../../index.md) / [GameViewContact](../index.md) / [View](./index.md)

# View

`interface View`

### Functions

| Name | Summary |
|---|---|
| [cancelLongJob](cancel-long-job.md) | `abstract fun cancelLongJob(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>感想戦モードの遅延処理のキャンセル |
| [drawBlackPiece](draw-black-piece.md) | `abstract fun drawBlackPiece(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, x: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, y: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>先手の駒描画 |
| [drawBoard](draw-board.md) | `abstract fun drawBoard(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>将棋盤描画 |
| [drawHint](draw-hint.md) | `abstract fun drawHint(x: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, y: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>駒を動かせるマスのヒントを描画 |
| [drawHoldPieceBlack](draw-hold-piece-black.md) | `abstract fun drawHoldPieceBlack(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, stock: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, x: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, y: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>先手の持ち駒を描画 |
| [drawHoldPieceWhite](draw-hold-piece-white.md) | `abstract fun drawHoldPieceWhite(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, stock: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, x: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, y: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>後手の持ち駒を描画 |
| [drawHorizontalStand](draw-horizontal-stand.md) | `abstract fun drawHorizontalStand(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>垂直方向の駒台描画 |
| [drawVerticalStand](draw-vertical-stand.md) | `abstract fun drawVerticalStand(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>水平方向の駒台描画 |
| [drawWhitePiece](draw-white-piece.md) | `abstract fun drawWhitePiece(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, x: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, y: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>後手の駒描画 |
| [gameEnd](game-end.md) | `abstract fun gameEnd(turn: `[`Turn`](../../../com.example.syogibase.domain.value/-turn/index.md)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>対局終了通知 |
| [onTouchEventByGameMode](on-touch-event-by-game-mode.md) | `abstract fun onTouchEventByGameMode(x: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, y: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>対局モード時のタッチイベント処理 |
| [onTouchEventByReplayMode](on-touch-event-by-replay-mode.md) | `abstract fun onTouchEventByReplayMode(x: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, y: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>感想戦モード時のタッチイベント処理 |
| [playbackEffect](playback-effect.md) | `abstract fun playbackEffect(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>駒音再生 |
| [setCellSize](set-cell-size.md) | `abstract fun setCellSize(size: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>マスのサイズ設定 |
| [setLongJobByBack](set-long-job-by-back.md) | `abstract fun setLongJobByBack(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>感想戦モードの一番最初まで戻る処理の遅延処理設定 |
| [setLongJobByGo](set-long-job-by-go.md) | `abstract fun setLongJobByGo(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>感想戦モードの一番最後まで進む処理の遅延処理設定 |
| [showDialog](show-dialog.md) | `abstract fun showDialog(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>成るか判別するダイアログ生成 |

### Inheritors

| Name | Summary |
|---|---|
| [GameView](../../../com.example.syogibase.presentation.view/-game-view/index.md) | `class GameView : `[`View`](https://developer.android.com/reference/android/view/View.html)`, `[`View`](./index.md)`, KoinComponent` |
