[app](../../index.md) / [com.example.syogibase.data.entity](../index.md) / [GameLog](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`GameLog(fromX: `[`X`](../../com.example.syogibase.domain.value/-x/index.md)`, fromY: `[`Y`](../../com.example.syogibase.domain.value/-y/index.md)`, piece: `[`Piece`](../-piece/index.md)`, turn: `[`Turn`](../../com.example.syogibase.domain.value/-turn/index.md)`, toX: `[`X`](../../com.example.syogibase.domain.value/-x/index.md)`, toY: `[`Y`](../../com.example.syogibase.domain.value/-y/index.md)`, stealPiece: `[`Piece`](../-piece/index.md)`?, stealTurn: `[`Turn`](../../com.example.syogibase.domain.value/-turn/index.md)`?, evolution: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`)`

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