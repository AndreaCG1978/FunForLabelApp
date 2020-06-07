<?php
/*
  $Id$

  osCommerce, Open Source E-Commerce Solutions
  http://www.oscommerce.com

  Copyright (c) 2007 osCommerce

  Released under the GNU General Public License
*/

// define the database table names used in the project
  define('TABLE_ACTION_RECORDER', 'action_recorder');
  define('TABLE_ADDRESS_BOOK', 'address_book');
  define('TABLE_ADDRESS_FORMAT', 'address_format');
  define('TABLE_ADMINISTRATORS', 'administrators');
  define('TABLE_BANNERS', 'banners');
  define('TABLE_BANNERS_HISTORY', 'banners_history');
  define('TABLE_CATEGORIES', 'categories');
  define('TABLE_CATEGORIES_DESCRIPTION', 'categories_description');
  define('TABLE_CONFIGURATION', 'configuration');
  define('TABLE_CONFIGURATION_GROUP', 'configuration_group');
  define('TABLE_COUNTER', 'counter');
  define('TABLE_COUNTER_HISTORY', 'counter_history');
  define('TABLE_COUNTRIES', 'countries');
  define('TABLE_CURRENCIES', 'currencies');
  define('TABLE_CUSTOMERS', 'customers');
  define('TABLE_CUSTOMERS_BASKET', 'customers_basket');
  define('TABLE_CUSTOMERS_BASKET_ATTRIBUTES', 'customers_basket_attributes');
  define('TABLE_CUSTOMERS_INFO', 'customers_info');
  define('TABLE_LANGUAGES', 'languages');
  define('TABLE_MANUFACTURERS', 'manufacturers');
  define('TABLE_MANUFACTURERS_INFO', 'manufacturers_info');
  define('TABLE_ORDERS', 'orders');
  define('TABLE_ORDERS_PRODUCTS', 'orders_products');
  define('TABLE_ORDERS_PRODUCTS_ATTRIBUTES', 'orders_products_attributes');
  define('TABLE_ORDERS_PRODUCTS_DOWNLOAD', 'orders_products_download');
  define('TABLE_ORDERS_STATUS', 'orders_status');
  define('TABLE_ORDERS_STATUS_HISTORY', 'orders_status_history');
  define('TABLE_ORDERS_TOTAL', 'orders_total');
  define('TABLE_PRODUCTS', 'products');
  define('TABLE_PRODUCTS_ATTRIBUTES', 'products_attributes');
  define('TABLE_PRODUCTS_ATTRIBUTES_DOWNLOAD', 'products_attributes_download');
  define('TABLE_PRODUCTS_DESCRIPTION', 'products_description');
  define('TABLE_PRODUCTS_IMAGES', 'products_images');
  define('TABLE_PRODUCTS_NOTIFICATIONS', 'products_notifications');
  define('TABLE_PRODUCTS_OPTIONS', 'products_options');
  define('TABLE_PRODUCTS_OPTIONS_VALUES', 'products_options_values');
  define('TABLE_PRODUCTS_OPTIONS_VALUES_TO_PRODUCTS_OPTIONS', 'products_options_values_to_products_options');
  define('TABLE_PRODUCTS_TO_CATEGORIES', 'products_to_categories');
  define('TABLE_REVIEWS', 'reviews');
  define('TABLE_REVIEWS_DESCRIPTION', 'reviews_description');
  define('TABLE_SESSIONS', 'sessions');
  define('TABLE_SPECIALS', 'specials');
  define('TABLE_TAX_CLASS', 'tax_class');
  define('TABLE_TAX_RATES', 'tax_rates');
  define('TABLE_GEO_ZONES', 'geo_zones');
  define('TABLE_ZONES_TO_GEO_ZONES', 'zones_to_geo_zones');
  define('TABLE_WHOS_ONLINE', 'whos_online');
  define('TABLE_ZONES', 'zones');

  /*
   * Tablas de Tag Creator Module
   */
  define('TCM_CREATORS_PRODUCTS', 'creators_products');
  define('TCM_CREATORS', 'creators');
  define('TCM_CREATOR_TYPES', 'creator_types');
  define('TCM_BORDERS_SOLID', 'borders_solid');
    define('TCM_CREATORS_HAS_BORDERS_SOLID', 'creators_has_borders_solid');
  define('TCM_BORDERS_TEXTURED', 'borders_textured');
    define('TCM_CREATORS_HAS_BORDERS_TEXTURED', 'creators_has_borders_textured');
  define('TCM_FILLS_SOLID', 'fills_solid');
    define('TCM_CREATORS_HAS_FILLS_SOLID', 'creators_has_fills_solid');
  define('TCM_FILLS_TEXTURED', 'fills_textured');
    define('TCM_CREATORS_HAS_FILLS_TEXTURED', 'creators_has_fills_textured');
  define('TCM_ICONS', 'icons');
    define('TCM_CREATORS_HAS_ICONS', 'creators_has_icons');
  define('TCM_UPLOADED_FILES', 'uploaded_files');
  define('TCM_FONTS', 'fonts');
  define('TCM_TEXT_AREAS', 'text_areas');
    define('TCM_TEXT_AREAS_HAS_FONTS', 'text_areas_has_fonts');
  define('TCM_TAGS', 'tags');
  define('TCM_TAG_LEGENDS', 'tag_legends');
  define('TCM_TAG_TEXT_OPTIONS', 'tag_text_options');
  define('TCM_COMBOS', 'combos');
  define('TCM_COMBOS_HAS_PRODUCTS', 'combos_has_products');
  define('TCM_COMBO_SETS', 'combo_sets');
  define('TCM_COMBO_SETS_HAS_PRODUCTS', 'combo_sets_has_products');
  /*
   * Fin Tag Creator Module
   */
// Discount Code - start
  define('TABLE_CUSTOMERS_TO_DISCOUNT_CODES', 'customers_to_discount_codes');
  define('TABLE_DISCOUNT_CODES', 'discount_codes');
// Discount Code - end
?>
