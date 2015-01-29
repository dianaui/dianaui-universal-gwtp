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
package com.dianaui.universal.gwtp.client;

import com.dianaui.universal.core.client.event.HideEvent;
import com.dianaui.universal.core.client.event.HideHandler;
import com.dianaui.universal.core.client.ui.Modal;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PopupViewCloseHandler;
import com.gwtplatform.mvp.client.PopupViewImpl;
import com.gwtplatform.mvp.client.ViewImpl;
import com.gwtplatform.mvp.client.proxy.NavigationEvent;
import com.gwtplatform.mvp.client.proxy.NavigationHandler;
import com.gwtplatform.mvp.client.view.CenterPopupPositioner;
import com.gwtplatform.mvp.client.view.PopupPositioner;

/**
 * @author <a href='mailto:donbeave@gmail.com'>Alexey Zhokhov</a>
 */
public abstract class ModalViewImpl extends ViewImpl implements PopupView {

    private HandlerRegistration autoHideHandler;

    private HandlerRegistration closeHandlerRegistration;
    private PopupViewCloseHandler popupViewCloseHandler;
    private final EventBus eventBus;

    private PopupPositioner positioner;

    /**
     * The {@link PopupViewImpl} class uses the {@link EventBus} to listen to
     * {@link NavigationEvent} in order to automatically close when this event
     * is fired, if desired. See
     * {@link #setAutoHideOnNavigationEventEnabled(boolean)} for details.
     *
     * @param eventBus The {@link EventBus}.
     */
    protected ModalViewImpl(final EventBus eventBus) {
        this(eventBus, new CenterPopupPositioner());
    }

    /**
     * @param eventBus   The {@link EventBus}.
     * @param positioner The {@link PopupPositioner} used to position the popup onReveal();
     * @see com.gwtplatform.mvp.client.view.CenterPopupPositioner
     * @see com.gwtplatform.mvp.client.view.RelativeToWidgetPopupPositioner
     * @see com.gwtplatform.mvp.client.view.TopLeftPopupPositioner
     */
    protected ModalViewImpl(EventBus eventBus, PopupPositioner positioner) {
        this.eventBus = eventBus;
        setPopupPositioner(positioner);
        if (repositionOnWindowResize()) {
            Window.addResizeHandler(new ResizeHandler() {
                @Override
                public void onResize(ResizeEvent event) {
                    if (isShowing()) {
                        showAndReposition();
                    }
                }
            });
        }
    }

    public boolean isShowing() {
        return asModal().isViewing();
    }

    @Override
    public void center() {
        // no op this method is here for legacy compatibility.
        // since by default the popup positioner centers the popup only
        // popups using a non default positioner would be affected by this method.
        // And if you're using popup positioners you won't be calling this method.
    }

    @Override
    public void hide() {
        asModal().hide();

        // events will not fire call closeHandler manually.
        if (popupViewCloseHandler != null) {
            popupViewCloseHandler.onClose();
        }
    }

    @Override
    public void setAutoHideOnNavigationEventEnabled(boolean autoHide) {
        if (autoHide) {
            if (autoHideHandler != null) {
                return;
            }
            autoHideHandler = eventBus.addHandler(NavigationEvent.getType(),
                    new NavigationHandler() {
                        @Override
                        public void onNavigation(NavigationEvent navigationEvent) {
                            hide();
                        }
                    });
        } else {
            if (autoHideHandler != null) {
                autoHideHandler.removeHandler();
            }
        }
    }

    @Override
    public void setCloseHandler(final PopupViewCloseHandler popupViewCloseHandler) {
        this.popupViewCloseHandler = popupViewCloseHandler;
        if (closeHandlerRegistration == null) {
            closeHandlerRegistration = asModal().addHideHandler(new HideHandler() {
                @Override
                public void onHide(HideEvent hideEvent) {
                    if (ModalViewImpl.this.popupViewCloseHandler != null) {
                        ModalViewImpl.this.popupViewCloseHandler.onClose();
                    }
                }
            });
        }
    }

    @Override
    public void setPopupPositioner(PopupPositioner popupPositioner) {
        this.positioner = popupPositioner;
    }

    @Override
    public void setPosition(int left, int top) {
        // TODO
    }

    @Override
    public void show() {
        asModal().show();
    }

    @Override
    public void showAndReposition() {
        onReposition();

        // TODO
        asModal().show();
    }

    /**
     * Override this method to add custom logic that runs before the popup is repositioned.
     * By default the popup will be repositioned on resize and this method will be called.
     * So you can add any resize logic here as well.
     */
    protected void onReposition() {
    }

    /**
     * By default PopupViews will reposition themselves when the window is resized.
     * If you don't want the popup to be repositioned or want to handle the resize yourself
     * overide this method to return false.
     *
     * @return whether to reposition on resize.
     */
    protected boolean repositionOnWindowResize() {
        return true;
    }


    /**
     * Retrieves this view as a {@link Modal}. See {@link #asWidget()}.
     *
     * @return This view as a {@link Modal} object.
     */
    protected Modal asModal() {
        return (Modal) asWidget();
    }

    protected EventBus getEventBus() {
        return eventBus;
    }

}
