package project2

import scala.util.Random

/**
 * Created by chelsea on 9/15/15.
 */
class Topology() {

  var idx: Int = 0
  var topType: Int = 0
  var numOfNodes: Integer = 10 // will change

  def findNode() = {
    var nextNode = Random.nextInt(numOfNodes)

    topType match {
      // Full
      case 0 => {
        do {
          nextNode = Random.nextInt(numOfNodes)
        } while (nextNode == idx)
      }
      // 3D
      case 1 => {
        var sideNodes = 3
        val cube = new Cube()
        nextNode = cube.getRandomCubeNeighbor(cube.generateCubeGraph(sideNodes),idx)
      }
      // Line
      case 2 => {
        var lr = Random.nextInt(2)
        if (idx == 0) {
          nextNode = idx + 1
        }
        else if (idx == numOfNodes-1) {
          nextNode = idx - 1
        }
        else if (lr == 0) {
          nextNode = idx - 1
        }
        else if (lr == 1) {
          nextNode = idx + 1
        }
      }
      // Imperfect 3D
      case 3 => {

      }
    }
    nextNode
  }

}
