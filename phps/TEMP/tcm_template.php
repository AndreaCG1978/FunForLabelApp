<?php
/* 
 * Tag Creator Module
 * 
 * AddOn para osCommerce que permite la generación dinámica de etiquetas.
 * 
 * Copyright (c) 2015 Jorge Alexis Viqueira <jviqueira@viqs.com.ar>
 * 
 * Licencia propietaria, permisos de uso concedido en exclusividad para el
 * sitio http://funforlabels.com. Prohibida la distribución y reproducción
 * de estos archivos por cualquier medio sin previa autorización escrita de
 * Jorge Alexis Viqueira <jviqueira@viqs.com.ar>
 */

if (!defined('TCM_RENDERER_INITIALIZED')) {
    require_once DIR_FS_CATALOG.'includes/modules/tcm/tcm_renderer.php';
    require_once DIR_FS_CATALOG.'includes/modules/tcm/tcm_tag.php';
    $product_id = $HTTP_GET_VARS['products_id'];
    //Parche para el bug del sitio definitivo que a veces el product_id viene como:
    //http://funforlabels.com/store/catalog/product_info.php?products_id=111?osCsid=o9ch3mrpnpm9tiu5lnt1jui2q4
    //http://funforlabels.com/store/catalog/product_info.php?products_id=110%A0osCsid=c63bcfgg3rljp2jd9eepehg747
    $product_id = (int)$product_id;
    $current_id = null;//Que adivine si el producto responde a un creador o combo
    define('TCM_RENDERER_INITIALIZED', true);
} else {
    $product_id=null;
    require_once DIR_FS_ADMIN.'includes/modules/tcm/tcm_tag.php';
}

//Traducciones
require_once DIR_FS_CATALOG_LANGUAGES.$language.'/modules/tcm/tcm_creator.php';

try {
    $tag = new TCM_TAG($product_id, $current_id);
    $tcm_has_creator = true;
} catch(Exception $e) {
    //echo $e->getCode().': '.$e->getMessage().'<br>';
    $tcm_has_creator = false;
    goto noCreatorSelected;
}

if (isset($template) && (isset($template['tab-start']))) {
    $tabStart = $template['tab-start'];
    $tabContentStart = $template['tab-start'];
} else {
    $tabStart = $tabContentStart = 1;
}

$tabs = [
            'title'  =>['name'=>TCM_RENDERER_TAB_TITLE, 'hint'=>TCM_RENDERER_TAB_TITLE_HINT, 'text'=>TCM_RENDERER_TAB_TITLE_TEXT, 'icon'=>'glyphicon-text-size', 'types'=>['title']],
            'text'   => ['name'=>TCM_RENDERER_TAB_TEXT, 'hint'=>TCM_RENDERER_TAB_TEXT_HINT, 'text'=>TCM_RENDERER_TAB_TEXT_TEXT, 'icon'=>'glyphicon-font', 'types'=>['text']],
            'border' => ['name'=>TCM_RENDERER_TAB_BORDER, 'hint'=>TCM_RENDERER_TAB_BORDER_HINT, 'text'=>TCM_RENDERER_TAB_BORDER_TEXT,'icon'=>'glyphicon-edit','types'=>['borderSolid', 'borderSolidCustom']],
            'fill'   => ['name'=>TCM_RENDERER_TAB_FILL, 'hint'=>TCM_RENDERER_TAB_FILL_HINT, 'text'=>TCM_RENDERER_TAB_FILL_TEXT, 'icon'=>'glyphicon-tint', 'types'=>['fillSolid', 'fillSolidCustom', 'fillTextured', 'fillTexturedCustom']],
            'icons'  => ['name'=>TCM_RENDERER_TAB_ICONS, 'hint'=>TCM_RENDERER_TAB_ICONS_HINT, 'text'=>TCM_RENDERER_TAB_ICONS_TEXT, 'icon'=>'glyphicon-picture', 'types'=>['icons', 'iconsCustom']]
        ];
?>
<style type="text/css">
    .wizard .nav-tabs > li {
        width: <?=  round(100/(count($tabs)+1),4)-0.0001?>%;
    }
    .row{
        overflow: hidden; 
    }

    [class*="col-"]{
        margin-bottom: -99999px;
        padding-bottom: 99999px;
    }
</style>

<div id="creatorContainer" class="container">
<?php
      if ((!$current_id) && $product_id) {    ?>

<iframe id="uploadiframe" name="uploadiframe" src="about:blank" style="width:0;height:0;border-style: none"></iframe>
<script type="text/javascript">
    function tcm_upload_complete(status) {
        $(document).trigger('tcm_uploadComplete', [status]);
    }
</script>
<!-- Modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="tcmCreatorModal">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="tcmCreatorModal"><?=TCM_RENDERER_TITLE?></h4>
      </div>
      <div class="modal-body">
          <div class="row" style="overflow: visible">
<?php }
?>
    
    <div class="col-xs-12 col-sm-6 col-md-4 col-lg-4" style="text-align: center">
        <div style="display:inline-block;width:98%">
            <img id="img_preview" src="<?=tep_href_link(FILENAME_TAG_RENDERER,'action=preview&'.($current_id?"creator=$current_id":"product=$product_id").'&rand='.(round(rand(0, 1)*1000)))?>" alt="<?=TCM_RENDERER_PREVIEW?>" class="img-responsive" style="min-width: 20em">
        </div>
        <div style="margin-top: 1em;background-color: #ffffff;">
            <?=TCM_RENDERER_ADVICE?>
        </div>
    </div>
    <div class="col-xs-12 col-sm-6 col-md-8 col-lg-8">
        <div class="wizard">
            <div class="wizard-inner" style="margin-top:1em">
                <div class="connecting-line"></div>
                <ul class="nav nav-tabs" role="tablist">
<?php
                $idx = 1;
                foreach($tabs as $t) {?>
                    <li role="presentation" class="<?=($idx==1)?'active tcm_visible':'tcm_visible'?>">
                        <a href="#step<?=$idx?>" data-toggle="tab" aria-controls="step<?=$idx?>" role="tab" title="<?=$t['hint']?>">
                            <span class="round-tab">
                                <i class="glyphicon <?=$t['icon']?>"></i>
                            </span>
                        </a>
                    </li>
<?php
                    $idx++;
                } ?>

                    <li role="presentation" class="tcm_visible">
                        <a href="#complete" data-toggle="tab" aria-controls="complete" role="tab" title="<?=TCM_RENDERER_TAB_COMPLETE?>">
                            <span class="round-tab">
                                <i class="glyphicon glyphicon-ok"></i>
                            </span>
                        </a>
                    </li>

                </ul>
            </div>

            <!--form role="form"-->
                <div class="tab-content">
<?php
                $idx = 1;
                $icon_controls  = false;//Flag para saber si debo unicar los controles adicionales a iconos
                foreach($tabs as $t) {
?>
                    <div class="tab-pane <?=($idx==1)?'active':''?>" role="tabpanel" id="step<?=$idx?>">
                        <div class="row" style="overflow: visible">
                            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                                <h3><?=$t['name']?>
                        <ul class="list-inline pull-right">
                            <?=($idx>1)?'<li><button type="button" class="btn btn-default prev-step">'.TCM_RENDERER_BTN_PREVIOUS.'</button></li>':''?>
                            <li><button type="button" class="btn btn-primary next-step"><?=TCM_RENDERER_BTN_NEXT?></button></li>
                            <li><button type="button" class="btn btn-primary finish-step"><?=TCM_RENDERER_BTN_FINISH?></button></li>
                        </ul>
                                </h3>
                                <p><?=$t['text']?></p>
                            </div>
                        </div>
                        <div class="row" style="overflow: visible">
                                   <?php
                    foreach($t['types'] as $type) {
                        switch($type) {
                            case 'borderSolid':         ?>
                                <div class="col-xs-12 col-sm-12 col-md-3 col-lg-3">
                                    <label for="borderSolidSelect"><?=TCM_RENDERER_BORDER_SOLID?>
                                        <div id="borderSolidSelect"></div>    
                                    </label>
                                </div>
<?php                           break;
                            case 'borderSolidCustom':   ?>
                                <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                    <label for="borderSolidCustomWidth"><?=TCM_RENDERER_WIDTH?>
                                        <input id="borderSolidCustomWidth" type="text" name="borderSolidCustom_width" value="" size="3" class="form-control"/>
                                    </label>
                                </div>
                                <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                    <label for="borderSolidCustomColor"><?=TCM_RENDERER_COLOR?><br/>
                                        <input id="borderSolidCustomColor" type="text" name="borderSolidCustom_color" class="startEmpty" value=""/>
                                    </label>
                                </div>
                                <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                    <label for="borderSolidCustomStyle"><?=TCM_RENDERER_STYLE?>
                                        <select id="borderSolidCustomStyle" name="borderSolidCustom_style" class="form-control">
                                            <option value="dashed"><?=TCM_RENDERER_DASHED?></option>
                                            <option value="dotted"><?=TCM_RENDERER_DOTTED?></option>
                                            <option value="solid" selected="selected"><?=TCM_RENDERER_SOLID?></option>
                                        </select>
                                    </label>
                                </div>
<?php                           break;
                            case 'fillSolid':           ?>
                                <div class="col-xs-6 col-sm-3 col-md-3 col-lg-3">
                                    <label for="fillSolidSelect"><?=TCM_RENDERER_FILL_SOLID?>
                                        <div id="fillSolidSelect"></div>    
                                    </label>
                                </div>
<?php                           break;
                            case 'fillSolidCustom':     ?>
                                <div class="col-xs-6 col-sm-3 col-md-3 col-lg-3">
                                    <label for="fillSolidCustomColor"><?=TCM_RENDERER_FILL_SOLID_CUSTOM?><br/>
                                        <input id="fillSolidCustomColor" type="text" name="fillSolidCustom_color" class="startEmpty" value="">
                                    </label>
                                </div>
<?php                           break;
                            case 'fillTextured':        ?>
                                <div class="col-xs-6 col-sm-3 col-md-3 col-lg-3">
                                    <label for="fillTexturedSelect"><?=TCM_RENDERER_FILL_TEXTURED?>
                                        <div id="fillTexturedSelect"></div>    
                                    </label>
                                </div>
<?php                           break;
                            case 'fillTexturedCustom':  ?>
                                <div class="col-xs-6 col-sm-3 col-md-3 col-lg-3">
                                    <form id="formFillTexturedCustom" method="post" enctype="multipart/form-data" target="uploadiframe"
                                          action="<?=((ENABLE_SSL_CATALOG===true)?HTTPS_CATALOG_SERVER:HTTP_CATALOG_SERVER).($current_id?DIR_WS_ADMIN:DIR_WS_HTTP_CATALOG).FILENAME_TAG_RENDERER?>?action=saveFillBackground">
                                        <label for="fillTexturedCustom"><?=TCM_RENDERER_FILL_TEXTURED_CUSTOM?><br>
                                            <span class="btn btn-default btn-file">
                                                <?=TCM_RENDERER_FILL_TEXTURED_CUSTOM_SELECT?> <input type="file" id="fillTexturedCustom" name="image">
                                            </span>
                                        </label>
                                        <img id="fillTexturedPreview" src="<?=(((!$current_id) && $product_id)?'':'../').DIR_WS_IMAGES.'tcm/none.png'?>" alt="<?=TCM_RENDERER_FILL_TEXTURED_CUSTOM_PREVIEW?>" style="width: 5em">
                                        <input type="hidden" name="<?=$current_id?'creator':'product'?>" value="<?=$current_id?$current_id:$product_id?>"><br>
                                        <?php if ($tag->isCombo()) { ?>
                                        <input type="hidden" name="combo" value="<?=$product_id?>"><br>
                                        <?php } ?>
                                        <?=TCM_RENDERER_FILL_TEXTURED_CUSTOM_TEXT?>
                                    </form>
                                </div>
<?php                           break;
                            case 'icons':           
                                $icon_controls  = true;     ?>
                                <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4">
                                    <label for="iconSelect"><?=TCM_RENDERER_ICON?>
                                        <div id="iconSelect"></div>    
                                    </label>
                                </div>
<?php                           break;
                            case 'iconsCustom':     
                                $icon_controls  = true;     ?>
                                <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4">
                                    <form id="formIconCustom" method="post" enctype="multipart/form-data" target="uploadiframe"
                                          action="<?=((ENABLE_SSL_CATALOG===true)?HTTPS_CATALOG_SERVER:HTTP_CATALOG_SERVER).($current_id?DIR_WS_ADMIN:DIR_WS_HTTP_CATALOG).FILENAME_TAG_RENDERER?>?action=saveIcon">
                                        <label for="iconCustom"><?=TCM_RENDERER_ICON_CUSTOM?><br>
                                            <span class="btn btn-default btn-file">
                                                <?=TCM_RENDERER_FILL_TEXTURED_CUSTOM_SELECT?> <input type="file" id="iconCustom" name="image">
                                            </span>
                                        </label>
                                        <img id="iconPreview" src="<?=(((!$current_id) && $product_id)?'':'../').DIR_WS_IMAGES.'tcm/none.png'?>">
                                        <input type="hidden" name="<?=$current_id?'creator':'product'?>" value="<?=$current_id?$current_id:$product_id?>"><br>
                                        <?php if ($tag->isCombo()) { ?>
                                        <input type="hidden" name="combo" value="<?=$product_id?>"><br>
                                        <?php } ?>
                                        <?=TCM_RENDERER_ICON_CUSTOM_TEXT?>
                                    </form>
                                </div>
<?php                           break;
                            case 'text':                    ?>
                                <div class="col-xs-12 col-sm-8 col-md-8 col-lg-8">
                                    <label for="textMultiline" class="">
                                        <?=TCM_RENDERER_TEXT_TEXT?>
                                        <textarea id="textMultiline" cols="40" rows="2" class="form-control"></textarea>
                                    </label>
                                    <label for="text" class="">
                                        <?=TCM_RENDERER_TEXT_TEXT?>
                                        <input type="text" id="text" size="40" class="form-control">
                                    </label>
                                </div>
                                <div class="col-xs-12 col-sm-4 col-md-4 col-lg-4">
                                    <label for="fontFace" class="">
                                        <?=TCM_RENDERER_TEXT_FONT?>
                                        <select id="fontFace" name="fontSize" class="form-control" style="width: 150px;background: url('<?=(((!$current_id) && $product_id)?'':'../').DIR_WS_IMAGES.'tcm/'?>Helvetica-Regular.png') no-repeat;">
                                        </select>
                                    </label>
                                </div>
                                <div class="clearfix visible-lg-block"></div>
                                <div class="col-xs-4 col-sm-3 col-md-3 col-lg-3">
                                    <label for="fontSize" class=""><?=TCM_RENDERER_TEXT_SIZE?>:
                                        <select id="fontSize" name="fontSize" class="form-control">
                                            <option value="8">8</option>
                                            <option value="10">10</option>
                                            <option value="12" selected="selected">12</option>
                                            <option value="14">14</option>
                                            <option value="16">16</option>
                                            <option value="18">18</option>
                                            <option value="20">20</option>
                                            <option value="22">22</option>
                                        </select>
                                    </label>
                                </div>
                                <div class="col-xs-4 col-sm-3 col-md-3 col-lg-3">
                                    <label for="textColor" class="control-margin "><?=TCM_RENDERER_TEXT_COLOR?><br/>
                                        <input id="textColor" type="text" name="textColor" class="startEmpty">
                                    </label>
                                </div>
                                <div class="col-xs-4 col-sm-3 col-md-3 col-lg-3">
                                    <div class="checkbox">
                                        <label for="textBold" class="">
                                            <input id="textBold" type="checkbox"> <?=TCM_RENDERER_TEXT_BOLD?>
                                        </label>
                                    </div>
                                </div>
                                <div class="col-xs-4 col-sm-3 col-md-3 col-lg-3">
                                    <div class="checkbox">
                                        <label for="textItalic" class="">
                                            <input id="textItalic" type="checkbox"> <?=TCM_RENDERER_TEXT_ITALIC?>
                                        </label>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                                    <label for="textInputLegend" class="control-label">
                                        <?=TCM_RENDERER_TEXT_LEGENDS?> (<span id="legend_count"></span>)
                                        <div class="input-group">
                                            <input id="textInputLegend" type="text" class="form-control" style="width: 12em">
                                            <span id="textInputLegendAdd" class="input-group-addon"><span class="glyphicon glyphicon-plus"></span></span>
                                        </div>
                                    </label>
                                    <div id="textLegends" style="font-size:1.5em;margin-bottom: .3em">
                                    </div>
                                </div>
<?php                           break;
                            case 'title':   ?>
                                <div class="col-xs-12 col-sm-8 col-md-8 col-lg-8">
                                    <label for="titleMultiline" class="">
                                        <?=TCM_RENDERER_TITLE_TEXT?>
                                        <textarea id="titleMultiline" cols="40" rows="2" class="form-control"></textarea>
                                    </label>
                                    <label for="title" class="">
                                        <?=TCM_RENDERER_TITLE_TEXT?>
                                        <input id="title" type="text" size="40" class="form-control">
                                    </label>
                                </div>
                                <div class="col-xs-12 col-sm-4 col-md-4 col-lg-4">
                                    <label for="titleFontFace" class="">
                                        <?=TCM_RENDERER_TEXT_FONT?>
                                        <select id="titleFontFace" name="titleFontSize" class="form-control" style="width: 150px;background: url('<?=(((!$current_id) && $product_id)?'':'../').DIR_WS_IMAGES.'tcm/'?>Helvetica-Regular.png') no-repeat;">
                                            <option value="1" style="width:120px;height:25px;background: url('<?=(((!$current_id) && $product_id)?'':'../').DIR_WS_IMAGES.'tcm/'?>Helvetica-Regular.png') no-repeat;" data-basename="Helvetica" label="Helvetica" selected="selected"></option>
                                        </select>
                                    </label>
                                </div>
                                <div class="clearfix visible-lg-block"></div>
                                <div class="col-xs-4 col-sm-3 col-md-3 col-lg-3">
                                    <label for="titleFontSize" class=""><?=TCM_RENDERER_TEXT_SIZE?>:
                                        <select id="titleFontSize" name="titleFontSize" class="form-control">
                                            <option value="8">8</option>
                                            <option value="10">10</option>
                                            <option value="12" selected="selected">12</option>
                                            <option value="14">14</option>
                                            <option value="16">16</option>
                                            <option value="18">18</option>
                                            <option value="20">20</option>
                                            <option value="22">22</option>
                                        </select>
                                    </label>
                                </div>
                                <div class="col-xs-4 col-sm-3 col-md-3 col-lg-3">
                                    <label for="titleColor" class="control-margin "><?=TCM_RENDERER_TEXT_COLOR?><br/>
                                        <input id="titleColor" type="text" name="titleColor" class="startEmpty">
                                    </label>
                                </div>
                                <div class="col-xs-4 col-sm-3 col-md-3 col-lg-3">
                                    <div class="checkbox">
                                        <label for="titleBold" class="">
                                            <input id="titleBold" type="checkbox"> <?=TCM_RENDERER_TEXT_BOLD?>
                                        </label>
                                    </div>
                                </div>
                                <div class="col-xs-4 col-sm-3 col-md-3 col-lg-3">
                                    <div class="checkbox">
                                        <label for="titleItalic" class="">
                                            <input id="titleItalic" type="checkbox"> <?=TCM_RENDERER_TEXT_ITALIC?>
                                        </label>
                                    </div>
                                </div>

<?php                           break;
                        }
                    }
                    if ($icon_controls) {
                        $icon_controls = false;     ?>
                            <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4">
                                <label for="iconFill" class="active-control control-margin"><?=TCM_RENDERER_ICON_FILL?><br/>
                                    <input id="iconFill" type="text" name="iconFill" class="startEmpty">
                                </label>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4">
                                <label for="iconWidth" class="active-control control-margin"><?=TCM_RENDERER_WIDTH?>
                                    <input id="iconWidth" type="text" name="iconWidth" value="1" size="3" class="form-control"/>
                                </label>
                            </div>
                            <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4">
                                <label for="iconStyle" class="active-control control-margin"><?=TCM_RENDERER_STYLE?>
                                    <select id="iconStyle" name="iconStyle" class="form-control">
                                        <option value="dashed"><?=TCM_RENDERER_DASHED?></option>
                                        <option value="dotted"><?=TCM_RENDERER_DOTTED?></option>
                                        <option value="solid" selected="selected"><?=TCM_RENDERER_SOLID?></option>
                                    </select>
                                </label>
                            </div>
                            <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4">
                                <label for="iconColor" class="active-control control-margin"><?=TCM_RENDERER_COLOR?><br/>
                                    <input id="iconColor" type="text" name="iconColor" class="startEmpty">
                                </label>
                            </div>
<?php               }           ?>
                        </div>
                    </div>
<?php
                    $idx++;
                }
?>
                    <div class="tab-pane" role="tabpanel" id="complete">
                        <h3><?=TCM_RENDERER_TAB_COMPLETE?>
                            <ul class="list-inline pull-right">
                                <li><button type="button" class="btn btn-default prev-step"><?=TCM_RENDERER_BTN_PREVIOUS?></button></li>
                                <li><button type="button" class="btn btn-primary btn-info-full buy-step"><?=IMAGE_BUTTON_IN_CART?></button></li>
                            </ul>
                        </h3>
                        <div style="clear:right"><?=TCM_RENDERER_COMPLETE_TEXT?></div>
                        <div class="row" style="overflow: visible">
                            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                                <label for="completeComments">Escr&iacute;banos:</label><br>
                                <textarea id="completeComments" name="completeComments" class="form-control" rows="3"></textarea>
                                <br>
                            </div>
                        </div>
                    </div>
                    <div class="clearfix"></div>
                </div>
            <!--/form-->
        </div>
    </div>

<?php if ((!$current_id) && $product_id) { ?>
          </div>
      </div>
    </div>
  </div>
</div>       
<?php } ?>

</div>
<script type="text/javascript">
(function () {
    var nextTab = function (elem) {
                    $jq12(elem).find('~li.tcm_visible:first').find('a[data-toggle="tab"]').click();
                  };
    var prevTab = function (elem) {
                    $jq12($jq12(elem).prevAll('li.tcm_visible')[0]).find('a[data-toggle="tab"]').click();
                  };

    //Initialize tooltips
    $jq12('.nav-tabs > li a[title]').tooltip();
    
    //Wizard
    $jq12('a[data-toggle="tab"]').on('show.bs.tab', function (e) {
        var $target = $jq12(e.target);
        if ($target.parent().hasClass('disabled')) {
            return false;
        }
    });

    $jq12(".next-step").click(function (e) {

        var $active = $jq12('.wizard .nav-tabs li.active');
        $active.find('~li.tcm_visible:first').removeClass('disabled');
        nextTab($active);

    });
    $jq12(".prev-step").click(function (e) {

        var $active = $jq12('.wizard .nav-tabs li.active');
        prevTab($active);

    });


<?php if ((!$current_id) && $product_id) { 
        if ($tag->isCombo()) { ?>
            configureDialog({},true);
            
<?php   }
?>
    $jq12(".buy-step").click(function () {
        if ((tcm_legendCount > 0) && (tcm_legendCount === tcm_legendTotal)) {
            alert("<?=TCM_RENDERER_TEXT_LEGENDS_NOT_DETECTED?>");
            return;
        }
        $('form[name=cart_quantity]').append('<input type="hidden" name="id[tcm_tag]" value="<?=dateToUnique(date('YmdHis'))?>">');
        $('form[name=cart_quantity]').submit();
    });
    
    $jq12(".finish-step").click(function() {
       $jq12("#myModal").modal('hide');
    });
<?php 
    }
?>
    
    /* Conjunto de update de parámetros */
    var sendParams = {}, _update = true, _programmed = false, lastChange='20150101000000', _changing = false,
                         _isCreator = <?=($current_id)?'true':'false'?>, _id=<?=($current_id)?$current_id:$product_id?>,
                         _isCombo = <?=$tag->isCombo()?'true':'false'?>, _comboId = <?=$product_id?>,tcm_legendCount = 0, tcm_legendTotal = 0;

    function showHide(name, show) {
        if (show) {
            $(name).parents('div:not(.checkbox,.input-group):first').show();
        } else {
            $(name).parents('div:not(.checkbox,.input-group):first').hide();
        }
    }
    window.tcm_showhide = showHide;
    function tabShowHide(step, show) {
        if (show) {
            $(".wizard-inner li a[aria-controls="+step+"]").parent('li').removeClass('hide').addClass('tcm_visible');
        } else {
            $(".wizard-inner li a[aria-controls="+step+"]").parent('li').addClass('hide').removeClass('tcm_visible');
        }
    }
    
    /* Inicializacion de los contenidos de las solapas */
    /* BORDER SOLID */
    var borderSolidIcons = [];
    var borderSolidSelect = new IconSelect('borderSolidSelect');
    /* BORDERS */
    $jq12('#borderSolidSelect').on('changed', tcm_change('border', 'solid'));
    $jq12('#borderSolidCustomWidth').TouchSpin({min:0, max:100, step:1, verticalbuttons:true});
    $("#borderSolidCustomColor").spectrum({showInput: true, showInitial:true, allowEmpty: true, preferredFormat: "hex",
                                chooseText: "<?=TCM_RENDERER_COLOR_CHOOSE?>",cancelText: "<?=TCM_RENDERER_COLOR_CANCEL?>"<?=((!$current_id) && $product_id)?', appendTo: "#myModal"':''?>});
    //Bindeado asi porque el "change" de Spectrum no funciona
    $("#borderSolidCustomColor").on('change', tcm_change('border', 'color'));
    $jq12('#borderSolidCustomWidth').on('change', tcm_change('border', 'width'));
    $jq12('#borderSolidCustomStyle').on('change', tcm_change('border', 'style'));
    /* FILL SOLID */
    var fillSolidIcons = [];
    var fillSolidSelect = new IconSelect('fillSolidSelect');
    $jq12('#fillSolidSelect').on('changed', tcm_change('fill', 'solid'));
    
    /* FILL SOLID CUSTOM */
    $("#fillSolidCustomColor").spectrum({showInput:true, showInitial:true, allowEmpty:true, preferredFormat: "hex",
            chooseText: "<?=TCM_RENDERER_COLOR_CHOOSE?>",cancelText: "<?=TCM_RENDERER_COLOR_CANCEL?>"<?=((!$current_id) && $product_id)?', appendTo: "#myModal"':''?>});
    $("#fillSolidCustomColor").on('change', tcm_change('fill', 'color'));
    
    /* FILL TEXTURED */
    var fillTexturedIcons = [];
    var fillTexturedSelect = new IconSelect('fillTexturedSelect');
    $jq12('#fillTexturedSelect').on('changed', tcm_change('fill', 'textured'));
    
    /* FILL TEXTURED CUSTOM */
    $('#fillTexturedCustom').on('change',
            (function () {
                var tcm_change_custom = tcm_change('fill', 'custom');
                return function() {
                    $(document).bind('tcm_uploadComplete', function(evt, data) {
                        $(this).unbind('tcm_uploadComplete');
                        if (data[0]==='OK') {
                            if (data[1]['fill']['file']) {
                                $('#fillTexturedPreview').attr('src', '<?=(((!$current_id) && $product_id)?'':'../').DIR_WS_IMAGES.'tcm/files/'?>'+data[1]['fill']['file']);
                            }
                            tcm_change_custom();
                        } else {
                            console.log('ERROR', data);
                        }
                    });
                    $('#formFillTexturedCustom').submit();
                    };
            })());
            
    $('#fillTexturedPreview').on('click', (function() {
                                            var tcm_change_custom = tcm_change('fill', 'custom');
                                            return function() {
                                                if ($('#fillTexturedPreview').attr('src') === '<?=(((!$current_id) && $product_id)?'':'../').DIR_WS_IMAGES.'tcm/none.png'?>') {
                                                    //none
                                                } else {
                                                    tcm_change_custom();
                                                }
                                            };
                                        })());
    /* ICON */
    var iconSelectIcons = [];
    var iconSelect = new IconSelect('iconSelect');
    $jq12('#iconSelect').on('changed', tcm_change('icon', 'icon'));
    $('#iconColor').spectrum({showInput:true, showInitial:true, allowEmpty:true, preferredFormat: "hex",
        chooseText: "<?=TCM_RENDERER_COLOR_CHOOSE?>",cancelText: "<?=TCM_RENDERER_COLOR_CANCEL?>"<?=((!$current_id) && $product_id)?', appendTo: "#myModal"':''?>});
    $('#iconFill').spectrum({showInput:true, showInitial:true, allowEmpty:true, preferredFormat: "hex",
        chooseText: "<?=TCM_RENDERER_COLOR_CHOOSE?>",cancelText: "<?=TCM_RENDERER_COLOR_CANCEL?>"<?=((!$current_id) && $product_id)?', appendTo: "#myModal"':''?>});
    $jq12('#iconWidth').TouchSpin({min:0, max:100, step:1, verticalbuttons:true});
    //Bindeado asi porque el "change" de Spectrum no funciona
    $("#iconColor").on('change', tcm_change('icon', 'color'));
    $("#iconFill").on('change', tcm_change('icon', 'fill'));
    $jq12('#iconWidth').on('change', tcm_change('icon', 'width'));
    $jq12('#iconStyle').on('change', tcm_change('icon', 'style'));
    $('#iconCustom').on('change',
        (function () {
            var tcm_change_custom = tcm_change('icon', 'custom');
            return function() {
                $(document).bind('tcm_uploadComplete', function(evt, data) {
                    $(this).unbind('tcm_uploadComplete');
                    if (data[0]==='OK') {
                        if (data[1]['icon']['file']) {
                            $('#iconPreview').attr('src', '<?=(((!$current_id) && $product_id)?'':'../').DIR_WS_IMAGES.'tcm/files/'?>'+data[1]['icon']['preview']);
                        }
                        tcm_change_custom();
                    } else {
                        console.log('ERROR', data);
                    }
                });
                $('#formIconCustom').submit();
                };
        })());

    $('#iconPreview').on('click', (function() {
                                            var tcm_change_custom = tcm_change('icon', 'custom');
                                            return function() {
                                                if ($('#iconPreview').attr('src') === '<?=(((!$current_id) && $product_id)?'':'../').DIR_WS_IMAGES.'tcm/none.png'?>') {
                                                    //none
                                                } else {
                                                    tcm_change_custom();
                                                }
                                            };
                                        })());
    /* TEXT */
    $('#text').on('change', tcm_change('text', 'text'));
    $('#textMultiline').on('change', tcm_change('text', 'textMultiline'));
    $('#text').on('keyup',  (function() {
        var old = $('#text').val(), f=tcm_change('text', 'text');
        return function() {
            if ($('#text').val() != old) {
                old = $('#text').val();
                f();
            }
        };
    })());
    $('#textMultiline').on('keyup',  (function() {
        var old = $('#textMultiline').val(), f=tcm_change('text', 'textMultiline');
        return function() {
            if ($('#textMultiline').val() != old) {
                old = $('#textMultiline').val();
                f();
            }
        };
    })());
    
    $('#textColor').spectrum({showInput:true, showInitial:true, allowEmpty:true, preferredFormat: "hex",
        chooseText: "<?=TCM_RENDERER_COLOR_CHOOSE?>",cancelText: "<?=TCM_RENDERER_COLOR_CANCEL?>"<?=((!$current_id) && $product_id)?', appendTo: "#myModal"':''?>});
    $('#fontFace').on('change', tcm_change('text', 'font'));
    $('#fontSize').on('change', tcm_change('text', 'size'));
    $('#textColor').on('change', tcm_change('text', 'color'));
    $('#textBold').on('change', tcm_change('text', 'bold'));
    $('#textItalic').on('change', tcm_change('text', 'italic'));

    var textLegends = $('#textInputLegend'), textCount = $('#legend_count'), textChange = tcm_change('text', 'legends');
    window.tcm_addLegend = function addLegend() {
        if ((textLegends.val() !== '') && (tcm_legendCount > 0)) {
            $('#textLegends').append(' <span class="label label-primary" style="cursor: pointer">'+textLegends.val()+' <span aria-label="<?=TCM_RENDERER_TEXT_LEGENDS_DELETE?>" class="glyphicon glyphicon-remove" style="cursor: pointer"></span></span>');
            var last = $('#textLegends').children('span').last();
            last.on('dblclick', function() {tcm_clickLegend(this, true);});
            last.find('span').on('click', function() {tcm_clickLegend(this, false);});
            textLegends.val('');
            tcm_legendCount--;
            textCount.text(tcm_legendCount);
            textChange();
        }
    };
    window.tcm_clickLegend = function clickLegend(wich, edit) {
         if (edit) {
             textLegends.val($(wich).text().trim());
             textLegends.focus();
             $(wich).remove();
         } else {
             $(wich).parent().remove();
         }
         tcm_legendCount++;
         textCount.text(tcm_legendCount);
         textChange();
    };

    $('#textInputLegendAdd').on('click', tcm_addLegend);
    $('#textInputLegend').on('keyup', function(event) {
         if ( event.which == 13 ) {
             tcm_addLegend();
         }
    });
    
    /* TITLE */
    
    $('#title').on('change', tcm_change('title', 'text'));
    $('#titleMultiline').on('change', tcm_change('title', 'textMultiline'));
    $('#title').on('keyup',  (function() {
        var old = $('#title').val(), f=tcm_change('title', 'text');
        return function() {
            if ($('#title').val() != old) {
                old = $('#title').val();
                f();
            }
        };
    })());
    $('#titleMultiline').on('keyup',  (function() {
        var old = $('#titleMultiline').val(), f=tcm_change('title', 'textMultiline');
        return function() {
            if ($('#titleMultiline').val() != old) {
                old = $('#titleMultiline').val();
                f();
            }
        };
    })());
    
    $('#titleColor').spectrum({showInput:true, showInitial:true, allowEmpty:true, preferredFormat: "hex",
        chooseText: "<?=TCM_RENDERER_COLOR_CHOOSE?>",cancelText: "<?=TCM_RENDERER_COLOR_CANCEL?>"<?=((!$current_id) && $product_id)?', appendTo: "#myModal"':''?>});
    $('#titleFontFace').on('change', tcm_change('title', 'font'));
    $('#titleFontSize').on('change', tcm_change('title', 'size'));
    $('#titleColor').on('change', tcm_change('title', 'color'));
    $('#titleBold').on('change', tcm_change('title', 'bold'));
    $('#titleItalic').on('change', tcm_change('title', 'italic'));
    
    /* Comentarios */
    $('#completeComments').on('change', tcm_change('comment'));
    $('#completeComments').on('keyup',  (function() {
        var old = $('#completeComments').val(), f=tcm_change('comment');
        return function() {
            if ($('#completeComments').val() != old) {
                old = $('#completeComments').val();
                f();
            }
        };
    })());

    function configureDialog(response, showlast) {
        $(".wizard-inner li a").parent('li').addClass('hide').removeClass('tcm_visible');
        var showTabs = [], availables, idx, options, first = false, font;

        _update = false;
        tcm_updating = true;
        if (response.hasOwnProperty("borderSolid") && response.borderSolid.enabled) {
            showHide('#borderSolidSelect', true);
            showTabs.push('step3');
            borderSolidIcons = [];
            borderSolidIcons.push({'iconFilePath':'<?=(((!$current_id) && $product_id)?'':'../').DIR_WS_IMAGES.'tcm/none.png'?>', 'iconValue':null, 'width':0, 'style':'solid', 'color':''});
            if (response.borderSolid.hasOwnProperty('availables')) {
                availables = response.borderSolid.availables;
                for(idx in availables) {
                    borderSolidIcons.push({'iconFilePath':'<?= (((!$current_id) && $product_id)?'':'../').DIR_WS_IMAGES?>'+availables[idx].img.substr(<?=strlen(DIR_WS_IMAGES)?>),'iconValue':idx,'width':availables[idx].width, 'style': availables[idx].style, 'color': availables[idx].color});
                }
            }
            borderSolidSelect.refresh(borderSolidIcons);
        } else {
            showHide('#borderSolidSelect', false);
        }

        if (response.hasOwnProperty('borderSolidCustom') && response.borderSolidCustom.enabled) {
            showTabs.push('step3');
            showHide('#borderSolidCustomWidth', true);
            showHide('#borderSolidCustomColor', true);
            showHide('#borderSolidCustomStyle', true);
        } else {
            showHide('#borderSolidCustomWidth', false);
            showHide('#borderSolidCustomColor', false);
            showHide('#borderSolidCustomStyle', false);
        }

        if (response.hasOwnProperty('fillSolid') && response.fillSolid.enabled) {
            showHide('#fillSolidSelect', true);
            showTabs.push('step4');
            fillSolidIcons = [];
            fillSolidIcons.push({'iconFilePath':'<?=(((!$current_id) && $product_id)?'':'../').DIR_WS_IMAGES.'tcm/none.png'?>', 'iconValue':null, 'width':0, 'style':'solid', 'color':''});
            if (response.fillSolid.hasOwnProperty('availables')) {
                availables = response.fillSolid.availables;
                for(idx in availables) {
                    fillSolidIcons.push({'iconFilePath':'<?= (((!$current_id) && $product_id)?'':'../').DIR_WS_IMAGES?>'+availables[idx].img.substr(<?=strlen(DIR_WS_IMAGES)?>),'iconValue':idx,'width':availables[idx].width, 'style': availables[idx].style, 'color': availables[idx].color});
                }
            }
            fillSolidSelect.refresh(fillSolidIcons);
        } else {
            showHide('#fillSolidSelect', false);
        }
        
        if (response.hasOwnProperty('fillSolidCustom') && response.borderSolidCustom.enabled) {
            showTabs.push('step4');
            showHide('#fillSolidCustomColor', true);
        } else {
            showHide('#fillSolidCustomColor', false);
        }

        if (response.hasOwnProperty('fillTextured') && response.fillTextured.enabled) {
            showHide('#fillTexturedSelect', true);
            showTabs.push('step4');
            fillTexturedIcons = [];
            fillTexturedIcons.push({'iconFilePath':'<?=(((!$current_id) && $product_id)?'':'../').DIR_WS_IMAGES.'tcm/none.png'?>', 'iconValue':null, 'width':0, 'style':'solid', 'color':''});
            if (response.fillTextured.hasOwnProperty('availables')) {
                availables = response.fillTextured.availables;
                for(idx in availables) {
                    fillTexturedIcons.push({'iconFilePath':'<?= (((!$current_id) && $product_id)?'':'../').DIR_WS_IMAGES?>'+availables[idx].img.substr(<?=strlen(DIR_WS_IMAGES)?>),'iconValue':idx,'width':availables[idx].width, 'style': availables[idx].style, 'color': availables[idx].color});
                }
            }
            fillTexturedSelect.refresh(fillTexturedIcons);
        } else {
            showHide('#fillTexturedSelect', false);
        }

        if (response.hasOwnProperty('fillTexturedCustom') && response.fillTexturedCustom.enabled) {
            showHide('#fillTexturedCustom', true);
            $('#fillTexturedPreview').attr('src', '<?=(((!$current_id) && $product_id)?'':'../').DIR_WS_IMAGES.'tcm/none.png'?>');
            showTabs.push('step4');
        } else {
            showHide('#fillTexturedCustom', false);
        }

        if (response.hasOwnProperty('icons') && response.icons.enabled) {
            showHide('#iconSelect', true);
            showTabs.push('step5');
            iconSelectIcons = [];
            iconSelectIcons.push({'iconFilePath':'<?=(((!$current_id) && $product_id)?'':'../').DIR_WS_IMAGES.'tcm/none.png'?>', 'iconValue':null, 'width':0, 'style':'solid', 'color':''});
            if (response.icons.hasOwnProperty('availables')) {
                availables = response.icons.availables;
                for(idx in availables) {
                    iconSelectIcons.push({'iconFilePath':'<?= (((!$current_id) && $product_id)?'':'../').DIR_WS_IMAGES?>'+availables[idx].img.substr(<?=strlen(DIR_WS_IMAGES)?>),'iconValue':idx,'width':availables[idx].width, 'style': availables[idx].style, 'color': availables[idx].color});
                }
            }
            iconSelect.refresh(iconSelectIcons);
        } else {
            showHide('#iconSelect', false);
        }

        if (response.hasOwnProperty('iconsCustom') && response.iconsCustom.enabled) {
            showHide('#iconCustom', true);
            $('#iconPreview').attr('src','<?=(((!$current_id) && $product_id)?'':'../').DIR_WS_IMAGES.'tcm/none.png'?>');
            showTabs.push('step5');
        } else {
            showHide('#iconCustom', false);
        }

        if ((response.hasOwnProperty('iconsCustom') && response.iconsCustom.enabled) || (response.hasOwnProperty('icons') && response.icons.enabled)) {
            showHide('#iconColor', true);
            showHide('#iconFill', true);
            showHide('#iconWidth', true);
            showHide('#iconStyle', true);
        } else {
            showHide('#iconColor', false);
            showHide('#iconFill', false);
            showHide('#iconWidth', false);
            showHide('#iconStyle', false);
        }

        if (response.hasOwnProperty('text') && response.text.enabled) {
            showTabs.push('step2');
            showHide('#text', true);
            if (response.text.multiline) {
                $('#text').parents('label:first').hide();
                $('#textMultiline').parents('label:first').show();
            } else {
                $('#text').parents('label:first').show();
                $('#textMultiline').parents('label:first').hide();
            }
            if (response.text.fonts) {
                showHide('#fontFace', true);
                $('#fontFace').empty();
                options = '';
                if (response.text.hasOwnProperty('availables')) {
                    availables = response.text.availables;
                    first = true;
                    for(idx in availables) {
                        font = (availables[idx].basename.indexOf('.') !== -1) ? availables[idx].basename.substr(0,availables[idx].basename.indexOf('.')) : availables[idx].basename;
                        options += '<option value="'+idx+'" style="width:120px;height:25px;background: url(\'<?=(((!$current_id) && $product_id)?'':'../').DIR_WS_IMAGES.'tcm/'?>'+font+'-Regular.png\') no-repeat;" data-basename="'+font+'" label="'+availables[idx].name+'"'+(first?' selected="selected"':'')+'></option>';
                        if (first) {
                            $('#fontFace').css('background-image', "url('<?=(((!$current_id) && $product_id)?'':'../').DIR_WS_IMAGES.'tcm/'?>"+font+"-Regular.png') no-repeat");
                        }
                        first = false;
                    }
                }
                $('#fontFace').append(options);
            } else {
                showHide('#fontFace', false);
            }
            showHide('#fontSize', response.text.size === true);
            showHide('#textColor', response.text.color === true);
            showHide('#textBold', response.text.bold === true);
            showHide('#textItalic', response.text.italic === true);
            if (response.text.multilegend) {
                $('#textInputLegend').parents('label:first').parent().show();
                tcm_legendCount = response.text.count;
                tcm_legendTotal = response.text.count;
                $('#legend_count').text(tcm_legendCount);
            } else {
                $('#textInputLegend').parents('label:first').parent().hide();
                tcm_legendCount = 0;
                tcm_legendTotal = 0;
                $('#legend_count').text(tcm_legendCount);
            }
        } else {
            showHide('#text', false);
            showHide('#textMultiline', false);
            showHide('#fontFace', false);
            showHide('#fontSize', false);
            showHide('#textColor', false);
            showHide('#textBold', false);
            showHide('#textItalic', false);
            $('#textInputLegend').parents('label:first').parent().hide();
        }

        if (response.hasOwnProperty('title') && response.title.enabled) {
            showTabs.push('step1');
            showHide('#title', true);
            if (response.title.multiline) {
                $('#title').parents('label:first').hide();
                $('#titleMultiline').parents('label:first').show();
            } else {
                $('#title').parents('label:first').show();
                $('#titleMultiline').parents('label:first').hide();
            }
            if (response.title.fonts) {
                showHide('#titleFontFace', true);
                $('#titleFontFace').empty();
                options = '';
                if (response.title.hasOwnProperty('availables')) {
                    availables = response.title.availables;
                    first = true;
                    for(idx in availables) {
                        font = (availables[idx].basename.indexOf('.') !== -1) ? availables[idx].basename.substr(0,availables[idx].basename.indexOf('.')) : availables[idx].basename;
                        options += '<option value="'+idx+'" style="width:120px;height:25px;background: url(\'<?=(((!$current_id) && $product_id)?'':'../').DIR_WS_IMAGES.'tcm/'?>'+font+'-Regular.png\') no-repeat;" data-basename="'+font+'" label="'+availables[idx].name+'"'+(first?' selected="selected"':'')+'></option>';
                        if (first) {
                            $('#fontFace').css('background-image', "url('<?=(((!$current_id) && $product_id)?'':'../').DIR_WS_IMAGES.'tcm/'?>"+font+"-Regular.png') no-repeat");
                        }
                        first = false;
                    }
                }
                $('#titleFontFace').append(options);
            } else {
                showHide('#titleFontFace', false);
            }
            
            showHide('#titleFontSize', response.title.size === true);
            showHide('#titleColor', response.title.color === true);
            showHide('#titleBold', response.title.bold === true);
            showHide('#titleItalic', response.title.italic === true);

        } else {
            showHide('#title', false);
            showHide('#titleMultiline', false);
            showHide('#titleFontFace', false);
            showHide('#titleSize', false);
            showHide('#titleColor', false);
            showHide('#titleBold', false);
            showHide('#titleItalic', false);
        }

        if (!_isCombo || showlast) {
            showTabs.push('complete');
        }
        
        if (showlast) {
            $('#img_preview').parent().parent().hide();
        } else {
            $('#img_preview').parent().parent().show();
        }

        for(var i in showTabs) {
            tabShowHide(showTabs[i], true);
        }
        if (response.hasOwnProperty('text') && response.text.enabled) {
        } else {
            showHide('#iconCustom', false);
        }
        $('.wizard .nav-tabs li.tcm_visible:first').find('a[data-toggle="tab"]')[0].click();
        
        //Busco de la ultima solapa visible y quito el "next"
        $('.tab-pane button.next-step').show();
        var a = $('ul.nav-tabs li.tcm_visible:last a');
        $(a.attr('href')+' button.next-step').hide();
        //Ahora agrego el "Finish"
        $('button.finish-step').hide();
        $(a.attr('href')+' button.finish-step').show();
        
        tcm_updating = false;
        _update = true;
        lastChange = '';
        refreshFromServer();
    }

    window.tcm_configureDialog = configureDialog;

    function getCreatorSettings(id) {
        var params = {action:'creatorSettings'};
        params[(_isCreator ? "creator":"product")]=_id;
        $.post('<?=tep_href_link(FILENAME_TAG_RENDERER)?>', params,
               function (d) { return configureDialog(d);}
              ).fail(function(){alert('Error al configurar la herramienta de diseño de etiquetas');});
    }

    function changeItem(id) {
        _id = id;                    

        getCreatorSettings(_id);
        $('#fillTexturedPreview').next().attr('name', _isCreator?'creator':'product').val(_id);
        $('#iconPreview').next().attr('name', _isCreator?'creator':'product').val(_id);
    }

    function isArray(val) {
        return Object.prototype.toString.call(val) === '[object Array]';
    }

    function setValue(type, attribute, value) {
        if (_update) {
            if (typeof sendParams[type] === 'undefined') {
                sendParams[type]={};
            }
            if (isArray(value) && (value.length===0)) {
                value='';
            }
            sendParams[type][attribute]=value;
        }
    }
    
    function _sendValues() {
        //Ajax
        if (sendParams != {}) {
            var params = {action:'set', data:sendParams};
            params[(_isCreator ? "creator":"product")]=_id;
            if (_isCombo) {
                params['combo']=_comboId;
            }
            window.tcm_params = params;
            $.post('<?=tep_href_link(FILENAME_TAG_RENDERER)?>', $.param(params), 
                function(response) {
                    if (response[0]==="OK") {
                        lastChange = response[1].lastChange;
                        $jq12('#img_preview').attr('src', '<?=tep_href_link(FILENAME_TAG_RENDERER,'action=preview').'&rand='?>'+lastChange+'&'+(_isCreator ? "creator=":"product=")+_id+(_isCombo?"&combo="+_comboId:''));
                    } else {
                        console.log("Error: ",response);
                    }
                });
            sendParams = {};
            _programmed = false;
        } else {
            console.log('sendValues() => No hay parámetros para actualizar');
        }
    }
    
    function sendValues() {
        if (!_programmed && _update) {
            _programmed = true;
            setTimeout(_sendValues, 1000);
        }
    }
    
    
    window.tcm_loadPreviewOn = function(img) {
        $jq12(img).attr('src', '<?=tep_href_link(FILENAME_TAG_RENDERER,'action=preview').'&rand='?>'+lastChange+'&'+(_isCreator ? "creator=":"product=")+_id+(_isCombo?"&combo="+_comboId:''));
    };
    
    //TODO quitar esto para eliminar globales
    window.tcm_setValue = setValue;
    window.tcm_sendValues = sendValues;
    window.tcm_changeItem = changeItem;
    
    var tcm_updating = false;
    function tcm_change(tab, field) {
        return function() {
            //console.log(tab, field, tcm_updating);
            if (!tcm_updating) {
                tcm_updating = true;
                var class1, class2;
                switch(tab) {
                    case 'border':
                        switch(field) {
                            case 'solid':
                                setValue('border', 'type', 'solid');
                                setValue('border', 'borders_solid_id', borderSolidSelect.getSelectedValue());
                                sendValues();
                                var selected = borderSolidIcons[borderSolidSelect.getSelectedIndex()];
                                if ($("#borderSolidCustomColor").length>0) {
                                    $("#borderSolidCustomColor").spectrum('set', selected.color);
                                    $jq12('#borderSolidCustomWidth').val(selected.width);
                                    $jq12('#borderSolidCustomStyle').val(selected.style);
                                }
                                class1='active-control';
                                class2='';
                                break;
                            case 'width':
                            case 'color':
                            case 'style':
                                setValue('border', 'type', 'custom');
                                setValue('border', 'color', $("#borderSolidCustomColor").spectrum("get")?$("#borderSolidCustomColor").spectrum("get").toHexString():null);
                                setValue('border', 'width', $jq12('#borderSolidCustomWidth').val());
                                setValue('border', 'style', $jq12('#borderSolidCustomStyle').val());
                                sendValues();
                                if ($('#borderSolidSelect').length>0)
                                    borderSolidSelect.setSelectedIndex(0);
                                class1='';
                                class2='active-control';
                                break;
                        }
                        $('#borderSolidSelect').parent('label').removeClass(class2).addClass(class1);
                        $('#borderSolidCustomWidth').parents('label').removeClass(class1).addClass(class2);
                        $('#borderSolidCustomColor').parent('label').removeClass(class1).addClass(class2);
                        $('#borderSolidCustomStyle').parent('label').removeClass(class1).addClass(class2);
                        break;
                    case 'fill':
                        $('#fillSolidSelect').parents('label').removeClass('active-control');
                        $('#fillSolidCustomColor').parents('label').removeClass('active-control');
                        $('#fillTexturedSelect').parents('label').removeClass('active-control');
                        $('#fillTexturedCustom').parents('label').removeClass('active-control');
                        switch(field) {
                            case 'solid':
                                setValue('fill', 'type', 'solid');
                                setValue('fill', 'fills_solid_id', fillSolidSelect.getSelectedValue());
                                sendValues();
                                if ($("#fillSolidCustomColor").length>0) {
                                    var selected = fillSolidIcons[fillSolidSelect.getSelectedIndex()];
                                    $("#fillSolidCustomColor").spectrum('set', selected.color);
                                }
                                if ($('#fillTexturedSelect').length>0)
                                    fillTexturedSelect.setSelectedIndex(0);
                                $('#fillSolidSelect').parents('label').addClass('active-control');
                                break;
                            case 'color':
                                setValue('fill', 'type', 'solidCustom');
                                setValue('fill', 'color', ($("#fillSolidCustomColor").spectrum("get"))?$("#fillSolidCustomColor").spectrum("get").toHexString():null);
                                sendValues();
                                if ($('#fillSolidSelect').length>0)
                                    fillSolidSelect.setSelectedIndex(0);
                                if ($('#fillTexturedSelect').length>0)
                                    fillTexturedSelect.setSelectedIndex(0);
                                $('#fillSolidCustomColor').parents('label').addClass('active-control');
                                break;
                            case 'textured':
                                setValue('fill', 'type', 'textured');
                                setValue('fill', 'fills_textured_id', fillTexturedSelect.getSelectedValue());
                                sendValues();
                                if ($("#fillSolidCustomColor").length>0) {
                                    var selected = fillSolidIcons[fillSolidSelect.getSelectedIndex()];
                                    $("#fillSolidCustomColor").spectrum('set', null);
                                }
                                if ($('#fillSolidSelect').length>0)
                                    fillSolidSelect.setSelectedIndex(0);
                                $('#fillTexturedSelect').parents('label').addClass('active-control');
                                break;
                            case 'custom':
                                setValue('fill', 'type', 'texturedCustom');
                                sendValues();
                                if ($('#fillSolidSelect').length>0)
                                    fillSolidSelect.setSelectedIndex(0);
                                if ($("#fillSolidCustomColor").length>0) {
                                    var selected = fillSolidIcons[fillSolidSelect.getSelectedIndex()];
                                    $("#fillSolidCustomColor").spectrum('set', null);
                                }
                                if ($('#fillTexturedSelect').length>0)
                                    fillTexturedSelect.setSelectedIndex(0);
                                $('#fillTexturedCustom').parents('label').addClass('active-control');
                                break;
                        }
                        break;
                    case 'icon':
                        switch(field) {
                            case 'icon':
                                setValue('icon', 'type', 'icon');
                                setValue('icon', 'icons_id', iconSelect.getSelectedValue());
                                setValue('icon', 'fill',$('#iconFill').spectrum('get')?$('#iconFill').spectrum('get').toHexString():null);
                                setValue('icon', 'color',$('#iconColor').spectrum('get')?$('#iconColor').spectrum('get').toHexString():null);
                                setValue('icon', 'style',$('#iconStyle').val());
                                setValue('icon', 'width',$('#iconWidth').val());
                                sendValues();
                                $('#iconSelect').parents('label').addClass('active-control');
                                $('#iconCustom').parents('label').removeClass('active-control');
                                break;
                            case 'fill':
                            case 'color':
                            case 'width':
                            case 'style':
                                setValue('icon', 'fill',$('#iconFill').spectrum('get')?$('#iconFill').spectrum('get').toHexString():null);
                                setValue('icon', 'color',$('#iconColor').spectrum('get')?$('#iconColor').spectrum('get').toHexString():null);
                                setValue('icon', 'style',$('#iconStyle').val());
                                setValue('icon', 'width',$('#iconWidth').val());
                                sendValues();
                                $('#iconCustom').parents('label').addClass('active-control');
                                $('#iconSelect').parents('label').removeClass('active-control');
                                break;
                            case 'custom':
                                setValue('icon', 'type', 'custom');
                                setValue('icon', 'fill',$('#iconFill').spectrum('get')?$('#iconFill').spectrum('get').toHexString():null);
                                setValue('icon', 'color',$('#iconColor').spectrum('get')?$('#iconColor').spectrum('get').toHexString():null);
                                setValue('icon', 'style',$('#iconStyle').val());
                                setValue('icon', 'width',$('#iconWidth').val());
                                sendValues();
                                $('#iconSelect').parents('label').removeClass('active-control');
                                $('#iconCustom').parents('label').addClass('active-control');
                                break;
                        }
                        break;
                    case 'text':
                        switch(field) {
                            case 'text':
                                setValue('text', 'text', $('#text').val());
                                sendValues();
                                break;
                            case 'textMultiline':
                                setValue('text', 'text', $('#textMultiline').val());
                                sendValues();
                                break;
                            case 'font':
                                if (!!!window.chrome) {
                                    $('#fontFace').css('background-image', 'url("<?=(((!$current_id) && $product_id)?'':'../').DIR_WS_IMAGES?>tcm/'+$('#fontFace option:selected').attr('data-basename')+'-Regular.png")');
                                } else {
                                    $('#fontFace').css('background-image', 'none');
                                }
                                setValue('text', 'fonts_id', $('#fontFace').val());
                                sendValues();
                                break;
                            case 'size':
                                setValue('text', 'size', $('#fontSize').val());
                                sendValues();
                                break;
                            case 'color':
                                setValue('text', 'color', $('#textColor').spectrum('get')?$('#textColor').spectrum('get').toHexString():null);
                                sendValues();
                                break;
                            case 'bold':
                                setValue('text', 'bold', $('#textBold').attr('checked')==='checked');
                                sendValues();
                                break;
                            case 'italic':
                                setValue('text', 'italic', $('#textItalic').attr('checked')==='checked');
                                sendValues();
                                break;
                            case 'legends':
                                var texts = [];
                                $('#textLegends').children('span').each(function(i,e) {texts.push($(e).text().trim());});
                                setValue('text', 'legends', texts);
                                sendValues();
                                break;
                        }
                        break;
                    case 'title':
                        switch(field) {
                            case 'text':
                                setValue('title', 'text', $('#title').val());
                                sendValues();
                                break;
                            case 'textMultiline':
                                setValue('title', 'text', $('#titleMultiline').val());
                                sendValues();
                                break;
                            case 'font':
                                if (!!!window.chrome) {
                                    $('#titleFontFace').css('background-image', 'url("<?=(((!$current_id) && $product_id)?'':'../').DIR_WS_IMAGES?>tcm/'+$('#titleFontFace option:selected').attr('data-basename')+'-Regular.png")');
                                } else {
                                    $('#titleFontFace').css('background-image', 'none');
                                }
                                setValue('title', 'fonts_id', $('#titleFontFace').val());
                                sendValues();
                                break;
                            case 'size':
                                setValue('title', 'size', $('#titleFontSize').val());
                                sendValues();
                                break;
                            case 'color':
                                setValue('title', 'color', $('#titleColor').spectrum('get')?$('#titleColor').spectrum('get').toHexString():null);
                                sendValues();
                                break;
                            case 'bold':
                                setValue('title', 'bold', $('#titleBold').attr('checked')==='checked');
                                sendValues();
                                break;
                            case 'italic':
                                setValue('title', 'italic', $('#titleItalic').attr('checked')==='checked');
                                sendValues();
                                break;
                        }
                        break;
                    case 'comment':
                        setValue('comment','comment', $('#completeComments').val());
                        sendValues();
                        break;
                    default:
                        console.log('tcm_change error: ', tab, field, value);
                }
                tcm_updating = false;
            }
        };
    }


    //Inicialización
    function refreshFromServer() {
        _update = false;
        var params = {action:'get'};
        params[(_isCreator ? "creator":"product")]=_id;
        if (_isCombo) {
            params['combo']=_comboId;
        }
        
        $.post('<?=tep_href_link(FILENAME_TAG_RENDERER)?>', params,
            function(response) {
                console.log('get', response);
                if (response[0]==="OK") {
                    var data=response[1];
                    _update = false;
                    tcm_updating = true;

                    //General
                    if (lastChange != data['lastChange']) {
                        $jq12('#img_preview').attr('src', '<?=tep_href_link(FILENAME_TAG_RENDERER,'action=preview').'&rand='?>'+response[1].lastChange+'&'+(_isCreator ? "creator=":"product=")+_id+(_isCombo?'&combo='+_comboId:''));
                        lastChange = data['lastChange'];
                    }

                    //Bordes
                    if (typeof data['border'] === 'object') {
                        if (data['border']['type']==='solid') {
                            for(var bs in borderSolidIcons) {
                                if (borderSolidIcons[bs]['iconValue']==parseInt(data['border']['borders_solid_id'])) {
                                    tcm_updating = false;
                                    borderSolidSelect.setSelectedIndex(bs);
                                    tcm_updating = true;
                                    if ($('#borderSolidCustomWidth').length>0) {
                                        $('#borderSolidCustomWidth').val(borderSolidIcons[bs]['width']);
                                        $('#borderSolidCustomStyle').val(borderSolidIcons[bs]['style']);
                                        $('#borderSolidCustomColor').spectrum('set', borderSolidIcons[bs]['color']);
                                    }
                                    break;
                                }
                            }
                        } else {
                            tcm_updating = false;
                            $('#borderSolidCustomWidth').val(data['border']['width']);
                            tcm_updating = true;
                            $('#borderSolidCustomStyle').val(data['border']['style']);
                            $('#borderSolidCustomColor').spectrum('set', data['border']['color']);
                            if ($('#borderSolidSelect').length>0)
                                borderSolidSelect.setSelectedIndex(0);
                        }
                    }

                    //Rellenos
                    if (typeof data['fill'] === 'object') {
                        switch(data['fill']['type']) {
                            case 'solid':
                                for(var fs in fillSolidIcons) {
                                    if (fillSolidIcons[fs]['iconValue']==parseInt(data['fill']['fills_solid_id'])) {
                                        tcm_updating = false;
                                        fillSolidSelect.setSelectedIndex(fs);
                                        tcm_updating = true;
                                        if ($('#fillSolidCustomColor').length>0)
                                            $('#fillSolidCustomColor').spectrum('set', fillSolidIcons[fs]['color']);
                                        break;
                                    }
                                }
                                if ($('#fillTexturedSelect').length>0)
                                    fillTexturedSelect.setSelectedIndex(0);
                                break;
                            case 'solidCustom':
                                tcm_updating = false;
                                $('#fillSolidCustomColor').spectrum('set', data['fill']['color']);
                                tcm_updating = true;
                                if ($('#fillSolidSelect').length>0)
                                    fillSolidSelect.setSelectedIndex(0);
                                if ($('#fillTexturedSelect').length>0)
                                    fillTexturedSelect.setSelectedIndex(0);
                                break;
                            case 'textured':
                                for(var tf in fillTexturedIcons) {
                                    if (fillTexturedIcons[tf]['iconValue'] == parseInt(data['fill']['fills_textured_id'])) {
                                        tcm_updating = false;
                                        fillTexturedSelect.setSelectedIndex(tf);
                                        tcm_updating = true;
                                        break;
                                    }
                                }
                                if ($('#fillSolidSelect').length>0)
                                    fillSolidSelect.setSelectedIndex(0);
                                if ($('#fillSolidCustomColor').length>0)
                                    $('#fillSolidCustomColor').spectrum('set', null);
                                break;
                            case 'texturedCustom':
                                if ($('#fillSolidSelect').length>0)
                                    fillSolidSelect.setSelectedIndex(0);
                                if ($('#fillTexturedSelect').length>0)
                                    fillTexturedSelect.setSelectedIndex(0);
                                if ($('#fillSolidCustomColor').length>0)
                                    $('#fillSolidCustomColor').spectrum('set', null);
                                if (data['fill']['file']) {
                                    tcm_updating = false;
                                    $('#fillTexturedPreview').attr('src', '<?=(((!$current_id) && $product_id)?'':'../').DIR_WS_IMAGES.'tcm/files/'?>'+data['fill']['file']);
                                    tcm_updating = true;
                                } else {
                                    $('#fillTexturedPreview').attr('src', '<?=(((!$current_id) && $product_id)?'':'../').DIR_WS_IMAGES.'tcm/none.png'?>');
                                }
                                break;
                        }
                    }

                    //Iconos
                    if (typeof data['icon'] === 'object') {
                        switch(data['icon']['type']) {
                            case 'icon':
                                for(var is in iconSelectIcons) {
                                    if (iconSelectIcons[is]['iconValue']==parseInt(data['icon']['icons_id'])) {
                                        tcm_updating = false;
                                        iconSelect.setSelectedIndex(is);
                                        tcm_updating = true;
                                        break;
                                    }
                                }
                                break;
                            case 'custom':
                                if (data['icon']['preview']) {
                                    $('#iconPreview').attr('src','<?=(((!$current_id) && $product_id)?'':'../').DIR_WS_IMAGES.'tcm/files/'?>'+data['icon']['preview']);
                                } else {
                                    $('#iconPreview').attr('src','<?=(((!$current_id) && $product_id)?'':'../').DIR_WS_IMAGES.'tcm/none.png'?>');
                                }
                                tcm_updating = false;
                                tcm_change('icon', 'custom')();
                                tcm_updating = true;
                                break;
                        }
                        $('#iconFill').spectrum('set', data['icon']['fill']);
                        $('#iconColor').spectrum('set', data['icon']['color']);
                        $('#iconStyle').val(data['icon']['style']);
                        $('#iconWidth').val(data['icon']['width']);

                    }

                    //Texto
                    if (typeof data['text'] === 'object') {
                        $('#text').val(data['text']['text']);
                        $('#textMultiline').val(data['text']['text']);
                        if ($('#fontFace').length > 0) {
                            $('#fontFace').val(data['text']['fonts_id']);
                            if ($('#fontFace')[0].nodeName.toLowerCase() === 'select') {
                                if (!!!window.chrome) {
                                    $('#fontFace').css('background-image', 'url("<?=(((!$current_id) && $product_id)?'':'../').DIR_WS_IMAGES?>tcm/'+$('#fontFace option[value='+data['text']['fonts_id']+']').attr('data-basename')+'-Regular.png")');
                                } else {
                                    $('#fontFace').css('background-image', 'none');
                                }
                            }
                        }
                        if ($('#fontSize').length>0)
                            $('#fontSize').val(data['text']['size']);
                        if ($('#textColor').length>0)
                            $('#textColor').spectrum('set', data['text']['color']);
                        if (($('#textBold').length>0) && (data['text']['bold']==='true')) {
                            $('#textBold').attr('checked', 'checked');
                        }
                        if (($('#textItalic').length>0) && (data['text']['italic']==='true')) {
                            $('#textItalic').attr('checked', 'checked');
                        }
                        if (($('#textLegends').length>0) && (data['text']['legends'] !== '')) {
                            var legends = (data['text']['legends']?data['text']['legends']:[]);
                            $('#textLegends').empty();
                            for(var i in legends) {
                                $('#textLegends').append(' <span class="label label-primary" style="cursor: pointer">'+legends[i]+' <span aria-label="<?=TCM_RENDERER_TEXT_LEGENDS_DELETE?>" class="glyphicon glyphicon-remove" style="cursor: pointer"></span></span>');
                                var last = $('#textLegends').children('span').last();
                                last.on('dblclick', function() {tcm_clickLegend(this, true);});
                                last.find('span').on('click', function() {tcm_clickLegend(this, false);});
                            }
                            tcm_legendCount = tcm_legendCount -legends.length;
                            $('#legend_count').text(tcm_legendCount);
                        }
                    }

                    //Título
                    if (typeof data['title'] === 'object') {
                        $('#title').val(data['title']['text']);
                        $('#titleMultiline').val(data['title']['text']);
                        if ($('#titleFontFace').length > 0) {
                            $('#titleFontFace').val(data['title']['fonts_id']);
                            if ($('#titleFontFace')[0].nodeName.toLowerCase() === 'select') {
                                if (!!!window.chrome) {
                                    $('#titleFontFace').css('background-image', 'url("<?=(((!$current_id) && $product_id)?'':'../').DIR_WS_IMAGES?>tcm/'+$('#titleFontFace option[value='+data['title']['fonts_id']+']').attr('data-basename')+'-Regular.png")');
                                } else {
                                    $('#titleFontFace').css('background-image', 'none');
                                }
                            }
                        }
                        if ($('#titleFontSize').length>0)
                            $('#titleFontSize').val(data['title']['size']);
                        if ($('#titleColor').length>0)
                            $('#titleColor').spectrum('set', data['title']['color']);
                        if (($('#titleBold').length>0) && (data['title']['bold']==='true')) {
                            $('#titleBold').attr('checked', 'checked');
                        }
                        if (($('#titleItalic').length>0) && (data['title']['italic']==='true')) {
                            $('#titleItalic').attr('checked', 'checked');
                        }
                    }
                    //Comentarios de compra
                    if (typeof data['comment'] != 'undefined')
                        $('#completeComments').val(data['comment']['comment']);
                }
                tcm_updating = false;
                _update = true;
            }
        );
    }
   
   function resize() {
        var container = $jq12('#creatorContainer'),
            parent = container.parent('div'),
            size = parent.innerWidth();
            container.removeClass('simulate-xs', 'simulate-sm', 'simulate-md');
        switch(true) {
            case (size < 768):
                //xs
                container.addClass('simulate-xs').css('width', size);
                break;
            case (size < 992):
                //sm
                container.addClass('simulate-sm').css('width', size);
                break;
            case (size < 1200):
                //md
                container.addClass('simulate-md').css('width', size);
                break;
            default:
                //lg
                container.css('width', size);
        }
   }
   //resize();
   //$(window).resize(resize);
   if (!_isCombo) {
        changeItem(_id);
    }

})();

</script>

<?php
noCreatorSelected:  
?>