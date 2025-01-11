package io.hexlet.spring;

import io.hexlet.spring.model.Page;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
@RestController
public class Application {
    private List<Page> pages = new ArrayList<Page>();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @GetMapping("/")
    public String home() {
        return "Hello World!";
    }

    @GetMapping("/pages")
    public ResponseEntity<List<Page>> index(@RequestParam(defaultValue = "10") Integer limit) {
        var result = pages.stream().limit(limit).toList();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(pages.size()))
                .body(result);
    }

    @GetMapping("/pages/{id}")
    public ResponseEntity<Page> show(@PathVariable String id) {
        var page = pages.stream()
                .filter(p -> p.getSlug().equals(id))
                .findFirst();
        return ResponseEntity.of(page);
    }

    @PostMapping("/pages")
    public Page create(@RequestBody Page page) {
        pages.add(page);
        return page;
    }

    @PutMapping("/pages/{id}")
    public Page update(@PathVariable String id, @RequestBody Page data) {
        var maybePage = pages.stream()
                .filter(p -> p.getSlug().equals(id))
                .findFirst();
        if (maybePage.isPresent()) {
            var page = maybePage.get();
            page.setSlug(data.getSlug());
            page.setName(data.getName());
            page.setBody(data.getBody());
        }
        return data;
    }

    @DeleteMapping("/pages/{id}")
    public void destroy(@PathVariable String id) {
        pages.removeIf(p -> p.getSlug().equals(id));
    }
}
