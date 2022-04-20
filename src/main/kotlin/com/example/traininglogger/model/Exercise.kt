package com.example.traininglogger.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
data class Exercise(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,

    var sets: Int? = null,

    var reps: Int? = null,

    var weight: Int? = null,

    var name: String? = null,

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    @JsonIgnore
    var session: Session? = null
)