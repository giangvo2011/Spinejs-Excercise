package models
import scala.io.Source
import java.io.PrintWriter

class FileProcessUtils() {
  
  val dbName = "/home/giangvo/Desktop/demoplay/db/db"
  
  def readDB:String = {
    return Source.fromFile(dbName).mkString
  }
  
  def writeDB(value:String) = {
    val out = new PrintWriter(dbName)
    try{ out.print(value) }
    finally{ out.close }
  }
  
}
