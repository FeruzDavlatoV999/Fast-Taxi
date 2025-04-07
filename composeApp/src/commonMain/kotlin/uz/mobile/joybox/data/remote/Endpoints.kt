package uz.mobile.joybox.data.remote

val regex = "\\{([^{}]*)\\}".toRegex()



object UserEndpoints{
    const val GET_USER = "api/v1/auth/me"
    const val GET_CATEGORIES = "api/v1/categories"
    const val GET_MOVIES = "api/v1/courses"
    const val SET_WATCH_INFO = "api/v1/user-watch-histories/update"
    const val GET_TAGS = "api/v1/tags"
    const val PAYMENT_TYPES = "api/v1/payment_systems"
    const val GET_HISTORY_MOVIES = "api/v1/user-watch-histories/my-history"
    const val GET_LIVE_URL = "api/v1/live-url"
    const val GET_LIVE_DATES = "api/v1/schedule-programs/dates"
    const val GET_LIVE_PROGRAM = "api/v1/schedule-programs"
    const val GET_BANNERS = "api/v1/banners"
    const val GET_BALANCE = "api/v1/users/billing-balance"
    const val BUY_SUBSCRIPTION = "api/v1/user_subscriptions/buy"
    const val GET_SETTINGS = "api/v1/general-settings"
    const val GET_SUBSCRIPTION= "api/v1/subscriptions"
    const val UPDATE_PROFILE = "api/v1/users/profile/update"
    const val COMMENTS = "api/v1/lessons/comment"
    const val NOTIFICATIONS = "api/v1/notifications"
    const val PROMO_CODE = "api/v1/promo-codes/apply"
    const val PAY_BALANCE = "api/v1/users/balance"
}

object AuthEndpoints {
    const val LOGIN = "api/v1/auth/login"
    const val REGISTER = "api/v1/auth/register"
    const val SMS = "api/v1/sms-codes/send"
    const val PASSWORD_SMS = "api/v1/auth/password/send-code"
    const val PASSWORD_RESET = "api/v1/auth/password/reset"
    const val REFRESH = "api/v1/auth/refresh"
}


inline fun <reified T : Any> String.getUrl(vararg params: T): String {
    val paramMap = mutableMapOf<String, Any>()
    for ((index, param) in params.withIndex()) {
        paramMap["param$index"] = param
    }

    val paramNames = regex.findAll(this).map { it.groupValues[1] }.toList()

    val paramMapWithName = mutableMapOf<String, Any>()
    for (name in paramNames) {
        if (paramMap.containsKey("param${paramNames.indexOf(name)}")) {
            paramMapWithName[name] = paramMap["param${paramNames.indexOf(name)}"] ?: ""
        } else if (!name.contains("{") && !name.contains("}")) {
            paramMapWithName[name] = name
        }
    }

    return this.replaceParams(paramMapWithName.toParamMap<T>())
}

inline fun <reified T : Any> Map<String, Any>.toParamMap(): Map<String, String> {
    val paramMap = mutableMapOf<String, String>()
    for ((key, value) in this) {
        if (value is T) {
            paramMap[key] = value.toString()
        }
    }
    return paramMap
}

fun String.replaceParams(paramMap: Map<String, String>): String {
    val result = StringBuilder()
    var index = 0
    while (index < this.length) {
        val startIndex = this.indexOf('{', index)
        if (startIndex == -1) {
            result.append(this.substring(index))
            break
        }
        val endIndex = this.indexOf('}', startIndex)
        if (endIndex == -1) {
            result.append(this.substring(index))
            break
        }
        result.append(this.substring(index, startIndex))
        val key = this.substring(startIndex + 1, endIndex)
        val value = paramMap[key] ?: "{$key}"
        result.append(value)
        index = endIndex + 1
    }
    return result.toString()
}
