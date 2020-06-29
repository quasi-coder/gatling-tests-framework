package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class RunTimeParameters extends Simulation{

  def userCount: Int = getProperty("USERS", "1").toInt
  def rampDuration: Int = getProperty("RAMP_DURATION", "10").toInt
  def testDuration: Int = getProperty("DURATION", "60").toInt
  def baseURL: String = getProperty("BASEURL","http://localhost:8080")

  val httpProtocol = http.baseURL(baseURL)

  /** Will look for the environment variable of the specified name,
   * and if it can’t find it will then look for the system property of the same name.
   * If it can’t find that either, it will default to some value that we specify.
   * */
  private def getProperty(propertyName: String, defaultValue: String) = {
    Option(System.getenv(propertyName))
      .orElse(Option(System.getProperty(propertyName)))
      .getOrElse(defaultValue)
  }
}



