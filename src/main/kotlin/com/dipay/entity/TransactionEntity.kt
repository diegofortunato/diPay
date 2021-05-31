package com.dipay.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "transaction_tb")
data class TransactionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "transaction_id")
    val transactionId: Long?,

    @Column(name = "transaction_value")
    var transactionValue: Double,

    @ManyToOne
    @JoinColumn(name = "user_payer_id")
    var userPayer: UserEntity?,

    @ManyToOne
    @JoinColumn(name = "user_payee_id")
    var userPayee: UserEntity?,
)
