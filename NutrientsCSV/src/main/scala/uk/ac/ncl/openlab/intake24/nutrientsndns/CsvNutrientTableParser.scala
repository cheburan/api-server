/*
This file is part of Intake24.

Copyright 2015, 2016 Newcastle University.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package uk.ac.ncl.openlab.intake24.nutrientsndns

import org.slf4j.LoggerFactory

import au.com.bytecode.opencsv.CSVReader
import java.io.FileReader
import scala.collection.JavaConversions._

case class CsvNutrientTableMapping(rowOffset: Int, idColumn: Int, descriptionColumn: Int, nutrientMapping: Map[Long, Int])

case class NutrientTableRecord(id: String, description: String, nutrients: Map[Long, Double])

object CsvNutrientTableParser {
  val log = LoggerFactory.getLogger(CsvNutrientTableParser.getClass)

  def excelColumnToOffset(colRef: String) = {
    def r(s: String) = s.foldRight((0, 1)) {
      case (ch, (acc, mul)) => (acc + (ch - 'A' + 1) * mul, mul * 26)
    }

    r(colRef)._1
  }

  def parseTable(fileName: String, mapping: CsvNutrientTableMapping): Seq[NutrientTableRecord] = {
    val rows = new CSVReader(new FileReader(fileName)).readAll().toSeq.map(_.toIndexedSeq)

    def readNutrients(row: IndexedSeq[String], rowIndex: Int): Map[Long, Double] = mapping.nutrientMapping.foldLeft(Map[Long, Double]()) {
      case (acc, (nutrientId, colNum)) => {
        try {
          acc + (nutrientId -> row(colNum - 1).toDouble)
        } catch {
          case e: Throwable => {
            if (nutrientId == 1l)
              log.error(s"Failed to read energy (kcal) in row $rowIndex! This is an essential nutrient column, please check the source table for errors.")
            else
              log.warn("Failed to read nutrient type " + nutrientId.toString + " in row " + rowIndex + ", assuming data N/A")
            acc
          }
        }
      }
    }

    rows.zipWithIndex.drop(mapping.rowOffset).map {
      case (row, index) =>
        NutrientTableRecord(row(mapping.idColumn), row(mapping.descriptionColumn), readNutrients(row, index))
    }
  }
}
