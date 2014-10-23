/*
 * #%L
 * Diana UI Core
 * %%
 * Copyright (C) 2014 Diana UI
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.dianaui.universal.gwtp.client.proxy;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.annotations.DefaultPlace;
import com.gwtplatform.mvp.client.annotations.ErrorPlace;
import com.gwtplatform.mvp.client.annotations.UnauthorizedPlace;
import com.gwtplatform.mvp.client.proxy.DefaultPlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.gwtplatform.mvp.shared.proxy.TokenFormatter;

import javax.inject.Inject;
import java.util.List;

/**
 * @author <a href='mailto:donbeave@gmail.com'>Alexey Zhokhov</a>
 */
public class PlaceManager extends DefaultPlaceManager {

    private final TokenFormatter tokenFormatter;
    private String historyToken = null;
    private String previousHistoryToken = null;

    @Inject
    public PlaceManager(final EventBus eventBus,
                        final TokenFormatter tokenFormatter,
                        final @DefaultPlace String defaultPlaceNameToken,
                        final @ErrorPlace String errorPlaceNameToken,
                        final @UnauthorizedPlace String unauthorizedPlaceNameToken) {
        super(eventBus, tokenFormatter, defaultPlaceNameToken, errorPlaceNameToken, unauthorizedPlaceNameToken);

        this.tokenFormatter = tokenFormatter;

        History.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(final ValueChangeEvent<String> event) {
                previousHistoryToken = historyToken;
                historyToken = event.getValue();
            }
        });
    }

    public String getPreviousHistoryToken() {
        return previousHistoryToken;
    }

    public String getPreviousNameToken() {
        PlaceRequest request = getPreviousPlaceRequest();

        if (request != null)
            return request.getNameToken();

        return null;
    }

    public PlaceRequest getPreviousPlaceRequest() {
        List<PlaceRequest> hierarchy = getPreviousPlaceHierarchy();

        if (hierarchy != null && !hierarchy.isEmpty())
            return hierarchy.get(hierarchy.size() - 1);

        return null;
    }

    public List<PlaceRequest> getPreviousPlaceHierarchy() {
        if (previousHistoryToken != null)
            return tokenFormatter.toPlaceRequestHierarchy(previousHistoryToken);

        return null;
    }


    public PlaceRequest getFirstPlaceRequestByToken(final String token) {
        for (PlaceRequest placeRequest : getCurrentPlaceHierarchy()) {
            if (placeRequest.getNameToken().equals(token))
                return placeRequest;
        }
        return null;
    }

    public String getHref(final PlaceRequest.Builder builder) {
        return getHref(builder, false);
    }

    public String getHref(final PlaceRequest.Builder builder, final boolean relative) {
        return "#" + (relative ? buildRelativeHistoryToken(builder.build()) : buildHistoryToken(builder.build()));
    }

    public String getHistoryToken(final PlaceRequest.Builder builder) {
        return getHistoryToken(builder, false);
    }

    public String getHistoryToken(final PlaceRequest.Builder builder, final boolean relative) {
        return relative ? buildRelativeHistoryToken(builder.build()) : buildHistoryToken(builder.build());
    }

}
