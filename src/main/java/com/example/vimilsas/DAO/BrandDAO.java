package com.example.vimilsas.DAO;
import com.example.vimilsas.entity.Brand;
import java.util.List;


public interface BrandDAO {
    void save(Brand brand) ;
    Brand findById(Integer id);       
    List<Brand> findAll();
    void delete(int id );
}
