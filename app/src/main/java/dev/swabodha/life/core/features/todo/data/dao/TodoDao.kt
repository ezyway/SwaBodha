package dev.swabodha.life.core.features.todo.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.swabodha.life.core.features.todo.data.entity.TodoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: TodoEntity)

    @Query("SELECT * FROM todos ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<TodoEntity>>

    @Query("DELETE FROM todos WHERE id = :id")
    suspend fun deleteById(id: String)

}