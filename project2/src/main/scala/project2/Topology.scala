package project2

import scala.math._
import scala.util.Random
import java.util.Scanner
import scala.collection.mutable.ArrayBuffer

/**
 * Created by chelsea on 9/15/15.
 */
class Topology() {

  var imperfect: Boolean = false

  def getCubeNeighbors(nodeName: Int, size: Int): ArrayBuffer[Int] = {
    val cubeFactor = getCubeFactor(size)
    var cubeListString = ""
    var cubeEdges = 0
    val nodesCube = pow(cubeFactor,3)
    var cubeStr = ""
    var result = new ArrayBuffer[Int]()
    var nodes: Int = 0
    var edges: Int = 0

    var node1: Int = 0
    var node2: Int = 0

    for (row <- 0 until cubeFactor) {
      for (col <- 0 until cubeFactor) {
        for (depth <- 0 until cubeFactor) {
          val currentNode = depth + (col * cubeFactor) + (row * cubeFactor * cubeFactor)

          // If not last depth
          if(depth != cubeFactor-1) {
            if ((currentNode < nodesCube) && ((currentNode+1) < nodesCube)) {
              cubeListString += "%d %d\n".format(currentNode, currentNode+1)
              cubeEdges = cubeEdges + 1
            }
          }

          // If not last col
          if(col != cubeFactor-1) {
            if ((currentNode < nodesCube) && ((currentNode + cubeFactor) < nodesCube)) {
              cubeListString += "%d %d\n".format(currentNode, currentNode + cubeFactor)
              cubeEdges = cubeEdges + 1
            }
          }

          // If not last row
          if(row != cubeFactor-1) {
            if ((currentNode < nodesCube) && ((currentNode + (cubeFactor * cubeFactor)) < nodesCube)) {
              cubeListString += "%d %d\n".format(currentNode, currentNode + (cubeFactor * cubeFactor))
              cubeEdges = cubeEdges + 1
            }
          }
        }
      }
    }
    // return Nodes, Edges, Cube List
    //System.out.println(cubeListString)
    //"%d %d\n%s".format(cubeFactor*cubeFactor*cubeFactor, cubeEdges, cubeListString)
    cubeStr = "%d %d\n%s".format(cubeFactor*cubeFactor*cubeFactor, cubeEdges, cubeListString)

    //println(cubeStr)

    val scan: Scanner = new Scanner(cubeStr)
    nodes = scan.nextInt()
    edges = scan.nextInt()
    //println("Nodes: " + nodes + "    Edges: " + edges)

    for (i <- 0 until edges) {
      node1 = scan.nextInt()
      node2 = scan.nextInt()

      if (node1 == nodeName) {
        result.+=(node2)
      }
      if (node2 == nodeName) {
        result.+=(node1)
      }
    }
    if (imperfect == true) {
      var r = 0
      do {
        r = Random.nextInt(nodesCube.toInt)
      } while (result.indexOf(r) > -1)
      result.+=(r)
    }
    result
  }

  // Gets smallest cube factor
  def getCubeFactor(numOfNodes: Int): Int = {
    var factor: Int = 3
    while (numOfNodes > pow(factor,3)) {
      factor = factor + 1
    }
    factor
  }

}