package uk.ac.ncl.openlab.intake24.foodsql

import anorm.AnormUtil.isNull
import anorm.{ResultSetParser, SqlQueryResult}
import uk.ac.ncl.openlab.intake24.errors._

case class FirstRowValidationClause[E, T](columnName: String, resultIfNull: () => Either[E, T])

trait FirstRowValidation {

  // see http://stackoverflow.com/a/38793141/622196 for explanation

  def parseWithFirstRowValidation[E >: UnexpectedDatabaseError, T](result: SqlQueryResult, validation: Seq[FirstRowValidationClause[E, T]], parser: ResultSetParser[T])(implicit connection: java.sql.Connection): Either[E, T] = {
    result.withResult {
      cursorOpt =>
        val firstRow = cursorOpt.get.row

        validation.find {
          case FirstRowValidationClause(name, _) => isNull(firstRow, name)
        } match {
          case Some(FirstRowValidationClause(_, result)) => result()
          case None => parser(cursorOpt) match {
            case anorm.Success(parsed) => Right(parsed)
            case anorm.Error(e) => {
              // val exception = new AnormException(e.message)              
              Left(UnexpectedDatabaseError(new RuntimeException(e.message)))
            }
          }
        }
    } match {
      case Left(errors) => Left(UnexpectedDatabaseError(errors.head))
      case Right(data) => data
    }
  }

  def localeValidation[T]: Seq[FirstRowValidationClause[LocaleError, T]] =
    Seq(FirstRowValidationClause("locale_id", () => Left(UndefinedLocale(new RuntimeException()))))

  def foodValidation[T](code: String): Seq[FirstRowValidationClause[LookupError, T]] =
    Seq(FirstRowValidationClause("food_code", () => Left(RecordNotFound(new RuntimeException(code)))))

  def categoryValidation[T](code: String): Seq[FirstRowValidationClause[LookupError, T]] =
    Seq(FirstRowValidationClause("category_code", () => Left(RecordNotFound(new RuntimeException(code)))))

  def localeAndFoodCodeValidation[T](code: String): Seq[FirstRowValidationClause[LocalLookupError, T]] =
    Seq(FirstRowValidationClause("food_code", () => Left(RecordNotFound(new RuntimeException(code)))),
      FirstRowValidationClause("locale_id", () => Left(UndefinedLocale(new RuntimeException()))))

  def localeAndCategoryCodeValidation[T](code: String): Seq[FirstRowValidationClause[LocalLookupError, T]] =
    Seq(FirstRowValidationClause("category_code", () => Left(RecordNotFound(new RuntimeException(code)))),
      FirstRowValidationClause("locale_id", () => Left(UndefinedLocale(new RuntimeException()))))

  def parseWithLocaleValidation[T](result: SqlQueryResult, parser: ResultSetParser[T])(additionalValidation: Seq[FirstRowValidationClause[LocaleError, T]] = Seq())(implicit conn: java.sql.Connection): Either[LocaleError, T] =
    parseWithFirstRowValidation(result, localeValidation[T] ++ additionalValidation, parser)

  def parseWithFoodValidation[T](code: String, result: SqlQueryResult, parser: ResultSetParser[T])(additionalValidation: Seq[FirstRowValidationClause[LookupError, T]] = Seq())(implicit conn: java.sql.Connection): Either[LookupError, T] =
    parseWithFirstRowValidation(result, foodValidation[T](code) ++ additionalValidation, parser)

  def parseWithCategoryValidation[T](code: String, result: SqlQueryResult, parser: ResultSetParser[T])(additionalValidation: Seq[FirstRowValidationClause[LookupError, T]] = Seq())(implicit conn: java.sql.Connection): Either[LookupError, T] =
    parseWithFirstRowValidation(result, categoryValidation[T](code) ++ additionalValidation, parser)

  def parseWithLocaleAndFoodValidation[T](code: String, result: SqlQueryResult, parser: ResultSetParser[T])(additionalValidation: Seq[FirstRowValidationClause[LocalLookupError, T]] = Seq())(implicit conn: java.sql.Connection): Either[LocalLookupError, T] =
    parseWithFirstRowValidation(result, localeAndFoodCodeValidation[T](code) ++ additionalValidation, parser)

  def parseWithLocaleAndCategoryValidation[T](code: String, result: SqlQueryResult, parser: ResultSetParser[T])(additionalValidation: Seq[FirstRowValidationClause[LocalLookupError, T]] = Seq())(implicit conn: java.sql.Connection): Either[LocalLookupError, T] =
    parseWithFirstRowValidation(result, localeAndCategoryCodeValidation[T](code) ++ additionalValidation, parser)
}
