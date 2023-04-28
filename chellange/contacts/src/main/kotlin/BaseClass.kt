package contacts

import java.time.LocalDateTime
import kotlin.reflect.full.declaredMemberProperties

sealed class BaseClass(phones: String) {
    open val createdTime = LocalDateTime.now().toString()
    open var editTime: String? = null

    open var phone = validNumber(phones)
        set(value) {
            field = validNumber(value)
        }

    private fun validNumber(sample: String) =
        if (sample.matches(Regex("\\+?(\\(?[a-zA-Z0-9]\\)?)+([- ]?([a-zA-Z0-9]){2,})?([- ]?([a-zA-Z0-9]){2,})*")) ||
            sample.matches(Regex("\\+?([a-zA-Z0-9])+([- ]?(\\(?[a-zA-Z0-9]\\)?){2,})?([- ]?([a-zA-Z0-9]){2,})*"))
        ) {
            sample
        } else {
            println("Wrong number format!")
            ""
        }

    open fun searchQuery(query: String): Boolean {
        val params = this::class.declaredMemberProperties
        for (param in params) {
            if (param.call(this).toString().contains(query, true)) return true
        }
        return false
    }

    abstract fun print()
    abstract fun edit()
}