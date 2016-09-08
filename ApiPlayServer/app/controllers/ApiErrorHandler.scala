package controllers

import uk.ac.ncl.openlab.intake24.services.fooddb.errors._

import play.api.http.ContentTypes
import play.api.mvc.Results

import upickle.default._
import org.slf4j.LoggerFactory

trait ApiErrorHandler extends Results {

  private val logger = LoggerFactory.getLogger(classOf[ApiErrorHandler])

  def databaseErrorBody(message: String) = s"""{"cause":"DatabaseError","errorMessage":"$message"}"""
  val recordNotFoundErrorBody = s"""{"cause":"RecordNotFound}"""
  val undefinedLocaleErrorBody = s"""{"cause":"UndefinedLocale"}"""
  val duplicateCodeErrorBody = s"""{"cause":"DuplicateCode"}"""
  val parentRecordNotFoundErrorBody = s"""{"cause":"ParentRecordNotFound"}"""

  def handleDatabaseError(message: String, e: Option[Throwable]) = {
    e.foreach(e => logger.error(message, e))
    InternalServerError(databaseErrorBody(message)).as(ContentTypes.JSON)
  }

  def translateDatabaseError[T](result: Either[DatabaseError, T])(implicit writer: Writer[T]) = result match {
    case Right(result) => Ok(write(result)).as(ContentTypes.JSON)
    case Left(DatabaseError(message, exception)) => handleDatabaseError(message, exception)
  }

  def translateLookupError[T](result: Either[LookupError, T])(implicit writer: Writer[T]) = result match {
    case Right(result) => Ok(write(result)).as(ContentTypes.JSON)
    case Left(RecordNotFound) => NotFound(recordNotFoundErrorBody).as(ContentTypes.JSON)
    case Left(DatabaseError(message, exception)) => handleDatabaseError(message, exception)
  }

  def translateLocaleError[T](result: Either[LocaleError, T])(implicit writer: Writer[T]) = result match {
    case Right(result) => Ok(write(result)).as(ContentTypes.JSON)
    case Left(UndefinedLocale) => NotFound(undefinedLocaleErrorBody).as(ContentTypes.JSON)
    case Left(DatabaseError(message, exception)) => handleDatabaseError(message, exception)
  }

  def translateLocalLookupError[T](result: Either[LocalLookupError, T])(implicit writer: Writer[T]) = result match {
    case Right(result) => Ok(write(result)).as(ContentTypes.JSON)
    case Left(UndefinedLocale) => NotFound(undefinedLocaleErrorBody).as(ContentTypes.JSON)
    case Left(RecordNotFound) => NotFound(recordNotFoundErrorBody).as(ContentTypes.JSON)
    case Left(DatabaseError(message, exception)) => handleDatabaseError(message, exception)
  }

  def translateUpdateError[T](result: Either[UpdateError, T])(implicit writer: Writer[T]) = result match {
    case Right(result) => Ok(write(result)).as(ContentTypes.JSON)
    case Left(DuplicateCode) => BadRequest(duplicateCodeErrorBody).as(ContentTypes.JSON)
    case Left(VersionConflict) => Conflict.as(ContentTypes.JSON)
    case Left(RecordNotFound) => NotFound(recordNotFoundErrorBody).as(ContentTypes.JSON)
    case Left(DatabaseError(message, exception)) => handleDatabaseError(message, exception)
  }

  def translateParentError[T](result: Either[ParentError, T])(implicit writer: Writer[T]) = result match {
    case Right(result) => Ok(write(result)).as(ContentTypes.JSON)
    case Left(ParentRecordNotFound) => BadRequest(parentRecordNotFoundErrorBody).as(ContentTypes.JSON)
    case Left(DatabaseError(message, exception)) => handleDatabaseError(message, exception)
  }

  def translateLocalUpdateError[T](result: Either[LocalUpdateError, T])(implicit writer: Writer[T]) = result match {
    case Right(result) => Ok(write(result)).as(ContentTypes.JSON)
    case Left(UndefinedLocale) => NotFound(undefinedLocaleErrorBody).as(ContentTypes.JSON)
    case Left(DuplicateCode) => BadRequest(duplicateCodeErrorBody).as(ContentTypes.JSON)
    case Left(VersionConflict) => Conflict.as(ContentTypes.JSON)
    case Left(RecordNotFound) => NotFound(recordNotFoundErrorBody).as(ContentTypes.JSON)
    case Left(DatabaseError(message, exception)) => handleDatabaseError(message, exception)
  }

  def translateDependentCreateError[T](result: Either[DependentCreateError, T])(implicit writer: Writer[T]) = result match {
    case Right(result) => Ok(write(result)).as(ContentTypes.JSON)
    case Left(DuplicateCode) => BadRequest(duplicateCodeErrorBody).as(ContentTypes.JSON)
    case Left(ParentRecordNotFound) => BadRequest(parentRecordNotFoundErrorBody).as(ContentTypes.JSON)
    case Left(DatabaseError(message, exception)) => handleDatabaseError(message, exception)
  }

  def translateDependentUpdateError[T](result: Either[DependentUpdateError, T])(implicit writer: Writer[T]) = result match {
    case Right(result) => Ok(write(result)).as(ContentTypes.JSON)
    case Left(RecordNotFound) => NotFound(recordNotFoundErrorBody).as(ContentTypes.JSON)
    case Left(DuplicateCode) => BadRequest(duplicateCodeErrorBody).as(ContentTypes.JSON)
    case Left(VersionConflict) => Conflict.as(ContentTypes.JSON)
    case Left(ParentRecordNotFound) => BadRequest(parentRecordNotFoundErrorBody).as(ContentTypes.JSON)
    case Left(DatabaseError(message, exception)) => handleDatabaseError(message, exception)
  }

  def translateLocalDependentUpdateError[T](result: Either[LocalDependentUpdateError, T])(implicit writer: Writer[T]) = result match {
    case Right(result) => Ok(write(result)).as(ContentTypes.JSON)
    case Left(DuplicateCode) => BadRequest(duplicateCodeErrorBody).as(ContentTypes.JSON)
    case Left(VersionConflict) => Conflict.as(ContentTypes.JSON)
    case Left(RecordNotFound) => NotFound(recordNotFoundErrorBody).as(ContentTypes.JSON)
    case Left(UndefinedLocale) => NotFound(undefinedLocaleErrorBody).as(ContentTypes.JSON)
    case Left(ParentRecordNotFound) => BadRequest(parentRecordNotFoundErrorBody).as(ContentTypes.JSON)
    case Left(DatabaseError(message, exception)) => handleDatabaseError(message, exception)
  }

  def translateDeleteError[T](result: Either[DeleteError, T])(implicit writer: Writer[T]) = result match {
    case Right(result) => Ok(write(result)).as(ContentTypes.JSON)
    case Left(RecordNotFound) => NotFound(recordNotFoundErrorBody).as(ContentTypes.JSON)
    case Left(DatabaseError(message, exception)) => handleDatabaseError(message, exception)
  }

  def translateCreateError[T](result: Either[CreateError, T])(implicit writer: Writer[T]) = result match {
    case Right(result) => Ok(write(result)).as(ContentTypes.JSON)
    case Left(DuplicateCode) => BadRequest(duplicateCodeErrorBody).as(ContentTypes.JSON)
    case Left(DatabaseError(message, exception)) => handleDatabaseError(message, exception)
  }

  def translateLocaleOrParentError[T](result: Either[LocaleOrParentError, T])(implicit writer: Writer[T]) = result match {
    case Right(result) => Ok(write(result)).as(ContentTypes.JSON)
    case Left(UndefinedLocale) => NotFound(undefinedLocaleErrorBody).as(ContentTypes.JSON)
    case Left(ParentRecordNotFound) => BadRequest(parentRecordNotFoundErrorBody).as(ContentTypes.JSON)
    case Left(DatabaseError(message, exception)) => handleDatabaseError(message, exception)
  }
}