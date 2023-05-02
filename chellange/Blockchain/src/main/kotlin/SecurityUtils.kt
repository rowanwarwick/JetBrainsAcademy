import java.io.File
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec

object SecurityUtils {
    private val publicKeyFile = File("publicKey")
    private val privateKeyFile = File("privateKey")

    fun initSecurity() {
        if (!(publicKeyFile.exists() && privateKeyFile.exists())) {
            val keyGenerator = KeyPairGenerator.getInstance("RSA")
            keyGenerator.initialize(1024)
            val pair = keyGenerator.generateKeyPair()
            FileOutputStream(File("publicKey")).use { it.write(pair.public.encoded) }
            FileOutputStream(File("privateKey")).use { it.write(pair.private.encoded) }
        }
    }

    fun signTransaction(transactionData: String): ByteArray {
        val rsa = Signature.getInstance("SHA1withRSA")
        rsa.initSign(privateKey)
        rsa.update(transactionData.toByteArray())
        return rsa.sign()
    }

    fun verifySignature(data: ByteArray, signature: ByteArray, publicKey: PublicKey): Boolean {
        val sig = Signature.getInstance("SHA1withRSA")
        sig.initVerify(publicKey)
        sig.update(data)
        return sig.verify(signature)
    }

    val publicKey: PublicKey
        get() {
            val keyBytes = Files.readAllBytes(publicKeyFile.toPath())
            val spec = X509EncodedKeySpec(keyBytes)
            val kf = KeyFactory.getInstance("RSA")
            return kf.generatePublic(spec)
        }

    private val privateKey: PrivateKey
        get() {
            val keyBytes = Files.readAllBytes(privateKeyFile.toPath())
            val spec = PKCS8EncodedKeySpec(keyBytes)
            val kf = KeyFactory.getInstance("RSA")
            return kf.generatePrivate(spec)
        }

    fun applySHA256(input: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(input.toByteArray(StandardCharsets.UTF_8))
        val hexString = StringBuilder()
        for (elem in hash) {
            val hex = Integer.toHexString(0xff and elem.toInt())
            if (hex.length == 1) hexString.append('0')
            hexString.append(hex)
        }
        return hexString.toString()
    }
}