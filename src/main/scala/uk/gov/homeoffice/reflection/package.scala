package uk.gov.homeoffice

import org.joor.Reflect._

package object reflection {
  type FullClassName = String

  /**
    * Instantiate a given class by either its default constructor or one with arguments.
    * The given class name must be a full name including the class's package e.g. java.lang.String
    * @param fullClassName Name of class such as java.lang.String
    * @param constructorArgs Any arguments according to the desired constructor to be used for instantiation
    * @tparam C Class type being instantiated - if not stipulated be wary of possible ClassCastException at runtime
    */
  def instantiate[C](fullClassName: FullClassName, constructorArgs: AnyRef*): C = {
    on(fullClassName).create(constructorArgs: _*).get.asInstanceOf[C]
  }
}