package com.unsalgasimliapplicationsnsug.yasilhesab.utility

fun getDeviceIcon(type: String): String = when (type.lowercase()) {
    "tv", "televizor" -> "\uD83D\uDCFA"
    "ac", "kondisioner" -> "\uD83C\uDF21️"
    "fridge", "soyuducu" -> "\uD83E\uDDCA"
    "microwave" -> "\uD83D\uDD2B"
    "dishwasher", "qabyuyan" -> "\uD83E\uDDFE"
    "kombi", "boiler", "hitter" -> "\uD83D\uDD25"
    "kettle", "çaydan" -> "\uD83E\uDED6"
    "blender" -> "\uD83E\uDD5E"
    "light", "lampa", "table lamp", "masa lampası" -> "\uD83D\uDCA1"
    "console", "konsol", "playstation" -> "\uD83C\uDFAE"
    "pc", "komputer", "laptop" -> "\uD83D\uDCBB"
    "printer" -> "\uD83D\uDDA8️"
    "audio", "speaker", "musiqi qutusu" -> "\uD83C\uDFB5"
    else -> "\uD83D\uDEE0️"
}