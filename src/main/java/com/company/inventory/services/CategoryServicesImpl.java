package com.company.inventory.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.inventory.dao.ICategoryDao;
import com.company.inventory.response.CategoryResponseREST;
import com.company.inventory.model.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
	
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<CategoryResponseREST> searchById(Long id) {
		// TODO Auto-generated method stub
		CategoryResponseREST response = new CategoryResponseREST();
		List<Category> list = new ArrayList<>();
		
		try {
			Optional<Category> category = categoryDao.findById(id);
			if(category.isPresent()) {
				list.add(category.get());
				response.getCategoryResponse().setCategory(list);
				response.setMedatada("Response ok", "00", "Categoría encontrada");
			}else {
				response.setMedatada("Response no ok", "-1", "No se encontró la categoria por id");
				return new ResponseEntity<CategoryResponseREST>(response, HttpStatus.NOT_FOUND);
			}
		}catch (Exception e) {
			// TODO: handle exception
			response.setMedatada("Response no ok", "-1", "Error categoria por id");
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseREST>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<CategoryResponseREST>(response, HttpStatus.OK);
	}

	@Override
	@Transactional
	public ResponseEntity<CategoryResponseREST> save(Category category) {
		// TODO Auto-generated method stub
		CategoryResponseREST response = new CategoryResponseREST();
		List<Category> list = new ArrayList<>();
		
		try {
			Category categorySaved = categoryDao.save(category);
			if(categorySaved != null) {
				list.add(categorySaved);
				response.getCategoryResponse().setCategory(list);
				response.setMedatada("Response ok", "00", "Categoría guardada");
			}else {
				response.setMedatada("Response no ok", "-1", "Error al grabar la categoria");
				return new ResponseEntity<CategoryResponseREST>(response, HttpStatus.BAD_REQUEST);
			}
		}catch (Exception e) {
			// TODO: handle exception
			response.setMedatada("Response no ok", "-1", "Error");
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseREST>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<CategoryResponseREST>(response, HttpStatus.OK);
	}

	@Override
	@Transactional
	public ResponseEntity<CategoryResponseREST> update(Category category, Long id) {
		// TODO Auto-generated method stub
		CategoryResponseREST response = new CategoryResponseREST();
		List<Category> list = new ArrayList<>();
		
		try {
			Optional<Category> categorySearch= categoryDao.findById(id);
			
			if(categorySearch.isPresent()) {
				
				categorySearch.get().setName(category.getName());
				categorySearch.get().setDescription(category.getDescription());
				
				Category categoryToupdate = categoryDao.save(categorySearch.get());
				
				if(categoryToupdate!=null) {
					list.add(categoryToupdate);
					response.getCategoryResponse().setCategory(list);
					response.setMedatada("Response ok", "00", "Categoría actualizada");
				}else {
					response.setMedatada("Response no ok", "-1", "Error al actualizar la categoria");
					return new ResponseEntity<CategoryResponseREST>(response, HttpStatus.BAD_REQUEST);
				}
			}else {
				response.setMedatada("Response no ok", "-1", "Error al encontrar la categoria");
				return new ResponseEntity<CategoryResponseREST>(response, HttpStatus.NOT_FOUND);
			}
			
		}catch (Exception e) {
			// TODO: handle exception
			response.setMedatada("Response no ok", "-1", "Error");
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseREST>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<CategoryResponseREST>(response, HttpStatus.OK);

	}

}
