package tripway.server.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional
import tripway.server.models.SubscriptionEntity

interface SubscriptionRepository : JpaRepository<SubscriptionEntity, Long> {
    @Modifying
    @Query("""INSERT INTO subscription (from_user_id, to_user_id) VALUES (:from_user_id, :to_user_id)""", nativeQuery = true)
    @Transactional
    fun subscribeUser(@Param("from_user_id") fromUserId: String, @Param("to_user_id") toUserID: String): Int

    @Modifying
    @Query("DELETE FROM subscription as s where s.from_user_id=:from and s.to_user_id=:to", nativeQuery = true)
    @Transactional
    fun unsubscribeUser(@Param("from") fromUserId: String, @Param("to") toUserID: String): Int

    @Query("SELECT count(*) from subscription as s where s.to_user_id=:fromUserId and s.from_user_id =:toUserId", nativeQuery = true)
    fun isSubscribedTo(@Param("fromUserId") fromUserId: String, @Param("toUserId") toUserID: String): Int

    @Query("SELECT count(*) from subscription as s where s.from_user_id=:user_id", nativeQuery = true)
    fun findSubscriptionsCount(@Param("user_id") userId: String): Int

    @Query("SELECT count(*) from subscription as s where s.to_user_id=:user_id", nativeQuery = true)
    fun findSubscribesCount(@Param("user_id") userId: String): Int

    @Query("SELECT count(*) from subscription as s where s.from_user_id=:from and s.to_user_id=:to", nativeQuery = true)
    fun isSubscribed(@Param("from") fromUserId: String, @Param("to") toUserID: String): Int

    fun findAllByFromUser_Id(uid: String): List<SubscriptionEntity>
}
