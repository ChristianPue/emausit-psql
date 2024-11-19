package com.example.demo.controller;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import net.coobird.thumbnailator.Thumbnails;
import java.io.ByteArrayOutputStream;

import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.service.ProductService;
import com.example.demo.service.UploadFileService;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private UploadFileService upload; // De la imagen

    @GetMapping("")
    public String show(Model model)
    {
        model.addAttribute("products", productService.findAll());
        return "products/show";
    }

    @GetMapping("/create")
    public String create()
    {
        return "products/create";
    }

    @PostMapping("/save")
    public String save(Product product, @RequestParam("img") MultipartFile file) throws IOException
    {
        LOGGER.info("Este es el objeto Product {}", product);
        
        User user = new User(1, "", "", "", "", "");
        product.setUser(user);

        // Guardado de la imagen
        if (product.getId() == null)
        {
            // Verificar tamaño del archivo
            if (file.getSize() > 1_000_000) { // Mayor a 1 MB
                LOGGER.info("El archivo supera 1 MB. Comenzando compresión...");
                
                // Comprimir imagen
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                Thumbnails.of(file.getInputStream())
                        .size(800, 800) // Ajusta según tus necesidades
                        .outputFormat("jpg") // Forzar formato si es necesario
                        .outputQuality(0.7) // Calidad de compresión (0.0 a 1.0)
                        .toOutputStream(outputStream);
                
                // Guardar la imagen comprimida
                String imageName = upload.saveCompressedImage(outputStream.toByteArray(), file.getOriginalFilename());
                product.setImage(imageName);
            } 
            else 
            {
                // Guardar la imagen directamente
                String imageName = upload.saveImage(file);
                product.setImage(imageName);
            }
        }

        productService.save(product);

        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model)
    {
        Optional<Product> optProduct = productService.get(id);
        Product product = optProduct.get();

        LOGGER.info("Producto bsucado: {}", product);
        model.addAttribute("product", product);

        return "products/edit";
    }

    @PostMapping("/update")
    public String update(Product product, @RequestParam("img") MultipartFile file) throws IOException
    {
        Product p = productService.get(product.getId()).get();

        if (file.isEmpty())
        {
            product.setImage(p.getImage());
        }
        else // Cuando se edita también la imagen
        {
            // Eliminar la imagen si es diferente a default.jpg
            if (!p.getImage().equals("default.jpg"))
            {
                upload.deleteImage(p.getImage());
            }

            String imageName = upload.saveImage(file);
            product.setImage(imageName);
        }

        product.setUser(p.getUser());
        productService.update(product);

        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id)
    {
        Product product = productService.get(id).get();

        // Eliminar la imagen si es diferente a default.jpg
        if (!product.getImage().equals("default.jpg"))
        {
            upload.deleteImage(product.getImage());
        }

        productService.delete(id);
        return "redirect:/products";
    }
}
