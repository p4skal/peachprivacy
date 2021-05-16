package com.peachprivacy.userservice.authentication

import javax.persistence.*

@Entity
@Table(name = "account")
class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    @Column(unique = true)
    lateinit var email: String

    lateinit var password: String

    lateinit var role: String
}