package com.company.inventory.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.inventory.dao.ICategoryDao;
import com.company.inventory.response.CategoryResponseREST;
import com.company.inventory.model.Category;

import java.util.List;

@Service
public class CategoryServicesImpl implements ICategoryService{

	@Autowired
	private ICategoryDao categoryDao;
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<CategoryResponseREST> search() {
		CategoryResponseREST response = new CategoryResponseREST();
		try {
			List<Category> category = (List<Category>) categoryDao.findAll();
			response.getCategoryResponse().setCategory(category);
			response.setMedatada("Response ok", "00", "Respuesta exitosa");
		}catch (Exception e) {
			// TODO: handle exception
			response.setMedatada("Response no ok", "-1", "Error");
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseREST>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<CategoryResponseREST>(response, HttpStatus.OK);
	}

}
