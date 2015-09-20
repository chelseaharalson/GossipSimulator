package project2

import akka.actor._
import scala.util.Random

/**
 * Created by chelsea on 9/15/15.
 */
class Worker(idx: Int, numOfNodes: Int, top: Int, alg: Int) extends Actor {

  var numOfMessages: Int = 0

  def receive = {
    // Gossip Rumor
    case Rumor(message) => {
      println("Message: " + message)
      val t = new Topology()
      t.numOfNodes = numOfNodes
      t.topType = top
      t.idx = idx
      val nextNode = t.findNode()
      //println(t.findNode())
      context.actorSelection("../" + nextNode.toString()) ! Rumor(message)
    }
    case msg: String => {
      println(msg)
    }
  }

}
