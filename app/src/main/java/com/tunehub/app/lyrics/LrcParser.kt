package com.tunehub.app.lyrics

import com.tunehub.app.domain.model.LyricLine

object LrcParser {
    private val timeRegex = Regex("\\[(\\d{2}):(\\d{2})(?:\\.(\\d{1,3}))?\\]")

    fun parse(lrc: String): List<LyricLine> {
        if (lrc.isBlank()) return emptyList()

        val lines = mutableListOf<LyricLine>()
        lrc.lines().forEach { rawLine ->
            val matches = timeRegex.findAll(rawLine).toList()
            if (matches.isEmpty()) return@forEach

            val text = rawLine.replace(timeRegex, "").trim()
            matches.forEach { match ->
                val minutes = match.groupValues[1].toLongOrNull() ?: 0L
                val seconds = match.groupValues[2].toLongOrNull() ?: 0L
                val fraction = match.groupValues.getOrNull(3).orEmpty()
                val millis = when (fraction.length) {
                    3 -> fraction.toLongOrNull() ?: 0L
                    2 -> (fraction.toLongOrNull() ?: 0L) * 10L
                    1 -> (fraction.toLongOrNull() ?: 0L) * 100L
                    else -> 0L
                }
                val timeMs = (minutes * 60 + seconds) * 1000 + millis
                lines.add(LyricLine(timeMs = timeMs, text = text))
            }
        }
        return lines.sortedBy { it.timeMs }
    }

    fun findActiveIndex(lines: List<LyricLine>, positionMs: Long): Int {
        if (lines.isEmpty()) return -1
        var index = lines.indexOfLast { it.timeMs <= positionMs }
        if (index < 0) index = 0
        return index
    }
}
