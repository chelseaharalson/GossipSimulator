package project2

import akka.actor._
import scala.util.Random

/**
 * Created by chelsea on 9/15/15.
 */
class Worker(numOfNodes: Int, top: Int, alg: Int) extends Actor {

  def receive = {
    // Gossip Rumor
    case SendRumor(node,message,top) => {
      println("Node: " + node + " Message: " + message + " Topology Type: " + top)
      val t = new Topology()
      t.topType = top
      t.numOfNodes = numOfNodes
      val nextNode = t.findNode()
      //println(t.findNode())
      for (i <- 0 to 10) {
        context.actorSelection("../" + t.findNode().toString()) ! SendRumor(nextNode, message, t.topType)
      }
    }
    case msg: String => {
      println(msg)
    }
  }

}
