package kim.nzxy.soon.util

import kotlin.random.Random


object SoonUtil {
    fun <T> random(vararg items: T): T {
        return items[Random.nextInt(0, items.size)]
    }
}