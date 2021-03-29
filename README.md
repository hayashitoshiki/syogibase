# syogibase

## 将棋アプリ

~~大学卒業時に作った将棋アプリを見返してみるとひどすぎたため、アーキテクチャ設計の勉強がてら~~  
拡張元となる普通の将棋の作成

### 主な機能

- [x] 対局
  - [x] 千日手
  - [x] トライルール
  - [ ] 27点法
- [x] 棋譜の保存
- [x] 棋譜の再生
- [x] 棋譜の検討

### 言語
Kotlin

### 仕様ライブラリ
Koin

### 主な使い方
1. Viewの生成  

xmlで生成
  ```xml:Fragment
<com.example.syogibase.presentation.view.GameView
        android:layout_width="match_parent"
        android:layout_height="250dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
```

ktで生成
```kt
 val gameView = GameView(this)
```

2. 対局終了通知リスナー設定　　
```kotlin
 gameView.setGameEndListener(object : GameViewContact.GameEndListener {
            override fun onGameEnd(winner: Turn?) {
                // TODO : 対局終了後処理
            }
        } )
```
  
###  イメージ
####  対局画面
<img src="https://github.com/teaTreeTree/syogibase/blob/master/capture/gameStart_capture.png" width="400">  


### 主なソース
https://github.com/hayashitoshiki/syogibase/tree/master/app/src/main

### ソースドキュメント
https://github.com/hayashitoshiki/syogibase/blob/master/app/doc/app/index.md
