package uk.ac.ncl.openlab.intake24.foodsql.admin

import uk.ac.ncl.openlab.intake24.api.data.admin.{CategoryHeader, FoodHeader}

trait HeaderRows {

  protected case class CategoryHeaderRow(code: String, description: String, local_description: Option[String], is_hidden: Boolean) {
    def asCategoryHeader = CategoryHeader(code, description, local_description, is_hidden)
  }

  protected case class FoodHeaderRow(code: String, description: String, local_description: Option[String]) {
    def asFoodHeader = FoodHeader(code, description, local_description)
  }

}