/*
 * Copyright (c) 2016, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package jdk.javadoc.internal.doclets.toolkit;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Access to the localizable resources used by a doclet.
 * The resources are split across two resource bundles:
 * one that contains format-neutral strings common to
 * all supported formats, and one that contains strings
 * specific to the selected doclet, such as the standard
 * HTML doclet.
 */
public class Resources {
    private final Configuration configuration;
    private final String commonBundleName;
    private final String docletBundleName;

    protected ResourceBundle commonBundle;
    protected ResourceBundle docletBundle;

    /**
     * Creates a {@code Resources} to provide access the resource
     * bundles used by a doclet.
     *
     * @param configuration the configuration for the doclet,
     *  to provide access the locale to be used when accessing the
     *  names resource bundles.
     * @param commonBundleName the name of the bundle containing the strings
     *  common to all output formats
     * @param docletBundleName the name of the bundle containing the strings
     *  specific to a particular format
     */
    public Resources(Configuration configuration, String commonBundleName, String docletBundleName) {
        this.configuration = configuration;
        this.commonBundleName = commonBundleName;
        this.docletBundleName = docletBundleName;
    }

    /**
     * Gets the string for the given key from one of the doclet's
     * resource bundles.
     *
     * The more specific bundle is checked first;
     * if it is not there, the common bundle is then checked.
     *
     * @param key the key for the desired string
     * @return the string for the given key
     * @throws MissingResourceException if the key is not found in either
     *  bundle.
     */
    public String getText(String key) throws MissingResourceException {
        initBundles();

        if (docletBundle.containsKey(key))
            return docletBundle.getString(key);

        return commonBundle.getString(key);
    }
    /**
     * Gets the string for the given key from one of the doclet's
     * resource bundles, substituting additional arguments into
     * into the resulting string with {@link MessageFormat#format}.
     *
     * The more specific bundle is checked first;
     * if it is not there, the common bundle is then checked.
     *
     * @param key the key for the desired string
     * @param args values to be substituted into the resulting string
     * @return the string for the given key
     * @throws MissingResourceException if the key is not found in either
     *  bundle.
     */
    public String getText(String key, Object... args) throws MissingResourceException {
        return MessageFormat.format(getText(key), args);
    }

    /**
     * Lazily initializes the bundles. This is (currently) necessary because
     * this object may be created before the locale to be used is known.
     */
    protected void initBundles() {
        if (commonBundle == null) {
            Locale locale = configuration.getLocale();
            this.commonBundle = ResourceBundle.getBundle(commonBundleName, locale);
            this.docletBundle = ResourceBundle.getBundle(docletBundleName, locale);
        }
    }
}
