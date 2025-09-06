package com.example.webservicepostgressql.controller

import com.example.webservicepostgressql.model.User
import com.example.webservicepostgressql.repository.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userRepository: UserRepository
) {

    // 11 - Hämta alla användare
    @GetMapping
    fun getAllUsers(): ResponseEntity<List<User>> {
        val users = userRepository.findAll()
        return if (users.isEmpty()) ResponseEntity.noContent().build()
        else ResponseEntity.ok(users)
    }

    // 18 - Hämta användare via ID
    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<User> {
        val user = userRepository.findById(id)
        return if (user.isPresent) ResponseEntity.ok(user.get())
        else ResponseEntity.notFound().build()
    }

    // 25 - Hämta användare via username
    @GetMapping("/by-username")
    fun getUserByUsername(@RequestParam username: String): ResponseEntity<User> {
        val user = userRepository.findByUsername(username)
        return if (user != null) ResponseEntity.ok(user)
        else ResponseEntity.notFound().build()
    }

    // 32 - Kolla om ett användarnamn finns
    @GetMapping("/exists")
    fun checkUsernameExists(@RequestParam username: String): ResponseEntity<Boolean> {
        val exists = userRepository.existsByUsername(username)
        return ResponseEntity.ok(exists)
    }

    // 38 - Skapa en ny användare (med koll om användarnamn finns)
    @PostMapping
    fun createUser(@RequestBody body: CreateUserRequest): ResponseEntity<Any> {
        if (userRepository.existsByUsername(body.username)) {
            return ResponseEntity.status(409).body("Användarnamnet '${body.username}' finns redan.")
        }

        val saved = userRepository.save(
            User(
                username = body.username,
                password = body.password,
                enabled = body.enabled
            )
        )
        return ResponseEntity.status(201).body(saved)
    }

    // 53 - Uppdatera användare
    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody body: UpdateUserRequest): ResponseEntity<User> {
        val existing = userRepository.findById(id)
        if (existing.isEmpty) return ResponseEntity.notFound().build()

        val oldUser = existing.get()

        val updatedUser = oldUser.copy(
            username = body.username ?: oldUser.username,
            password = body.password ?: oldUser.password,
            enabled = body.enabled ?: oldUser.enabled
        )

        return ResponseEntity.ok(userRepository.save(updatedUser))
    }

    // 66 - Radera användare
    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Void> {
        if (!userRepository.existsById(id)) return ResponseEntity.notFound().build() // 69
        userRepository.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
