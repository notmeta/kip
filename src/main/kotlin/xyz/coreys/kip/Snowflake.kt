package xyz.coreys.kip

/*
 * Created in reference to Twitters old ID-generator by the same name.
 * https://github.com/twitter/snowflake
 */

private const val KIP_EPOCH_IN_SECONDS = 1523480000

class Snowflake(
    private val sequenceBits: Int
) {

    private val sequenceMask = -1 xor (-1 shl sequenceBits)
    private var sequence = 0

    private var lastTimestamp = -1L

    val nextId: Long @Synchronized get() {
        var timestamp = getKipTimestamp()

        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) and sequenceMask
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp)
            }
        } else {
            sequence = 0
        }

        lastTimestamp = timestamp
        return (timestamp shl sequenceBits) or sequence.toLong()
    }

    private fun tilNextMillis(lastTimestamp: Long): Long {
        var timestamp = getKipTimestamp()
        while (timestamp <= lastTimestamp) {
            timestamp = getKipTimestamp()
        }
        return timestamp
    }

    private fun getKipTimestamp(): Long = (System.currentTimeMillis() / 1000) - KIP_EPOCH_IN_SECONDS

}
