package briiqn.lol

import java.io.File

object Main {
    private var encryptor: EncryptionManager = EncryptionManager()
    @JvmStatic
    fun main(args: Array<String>) {
        encryptor.encryptDirectory(File("/home/brian/Downloads/Cheese").toPath(), "jpg")
    }
}
