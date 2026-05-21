package es.quatroges.qgestpv_v3.bluetooth.ipad045_utils;

import android.graphics.Bitmap;

public class Datos {
    public enum enTipo { TEXTO,QR,BITMAP}
    enTipo tipo;
    String texto;
    int font;
    int aling;
    String QR;
    int module;
    int errorCorrectionLevel;
    Bitmap bitmap;
    int bitmapSize;

    public Datos(String texto, int font, int aling) {
        this.tipo = enTipo.TEXTO;
        this.texto = texto;
        this.font = font;
        this.aling = aling;

    }

    public Datos() {
    }

    public void addQR(String QR, int module, int errorCorrectionLevel, int aling) {
        this.QR = QR;
        this.module = module;
        this.errorCorrectionLevel = errorCorrectionLevel;
        this.aling = aling;
        this.tipo = enTipo.QR;
    }

    public void addBitmap(Bitmap bitmap, int bitmapSize, int aling) {
        this.bitmap = bitmap;
        this.bitmapSize = bitmapSize;
        this.aling = aling;
        this.tipo = enTipo.BITMAP;
    }

    public String getTexto() {
        return texto;
    }

    public int getFont() {
        return font;
    }

    public int getAling() {
        return aling;
    }

    public enTipo getTipo() {
        return tipo;
    }

    public String getQR() {
        return QR;
    }

    public int getModule() {
        return module;
    }

    public int getErrorCorrectionLevel() {
        return errorCorrectionLevel;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getBitmapSize() {
        return bitmapSize;
    }
}
