package com.ssafy.enjoytrip.features.image.domain.component;

import lombok.Getter;

@Getter
public enum Mimetype {
    JPEG("image/jpeg"),
    PNG("image/png"),
//    GIF("image/gif"),
//    SVG("image/svg+xml"),
//    WEBP("image/webp"),
//    BMP("image/bmp"),
//    TIFF("image/tiff"),
//    ICO("image/x-icon"),
//    HEIC("image/heic"),
//    AVIF("image/avif")
    ;

    private final String value;

    Mimetype(String value) {
        this.value = value;
    }

    public static boolean isSupported(String type) {
        if (type == null) {
            return false;
        }
        for (Mimetype mt : values()) {
            if (mt.value.equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }

    public static Mimetype from(String type) {
        for (Mimetype mt : values()) {
            if (mt.value.equalsIgnoreCase(type)) {
                return mt;
            }
        }
        throw new IllegalArgumentException("Unsupported image MIME type: " + type);
    }
}
