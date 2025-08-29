package com.payetonkawa.product.service;

import com.payetonkawa.product.exception.ResourceNotFoundException;
import com.payetonkawa.product.model.Produit;
import com.payetonkawa.product.repository.ProduitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProduitServiceImplTest {

    @Mock
    private ProduitRepository produitRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private ProduitServiceImpl produitService;

    private Produit produit;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        produit = new Produit();
        produit.setId(1L);
        produit.setNom("Produit Test");
        produit.setDescription("Description Test");
        produit.setPrix(100.0);
    }

    @Test
    public void testGetAllProduits() {
        when(produitRepository.findAll()).thenReturn(List.of(produit));

        assertEquals(1, produitService.getAllProduits().size());
        verify(produitRepository, times(1)).findAll();
    }

    @Test
    public void testGetProduitByIdSuccess() {
        when(produitRepository.findById(1L)).thenReturn(java.util.Optional.of(produit));

        Produit result = produitService.getProduitById(1L);
        assertNotNull(result);
        assertEquals("Produit Test", result.getNom());
    }

    @Test
    public void testGetProduitByIdNotFound() {
        when(produitRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> produitService.getProduitById(1L));
    }

    @Test
    public void testCreateProduit() {
        when(produitRepository.save(any(Produit.class))).thenReturn(produit);

        Produit createdProduit = produitService.createProduit(produit);
        assertNotNull(createdProduit);
        assertEquals("Produit Test", createdProduit.getNom());
        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), eq(produit));
    }

    @Test
    public void testUpdateProduit() {
        when(produitRepository.findById(1L)).thenReturn(java.util.Optional.of(produit));
        when(produitRepository.save(any(Produit.class))).thenReturn(produit);

        Produit updatedProduit = produitService.updateProduit(1L, produit);
        assertNotNull(updatedProduit);
        assertEquals("Produit Test", updatedProduit.getNom());
        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), eq(updatedProduit));
    }

    @Test
    public void testDeleteProduit() {
        when(produitRepository.findById(1L)).thenReturn(java.util.Optional.of(produit));

        produitService.deleteProduit(1L);
        verify(produitRepository, times(1)).delete(any(Produit.class));
        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), eq("DELETE:1"));
    }
}
