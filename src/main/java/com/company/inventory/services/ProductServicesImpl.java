package com.company.inventory.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.inventory.dao.ICategoryDao;
import com.company.inventory.dao.IProductDao;
import com.company.inventory.model.Category;
import com.company.inventory.model.Product;
import com.company.inventory.response.ProductResponseRest;
import com.company.inventory.util.Util;

@Service
public class ProductServicesImpl implements IProductServices {
	
	private ICategoryDao categoryDao;
	private IProductDao productDao;

	public ProductServicesImpl(ICategoryDao categoryDao, IProductDao productDao) {
		super();
		this.categoryDao = categoryDao;
		this.productDao = productDao;
	}

	@Override
	@Transactional
	public ResponseEntity<ProductResponseRest> save(Product product, Long categoryId) {
		ProductResponseRest response = new ProductResponseRest();
		List<Product> list = new ArrayList<>();
		try {
			Optional<Category> category = categoryDao.findById(categoryId);
			if(category.isPresent()) {
				product.setCategory(category.get());
			}else {
				response.setMetadata("Error", "-1", "Categoría no encontrada");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}
			Product productSaved = productDao.save(product);
			if(productSaved != null) {
				list.add(productSaved);
				response.getProduct().setProducts(list);
				response.setMetadata("OK", "00", "Producto guardado");
			}else {
				response.setMetadata("Error", "-1", "Producto no guardado");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.getStackTrace();
			response.setMetadata("Error", "-1", "Error");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ProductResponseRest> searchById(Long id) {
		// TODO Auto-generated method stub
		ProductResponseRest response = new ProductResponseRest();
		List<Product> list = new ArrayList<>();
		try {
			Optional<Product> product = productDao.findById(id);
			if(product.isPresent()) {
				byte [] imageDecompressed = Util.decompressZLib(product.get().getPicture());
				product.get().setPicture(imageDecompressed);
				list.add(product.get());
				response.getProduct().setProducts(list);
				response.setMetadata("OK", "00", "Producto encontrado");
			}else {
				response.setMetadata("Error", "-1", "Producto no encontrado");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}
			
		} catch (Exception e) {
			e.getStackTrace();
			response.setMetadata("Error", "-1", "Error");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ProductResponseRest> searchByName(String name) {
		// TODO Auto-generated method stub
		ProductResponseRest response = new ProductResponseRest();
		List<Product> list = new ArrayList<>();
		List<Product> listAux = new ArrayList<>();

		try {
			listAux = productDao.findByNameContainingIgnoreCase(name);
			
			if(listAux.size()>0) {
				listAux.stream().forEach((p) -> {
					byte [] imageDecompressed = Util.decompressZLib(p.getPicture());
					p.setPicture(imageDecompressed);
					list.add(p);
				});
				response.getProduct().setProducts(list);
				response.setMetadata("OK", "00", "Productos encontrados");
			}else {
				response.setMetadata("Error", "-1", "Productos no encontrados");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}
			
		} catch (Exception e) {
			e.getStackTrace();
			response.setMetadata("Error", "-1", "Error al buscar producto por nombre");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
			
	}

	@Override
	@Transactional
	public ResponseEntity<ProductResponseRest> deleteById(Long id) {
		// TODO Auto-generated method stub
		ProductResponseRest response = new ProductResponseRest();
		try {
			productDao.deleteById(id);
			response.setMetadata("OK", "00", "Producto eliminado");
			
		} catch (Exception e) {
			e.getStackTrace();
			response.setMetadata("Error", "-1", "Error al eliminar producto");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
	
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ProductResponseRest> search() {
		// TODO Auto-generated method stub
		ProductResponseRest response = new ProductResponseRest();
		List<Product> list = new ArrayList<>();
		List<Product> listAux = new ArrayList<>();

		try {
			listAux = (List<Product>) productDao.findAll();
			
			if(listAux.size()>0) {
				listAux.stream().forEach((p) -> {
					byte [] imageDecompressed = Util.decompressZLib(p.getPicture());
					p.setPicture(imageDecompressed);
					list.add(p);
				});
				response.getProduct().setProducts(list);
				response.setMetadata("OK", "00", "Productos encontrados");
			}else {
				response.setMetadata("Error", "-1", "Productos no encontrados");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}
			
		} catch (Exception e) {
			e.getStackTrace();
			response.setMetadata("Error", "-1", "Error al buscar productos");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
	}
}
