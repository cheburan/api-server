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

package uk.ac.ncl.openlab.intake24.services

import org.scalatest.FunSuite
import net.scran24.fooddef.CategoryHeader
import net.scran24.fooddef.Food
import net.scran24.fooddef.InheritableAttributes
import net.scran24.fooddef.PortionSizeMethod
import net.scran24.fooddef.PortionSizeMethodParameter
import net.scran24.fooddef.FoodData
import net.scran24.fooddef.AsServedSet
import net.scran24.fooddef.AsServedImage
import net.scran24.fooddef.GuideImage
import net.scran24.fooddef.GuideImageWeightRecord
import net.scran24.fooddef.DrinkwareSet
import net.scran24.fooddef.DrinkScale
import net.scran24.fooddef.VolumeFunction
import net.scran24.fooddef.Prompt
import net.scran24.fooddef.FoodLocal

abstract class IndexFoodDataServiceTest extends FunSuite {

  val service: IndexFoodDataService

  val defaultLocale = "en_GB"
  
  // no tests yet

}