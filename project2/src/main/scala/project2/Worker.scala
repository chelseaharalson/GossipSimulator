package project2

import akka.actor._
import scala.util.Random

/**
 * Created by chelsea on 9/15/15.
 */
class Worker(numOfNodes: Int, top: Int, alg: Int) extends Actor {

  def receive = {
    case Gossip => {

    }
  }

}
