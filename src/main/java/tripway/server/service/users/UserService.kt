package tripway.server.service.users

import tripway.server.models.UserEntity

interface UserService {
    fun getUserById(id: String): UserEntity
    val allUser: List<UserEntity>
    fun saveUser(user: UserEntity)
    fun removeUser(user: UserEntity)
    fun removeUser(id: String)
    fun updateUser(user: UserEntity)
    fun updateUser(id: String)
    fun subscribeUser(fromUserId: String, toUserId: String): Int
    fun unsubscribeUser(fromUserId: String, toUserId: String): Int
    fun getSubscribersCount(uid: String): Int
    fun getSubscriptionsCount(uid: String): Int
    fun isSubscribed(from: String, to: String): Boolean
}
