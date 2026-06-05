package cl.usm.gestionPeliculasMemoria.controllers;

import cl.usm.gestionPeliculasMemoria.entities.Comentario;
import cl.usm.gestionPeliculasMemoria.entities.Pelicula;
import cl.usm.gestionPeliculasMemoria.services.PeliculasServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PeliculasControllerTest {

    @Mock
    private PeliculasServiceImpl peliculasService;

    @InjectMocks
    private PeliculasController peliculasController;

    private Pelicula p1;
    private Pelicula p2;
    private Comentario comentario1;
    private Comentario comentario2;
    private String idInexistente = "NO_EXISTE";
    private String idError = "ID_ERROR";

    @BeforeEach
    void setUp(){
        p1 = new Pelicula("Peli1", "Inception", "ChrisNol", "token1", null);
        p2 = new Pelicula("Peli2", "Interstellar", "ChrisNol", "token2", null);
        comentario1 = new Comentario("juan_perez", "Excelente pelicula");
        comentario2 = new Comentario("sancha_polen", "Mala pelicula. Horrible.");;
    }

    @Test
    void getAll_peliculasSinFiltro() {
        List<Pelicula> peliculaList = Arrays.asList(p1, p2);

        when(peliculasService.getAll()).thenReturn(peliculaList);

        ResponseEntity<List<Pelicula>> response = peliculasController.getAll(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Inception", response.getBody().get(0).getTitulo());

        verify(peliculasService,Mockito.times(1)).getAll();
        verify(peliculasService, Mockito.never()).filter(Mockito.anyString());

    }

    @Test
    void getAll_peliculasConFiltro(){
        String query = "Inter";
        List<Pelicula> peliculasFiltradas = Collections.singletonList(p2);

        when(peliculasService.filter(query)).thenReturn(peliculasFiltradas);

        ResponseEntity<List<Pelicula>> response = peliculasController.getAll(query);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Interstellar", response.getBody().get(0).getTitulo());

        verify(peliculasService, Mockito.times(1)).filter(query);
        verify(peliculasService, Mockito.never()).getAll();

    }

    @Test
    void getAll_ConFiltroVacio_RetornaListaCompleta() {
        String queryVacia = "";
        List<Pelicula> peliculasFicticias = Collections.singletonList(p1);

        when(peliculasService.getAll()).thenReturn(peliculasFicticias);

        ResponseEntity<List<Pelicula>> response = peliculasController.getAll(queryVacia);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        Mockito.verify(peliculasService, Mockito.times(1)).getAll();
        Mockito.verify(peliculasService, Mockito.never()).filter(Mockito.anyString());
    }

    @Test
    void getAll_CuandoServicioFalla_RetornaInternalServerError(){
        when(peliculasService.getAll()).thenThrow(new RuntimeException("Error simulado interno"));

        ResponseEntity<List<Pelicula>> response = peliculasController.getAll(null);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void createPelicula_Exito() {
        when(peliculasService.createPelicula(p1)).thenReturn(p1);

        ResponseEntity<?> response = peliculasController.createPelicula(p1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(p1, response.getBody());
    }

    @Test
    void createPelicula_FalloInterno(){
        when(peliculasService.createPelicula(p1)).thenReturn(null);

        ResponseEntity<?> response = peliculasController.createPelicula(p1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());

        verify(peliculasService, Mockito.times(1)).createPelicula(p1);
    }

    @Test
    void findById_Exito() {
        when(peliculasService.findById("Peli1")).thenReturn(p1);

        ResponseEntity<Pelicula> response = peliculasController.findById("Peli1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(p1.getTitulo(), response.getBody().getTitulo());
    }

    @Test
    void findById_NoEncontrada(){
        when(peliculasService.findById(idInexistente)).thenReturn(null);

        ResponseEntity<Pelicula> response = peliculasController.findById(idInexistente);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

    }

    @Test
    void findById_FalloInterno(){
        when(peliculasService.findById(idError)).thenThrow(new RuntimeException("Error interno simulado"));

        ResponseEntity<Pelicula> response = peliculasController.findById(idError);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());

    }

    @Test
    void getComentarios_ExistePelicula() {
        Comentario[] comentarios = new Comentario[]{comentario1};
        p1.setComentarios(comentarios);

        String idExistente = p1.getId();
        when(peliculasService.findById(idExistente)).thenReturn(p1);

        ResponseEntity<?> response = peliculasController.getComentarios(idExistente);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Comentario[] body = (Comentario[]) response.getBody();
        assertEquals(1, body.length);
        assertEquals("juan_perez", body[0].getUsuario());
        assertEquals("Excelente pelicula", body[0].getComentario());
    }

    @Test
    void getComentarios_NoExistePelicula() {
        when(peliculasService.findById(idInexistente)).thenReturn(null);

        ResponseEntity<?> response = peliculasController.getComentarios(idInexistente);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getComentarios_ErrorInterno() {
        when(peliculasService.findById(idError)).thenThrow(new RuntimeException("Error al leer comentarios"));

        ResponseEntity<?> response = peliculasController.getComentarios(idError);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }
}