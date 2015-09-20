package project2

import akka.actor._
import scala.util.Random

/**
 * Created by chelsea on 9/15/15.
 */

object Main {
  var topologyType = Array("full", "3D", "line", "imp3D")
  var algorithmType = Array("gossip", "push-sum")

  def main(args: Array[String]) = {
    if (!isNumber(args(0))) {
      println("Invalid number of nodes!")
      System.exit(0)
    }
    if (!topologyType.contains(args(1))) {
      println("Invalid topology type!")
      System.exit(0)
    }
    if (!algorithmType.contains(args(2))) {
      println("Invalid algorithm type!")
      System.exit(0)
    }

    /*val t = new Topology()
    t.numOfNodes = args(0).toInt
    t.idx = 13
    t.topType = toppologyType.indexOf(args(1))
    println("Next Node = " + t.findNode())*/

    val system = ActorSystem("Gossip-PushSum")
    val master = system.actorOf(Props(new Master(args(0).toInt, topologyType.indexOf(args(1)), algorithmType.indexOf(args(2)))), "master")
    master ! "CreateActors"
  }

  // Checks if parameter is a number
  def isNumber(inputString: String): Boolean = {
    inputString.forall(_.isDigit)
  }

}
