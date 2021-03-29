[app](../../index.md) / [com.example.syogibase.data.entity](../index.md) / [Board](./index.md)

# Board

`class Board`

将棋盤

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Board()`<br>将棋盤 |

### Functions

| Name | Summary |
|---|---|
| [clear](clear.md) | `fun clear(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>全てのマスの情報をクリアする |
| [getCell](get-cell.md) | `fun getCell(x: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, y: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Cell`](../-cell/index.md)<br>指定されたマスの情報を取得 |
| [getCells](get-cells.md) | `fun getCells(): `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`Cell`](../-cell/index.md)`>>`<br>将棋盤の全てのマスを返す |
| [getCountByHint](get-count-by-hint.md) | `fun getCountByHint(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>コマを置ける場所の総数を返す |
| [restHintAll](rest-hint-all.md) | `fun restHintAll(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>全てのマスのヒントをリセット |
| [setBoard](set-board.md) | `fun setBoard(customBoard: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`Cell`](../-cell/index.md)`>>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>将棋盤を指定した盤面で設定 |
| [setHandicap](set-handicap.md) | `fun setHandicap(turn: `[`Turn`](../../com.example.syogibase.domain.value/-turn/index.md)`, handicap: `[`Handicap`](../../com.example.syogibase.domain.value/-handicap/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>駒落ちのハンデを設定 |
| [setHint](set-hint.md) | `fun setHint(x: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, y: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, bool: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>指定されたマスに動かせるか設定 |

### Companion Object Properties

| Name | Summary |
|---|---|
| [COLS](-c-o-l-s.md) | `const val COLS: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [ROWS](-r-o-w-s.md) | `const val ROWS: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
