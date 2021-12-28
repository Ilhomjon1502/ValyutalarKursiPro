package uz.ilhomjon.valyutalarkursipro.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import uz.ilhomjon.valyutalarkursipro.models.Valyuta

@Dao
interface ValyutaDAo {

    @Query("select * from valyuta")
    fun getAllValyutaModel():List<Valyuta>

    @Insert
    fun addValyutaModel(valyutaListModel: Valyuta)
}