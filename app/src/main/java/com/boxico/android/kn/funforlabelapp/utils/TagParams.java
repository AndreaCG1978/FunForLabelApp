package com.boxico.android.kn.funforlabelapp.utils;

import android.graphics.Color;
import android.graphics.Typeface;

import com.boxico.android.kn.funforlabelapp.dtos.LabelFont;
import com.boxico.android.kn.funforlabelapp.dtos.LabelImage;

public class TagParams {

    private LabelImage image;
    private String fontSizeText;
    private Typeface fontText;
    private String fontSizeTitle;
    private Typeface fontTitle;
    private int colorText = Color.BLACK;
    private int colorTitle = Color.BLACK;
    private String text;
    private String title;
    private boolean isTitle = false;
    private long idProduct = -1;

    private int posSizeText;
    private int posSizeTitle;
    private int posFontText;
    private int posFontTitle;
    private int posColorText;
    private int posColorTitle;
    private int posImage;

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
