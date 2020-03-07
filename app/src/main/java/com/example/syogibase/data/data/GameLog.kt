package com.example.syogibase.data.data

//１局の対局ログ格納クラス

class GameLog(val preX:Int,var preY:Int,val afterPiece:Piece, val afterTurn:Int,
              val newX:Int, val newY:Int, val beforpiece:Piece, val beforturn:Int)