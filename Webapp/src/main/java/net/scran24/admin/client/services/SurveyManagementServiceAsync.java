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

This file is based on Intake24 v1.0.

© Crown copyright, 2012, 2013, 2014

Licensed under the Open Government Licence 3.0: 

http://www.nationalarchives.gov.uk/doc/open-government-licence/
*/

package net.scran24.admin.client.services;

import java.util.List;

import org.workcraft.gwt.shared.client.Option;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SurveyManagementServiceAsync
{
    public static final class Util 
    { 
        private static SurveyManagementServiceAsync instance;

        public static final SurveyManagementServiceAsync getInstance()
        {
            if ( instance == null )
            {
                instance = (SurveyManagementServiceAsync) GWT.create( SurveyManagementService.class );
            }
            return instance;
        }

        private Util() {}
    }

	void createSurvey(String id, String scheme_id, String locale, boolean allowGenUsers, Option<String> surveyMonkeyUrl, AsyncCallback<Option<String>> callback);

	void listSurveys(AsyncCallback<List<String>> callback);
}
