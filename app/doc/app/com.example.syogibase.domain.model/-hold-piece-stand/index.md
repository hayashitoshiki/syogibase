[app](../../index.md) / [com.example.syogibase.domain.model](../index.md) / [HoldPieceStand](./index.md)

# HoldPieceStand

`class HoldPieceStand`

持ち駒の定義クラス

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `HoldPieceStand()`<br>持ち駒の定義クラス |

### Properties

| Name | Summary |
|---|---|
| [pieceList](piece-list.md) | `val pieceList: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`Piece`](../-piece/index.md)`, `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`>`<br>持ち駒と所持数 |

### Functions

| Name | Summary |
|---|---|
| [add](add.md) | `fun add(piece: `[`Piece`](../-piece/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>持ち駒を追加する |
| [getStandPiece](get-stand-piece.md) | `fun getStandPiece(piece: `[`Piece`](../-piece/index.md)`): `[`Piece`](../-piece/index.md)`?`<br>指定した持ち駒を取得する |
| [remove](remove.md) | `fun remove(piece: `[`Piece`](../-piece/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>持ち駒を使用する |
| [reset](reset.md) | `fun reset(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>持ち駒の所持数を初期化する |
| [setCustomStand](set-custom-stand.md) | `fun setCustomStand(holdPieceBlack: `[`MutableMap`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)`<`[`Piece`](../-piece/index.md)`, `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>指定した持ち駒を設定する |
