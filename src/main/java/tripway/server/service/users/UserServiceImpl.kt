package tripway.server.service.users

import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tripway.server.exceptions.ApiException
import tripway.server.models.SubscriptionEntity
import tripway.server.models.UserEntity
import tripway.server.repository.SubscriptionRepository
import tripway.server.repository.UserRepository

@Slf4j
@Service
class UserServiceImpl : UserService {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var subscriptionRepository: SubscriptionRepository

    override fun getUserById(id: String): UserEntity {
        return userRepository.findById(id).orElse(null)
    }

    override val allUser: List<UserEntity>
        get() = userRepository.findAll()

    override fun subscribeUser(fromUserId: String, toUserId: String): Int {
        if(subscriptionRepository.isSubscribed(fromUserId, toUserId)>0){
            throw ApiException(ApiException.Type.UserAlreadySubscribed)
        }
        return subscriptionRepository.subscribeUser(fromUserId, toUserId)
    }

    override fun unsubscribeUser(fromUserId: String, toUserId: String): Int {
        if(subscriptionRepository.isSubscribed(fromUserId, toUserId)==0){
            throw ApiException(ApiException.Type.UserIsntSubscribed)
        }
        return subscriptionRepository.unsubscribeUser(fromUserId, toUserId)
    }

    override fun getSubscribersCount(uid: String):Int {
        return subscriptionRepository.findSubscribesCount(uid)
    }

    override fun isSubscribed(from: String, to: String): Boolean{
        return subscriptionRepository.isSubscribed(from, to) > 0
    }

    override fun getSubscriptionsCount(uid: String):Int {
        return subscriptionRepository.findSubscriptionsCount(uid)
    }

    override fun saveUser(user: UserEntity) {
        userRepository.save(user)
    }

    override fun removeUser(user: UserEntity) {
        userRepository.delete(user)
    }

    override fun removeUser(id: String) {
        userRepository.deleteById(id)
    }

    override fun updateUser(user: UserEntity) {}

    override fun updateUser(id: String) {}

    val all: List<UserEntity>
        get() = userRepository.findAll()
}
