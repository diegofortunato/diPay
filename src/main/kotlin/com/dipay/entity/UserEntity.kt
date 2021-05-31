package com.dipay.entity

import com.dipay.constant.UserTypeEnum
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(
    name = "user_tb",
    uniqueConstraints =
    [UniqueConstraint(columnNames = ["user_document", "user_email"])]
)
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    val userId: Long,

    @Column(name = "user_full_name")
    var userFullName: String,

    @Column(name = "user_document")
    var userDocument: String,

    @Column(name = "user_email")
    var userEmail: String,

    @Column(name = "user_password")
    var userPassword: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    var userType: UserTypeEnum,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userPayer")
    var userTransactionsPayer: List<TransactionEntity>?,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userPayee")
    var userTransactionsPayee: List<TransactionEntity>?,

    @OneToOne(cascade = [(CascadeType.ALL)])
    @JoinColumn(name = "wallet_id", referencedColumnName = "wallet_owner")
    var userWallet: WalletEntity?
)
