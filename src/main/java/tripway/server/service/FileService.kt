package tripway.server.service

import com.amazonaws.AmazonServiceException
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import tripway.server.AwsS3.Companion.bucketName
import tripway.server.exceptions.ApiException
import tripway.server.exceptions.ApiException.*
import tripway.server.repository.ImageRepository
import tripway.server.repository.PointRepository
import tripway.server.repository.TripRepository
import java.net.URL
import java.util.*
import java.util.logging.Logger

@Service
class FileService {
    @Autowired
    private lateinit var pointRepository: PointRepository

    @Autowired
    private lateinit var imageRepository: ImageRepository

    @Autowired
    private lateinit var tripRepository: TripRepository

    @Autowired
    private lateinit var s3Client: AmazonS3Client

    fun save(images: List<MultipartFile>, pointId: Long) {
        //todo throw ошибка! нет такого поинта
        //todo распаралелить с помощью корутин сохранение фоток
        val point = pointRepository.findByIdOrNull(pointId)
                ?: throw ApiException(Type.PointDoesntExist)

        val photosByteArray = images.map {it.bytes}
        val uuidPhotos = photosByteArray.map {
            UUID.randomUUID().toString()
        }
        images.forEachIndexed { index, image ->
            val objectMetadata = ObjectMetadata().apply {
                contentLength = image.size
                contentType = image.contentType
            }
            val request = PutObjectRequest(bucketName,
                    uuidPhotos[index],
                    image.inputStream,
                    objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead)
            val result = s3Client.putObject(request)
            Logger.getGlobal().info(result.toString())
            //todo result обработать ошибку дублирующейся фотки, чтобы она не загрузилась в бд
            //todo если не удалось загрузить фотку в S3 абортить создание поинта
        }

        try {
            uuidPhotos.forEach {
                imageRepository.savePhoto(it,pointId)
            }
            val trip = point.trip.copy(photo = uuidPhotos.last())
            tripRepository.save(trip)
        }
        catch (e: Exception){
            //откатываем
            if(pointRepository.findByTrip_IdOrderByCreated(point.trip.id).size==1){
                tripRepository.deleteById(point.trip.id)
            }
            else pointRepository.deleteById(pointId)
        }

//        val newPointPhotos = point.photos?: listOf<String>() + uuidPhotos
//        pointRepository.save(point.copy(photos = newPointPhotos))
    }

    fun getImageUrlByUUID(uuid: String): URL = s3Client.getUrl(bucketName, uuid)

    fun removeImg(uuid: String) {
        try {
            s3Client.deleteObject(bucketName, uuid)
        }
        catch (e : AmazonServiceException) {
            e.printStackTrace()
        }
    }
}
