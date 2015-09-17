package project2

import akka.actor._
import scala.util.Random

/**
 * Created by chelsea on 9/15/15.
 */
class Master(topology: Topology, alg: String, numOfNodes: String) extends Actor {

  def receive = {
    case PushSum(s, w) => {

    }
  }

}
