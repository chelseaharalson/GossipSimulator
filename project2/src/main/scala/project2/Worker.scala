package project2

import akka.actor._
import scala.util.Random

/**
 * Created by chelsea on 9/15/15.
 */
class Worker(numOfNodes: Integer, topology: String, alg: String) extends Actor {

  def receive = {
    case PushSum(s, w) => {

    }
  }

}
