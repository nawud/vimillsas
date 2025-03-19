package com.example.vimilsas.DAO;



import com.example.vimilsas.entity.Brand;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class BrandDAOImp implements BrandDAO {

    private final EntityManager entityManager;

    //@Autowired
    public BrandDAOImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void save(Brand brand) {
        entityManager.persist(brand);
    }

    public Brand findById(Integer id) {
        return entityManager.find(Brand.class, id);
    }

    @Override
    public List<Brand> findAll() {

        TypedQuery<Brand> query = entityManager.createQuery("FROM Brand", Brand.class);
        return query.getResultList();
    }

    @Transactional
    @Override
    public void delete(int id) {

        Brand brand = entityManager.find(Brand.class, id);
        if (brand != null) {
            entityManager.remove(brand);
            System.out.println("Marca con ID " + id + " eliminada.");
        } else {
            System.out.println("No se encontró una marca con ID " + id + ".");
        }
    }
}
