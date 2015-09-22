package project2

import akka.actor._

/**
 * Created by chelsea on 9/15/15.
 */
class Worker(idx: Int, numOfNodes: Int, top: Int, alg: Int) extends Actor {

  var numOfMessages: Int = 0
  var gossipTermination: Int = 10
  var numOfTimesSent: Int = 6
  var messageCounter: Int = 0
  var cycleCounter: Int = 0
  var s: Double = idx
  var w: Double = 1

  def receive = {
    // Gossip Rumor
    case Rumor(message) => {
      numOfMessages = numOfMessages + 1
      if (numOfMessages <= gossipTermination) {
        //println("Index: " + idx + "; Num of Messages: " + numOfMessages)
        val t = new Topology()
        t.numOfNodes = numOfNodes
        t.topType = top
        t.idx = idx
        //println(t.findNode())
        // Go through 10 times and send to new random node
        // Guarantee finish if equal to number of nodes
        for (i <- 0 to numOfTimesSent) {
          val nextNode = t.findNode()
          context.actorSelection("../" + nextNode.toString()) ! Rumor(message)
        }
      }
      //println("Num Of Messages: " + numOfMessages + "   Index: " + idx)
      if (numOfMessages == gossipTermination) {
        println("Index Finished: " + idx)
        context.parent ! FinishGossip()
      }
    }
    case PushSum(ps,pw) => {
      println("Index: " + idx + "   s: " + s + "   ps: " + ps)
      if ( ((((s + ps) / 2) / ((w + pw) / 2)) - (s / w)) < (10E-10) ) {
        cycleCounter = cycleCounter + 1
      }
      else if (cycleCounter < 3) {
        cycleCounter = 0
      }
      if (cycleCounter < 3) {
        s = (s + ps) / 2
        w = (w + pw) / 2
        val t = new Topology()
        t.numOfNodes = numOfNodes
        t.topType = top
        t.idx = idx
        for (i <- 0 to numOfTimesSent) {
          val nextNode = t.findNode()
          context.actorSelection("../" + nextNode.toString()) ! PushSum(s, w)
        }
      }
      if (cycleCounter == 3) {
        context.parent ! FinishPushSum(s, w)
      }
    }
    case msg: String => {
      println(msg)
    }
  }

}
