package com.example.syogibase.data

import com.example.syogibase.data.local.Cell
import com.example.syogibase.data.local.GameLog
import com.example.syogibase.data.local.Piece
import com.example.syogibase.data.local.PieceMove


interface BoardRepository {

    //動かす前の駒の状態をセット
    fun setPre(x:Int, y:Int)
    //最新手を返す
    fun getLogList(): GameLog
    //駒を動かす
    fun setMove(x:Int,y:Int, turn:Int,evolution:Boolean)
    //１手戻す
    fun setBackMove()
    //成る
    fun setEvolution()
    //成れる駒か判別
    fun findEvolutionBy(x:Int, y:Int):Boolean
    //打った駒の打つ前のY軸取得
    fun findLogY():Int
    //ヒントが表示されているマスの数を返す
    fun getCountByHint():Int
    //持ち駒リスト取得
    fun getAllHoldPiece(turn:Int):Map<Piece,Int>
    //持ち駒の数を取得
    fun getCountHoldPiece(turn:Int):Int
    //持ち駒マスから取得
    fun findHoldPieceBy(i:Int, turn:Int):Piece
    //取った駒を表示
    fun getTakePice():Piece
    //持ち駒追加
    fun setHoldPiece()
    //強制的にならないといけない駒かチェック
    fun checkForcedevolution():Boolean
    //駒の動きを取得
    fun getMove(x:Int, y:Int):Array<Array<PieceMove>>
    //ヒントセット
    fun setHint(x:Int, y:Int)
    //ヒントリセット
    fun resetHint()
    //そのマスの駒の所有者を返す
    fun getTurn(x:Int, y:Int):Int
    //そのマスのヒントを返す
    fun getHint(x:Int, y:Int):Boolean
    //そのマスの駒を返す
    fun getPiece(x:Int, y:Int): Piece
    //そのマスの駒の名前を返す
    fun getJPName(x:Int, y:Int):String
    //指定した手番の王様の座標を返す
    fun findKing(turn:Int):Pair<Int, Int>
    //指定したマスの情報を返す
    fun getCellInformation(x:Int, y:Int): Cell
}