package project2

import akka.actor._
import scala.math._

/**
 * Created by chelsea on 9/15/15.
 */
class Master(numOfNodes: Int, top: Int, alg: Int) extends Actor {

  var startTime: Long = 0

  def receive = {
    case msg: String => {
      if (msg.equals("CreateActors")) {
        //println("It works!!!")
        var numNodes: Int = numOfNodes
        if (top == 1 || top == 3) {
          var numNodes = pow(numOfNodes,3).toInt
        }
        for (i <- 0 to numNodes) {
          // Create the actors
          context.actorOf(Props(new Worker(numNodes, top, alg)), i.toString)
        }
      }
    }
  }

}
