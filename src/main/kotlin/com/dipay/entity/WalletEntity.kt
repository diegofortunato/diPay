package com.dipay.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(
    name = "wallet_tb",
    uniqueConstraints =
    [UniqueConstraint(columnNames = ["wallet_owner"])]
)
data class WalletEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "wallet_id")
    val walletId: Long?,

    @Column(name = "wallet_value")
    var walletValue: Double,

    @Column(name = "wallet_owner")
    var walletOwnerID: Long,
)
