[app](../../index.md) / [com.example.syogibase.domain.value](../index.md) / [GameResult](./index.md)

# GameResult

`sealed class GameResult`

対局終了の判定結果の種類

### Types

| Name | Summary |
|---|---|
| [Continue](-continue.md) | `object Continue : `[`GameResult`](./index.md)<br>まだ決着がついていない |
| [Draw](-draw.md) | `object Draw : `[`GameResult`](./index.md)<br>引き分け |
| [Win](-win/index.md) | `data class Win : `[`GameResult`](./index.md)<br>勝ち |

### Inheritors

| Name | Summary |
|---|---|
| [Continue](-continue.md) | `object Continue : `[`GameResult`](./index.md)<br>まだ決着がついていない |
| [Draw](-draw.md) | `object Draw : `[`GameResult`](./index.md)<br>引き分け |
| [Win](-win/index.md) | `data class Win : `[`GameResult`](./index.md)<br>勝ち |
