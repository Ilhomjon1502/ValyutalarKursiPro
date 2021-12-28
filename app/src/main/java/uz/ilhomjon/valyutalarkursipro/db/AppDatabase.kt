package uz.ilhomjon.valyutalarkursipro.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.ilhomjon.valyutalarkursipro.models.Valyuta

@Database(entities = [Valyuta::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun valyutaDao():ValyutaDAo

    companion object {
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context, AppDatabase::class.java, "news_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }
}