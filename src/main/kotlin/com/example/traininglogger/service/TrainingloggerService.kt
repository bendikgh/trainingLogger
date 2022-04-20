package com.example.traininglogger.service

import com.example.traininglogger.datasource.ExerciseRepository
import com.example.traininglogger.datasource.SessionRepository
import com.example.traininglogger.model.Exercise
import com.example.traininglogger.model.Session
import com.example.traininglogger.model.User
import com.example.traininglogger.model.dtos.ExerciseDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class TrainingloggerService  {

    @Autowired
    lateinit var sessionRepository: SessionRepository

    @Autowired
    lateinit var exerciseRepository: ExerciseRepository

    fun getSessions(): Collection<Session> = this.sessionRepository.findAll().toList()

    fun getSession(id: Long): Session = this.sessionRepository.findById(id).get()

    fun addSession(): Session {
        val user: User = SecurityContextHolder.getContext().authentication.principal as User
        return this.sessionRepository.save(Session(
            null,
            null,
            null,
            user
        ))
    }

    fun getExercises() : Collection<Exercise> = this.exerciseRepository.findAll().toList()

    fun addExercise(dto: ExerciseDto) : ExerciseDto {
        var session: Session = this.sessionRepository.findById(dto.session).get();
        var ex = Exercise(name = dto.name, reps = dto.reps, sets = dto.sets, session = session, id = null, weight = dto.weight)
        this.exerciseRepository.save(ex);
        return dto;
    }
}