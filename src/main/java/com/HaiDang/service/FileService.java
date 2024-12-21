package com.HaiDang.service;

import com.HaiDang.model.Product;
import com.HaiDang.repository.ProductRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileService {
    private static final String UPLOAD_DIR = "D:\\Uploads\\";
    @Autowired
    ProductRepository productRepository;
    @Autowired
    private Cloudinary cloudinary;

    public boolean uploadFile(MultipartFile file){
        try{

            File upload = new File(UPLOAD_DIR);
            if (!upload.exists()) {
                upload.mkdirs();
            }
            else{
                System.out.println("Đã tạo thư mục xong");
            }
            String path = UPLOAD_DIR + file.getOriginalFilename();
            File f = new File(path);
            System.out.println(f.getAbsoluteFile());
            file.transferTo(f);
            System.out.println("Cuối");
        }catch (Exception e){
            System.out.println("Lỗi: "+e.getMessage());
            return false;
        }
        return true;
    }
    public Product upLoadImage(MultipartFile file, Long id) throws IOException {
        assert file.getOriginalFilename() != null;
        String publicValue = generatePublicValue(file.getOriginalFilename());
        String extension = getFileName(file.getOriginalFilename())[1];
        File fileUpload = convert(file);
        cloudinary.uploader().upload(fileUpload, ObjectUtils.asMap("public_id", publicValue));
        clearDisk(fileUpload);

        String url = cloudinary.url().generate(StringUtils.join(publicValue, ".", extension));
        Optional<Product> productOptional = productRepository.findById(id);
        Product product = productOptional.get();
        product.setImageUrl(url);
        productRepository.save(product);
        return product;
    }
    private File convert(MultipartFile file) throws IOException {
        assert file.getOriginalFilename()!=null;
        File convertFile = new File(StringUtils.join(generatePublicValue(file.getOriginalFilename()), getFileName(file.getOriginalFilename())[1]));
        try(InputStream is = file.getInputStream()){
            Files.copy(is, convertFile.toPath());
        }
        return convertFile;
    }

    private void clearDisk(File file){
        try {
            Path path = file.toPath();
            Files.delete(path);
        }catch (Exception e){

        }
    }

    public String generatePublicValue(String originalName){
        String fileName = getFileName(originalName)[0];
        return StringUtils.join(UUID.randomUUID().toString(),"_", fileName);
    }
    public String[] getFileName(String originalName){
        return originalName.split("\\.");
    }
}
