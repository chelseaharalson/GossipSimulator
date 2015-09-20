package project2

import akka.actor._
import scala.util.Random

/**
 * Created by chelsea on 9/15/15.
 */
class Worker(numOfNodes: Int, top: Int, alg: Int) extends Actor {

  def receive = {
    // Gossip Rumor
    case SendRumor(node,message) => {
      println("Node: " + node + " Message: " + message)
    }
    case msg: String => {
      println(msg)
      //context.actorSelection("../") ! msg
    }
  }

}
