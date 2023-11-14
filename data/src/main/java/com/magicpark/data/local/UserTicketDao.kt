package com.magicpark.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.magicpark.domain.model.UserTicket
import kotlinx.coroutines.flow.Flow

@Dao
interface UserTicketDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveUserTickets(items: List<UserTicket>): List<Long>

    @Query("SELECT * FROM wallet WHERE id LIKE :id LIMIT 1")
    fun getUserTicketById(id: Long): UserTicket

    @Query("SELECT * FROM wallet")
    fun getUserTickets(): List<UserTicket>

    @Query("SELECT * FROM wallet WHERE user_id LIKE :id LIMIT 1")
    fun getUserTicket(id: Long): List<UserTicket>
}
