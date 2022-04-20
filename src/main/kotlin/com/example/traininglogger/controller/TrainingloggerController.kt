package com.example.traininglogger.controller

import com.example.traininglogger.model.Exercise
import com.example.traininglogger.model.Session
import com.example.traininglogger.model.dtos.ExerciseDto
import com.example.traininglogger.service.TrainingloggerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping

@RestController
@RequestMapping("/api/logger")
class TrainingloggerController(val service: TrainingloggerService)
{

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(e: NoSuchElementException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(e: IllegalArgumentException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.BAD_REQUEST)

    @GetMapping
    fun getLog(): Collection<Session> = service.getSessions();

    @PostMapping
    fun postSession(): Session = service.addSession()

    @PostMapping("exercise")
    fun postExercise(@RequestBody exercise: ExerciseDto): ExerciseDto = service.addExercise(exercise)

}