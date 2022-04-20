package com.example.traininglogger.security

import com.example.traininglogger.model.User
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component

@Mapper(componentModel = "spring")
@Component
class UserViewMapper {

    fun toUserView(user: User?): UserView = UserView(user!!.id!!, user.username!!, user.getFullname()!!)

}