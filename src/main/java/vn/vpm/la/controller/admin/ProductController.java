package vn.vpm.la.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import vn.vpm.la.domain.Product;
import vn.vpm.la.service.ProductService;
import vn.vpm.la.service.UploadService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;



@Controller
public class ProductController {

    private final ProductService productService;
    private final UploadService uploadService;

    public ProductController(ProductService productService,  UploadService uploadService) {
        this.productService = productService;
        this.uploadService  =  uploadService;
    }

    @GetMapping("/admin/product")
    public String getDashboard(Model model){
        List<Product> lists = this.productService.getAllProduct();
        model.addAttribute("products", lists);
        return "admin/product/show";
    }

    // Create
    @GetMapping("/admin/product/create")
    public String getProductsPage(Model model) {
        model.addAttribute("newProduct" , new Product());
        return "admin/product/create";
    }

    @PostMapping("/admin/product/create")
    public String postCreateProduct(Model model, @ModelAttribute("newProduct") @Valid Product product
    ,BindingResult newProductBindingResult, @RequestParam("hoidanitFile") MultipartFile file) {

        //kiểm tra nếu newProductBindingResult (những thuộc tính không input vào thì refresh về trang create product)
        if (newProductBindingResult.hasErrors()) {
            return "admin/product/create"; 
        }

        String image = this.uploadService.handleSaveUploadFile(file, "product");
        product.setImage(image); 

        this.productService.handleSaveProduct(product);
        return "redirect:/admin/product";
    }

    // Detail
    @GetMapping("/admin/product/{id}")
    public String getMethodName(Model model,  @PathVariable Long id) {

        model.addAttribute("id", id);
        Product product = this.productService.getfindByIdProduct(id).get();
        model.addAttribute("product", product);
        return "admin/product/detail";
    }

    // Delete
    @GetMapping("/admin/product/delete/{id}")
    public String getProductDetelePage(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        model.addAttribute("newProduct",this.productService.getfindByIdProduct(id) );
        return "admin/product/delete";
    }
    
    @PostMapping("/admin/product/delete")
    public String postProductDetele(Model model, @ModelAttribute("newProduct") Product product) {
        this.productService.handleDeleteProduct(product.getId());
        return "redirect:/admin/product";
    }
    
    // Update
    @GetMapping("/admin/product/update/{id}")
    public String getMethodName(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        model.addAttribute("newProduct", this.productService.getfindByIdProduct(id).get());
        return "admin/product/update";
    }
    
    @PostMapping("/admin/product/update")
    public String postUpdateProduct(Model model, @ModelAttribute("newProduct") @Valid Product product,
    BindingResult newProductBindingResult,
    @RequestParam("hoidanitFile") MultipartFile file) {
        
        // validate
        if (newProductBindingResult.hasErrors()) {
            return "admin/product/update";
        }
        Product currentProduct = this.productService.getfindByIdProduct(product.getId()).get();
        model.addAttribute("newProduct", currentProduct );
        if (currentProduct != null) {

            // SetImage
            if (!file.isEmpty()) {
                String img = this.uploadService.handleSaveUploadFile(file, "product");
                currentProduct.setImage(img);
            }
            currentProduct.setName(product.getName());
            currentProduct.setDetailDesc(product.getDetailDesc());
            currentProduct.setFactory(product.getFactory());
            currentProduct.setDetailDesc(product.getDetailDesc());
            currentProduct.setPrice(product.getPrice());
            currentProduct.setQuantity(product.getQuantity());
            currentProduct.setShortDesc(product.getShortDesc());
            currentProduct.setTarget(product.getTarget());

            this.productService.handleSaveProduct(currentProduct);
            
        }        
        return "redirect:/admin/product";
    }
    
    
}
