package org.matruss.labyrinth.model

import com.typesafe.config.ConfigFactory

import org.junit.runner.RunWith
import org.scalatest.{FreeSpec, Matchers}
import org.scalatest.junit.JUnitRunner
import org.matruss.labyrinth.config.LabyrinthConfiguration
import org.matruss.labyrinth.URIUtils._

@RunWith(classOf[JUnitRunner])
class WebLinkSpec extends FreeSpec with Matchers {
  private val conf = LabyrinthConfiguration( ConfigFactory.load("application.test.conf") )

  "Web link class" - {
    "should correctly define if it is external" in {
      val internalLink = WebLink( buildURI("www.google.com"),"/cookies", conf.site)
      internalLink.isExternal shouldBe false

      val externalLink = WebLink( buildURI("www.google.com"),"www.facebook.com", conf.site)
      externalLink.isExternal shouldBe true
    }
    "should correctly define if it is to follow" in {
      val toFollowBare = WebLink( buildURI("www.google.com"),"", conf.site)
      toFollowBare.toFollow shouldBe true

      val toFollowWithPath = WebLink( buildURI("www.google.com"),"/cookies", conf.site)
      toFollowWithPath.toFollow shouldBe true

      val notToFollow = WebLink( buildURI("www.google.com"),"/blog", conf.site)
      notToFollow.toFollow shouldBe false

      val notToFollowDeep = WebLink( buildURI("www.google.com"),"/about/blogs", conf.site)
      notToFollowDeep.toFollow shouldBe false
    }
  }
}
