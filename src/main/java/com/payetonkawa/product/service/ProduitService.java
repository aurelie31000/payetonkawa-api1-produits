package com.payetonkawa.product.service;

import com.payetonkawa.product.model.Produit;
import java.util.List;

public interface ProduitService {
    List<Produit> getAllProduits();
    Produit getProduitById(Long id);
    Produit createProduit(Produit produit);
    Produit updateProduit(Long id, Produit produitDetails);
    void deleteProduit(Long id);
}
