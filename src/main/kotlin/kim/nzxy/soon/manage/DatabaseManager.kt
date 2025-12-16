package kim.nzxy.soon.manage

import java.nio.file.Paths
import java.sql.Connection
import java.sql.DriverManager
import kotlin.io.path.absolutePathString


class DatabaseManager private constructor() {
    private val connection: Connection

    companion object {
        private const val DB_FILE_NAME = "soon_idea_plugin.db"
        private val INSTANCE = DatabaseManager()
        fun getInstance(): DatabaseManager = INSTANCE
    }

    init {
        val dbPath = Paths.get(System.getProperty("user.home")).resolve(".soon_plugin").resolve(DB_FILE_NAME)
        dbPath.toFile().parentFile.mkdirs()
        Class.forName("org.sqlite.JDBC")
        val url = "jdbc:sqlite:${dbPath.absolutePathString()}"
        connection = DriverManager.getConnection(url)

    }

    fun getConnection(): Connection = connection

    fun close() {
        try {
            connection.close()
        } catch (e: Exception) {
            println("关闭数据库失败: ${e.message}")
        }
    }
}