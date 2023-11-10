package com.joseleonardo.unittests.mockito.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.joseleonardo.data.dto.v1.LivroDTO;
import com.joseleonardo.exceptions.RequiredObjectIsNullException;
import com.joseleonardo.model.Livro;
import com.joseleonardo.repositories.LivroRepository;
import com.joseleonardo.services.LivroService;
import com.joseleonardo.unittests.mapper.mocks.MockLivro;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class LivroServiceTest {

	private MockLivro input;
	
	@InjectMocks
	private LivroService livroService;
	
	@Mock
	private LivroRepository livroRepository;
	
	@BeforeEach
	void setUpMocks() throws Exception {
		input = new MockLivro();
		
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testListarTodas() {
		List<Livro> list = input.mockEntityList();
		
		when(livroRepository.findAll()).thenReturn(list);
		
		List<LivroDTO> livros = livroService.listarTodos();
		
		assertNotNull(livros);
		
		assertEquals(14, livros.size());
		
		LivroDTO livroUm = livros.get(1);
		
		assertNotNull(livroUm);
		assertNotNull(livroUm.getId());
		assertNotNull(livroUm.getLinks());
		
		assertTrue(livroUm.toString().contains("[</api/livros/v1/1>;rel=\"self\"]"));
		
		assertEquals("Algum Autor1", livroUm.getAutor());
		assertEquals("Algum Título1", livroUm.getTitulo());
		assertEquals(25D, livroUm.getPreco());
		assertNotNull(livroUm.getDataLancamento());
		
		LivroDTO liveroQuatro = livros.get(4);
		
		assertNotNull(liveroQuatro);
		assertNotNull(liveroQuatro.getId());
		assertNotNull(liveroQuatro.getLinks());
		
		assertTrue(liveroQuatro.toString().contains("[</api/livros/v1/4>;rel=\"self\"]"));
		
		assertEquals("Algum Autor4", liveroQuatro.getAutor());
		assertEquals("Algum Título4", liveroQuatro.getTitulo());
		assertEquals(25D, liveroQuatro.getPreco());
		assertNotNull(liveroQuatro.getDataLancamento());
		
		LivroDTO livroSete = livros.get(7);
		
		assertNotNull(livroSete);
		assertNotNull(livroSete.getId());
		assertNotNull(livroSete.getLinks());
		
		assertTrue(livroSete.toString().contains("[</api/livros/v1/7>;rel=\"self\"]"));
		
		assertEquals("Algum Autor7", livroSete.getAutor());
		assertEquals("Algum Título7", livroSete.getTitulo());
		assertEquals(25D, livroSete.getPreco());
		assertNotNull(livroSete.getDataLancamento());
	}

	@Test
	void testBuscarPorId() {
		Livro entity = input.mockEntity(1);
		
		when(livroRepository.findById(1L)).thenReturn(Optional.of(entity));
		
		LivroDTO resultado = livroService.buscarPorId(1L);
		
		assertNotNull(resultado);
		assertNotNull(resultado.getId());
		assertNotNull(resultado.getLinks());
		
		assertTrue(resultado.toString().contains("[</api/livros/v1/1>;rel=\"self\"]"));
		
		assertEquals("Algum Autor1", resultado.getAutor());
		assertEquals("Algum Título1", resultado.getTitulo());
		assertEquals(25D, resultado.getPreco());
		assertNotNull(resultado.getDataLancamento());
	}

	@Test
	void testSalvar() {
		Livro entity = input.mockEntity(1);
		Livro persistido = entity;
		
		when(livroRepository.save(entity)).thenReturn(persistido);
		
		LivroDTO dto = input.mockVO(1);
		dto.setId(1L);
		
		LivroDTO resultado = livroService.salvar(dto);
		
		assertNotNull(resultado);
		assertNotNull(resultado.getId());
		assertNotNull(resultado.getLinks());
		
		assertTrue(resultado.toString().contains("[</api/livros/v1/1>;rel=\"self\"]"));
		
		assertEquals("Algum Autor1", resultado.getAutor());
		assertEquals("Algum Título1", resultado.getTitulo());
		assertEquals(25D, resultado.getPreco());
		assertNotNull(resultado.getDataLancamento());
	}
	
	@Test
	void testSalvarComLivroNulo() {
		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
			livroService.salvar(null);
		});
		
		String mensagemEsperada = "Não é permitido persistir um objeto nulo!";
		String mensagemAtual = exception.getMessage();
		
		assertTrue(mensagemAtual.contains(mensagemEsperada));
	}

	@Test
	void testAtualizar() {
		Livro entity = input.mockEntity(1);
		Livro persistido = entity;
		
		when(livroRepository.findById(1L)).thenReturn(Optional.of(entity));
		when(livroRepository.save(entity)).thenReturn(persistido);
		
		LivroDTO dto = input.mockVO(1);
		dto.setId(1L);
		
		LivroDTO resultado = livroService.atualizar(dto);
		
		assertNotNull(resultado);
		assertNotNull(resultado.getId());
		assertNotNull(resultado.getLinks());
		
		assertTrue(resultado.toString().contains("[</api/livros/v1/1>;rel=\"self\"]"));
		
		assertEquals("Algum Autor1", resultado.getAutor());
		assertEquals("Algum Título1", resultado.getTitulo());
		assertEquals(25D, resultado.getPreco());
		assertNotNull(resultado.getDataLancamento());
	}
	
	@Test
	void testAtualizarComLivroNulo() {
		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
			livroService.atualizar(null);
		});
		
		String mensagemEsperada = "Não é permitido persistir um objeto nulo!";
		String mensagemAtual = exception.getMessage();
		
		assertTrue(mensagemAtual.contains(mensagemEsperada));
	}

	@Test
	void testDeletar() {
		Livro entity = input.mockEntity(1);
		
		when(livroRepository.findById(1L)).thenReturn(Optional.of(entity));
		
		livroService.deletar(1L);
	}

}
