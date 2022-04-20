package com.example.traininglogger.model

import com.example.traininglogger.model.dtos.CreateUserRequest
import com.example.traininglogger.security.Role
import org.slf4j.LoggerFactory
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors
import javax.persistence.*
import kotlin.jvm.Transient

@Entity
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,

    private var username : String? = null,

    private var fullname: String? = null,

    private var password: String? = null

) : UserDetails {

    constructor(username: String, password: String) : this(null, username, null, password)

    constructor() : this(null, null, null, null)

    constructor(user: CreateUserRequest) : this(
        null,
        user.username,
        user.fullname,
        user.password
    )

    var enabled: Boolean = true
    var credentialsNonExpired: Boolean = true
    var accountNonExpired: Boolean = true
    var accountNonLocked: Boolean = true

    @Transient
    private val log = LoggerFactory.getLogger(User::class.java)

//    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @OneToMany(
        cascade = [CascadeType.ALL],
        targetEntity=com.example.traininglogger.security.Role::class,
        fetch = FetchType.EAGER,
    )
    @JoinColumn(name="User_id")
    var roles: Set<Role> = emptySet()


    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return this.roles
            .stream()
            .map { role ->
                log.debug("Granting Authority to user with role: $role")
                SimpleGrantedAuthority(role.toString()) }
            .collect(Collectors.toList())
    }

    override fun getPassword(): String {
        return this.password!!;
    }

    fun setPassword(password: String)
    {
        this.password = password
        return
    }

    override fun getUsername(): String {
        return this.username!!;
    }

    fun getFullname(): String {
        return this.fullname!!;
    }

    override fun isAccountNonExpired(): Boolean {
        return this.accountNonExpired;
    }

    override fun isAccountNonLocked(): Boolean {
        return return this.accountNonLocked;
    }

    override fun isCredentialsNonExpired(): Boolean {
        return this.credentialsNonExpired;
    }

    override fun isEnabled(): Boolean {
        return this.enabled;
    }
}