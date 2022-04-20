package com.example.traininglogger.datasource

import com.example.traininglogger.model.User
import org.springframework.data.repository.CrudRepository
import java.util.Optional

interface UserRepository : CrudRepository<User, Long> {

    fun findByUsername(username: String): Optional<User?>;
}