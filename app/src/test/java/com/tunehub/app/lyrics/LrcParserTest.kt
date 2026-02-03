package com.tunehub.app.lyrics

import org.junit.Assert.assertEquals
import org.junit.Test

class LrcParserTest {
    @Test
    fun parse_lrc_lines() {
        val lrc = """
            [00:01.00]Hello
            [00:02.50]World
        """.trimIndent()

        val lines = LrcParser.parse(lrc)

        assertEquals(2, lines.size)
        assertEquals(1000L, lines[0].timeMs)
        assertEquals("Hello", lines[0].text)
        assertEquals(2500L, lines[1].timeMs)
        assertEquals("World", lines[1].text)
    }
}
