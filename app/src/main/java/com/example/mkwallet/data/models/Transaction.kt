package com.example.mkwallet.data.models
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "transaction",
    foreignKeys = [ForeignKey(entity = Account::class, parentColumns = ["currency"],
                                childColumns = ["transaction_currency"], onDelete = CASCADE )])
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "transaction_id")
    val transactionId: Int = 0,
    @ColumnInfo(name = "date_time")
    val dateTime: Long,
    @ColumnInfo(name = "value")
    val value: Int,
    @ColumnInfo(name = "image_path")
    val imagePath: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "category")
    val category: Int,
    @ColumnInfo(name = "transaction_currency", index = true)
    val transactionCurrency: String,
    @ColumnInfo(name = "foreign_party")
    val foreignParty: String
)
