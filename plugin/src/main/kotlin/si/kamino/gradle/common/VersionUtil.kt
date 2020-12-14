package si.kamino.gradle.common

object VersionUtil {

    fun is410OrAbove(): Boolean {
        return versionCompare(com.android.Version.ANDROID_GRADLE_PLUGIN_VERSION, "4.1.0") >= 0
    }

    private fun versionCompare(str1: String, str2: String): Int {
        val vals1: List<String> = str1.split("-")[0].split(".")
        val vals2: List<String> = str2.split("-")[0].split(".")
        var i = 0
        // set index to first non-equal ordinal or length of shortest version string
        // set index to first non-equal ordinal or length of shortest version string
        while (i < vals1.size && i < vals2.size && vals1[i] == vals2[i]) {
            i++
        }

        // compare first non-equal ordinal number

        // compare first non-equal ordinal number
        return if (i < vals1.size && i < vals2.size) {
            val diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]))
            Integer.signum(diff)
        } else {
            Integer.signum(vals1.size - vals2.size)
        }
    }

}