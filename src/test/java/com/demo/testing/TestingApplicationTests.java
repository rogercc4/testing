package com.demo.testing;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TestingApplicationTests {

	@Mock List<String> datos;
	
	@Test
	void contextLoads() {
		String valorEsperado = "Hola";
		when(datos.get(0)).thenReturn(valorEsperado);
		assertEquals(valorEsperado, datos.get(0));
	}
}
