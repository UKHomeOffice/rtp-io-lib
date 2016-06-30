package uk.gov.homeoffice.crypt

import org.specs2.mutable.Specification

class HmacSha256Spec extends Specification {
  val signingKey = "OriginalMarked5D1stinct7on"

  "HmacSha256 create" should {
    "should create a base 64 encoded signature for a given data and signing password" in {
      val expectedData = Array(100, -47, 99, -15, -15, -6, -95, -1, -49, 63, -56, 82, -123, -104, -90, -65, -70, 74, -110, 90, -32, 3, -98, -84, -42, -117, 102, 87, -38, 23, -70, -45)
      val hmac256 = new HmacSha256
      hmac256.create(signingKey, "test") mustEqual expectedData
    }
  }
}