package models

case class ArtistInfoRequest(ids: Seq[Int], limit: Int, offset: Int)
    extends IInfoRequest

case class ArtistInfo(name: String,
                      gender: Int,
                      profilePath: String,
                      movies: Seq[Int],
                      id: Int)

case class ArtistInfos(metadata: Metadata, data: Seq[ArtistInfo])
