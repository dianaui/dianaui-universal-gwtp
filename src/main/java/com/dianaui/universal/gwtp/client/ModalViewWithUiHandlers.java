package com.dianaui.universal.gwtp.client;

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

import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.UiHandlers;

/**
 * @author <a href='mailto:donbeave@gmail.com'>Alexey Zhokhov</a>
 */
public abstract class ModalViewWithUiHandlers<C extends UiHandlers> extends ModalViewImpl implements HasUiHandlers<C> {

    private C uiHandlers;

    protected ModalViewWithUiHandlers(EventBus eventBus) {
        super(eventBus);
    }

    /**
     * Access the {@link UiHandlers} associated with this {@link com.gwtplatform.mvp.client.View}.
     * <p/>
     * <b>Important!</b> Never call {@link #getUiHandlers()} inside your constructor
     * since the {@link UiHandlers} are not yet set.
     *
     * @return The {@link UiHandlers}, or {@code null} if they are not yet set.
     */
    protected C getUiHandlers() {
        return uiHandlers;
    }

    @Override
    public void setUiHandlers(C uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

}
