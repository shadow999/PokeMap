package utils

import java.math.BigInteger
import java.nio.ByteBuffer
import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.security.spec.RSAPublicKeySpec
import java.util.Base64
import javax.crypto.Cipher

import org.apache.commons.codec.digest.DigestUtils

object GPSOAuthHelper {
  val b64Key: String = "AAAAgMom/1a/v0lblO2Ubrt60J2gcuXSljGFQXgcyZWveW" +
    "LEwo6prwgi3iJIZdodyhKZQrNWp5nKJ3srRXcUW+F1BD3baEVGcmEgqaL" +
    "ZUNBjm057pKRI16kB0YppeGx5qIQ5QjKzsR8ETQbKLNWgRY0QRNVz34kM" +
    "JR3P/LgHax/6rmf5AAAAAwEAAQ=="

  val googleAuthUrl: String = "https://android.clients.google.com/auth"

  lazy val decodedPublicKey: Array[Byte] = Base64.getDecoder.decode(b64Key)

  lazy val googlePublicKey: RSAPublicKey = {
    val decodedBase64: Array[Byte] = decodedPublicKey
    val i: Int = ByteBuffer.wrap(decodedBase64.slice(0, 4)).getInt
    val modulus: BigInteger = new BigInteger(1, decodedBase64.slice(4, i+4))
    val j: Int = ByteBuffer.wrap(decodedBase64.slice(i+4, i+8)).getInt
    val exponent: BigInteger = new BigInteger(1, decodedBase64.slice(i+8, i+8+j))
    val rsaPublicKeySpec: RSAPublicKeySpec = new RSAPublicKeySpec(modulus, exponent)
    KeyFactory.getInstance("RSA").generatePublic(rsaPublicKeySpec).asInstanceOf[RSAPublicKey]
  }

  def createMasterAuthParameters(email: String, password: String): Map[String, String] = {
    Map(
      "accountType" -> "HOSTED_OR_GOOGLE",
      "Email" -> email,
      "has_permission" -> "1",
      "add_account" -> "1",
      "EncryptedPasswd" -> createSignature(email, password),
      "service" -> "ac2dm",
      "source" -> "android",
      "androidId" -> AppConstant.GOOGLE_LOGIN_ANDROID_ID,
      "device_country" -> "us",
      "operatorCountry" -> "us",
      "lang" -> "en",
      "sdk_version" -> "17"
    )
  }

  def createOAuthParameters(email: String, masterToken: String): Map[String, String] = {
    Map(
      "accountType" -> "HOSTED_OR_GOOGLE",
      "Email" -> email,
      "has_permission" -> "1",
      "EncryptedPasswd" -> masterToken,
      "service" -> AppConstant.GOOGLE_LOGIN_SERVICE,
      "source" -> "android",
      "androidId" -> AppConstant.GOOGLE_LOGIN_ANDROID_ID,
      "app" -> AppConstant.GOOGLE_LOGIN_APP,
      "client_sig" -> AppConstant.GOOGLE_LOGIN_CLIENT_SIG,
      "device_country" -> "us",
      "operatorCountry" -> "us",
      "lang" -> "en",
      "sdk_version" -> "17"
    )
  }

  private def createSignature(email: String, password: String): String = {
    val prefix: Array[Byte] = Array[Byte](0x00.toByte)

    val hash: Array[Byte] = DigestUtils.sha1(decodedPublicKey).slice(0, 4)

    val cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding")
    cipher.init(Cipher.ENCRYPT_MODE, googlePublicKey)
    val encodedCredential = s"${email}\u0000${password}".getBytes("UTF-8")
    val encrypted = cipher.doFinal(encodedCredential)

    new String(Base64.getUrlEncoder.encode(prefix ++ hash ++ encrypted))
  }
}
