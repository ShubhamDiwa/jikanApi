import com.google.gson.annotations.SerializedName
import com.shubham.jikananimeseekhotask.data.remote.dto.GenreDto
import com.shubham.jikananimeseekhotask.data.remote.dto.ImagesDto
import com.shubham.jikananimeseekhotask.data.remote.dto.TrailerDto

data class AnimeDetailDto(
    @SerializedName("mal_id")
    val malId: Int,

    val title: String,

    @SerializedName("title_english")
    val titleEnglish: String?,

    val synopsis: String?,

    val episodes: Int?,

    val score: Double?,

    val images: ImagesDto,

    val trailer: TrailerDto?,

    val genres: List<GenreDto>
)

