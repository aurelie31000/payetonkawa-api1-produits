package com.payetonkawa.product.controller;
import com.payetonkawa.product.model.Produit;
import com.payetonkawa.product.service.ProduitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/produits")
public class ProduitController {
    private final ProduitService produitService;
    public ProduitController(ProduitService produitService) { this.produitService = produitService; }
    @GetMapping
    public List<Produit> getAllProduits() { return produitService.getAllProduits(); }
    @GetMapping("/{id}")
    public ResponseEntity<Produit> getProduitById(@PathVariable Long id) { return ResponseEntity.ok(produitService.getProduitById(id)); }
    @PostMapping
    public ResponseEntity<Produit> createProduit(@RequestBody Produit produit) { return new ResponseEntity<>(produitService.createProduit(produit), HttpStatus.CREATED); }
    @PutMapping("/{id}")
    public ResponseEntity<Produit> updateProduit(@PathVariable Long id, @RequestBody Produit produitDetails) { return ResponseEntity.ok(produitService.updateProduit(id, produitDetails)); }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduit(@PathVariable Long id) { produitService.deleteProduit(id); return ResponseEntity.noContent().build(); }
}
