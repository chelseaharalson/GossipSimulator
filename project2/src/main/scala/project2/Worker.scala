package project2

import akka.actor._
import scala.util.Random
import scala.collection.mutable.ArrayBuffer

/**
 * Created by chelsea on 9/15/15.
 */
class Worker(nodeName: Int, numOfNodes: Int, top: Int, alg: Int) extends Actor {

  var numOfMessagesReceived: Int = 0
  var gossipTermination: Int = 10
  var messageCounter: Int = 0
  var cycleCounter: Int = 0
  var s: Double = nodeName
  var w: Double = 1
  var pushSumTermination: Int = 3
  var failureTest: Boolean = false
  var failureIndex: Int = 5
  var nodeList = new ArrayBuffer[Int]()
  var receivedMessage = ""
  var finished = false
  var receivedSW = false

  // Full
  if (top == 0) {
    for (i <- 0 until numOfNodes) {
      nodeList.+=(i)
    }
    nodeList.remove(nodeName)
  }
  // 3D
  else if (top == 1) {
    val t = new Topology()
    nodeList = t.getCubeNeighbors(nodeName,numOfNodes)
  }
  // Line
  else if (top == 2) {
    if (nodeName == 0) {
      // Can only move to right if first one
      nodeList.+=(1)
    }
    else if (nodeName == numOfNodes-1) {
      // If last one, move to the left
      nodeList.+=(numOfNodes-2)
    }
    else {
      nodeList.+=(nodeName-1)
      nodeList.+=(nodeName+1)
    }
  }
  // Imperfect 3D
  else if (top == 3) {
    val t = new Topology()
    t.imperfect = true
    nodeList = t.getCubeNeighbors(nodeName,numOfNodes)
  }

  def receive = {
    // Gossip Rumor
    case Rumor(message) => {
      receivedMessage = message
      numOfMessagesReceived = numOfMessagesReceived + 1
      finished = numOfMessagesReceived > gossipTermination
    }
    case PushSum(ps,pw) => {
      receivedSW = true
      if (finished == false) {
        if (((((s + ps) / 2) / ((w + pw) / 2)) - (s / w)) < (10E-10)) {
          cycleCounter = cycleCounter + 1
        }
        else if (cycleCounter < 3) {
          cycleCounter = 0
        }

        if (cycleCounter < 3) {
          s = (s + ps) / 2
          w = (w + pw) / 2
        }
        else {
          finished = true
        }
      }
    }
    case GetProgress() => {
      if (receivedMessage != "" || receivedSW == true) {
        if (!finished) {
          if (failureTest == false) {
            val nextNode = getRandomNode()
            if (receivedSW == true) {
              context.actorSelection("../" + nextNode.toString()) ! PushSum(s,w)
            }
            else {
              context.actorSelection("../" + nextNode.toString()) ! Rumor(receivedMessage)
            }
          }
        }
      }
      if (finished) {
        // 1 means finished
        context.parent ! SendProgress(nodeName,1)
      }
      else {
        // 0 means still sending
        context.parent ! SendProgress(nodeName,0)
      }
    }
  }

  def getRandomNode(): Int = {
    var nextNode = 0
    var pos = 0
    top match {
      // Full
      case 0 => {
        nextNode = Random.nextInt(nodeList.size)
      }
      // 3D
      case 1 => {
        pos = Random.nextInt(nodeList.size)
        nextNode = nodeList(pos)
      }
      // Line
      case 2 => {
        val moveLR = Random.nextInt(2)
        if (nodeList.size == 1) {
          nextNode = nodeList(0)
        }
        else {
          if (moveLR == 1) {
            // Move to the right
            nextNode = nodeList(1)
          }
          else {
            // Move to the left
            nextNode = nodeList(0)
          }
        }
      }
      // Imperfect 3D
      case 3 => {
        pos = Random.nextInt(nodeList.size)
        nextNode = nodeList(pos)
      }
    }
    nextNode
  }

}