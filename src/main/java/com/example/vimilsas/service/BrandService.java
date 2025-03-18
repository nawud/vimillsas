package com.example.vimilsas.service;

import com.example.vimilsas.DAO.BrandDAO;
import com.example.vimilsas.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandService {

    private final BrandDAO brandDAO;

    @Autowired
    public BrandService(BrandDAO brandDAO) {
        this.brandDAO = brandDAO;
    }

    public List<Brand> getAllBrands() {
        return brandDAO.findAll();
    }

    public Brand getBrandById(int id) {
        return brandDAO.findById(id);
    }
}
