package com.prezi.fail.cli

import org.apache.commons.cli.Option
import com.prezi.fail.Config

enum class CliConfigKey(val key: String, val opt: Option) {
    API_ENDPOINT : CliConfigKey("fail.cli.apiEndpoint", Option(null, "api", true, "URL prefix to the Fail API"))
    DEBUG        : CliConfigKey("fail.cli.debug", Option("v", "debug", false, "Set root logger to DEBUG level"))
    TRACE        : CliConfigKey("fail.cli.trace", Option("vv", "trace", false, "Set root logger to TRACE level"))
    DRY_RUN      : CliConfigKey("fail.dryRun", Option("n", "dry-run", false, "Don't actually do any non-read-only actions"))
    override fun toString() = key
}

open class CliConfig : Config<CliConfigKey>() {
    val DEFAULT_API_ENDPOINT = "http://localhost:8080/"
    val DEFAULT_DEBUG = false
    val DEFAULT_TRACE = false
    val DEFAULT_DRY_RUN = false

    val DEFAULT_LIST_BEFORE = null
    val DEFAULT_LIST_AFTER = null
    val DEFAULT_LIST_CONTEXT = null
    val DEFAULT_LIST_AT = { System.currentTimeMillis() / 1000 }

    open fun getApiEndpoint(): String = getString(CliConfigKey.API_ENDPOINT) ?: DEFAULT_API_ENDPOINT
    open fun isDebug(): Boolean = getBool(CliConfigKey.DEBUG, DEFAULT_DEBUG)
    open fun isTrace(): Boolean = getBool(CliConfigKey.TRACE, DEFAULT_TRACE)
    open fun isDryRun() = getBool(CliConfigKey.DRY_RUN, DEFAULT_DRY_RUN)

    override public fun getToggledValue(key: CliConfigKey): String {
        return when (key) {
            CliConfigKey.DRY_RUN -> (!DEFAULT_DRY_RUN).toString()
            CliConfigKey.DEBUG -> (!DEFAULT_DEBUG).toString()
            CliConfigKey.TRACE -> (!DEFAULT_TRACE).toString()
            else -> throw RuntimeException("SargeConfig.getToggledValue called with invalid key ${key}")
        }
    }
}
