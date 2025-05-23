package com.ssafy.enjoytrip.features.image.adapter.out.storage;

import com.ssafy.enjoytrip.features.image.application.port.out.ImageStoragePort;
import com.ssafy.enjoytrip.features.image.domain.component.Mimetype;
import com.ssafy.enjoytrip.features.image.domain.component.RelativePath;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ImageFileSystemStoarageAdapter implements
        ImageStoragePort {
    @Value("${upload.image.location}")
    private String baseLocation;
    @Value("${upload.image.thumbnail.width}")
    private int thumbnailWidth;
    @Value("${upload.image.thumbnail.height}")
    private int thumbnailHeight;
    private Path basePath;

    @PostConstruct
    public void init() {
        this.basePath = Paths.get(baseLocation);
    }

    @Override
    public ImageMeta saveImage(String fileName, byte[] content, Mimetype mimetype) {
        try {
            String dir1 = fileName.substring(0, 2);
            String dir2 = fileName.substring(2, 4);

            // 1. 저장 디렉토리 경로 설정
            Path imageDir = basePath.resolve(Paths.get(dir1, dir2, "image"));
            Path thumbnailDir = basePath.resolve(Paths.get(dir1, dir2, "thumbnail"));
            Files.createDirectories(imageDir);
            Files.createDirectories(thumbnailDir);

            // 2. 확장자 및 파일 경로 설정
            String extension = getExtensionFromMimeType(mimetype);
            String imageFileName = fileName + "." + extension;
            String thumbnailFileName = fileName + "_thumb.jpg";

            Path imagePath = imageDir.resolve(imageFileName);
            Path thumbnailPath = thumbnailDir.resolve(thumbnailFileName);

            // 3. 파일 저장
            Files.write(imagePath, content);

            // 4. 썸네일 생성
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(content));
            BufferedImage thumbnailImage = toJpegCompatible(originalImage, thumbnailWidth, thumbnailHeight); // 썸네일 크기
            ImageIO.write(thumbnailImage, "jpg", thumbnailPath.toFile());

            String relBase = String.format("/uploads/images/%s/%s", dir1, dir2);
            return new ImageMeta(
                    new RelativePath(relBase + "/image/" + imageFileName),
                    new RelativePath(relBase + "/thumbnail/" + thumbnailFileName),
                    mimetype
            );
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 중 오류 발생", e);
        }
    }

    private String getExtensionFromMimeType(Mimetype mimeType) {
        return switch (mimeType) {
            case JPEG -> "jpg";
            case PNG -> "png";
            default -> throw new IllegalArgumentException("지원하지 않는 MIME 타입: " + mimeType);
        };
    }

    private BufferedImage toJpegCompatible(BufferedImage src, int width, int height) {
        // 1. 리사이즈된 알파포함 이미지
        BufferedImage resizedWithAlpha = resizeImage(src, width, height);

        // 2. 알파 없는 RGB 이미지 생성 (흰 배경)
        BufferedImage rgbImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = rgbImage.createGraphics();
        g.setPaint(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.drawImage(resizedWithAlpha, 0, 0, null);
        g.dispose();

        return rgbImage;
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        BufferedImage resized = new BufferedImage(width, height, originalImage.getType());
        Graphics2D g = resized.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return resized;
    }
}
