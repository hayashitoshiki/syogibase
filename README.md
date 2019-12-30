# syogibase

## 将棋アプリ

大学卒業時に作った将棋アプリを見返してみるとひどすぎたため、
アーキテクチャ設計の勉強がてら拡張元となる普通の将棋の作成

#### 言語
Kotlin

#### アーキテクチャ
MVP

#### 仕様ライブラリ
Koin

#### 将棋に関連する各クラスの詳細  

*	View  
      * GameView・・・画面表示  
*	Presenter  
      * GameLogicPresenter・・・将棋ロジック  
*	Data  
    * Repository  
        * BoardReoisutory・・・盤面データ取得  
    * Model  
      * Beard・・・将棋盤の情報  
      * Cell・・・１マスの情報  
      * Piece・・・駒の情報  
      * GameLog・・・一局のログデータ  
