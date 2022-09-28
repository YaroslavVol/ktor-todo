package yarvol.dao

interface DAOFacade<T> {
    suspend fun selectAll(): List<T>
    suspend fun select(id: Int): T?
    suspend fun insert(type: T): T?
    suspend fun edit(id: Int, type: T): Boolean
    suspend fun delete(id: Int): Boolean
}