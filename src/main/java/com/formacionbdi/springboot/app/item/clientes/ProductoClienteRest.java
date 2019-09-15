package com.formacionbdi.springboot.app.item.clientes;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.formacionbdi.springboot.app.commons.models.entity.Producto;

@FeignClient(name = "servicio-productos")
public interface ProductoClienteRest {

	@GetMapping("/listar")
	public List<Producto> listar();
	
	@GetMapping("/producto/{id}")
	public Producto detalle(@PathVariable Long id);
	
	@PostMapping("/producto")
	public Producto crear(@RequestBody Producto producto);
	
	@PutMapping("/producto/{id}")
	public Producto update(@RequestBody Producto producto,@PathVariable Long id);
	
	@DeleteMapping("/producto/{id}/eliminar")
	public void elminar(@PathVariable Long id);
	
}
