package models

trait IRequest

trait IInfoRequest extends IRequest {
  def ids: Seq[Int]
  def limit: Int
  def offset: Int
}
