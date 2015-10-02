package project2

/**
 * Created by chelsea on 9/15/15.
 */

case class Gossip()
case class Rumor(message: String)
case class FinishGossip(idx: Int)
case class PushSum(s: Double, w: Double)
case class FinishPushSum(idx: Int, s: Double, w: Double)