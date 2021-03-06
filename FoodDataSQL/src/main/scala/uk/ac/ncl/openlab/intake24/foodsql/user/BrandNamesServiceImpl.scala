package uk.ac.ncl.openlab.intake24.foodsql.user

import javax.inject.Inject
import javax.sql.DataSource

import anorm._
import com.google.inject.Singleton
import com.google.inject.name.Named
import uk.ac.ncl.openlab.intake24.errors.LocalLookupError
import uk.ac.ncl.openlab.intake24.foodsql.{FirstRowValidation, FirstRowValidationClause}
import uk.ac.ncl.openlab.intake24.services.fooddb.user.BrandNamesService
import uk.ac.ncl.openlab.intake24.sql.{SqlDataService, SqlResourceLoader}

@Singleton
class BrandNamesServiceImpl @Inject()(@Named("intake24_foods") val dataSource: DataSource) extends BrandNamesService with SqlDataService with FirstRowValidation with SqlResourceLoader {

  private lazy val getBrandNamesQuery = sqlFromResource("user/get_brand_names_frv.sql")

  protected def getBrandNamesComposable(foodCode: String, locale: String)(implicit conn: java.sql.Connection): Either[LocalLookupError, Seq[String]] = {
    val result = SQL(getBrandNamesQuery).on('food_code -> foodCode, 'locale_id -> locale).executeQuery()

    parseWithLocaleAndFoodValidation(foodCode, result, SqlParser.str("name").+)(Seq(FirstRowValidationClause("name", () => Right(List()))))
  }

  def getBrandNames(foodCode: String, locale: String): Either[LocalLookupError, Seq[String]] = tryWithConnection {
    implicit conn =>
      getBrandNamesComposable(foodCode, locale)
  }
}
