package uk.gov.homeoffice.json.stuff

case class MyStuff(id: String, moreStuff: MoreStuff)

case class MoreStuff(howMuch: Int)