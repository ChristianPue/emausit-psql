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
    public String save(Product p, @RequestParam("img") MultipartFile file) throws IOException
    {
        LOGGER.info("Este es el objeto Product {}", p);
        
        User u = new User(1, "", "", "", "", "");
        p.setUser(u);

        // Guardado de la imagen
        if (p.getId() == null)
        {
            String imageName = upload.saveImage(file);
            p.setImage(imageName);
        }
        else
        {
            if (file.isEmpty()) 
            {
                Product p_ = productService.get(p.getId()).get();
                p.setImage(p_.getImage());
            }
            else
            {
                String imageName = upload.saveImage(file);
                p.setImage(imageName);
            }
        }

        productService.save(p);

        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model)
    {
        Optional<Product> optProduct = productService.get(id);
        Product p = optProduct.get();

        LOGGER.info("Producto bsucado: {}", p);
        model.addAttribute("product", p);

        return "products/edit";
    }

    @PostMapping("/update")
    public String update(Product p, @RequestParam("img") MultipartFile file) throws IOException
    {
        if (!file.isEmpty())
        {
            Product p_ = productService.get(p.getId()).get();
            p.setImage(p_.getImage());
        }
        else // Cuando se edita también la imagen
        {
            Product p_ = productService.get(p.getId()).get();

            // Eliminar la imagen si es diferente a default.jpg
            if (!p_.getImage().equals("default.jpg"))
            {
                upload.deleteImage(p_.getImage());
            }

            String imageName = upload.saveImage(file);
            p.setImage(imageName);
        }

        productService.update(p);

        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id)
    {
        Product p = productService.get(id).get();

        // Eliminar la imagen si es diferente a default.jpg
        if (!p.getImage().equals("default.jpg"))
        {
            upload.deleteImage(p.getImage());
        }

        productService.delete(id);
        return "redirect:/products";
    }
}
