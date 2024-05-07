package com.aditya.shop;

import com.aditya.shop.entity.Product;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@RestController //Untuk membuat api
public class App {
    public static void main(String[] args) {
        SpringApplication.run(ShopApplication.class, args);
    }

//    produce = ini dari server, kita mau mengembalikan data apa ke client, misal JSON
//    consume = request yang diterima, maksudnya server kita mengkonsumsi data apa


    @GetMapping(path = "/hello")
    public String helloWorld() {
        return "<h1>Hello world</h1>";
    }

    @GetMapping(path = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getProducts(){
        return Map.of(
                "id", "1",
                "name", "Apel",
                "price", 3000
        );
    }

//    Query Param
    @GetMapping(path = "/menus")
    public List<Map<String, Object>> getMenusFilter(
            @RequestParam(name = "name", required = false) String nama,
            @RequestParam(name = "maxPrice", required = false) Integer max
    ) {
        Map<String, Object> menu = Map.of(
                "id", "1",
                "name", ""+nama,
                "price", ""+max
        );

        List<Map<String, Object>> menus = new ArrayList<>();
        menus.add(menu);

        return menus;
    }

//    path variable
//    biasanya untuk findById
    @GetMapping(path = "/menus/{id}")
    public String getMenuById(@PathVariable(name = "id") String id /*atau tanpa String id*/) {
        return "Menu dengan id = " + id;
    }


    @PostMapping(
            path = "/api/v0/products",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Product createProduct(@RequestBody Product product) {
        return product;
    }

}
