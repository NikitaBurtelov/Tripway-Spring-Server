package tripway.server.exceptions

class ApiException(val type: Type): Exception() {
    enum class Type{
        AlreadyExistException,
        UserDoesntExist,
        UserAlreadySubscribed,
        UserIsntSubscribed,
        BadRequest,
        PointDoesntExist,
        TripDoesntExist
    }
}
