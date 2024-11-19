package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileService {
    private final String folder = "images//";

    public String saveImage(MultipartFile file) throws IOException
    {
        if (!file.isEmpty())
        {
            byte [] bytes = file.getBytes();
            Path path = Paths.get(folder + file.getOriginalFilename());
            Files.write(path, bytes);
            return file.getOriginalFilename();
        }
        return "default.jpg";
    }

    public void deleteImage(String name)
    {
        String path = "images://";

        File file = new File(path + name);
        file.delete();
    }

    // Otros métodos
    public String saveCompressedImage(byte[] compressedImage, String originalFilename) throws IOException {
        String compressedImageName = "compressed_" + originalFilename;
        File file = new File(folder + compressedImageName); // Cambia a tu ruta
        Files.write(file.toPath(), compressedImage);
        return compressedImageName;
    }
}
