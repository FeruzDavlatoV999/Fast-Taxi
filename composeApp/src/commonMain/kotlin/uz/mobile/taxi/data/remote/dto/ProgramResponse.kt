package uz.mobile.taxi.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ProgramResponse(
    @SerialName("paginator")
    val paginator: Paginator? = null,
    @SerialName("schedulePrograms")
    val schedulePrograms: List<ScheduleProgram>? = null,
) {

    @Serializable
    data class ScheduleProgram(
        @SerialName("created_at")
        val createdAt: String? = null,
        @SerialName("date")
        val date: String? = null,
        @SerialName("end_time")
        val endTime: String? = null,
        @SerialName("id")
        val id: Int? = null,
        @SerialName("start_time")
        val startTime: String? = null,
        @SerialName("title")
        val title: String? = null,
        @SerialName("updated_at")
        val updatedAt: String? = null,
        @SerialName("url")
        val url: String? = null,
    )
}
