package com.boxico.android.kn.funforlabelapp.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;

import com.boxico.android.kn.funforlabelapp.dtos.Creator;
import com.boxico.android.kn.funforlabelapp.dtos.LabelAttributes;
import com.boxico.android.kn.funforlabelapp.dtos.LabelFont;
import com.boxico.android.kn.funforlabelapp.dtos.LabelImage;

public class TagParams {

    private LabelImage image;
    private String fontSizeText = "8.0";
    private Typeface fontText;
    private String fontSizeTitle = "8.0";
    private Typeface fontTitle;
    private int colorText = Color.BLACK;
    private int colorTitle = Color.BLACK;
    private String text;
    private String title;
    private boolean isTitle = false;
    private long idProduct = -1;
    private boolean inicializadoSize = false;
    private boolean inicializadoFont = false;


    private int posSizeText;
    private int posSizeTitle;
    private int posFontText;
    private int posFontTitle;
    private int posColorText;
    private int posColorTitle;
    private int posImage;
    private Creator creator;
    private LabelAttributes[] labelAttributes;
    private LabelFont[] fonts;
    private LabelImage[] images;
    private String fontTitleBaseName;
    private String fontTextBaseName;
    private int fontTitleId = 3;
    private int fontTextId = 3;
    private String backgroundFilename;

    public boolean isInicializadoSize() {
        return inicializadoSize;
    }

    public void setInicializadoSize(boolean inicializadoSize) {
        this.inicializadoSize = inicializadoSize;
    }

    public boolean isInicializadoFont() {
        return inicializadoFont;
    }

    public void setInicializadoFont(boolean inicializadoFont) {
        this.inicializadoFont = inicializadoFont;
    }

    public String getBackgroundFilename() {
        return backgroundFilename;
    }

    public void setBackgroundFilename(String backgroundFilename) {
        this.backgroundFilename = backgroundFilename;
    }

    public int getFontTitleId() {
        return fontTitleId;
    }

    public void setFontTitleId(int fontTitleId) {
        this.fontTitleId = fontTitleId;
    }

    public int getFontTextId() {
        return fontTextId;
    }

    public void setFontTextId(int fontTextId) {
        this.fontTextId = fontTextId;
    }

    public String getFontTitleBaseName() {
        return fontTitleBaseName;
    }

    public void setFontTitleBaseName(String fontTitleBaseName) {
        this.fontTitleBaseName = fontTitleBaseName;
    }

    public String getFontTextBaseName() {
        return fontTextBaseName;
    }

    public void setFontTextBaseName(String fontTextBaseName) {
        this.fontTextBaseName = fontTextBaseName;
    }

    public LabelImage[] getImages() {
        return images;
    }

    public void setImages(LabelImage[] images) {
        this.images = images;
    }

    public LabelFont[] getFonts() {
        return fonts;
    }

    public void setFonts(LabelFont[] fonts) {
        this.fonts = fonts;
    }

    public LabelAttributes[] getLabelAttributes() {
        return labelAttributes;
    }

    public void setLabelAttributes(LabelAttributes[] labelAttributes) {
        this.labelAttributes = labelAttributes;
    }

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    public int getPosSizeText() {
        return posSizeText;
    }

    public void setPosSizeText(int posSizeText) {
        this.posSizeText = posSizeText;
    }

    public int getPosSizeTitle() {
        return posSizeTitle;
    }

    public void setPosSizeTitle(int posSizeTitle) {
        this.posSizeTitle = posSizeTitle;
    }

    public int getPosFontText() {
        return posFontText;
    }

    public void setPosFontText(int posFontText) {
        this.posFontText = posFontText;
    }

    public int getPosFontTitle() {
        return posFontTitle;
    }

    public void setPosFontTitle(int posFontTitle) {
        this.posFontTitle = posFontTitle;
    }

    public int getPosColorText() {
        return posColorText;
    }

    public void setPosColorText(int posColorText) {
        this.posColorText = posColorText;
    }

    public int getPosColorTitle() {
        return posColorTitle;
    }

    public void setPosColorTitle(int posColorTitle) {
        this.posColorTitle = posColorTitle;
    }

    public int getPosImage() {
        return posImage;
    }

    public void setPosImage(int posImage) {
        this.posImage = posImage;
    }

    public Typeface getFontText() {
        return fontText;
    }

    public void setFontText(Typeface fontText) {
        this.fontText = fontText;
    }

    public Typeface getFontTitle() {
        return fontTitle;
    }

    public void setFontTitle(Typeface fontTitle) {
        this.fontTitle = fontTitle;
    }

    public long getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(long idProduct) {
        this.idProduct = idProduct;
    }

    public LabelImage getImage() {
        return image;
    }

    public void setImage(LabelImage image) {
        this.image = image;
    }

    public String getFontSizeText() {
        return fontSizeText;
    }

    public void setFontSizeText(String fontSizeText) {
        this.fontSizeText = fontSizeText;
    }


    public String getFontSizeTitle() {
        return fontSizeTitle;
    }

    public void setFontSizeTitle(String fontSizeTitle) {
        this.fontSizeTitle = fontSizeTitle;
    }


    public int getColorText() {
        return colorText;
    }

    public void setColorText(int colorText) {
        this.colorText = colorText;
    }

    public int getColorTitle() {
        return colorTitle;
    }

    public void setColorTitle(int colorTitle) {
        this.colorTitle = colorTitle;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isTitle() {
        return isTitle;
    }

    public void setTitle(boolean title) {
        isTitle = title;
    }
}
