package com.payetonkawa.product.service;
import com.payetonkawa.product.exception.ResourceNotFoundException;
import com.payetonkawa.product.model.Produit;
import com.payetonkawa.product.repository.ProduitRepository;
import com.payetonkawa.product.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class ProduitService {
    private final ProduitRepository produitRepository;
    private final RabbitTemplate rabbitTemplate;
    public ProduitService(ProduitRepository produitRepository, RabbitTemplate rabbitTemplate) { 
        this.produitRepository = produitRepository;
        this.rabbitTemplate = rabbitTemplate;
    }
    public List<Produit> getAllProduits() { return produitRepository.findAll(); }
    public Produit getProduitById(Long id) { return produitRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec id " + id)); }
    public Produit createProduit(Produit produit) { 
        Produit createdProduit = produitRepository.save(produit);
        rabbitTemplate.convertAndSend(RabbitMQConfig.PRODUIT_CHANGE_QUEUE, createdProduit);
        return createdProduit;
    }
    public Produit updateProduit(Long id, Produit produitDetails) {
        Produit produit = getProduitById(id);
        produit.setNom(produitDetails.getNom());
        produit.setDescription(produitDetails.getDescription());
        produit.setPrix(produitDetails.getPrix());
        Produit updatedProduit = produitRepository.save(produit);
        rabbitTemplate.convertAndSend(RabbitMQConfig.PRODUIT_CHANGE_QUEUE, updatedProduit);
        return updatedProduit;
    }
    public void deleteProduit(Long id) { 
        produitRepository.delete(getProduitById(id));
        rabbitTemplate.convertAndSend(RabbitMQConfig.PRODUIT_CHANGE_QUEUE, "DELETE:" + id);
    }
}
