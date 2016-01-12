/*
This file is part of Intake24.

© Crown copyright, 2012, 2013, 2014.

This software is licensed under the Open Government Licence 3.0:

http://www.nationalarchives.gov.uk/doc/open-government-licence/
*/

package net.scran24.user.client.survey.prompts;

import net.scran24.common.client.WidgetFactory;
import net.scran24.user.client.survey.SurveyStageInterface;
import net.scran24.user.client.survey.flat.Prompt;
import net.scran24.user.client.survey.flat.Survey;
import net.scran24.user.client.survey.flat.SurveyOperation;
import net.scran24.user.client.survey.prompts.messages.PromptMessages;

import org.workcraft.gwt.shared.client.Callback1;
import org.workcraft.gwt.shared.client.Function1;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

public class EmptySurveyPrompt implements Prompt<Survey, SurveyOperation> {
	private final PromptMessages messages = GWT.create(PromptMessages.class);
	
	@Override
	public SurveyStageInterface getInterface(final Callback1<SurveyOperation> onComplete,
			final Callback1<Function1<Survey, Survey>> onIntermediateStateChange) {

		FlowPanel content = new FlowPanel();
		
		content.add(WidgetFactory.createPromptPanel(SafeHtmlUtils.fromSafeConstant(messages.emptySurvey_promptText())));
		
		Button addMeal = WidgetFactory.createButton(messages.energyValidation_addMealButtonLabel(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				onComplete.call(SurveyOperation.addMealRequest(0));
			}
		});
				
		content.add(WidgetFactory.createButtonsPanel(addMeal));

		return new SurveyStageInterface.Aligned(content, HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_TOP,
				SurveyStageInterface.DEFAULT_OPTIONS);
	}

	@Override
	public String toString() {
		return "Empty survey";
	}
}