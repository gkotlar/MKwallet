package com.example.mkwallet.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "exchange_rates")
class GetExchangeRates(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "valuta")
    @SerializedName("valuta")
    var valuta : Double,
    @ColumnInfo(name = "datum")
    @SerializedName("datum")
    var datum : String,
    @ColumnInfo(name = "oznaka")
    @SerializedName("oznaka")
    var oznaka : String,
    @ColumnInfo(name = "drzava")
    @SerializedName("drzava")
    var drzava : String,
    @ColumnInfo(name = "kupoven")
    @SerializedName("kupoven")
    var kupoven : Double,
    @ColumnInfo(name = "sreden")
    @SerializedName("sreden")
    var sreden : Double,
    @ColumnInfo(name = "prodazen")
    @SerializedName("prodazen")
    var prodazen : Double,
    @ColumnInfo(name = "drzava_ang")
    @SerializedName("drzavaAng")
    var drzavaAng : String,
    @ColumnInfo(name = "naziv_mak")
    @SerializedName("nazivMak")
    var nazivMak : String,
    @ColumnInfo(name = "naziv_ang")
    @SerializedName("nazivAng")
    var nazivAng : String,
    @ColumnInfo(name = "datum_f")
    @SerializedName("datum_f")
    var datum_f : String,
)


