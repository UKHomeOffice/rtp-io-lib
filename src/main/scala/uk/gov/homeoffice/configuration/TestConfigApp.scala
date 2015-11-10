package uk.gov.homeoffice.configuration

import com.typesafe.config.ConfigFactory

/**
 * In IntelliJ, right click this file and choose "Run TestConfigApp".
 * The first time will give the exception:
 * com.typesafe.config.ConfigException$Missing: No configuration setting found for key 'welcome'
 *
 * The default configuration file is application.conf.
 * This library does not provide one and so the configuration property required by this test application will be missing by default.
 *
 * Now edit the run configuration to include the "VM options":
 * -Dconfig.resource=application.example.conf
 */
object TestConfigApp extends App {
  val config = ConfigFactory.load()

//  println(config.getString("welcome"))   - doesnt work so rather silly example App - fix or delete
}