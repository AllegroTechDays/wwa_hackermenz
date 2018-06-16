package home.antonyaskiv.hackaton.Model

/**
 * Created by AntonYaskiv on 16.06.2018.
 */
data class Request(var distance_from: String? = null, var distance_to: String? = null
                   , var duration_from: String? = null, var duration_to: String? = null,
                   var maxSpeed_from: String? = null, var maxSpeed_to: String? = null,
                   var averageSpeed_from: String? = null, var averageSpeed_to: String? = null,
                   var bikeType: Array<BikeType>? = null, var sourceMetadataRequest: SourceMetadataRequest? = null
                   , var weatherRequest: WeatherRequest? = null, var userMetadataRequest: UserMetadataRequest? = null, var locationRequest: LocationRequest)

data class SourceMetadataRequest(var finishedAtDayNumber: Array<FinishedAtDayNumber>?, var createdAt_from: String?, var createdAt_to: String?)

data class WeatherRequest(var temperature_from: String?, var temperature_to: String?
                          , var precipitation_from: String?, var precipitation_to: String?)

data class UserMetadataRequest(var ageAtActivityCreation_from: String?, var ageAtActivityCreation_to: String?, var sex: Sex?)
data class LocationRequest(var longitude_from: String, var latitude_from: String, var longitude_to: String, var latitude_to: String)
enum class FinishedAtDayNumber {
    Zero, One, Two
}

enum class BikeType {
    City, Mountain, Road, Trekking, Electric, Stationary
}

enum class Sex { M, F }


data class Response(var id: String, var distance: Int, var duration: Int,
                    var maxSpeed: Double, var averageSpeed: Double, var altitude: Int, var terrain: String,
                    var weatherRequest: WeatherResponse?, var path: Path, var userMetadata: UserMetadataResponse?, var sourceMetadata: SourceMetadataResponse?)

data class WeatherResponse(var a: String)
data class UserMetadataResponse(var ageAtActivityCreation: Int, var sex: String)
data class SourceMetadataResponse(var createdAt: CreatedAt?, var finishedAt: CreatedAt?)
data class CreatedAt(var date: String, var timezone_type: Int, var timezone: String)
data class Path(var type: String, var coordinates: List<List<String>>)
