package simulations

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._


class BaseSimulation extends RunTimeParameters {

  val getscenario = scenario("GET")
        .exec(http("GET")
          .get("/employees")
          .headers(Map("Content-Type" -> "application/json"))
          .check(status.is(200))
          .check(status.in(200 to 210))
          .check(jsonPath("$[0].role").is("burglar"))
          .check(jsonPath("$[1].name").saveAs("secondName")))
          .exec(session => {
            val mySesionVariable = session("secondName").as[String]
            println(mySesionVariable)
            session
          })

  val postscenario = scenario("POST")
    .feed(csv("data/employeeFeeder.csv"))
    .exec(
      http("POST")
          .post("/employees")
          .headers(Map("Content-Type" -> "application/json"))
          .body(ElFileBody("bodies/divya.json"))
          .check(status.is(200))
          .check(bodyString.saveAs("post_response"))).exitHereIfFailed


 setUp(getscenario.inject(atOnceUsers(userCount)))
    .protocols(httpProtocol)
    .maxDuration(testDuration)
    .assertions(
      global.responseTime.max.lessThan(100),
      global.successfulRequests.percent.greaterThan(95))
}
