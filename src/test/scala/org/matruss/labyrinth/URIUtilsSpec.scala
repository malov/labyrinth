package org.matruss.labyrinth

import org.junit.runner.RunWith
import org.scalatest.{FreeSpec, Matchers}
import org.scalatest.junit.JUnitRunner
import URIUtils._

import scala.util.{Success, Try}

@RunWith(classOf[JUnitRunner])
class URIUtilsSpec extends FreeSpec with Matchers {

  "URI Utility method" - {
    "should build proper absolute URI" in {
      Try { buildURI("http://www.google.com") } shouldBe a [Success[_]]
      val uri = buildURI("http://www.google.com")
      uri.toString shouldEqual "http://www.google.com"
      uri.getScheme shouldEqual "http"
      uri.getHost shouldEqual "www.google.com"
    }
    "should add protocol if necessary" in {
      val uri = buildURI("www.google.com")
      uri.toString shouldEqual "http://www.google.com"
      uri.getScheme shouldEqual "http"
      uri.getHost shouldEqual "www.google.com"
    }
    "should build URI out of two proper strings" in {
      val uri = buildURI("www.google.com","/policies")
      uri.toString shouldEqual "http://www.google.com/policies"
      uri.getScheme shouldEqual "http"
      uri.getHost shouldEqual "www.google.com"
      uri.getPath shouldEqual "/policies"
    }
    "should build URI out of another URI and relative path" in {
      val uri = buildURI( buildURI("www.google.com"),"/policies")
      uri.toString shouldEqual "http://www.google.com/policies"
      uri.getScheme shouldEqual "http"
      uri.getHost shouldEqual "www.google.com"
      uri.getPath shouldEqual "/policies"
    }
    "should build URI out of relative path if it is in fact URI" in {
      val uri = buildURI( buildURI("www.google.com"),"http://www.facebook.com")
      uri.toString shouldEqual "http://www.facebook.com"
      uri.getScheme shouldEqual "http"
      uri.getHost shouldEqual "www.facebook.com"
    }
    "should build URI out of relative path if it is in fact URI without protocol" in {
      val uri = buildURI( buildURI("www.google.com"),"www.facebook.com")
      uri.toString shouldEqual "http://www.facebook.com"
      uri.getScheme shouldEqual "http"
      uri.getHost shouldEqual "www.facebook.com"
    }
  }
}
