package uk.gov.homeoffice.crypt

/**
  * Secrets for encryption/decryption.
  * @param encryptionKey String
  * @param signingPassword String
  * @param algorithm String Make sure this matches the algorithm in the transformation.
  * @param transformation String of the form "algorithm/mode/padding" or just "algorithm".
  */
case class Secrets(encryptionKey: String, signingPassword: String, algorithm: String = "AES", transformation: String = "AES/CBC/PKCS5Padding")