package kim.nzxy.soon.services

import com.intellij.openapi.components.Service
import com.intellij.util.SmartList
import kim.nzxy.soon.entity.SoonInfo
import kim.nzxy.soon.entity.SoonPriority
import kim.nzxy.soon.entity.SoonStatus
import kim.nzxy.soon.manage.DatabaseManager
import kim.nzxy.soon.util.SoonUtil
import kotlin.random.Random

@Service(Service.Level.APP)
class SoonDataService() {
    private val dbManager = DatabaseManager.getInstance()

    fun findAll(): SmartList<SoonInfo> {
        val result = SmartList<SoonInfo>()
        for (i in 1..10) {
            val random = Random.nextInt(1, 100)
            result.add(
                SoonInfo(
                    i.toLong(),
                    SoonUtil.random(*SoonStatus.entries.toTypedArray()),
                    SoonUtil.random(*SoonPriority.entries.toTypedArray()),
                    "title$random",
                )
            )
        }
        return result
    }
}
