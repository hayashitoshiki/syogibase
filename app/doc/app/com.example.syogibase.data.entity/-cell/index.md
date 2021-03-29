[app](../../index.md) / [com.example.syogibase.data.entity](../index.md) / [Cell](./index.md)

# Cell

`class Cell`

将棋盤のマスの情報クラス

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Cell()`<br>将棋盤のマスの情報クラス |

### Properties

| Name | Summary |
|---|---|
| [hint](hint.md) | `var hint: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>マスに対して駒を動かせるか |
| [piece](piece.md) | `var piece: `[`Piece`](../-piece/index.md)`?`<br>駒の情報 |
| [turn](turn.md) | `var turn: `[`Turn`](../../com.example.syogibase.domain.value/-turn/index.md)`?`<br>駒の手番の情報 |

### Functions

| Name | Summary |
|---|---|
| [clear](clear.md) | `fun clear(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>マスに対する情報を全て空にする |
| [setNone](set-none.md) | `fun setNone(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>マスに対して設定している駒の情報を空にする |
| [setPiece](set-piece.md) | `fun setPiece(piece: `[`Piece`](../-piece/index.md)`, turn: `[`Turn`](../../com.example.syogibase.domain.value/-turn/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>マスに駒を設定する |
