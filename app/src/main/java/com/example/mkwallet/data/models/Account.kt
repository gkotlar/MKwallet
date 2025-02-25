package com.example.mkwallet.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "account")
data class Account(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "currency")
    var currency : String

)
