package com.xwilarg.dailylearning

import java.time.LocalDate

data class VocabularyInfo (
    val date: LocalDate?,
    val word: String,
    val meaning: Array<String>,
    val reading: String?
)