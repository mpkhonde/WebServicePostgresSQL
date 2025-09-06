package com.example.webservicepostgressql.repository

import com.example.webservicepostgressql.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {

    // Koll om användarnamn finns
    fun existsByUsername(username: String): Boolean

    // Hämta användare via username
    fun findByUsername(username: String): User?
}
