/**
 * LocalizationDemo.java
 * 
 * Copyright � 1998-2011 Research In Motion Limited
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Note: For the sake of simplicity, this sample application may not leverage
 * resource bundles and resource strings.  However, it is STRONGLY recommended
 * that application developers make use of the localization features available
 * within the BlackBerry development platform to ensure a seamless application
 * experience across a variety of languages and geographies.  For more information
 * on localizing your application, please refer to the BlackBerry Java Development
 * Environment Development Guide associated with this release.
 */

package com.rim.samples.device.localizationdemo;

import net.rim.device.api.command.Command;
import net.rim.device.api.command.CommandHandler;
import net.rim.device.api.command.ReadOnlyCommandMetadata;
import net.rim.device.api.i18n.ResourceBundle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.ChoiceField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.MainScreen;

/**
 * This sample has been localized using the ResourceBundle and
 * ResourceBundleFamily classes, which are members of the
 * net.rim.device.api.il8n package.
 * 
 * Instead of hard-coding textual elements in your source code, store text
 * strings in separate resource files. In your source code, use unique
 * identifiers that map to the appropriate resource files. The resource files
 * for a particular locale are stored within a ResourceBundle onject.
 * 
 */
public class LocalizationDemo extends UiApplication {
    /**
     * Entry point for application.
     * 
     * @param args
     *            Command line arguments (not used)
     */
    public static void main(final String[] args) {
        // Create a new instance of the application and make the currently
        // running thread the application's event dispatch thread.
        final LocalizationDemo theApp = new LocalizationDemo();
        theApp.enterEventDispatcher();
    }

    /**
     * Creates a new LocalizationDemo object
     */
    public LocalizationDemo() {
        pushScreen(new LocalizationDemoScreen());
    }
}

/**
 * This class displays a country's information in its native format.
 */
final class LocalizationDemoScreen extends MainScreen implements
        LocalizationDemoResource, FieldChangeListener {
    private final InfoScreen _infoScreen;
    private final ObjectChoiceField _choiceField;

    // Create a ResourceBundle object to contain the localized resources.
    private static ResourceBundle _resources = ResourceBundle.getBundle(
            BUNDLE_ID, BUNDLE_NAME);

    /**
     * Creates a new LocalizationDemoScreen object
     */
    public LocalizationDemoScreen() {
        final LabelField title =
                new LabelField(_resources.getString(APPLICATION_TITLE));
        setTitle(title);

        add(new RichTextField(_resources.getString(FIELD_TITLE),
                Field.NON_FOCUSABLE));
        add(new SeparatorField());
        add(new LabelField());

        final String choices[] = _resources.getStringArray(FIELD_COUNTRIES);
        _choiceField =
                new ObjectChoiceField(_resources.getString(FIELD_CHOICE),
                        choices);
        _choiceField.setChangeListener(this);
        add(_choiceField);

        // Views the country's information
        final MenuItem viewItem =
                new MenuItem(_resources, MENUITEM_VIEW, 0x230010, 0);
        viewItem.setCommand(new Command(new CommandHandler() {
            /**
             * @see net.rim.device.api.command.CommandHandler#execute(ReadOnlyCommandMetadata,
             *      Object)
             */
            public void execute(final ReadOnlyCommandMetadata metadata,
                    final Object context) {
                pushInfoScreen();
            }
        }));
        addMenuItem(viewItem);

        _infoScreen = new InfoScreen();

        _choiceField.setFocus();
    }

    /**
     * FieldChangeListener implementation.
     * 
     * @see net.rim.device.api.ui.FieldChangeListener#fieldChanged(Field,int)
     */
    public void fieldChanged(final Field field, final int context) {
        // Display _infoScreen only when a choice is selected and then clicked.
        if (field instanceof ObjectChoiceField
                && (context & ChoiceField.CONTEXT_CHANGE_OPTION) != 0) {
            pushInfoScreen();
        }
    }

    /**
     * Displays the selected country's information.
     */
    private void pushInfoScreen() {
        final int selectedIndex = _choiceField.getSelectedIndex();
        _infoScreen.updateScreen(selectedIndex);
        UiApplication.getUiApplication().pushScreen(_infoScreen);
    }

    /**
     * Prevent the save dialog from being displayed.
     * 
     * @see net.rim.device.api.ui.container.MainScreen#onSavePrompt()
     */
    public boolean onSavePrompt() {
        return true;
    }
}

/**
 * This class handles displaying a country's information, formatted using the
 * appropriate locale.
 */
class InfoScreen extends MainScreen implements LocalizationDemoResource {
    private final LabelField _countryField;
    private final BasicEditField _popField;
    private final BasicEditField _langField;
    private final BasicEditField _citiesField;

    private static final int US = 0;
    private static final int CHINA = 1;
    private static final int GERMANY = 2;

    private static ResourceBundle _resources = ResourceBundle.getBundle(
            BUNDLE_ID, BUNDLE_NAME);

    /**
     * Creates a new InfoScreen object
     */
    public InfoScreen() {
        _countryField = new LabelField();
        _popField =
                new BasicEditField(_resources.getString(FIELD_POP), null, 20,
                        Field.NON_FOCUSABLE);
        _langField =
                new BasicEditField(_resources.getString(FIELD_LANG), null, 20,
                        Field.NON_FOCUSABLE);
        _citiesField =
                new BasicEditField(_resources.getString(FIELD_CITIES), null,
                        50, Field.NON_FOCUSABLE);

        add(_countryField);
        add(new SeparatorField());
        add(_popField);
        add(_langField);
        add(_citiesField);
    }

    /**
     * Display the currently selected country's information.
     * 
     * @param index
     *            The index of the selected country
     */
    void updateScreen(final int index) {
        switch (index) {
        case US:
            _countryField.setText(_resources.getString(FIELD_US));
            _popField.setText(_resources.getString(FIELD_US_POP));
            _langField.setText(_resources.getString(FIELD_US_LANG));
            _citiesField.setText(_resources.getString(FIELD_US_CITIES));
            break;

        case CHINA:
            _countryField.setText(_resources.getString(FIELD_CHINA));
            _popField.setText(_resources.getString(FIELD_CHINA_POP));
            _langField.setText(_resources.getString(FIELD_CHINA_LANG));
            _citiesField.setText(_resources.getString(FIELD_CHINA_CITIES));
            break;

        case GERMANY:
            _countryField.setText(_resources.getString(FIELD_GERMANY));
            _popField.setText(_resources.getString(FIELD_GERMANY_POP));
            _langField.setText(_resources.getString(FIELD_GERMANY_LANG));
            _citiesField.setText(_resources.getString(FIELD_GERMANY_CITIES));
            break;
        }
    }
}
