package com.example.traininglogger.model.dtos

data class ExerciseDto(
    val sets: Int,

    val reps: Int,

    val name: String,

    val session: Long,

    var weight: Int?
)