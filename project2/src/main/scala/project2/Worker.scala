package project2

import akka.actor._

/**
 * Created by chelsea on 9/15/15.
 */
class Worker(idx: Int, numOfNodes: Int, top: Int, alg: Int) extends Actor {

  var numOfMessages: Int = 0
  var gossipTermination: Int = 10
  var numOfTimesSent: Int = numOfNodes

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
        context.parent ! Finish()
      }
    }
    case msg: String => {
      println(msg)
    }
  }

}
