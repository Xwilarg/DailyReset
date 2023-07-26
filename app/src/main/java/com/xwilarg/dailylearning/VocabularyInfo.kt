package com.xwilarg.dailylearning

import java.time.LocalDate

data class VocabularyInfo (
    val date: String,
    val word: String,
    val meaning: Array<String>,
    val reading: String?
)