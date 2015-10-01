package project2

/**
 * Created by chelsea on 9/15/15.
 */

case class Gossip()
case class Rumor(message: String)
case class PushSum(s: Double, w: Double)
case class FinishPushSum(nodeName: Int, s: Double, w: Double)
case class GetProgress()
case class SendProgress(nodeName: Int, result: Int)