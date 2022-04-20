package com.example.traininglogger.model

import org.springframework.data.annotation.CreatedDate
import java.util.Date
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Temporal
import javax.persistence.TemporalType

@Entity
data class Session(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var sessionId: Long?,

    @OneToMany(mappedBy = "session", targetEntity = Exercise::class)
    var exrcises: Collection<Exercise>?,

    @CreatedDate
    @Temporal(TemporalType.DATE)
    var date: Date? = Date(),

    @OneToOne()
    @JoinColumn(name = "user_id")
    var user: User?

)
{
    constructor(): this(null, null, null, null)
}