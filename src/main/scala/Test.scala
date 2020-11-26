import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import play.api.libs.json.{JsObject, Json, OFormat}
import sttp.model.StatusCode
import sttp.tapir.swagger.akkahttp.SwaggerAkka

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}

object Test extends App {

  case class Error1(message: String, data: JsObject)
  case class Error2(message: String)

  implicit val error1Fmt: OFormat[Error1] = Json.format
  implicit val error2Fmt: OFormat[Error2] = Json.format

  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "test")
  implicit val ec: ExecutionContext = system.executionContext

  import sttp.tapir._
  import sttp.tapir.generic.auto._
  import sttp.tapir.json.play._

  val testEndpoint = endpoint
    .in("test")
    .get
    .out(stringBody.example("OK"))
    .errorOut(
      oneOf(
        statusMapping(
          StatusCode.BadRequest,
          jsonBody[Error1].description("Bad request - Error 1.")
        ),
        statusMapping(
          StatusCode.BadRequest,
          jsonBody[Error2].description("Bad request - Error 2.")
        )
      )
    )
    .serverLogic[Future](_ => Future.successful(Right("OK")))

  import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
  import sttp.tapir.openapi.circe.yaml._
  val openApi = OpenAPIDocsInterpreter.toOpenAPI(testEndpoint, title = "BOWI API", version = "0.1").toYaml
  println("begin openapi.yaml:\n")
  println(openApi)
  println("end openapi.yaml\n")

  import sttp.tapir.server.akkahttp._
  import akka.http.scaladsl.server.Directives._
  val route = concat(AkkaHttpServerInterpreter.toRoute(testEndpoint), new SwaggerAkka(openApi).routes)

  Http()
    .newServerAt("localhost", 8080)
    .bind(route)
    .flatMap { binding =>
      println(s"Server online at http://localhost:8080/")
      println(s"Documentation available at http://localhost:8080/docs")
      println("Press enter key to exit ...")
      scala.io.StdIn.readLine()
      binding.terminate(5.seconds)
    }
    .onComplete(_ => system.terminate())

}
