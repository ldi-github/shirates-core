package shirates.core.utility.misc

import java.net.URL


object UrlUtility {

    /**
     * isLocal
     */
    fun isLocal(url: String?): Boolean {

        try {
            val u = URL(url)
            return u.host == "127.0.0.1" || u.host == "localhost" || u.host == "::1"
        } catch (t: Throwable) {
            return true
        }
    }

    /**
     * isRemote
     */
    fun isRemote(url: String?): Boolean {

        return isLocal(url = url).not()
    }
}