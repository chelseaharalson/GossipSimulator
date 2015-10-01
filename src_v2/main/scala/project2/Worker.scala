package project2

import akka.actor._
import scala.util.Random
import scala.concurrent.duration._
import java.util.concurrent.TimeUnit

/**
 * Created by chelsea on 9/15/15.
 */
class Worker(idx: Int, numOfNodes: Int, top: Int, alg: Int) extends Actor {

  var numOfMessages: Int = 0
  var gossipTermination: Int = 10
  var numOfTimesSent: Int = 2
  var messageCounter: Int = 0
  var cycleCounter: Int = 0
  var s: Double = idx
  var w: Double = 1
  var pushSumTermination: Int = 3
  var pushSumFinished: Boolean = false
  var failureTest: Boolean = false
  var failureIndex: Int = 5

  val t = new Topology(numOfNodes)    // comment out later

  def receive = {
    // Gossip Rumor
    case Rumor(message) => {
      numOfMessages = numOfMessages + 1
      if (numOfMessages <= gossipTermination) {
        //println("Index: " + idx + "; Num of Messages: " + numOfMessages)

        /*if (failureTest == false) {
          for (i <- 0 until numOfTimesSent) {
            val nextNode = t.findNode()
            //println("Index: " + t.idx + "   Next Node: " + nextNode)
            context.actorSelection("../" + nextNode.toString()) ! Rumor(message)
          }
        }*/

        context.parent ! GetRandomNumber(idx)



      }
      //println("Num Of Messages: " + numOfMessages + "   Index: " + idx)
      if (numOfMessages == gossipTermination) {
        //println("Index Finished: " + idx)
        context.parent ! FinishGossip(idx)
      }
    }
    case PushSum(ps,pw) => {
      if (pushSumFinished == false) {
        if (((((s + ps) / 2) / ((w + pw) / 2)) - (s / w)) < (10E-10)) {
          cycleCounter = cycleCounter + 1
        }
        else if (cycleCounter < 3) {
          cycleCounter = 0
        }
        if (cycleCounter < 3) {

        /*if (idx == 3) {
          println("Index: " + idx + "   s: " + s + "   ps: " + ps)
        }*/

          //println("Index: " + idx + "   s: " + s + "   ps: " + ps)

          s = (s + ps) / 2
          w = (w + pw) / 2
          //val t = new Topology(numOfNodes)
          //t.numOfNodes = numOfNodes
          t.topType = top
          t.idx = idx
          for (i <- 0 to numOfTimesSent) {
            val nextNode = t.findNode()
            context.actorSelection("../" + nextNode.toString()) ! PushSum(s, w)
          }
        }
        else {
          pushSumFinished = true
        }
        if (cycleCounter == pushSumTermination) {
          context.parent ! FinishPushSum(idx, s, w)
        }
      }
    }
    case SendToWorker(idx) => {
      context.actorSelection("../" + idx.toString()) ! Rumor("hi")
    }
    case msg: String => {
      println(msg)
    }
  }

}
