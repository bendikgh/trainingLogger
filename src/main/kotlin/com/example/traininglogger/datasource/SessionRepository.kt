package com.example.traininglogger.datasource

import com.example.traininglogger.model.Session
import org.springframework.data.repository.CrudRepository

interface SessionRepository : CrudRepository<Session, Long>