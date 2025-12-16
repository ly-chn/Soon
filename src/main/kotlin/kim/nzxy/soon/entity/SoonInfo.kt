package kim.nzxy.soon.entity

/**
 * 实体类
 */
data class SoonInfo(
    val id: Long,
    val group: String = "未分组",
    val status: SoonStatus,
    val priority: SoonPriority,
    val title: String,
)

interface BasicEnum : Comparable<BasicEnum> {
    val sort: Int
    val shortTitle: String
    val fullTitle: String

    override fun compareTo(other: BasicEnum): Int = sort.compareTo(other.sort)
}

/**
 * 状态
 */
enum class SoonStatus(
    override val sort: Int,
    override val shortTitle: String,
    override val fullTitle: String,
) : BasicEnum {
    WAITING(1, "\uD83D\uDCA1", "\uD83D\uDCA1待办"),
    ONGOING(2, "⏳", "⏳在办"),
    SETTLED(3, "✅", "✅已办"),
}

/**
 * 优先级
 */
enum class SoonPriority(
    override val sort: Int,
    override val shortTitle: String,
    override val fullTitle: String,
) : BasicEnum {
    L1(1, "\uD83D\uDFE2", "\uD83D\uDFE2低"),
    L2(2, "\uD83D\uDFE1", "\uD83D\uDFE1中"),
    L3(3, "\uD83D\uDD34", "\uD83D\uDD34高"),
}