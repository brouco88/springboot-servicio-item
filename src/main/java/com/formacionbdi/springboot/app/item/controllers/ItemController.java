package com.formacionbdi.springboot.app.item.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.springboot.app.item.models.Item;
import com.formacionbdi.springboot.app.item.models.Producto;
import com.formacionbdi.springboot.app.item.models.service.ItemsService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RefreshScope
@RestController
public class ItemController {

	private static Logger LOG = LoggerFactory.getLogger(ItemController.class);
	
	@Autowired
	private Environment env;
	@Autowired
	@Qualifier("serviceFeing")
	//@Qualifier("serviceRestTemplate")
	private ItemsService itemService;
	
	@Value("${configuracion.texto}")
	private String texto;
	
	@GetMapping("/listar")
	public List<Item> listar(){
		return itemService.findAll();
	}
	
	@HystrixCommand(fallbackMethod = "metodoAlternativo")
	@GetMapping("/item/{id}/cantidad/{cantidad}")
	public Item detalle(@PathVariable Long id,@PathVariable Integer cantidad) {
		return itemService.findById(id, cantidad);
	}
	
	public Item metodoAlternativo(Long id, Integer cantidad) {
		Item item = new Item();
		Producto producto = new Producto();
		item.setCantidad(cantidad);
		producto.setId(id);
		producto.setNombre("Producto Default");
		producto.setPrecio(100.00);
		item.setProducto(producto);
		return item;
		
	}
	
	@GetMapping("/obtener-config")
	public ResponseEntity<?> obtenerConfig(@Value("${server.port}")String  port){
		Map<String,String> json = new HashMap<>();
		json.put("Entorno", texto);
		json.put("Puerto", port);
		LOG.info("Entorno: "+texto);
		LOG.info("Puerto:"+port);
		if(env.getActiveProfiles().length>0 && env.getActiveProfiles()[0].equals("dev")){
			json.put("Autor", env.getProperty("configuracion.autor.nombre"));
			json.put("Email", env.getProperty("configuracion.autor.email"));
			
		}
		
		return new ResponseEntity<Map<String,String>>(json,HttpStatus.OK);
	}
	
	@PostMapping("/producto")
	@ResponseStatus(HttpStatus.CREATED)
	public  Producto crear(@RequestBody Producto producto) {
		return itemService.save(producto);
	}
	
	@PutMapping("/producto/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public  Producto crear(@RequestBody Producto producto,@PathVariable Long id) {
		return itemService.update(producto, id);
	}
	
	@DeleteMapping("/producto/{id}/eliminar")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void elminar(@PathVariable Long id) {
		itemService.delete(id);
	}
}
