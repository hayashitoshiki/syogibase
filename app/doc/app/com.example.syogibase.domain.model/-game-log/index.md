[app](../../index.md) / [com.example.syogibase.domain.model](../index.md) / [GameLog](./index.md)

# GameLog

`data class GameLog`

１手の指し手のログ

### Parameters

`fromX` - 動かす前のX座標

`fromY` - 動かす前のY座標

`piece` - 動かす駒

`turn` - 動かす駒の手番

`toX` - 動かす先のX座標

`toY` - 動かす先のY座標

`stealPiece` - 動かした先にある駒(駒がない場合はnull)

`stealTurn` - 動かした先にある駒の手番(駒がない場合はnull)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `GameLog(fromX: `[`X`](../../com.example.syogibase.domain.value/-x/index.md)`, fromY: `[`Y`](../../com.example.syogibase.domain.value/-y/index.md)`, piece: `[`Piece`](../-piece/index.md)`, turn: `[`Turn`](../../com.example.syogibase.domain.value/-turn/index.md)`, toX: `[`X`](../../com.example.syogibase.domain.value/-x/index.md)`, toY: `[`Y`](../../com.example.syogibase.domain.value/-y/index.md)`, stealPiece: `[`Piece`](../-piece/index.md)`?, stealTurn: `[`Turn`](../../com.example.syogibase.domain.value/-turn/index.md)`?, evolution: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`)`<br>１手の指し手のログ |

### Properties

| Name | Summary |
|---|---|
| [evolution](evolution.md) | `var evolution: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [fromX](from-x.md) | `val fromX: `[`X`](../../com.example.syogibase.domain.value/-x/index.md)<br>動かす前のX座標 |
| [fromY](from-y.md) | `val fromY: `[`Y`](../../com.example.syogibase.domain.value/-y/index.md)<br>動かす前のY座標 |
| [piece](piece.md) | `val piece: `[`Piece`](../-piece/index.md)<br>動かす駒 |
| [stealPiece](steal-piece.md) | `val stealPiece: `[`Piece`](../-piece/index.md)`?`<br>動かした先にある駒(駒がない場合はnull) |
| [stealTurn](steal-turn.md) | `val stealTurn: `[`Turn`](../../com.example.syogibase.domain.value/-turn/index.md)`?`<br>動かした先にある駒の手番(駒がない場合はnull) |
| [toX](to-x.md) | `val toX: `[`X`](../../com.example.syogibase.domain.value/-x/index.md)<br>動かす先のX座標 |
| [toY](to-y.md) | `val toY: `[`Y`](../../com.example.syogibase.domain.value/-y/index.md)<br>動かす先のY座標 |
| [turn](turn.md) | `val turn: `[`Turn`](../../com.example.syogibase.domain.value/-turn/index.md)<br>動かす駒の手番 |
