package uk.ac.ncl.openlab.intake24.system.output;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.workcraft.gwt.shared.client.Callback1;

import au.com.bytecode.opencsv.CSVWriter;
import net.scran24.datastore.DataStore;
import net.scran24.datastore.DataStoreException;
import net.scran24.datastore.MissingFoodRecord;
import net.scran24.datastore.NutritionMappedFood;
import net.scran24.datastore.NutritionMappedMeal;
import net.scran24.datastore.NutritionMappedSurveyRecordWithId;
import net.scran24.datastore.shared.CustomDataScheme;
import net.scran24.datastore.shared.CustomDataScheme.CustomFieldDef;
import net.scran24.datastore.shared.DataSchemeMap;
import net.scran24.datastore.shared.SurveySchemes;
import uk.ac.ncl.openlab.intake24.nutrients.Nutrient;
import uk.ac.ncl.openlab.intake24.nutrients.Nutrient$;

public class CSVOutput {

	private final DataStore dataStore;

	public CSVOutput(DataStore dataStore) {
		this.dataStore = dataStore;
	}

	public void writeCSV(final String survey_id, final long timeFrom, final long timeTo, OutputStream outputStream) throws IOException {
		final CSVWriter writer = new CSVWriter(new PrintWriter(outputStream));

		try {
			final CustomDataScheme dataScheme = DataSchemeMap
					.dataSchemeFor(SurveySchemes.schemeForId(dataStore.getSurveyParameters(survey_id).schemeName));

			ArrayList<String> header = new ArrayList<String>();

			header.add("Survey ID");
			header.add("User ID");

			for (CustomFieldDef f : dataScheme.userCustomFields())
				header.add(f.description);

			header.add("Start time");
			header.add("Submission time");
			header.add("Time to complete");

			for (CustomFieldDef f : dataScheme.surveyCustomFields())
				header.add(f.description);

			header.add("Meal ID");
			header.add("Meal name");

			for (CustomFieldDef f : dataScheme.mealCustomFields())
				header.add(f.description);

			header.add("Food ID");
			header.add("Search term");
			header.add("Intake24 food code");
			header.add("Description (en)");
			header.add("Description (local)");
			header.add("Nutrient table name");
			header.add("Nutrient table code");
			header.add("Food group code");
			header.add("Food group (en)");
			header.add("Food group (local)");
			header.add("Ready meal");
			header.add("Brand");

			for (CustomFieldDef f : dataScheme.foodCustomFields())
				header.add(f.description);

			header.add("Serving size (g/ml)");
			header.add("Serving image");
			header.add("Leftovers (g/ml)");
			header.add("Leftovers image");
			header.add("Portion size (g/ml)");
			header.add("Reasonable amount");

			// For missing foods

			header.add("Missing food description");
			header.add("Missing food portion size");
			header.add("Missing food leftovers");

			Set<Nutrient> nutrients = scala.collection.JavaConverters.setAsJavaSetConverter(Nutrient$.MODULE$.types()).asJava();

			final List<Nutrient> sortedNutrients = new ArrayList<Nutrient>(nutrients);

			Collections.sort(sortedNutrients, new Comparator<Nutrient>() {
				@Override
				public int compare(Nutrient arg0, Nutrient arg1) {
					return arg0.key().compareTo(arg1.key());
				}
			});

			for (Nutrient n : sortedNutrients)
				header.add(n.key());

			writer.writeNext(header.toArray(new String[header.size()]));

			final DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.UK);

			dataStore.processSurveys(survey_id, timeFrom, timeTo, new Callback1<NutritionMappedSurveyRecordWithId>() {
				public void call(NutritionMappedSurveyRecordWithId survey) {

					int mealId = 0;

					for (NutritionMappedMeal meal : survey.survey.meals) {
						String mealName = meal.name;
						int foodId = 0;
						for (NutritionMappedFood food : meal.foods) {
							ArrayList<String> row = new ArrayList<String>();

							row.add(survey.id);
							row.add(survey.survey.userName);

							// user custom fields
							for (CustomFieldDef f : dataScheme.userCustomFields()) {
								if (survey.userCustomFields.containsKey(f.key))
									row.add(survey.userCustomFields.get(f.key));
								else
									row.add("N/A");
							}

							row.add(dateFormat.format(new Date(survey.survey.startTime)));
							row.add(dateFormat.format(new Date(survey.survey.endTime)));
							row.add(((survey.survey.endTime - survey.survey.startTime) / (1000 * 60)) + " min");

							// survey custom fields
							for (CustomFieldDef f : dataScheme.surveyCustomFields()) {
								if (survey.survey.customData.containsKey(f.key))
									row.add(survey.survey.customData.get(f.key));
								else
									row.add("N/A");
							}

							row.add(Integer.toString(mealId));
							row.add(mealName);

							// meal custom fields
							for (CustomFieldDef f : dataScheme.mealCustomFields()) {
								if (meal.customData.containsKey(f.key))
									row.add(meal.customData.get(f.key));
								else
									row.add("N/A");
							}

							row.add(Integer.toString(foodId));
							row.add(food.searchTerm);
							row.add(food.code);
							row.add(food.englishDescription);
							row.add(food.localDescription.getOrElse("N/A"));
							row.add(food.nutrientTableID);
							row.add(food.nutrientTableCode);

							row.add(Integer.toString(food.foodGroupCode));
							row.add(food.foodGroupEnglishDescription);
							row.add(food.foodGroupLocalDescription.getOrElse("N/A"));
							row.add(food.isReadyMeal ? "yes" : "no");
							row.add(food.brand);

							// food custom fields
							for (CustomFieldDef f : dataScheme.foodCustomFields()) {
								if (food.customData.containsKey(f.key))
									row.add(food.customData.get(f.key));
								else
									row.add("N/A");
							}

							double weight = food.portionSize.servingWeight() - food.portionSize.leftoversWeight();

							row.add(String.format("%.2f", food.portionSize.servingWeight()));
							row.add(food.portionSize.data.containsKey("servingImage") ? food.portionSize.data.get("servingImage") : "N/A");
							row.add(String.format("%.2f", food.portionSize.leftoversWeight()));
							row.add(food.portionSize.data.containsKey("leftoversImage") ? food.portionSize.data.get("leftoversImage") : "N/A");
							row.add(String.format("%.2f", weight));

							row.add(food.reasonableAmount ? "yes" : "no");

							// missing foods

							String missingFoodDescription = food.customData.get(MissingFoodRecord.KEY_DESCRIPTION);
							if (missingFoodDescription == null)
								missingFoodDescription = "N/A";
							String missingFoodPortionSize = food.customData.get(MissingFoodRecord.KEY_PORTION_SIZE);
							if (missingFoodPortionSize == null)
								missingFoodPortionSize = "N/A";
							String missingFoodLeftovers = food.customData.get(MissingFoodRecord.KEY_LEFTOVERS);
							if (missingFoodLeftovers == null)
								missingFoodLeftovers = "N/A";

							row.add(missingFoodDescription);
							row.add(missingFoodPortionSize);
							row.add(missingFoodLeftovers);

							for (Nutrient n : sortedNutrients)
								if (food.nutrients.containsKey(n.key()))
									row.add(String.format("%.2f", food.nutrients.get(n.key())));
								else
									row.add("N/A");

							writer.writeNext(row.toArray(new String[row.size()]));
							foodId++;
						}
						mealId++;
					}
				}
			});
		} catch (DataStoreException e) {
			throw new RuntimeException(e);
		} finally {
			writer.close();
		}
	}
}